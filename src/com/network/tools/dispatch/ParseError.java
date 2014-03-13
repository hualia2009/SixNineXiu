/**
 * 
 */
package com.network.tools.dispatch;

public class ParseError extends EnergyError {

	/**
     * 
     */
	private static final long serialVersionUID = 3213293417553408697L;

	public ParseError() {
		super();
	}

	public ParseError(NetworkResponse networkResponse) {
		super(networkResponse);
	}

	public ParseError(Throwable cause) {
		super(cause);
	}
}
