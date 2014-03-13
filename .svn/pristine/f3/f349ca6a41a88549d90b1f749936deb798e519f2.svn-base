package com.network.tools.dispatch;

import android.content.Intent;

public class AuthFailureError extends EnergyError {

	/**
     * 
     */
	private static final long serialVersionUID = -1696186313584368406L;

	private Intent mResolutionIntent;

	public AuthFailureError() {
		super();
	}

	public AuthFailureError(NetworkResponse response) {
		super(response);
	}

	public AuthFailureError(String message) {
		super(message);
	}

	public AuthFailureError(Intent intent) {
		super();
		mResolutionIntent = intent;
	}

	public AuthFailureError(String message, Throwable reason) {
		super(message, reason);
	}

	public Intent getResolutionIntent() {
		return mResolutionIntent;
	}

	public String getMessage() {
		if (mResolutionIntent != null) {
			return "User needs to (re)enter credentials.";
		}
		return super.getMessage();
	}
}
