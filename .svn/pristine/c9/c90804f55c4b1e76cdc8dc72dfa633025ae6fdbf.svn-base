package com.network.tools.toolbox;

import java.util.concurrent.ThreadFactory;

import android.os.HandlerThread;

public class BackgroundThreadFactory implements ThreadFactory {

	public static HandlerThread createHandlerThread(String name) {
		return new HandlerThread(name, android.os.Process.THREAD_PRIORITY_BACKGROUND);
	}

	public static Thread createThread(Runnable r) {
		return new Thread(wrap(r));
	}

	public static Thread createThread(String name, Runnable r) {
		return new Thread(wrap(r), name);
	}

	private static Runnable wrap(final Runnable r) {
		return new Runnable() {
			@Override
			public void run() {
				android.os.Process
						.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
				r.run();
			}
		};
	}

	@Override
	public Thread newThread(Runnable r) {
		return createThread(r);
	}

}
