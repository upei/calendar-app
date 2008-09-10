package ca.upei.ic.timetable.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class CourseStartTimeModelView implements Model, View {
	
	private FindCourseViewController controller_;
	private ListBox hour_;
	private ListBox day_;
	
	private HorizontalPanel panel_;
	
	public CourseStartTimeModelView(FindCourseViewController controller) {
		controller_ = controller;
		hour_ = GWT.create(ListBox.class);
		
		// initialize the list box
		hour_.addItem("All", "all");
		for (int hour=8; hour < 20; hour++) {
			hour_.addItem(hour + ":00", (hour > 12 ? hour - 12 : hour) + ":00");
			hour_.addItem(hour + ":30", (hour > 12 ? hour - 12 : hour) + ":30");
		}
		
		day_ = GWT.create(ListBox.class);
		
		// initialize the list box
		day_.addItem("All", "all");
		day_.addItem("Monday");
		day_.addItem("Tuesday");
		day_.addItem("Wednesday");
		day_.addItem("Thursday");
		day_.addItem("Friday");
		
		// create the panel
		panel_ = PanelUtils.horizontalPanel(hour_, day_);
		
		controller_.setStartTimeCriteria(getDay(), getHour());
		
		// add change listener
		day_.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				controller_.setStartTimeCriteria(getDay(), getHour());
			}
		});
		
		hour_.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				controller_.setStartTimeCriteria(getDay(), getHour());
			}
		});
		
	}
	
	public String getHour() {
		return hour_.getValue(hour_.getSelectedIndex());
	}
	
	public String getDay() {
		return day_.getValue(day_.getSelectedIndex());
	}

	public void clear() {
		// nop
	}

	public void loadJSON(JSONValue value) {
		// nop
	}

	public void addSubView(View subView) {
		// nop
	}

	public ViewController getController() {
		return controller_;
	}

	public Widget getWidget() {
		return panel_;
	}

	public void hide() {
		panel_.setVisible(false);
	}

	public void show() {
		panel_.setVisible(true);
	}

}
