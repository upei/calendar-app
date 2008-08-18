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
