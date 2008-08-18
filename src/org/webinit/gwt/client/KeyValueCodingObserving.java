package org.webinit.gwt.client;

import java.util.Map;
import java.util.Set;

/**
 * The interface for managed Key/Value coding/observing classes.
 * 
 * @author felix
 */
public interface KeyValueCodingObserving extends NeedProxy {
	/**
	 * Get a value.
	 * 
	 * @param key
	 * @return
	 */
	public Object valueForKey(String key);
	/**
	 * Called by {@link #valueForKey(String)} to get the ``undefined'' value
	 * for this key.
	 * 
	 * @param key
	 * @return
	 */
	public Object valueForUndefinedKey(String key);
	
	/**
	 * Set the value.
	 * 
	 * @param key
	 * @param value
	 */
	public void setValueForKey(String key, Object value);
	/**
	 * Set the value for the key if it is undefined.
	 * 
	 * @param key
	 * @param value
	 */
	public void setValueForUndefinedKey(String key, Object value);
	
	/**
	 * Validate a value for this key.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean validateValueForKey(String key, Object value);
	
	// register for observation
	/**
	 * Observe a key with {@link KeyValueObservingOption}
	 */
	public void addObserver(KeyValueCodingObserving object, String key, Set<KeyValueObservingOption> options, Object context);
	/**
	 * Remove an observer.
	 * 
	 * @param object
	 * @param key
	 * @return
	 */
	public boolean removeObserver(KeyValueCodingObserving object, String key);
	// notifying observers of changes
	/**
	 * Called before a key-ed value is changed.
	 */
	public void willChangeValueForKey(String key);
	/**
	 * Called after a value is changed.
	 * @param key
	 */
	public void didChangeValueForKey(String key);
	// change notification
	/**
	 * Called before {@link #willChangeValueForKey(String)}, if {@link KeyValueObservingOption.Prior} is set.
	 * And called after {@link #didChangeValueForKey(String)}.
	 */
	public void observeValueForKey(String key, KeyValueCodingObserving object, Map<KeyValueChangeKey, Object> change, Object context);
}
