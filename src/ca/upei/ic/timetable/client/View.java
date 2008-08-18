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

import com.google.gwt.user.client.ui.Widget;

/**
 * A basic View interface
 * 
 * @author felix
 */
public interface View {
	/**
	 * Show the View on the web page
	 */
	public void show();
	/**
	 * Hide the View from the web page
	 */
	public void hide();
	/**
	 * Get the View Controller for this View
	 * @return
	 */
	public ViewController getController();
	/**
	 * Add a sub-view to this View
	 * 
	 * This method is OPTIONAL.
	 * 
	 * @param subView
	 */
	public void addSubView(View subView);
	
	/**
	 * Get the Widget that representing this View.
	 * 
	 * @return The Widget
	 */
	public Widget getWidget();
}
