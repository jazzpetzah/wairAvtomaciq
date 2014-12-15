package com.wearezeta.auto.common;

public class BackendRequestException extends Exception {
	private static final long serialVersionUID = 3106861319171268398L;
	
	private int returnCode = -1;
	
	public int getReturnCode() {
		return this.returnCode;
	}

	public BackendRequestException(String message) {
		super(message);
	}
	
	public BackendRequestException(String message, int returnCode) {
		super(message);
		this.returnCode = returnCode;
	}
}
