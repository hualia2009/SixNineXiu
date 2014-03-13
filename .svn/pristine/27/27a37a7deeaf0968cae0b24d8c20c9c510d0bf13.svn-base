/**
 * 
 */
package com.network.tools.toolbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.network.tools.dispatch.EnergyError;
import com.network.tools.dispatch.Request;
import com.network.tools.dispatch.RequestQueue;
import com.network.tools.dispatch.Response;
import com.network.tools.dispatch.RequestQueue.RequestFilter;

/**
 * Bitmap loader to load images and manage the lifecycle of bitmap.
 */
public class BitmapLoader {

	/** 执行图片批量更新的任务 */
	private Runnable mRunnable;

	/** 批量绑定Bitmap到UI视图 */
	private final Handler mHandler;

	/** 请求队列用于处理所有图片加载请求 */
	private final RequestQueue mRequestQueue;

	/** Bitmap缓存机制，实现最近最少访问算法 */
	private final BitmapLruCache mCachedRemoteImages;

	private static final int MAX_BITMAP_SIZE_IN_BYTES = 0x7d000;

	/** 正在处理的图片下载请求集合 */
	private final HashMap<String, RequestListenerWrapper> mInFlightRequests;

	/** 图片请求处理完成后，延迟批量更新的集合 */
	private final HashMap<String, RequestListenerWrapper> mBatchedResponses;

	private TentativeGcRunner mTentativeGcRunner;

	/**
	 * Creates an instance of BitmapLoader.
	 * 
	 * @param requestQueue
	 *            The request queue to handle bitmap request.
	 */
	public BitmapLoader(RequestQueue requestQueue) {
		mRequestQueue = requestQueue;
		mHandler = new Handler(Looper.getMainLooper());
		mInFlightRequests = new HashMap<String, RequestListenerWrapper>();
		mBatchedResponses = new HashMap<String, RequestListenerWrapper>();
		// Get max available VM memory, exceeding this amount will throw an
		// OutOfMemory exception. Stored in bytes as LruCache takes an
		// int in its constructor.
		final int maxMemory = (int) Runtime.getRuntime().maxMemory();
		// Use 1/8th of the available memory for this memory cache.
		final int bitmapCacheSize = maxMemory / 8;
		VolleyLog.wtf(" Get VM memory [MaxMemory:%dMB]-[BitmapCacheSize:%dMB]",
				maxMemory / 1024 / 1024, bitmapCacheSize / 1024 / 1024);
		mCachedRemoteImages = new BitmapLruCache(bitmapCacheSize);
		mTentativeGcRunner = new TentativeGcRunner();
	}

	/**
	 * Clear the bitmap cache when application exits.
	 */
	public void clearBitmapCache() {
		mCachedRemoteImages.evictAll();
		VolleyLog.wtf("Clear the bitmap cache when application exits");
	}

	/**
	 * Generates the cache key.
	 * 
	 * @param url
	 *            the image url to load.
	 * @param maxWidth
	 * @param maxHeight
	 * @return cacheKey
	 */
	private static String getCacheKey(String url, int maxWidth, int maxHeight) {
		StringBuilder cacheKey = new StringBuilder();
		cacheKey.append("#W").append(maxWidth).append("#H").append(maxHeight).append(url);
		return cacheKey.toString();
	}

	/**
	 * Handle the bitmap response for batch.
	 * 
	 * @param cacheKey
	 * @param wrapper
	 */
	private void batchResponse(String cacheKey, RequestListenerWrapper wrapper) {
		mBatchedResponses.put(cacheKey, wrapper);
		if (mRunnable == null) {
			mRunnable = new Runnable() {
				@Override
				public void run() {
					Iterator<RequestListenerWrapper> wrappers = mBatchedResponses.values()
							.iterator();
					while (wrappers.hasNext()) {
						RequestListenerWrapper wrapper = wrappers.next();
						Iterator<BitmapContainer> i = wrapper.handlers.iterator();
						while (i.hasNext()) {
							BitmapContainer container = i.next();
							container.mBitmap = wrapper.responseBitmap;
							container.mBitmapLoaded.onResponse(container);
						}
					}
					mBatchedResponses.clear();
					mRunnable = null;
				}
			};
			mHandler.postDelayed(mRunnable, 100);
		}
	}

	/**
	 * Do something when retrieve image error.
	 * 
	 * @param cacheKey
	 */
	private void onGetImageError(String cacheKey) {
		RequestListenerWrapper wrapper = mInFlightRequests.remove(cacheKey);
		batchResponse(cacheKey, wrapper);
	}

	/**
	 * Do something when retrieve image success.
	 * 
	 * @param cacheKey
	 * @param response
	 *            the Bitmap.
	 */
	private void onGetImageSuccess(String cacheKey, Bitmap response) {
		long responseBytes = response.getHeight() * response.getRowBytes();
		if (responseBytes <= MAX_BITMAP_SIZE_IN_BYTES) {
			mCachedRemoteImages.put(cacheKey, response);
		} else {
			VolleyLog.d("Don't put bitmap [%s] to RAM Memory, it will occupy Memory [%d]",
					cacheKey, responseBytes);
		}
		RequestListenerWrapper wrapper = mInFlightRequests.remove(cacheKey);
		if (wrapper != null) {
			wrapper.responseBitmap = response;
			batchResponse(cacheKey, wrapper);
		}
	}

	/**
	 * Drain all bitmap request begin with a specified request sequence number.
	 * 
	 * @param sequenceNumber
	 *            the sequence number in bitmap request queue.
	 */
	public void drain(final int sequenceNumber) {
		mRequestQueue.cancelAll(new RequestFilter() {
			@Override
			public boolean apply(Request<?> request) {
				return request.getSequence() < sequenceNumber;
			}
		});
		List<String> wrappersToRemove = new ArrayList<String>();
		Iterator<String> i = mInFlightRequests.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			if (mInFlightRequests.get(key).request == null
					|| mInFlightRequests.get(key).request.getSequence() < sequenceNumber) {
				wrappersToRemove.add(key);
			}
		}
		Iterator<String> ri = wrappersToRemove.iterator();
		while (ri.hasNext()) {
			String key = ri.next();
			mInFlightRequests.remove(key);
		}
	}

	/**
	 * To handle the bitmap request.
	 * 
	 * @param requestUrl
	 * @param cacheKey
	 * @param defaultImage
	 * @param remoteRequestCreator
	 * @param bitmapLoadedHandler
	 * @return BitmapContainer The container to hold the bitmap response.
	 */
	private BitmapContainer get(String requestUrl, String cacheKey, Bitmap defaultImage,
			RemoteRequestCreator remoteRequestCreator, BitmapLoadedHandler bitmapLoadedHandler) {
		if (TextUtils.isEmpty(requestUrl)) {
			return new BitmapContainer(defaultImage, null, null, null);
		}
		Bitmap cachedBitmap = mCachedRemoteImages.get(cacheKey);
		if (cachedBitmap != null) {
			return new BitmapContainer(cachedBitmap, requestUrl, null, null);
		} else {
			BitmapContainer bitmapContainer = new BitmapContainer(defaultImage, requestUrl,
					cacheKey, bitmapLoadedHandler);
			RequestListenerWrapper wrapper = mInFlightRequests.get(cacheKey);
			if (wrapper != null) {
				wrapper.addHandler(bitmapContainer);
			} else {
				Request<?> newRequest = remoteRequestCreator.create();
				mRequestQueue.add(newRequest);
				mInFlightRequests.put(cacheKey, new RequestListenerWrapper(newRequest,
						bitmapContainer));
			}
			return bitmapContainer;
		}
	}

	/**
	 * Load a bitmap with the specified parameters, also you should implement a BitmapLoadedHandler
	 * to handle the bitmap response.
	 * 
	 * @param requestUrl
	 * @param defaultImage
	 * @param bitmapLoadedHandler
	 * @param maxWidth
	 * @param maxHeight
	 * @return BitmapContainer The container to hold the bitmap response.
	 */
	public BitmapContainer get(final String requestUrl, Bitmap defaultImage,
			BitmapLoadedHandler bitmapLoadedHandler, final int maxWidth, final int maxHeight,
			final int rotate, final boolean rounded) {
		final String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight);
		return get(requestUrl, cacheKey, defaultImage, new RemoteRequestCreator() {
			@Override
			public Request<Bitmap> create() {
				mTentativeGcRunner.onAllocatingSoon(2 * maxWidth * maxHeight);
				return new ImageRequest(requestUrl, maxWidth, maxHeight, rotate, rounded,
						new Response.Listener<Bitmap>() {
							@Override
							public void onResponse(Bitmap response) {
								onGetImageSuccess(cacheKey, response);
							}
						}, Bitmap.Config.RGB_565, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(EnergyError error) {
								onGetImageError(cacheKey);
							}
						});
			}
		}, bitmapLoadedHandler);
	}

	public BitmapContainer get(final Context context, final String requestUrl,
			Bitmap defaultImage, BitmapLoadedHandler bitmapLoadedHandler, final int maxWidth,
			final int maxHeight, final boolean installedPackage) {
		final String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight);
		return get(requestUrl, cacheKey, defaultImage, new RemoteRequestCreator() {
			@Override
			public Request<Bitmap> create() {
				mTentativeGcRunner.onAllocatingSoon(2 * maxWidth * maxHeight);
				return new PackageIconRequest(context, requestUrl,
						new Response.Listener<Bitmap>() {
							@Override
							public void onResponse(Bitmap response) {
								onGetImageSuccess(cacheKey, response);
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(EnergyError error) {
								onGetImageError(cacheKey);
							}
						}, installedPackage);
			}
		}, bitmapLoadedHandler);
	}

	/**
	 * The call back interface to handle the bitmap response.
	 * 
	 * @author wuss
	 * 
	 */
	public interface BitmapLoadedHandler extends Response.Listener<BitmapContainer> {

		@Override
		public void onResponse(BitmapContainer container);
	}

	/**
	 * The bitmap container to hold bitmap request info.
	 * 
	 * @author wuss
	 * 
	 */
	public class BitmapContainer {

		private Bitmap mBitmap;

		private BitmapLoadedHandler mBitmapLoaded;

		private String mCacheKey;

		private final String mRequestUrl;

		public BitmapContainer(Bitmap bitmap, String requestUrl, String cacheKey,
				BitmapLoadedHandler handler) {
			mBitmap = bitmap;
			mRequestUrl = requestUrl;
			mCacheKey = cacheKey;
			mBitmapLoaded = handler;
		}

		public void cancelRequest() {
			if (mBitmapLoaded == null) {
				return;
			}
			RequestListenerWrapper wrapper = mInFlightRequests.get(mCacheKey);
			if (wrapper != null) {
				boolean canceled = wrapper.removeHandlerAndCancelIfNecessary(this);
				if (canceled) {
					mInFlightRequests.remove(mCacheKey);
				}
			} else {
				wrapper = mBatchedResponses.get(mCacheKey);
				if (wrapper != null) {
					wrapper.removeHandlerAndCancelIfNecessary(this);
					if (wrapper.handlers.size() == 0) {
						mBatchedResponses.remove(mCacheKey);
					}
				}
			}
		}

		public Bitmap getBitmap() {
			return mBitmap;
		}

		public String getRequestUrl() {
			return mRequestUrl;
		}
	}

	class RequestListenerWrapper {

		private List<BitmapContainer> handlers;

		private Request<?> request;

		private Bitmap responseBitmap;

		public RequestListenerWrapper(Request<?> request, BitmapContainer bitmapContainer) {
			handlers = new ArrayList<BitmapContainer>();
			this.request = request;
			handlers.add(bitmapContainer);
		}

		public void addHandler(BitmapContainer container) {
			handlers.add(container);
		}

		public boolean removeHandlerAndCancelIfNecessary(BitmapContainer container) {
			handlers.remove(container);
			if (handlers.size() == 0) {
				request.cancel();
				return true;
			}
			return false;
		}
	}

	/**
	 * To create a remote bitmap request.
	 * 
	 * @author wuss
	 * 
	 */
	interface RemoteRequestCreator {
		/**
		 * create a bitmap request.
		 * 
		 * @return Request
		 */
		public Request<?> create();
	}

}
