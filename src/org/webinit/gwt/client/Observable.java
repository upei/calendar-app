/*
 *  Copyright 2008 Yuxing Huang <felix@webinit.org>
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

import java.util.List;
import java.util.Set;

/**
 * Interface for managed Observable classes
 * 
 * All classes that implements this interface need not implements the methods
 * in this interface. But if a class want to override the managed implementation,
 * it must annotate @OverrideProxy before the implemented methods to emphasize
 * its implementation.
 * 
 * @author felix
 */
public interface Observable extends NeedProxy {
	/**
	 * Get all observed events from the Observable object.
	 * 
	 * @return
	 */
	public Set<String> getObservedEvents();
	/**
	 * Get a list of observers for the event.
	 * 
	 * @param event
	 * @return
	 */
	public List<Observer> getObserversOfEvent(String event);
	/**
	 * Whether this observer has observed the event.
	 * 
	 * @param event
	 * @param observer
	 * @return
	 */
	public boolean hasObserver(String event, Observer observer);
	/**
	 * Add an observer for an event.
	 * 
	 * @param event
	 * @param observer
	 */
	public void addObserver(String event, Observer observer);
	/**
	 * Add an observer for an event, if this observer has not observed this event.
	 * 
	 * @param event
	 * @param observer
	 */
	public void addUniqueObserver(String event, Observer observer);
	/**
	 * Remove an observer from the event.
	 * 
	 * @param event
	 * @param observer
	 * @return
	 */
	public boolean removeObserver(String event, Observer observer);
	/**
	 * Trigger an event.
	 * 
	 * @param event The event name.
	 * @param object The sender.
	 * @param arg Any context information.
	 * @return true if all observers returns true.
	 * @see Observer
	 */
	public boolean trigger(String event, Object object, Object arg);
}
