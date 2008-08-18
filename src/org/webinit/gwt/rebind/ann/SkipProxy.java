package org.webinit.gwt.rebind.ann;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used in The Interface and the managed proxy to notify the
 * managed environment that the following method should not be
 * taken care by the proxy generator.
 * 
 * @author felix
 *
 */
@Retention(RetentionPolicy.CLASS)
public @interface SkipProxy {

}
