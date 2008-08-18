package ca.upei.ic.timetable.client;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * The controller of a View
 * 
 * @author felix
 *
 */
public class CourseCalendarViewController implements ViewController {

	private CalendarView view_;
	private Calendar calendar_;
	private ApplicationController app_;
	private Map<Integer, CalendarItem> items_;
	
	public CourseCalendarViewController(ApplicationController controller) {
		app_ = controller;
		calendar_ = GWT.create(Calendar.class);
		calendar_.setType(Calendar.FIVE);
		
		view_ = new CalendarView(this, calendar_);
		items_ = new HashMap<Integer, CalendarItem>();
	}
	
	public View getView() {
		return view_;
	}
	
	/**
	 * Add a course to the Calendar
	 * 
	 * @param id
	 * @param course
	 */
	public void addCourse(int id, JSONObject course) {
		// clear the message first
		String name = ((JSONString) course.get("name")).stringValue();
		String title = ((JSONString) course.get("title")).stringValue();
		String date = ((JSONString) course.get("time")).stringValue();

		CalendarItem item = new CalendarItem(name);
		item.setContent(title);
		
		// analyze the date and add the item.
		// filtered out all invalid characters
		String filteredDate = date.replaceAll("[^MTWHF0-9:-]", "");
		Queue<Integer> day = new LinkedList<Integer>();
		
		// begin analysis
		for (int i=0; i<filteredDate.length(); i++) {
			switch (filteredDate.charAt(i)) {
			// weekday
			case 'M':
				day.offer(Calendar.MONDAY);
				break;
			case 'W':
				day.offer(Calendar.WEDNESDAY);
				break;
			case 'F':
				day.offer(Calendar.FRIDAY);
				break;
			case 'T':
				if (i+1 < filteredDate.length() && filteredDate.charAt(i+1) == 'H') {
					day.offer(Calendar.THURSDAY);
					i++;
				}
				else {
					day.offer(Calendar.TUESDAY);
				}
				break;
			// time
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case ':':
			case '-':
				// work until the end of time
				int j = i+1;
				while (j < filteredDate.length() && (Character.isDigit(filteredDate.charAt(j)) || filteredDate.charAt(j) == ':' ||
						filteredDate.charAt(j) == '-')) {
					j++;
				}
				// grab the time
				String time = filteredDate.substring(i, j);
				// check the time format
				if (!time.matches("^\\d{1,2}:\\d{2,2}-\\d{1,2}:\\d{2,2}")) {
					i = j;
					break;
				}
				
				// get the time
				String[] two = time.split("-");
				// get the begin and end
				int begin = Integer.parseInt(two[0].replaceAll(":", ""));
				int end = Integer.parseInt(two[1].replaceAll(":", ""));
				
				// offer the calendar
				try {
					while (day.peek() != null) {
						int weekday = day.poll();
						item.addTimeInterval(weekday, begin, end);
					}
				}
				catch (TimetableException e) {
					app_.error(ApplicationController.OOPS, e);
				}

				// done
				i = j;
				break;
			}
		}
		
		// add the item
		items_.put(id, item);
		// add to calendar
		calendar_.addItem(item);
	}
	
	/**
	 * Remove a course from the Calendar
	 * 
	 * @param id
	 * @return
	 */
	public synchronized boolean removeCourse(int id) {
		if (items_.containsKey(id)) {
			calendar_.removeItem(items_.get(id));
			items_.remove(id);
			return true;
		}
		else {
			return false;
		}
	}

}
