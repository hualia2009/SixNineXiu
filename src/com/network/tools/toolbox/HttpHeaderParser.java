/**
 * 
 */
package com.network.tools.toolbox;

import java.util.Map;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.protocol.HTTP;

import com.network.tools.dispatch.Cache;
import com.network.tools.dispatch.NetworkResponse;

/**
 * The helper class used to parse HTTP Header.
 */
public class HttpHeaderParser {

	public static Cache.Entry parseCacheHeaders(NetworkResponse response) {
		long now = System.currentTimeMillis();

		Map<String, String> headers = response.headers;

		long serverDate = 0;
		long serverExpires = 0;
		long softExpire = 0;
		long maxAge = 0;
		boolean hasCacheControl = false;

		String serverEtag = null;
		String headerValue;

		headerValue = headers.get("Date");
		if (headerValue != null) {
			serverDate = parseDateAsEpoch(headerValue);
		}

		headerValue = headers.get("Cache-Control");
		VolleyLog.v("HTTP header (Cache-Control) %s -->", headerValue);

		if (headerValue != null) {
			hasCacheControl = true;
			String[] tokens = headerValue.split(",");
			for (int i = 0; i < tokens.length; i++) {
				String token = tokens[i].trim();
				if (token.equals("no-cache") || token.equals("no-store")) {
					return null;
				} else if (token.startsWith("max-age=")) {
					try {
						maxAge = Long.parseLong(token.substring(8));
					} catch (Exception e) {
					}
				} else if (token.equals("must-revalidate")
						|| token.equals("proxy-revalidate")) {
					maxAge = 0;
				}
			}
		}

		headerValue = headers.get("Expires");
		if (headerValue != null) {
			serverExpires = parseDateAsEpoch(headerValue);
		}

		serverEtag = headers.get("ETag");

		// Cache-Control takes precedence over an Expires header, even if both
		// exist and Expires
		// is more restrictive.
		if (hasCacheControl) {
			softExpire = now + maxAge * 1000;
		} else if (serverDate > 0 && serverExpires >= serverDate) {
			// Default semantic for Expire header in HTTP specification is
			// softExpire.
			softExpire = now + (serverExpires - serverDate);
		}
		VolleyLog
				.v("HTTP header (serverDate) %d --> (serverExpires) %d --> (softExpire) %d --> (serverEtag) %s",
						serverDate, serverExpires, softExpire, serverEtag);

		Cache.Entry entry = new Cache.Entry();
		entry.data = response.data;
		return entry;
	}

	/**
	 * Returns the charset specified in the Content-Type of this header, or the
	 * HTTP default (ISO-8859-1) if none can be found.
	 */
	public static String parseCharset(Map<String, String> headers) {
		String contentType = headers.get(HTTP.CONTENT_TYPE);
		if (contentType != null) {
			String[] params = contentType.split(";");
			for (int i = 1; i < params.length; i++) {
				String[] pair = params[i].trim().split("=");
				if (pair.length == 2) {
					if (pair[0].equals("charset")) {
						return pair[1];
					}
				}
			}
		}

		return HTTP.DEFAULT_CONTENT_CHARSET;
	}

	public static long parseDateAsEpoch(String dateStr) {
		try {
			return DateUtils.parseDate(dateStr).getTime();
		} catch (DateParseException e) {
			return 0;
		}
	}

}
