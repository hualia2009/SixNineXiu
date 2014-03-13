/**
 * 
 */
package com.network.tools.toolbox;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.conn.ConnectTimeoutException;

import android.os.SystemClock;

import com.network.tools.dispatch.AuthFailureError;
import com.network.tools.dispatch.EnergyError;
import com.network.tools.dispatch.NetworkError;
import com.network.tools.dispatch.NetworkResponse;
import com.network.tools.dispatch.NoConnectionError;
import com.network.tools.dispatch.Request;
import com.network.tools.dispatch.RetryPolicy;
import com.network.tools.dispatch.ServerError;
import com.network.tools.dispatch.TimeoutError;

/**
 * A network helper class to perform request and accept network response.
 */
public class BasicNetwork {

	protected final HttpClientStack mHttpStack;

	private static final int SLOW_REQUEST_THRESHOLD_MS = 3000;

	private static int DEFAULT_POOL_SIZE = 4096;

	protected static final boolean DEBUG = VolleyLog.DEBUG;

	protected final ByteArrayPool mPool;

	/**
	 * @param httpStack
	 *            HTTP stack to be used
	 */
	public BasicNetwork(HttpClientStack httpStack) {
		// If a pool isn't passed in, then build a small default pool that will give us a lot of
		// benefit and not use too much memory.
		this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
	}

	/**
	 * @param httpStack
	 *            HTTP stack to be used
	 * @param pool
	 *            a buffer pool that improves GC performance in copy operations
	 */
	public BasicNetwork(HttpClientStack httpStack, ByteArrayPool pool) {
		mHttpStack = httpStack;
		mPool = pool;
	}

	private static Map<String, String> convertHeaders(Header[] headers) {
		Map<String, String> result = new HashMap<String, String>();
		int length = headers.length;
		for (int i = 0; i < length; i++) {
			result.put(headers[i].getName(), headers[i].getValue());
		}
		return result;
	}

	/**
	 * Logs requests that took over SLOW_REQUEST_THRESHOLD_MS to complete.
	 */
	private void logSlowRequests(long requestLifetime, Request<?> request,
			byte[] responseContents, StatusLine statusLine) {
		if (DEBUG || requestLifetime > SLOW_REQUEST_THRESHOLD_MS) {
			VolleyLog.d("HTTP response for request=<%s> [lifetime=%d], [size=%s], "
					+ "[rc=%d], [retryCount=%s]", request, requestLifetime,
					responseContents != null ? responseContents.length : "null",
					statusLine.getStatusCode(), request.getRetryPolicy().getCurrentRetryCount());
		}
	}

	/** Reads the contents of HttpEntity into a byte[]. */
	private byte[] entityToBytes(HttpEntity entity) throws IOException, ServerError {
		PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(mPool,
				(int) entity.getContentLength());
		byte[] buffer = null;
		try {
			InputStream in = entity.getContent();
			if (in == null) {
				throw new ServerError();
			}
			buffer = mPool.getBuf(1024);
			int count;
			while ((count = in.read(buffer)) != -1) {
				bytes.write(buffer, 0, count);
			}
			return bytes.toByteArray();
		} finally {
			try {
				// Close the InputStream and release the resources by "consuming the content".
				entity.consumeContent();
			} catch (IOException e) {
				// This can happen if there was an exception above that left the entity in
				// an invalid state.
				VolleyLog.v("Error occured when calling consumingContent");
			}
			mPool.returnBuf(buffer);
			bytes.close();
		}
	}

	public NetworkResponse performRequest(Request<?> request) throws EnergyError {
		long requestStart = SystemClock.elapsedRealtime();
		while (true) {
			HttpResponse httpResponse = null;
			byte[] responseContents = null;
			Map<String, String> responseHeaders = new HashMap<String, String>();
			try {
				// Gather headers.
				Map<String, String> headers = new HashMap<String, String>();
				// addCacheHeaders(headers, request.getCacheEntry());
				httpResponse = mHttpStack.performRequest(request, headers);
				StatusLine statusLine = httpResponse.getStatusLine();

				responseHeaders = convertHeaders(httpResponse.getAllHeaders());
				// Handle cache validation.
				if (statusLine.getStatusCode() == HttpStatus.SC_NOT_MODIFIED) {
					return new NetworkResponse(HttpStatus.SC_NOT_MODIFIED,
							request.getCacheEntry().data, responseHeaders, true);
				}

				responseContents = entityToBytes(httpResponse.getEntity());
				// if the request is slow, log it.
				long requestLifetime = SystemClock.elapsedRealtime() - requestStart;
				logSlowRequests(requestLifetime, request, responseContents, statusLine);

				if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
					throw new IOException();
				}
				return new NetworkResponse(HttpStatus.SC_OK, responseContents, responseHeaders,
						false);
			} catch (SocketTimeoutException e) {
				attemptRetryOnException("socket", request, new TimeoutError());
			} catch (ConnectTimeoutException e) {
				attemptRetryOnException("connection", request, new TimeoutError());
			} catch (MalformedURLException e) {
				throw new RuntimeException("Bad URL " + request.getUrl(), e);
			} catch (IOException e) {
				int statusCode = 0;
				NetworkResponse networkResponse = null;
				if (httpResponse != null) {
					statusCode = httpResponse.getStatusLine().getStatusCode();
				} else {
					throw new NoConnectionError(e);
				}
				VolleyLog.e("Unexpected response code %d for %s", statusCode, request.getUrl());
				if (responseContents != null) {
					networkResponse = new NetworkResponse(statusCode, responseContents,
							responseHeaders, false);
					if (statusCode == HttpStatus.SC_UNAUTHORIZED
							|| statusCode == HttpStatus.SC_FORBIDDEN) {
						attemptRetryOnException("auth", request, new AuthFailureError(
								networkResponse));
					} else {
						// TODO: Only throw ServerError for 5xx status codes.
						throw new ServerError(networkResponse);
					}
				} else {
					throw new NetworkError(networkResponse);
				}
			}
		}
	}

	private static void attemptRetryOnException(String logPrefix, Request<?> request,
			EnergyError exception) throws EnergyError {
		RetryPolicy retryPolicy = request.getRetryPolicy();
		int oldTimeout = request.getTimeoutMs();
		try {
			retryPolicy.retry(exception);
		} catch (EnergyError e) {
			request.addMarker(String.format("%s-timeout-giveup [timeout=%s]", logPrefix,
					oldTimeout));
			throw e;
		}
		request.addMarker(String.format("%s-retry [timeout=%s]", logPrefix, oldTimeout));
	}

	protected void logError(String what, String url, long start) {
		long now = SystemClock.elapsedRealtime();
		VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", what, (now - start), url);
	}
}
