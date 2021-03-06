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
