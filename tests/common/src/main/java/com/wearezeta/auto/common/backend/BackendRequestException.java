package com.wearezeta.auto.common.backend;

import com.wearezeta.auto.common.rest.RESTError;

public class BackendRequestException extends RESTError {
	private static final long serialVersionUID = -5694123643050116766L;

	public BackendRequestException(String message, int returnCode) {
		super(message, returnCode);
	}
}
