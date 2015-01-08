package com.wearezeta.auto.common.backend;

public final class AuthToken {
	private String value;

	public String getValue() {
		return this.value;
	}

	public String getValueOrThrowError() throws AuthTokenIsNotSetException {
		if (this.value == null) {
			throw new AuthTokenIsNotSetException(
					"Auth token should be set (make sure the user is logged in)!");
		}
		return this.value;
	}

	public static class AuthTokenIsNotSetException extends Exception {
		private static final long serialVersionUID = 5591320171316399233L;

		public AuthTokenIsNotSetException(String msg) {
			super(msg);
		}
	}

	private String type;

	public String getType() {
		return this.type;
	}

	public AuthToken(String type, String value) {
		this.type = type;
		this.value = value;
	}
}
