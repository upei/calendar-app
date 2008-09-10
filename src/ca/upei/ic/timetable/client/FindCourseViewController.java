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

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;


/**
 * Find Course controller
 * 
 * Controller for find a course.
 * 
 * @author felix
 *
 */
public class FindCourseViewController implements ViewController {
	
	private ApplicationController app_;
	private FindCourseView view_;
	private final DepartmentModelView department_;
	private final LevelModelView level_;
	private final SemesterModelView semester_;
	private final CourseStartTimeModelView startTime_;
	private boolean departmentLoaded_ = false;
	private boolean levelLoaded_ = false;
	private boolean semesterLoaded_ = false;
	
	/**
	 * Initialize find course dialog
	 */
	public FindCourseViewController(ApplicationController app) {
		this.app_ = app;
		department_ = new DepartmentModelView(this);
		level_ = new LevelModelView(this);
		semester_ = new SemesterModelView(this);
		startTime_ = new CourseStartTimeModelView(this);
	}
	
	/**
	 * Get the department model
	 * 
	 * @return the department model
	 */
	public DepartmentModelView getDepartmentModel() {
		if (!departmentLoaded_) {
			Service.defaultInstance().askDepartments(new RequestCallback(){

				public void onError(Request request, Throwable exception) {
					app_.error(ApplicationController.OOPS, exception);
				}

				public void onResponseReceived(Request request,
						Response response) {
					JSONValue value = JSONParser.parse(response.getText());
					department_.loadJSON(value);
					departmentLoaded_ = true;
				}
				
			});
		}
		
		return department_;
	}
	
	/**
	 * Get the level model
	 * 
	 * @return the level model
	 */
	public LevelModelView getLevelModel() {
		if (!levelLoaded_) {
			Service.defaultInstance().askCourseLevels(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					app_.error(ApplicationController.OOPS, exception);
				}

				public void onResponseReceived(Request request,
						Response response) {
					JSONValue value = JSONParser.parse(response.getText());
					level_.loadJSON(value);
					levelLoaded_ = true;
				}
				
			});
		}
		
		return level_;
	}
	
	public SemesterModelView getSemesterModel() {
		if (!semesterLoaded_) {
			Service.defaultInstance().askSemesters(new RequestCallback() {
				
				public void onError(Request request, Throwable exception) {
					app_.error(ApplicationController.OOPS, exception);
				}
				
				public void onResponseReceived(Request request, Response response) {
					try {
						String text = response.getText();
						JSONValue value = JSONParser.parse(text);
						semester_.loadJSON(value);
						semesterLoaded_ = true;
					}
					catch (Exception e) {
						app_.error("Error: " + response.getText(), e);
					}
				}
				
			});
		}
		
		return semester_;
	}
	
	public CourseStartTimeModelView getStartTimeModel() {
		return startTime_;
	}
	
	public void setSemesterCriteria(String name, boolean included) {
		app_.getCourseController().setSemesterCriteria(name, included);
	}
	
	public void setLevelCriteria(String name, boolean included) {
		app_.getCourseController().setLevelCriteria(name, included);
	}
	
	public void setDepartmentCriteria(String name, boolean included) {
		app_.getCourseController().setDepartmentCriteria(name, included);
	}
	
	public void setStartTimeCriteria(String day, String startTime) {
		app_.getCourseController().setCourseStartTimeCriteria(day, startTime);
	}
	
	public void search() {
		app_.getCourseController().search();
	}
	
	public FindCourseView getView() {
		if (null == view_) {
			view_ = new FindCourseView(this);
		}

		return view_;
	}
}
