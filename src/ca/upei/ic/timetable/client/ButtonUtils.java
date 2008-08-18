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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;

/**
 * This is the helper class for creating buttons.
 * 
 * @author felix
 *
 */
public class ButtonUtils {
	
	/**
	 * Create an auto-sized button
	 * 
	 * @param caption
	 * @param clickListener
	 * @param focusListener
	 * @return
	 */
	public static Button button(String caption, ClickListener clickListener, FocusListener focusListener) {
		Button button = GWT.create(Button.class);
		button.setText(caption);
		
		if (null != clickListener)
			button.addClickListener(clickListener);
		
		if (null != focusListener)
			button.addFocusListener(focusListener);
		
		return button;
	}
	
	/**
	 * Create a fix size button
	 * 
	 * @param caption
	 * @param width
	 * @param height
	 * @param clickListener
	 * @param focusListener
	 * @return
	 */
	public static Button button(String caption, int width, int height, ClickListener clickListener, FocusListener focusListener) {
		Button button = button(caption, clickListener, focusListener);
		UIUtils.objectSetPixelSizeIntelligently(button, width, height);
		return button;
	}
	
}
