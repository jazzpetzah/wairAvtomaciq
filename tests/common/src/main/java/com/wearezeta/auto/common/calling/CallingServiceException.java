package com.wearezeta.auto.common.calling;

public class CallingServiceException extends Exception {

	private static final long serialVersionUID = -77484643126202998L;

	public CallingServiceException() {
		super();
	}

	public CallingServiceException(String message) {
		super(message);
	}

	public CallingServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public CallingServiceException(Throwable cause) {
		super(cause);
	}
}
