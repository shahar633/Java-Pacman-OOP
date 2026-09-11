package il.ac.tau.cs.software1.pacman;

import il.ac.tau.cs.software1.core.*;
import il.ac.tau.cs.software1.components.*;

public class PacmanBonusPotion extends PacmanBonus {
	public PacmanBonusPotion(PacmanGridComponent initialPosition) {
		super(initialPosition);
		this.imageComponent = new ImageComponent("bonus-potion");
		this.bonusScore = 500;
	}
	
	@Override
	public void onHit(GameObject other) {
		/* Q4 */
		super.onHit(other); // First, call the base class method to add score
		if (other instanceof PacmanPlayer) {
			EventManager.getInstance().notifyEvent("potionCollected", this, null);
		}
	}
}
