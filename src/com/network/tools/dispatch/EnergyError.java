package com.network.tools.dispatch;

/**
 * The Exception class to handle Exception.
 */
public class EnergyError extends Exception {

	/**
     * 
     */
	private static final long serialVersionUID = 6275348424582021048L;

	public final NetworkResponse networkResponse;

	public EnergyError(NetworkResponse response) {
		super();
		networkResponse = response;
	}

	public EnergyError() {
		super();
		networkResponse = null;
	}

	public EnergyError(String exceptionMessage, Throwable reason) {
		super(exceptionMessage, reason);
		networkResponse = null;
	}

	public EnergyError(String exceptionMessage) {
		super(exceptionMessage);
		networkResponse = null;
	}

	public EnergyError(Throwable cause) {
		super(cause);
		networkResponse = null;
	}
}
