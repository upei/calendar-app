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
