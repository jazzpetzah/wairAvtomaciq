package com.wearezeta.auto.common.localytics;

public final class LocalyticsToken {
	private String key;
	private String secret;

	public LocalyticsToken(String key, String secret) {
		this.key = key;
		this.secret = secret;
	}

	public String getKey() {
		return key;
	}

	public String getSecret() {
		return secret;
	}
}
