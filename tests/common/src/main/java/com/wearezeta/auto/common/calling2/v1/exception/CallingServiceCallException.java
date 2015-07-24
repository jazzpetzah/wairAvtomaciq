package com.wearezeta.auto.common.calling2.v1.exception;

import com.wearezeta.auto.common.rest.RESTError;

public class CallingServiceCallException extends RESTError {

	public CallingServiceCallException(String message, int returnCode) {
		super(message, returnCode);
	}

	public CallingServiceCallException(RESTError exception) {
		super(exception.getMessage(), exception.getReturnCode());
	}

}
