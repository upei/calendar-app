package org.webinit.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class Test implements EntryPoint, Observer {

	public void onModuleLoad() {
		Observable o1 = GWT.create(Observable.class);
		o1.addObserver("event1", this);
		o1.trigger("event1", null, null);
	}
	
	public boolean observe(String event, Object object, Object arg) {
		GWT.log(event + " triggered", null);
		return true;
	}

}

