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
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A View for listing course levels
 * 
 * @author felix
 */
public class LevelModelView implements Model, View {
	
	private final VerticalPanel panel_;
	private final FindCourseViewController controller_;
	
	public LevelModelView(FindCourseViewController controller) {
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

	public void loadJSON(JSONValue value) {
		JSONArray array = (JSONArray) value;
		// load the values
		for (int i=0; i<array.size(); i++) {
			// set value and name
			JSONString s = (JSONString) array.get(i);
			CheckBox box = new CheckBox("Level " + s.stringValue());
			box.setName(s.stringValue()); // to store the value
			box.setChecked(true);
			controller_.setLevelCriteria(s.stringValue(), true);

			// add click listener
			box.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					final CheckBox box = (CheckBox) sender;
					final String name = box.getName();
					controller_.setLevelCriteria(name, box.isChecked());
				}
				
			});
			
			panel_.add(box);
		}
	}

	public void addSubView(View subView) {
		throw new RuntimeException("Not Implemeted.");
	}

	public ViewController getController() {
		return controller_;
	}

	public void hide() {
		panel_.setVisible(false);
	}

	public void show() {
		panel_.setVisible(true);
	}

}
