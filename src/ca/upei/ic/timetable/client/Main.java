package ca.upei.ic.timetable.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Main implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
	  ApplicationController app = new ApplicationController();
	  app.run();
  }
}
