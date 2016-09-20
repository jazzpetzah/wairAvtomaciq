package com.wearezeta.auto.common.driver.facebook_ios_driver;

import com.wearezeta.auto.common.rest.RESTError;

public class FBDriverAPIException extends RESTError {

	public FBDriverAPIException(String message, int returnCode) {
		super(message, returnCode);
	}

	private static final long serialVersionUID = -77484643126202998L;
}
