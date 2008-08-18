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

import java.util.Map;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;

/**
 * Access remote services.
 *  
 * @author felix
 */
public class Remote {
	private String url_;
	
	public Remote(String url) {
		url_ = url;
	}

	/**
	 * Calls a remote method using HTTP GET
	 * 
	 * @param method
	 * @param params
	 * @param callback
	 */
	public void get(String method, Map<String, String> params, RequestCallback callback) {
		// build the query
		StringBuffer q = new StringBuffer();
		q.append("?method=" + method);
		
		if (null != params) {
			for (String key: params.keySet()) {
				q.append("&" + key + "=" + params.get(key));
			}
		}
		
		// build the request builder
		RequestBuilder req = new RequestBuilder(RequestBuilder.GET, url_ + q);
		
		req.setRequestData("");
		req.setCallback(callback);
		Request request = null;
		try {
			request = req.send();
		}
		catch (RequestException re) {
			callback.onError(request, re);
		}
	}
	
	/**
	 * Calls a remote method using HTTP POST
	 * 
	 * @param method
	 * @param contentType
	 * @param data
	 * @param callback
	 */
	public void post(String method, String contentType, String data, RequestCallback callback) {
		StringBuffer q = new StringBuffer();
		q.append("?method=" + method);
		
		RequestBuilder req = new RequestBuilder(RequestBuilder.POST, url_ + q);
		
		req.setHeader("Content-type", contentType);
		req.setRequestData(data);
		req.setCallback(callback);
		Request request = null;
		try {
			request = req.send();
		}
		catch (RequestException re) {
			callback.onError(request, re);
		}
	}
}
