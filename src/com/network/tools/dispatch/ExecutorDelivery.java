package com.network.tools.dispatch;

import java.util.concurrent.Executor;

import android.os.Handler;

/**
 * The class used to deliver response.
 */
public class ExecutorDelivery {

	private final Executor mResponsePoster;

	private int mDiscardBefore = 0;

	public ExecutorDelivery(final Handler handler) {
		mResponsePoster = new Executor() {
			@Override
			public void execute(Runnable command) {
				handler.post(command);
			}
		};
	}

	public void discardBefore(int sequence) {
		mDiscardBefore = sequence;
	}

	public void postError(Request<?> request, EnergyError error) {
		request.addMarker("post-error");
		Response<?> response = Response.error(error);
		mResponsePoster.execute(new ResponseDeliveryRunnable(request, response,
				null));
	}

	public void postResponse(Request<?> request, Response<?> response) {
		postResponse(request, response, null);
	}

	public void postResponse(Request<?> request, Response<?> response,
			Runnable runnable) {
		request.markDelivered();
		request.addMarker("post-response");
		mResponsePoster.execute(new ResponseDeliveryRunnable(request, response,
				runnable));
	}

	@SuppressWarnings("rawtypes")
	class ResponseDeliveryRunnable implements Runnable {

		private final Request mRequest;

		private final Response mResponse;

		private final Runnable mRunnable;

		public ResponseDeliveryRunnable(Request request, Response response,
				Runnable runnable) {
			mRequest = request;
			mResponse = response;
			mRunnable = runnable;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			boolean drained = mRequest.isDrainable()
					&& (mRequest.getSequence() < mDiscardBefore);

			// If this request has been drained or canceled, finish it and don't
			// deliver.
			if (drained || mRequest.isCanceled()) {
				mRequest.finish("canceled-at-delivery");
				return;
			}

			// Deliver a normal response or error, depending.
			if (mResponse.isSuccess()) {
				mRequest.deliverResponse(mResponse.result);
			} else {
				mRequest.deliverError(mResponse.error);
			}
			mRequest.finish("done");

			// If we have been provided a post-delivery runnable, run it.
			if (mRunnable != null) {
				mRunnable.run();
			}
		}
	}
}
