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
