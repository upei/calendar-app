package ca.upei.ic.timetable.client;

import com.google.gwt.user.client.ui.UIObject;

/**
 * Helper methods for UIObjects
 * 
 * @author felix
 */
public class UIUtils {
	
	public static void objectSetPixelSizeIntelligently(UIObject object, int width, int height) {
		
		if (width >= 0 && height >= 0) {
			object.setPixelSize(width, height);
		}
		else if (width >=0 && height < 0) {
			object.setWidth(Integer.toString(width) + "px");
		}
		else if (width < 0 && height >= 0) {
			object.setHeight(Integer.toString(height) + "px");
		}
	}
}
