package com.network.tools.dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.Looper;

import com.network.tools.toolbox.BasicNetwork;
import com.network.tools.toolbox.VolleyLog;

/**
 * Implements a request queue and start a ThreadPool to handle all requests that you added to it.
 */
@SuppressWarnings("rawtypes")
public class RequestQueue {

	private final Cache mCache;

	private final BasicNetwork mNetwork;

	private final ExecutorDelivery mDelivery;

	/** The network dispatchers. */
	private NetworkDispatcher[] mDispatchers;

	/** The network dispatchers. */
	private CacheDispatcher mCacheDispatcher;

	private final Set<Request> mCurrentRequests = new HashSet<Request>();

	/** The cache queue. */
	private final PriorityBlockingQueue<Request> mCacheQueue = new PriorityBlockingQueue<Request>();

	private final PriorityBlockingQueue<Request> mNetworkQueue = new PriorityBlockingQueue<Request>();

	private final Map<String, Queue<Request>> mWaitingRequests = new HashMap<String, Queue<Request>>();

	private static AtomicInteger sSequenceGenerator = new AtomicInteger();

	private static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 1;

	public RequestQueue(Cache cache, BasicNetwork network, int threadPoolSize,
			ExecutorDelivery delivery) {
		mCache = cache;
		mNetwork = network;
		mDelivery = delivery;
		mDispatchers = new NetworkDispatcher[threadPoolSize];
	}

	public RequestQueue(Cache cache, BasicNetwork network, int threadPoolSize) {
		this(cache, network, threadPoolSize, new ExecutorDelivery(new Handler(
				Looper.getMainLooper())));
	}

	public RequestQueue(Cache cache, BasicNetwork network) {
		this(cache, network, DEFAULT_NETWORK_THREAD_POOL_SIZE);
	}

	/**
	 * Adds a Request to the dispatch queue.
	 * 
	 * @param request
	 *            The request to service
	 * @return The passed-in request
	 */
	public Request add(Request request) {
		request.setRequestQueue(this);
		synchronized (mCurrentRequests) {
			mCurrentRequests.add(request);
		}
		request.setSequence(sSequenceGenerator.incrementAndGet());
		request.addMarker("add-to-queue");
		if (!request.shouldCache()) {
			mNetworkQueue.add(request);
			return request;
		}
		synchronized (mWaitingRequests) {
			String cacheKey = request.getCacheKey();
			if (mWaitingRequests.containsKey(cacheKey)) {
				Queue<Request> stagedRequests = mWaitingRequests.get(cacheKey);
				if (stagedRequests == null) {
					stagedRequests = new LinkedList<Request>();
				}
				stagedRequests.add(request);
				mWaitingRequests.put(cacheKey, stagedRequests);
				if (VolleyLog.DEBUG) {
					VolleyLog
							.v("Request for cacheKey=%s is in flight, putting on hold.", cacheKey);
				}
			} else {
				mWaitingRequests.put(cacheKey, null);
				mCacheQueue.add(request);
			}
		}
		return request;
	}

	void finish(Request<?> request) {
		// Remove from the set of requests currently being processed.
		synchronized (mCurrentRequests) {
			mCurrentRequests.remove(request);
		}
		if (request.shouldCache()) {
			synchronized (mWaitingRequests) {
				String cacheKey = request.getCacheKey();
				Queue<Request> waitingRequests = mWaitingRequests.remove(cacheKey);
				if (waitingRequests != null) {
					if (VolleyLog.DEBUG) {
						VolleyLog.v("Releasing %d waiting requests for cacheKey=%s.",
								waitingRequests.size(), cacheKey);
					}
					// Process all queued up requests. They won't be considered
					// as in flight, but
					// that's not a problem as the cache has been primed by
					// 'request'.
					mCacheQueue.addAll(waitingRequests);
				}
			}
		}
	}

	public void cancelAll(RequestFilter filter) {
		synchronized (mCurrentRequests) {
			Iterator<Request> i = mCurrentRequests.iterator();
			while (i.hasNext()) {
				Request<?> request = i.next();
				if (filter.apply(request)) {
					request.cancel();
				}
			}
		}
	}

	public void cancelAll(final Object tag) {
		if (tag == null) {
			throw new IllegalArgumentException("Cannot cancelAll with a null tag");
		}
		cancelAll(new RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				return request.getTag() == tag;
			}
		});
	}

	private void cancelDrainable(PriorityBlockingQueue<Request> queue, int sequenceNumber) {
		List<Request> pending = new ArrayList<Request>();
		// Remove all requests from the queue in order to work on them.
		queue.drainTo(pending);
		for (Request request : pending) {
			if (request.isDrainable() && request.getSequence() < sequenceNumber) {
				request.cancel();
			}
			// Put the request back on the queue. If it is canceled, it
			// will be discarded by one of the dispatchers or the delivery.
			queue.add(request);
		}
	}

	public void drain(int sequenceNumber) {
		cancelDrainable(mCacheQueue, sequenceNumber);
		cancelDrainable(mNetworkQueue, sequenceNumber);
		mDelivery.discardBefore(sequenceNumber);
		if (VolleyLog.DEBUG) {
			VolleyLog.v("Draining requests with sequence number below %s", sequenceNumber);
		}
	}

	/**
	 * Gets a sequence number.
	 */
	public static int getSequenceNumber() {
		return sSequenceGenerator.incrementAndGet();
	}

	public void start() {
		stop();
		mCacheDispatcher = new CacheDispatcher(mCacheQueue, mNetworkQueue, mCache, mDelivery);
		mCacheDispatcher.start();
		int length = mDispatchers.length;
		for (int i = 0; i < length; i++) {
			NetworkDispatcher networkDispatcher = new NetworkDispatcher(mNetworkQueue, mNetwork,
					mCache, mDelivery);
			mDispatchers[i] = networkDispatcher;
			VolleyLog.d("Start %s dispatched to RequestQueue",
					new Object[] { networkDispatcher.getName() });
			networkDispatcher.start();
		}
	}

	public void stop() {
		if (mCacheDispatcher != null) {
			mCacheDispatcher.quit();
		}
		for (NetworkDispatcher networkDispatcher : mDispatchers) {
			if (networkDispatcher != null) {
				VolleyLog.d("Stop %s from  RequestQueue",
						new Object[] { networkDispatcher.getName() });
				networkDispatcher.quit();
			}
		}
	}

	public interface RequestFilter {

		public boolean apply(Request<?> request);

	}
}
