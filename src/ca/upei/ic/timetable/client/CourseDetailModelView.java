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

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The view for course details.
 * 
 * @author felix
 *
 */
public class CourseDetailModelView implements Model, View {
	
	private CourseDetailController controller_;
	private DialogBox dialogBox_;
	
	public CourseDetailModelView(CourseDetailController controller) {
		this.controller_ = controller;
		dialogBox_ = new DialogBox(true, false);
		dialogBox_.setText("Course Details");
	}

	public void addSubView(View subView) {
	}

	public ViewController getController() {
		return controller_;
	}

	public Widget getWidget() {
		return dialogBox_;
	}

	public void hide() {
		dialogBox_.hide();
	}

	public void show() {
		dialogBox_.show();
	}
	public void showAt(int left, int top) {
		dialogBox_.setPopupPosition(left, top);
		dialogBox_.show();
	}

	public void clear() {
		dialogBox_.clear();
	}

	public void loadJSON(JSONValue value) {
		// get all the parameters
		JSONObject object = (JSONObject) value;
		boolean success = ((JSONBoolean) object.get("success")).booleanValue();
		
		if (!success) {
			// do nothing and exit
			throw new RuntimeException("This course id does not exist.");
		}
		
		String department = ((JSONString) object.get("department")).stringValue();
		String name = ((JSONString) object.get("name")).stringValue();
		String title = ((JSONString) object.get("title")).stringValue();
		String location = ((JSONString) object.get("location")).stringValue();
		String time = ((JSONString) object.get("time")).stringValue();
		String semester = ((JSONString) object.get("semester")).stringValue();
		String status = ((JSONString) object.get("status")).stringValue();
		String description = ((JSONString) object.get("description")).stringValue();
		String instructors = ((JSONString) object.get("instructors")).stringValue();
		
		// build the dialog box
		clear(); // clear the dialog box first.
		
		// build the panels
		VerticalPanel panel = PanelUtils.verticalPanel(
				PanelUtils.simplePanel(new HTML("<strong>Name</strong>"), 100, -1), PanelUtils.simplePanel(new HTML(name), 300, -1),
				PanelUtils.simplePanel(new HTML("<strong>Title</strong>"), 100, -1), PanelUtils.simplePanel(new HTML(title), 300, -1),
				PanelUtils.simplePanel(new HTML("<strong>Status</strong>"), 100, -1), PanelUtils.simplePanel(new HTML(status), 300, -1),
				PanelUtils.simplePanel(new HTML("<strong>Department</strong>"), 100, -1), PanelUtils.simplePanel(new HTML(department), 300, -1),
				PanelUtils.simplePanel(new HTML("<strong>Instructors</strong>"), 100, -1), PanelUtils.simplePanel(new HTML(instructors), 300, -1),
				PanelUtils.simplePanel(new HTML("<strong>Semester</strong>"), 100, -1), PanelUtils.simplePanel(new HTML(semester), 300, -1),
				PanelUtils.simplePanel(new HTML("<strong>Location</strong>"), 100, -1), PanelUtils.simplePanel(new HTML(location), 300, -1),
				PanelUtils.simplePanel(new HTML("<strong>Time</strong>"), 100, -1), PanelUtils.simplePanel(new HTML(time), 300, -1),
				PanelUtils.simplePanel(new HTML("<strong>Description</strong>"), 100, -1), PanelUtils.simplePanel(new HTML(description), 300, -1)
				);
		
		dialogBox_.setWidget(panel);
	}

}
