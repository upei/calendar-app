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
