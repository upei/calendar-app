package org.webinit.gwt.client;

/**
 * Any class that acts as an Observer needs to implement this interface.
 *  
 * @author felix
 */
public interface Observer {
	/**
	 * This method is triggered when an event takes place.
	 * 
	 * @param event
	 * @param object
	 * @param arg
	 * @return
	 */
	public boolean observe(String event, Object object, Object arg);
}
