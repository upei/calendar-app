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
