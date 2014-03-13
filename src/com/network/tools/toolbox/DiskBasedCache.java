/**
 * 
 */
package com.network.tools.toolbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.os.SystemClock;

import com.network.tools.dispatch.Cache;

/**
 * Cache implementation that caches files directly onto the hard disk in the
 * specified directory. The default disk usage size is 5MB, but is configurable.
 */
public class DiskBasedCache implements Cache {

	/** Map of the Key, CacheHeader pairs */
	private final Map<String, CacheHeader> mEntries = new LinkedHashMap<String, CacheHeader>(
			16, .75f, true);

	/** Total amount of space currently used by the cache in bytes. */
	private long mTotalSize = 0;

	/** The root directory to use for the cache. */
	private final File mRootDirectory;

	/** The maximum size of the cache in bytes. */
	private final int mMaxCacheSizeInBytes;

	/** Default maximum disk usage in bytes. */
	private static final int DEFAULT_DISK_USAGE_BYTES = 5 * 1024 * 1024;

	/** High water mark percentage for the cache */
	private static final float HYSTERESIS_FACTOR = 0.9f;

	/** Current cache version */
	private static final int CACHE_VERSION = 1;

	/**
	 * Constructs an instance of the DiskBasedCache at the specified directory.
	 * 
	 * @param rootDirectory
	 *            The root directory of the cache.
	 * @param maxCacheSizeInBytes
	 *            The maximum size of the cache in bytes.
	 */
	public DiskBasedCache(File rootDirectory, int maxCacheSizeInBytes) {
		mRootDirectory = rootDirectory;
		mMaxCacheSizeInBytes = maxCacheSizeInBytes;
	}

	/**
	 * Constructs an instance of the DiskBasedCache at the specified directory
	 * using the default maximum cache size of 5MB.
	 * 
	 * @param rootDirectory
	 *            The root directory of the cache.
	 */
	public DiskBasedCache(File rootDirectory) {
		this(rootDirectory, DEFAULT_DISK_USAGE_BYTES);
	}

	/**
	 * Clears the cache. Deletes all cached files from disk.
	 */
	@Override
	public synchronized void clear() {
		File[] files = mRootDirectory.listFiles();
		if (files != null) {
			for (File file : files) {
				file.delete();
			}
		}
		mEntries.clear();
		mTotalSize = 0;
		VolleyLog.d("Cache cleared.");
	}

	/**
	 * Returns the cache entry with the specified key if it exists, null
	 * otherwise.
	 */
	@Override
	public synchronized Entry get(String key) {
		CacheHeader entry = mEntries.get(key);
		// if the entry does not exist, return.
		if (entry == null) {
			return null;
		}

		File file = getFileForKey(key);
		CountingInputStream cis = null;
		try {
			cis = new CountingInputStream(new FileInputStream(file));
			CacheHeader.readHeader(cis); // eat header
			byte[] data = streamToBytes(cis,
					(int) (file.length() - cis.bytesRead));
			return entry.toCacheEntry(data);
		} catch (IOException e) {
			VolleyLog.d("%s: %s", file.getAbsolutePath(), e.toString());
			remove(key);
			return null;
		} finally {
			if (cis != null) {
				try {
					cis.close();
				} catch (IOException ioe) {
					return null;
				}
			}
		}
	}

	/**
	 * Initializes the DiskBasedCache by scanning for all files currently in the
	 * specified root directory.
	 */
	@Override
	public synchronized void initialize() {
		File[] files = mRootDirectory.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				CacheHeader entry = CacheHeader.readHeader(fis);
				entry.size = file.length();
				putEntry(entry.key, entry);
			} catch (IOException e) {
				if (file != null) {
					file.delete();
				}
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException ignored) {
				}
			}
		}
	}

	/**
	 * Puts the entry with the specified key into the cache.
	 */
	@Override
	public synchronized void put(String key, Entry entry) {
		pruneIfNeeded(entry.data.length);
		File file = getFileForKey(key);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			CacheHeader e = new CacheHeader(key, entry);
			e.writeHeader(fos);
			fos.write(entry.data);
			fos.close();
			putEntry(key, e);
			return;
		} catch (IOException e) {
		}
		boolean deleted = file.delete();
		if (!deleted) {
			VolleyLog.d("Could not clean up file %s",
					file.getAbsolutePath());
		}
	}

	/**
	 * Removes the specified key from the cache if it exists.
	 */
	@Override
	public synchronized void remove(String key) {
		boolean deleted = getFileForKey(key).delete();
		removeEntry(key);
		if (!deleted) {
			VolleyLog.d(
					"Could not delete cache entry for key=%s, filename=%s",
					key, getFilenameForKey(key));
		}
	}

	/**
	 * Creates a pseudo-unique filename for the specified cache key.
	 * 
	 * @param key
	 *            The key to generate a file name for.
	 * @return A pseudo-unique filename.
	 */
	private String getFilenameForKey(String key) {
		int firstHalfLength = key.length() / 2;
		String localFilename = String.valueOf(key.substring(0, firstHalfLength)
				.hashCode());
		localFilename += String.valueOf(key.substring(firstHalfLength)
				.hashCode());
		return localFilename;
	}

	/**
	 * Returns a file object for the given cache key.
	 */
	public File getFileForKey(String key) {
		return new File(mRootDirectory, getFilenameForKey(key));
	}

	/**
	 * Prunes the cache to fit the amount of bytes specified.
	 * 
	 * @param neededSpace
	 *            The amount of bytes we are trying to fit into the cache.
	 */
	private void pruneIfNeeded(int neededSpace) {
		if ((mTotalSize + neededSpace) < mMaxCacheSizeInBytes) {
			return;
		}
		if (VolleyLog.DEBUG) {
			VolleyLog.v("Pruning old cache entries.");
		}

		long before = mTotalSize;
		int prunedFiles = 0;
		long startTime = SystemClock.elapsedRealtime();

		Iterator<Map.Entry<String, CacheHeader>> iterator = mEntries.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, CacheHeader> entry = iterator.next();
			CacheHeader e = entry.getValue();
			boolean deleted = getFileForKey(e.key).delete();
			if (deleted) {
				mTotalSize -= e.size;
			} else {
				VolleyLog.d(
						"Could not delete cache entry for key=%s, filename=%s",
						e.key, getFilenameForKey(e.key));
			}
			iterator.remove();
			prunedFiles++;

			if ((mTotalSize + neededSpace) < mMaxCacheSizeInBytes
					* HYSTERESIS_FACTOR) {
				break;
			}
		}

		if (VolleyLog.DEBUG) {
			VolleyLog.v("pruned %d files, %d bytes, %d ms", prunedFiles,
					(mTotalSize - before), SystemClock.elapsedRealtime()
							- startTime);
		}
	}

	/**
	 * Puts the entry with the specified key into the cache.
	 * 
	 * @param key
	 *            The key to identify the entry by.
	 * @param entry
	 *            The entry to cache.
	 */
	private void putEntry(String key, CacheHeader entry) {
		if (!mEntries.containsKey(key)) {
			mTotalSize += entry.size;
		} else {
			CacheHeader oldEntry = mEntries.get(key);
			mTotalSize += (entry.size - oldEntry.size);
		}
		mEntries.put(key, entry);
	}

	/**
	 * Removes the entry identified by 'key' from the cache.
	 */
	private void removeEntry(String key) {
		CacheHeader entry = mEntries.get(key);
		if (entry != null) {
			mTotalSize -= entry.size;
			mEntries.remove(key);
		}
	}

	/**
	 * Reads the contents of an InputStream into a byte[].
	 * */
	private static byte[] streamToBytes(InputStream in, int length)
			throws IOException {
		byte[] bytes = new byte[length];
		int count;
		int pos = 0;
		while (pos < length
				&& ((count = in.read(bytes, pos, length - pos)) != -1)) {
			pos += count;
		}
		if (pos != length) {
			throw new IOException("Expected " + length + " bytes, read " + pos
					+ " bytes");
		}
		return bytes;
	}

	/**
	 * Handles holding onto the cache headers for an entry.
	 */
	private static class CacheHeader {

		public String key;

		public long size;

		private CacheHeader() {
		}

		public CacheHeader(String key, Cache.Entry entry) {
			this.key = key;
			this.size = entry.data.length;
		}

		public static CacheHeader readHeader(InputStream is) throws IOException {
			CacheHeader entry = new CacheHeader();
			ObjectInputStream ois = new ObjectInputStream(is);
			int version = ois.readByte();
			if (version != CACHE_VERSION) {
				throw new IOException();
			}
			entry.key = ois.readUTF();
			return entry;
		}

		public Cache.Entry toCacheEntry(byte[] data) {
			Cache.Entry e = new Cache.Entry();
			e.data = data;
			return e;
		}

		public boolean writeHeader(OutputStream os) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeByte(CACHE_VERSION);
				oos.writeUTF(key);
				oos.flush();
				return true;
			} catch (IOException e) {
				VolleyLog.d("%s", new Object[] { e.toString() });
				return false;
			}
		}
	}

	private static class CountingInputStream extends FilterInputStream {
		private int bytesRead = 0;

		private CountingInputStream(InputStream in) {
			super(in);
		}

		@Override
		public int read() throws IOException {
			int result = super.read();
			if (result != -1) {
				bytesRead++;
			}
			return result;
		}

		@Override
		public int read(byte[] buffer, int offset, int count)
				throws IOException {
			int result = super.read(buffer, offset, count);
			if (result != -1) {
				bytesRead += result;
			}
			return result;
		}
	}
}
