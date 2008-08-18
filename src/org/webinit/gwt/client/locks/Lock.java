package org.webinit.gwt.client.locks;

/**
 * The Simple Lock interface for GWT clients
 * 
 * @author felix
 */
public interface Lock {
	/**
	 * Lock
	 * 
	 * @param sender the sender of this lock
	 * @param message the message to issue this lock
	 */
	public void lock(Object owner, Object message);
	
	/**
	 * Unlock it.
	 * 
	 * @param sender the owner of this lock
	 * @param message the message to issue the unlock
	 */
	public void unlock(Object owner, Object message) throws IllegalStateException;
}
