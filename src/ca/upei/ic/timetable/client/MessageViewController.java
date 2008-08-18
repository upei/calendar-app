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

/**
 * Controller for the messages
 * @author felix
 */
public class MessageViewController implements ViewController {
	
	private MessageView view_;
	private ApplicationController app_;
	
	public MessageViewController(ApplicationController app) {
		app_ = app;
		view_ = new MessageView(this);
	}

	public View getView() {
		return view_;
	}
	
	public void log(String message) {
		view_.log(message);
	}
	
	public void debug(String message) {
		view_.debug(message);
	}

	public void clearMessage() {
		view_.clearMessage();
	}

}
