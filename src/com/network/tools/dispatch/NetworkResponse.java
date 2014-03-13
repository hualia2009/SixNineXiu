package com.network.tools.dispatch;

import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpStatus;

/**
 * To wrap the network response.
 */
public class NetworkResponse {

	public final byte[] data;

	public final boolean notModified;

	public final int statusCode;

	public final Map<String, String> headers;

	public NetworkResponse(int statusCode, byte[] data,
			Map<String, String> headers, boolean notModified) {
		this.statusCode = statusCode;
		this.data = data;
		this.headers = headers;
		this.notModified = notModified;
	}

	public NetworkResponse(byte[] data) {
		this(HttpStatus.SC_OK, data, Collections.<String, String> emptyMap(),
				false);
	}
}
