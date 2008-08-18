package org.webinit.gwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Observable_WIProxyImpl implements Observable {

	private Map<String, List<Observer>> observers_ = null;
	
	private Map<String, List<Observer>> getObservers() {
		if (null == observers_) {
			observers_ = new HashMap<String, List<Observer>>();
		}
		
		return observers_;
	}
	
	/**
	 * Get the observed events.
	 *
	 * @return
	 */
	public Set<String> getObservedEvents() {
		return getObservers().keySet();
	}
	
	/**
	 * Get the observers of an event.
	 * 
	 * @param event
	 * @return
	 */
	public List<Observer> getObserversOfEvent(String event) {
		
		if (getObservers().containsKey(event)) {
			return getObservers().get(event);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Add an observer to an event.
	 * 
	 * @param event
	 * @param observer
	 */
	public void addObserver(String event, Observer observer) {
		List<Observer> eventObservers;
		// get or create the observer list of the event
		if (getObservers().containsKey(event)) {
			eventObservers = getObservers().get(event);
		}
		else {
			eventObservers = new ArrayList<Observer>();
			getObservers().put(event, eventObservers);
		}
		
		eventObservers.add(observer);
	}
	
	public boolean hasObserver(String event, Observer observer) {
		if (!getObservers().containsKey(event))
			return false;
		if (!getObserversOfEvent(event).contains(observer))
			return false;
		
		return true;
	}
	
	public void addUniqueObserver(String event, Observer observer) {
		if (!hasObserver(event, observer))
			addObserver(event, observer);
	}
	
	/**
	 * Remove an observer from an event.
	 * 
	 * @param event
	 * @param observer
	 * @return
	 */
	public boolean removeObserver(String event, Observer observer) {
		List<Observer> eventObservers;
		if (!getObservers().containsKey(event)) {
			return false;
		}
		
		eventObservers = getObservers().get(event);
		return eventObservers.remove(observer);
	}
	
	/**
	 * Trigger an event.
	 * 
	 * @param event
	 * @param object
	 * @param arg
	 * @return
	 */
	public boolean trigger(String event, Object object, Object arg) {
		List<Observer> eventObservers;
		// no event? do nothing, return true
		if (!getObservers().containsKey(event))
			return true;
		
		eventObservers = getObservers().get(event);
		boolean result = true;
		
		for (Observer ob: eventObservers) {
			result &= ob.observe(event, object, arg);
		}
		
		return result;
	}

}
