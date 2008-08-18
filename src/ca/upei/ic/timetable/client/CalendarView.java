package ca.upei.ic.timetable.client;

import com.google.gwt.core.client.GWT;

/**
 * The CalendarView provides the view part of the calendar
 * 
 * @author felix
 *
 */
public class CalendarView implements View {
	
	private Calendar calendar_;
	private CourseCalendarViewController controller_;
	private CalendarPanel panel_;
	
	public CalendarView(CourseCalendarViewController controller, Calendar calendar) {
		calendar_ = calendar;
		controller_ = controller;
		panel_ = GWT.create(CalendarPanel.class);
		panel_.setPixelSize(720, 590);
		panel_.setCalendar(calendar_);
		panel_.init();
	}

	public void addSubView(View subView) {
		// TODO Auto-generated method stub
		throw new RuntimeException("Not implemented.");
	}

	public ViewController getController() {
		return controller_;
	}

	public CalendarPanel getWidget() {
		return panel_;
	}

	public void hide() {
		panel_.setVisible(false);
	}

	public void show() {
		panel_.setVisible(true);
	}

}
