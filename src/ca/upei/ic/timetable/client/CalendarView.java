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
