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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class CourseViewController implements ViewController {
	
	private final Service service_;
	private ApplicationController app_;
	private CourseModelView view_;
	private Set<Integer> selectionIndexes_;
	private Map<Integer,JSONObject> selectionValues_;
	
	private Map<String,Boolean> levelCriteria_;
	private Map<String,Boolean> departmentCriteria_;
	private Map<String, Boolean> semesterCriteria_;
	
	public CourseViewController(ApplicationController app) {
		// initialization
		app_ = app;
		service_ = Service.defaultInstance();
		view_ = new CourseModelView(this);

		// selection
		selectionIndexes_ = new TreeSet<Integer>();
		selectionValues_ = new HashMap<Integer,JSONObject>();
		
		// create the criteria hash maps
		levelCriteria_ = new HashMap<String,Boolean>();
		departmentCriteria_ = new HashMap<String,Boolean>();
		semesterCriteria_ = new HashMap<String,Boolean>();
	}
	
	public CourseModelView getView() {
		return view_;
	}
	
	public void setLevelCriteria(String name, boolean included) {
		levelCriteria_.put(name, included);
	}
	
	public void setDepartmentCriteria(String name, boolean included) {
		departmentCriteria_.put(name, included);
	}
	
	public void setSemesterCriteria(String name, boolean included) {
		semesterCriteria_.put(name, included);
	}
	
	public void search() {
		JSONObject department = new JSONObject();
		for (String d: departmentCriteria_.keySet()) {
			if (departmentCriteria_.get(d)) {
				department.put(d, JSONBoolean.getInstance(true));
			}
		}
		JSONObject level = new JSONObject();
		for (String l: levelCriteria_.keySet()) {
			if (levelCriteria_.get(l)) {
				level.put(l, JSONBoolean.getInstance(true));
			}
		}
		JSONObject semester = new JSONObject();
		for (String s: semesterCriteria_.keySet()) {
			if (semesterCriteria_.get(s))
				semester.put(s, JSONBoolean.getInstance(true));
		}
		
		JSONObject value = new JSONObject();
		value.put("department", department);
		value.put("level", level);
		value.put("semester", semester);
		
		Service.defaultInstance().askCourses(value.toString(), new RequestCallback() {

			public void onError(Request request, Throwable exception) {
				app_.error(ApplicationController.OOPS, exception);
			}

			public void onResponseReceived(Request request, Response response) {

				try {
					JSONArray parsedArray = (JSONArray) JSONParser.parse(response.getText());
					// remove the selected courses
					// filtered array
					JSONArray filteredArray = new JSONArray();
					int filtered_index = 0;
					for (int i=0; i<parsedArray.size(); i++) {
						JSONObject o = (JSONObject) parsedArray.get(i);
						int id = (int) ((JSONNumber) o.get("id")).doubleValue();
						if (!isSelected(id)) {
							filteredArray.set(filtered_index++, o);
						}
					}
					// clear out the view so we can add courses
					view_.clear();
					// add the selected values first
					for (int index: selectionIndexes_) {
						view_.addCourse(index, selectionValues_.get(index), true);
					}
					// add the remaining courses
					view_.loadJSON(filteredArray);
				}
				catch (Throwable e) {
					app_.error("Error: " + response.getText(), e);
				}
			}
			
		});
	}
	
	public void select(int index, JSONObject value) {
		selectionIndexes_.add(index);
		selectionValues_.put(index, value);
		app_.getCourseCalendarController().addCourse(index, value);
	}
	
	public void unselect(int index) {
		selectionIndexes_.remove(index);
		selectionValues_.remove(index);
		app_.getCourseCalendarController().removeCourse(index);
	}
	
	public boolean isSelected(int index) {
		return selectionIndexes_.contains(index);
	}
	
	/**
	 * Clear all courses in the view
	 */
	public void clear() {
		// clear all courses in the view
		view_.clear();
		// unselect all courses
		while (selectionIndexes_.iterator().hasNext()) {
			int index = selectionIndexes_.iterator().next();
			unselect(index);
		}
	}
}
