/**
 * 
 */
package com.network.tools.toolbox;

import com.network.tools.dispatch.Request;
import com.network.tools.dispatch.Response;

/**
 * The basic JsonRequest.
 * 
 */
public abstract class JsonRequest<T> extends Request<T> {

	private Response.Listener<T> mListener;

	public JsonRequest(String url, Response.Listener<T> listener,
			Response.ErrorListener errorListener) {
		super(url, errorListener);
		mListener = listener;
	}

	@Override
	protected void deliverResponse(T response) {
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}
}
