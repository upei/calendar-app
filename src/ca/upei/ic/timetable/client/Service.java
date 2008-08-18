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

import com.google.gwt.http.client.RequestCallback;

/**
 * Provides services for controllers to get remote data
 * 
 * @author felix
 */
public class Service {

	private static Service instance_ = null;
	private static String DEFAULT_URL = getDefaultServiceURL();
	
	public static synchronized Service defaultInstance() {
		if (null == instance_) {
			instance_ = new Service(DEFAULT_URL);
		}
		
		return instance_;
	}
	
	private String url_;
	private Remote remote_;
	
	public Service(String url) {
		url_ = url;
		remote_ = new Remote(url_);
	}
	
	public void askCourseLevels(RequestCallback callback) {
		remote_.get("get_course_levels", null, callback);
	}
	
	public void askDepartments(RequestCallback callback) {
		remote_.get("get_departments", null, callback);
	}
	
	public void askCourses(String params, RequestCallback callback) {
		remote_.post("get_courses_of_departments_and_levels", "application/json", params, callback);
	}
	
	public void askSemesters(RequestCallback callback) {
		remote_.get("get_semesters", null, callback);
	}
	
	public static native String getDefaultServiceURL() /*-{
		return $wnd.__wi_CourseSelector_Service_URL;
	}-*/;
	
}
