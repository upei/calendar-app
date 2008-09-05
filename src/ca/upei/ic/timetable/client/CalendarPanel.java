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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.webinit.gwt.client.Observable;
import org.webinit.gwt.client.Observer;

import ca.upei.ic.timetable.client.CalendarItem.Day;
import ca.upei.ic.timetable.client.CalendarItem.TimeInterval;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GWT Calendar Widget
 * 
 * @author felix
 */
public abstract class CalendarPanel extends Composite implements Observer,
		Observable {

	private Calendar calendar_;
	private ScrollPanel outerPanel_;
	private HorizontalPanel panel_;
	private Map<CalendarItem, Set<Widget>> itemWidgets_;
	private int courseWidth_;
	private int calendarInnerHeight_;
	private int calendarInnerWidth_;
	private int calendarLeftDescriptionWidth_ = 60;
	private int width_, height_;

	public CalendarPanel() {
	}

	/**
	 * Set the Calendar model
	 * 
	 * @param calendar
	 */
	public void setCalendar(Calendar calendar) {
		calendar_ = calendar;
	}

	/**
	 * Set the pixel size
	 */
	@Override
	public void setPixelSize(int width, int height) {
		this.width_ = width;
		this.height_ = height;
	}

	/**
	 * This method must be called after all settings done.
	 */
	void init() {

		calendarInnerHeight_ = Calendar.RESOLUTION * 14 * 4;
		calendarInnerWidth_ = width_ - 17;

		// create the horizontalPanel
		panel_ = GWT.create(HorizontalPanel.class);

		// add panels
		for (int i = 0; i < 7; i++) {
			panel_.add((Widget) GWT.create(AbsolutePanel.class));
		}

		// create the scroll panel
		outerPanel_ = GWT.create(ScrollPanel.class);

		// create the absolute wrapper
		AbsolutePanel wrapper = GWT.create(AbsolutePanel.class);
		wrapper.setPixelSize(calendarInnerWidth_, calendarInnerHeight_);

		// set the calendar width and height
		panel_.setPixelSize(
				calendarInnerWidth_ - calendarLeftDescriptionWidth_,
				calendarInnerHeight_);

		// add the sub panel and set size
		outerPanel_.add(wrapper);
		outerPanel_.setPixelSize(width_, height_);

		// deal with type
		switch (calendar_.getType()) {
		case Calendar.FIVE:
			panel_.getWidget(0).setSize("0px", "0px");

			courseWidth_ = (calendarInnerWidth_ - calendarLeftDescriptionWidth_) / 5; // minus
																						// the
																						// scroll
																						// bar
																						// width
			for (int i = 1; i < 6; i++) {
				panel_.getWidget(i).setPixelSize(courseWidth_,
						calendarInnerHeight_);
			}
			panel_.getWidget(6).setSize("0px", "0px");

			break;
		case Calendar.SEVEN:
			courseWidth_ = (calendarInnerWidth_ - calendarLeftDescriptionWidth_) / 7;
			for (int i = 0; i < 7; i++) {
				panel_.getWidget(i).setPixelSize(courseWidth_,
						calendarInnerHeight_);
			}
			break;
		default:
			throw new IllegalArgumentException("Calendar type is invalid.");
		}

		// add to observer list
		calendar_.addObserver("itemDidAdd", this);
		calendar_.addObserver("itemDidRemove", this);

		// create the grid
		HorizontalPanel grid = new HorizontalPanel();
		grid.setPixelSize(calendarInnerWidth_ - calendarLeftDescriptionWidth_,
				calendarInnerHeight_);
		grid.addStyleName("grid");

		// FIXME tricky part, needs to change
		for (int i = 0; i < 5; i++) {
			VerticalPanel gridColumn = GWT.create(VerticalPanel.class);
			gridColumn.addStyleName("gridColumn");
			gridColumn.setHeight(Integer.toString(calendarInnerHeight_) + "px");
			gridColumn.setWidth(Integer.toString(courseWidth_ - 1) + "px");

			for (int j = 0; j < 14 * 60 / Calendar.RESOLUTION / 2; j++) {
				SimplePanel cell = GWT.create(SimplePanel.class);
				cell.addStyleName("gridCell");
				cell.setHeight(Integer.toString(Calendar.RESOLUTION * 2 - 1)
						+ "px");
				cell.setWidth(Integer.toString(courseWidth_ - 1) + "px");
				gridColumn.add(cell);
			}
			grid.add(gridColumn);
		}

		// create the with description panel
		HorizontalPanel panelWithDescription = GWT
				.create(HorizontalPanel.class);
		VerticalPanel leftDescription = GWT.create(VerticalPanel.class);
		leftDescription.setPixelSize(calendarLeftDescriptionWidth_,
				calendarInnerHeight_);
		leftDescription.addStyleName("timeColumn");
		// first row
		SimplePanel firstTimeCell = GWT.create(SimplePanel.class);
		firstTimeCell.addStyleName("timeCell");
		firstTimeCell.setPixelSize(calendarLeftDescriptionWidth_ - 1,
				Calendar.RESOLUTION);
		leftDescription.add(firstTimeCell);

		for (int i = 0; i < 14 * 60 / Calendar.RESOLUTION/2; i++) {
			SimplePanel cell = GWT.create(SimplePanel.class);
			cell.addStyleName("timeCell");
			cell.setPixelSize(calendarLeftDescriptionWidth_ - 1,
					Calendar.RESOLUTION*2);

			String half;
			
			if (i % 2 == 0) {
				half = "30";
			}
			else {
				half = "00";
			}

			
			// half time adjustment
			int tp = i / 2 + 8;
			tp = tp + i % 2;

			String ampm = "am";
			
			if (tp >= 12) {
				ampm = "pm";
			}
			if (tp > 12) {
				tp -= 12;
			}
			
			cell.add(new HTML(Integer.toString(tp) + ":" + half + ampm + "&nbsp;"));

			leftDescription.add(cell);
		}

		panelWithDescription.add(leftDescription);
		panelWithDescription.add(panel_);

		// add the elements
		wrapper.add(grid, calendarLeftDescriptionWidth_, 0);
		wrapper.add(panelWithDescription, 0, 0);

		initWidget(outerPanel_);

		// set the style primary name
		outerPanel_.setStylePrimaryName("wi-CalendarPanel");
		// create the map
		itemWidgets_ = new HashMap<CalendarItem, Set<Widget>>();
	}

	/**
	 * The Event Handler
	 */
	public boolean observe(String event, Object object, Object arg) {
		if (event.equals("itemShouldAdd")) {
			return itemShouldAdd((Calendar) object, (CalendarItem) arg);
		} else if (event.equals("itemDidAdd")) {
			itemDidAdd((Calendar) object, (CalendarItem) arg);
		} else if (event.equals("itemShouldRemove")) {
			return itemShouldRemove((Calendar) object, (CalendarItem) arg);
		} else if (event.equals("itemDidRemove")) {
			itemDidRemove((Calendar) object, (CalendarItem) arg);
		}
		return false;
	}

	protected boolean itemShouldAdd(Calendar calendar, CalendarItem item) {
		if (itemWidgets_.containsKey(item))
			return false;
		else
			return true;
	}

	protected boolean itemShouldRemove(Calendar calendar, CalendarItem item) {
		if (itemWidgets_.containsKey(item))
			return true;
		else
			return false;
	}

	protected void reorganizePanel(AbsolutePanel panel) {
		Set<Widget> remaining = new HashSet<Widget>();
		Set<Widget> processing = new HashSet<Widget>();

		// add all widgets to remaining
		for (Widget w : panel) {
			remaining.add(w);
		}

		// loop until no remaining widgets
		while (remaining.size() > 0) {
			// get the first widget
			Widget first = remaining.iterator().next();

			int top = panel.getWidgetTop(first);
			int bottom = panel.getWidgetTop(first) + first.getOffsetHeight();

			remaining.remove(first);
			processing.add(first);

			// find out the overlapping widgets from the first one
			boolean continueProcessing = true;
			while (continueProcessing) {
				// default to terminate the processing
				// this is a hack because no goto statement is supported in
				// Java.
				boolean terminateProcessing = true;
				// iterate all widgets until one overlapping widget is found.
				for (Widget widget : remaining) {
					int widgetTop = panel.getWidgetTop(widget);
					int widgetBottom = panel.getWidgetTop(widget)
							+ widget.getOffsetHeight();
					// found
					if (top < widgetBottom && widgetTop < bottom) {
						// add to processing set, remove from remaining set
						processing.add(widget);
						remaining.remove(widget);
						// reset top and bottom if necessary
						if (top > widgetTop)
							top = widgetTop;
						if (bottom < widgetBottom)
							bottom = widgetBottom;
						// don't terminate processing
						terminateProcessing = false;
						break;
					}
				}
				// really want to terminate the process?
				if (terminateProcessing)
					continueProcessing = false;
			}

			// all processing widgets found, try to reposition/resize the
			// widgets
			int count = processing.size();
			int index = 0;
			for (Widget widget : processing) {
				int widgetWidth = (panel.getOffsetWidth() - 2) / count;
				int widgetTop = panel.getWidgetTop(widget);
				int widgetLeft = index * widgetWidth;
				widget.setWidth(Integer.toString(widgetWidth) + "px");
				panel.setWidgetPosition(widget, widgetLeft, widgetTop);
				index++;
			}

			// clear the processing set
			processing.clear();
		}
	}

	protected void itemDidAdd(Calendar calendar, CalendarItem item) {
		// create the widget pool
		if (!itemWidgets_.containsKey(item)) {
			itemWidgets_.put(item, new HashSet<Widget>());
		}

		// iterate all entries in the item
		for (Map.Entry<Integer, Day> entry : item.getDays().entrySet()) {
			int day = entry.getKey();
			// get the parent panel
			AbsolutePanel parent = (AbsolutePanel) panel_.getWidget(day);

			// build the widgets
			Set<TimeInterval> intervals = entry.getValue().getTimeIntervals();
			// iterate all time intervals of a day.
			for (TimeInterval ti : intervals) {
				// create the widget
				SimplePanel widget = GWT.create(SimplePanel.class);

				HTML html = new HTML("<h3>" + item.getTitle() + "</h3><p>"
						+ item.getContent() + "</p>");
				widget.setWidget(html);

				// set the style
				widget.setStylePrimaryName("wi-CalendarPanel");
				widget.addStyleDependentName("selectedCourse");

				int begin = 0;
				if (ti.begin() < 480 / Calendar.RESOLUTION) { // if it's an
																// afternoon
																// course
					begin = ti.begin() + 240 / Calendar.RESOLUTION;
				} else { // no it's a morning course.
					begin = ti.begin() - 480 / Calendar.RESOLUTION;
				}

				int end;
				if ((ti.end() <= 540 / Calendar.RESOLUTION)
						|| (begin >= 600 / Calendar.RESOLUTION)) { // it it ends
																	// before
																	// 930 or
																	// begin at
																	// 600pm
					end = ti.end() + 240 / Calendar.RESOLUTION;
				} else {
					end = ti.end() - 480 / Calendar.RESOLUTION;
				}
				// set the size
				int height = (end - begin) * Calendar.RESOLUTION;
				widget.setPixelSize(courseWidth_ - 2, height - 2); // minus the
																	// border
																	// width

				parent.add(widget, 0, begin * Calendar.RESOLUTION);

				// add to widget pool
				itemWidgets_.get(item).add(widget);
			}
		}

		// reorganize all panels
		for (int i = 0; i < panel_.getWidgetCount(); i++) {
			AbsolutePanel panel = (AbsolutePanel) panel_.getWidget(i);
			reorganizePanel(panel);
		}
	}

	protected void itemDidRemove(Calendar calendar, CalendarItem item) {
		for (Widget w : itemWidgets_.get(item)) {
			w.removeFromParent();
		}
		itemWidgets_.remove(item);
		// reorganize all panels
		for (int i = 0; i < panel_.getWidgetCount(); i++) {
			AbsolutePanel panel = (AbsolutePanel) panel_.getWidget(i);
			reorganizePanel(panel);
		}
	}

	/**
	 * Get Inner height of the Calendar
	 * 
	 * @return
	 */
	public int getRealHeight() {
		return calendarInnerHeight_;
	}
}
