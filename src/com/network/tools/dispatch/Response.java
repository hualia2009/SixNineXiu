package com.network.tools.dispatch;

/**
 * Encapsulates a parsed response for delivery.
 * 
 * @param <T>
 *            Parsed type of this response
 */
public class Response<T> {

	public final T result;

	public final EnergyError error;

	public final Cache.Entry cacheEntry;

	private Response(T result, Cache.Entry cacheEntry) {
		this.cacheEntry = cacheEntry;
		this.result = result;
		this.error = null;
	}

	private Response(EnergyError error) {
		this.cacheEntry = null;
		this.result = null;
		this.error = error;
	}

	/**
	 * Returns a failed response containing the given error code and an optional
	 * localized message displayed to the user.
	 */
	public static <T> Response<T> error(EnergyError error) {
		return new Response<T>(error);
	}

	/** Returns a successful response containing the parsed result. */
	public static <T> Response<T> success(T result, Cache.Entry cacheEntry) {
		return new Response<T>(result, cacheEntry);
	}

	/**
	 * Returns whether this response is considered successful.
	 */
	public boolean isSuccess() {
		return error == null;
	}

	/**
	 * Callback interface for delivering parsed responses.
	 * 
	 * @param <T>
	 */
	public interface Listener<T> {
		/** Called when a response is received. */
		public void onResponse(T response);

	}

	/**
	 * Callback interface for delivering error responses.
	 * 
	 * @param <T>
	 */
	public interface ErrorListener {
		/**
		 * Callback method that an error has been occurred with the provided
		 * error code and optional user-readable message.
		 */
		public void onErrorResponse(EnergyError error);
	}
}
