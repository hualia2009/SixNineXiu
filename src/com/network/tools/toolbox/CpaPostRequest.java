package com.network.tools.toolbox;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.network.tools.dispatch.NetworkResponse;
import com.network.tools.dispatch.ParseError;
import com.network.tools.dispatch.Response;

public class CpaPostRequest extends JsonRequest<JSONObject> {

	private final Map<String, String> mPostParams;

	public CpaPostRequest(String url, Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(url, listener, errorListener);
		mPostParams = new HashMap<String, String>();
		setShouldCache(false);
	}

	public void addPostParam(String key, String value) {
		mPostParams.put(key, value);
	}

	@Override
	public Map<String, String> getPostParams() {
		return mPostParams;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

}
