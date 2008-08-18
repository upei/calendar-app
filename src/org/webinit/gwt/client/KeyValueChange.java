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

/**
 * Different types of Key/Value change in iterable types.
 * 
 * @author felix
 */
public enum KeyValueChange {
	/**
	 * An item is changed in a Collection.
	 */
	Setting(),
	/**
	 * An item is inserted to a Collection.
	 */
	Insertion(),
	/**
	 * An item is removed from a Collection.
	 */
	Removal(),
	/**
	 * An item is replaced in a Collection.
	 */
	Replacement();
}
