package com.wearezeta.auto.osx.common;

public enum SearchResultTypeEnum {
	CONNECTION("connected"), GROUP("group"), NOT_CONNECTED("not connected"), BLOCKED(
			"blocked");

	private String state;

	private SearchResultTypeEnum(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public static SearchResultTypeEnum getTypeByState(String state) {
		if (state.isEmpty()) {
			return CONNECTION;
		}
		for (SearchResultTypeEnum value : SearchResultTypeEnum.values()) {
			if (value.getState().equals(state)) {
				return value;
			}
		}
		return null;
	}
}
