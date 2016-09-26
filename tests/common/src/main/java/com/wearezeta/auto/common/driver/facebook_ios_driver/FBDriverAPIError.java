package com.wearezeta.auto.common.driver.facebook_ios_driver;

import com.wearezeta.auto.common.rest.RESTError;

public class FBDriverAPIError extends RESTError {

	public FBDriverAPIError(String message, int returnCode) {
		super(message, returnCode);
	}

	public FBDriverAPIError(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -77484643126202998L;
}
