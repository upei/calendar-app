package ca.upei.ic.timetable.client;

import com.google.gwt.user.client.ui.ClickListener;

public interface CellClickListener extends ClickListener {
	public void setContext(Object context);
}
