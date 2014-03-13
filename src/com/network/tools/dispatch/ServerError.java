/**
 * 
 */
package com.network.tools.dispatch;

public class ServerError extends EnergyError {

	/**
     * 
     */
	private static final long serialVersionUID = 1512752966679297938L;

	public ServerError() {
		super();
	}

	public ServerError(NetworkResponse networkResponse) {
		super(networkResponse);
	}

}
