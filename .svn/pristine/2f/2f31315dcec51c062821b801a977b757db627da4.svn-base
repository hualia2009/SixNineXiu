/**
 * 
 */
package com.network.tools.dispatch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.network.tools.toolbox.VolleyLog;
import com.network.tools.toolbox.VolleyLog.MarkerLog;

/**
 * Base class for all network requests.
 * 
 * @param <T>
 *            The type of parsed response this request expects.
 */
public abstract class Request<T> implements Comparable<Request<T>> {

	/** URL of this request. */
	private final String mUrl;

	private boolean mIsLocal = false;

	private boolean mCanceled = false;

	private boolean mDrainable = true;

	private boolean mShouldCache = true;

	/** Sequence number of this request, used to enforce FIFO ordering. */
	private Integer mSequence;

	private Object mTag;

	private long mRequestBirthTime = 0;

	private RequestQueue mRequestQueue;

	private RetryPolicy mRetryPolicy;

	private Cache.Entry mCacheEntry;

	private boolean mResponseDelivered = false;

	/** Listener interface for errors. */
	private final Response.ErrorListener mErrorListener;

	/**
	 * Default encoding for POST parameters. See {@link #getPostParamsEncoding()}.
	 */
	private static final String DEFAULT_POST_PARAMS_ENCODING = "UTF-8";

	/**
	 * Threshold at which we should log the request (even when debug logging is not enabled).
	 */
	private static final long SLOW_REQUEST_THRESHOLD_MS = 3000;

	/** An event log tracing the lifetime of this request; for debugging. */
	private final MarkerLog mEventLog = MarkerLog.ENABLED ? new MarkerLog() : null;

	public Request(String url, Response.ErrorListener errorListener) {
		mUrl = url;
		mErrorListener = errorListener;
		setRetryPolicy(new DefaultRetryPolicy());
	}

	public void setRetryPolicy(RetryPolicy retryPolicy) {
		mRetryPolicy = retryPolicy;
	}

	private byte[] encodePostParameters(Map<String, String> postParams, String postParamsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			Iterator<Map.Entry<String, String>> i = postParams.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry<String, String> entry = i.next();
				encodedParams.append(URLEncoder.encode(entry.getKey(), postParamsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(), postParamsEncoding));
				encodedParams.append('&');
			}
			return encodedParams.toString().getBytes(postParamsEncoding);
		} catch (UnsupportedEncodingException uee) {
			StringBuilder exception = new StringBuilder();
			exception.append("Encoding not supported: ");
			exception.append(postParamsEncoding);
			throw new RuntimeException(exception.toString(), uee);
		}
	}

	/**
	 * Adds an event to this request's event log; for debugging.
	 */
	public void addMarker(String tag) {
		if (MarkerLog.ENABLED) {
			mEventLog.add(tag, Thread.currentThread().getId());
		} else if (mRequestBirthTime == 0) {
			mRequestBirthTime = SystemClock.elapsedRealtime();
		}
	}

	/**
	 * Notifies the request queue that this request has finished (successfully or with error).
	 * 
	 * <p>
	 * Also dumps all events from this request's event log; for debugging.
	 * </p>
	 */
	void finish(final String tag) {
		if (mRequestQueue != null) {
			mRequestQueue.finish(this);
		}
		if (MarkerLog.ENABLED) {
			final long threadId = Thread.currentThread().getId();
			if (Looper.myLooper() != Looper.getMainLooper()) {
				// If we finish marking off of the main thread, we need to
				// actually do it on the main thread to ensure correct ordering.
				Handler mainThread = new Handler(Looper.getMainLooper());
				mainThread.post(new Runnable() {
					@Override
					public void run() {
						mEventLog.add(tag, threadId);
						mEventLog.finish(this.toString());
					}
				});
				return;
			}
			mEventLog.add(tag, threadId);
			mEventLog.finish(this.toString());
		} else {
			long requestTime = SystemClock.elapsedRealtime() - mRequestBirthTime;
			if (requestTime >= SLOW_REQUEST_THRESHOLD_MS) {
				VolleyLog.d("%d ms: %s", requestTime, this.toString());
			}
		}
	}

	protected abstract void deliverResponse(T response);

	/**
	 * 
	 * @param error
	 */
	public void deliverError(EnergyError error) {
		if (mErrorListener != null) {
			mErrorListener.onErrorResponse(error);
		}
	}

	public Map<String, String> getHeaders() {
		return Collections.emptyMap();
	}

	public Cache.Entry getCacheEntry() {
		return mCacheEntry;
	}

	public String getCacheKey() {
		return getUrl();
	}

	public byte[] getPostBody() throws AuthFailureError {
		Map<String, String> postParams = getPostParams();
		if (postParams != null && postParams.size() > 0) {
			return encodePostParameters(postParams, getPostParamsEncoding());
		}
		return null;
	}

	public String getPostBodyContentType() {
		StringBuilder contentType = new StringBuilder();
		contentType.append("application/x-www-form-urlencoded; charset=");
		contentType.append(getPostParamsEncoding());
		return contentType.toString();
	}

	protected String getPostParamsEncoding() {
		return DEFAULT_POST_PARAMS_ENCODING;
	}

	protected Map<String, String> getPostParams() throws AuthFailureError {
		return null;
	}

	public boolean hasHadResponseDelivered() {
		return mResponseDelivered;
	}

	public Priority getPriority() {
		return Priority.NORMAL;
	}

	public RetryPolicy getRetryPolicy() {
		return mRetryPolicy;
	}

	/**
	 * Returns the sequence number of this request.
	 */
	public final int getSequence() {
		if (mSequence == null) {
			throw new IllegalStateException("getSequence called before setSequence");
		}
		return mSequence.intValue();
	}

	public Object getTag() {
		return mTag;
	}

	public final int getTimeoutMs() {
		return mRetryPolicy.getCurrentTimeout();
	}

	public boolean isCanceled() {
		return mCanceled;
	}

	public void cancel() {
		mCanceled = true;
	}

	public boolean isDrainable() {
		return mDrainable;
	}

	public boolean isLocal() {
		return mIsLocal;
	}

	public void setLocal(boolean isLocal) {
		mIsLocal = isLocal;
	}

	public void markDelivered() {
		mResponseDelivered = true;
	}

	public void setDrainable(boolean drainable) {
		mDrainable = drainable;
	}

	public void setRequestQueue(RequestQueue requestQueue) {
		mRequestQueue = requestQueue;
	}

	public final void setShouldCache(boolean shouldCache) {
		mShouldCache = shouldCache;
	}

	public final boolean shouldCache() {
		return mShouldCache;
	}

	public void setCacheEntry(Cache.Entry entry) {
		mCacheEntry = entry;
	}

	public final void setSequence(int sequence) {
		mSequence = sequence;
	}

	public void setTag(Object tag) {
		mTag = tag;
	}

	protected abstract Response<T> parseNetworkResponse(NetworkResponse response);

	protected EnergyError parseNetworkError(EnergyError energyError) {
		return energyError;
	}

	public String getUrl() {
		return mUrl;
	}

	/**
	 * Priority values. Requests will be processed from higher priorities to lower priorities, in
	 * FIFO order.
	 */
	public enum Priority {
		LOW, NORMAL, HIGH, IMMEDIATE
	}

	/**
	 * Our comparator sorts from high to low priority, and secondarily by sequence number to
	 * provide FIFO ordering.
	 */
	@Override
	public int compareTo(Request<T> other) {
		Priority left = this.getPriority();
		Priority right = other.getPriority();

		// High-priority requests are "lesser" so they are sorted to the front.
		// Equal priorities are sorted by sequence number to provide FIFO
		// ordering.
		return left == right ? this.mSequence - other.mSequence : right.ordinal() - left.ordinal();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (mCanceled) {
			sb.append("[X] ");
		} else {
			sb.append("[ ] ");
		}
		sb.append(getUrl());
		sb.append(" ");
		sb.append(getPriority());
		sb.append(" ");
		sb.append(mSequence);
		return sb.toString();
	}
}
