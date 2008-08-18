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
	
	public void setSemesterCriteria(String name, boolean included) {
		app_.getCourseController().setSemesterCriteria(name, included);
	}
	
	public void setLevelCriteria(String name, boolean included) {
		app_.getCourseController().setLevelCriteria(name, included);
	}
	
	public void setDepartmentCriteria(String name, boolean included) {
		app_.getCourseController().setDepartmentCriteria(name, included);
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
