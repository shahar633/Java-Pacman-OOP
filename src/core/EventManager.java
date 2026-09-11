package il.ac.tau.cs.software1.core;

import java.util.*;

public class EventManager {
	Map<String, List<EventSubscription>> subscribers = new HashMap<>();
	
	public void subscribe(String event, GameObject subscriber, IEventCallback callback) {
		/* Q2 */
		EventSubscription es = new EventSubscription(subscriber, callback);
		if (!subscribers.containsKey(event)) {
			subscribers.put(event, new ArrayList<>());			
		}
		subscribers.get(event).add(es);
	}
	
	public void notifyEvent(String event, GameObject publisher, Object data) {
		/* Q3 */
		if (!subscribers.containsKey(event)) {
			return;			
		}
		List<EventSubscription> lst = subscribers.get(event);
		EventData eventData = new EventData(publisher, data);
		for (EventSubscription es : lst){
			es.callback.call(eventData);
		}
	}
	
	// --------------------------------------- (Singleton)
	/* Q1 */
	private static final EventManager instance = new EventManager();

	public static EventManager getInstance() {
		return instance;
	}

	private EventManager() {
	}
}
