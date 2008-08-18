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
