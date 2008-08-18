package ca.upei.ic.timetable.client;

/**
 * Main Controller for the Application
 * 
 * This class is the real entry point of the application. It loads other controllers
 * on demand or creates them at the start. All other controllers are retrievable
 * from this controller. This controller also handles application-wide features
 * such as logging and printing.
 * 
 * @author felix
 */
public class ApplicationController implements ViewController {

	public static final String OOPS = "Oops! It seems to be a problem when loading the page. Please try to refresh the page.";
	private final static boolean _DEBUG = true;
	

	private FindCourseViewController findCourseController_;
	private CourseViewController courseController_;
	private MessageViewController messageController_;
	private CourseCalendarViewController calendarController_;
	private ApplicationView view_;
	
	public ApplicationController() {
	}
	
	public FindCourseViewController getFindCourseController() {
		if (null == findCourseController_) {
			findCourseController_ = new FindCourseViewController(this);
		}
		
		return findCourseController_;
	}

	public CourseViewController getCourseController() {
		if (null == courseController_) {
			courseController_ = new CourseViewController(this);
		}
		
		return courseController_;
	}
	
	public CourseCalendarViewController getCourseCalendarController() {
		if (null == calendarController_) {
			calendarController_ = new CourseCalendarViewController(this);
		}
		
		return calendarController_;
	}
	
	public MessageViewController getMessageController() {
		if (null == messageController_)
			messageController_ = new MessageViewController(this);
		
		return messageController_;
	}
	
	public void log(String message) {
		this.getMessageController().log(message);
	}
	
	public void debug(String message) {
		if (_DEBUG)
		this.getMessageController().debug(message);
	}
	
	public void clearMessage() {
		this.getMessageController().clearMessage();
	}
	
	public void error(String message, Throwable throwable) {
		log(message);
		if (null != throwable) {
			log(throwable.getMessage());
		}
		if (null != throwable.getCause()) {
			debug(throwable.getCause().getMessage());
		}
	}
	
	public void run() {
		getView().show();
	}

	public View getView() {
		if (null == view_) {
			view_ = new ApplicationView(this);
		}
		
		return view_;
	}
	
	public native void print(String cssHref, String html) /*-{
		var frame = $doc.getElementById('__gwt_printingFrame');
		if (!frame) {
			$wnd.alert('Error: No printing frame!');
			return;
		}
		var pop = window.open('about:blank', '__gwt_printingWindow', 'width=720,height=400,toolbar=0,status=0,menubar=0,scrollbars=1,location=0');
		
		var doc = pop.document;
		doc.write('<html><head><link rel="stylesheet" type="text/css" href="'
				+cssHref
				+'"><link rel="stylesheet" type="text/css" href="print.css" media="print"><title>Calendar</title></head><body>');
		doc.write(html);
		doc.write('</body></html>');
		doc.close();
		pop.focus();
		pop.print();
	}-*/;
}
