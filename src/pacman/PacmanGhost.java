package il.ac.tau.cs.software1.pacman;

import il.ac.tau.cs.software1.math.*;
import java.util.Random;
import il.ac.tau.cs.software1.core.*;
import il.ac.tau.cs.software1.components.*;

public class PacmanGhost extends PacmanGameObject {
	// TOOD: Implement behavior! In class we'll talk on how to approach this.
	protected ImageComponent regularImageComponent;
	protected ImageComponent scaredImageComponent;
	protected boolean isScared = false;
	private Random random = new Random();
	private java.util.List<PacmanGameObject> allObjects;
	
	public PacmanGhost(PacmanGridComponent initialPosition, java.util.List<PacmanGameObject> allObjects) {
		super(initialPosition);
		this.allObjects = allObjects;
		regularImageComponent = new ImageComponent("ghost");
		scaredImageComponent = new ImageComponent("ghost-scared");
		setScared(false);
		
		/* Q5 */
		EventManager.getInstance().subscribe("potionCollected", this, (eventData) -> {
			System.out.println("Collected potion @ " + eventData.publisher.getTransformComponent().position.toString());
			setScared(!getScared());
		});
	}
	
	@Override
	public void update() {
		Vector2 targetVelocity = new Vector2(0,0);

		if (isScared) {
			// Fleeing behavior: move randomly
			int direction = random.nextInt(4); // 0=right, 1=left, 2=up, 3=down
			switch (direction) {
				case 0: velocityComponent.velocity = new Vector2(1.0f, 0.0f); break; // Right
				case 1: velocityComponent.velocity = new Vector2(-1.0f, 0.0f); break; // Left
				case 2: velocityComponent.velocity = new Vector2(0.0f, 1.0f); break; // Up
				case 3: velocityComponent.velocity = new Vector2(0.0f, -1.0f); break; // Down
			}
			targetVelocity = velocityComponent.velocity;
		} else {
			// Chasing behavior: find player and move towards them
			PacmanPlayer player = null;
			for (PacmanGameObject obj : allObjects) {
				if (obj instanceof PacmanPlayer) {
					player = (PacmanPlayer) obj;
					break;
				}
			}
			
			if (player != null) {
				int dx = player.gridComponent.x - this.gridComponent.x;
				int dy = player.gridComponent.y - this.gridComponent.y;
	
				// Decide whether to move horizontally or vertically
				if (Math.abs(dx) > Math.abs(dy)) {
					// Move horizontally
					targetVelocity = new Vector2(Math.signum(dx), 0.0f);
				} else {
					// Move vertically
					targetVelocity = new Vector2(0.0f, Math.signum(dy));
				}
			}
		}

		// Check for wall collision before moving
		Vector2 adjustedVelocity = new Vector2(
			targetVelocity.x * PacmanConstants.cellWidth,
			targetVelocity.y * PacmanConstants.cellHeight
		);

		if (CollisionDetection.canPerformTick(this, allObjects, adjustedVelocity, (obj) -> obj instanceof PacmanObstacle)) {
			// If the path is clear, apply movement
			gridComponent.x += (int)targetVelocity.x;
			gridComponent.y += (int)targetVelocity.y;
			// Add wrapping logic for ghosts
			if (gridComponent.x < 0) gridComponent.x = PacmanConstants.gridWidth - 1;
			if (gridComponent.x >= PacmanConstants.gridWidth) gridComponent.x = 0;
			snapTransformToGrid();
		}
	}
	
	protected void setScared(boolean scared) {
		isScared = scared;
		imageComponent = isScared ? scaredImageComponent : regularImageComponent;
	}
	
	public boolean getScared() {
		return isScared;
	}
	
}
