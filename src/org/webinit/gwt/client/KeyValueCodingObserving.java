/*
 *  Copyright 2008 University of Prince Edward Island
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
