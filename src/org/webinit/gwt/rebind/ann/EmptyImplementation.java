package org.webinit.gwt.rebind.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Notify the managed environment that this implementation is empty. A class
 * should implement this method with {@link OverrideProxy} annotation.
 *  
 * @author felix
 */
@Retention(RetentionPolicy.CLASS)
public @interface EmptyImplementation {

}
