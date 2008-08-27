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
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Helper utility for creating panels 
 * @author felix
 */
public class PanelUtils {

	/**
	 * Create a HorizontalPanel that contains a list of widgets
	 * @param widgets
	 * @return
	 */
	public static HorizontalPanel horizontalPanel(Widget... widgets) {
		HorizontalPanel panel = GWT.create(HorizontalPanel.class);
		for (Widget widget: widgets)
			panel.add(widget);
		return panel;
	}
	
	/**
	 * Create a VerticalPanel that contains a list of widgets
	 * @param widgets
	 * @return
	 */
	public static VerticalPanel verticalPanel(Widget...widgets) {
		VerticalPanel panel = GWT.create(VerticalPanel.class);
		for (Widget widget: widgets)
			panel.add(widget);
		return panel;
	}
	
	/**
	 * Create a DecoratorPanel that contains one widget
	 * @param widget
	 * @return
	 */
	public static DecoratorPanel decoratorPanel(Widget widget) {
		DecoratorPanel panel = GWT.create(DecoratorPanel.class);
		panel.setWidget(widget);
		return panel;
	}
	
	/**
	 * Create a ScrollPanel that contains one widget
	 * @param widget
	 * @return
	 */
	public static ScrollPanel scrollPanel(Widget widget) {
		ScrollPanel panel = GWT.create(ScrollPanel.class);
		panel.setWidget(widget);
		return panel;
	}
	
	/**
	 * Create a ScrollPanel that has fixed width and height.
	 * 
	 * @param widget
	 * @param width
	 * @param height
	 * @return
	 */
	public static ScrollPanel scrollPanel(Widget widget, int width, int height) {
		ScrollPanel panel = scrollPanel(widget);
		UIUtils.objectSetPixelSizeIntelligently(panel, width, height);
		return panel;
	}
	
	/**
	 * Create a SimplePanel that contains one widget.
	 * 
	 * @param widget
	 * @return
	 */
	public static SimplePanel simplePanel(Widget widget) {
		SimplePanel panel = GWT.create(SimplePanel.class);
		panel.setWidget(widget);
		return panel;
	}
	
	/**
	 * Create a fixed size SimplePanel that contains one widget.
	 * 
	 * @param widget
	 * @param width
	 * @param height
	 * @return
	 */
	public static SimplePanel simplePanel(Widget widget, int width, int height) {
		SimplePanel panel = simplePanel(widget);
		UIUtils.objectSetPixelSizeIntelligently(panel, width, height);
		return panel;
	}
	
	/**
	 * Create a Focus Panel
	 * 
	 * @param widget
	 * @param clickListener
	 * @param focusListener
	 * @param keyboardListener
	 * @param mouseListener
	 * @return
	 */
	public static FocusPanel focusPanel(Widget widget, ClickListener clickListener,
			FocusListener focusListener, KeyboardListener keyboardListener, MouseListener mouseListener) {
		FocusPanel panel = GWT.create(FocusPanel.class);
		if (clickListener != null)
			panel.addClickListener(clickListener);
		if (focusListener != null)
			panel.addFocusListener(focusListener);
		if (keyboardListener != null)
			panel.addKeyboardListener(keyboardListener);
		if (mouseListener != null)
			panel.addMouseListener(mouseListener);
		panel.setWidget(widget);
		return panel;
	}
}
