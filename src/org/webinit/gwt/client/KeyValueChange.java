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
