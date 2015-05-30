package com.wearezeta.auto.common.calling;

import com.wearezeta.auto.common.rest.RESTError;

public class CallingServiceException extends RESTError {

	public CallingServiceException(String message, int returnCode) {
		super(message, returnCode);
	}

	private static final long serialVersionUID = -77484643126202998L;

}
