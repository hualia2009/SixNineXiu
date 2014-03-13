package com.network.tools.dispatch;

/**
 * An interface for a cache keyed by a String with a byte array as data.
 */
public interface Cache {
	/**
	 * Retrieves an entry from the cache.
	 * 
	 * @param key
	 *            Cache key
	 * @return An {@link Entry} or null in the event of a cache miss
	 */
	public Entry get(String key);

	/**
	 * Adds or replaces an entry to the cache.
	 * 
	 * @param key
	 *            Cache key
	 * @param entry
	 *            Data to store and metadata for cache coherency, TTL, etc.
	 */
	public void put(String key, Entry entry);

	/**
	 * Performs any potentially long-running actions needed to initialize the
	 * cache; will be called from a worker thread.
	 */
	public void initialize();

	/**
	 * Removes an entry from the cache.
	 * 
	 * @param key
	 *            Cache key
	 */
	public void remove(String key);

	/**
	 * Empties the cache.
	 */
	public void clear();

	/**
	 * Data and metadata for an entry returned by the cache.
	 */
	public static class Entry {
		/** The data returned from cache. */
		public byte[] data;
	}

}
