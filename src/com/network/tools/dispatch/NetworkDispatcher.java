package com.network.tools.dispatch;

import java.util.concurrent.BlockingQueue;

import com.network.tools.toolbox.BasicNetwork;
import com.network.tools.toolbox.VolleyLog;

/**
 * Dispatch and perform request, then post response to UI thread.
 */
@SuppressWarnings("rawtypes")
public class NetworkDispatcher extends Thread {

	private final Cache mCache;

	private final ExecutorDelivery mDelivery;

	private final BasicNetwork mNetwork;

	private final BlockingQueue<Request> mQueue;

	private volatile boolean mQuit = false;

	public NetworkDispatcher(BlockingQueue<Request> queue,
			BasicNetwork network, Cache cache, ExecutorDelivery delivery) {
		mCache = cache;
		mQueue = queue;
		mNetwork = network;
		mDelivery = delivery;
	}

	/**
	 * Forces this dispatcher to quit immediately. If any requests are still in
	 * the queue, they are not guaranteed to be processed.
	 */
	public void quit() {
		mQuit = true;
		interrupt();
	}

	public void run() {
		Request request;
		while (true) {
			try {
				request = mQueue.take();
			} catch (InterruptedException e) {
				// We may have been interrupted because it was time to quit.
				if (mQuit) {
					return;
				}
				continue;
			}
			try {
				request.addMarker("network-queue-take");

				// If the request was cancelled already, do not perform the
				// network request.
				if (request.isCanceled()) {
					request.finish("network-discard-cancelled");
					continue;
				}

				// Perform the network request.
				NetworkResponse networkResponse = mNetwork
						.performRequest(request);
				request.addMarker("network-http-complete");

				if (networkResponse.notModified
						&& request.hasHadResponseDelivered()) {
					request.finish("not-modified");
					continue;
				}

				// Parse the response here on the worker thread.
				Response<?> response = request
						.parseNetworkResponse(networkResponse);
				request.addMarker("network-parse-complete");

				// Write to cache if applicable.
				if (request.shouldCache() && response.cacheEntry != null) {
					mCache.put(request.getCacheKey(), response.cacheEntry);
					request.addMarker("network-cache-written");
				}

				// Post the response back.
				request.markDelivered();
				mDelivery.postResponse(request, response);
			} catch (EnergyError energyError) {
				parseAndDeliverNetworkError(request, energyError);
			} catch (Exception e) {
				VolleyLog.e("Unhandled exception %s", e.toString());
				mDelivery.postError(request, new EnergyError(e));
			}
		}
	}

	private void parseAndDeliverNetworkError(Request<?> request,
			EnergyError error) {
		mDelivery.postError(request, request.parseNetworkError(error));
	}
}
