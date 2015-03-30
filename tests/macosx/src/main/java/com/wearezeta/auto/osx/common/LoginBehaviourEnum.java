package com.wearezeta.auto.osx.common;

public enum LoginBehaviourEnum {
	SUCCESSFUL("successful login"), ERROR("error"), NO_INTERNET(
			"no internet message");

	private String result;

	private LoginBehaviourEnum(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}
}
