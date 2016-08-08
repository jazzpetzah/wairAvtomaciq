package com.wearezeta.auto.ios.tools.ABProvisioner;

import com.wearezeta.auto.common.rest.RESTError;

public class ABProvisionerAPIException extends RESTError {

	public ABProvisionerAPIException(String message, int returnCode) {
		super(message, returnCode);
	}

	private static final long serialVersionUID = -77484643126202998L;
}
