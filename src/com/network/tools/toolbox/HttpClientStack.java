package com.network.tools.toolbox;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.http.AndroidHttpClient;

import com.network.tools.dispatch.AuthFailureError;
import com.network.tools.dispatch.Request;

/**
 * An HttpStack that performs request over an {@link HttpClient}.
 */
public class HttpClientStack {

	protected final HttpClient mClient;

	private static final String USER_AGENT = "unused/0";

	public HttpClientStack(Context context) {
		mClient = AndroidHttpClient.newInstance(USER_AGENT, context);
	}

	private static void addHeaders(HttpUriRequest httpRequest,
			Map<String, String> headers) {
		Iterator<String> i = headers.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			httpRequest.setHeader(key, headers.get(key));
		}
	}

	protected void onPrepareRequest(HttpUriRequest request) throws IOException {

	}

	public HttpResponse performRequest(Request<?> request,
			Map<String, String> additionalHeaders) throws IOException,
			AuthFailureError {
		byte[] postBody = request.getPostBody();
		HttpUriRequest httpRequest = null;
		if (postBody != null) {
			HttpPost postRequest = new HttpPost(request.getUrl());
			postRequest.addHeader("Content-Type",
					request.getPostBodyContentType());
			HttpEntity entity = new ByteArrayEntity(postBody);
			postRequest.setEntity(entity);
			httpRequest = postRequest;
		} else {
			httpRequest = new HttpGet(request.getUrl());
		}
		addHeaders(httpRequest, additionalHeaders);
		addHeaders(httpRequest, request.getHeaders());
		onPrepareRequest(httpRequest);
		HttpParams httpParams = httpRequest.getParams();
		int timeoutMs = request.getTimeoutMs();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
		HttpClientParams.setRedirecting(httpParams, true);
		return mClient.execute(httpRequest);
	}

}
