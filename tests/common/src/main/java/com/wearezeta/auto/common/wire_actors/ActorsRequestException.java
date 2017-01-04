package com.wearezeta.auto.common.wire_actors;

import com.wearezeta.auto.common.rest.RESTError;

public class ActorsRequestException extends RESTError {
	public ActorsRequestException(String message, int returnCode) {
		super(message, returnCode);
	}
}
