package com.wearezeta.auto.common.misc;

public class BuildVersionInfo {
	private String clientBuildNumber;
	private String zmessagingBuildNumber;
	
	public BuildVersionInfo() {
		this.clientBuildNumber = "no info";
		this.zmessagingBuildNumber = "no info";
	}
	
	
	public BuildVersionInfo(String client, String zmessaging) {
		this.clientBuildNumber = client;
		this.zmessagingBuildNumber = zmessaging;
	}
	
	public String toString() {
		return "ZClient - " + clientBuildNumber + " (zmessaging - " + zmessagingBuildNumber + ")";
	}
}
