package ca.upei.ic.timetable.client;

public class TimetableException extends Exception {
		public TimetableException(Throwable exception) {
			super(exception);
		}
		public TimetableException(String message) {
			super(message);
		}
}
