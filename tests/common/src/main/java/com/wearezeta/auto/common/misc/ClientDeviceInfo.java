package com.wearezeta.auto.common.misc;

public class ClientDeviceInfo {
	private String operatingSystemName;
	private String operatingSystemBuild;
	private String deviceName;
	private String gsmNetworkType;
	private String isWifiEnabled;
	
	public ClientDeviceInfo() {
		this.operatingSystemName = "no info";
		this.operatingSystemBuild = "no info";
		this.deviceName = "no info";
		this.gsmNetworkType = "no info";
		this.isWifiEnabled = "no info";
	}
	
	
	public ClientDeviceInfo(String os, String osBuild, String device, String carrier, String isWifi) {
		this.operatingSystemName = os;
		this.operatingSystemBuild = osBuild;
		this.deviceName = device;
		this.gsmNetworkType = carrier;
		this.isWifiEnabled = isWifi;
	}
	
	public String toString() {
		return String.format("OS: %s %s, Device: %s, Network: %s (Wifi: %s)", operatingSystemName, operatingSystemBuild, deviceName, gsmNetworkType, isWifiEnabled);
	}
}
