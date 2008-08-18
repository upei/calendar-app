package ca.upei.ic.timetable.client;

import com.google.gwt.json.client.JSONValue;

/**
 * An interface for Models
 *  
 * @author felix
 */
public interface Model {
	/**
	 * Load a JSON object into the Model
	 * 
	 * @param value
	 */
	public void loadJSON(JSONValue value);
	
	/**
	 * Clear out the content of the Model
	 */
	public void clear();
}
