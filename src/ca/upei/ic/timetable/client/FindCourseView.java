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

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A View that includes a list of levels and a list of departments
 * 
 * @author felix
 *
 */
public class FindCourseView implements View {
	private final DialogBox dialogBox_;
	private FindCourseViewController controller_;
	private final Widget levelTable_;
	private final Widget departmentTable_;
	private final Widget semesterTable_;

	public FindCourseView(FindCourseViewController controller) {
		controller_ = controller;
		
		// set up the dialog box
		dialogBox_ = new DialogBox(false, true); // autohide = false, modal = true
		dialogBox_.setAnimationEnabled(true);
		dialogBox_.setText("Find Courses...");
		
		
		// i have a horizontal panel
		HorizontalPanel filterPanel = new HorizontalPanel();
		// i have a level flex table
		levelTable_ = controller_.getLevelModel().getWidget();
		departmentTable_ = controller_.getDepartmentModel().getWidget();
		semesterTable_ = controller_.getSemesterModel().getWidget();
		
		// button panel
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
		
		// i have an OK button
		final Button okButton = new Button("Search");
		okButton.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				// search and close the dialog
				controller_.search();
				hide();
			}
			
		});
		
		okButton.addKeyboardListener(new KeyboardListener() {

			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
			}

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
			}
			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				if (keyCode == KeyboardListener.KEY_ENTER) {
					okButton.click();
				}
			}
			
		});
		
		final Button cancelButton = new Button("Cancel");
		cancelButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				hide();
			}
		});
		
		cancelButton.addKeyboardListener(new KeyboardListener() {

			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
			}

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
			}

			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				if (keyCode == KeyboardListener.KEY_ESCAPE)
					cancelButton.click();
			}
			
		});


		
		SimplePanel empty = new SimplePanel();
		empty.setWidth("230px");
		buttonPanel.add(empty);
		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);
		buttonPanel.setSpacing(5);
		buttonPanel.setWidth("485px");
		
		// add the panel to the dialog box
		dialogBox_.add(PanelUtils.verticalPanel(PanelUtils.horizontalPanel(
				PanelUtils.verticalPanel(
						PanelUtils.scrollPanel(levelTable_, 250, 180),
						PanelUtils.scrollPanel(semesterTable_, 250, 120)
						),
				PanelUtils.scrollPanel(departmentTable_, 250, 320)
				),
				buttonPanel
		));
		dialogBox_.setPopupPosition(240, 0);
	}
	
	public FindCourseViewController getController() {
		return controller_;
	}
	
	public void show() {
		dialogBox_.show();
	}
	
	public void hide() {
		dialogBox_.hide();
	}
	
	public void addSubView(View subView) {
		dialogBox_.add(subView.getWidget());
	}
	
	public Widget getWidget() {
		return dialogBox_;
	}

}
