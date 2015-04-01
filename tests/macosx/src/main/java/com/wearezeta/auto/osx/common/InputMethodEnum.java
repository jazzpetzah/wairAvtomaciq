package com.wearezeta.auto.osx.common;

public enum InputMethodEnum {

	SEND_KEYS("sending keys"), APPLE_SCRIPT("AppleScript");

	private String method;

	private InputMethodEnum(String method) {
		this.method = method;
	}

	public String getMethod() {
		return method;
	}
}
