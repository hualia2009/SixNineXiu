package com.network.tools.toolbox;

import android.os.Handler;
import android.os.HandlerThread;

public class TentativeGcRunner {

	private int mAllocatedSinceLastRun;

	private boolean mEnabled;

	private Runnable mGcRunnable;

	private Handler mHandler;

	private HandlerThread mHandlerThread;

	private static final int MAX_ALLOCATED_SIZE_IN_BYTES = 6 * 1024 * 1024;

	public TentativeGcRunner() {
		mGcRunnable = new Runnable() {
			@Override
			public void run() {
				System.gc();
			}
		};
		int processorCount = Runtime.getRuntime().availableProcessors();
		VolleyLog.wtf("Processor count [%d].", processorCount);
		// if (android.os.Build.VERSION.SDK_INT >= 11 && processorCount > 1) {
		mEnabled = true;
		// }
		if (mEnabled) {
			mHandlerThread = BackgroundThreadFactory.createHandlerThread("tentative-gc-runner");
			mHandlerThread.start();
			mHandler = new Handler(mHandlerThread.getLooper());
		}
	}

	public void onAllocatingSoon(int size) {
		if (!mEnabled) {
			return;
		}
		mAllocatedSinceLastRun = mAllocatedSinceLastRun + size;
		if (size > 0x14000 && mAllocatedSinceLastRun > MAX_ALLOCATED_SIZE_IN_BYTES) {
			mHandler.post(mGcRunnable);
			VolleyLog.wtf("Runs the garbage collector [%d] > [%d]--> [%d] > [%d]", size, 0x14000,
					mAllocatedSinceLastRun, MAX_ALLOCATED_SIZE_IN_BYTES);
			mAllocatedSinceLastRun = 0;
		}
	}
}
