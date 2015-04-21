package com.wearezeta.auto.common.calling;

public class CallingServiceException extends Exception {

	private static final long serialVersionUID = -77484643126202998L;

	private int returnCode = -1;

	public int getReturnCode() {
		return this.returnCode;
	}

	public CallingServiceException(String message, int returnCode) {
		super(message);
		this.returnCode = returnCode;
	}
}
