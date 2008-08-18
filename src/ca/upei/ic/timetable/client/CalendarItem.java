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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An item in the Calendar.
 * 
 * This class represents an event that happens on one day or happens across
 * several days (such as a course that can take place on several different
 * days).
 * 
 * @author felix
 *
 */
public class CalendarItem  {
	
	/** a class to represent time interval in one day for an CalendarItem */
	public static class TimeInterval {
		private int begin_, end_;
		private int resolution_;
		
		public TimeInterval(int start, int end) {
			this(start, end, Calendar.RESOLUTION);
		}
		
		private TimeInterval(int start, int end, int resolution) {
			resolution_ = resolution;
			int beginHour = start / 100;
			int beginMin = start % 100;
			int endHour = end / 100;
			int endMin = end % 100;
			begin_ = (int) Math.round((beginHour * 60.0 + beginMin) / resolution_);
			end_ = (int) Math.round((endHour * 60.0 + endMin) / resolution_);
		}
		
		public int begin() {
			return begin_;
		}
		
		public int end() {
			return end_;
		}
	}
	
	/** a class to represent a day for an CalendarItem */
	public static class Day {
		private Set<TimeInterval> intervals_;
		
		public Day() {
			intervals_ = new HashSet<TimeInterval>(); 
		}
		
		public Day(TimeInterval interval) throws TimetableException {
			this();
			addTimeInterval(interval);
		}
		
		public void addTimeInterval(TimeInterval interval) throws TimetableException {
			// check if overlapping. no overlapping within an item. so if it's overlapped, throw an exception
			if (isOverlapped(interval))
				throw new TimetableException("Interval overlapped in one single item.");
			
			intervals_.add(interval);
		}
		
		public boolean isOverlapped(TimeInterval interval) {
			for (TimeInterval ti: intervals_) {
				if (ti.begin() > interval.end() || interval.begin() > ti.end())
					return true;
			}
			return false;
		}
		
		public Set<TimeInterval> getTimeIntervals() {
			return Collections.unmodifiableSet(intervals_);
		}
	}
	
	private String title_;
	private String content_;
	private Map<Integer,Day> days_;
	
	/**
	 * Create an CalendarItem
	 */
	public CalendarItem() {
		days_ = new HashMap<Integer,Day>();
	}
	
	/**
	 * Create an CalendarItem with the title
	 * 
	 * @param title
	 */
	public CalendarItem(String title) {
		this();
		title_ = title;
		content_ = "";
	}
	
	/**
	 * Accessor for title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		title_ = title;
	}
	
	public String getTitle() {
		return title_;
	}
	
	/**
	 * Accessor for content
	 * @param content
	 */
	public void setContent(String content) {
		content_ = content;
	}
	
	public String getContent() {
		return content_;
	}
	
	/**
	 * Accessor for days
	 * @return
	 */
	public Map<Integer,Day> getDays() {
		return Collections.unmodifiableMap(days_);
	}

	/**
	 * Check whether the item happens on a day
	 *  
	 * @param day
	 * @return
	 */
	public boolean hasDay(int day) {
		return days_.containsKey(day);
	}
	
	/**
	 * Add an time period to the item.
	 * 
	 * @param day
	 * @param start
	 * @param end
	 * @throws TimetableException
	 */
	public void addTimeInterval(int day, int start, int end) throws TimetableException {
		// get the day object first, if it does not exist, create one.
		Day theDay;
		if (days_.containsKey(day))
			theDay = days_.get(day);
		else {
			theDay = new Day();
			days_.put(day, theDay);
		}
		
		// create the time interval
		TimeInterval interval = new TimeInterval(start, end);
		// add the interval. exception may be thrown here.
		theDay.addTimeInterval(interval);
	}
	
}
