package com.wearezeta.auto.common.rest;

public class RESTError extends Exception {
	private static final long serialVersionUID = -6815876370819026043L;
	private int returnCode = -1;

	public int getReturnCode() {
		return this.returnCode;
	}

	public RESTError(String message, int returnCode) {
		super(message);
		this.returnCode = returnCode;
	}
}
