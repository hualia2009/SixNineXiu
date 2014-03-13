/**
 * 
 */
package com.network.tools.dispatch;

import java.util.concurrent.BlockingQueue;

import com.network.tools.toolbox.VolleyLog;

@SuppressWarnings("rawtypes")
public class CacheDispatcher extends Thread {

	private final Cache mCache;

	private volatile boolean mQuit = false;

	private final ExecutorDelivery mDelivery;

	private final BlockingQueue<Request> mCacheQueue;

	private final BlockingQueue<Request> mNetworkQueue;

	// private static final String TAG = "CacheDispatcher";

	private static final boolean DEBUG = VolleyLog.DEBUG;

	public CacheDispatcher(BlockingQueue<Request> cacheQueue, BlockingQueue<Request> networkQueue,
			Cache cache, ExecutorDelivery delivery) {
		mCacheQueue = cacheQueue;
		mNetworkQueue = networkQueue;
		mCache = cache;
		mDelivery = delivery;
	}

	public void quit() {
		mQuit = true;
		interrupt();
	}

	public void run() {
		if (DEBUG) {
			VolleyLog.v("start new dispatcher");
		}
		mCache.initialize();
		while (true) {
			try {
				final Request request = mCacheQueue.take();
				request.addMarker("cache-queue-take");
				if (request.isCanceled()) {
					request.finish("cache-discard-canceled");
					continue;
				}
				if (request.isLocal()) {
					Response<?> response = request.parseNetworkResponse(null);
					request.addMarker("cache-hit-local-parsed");
					mDelivery.postResponse(request, response);
					continue;
				}
				Cache.Entry entry = mCache.get(request.getCacheKey());
				if (entry == null) {
					request.addMarker("cache-miss");
					mNetworkQueue.put(request);
					continue;
				}
				request.addMarker("cache-hit");
				Response<?> response = request
						.parseNetworkResponse(new NetworkResponse(entry.data));
				request.addMarker("cache-hit-parsed");
				mDelivery.postResponse(request, response);
			} catch (InterruptedException e) {
				// We may have been interrupted because it was time to quit.
				if (mQuit) {
					return;
				}
				continue;
			}
		}
	}
}
