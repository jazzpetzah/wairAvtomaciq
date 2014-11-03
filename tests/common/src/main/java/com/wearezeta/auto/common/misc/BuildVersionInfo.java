package com.wearezeta.auto.common.misc;

public class BuildVersionInfo {
	private String clientBuildNumber;
	private String zmessagingBuildNumber;
	
	public BuildVersionInfo() {
		this.setClientBuildNumber("no info");
		this.zmessagingBuildNumber = "no info";
	}
	
	
	public BuildVersionInfo(String client, String zmessaging) {
		this.setClientBuildNumber(client);
		this.zmessagingBuildNumber = zmessaging;
	}
	
	public String toString() {
		return "ZClient - " + getClientBuildNumber() + " (zmessaging - " + zmessagingBuildNumber + ")";
	}


	public String getClientBuildNumber() {
		return clientBuildNumber;
	}


	public void setClientBuildNumber(String clientBuildNumber) {
		this.clientBuildNumber = clientBuildNumber;
	}
}
