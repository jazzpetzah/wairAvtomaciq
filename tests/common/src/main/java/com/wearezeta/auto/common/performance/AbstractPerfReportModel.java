package com.wearezeta.auto.common.performance;


import java.util.NoSuchElementException;

import org.json.JSONObject;

import com.wearezeta.auto.common.misc.ClientDeviceInfo;

public abstract class AbstractPerfReportModel {
	protected static final String UNKNOWN_VALUE = "Unknown";

	/**
	 * All times are in milliseconds
	 */
	private String deviceName = UNKNOWN_VALUE;
	private String deviceOSVersion = UNKNOWN_VALUE;
	private NetworkType networkType;
	private String buildNumber = UNKNOWN_VALUE;

	public String getDeviceOSVersion() {
		return deviceOSVersion;
	}

	protected void setDeviceOSVersion(String deviceOSVersion) {
		this.deviceOSVersion = deviceOSVersion;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	protected void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getDeviceName() {
		return deviceName;
	}

	protected void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public NetworkType getNetworkType() {
		return networkType;
	}

	protected void setNetworkType(NetworkType networkType) {
		this.networkType = networkType;
	}

	public enum NetworkType {
		WiFi("WiFi"), FourG("4G");

		private final String internalName;

		private NetworkType(String internalName) {
			this.internalName = internalName;
		}

		public static NetworkType fromString(String str) {
			for (NetworkType item : NetworkType.values()) {
				if (str.equalsIgnoreCase(item.toString())) {
					return item;
				}
			}

			throw new NoSuchElementException(String.format(
					"No such network type type available: %s", str));
		}

		@Override
		public String toString() {
			return this.internalName;
		}
	}

	public AbstractPerfReportModel() {
		// Keep this empty constructor for serialization purposes
	}

	public JSONObject asJSON() {
		final JSONObject result = new JSONObject();
		result.put("deviceName", this.getDeviceName());
		result.put("deviceOSVersion", this.getDeviceOSVersion());
		result.put("networkType", this.getNetworkType().toString());
		result.put("buildNumber", this.getBuildNumber());
		return result;
	}

	protected void loadFromJSON(final JSONObject jsonObj) throws Exception {
		this.setDeviceName(jsonObj.getString("deviceName"));
		this.setDeviceOSVersion(jsonObj.getString("deviceOSVersion"));
		this.setNetworkType(NetworkType.fromString(jsonObj
				.getString("networkType")));
		this.setBuildNumber(jsonObj.getString("buildNumber"));
	}

	protected void loadValuesFromDeviceInfo(ClientDeviceInfo deviceInfo) {
		// FIXME: handle other network types
		this.setNetworkType(deviceInfo.isWifiEnabled() ? NetworkType.WiFi
				: NetworkType.FourG);
		this.setDeviceName(deviceInfo.getDeviceName());
		this.setDeviceOSVersion(deviceInfo.getOperatingSystemBuild());
	}
}
