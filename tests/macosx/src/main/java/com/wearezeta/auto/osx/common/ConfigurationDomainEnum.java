package com.wearezeta.auto.osx.common;

public enum ConfigurationDomainEnum {

	DEVELOPMENT("com.wearezeta.zclient.mac.development"), BETA(
			"com.wearezeta.zclient.mac.internal"), RELEASE(
					"com.wearezeta.zclient.mac");

	private String domain;

	private ConfigurationDomainEnum(String domainPath) {
		domain = domainPath;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public static String[] domainsList = new String[] {
		"com.wearezeta.zclient.mac.development",
		"com.wearezeta.zclient.mac.internal", "com.wearezeta.zclient.mac" };
}
