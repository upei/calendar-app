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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A view that show system messages
 * @author felix
 *
 */
public class MessageView implements View {
	
	private MessageViewController controller_;
	private DialogBox dialogBox_;
	private VerticalPanel panel_;
	
	public MessageView(MessageViewController controller) {
		controller_ = controller;
		dialogBox_ = new DialogBox(true, true);
		dialogBox_.setText("Messages");
		dialogBox_.setAnimationEnabled(true);
		dialogBox_.hide();
		// XXX a hack to make sure the dialog display top most
		dialogBox_.getElement().getStyle().setProperty("z-index", "100");
		
		ScrollPanel scrolled = new ScrollPanel();
		scrolled.setPixelSize(350, 250);

		panel_ = new VerticalPanel();
		panel_.setWidth("232px");
		
		scrolled.add(panel_);
		Button closeButton = new Button("Close");
		closeButton.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				dialogBox_.hide();
			}
			
		});
		
		closeButton.addKeyboardListener(new KeyboardListener() {

			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
			}
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				if (keyCode == KeyboardListener.KEY_ESCAPE)
					dialogBox_.hide();
			}
			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
			}
			
		});

		Button clearButton = new Button("Clear");
		clearButton.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				clearMessage();
			}
			
		});
		
		dialogBox_.setWidget(
				PanelUtils.verticalPanel(
						scrolled,
						PanelUtils.horizontalPanel(closeButton, clearButton)
				)
		);
	}

	public void addSubView(View subView) {
		throw new RuntimeException("Not Implemented.");
	}
	
	public void log(String msg) {
		panel_.add(new HTML(msg));
		dialogBox_.show();
	}
	
	public void debug(String msg) {
		log(msg);
	}
	
	public void clearMessage() {
		panel_.clear();
	}

	public ViewController getController() {
		return controller_;
	}

	public Widget getWidget() {
		return dialogBox_;
	}

	public void hide() {
		dialogBox_.show();
	}

	public void show() {
		dialogBox_.hide();
	}
}
