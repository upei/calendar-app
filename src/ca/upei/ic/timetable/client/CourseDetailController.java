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
 * The controller to show the course details
 * 
 * @author felix
 */
public class CourseDetailController implements ViewController {

	private ApplicationController app_;
	private CourseDetailModelView view_ = null;
	
	public CourseDetailController(ApplicationController controller) {
		app_ = controller;
	}
	
	public CourseDetailModelView getView() {
		if (view_ == null)
			view_ = new CourseDetailModelView(this);
		return view_;
	}
	
	/**
	 * Show the details of a course in a dialog box
	 * 
	 * @param courseId
	 */
	public void showCourseDetail(final int courseId) {
		
		Service.defaultInstance().cancelCourseDetailRequest();
		
		Service.defaultInstance().askCourseDetail(courseId, new RequestCallback() {

			public void onError(Request request, Throwable exception) {
				app_.error("Cannot get course details for course " + courseId, exception);
			}

			public void onResponseReceived(Request request, Response response) {
				try {
					String result = response.getText();
					JSONValue value = JSONParser.parse(result);
					try {
						getView().loadJSON(value);
						getView().showAt(265, 28);
					} catch (Exception ex) {
						// do nothing
					}
				}
				catch (Exception ex) {
					app_.error("Cannot get course details for course " + courseId, ex);
				}
			}
			
		});
	}
	
	public void hideCourseDetail() {
		getView().hide();
	}

}
