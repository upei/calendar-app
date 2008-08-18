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

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The View for listing courses
 * 
 * @author felix
 *
 */
public class CourseModelView implements Model, View {

	private CourseViewController controller_;
	private VerticalPanel panel_;
	
	/**
	 * 
	 * @param controller
	 */
	public CourseModelView(CourseViewController controller) {
		controller_ = controller;
		panel_ = new VerticalPanel();
		panel_.setSpacing(3);
	}
	
	public void clear() {
		panel_.clear();
	}

	public Widget getWidget() {
		return panel_;
	}
	
	public void addCourse(final int id, final JSONObject value, boolean selected) {
		CheckBox box = new CheckBox();
		String name = ((JSONString) value.get("name")).stringValue();
		String title = ((JSONString) value.get("title")).stringValue();
		String time = ((JSONString) value.get("time")).stringValue();
		box.setText(name + " " + title);
		box.setName(Integer.toString(id));
		box.setChecked(selected);
		box.setEnabled(time.matches(".*\\d{1,2}:\\d{2,2}\\s*-\\s*\\d{1,2}:\\d{2,2}.*"));
		
		// add click listener
		box.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				final CheckBox box = (CheckBox) sender;
				if (box.isChecked()) {
					controller_.select(id, value);
				}
				else {
					controller_.unselect(id);
				}
			}
			
		});
		
		panel_.add(box);
	}

	public void loadJSON(JSONValue value) {
		JSONArray array = (JSONArray) value;
		// load the values
		for (int i=0; i<array.size(); i++) {
			// set value and name
			JSONObject o = (JSONObject) array.get(i);
			int id = (int) ((JSONNumber) o.get("id")).doubleValue();
			addCourse(id, o, false);
		}
	}

	public void addSubView(View subView) {
		throw new RuntimeException("Not Implemeted.");
	}

	public CourseViewController getController() {
		return controller_;
	}

	public void hide() {
		panel_.setVisible(false);
	}

	public void show() {
		panel_.setVisible(true);
	}

}
