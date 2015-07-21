package com.wearezeta.auto.common.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class PerfReportModel {
	private static final String UNKNOWN_VALUE = "Unknown";

	private String deviceName = UNKNOWN_VALUE;
	private String deviceOSVersion = UNKNOWN_VALUE;
	private NetworkType networkType;
	private long appStartupTimeMillis;
	private int usersCount;
	private String buildNumber = UNKNOWN_VALUE;
	private long signInTime;
	private List<Long> convoLoadTimes = new ArrayList<>();

	public String getDeviceOSVersion() {
		return deviceOSVersion;
	}

	protected void setDeviceOSVersion(String deviceOSVersion) {
		this.deviceOSVersion = deviceOSVersion;
	}

	public long getSignInTime() {
		return signInTime;
	}

	protected void setSignInTime(long signInTime) {
		this.signInTime = signInTime;
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

	public long getAppStartupTimeMillis() {
		return appStartupTimeMillis;
	}

	protected void setAppStartupTimeMillis(long appStartupTimeMillis) {
		this.appStartupTimeMillis = appStartupTimeMillis;
	}

	public int getUsersCount() {
		return usersCount;
	}

	protected void setUsersCount(int usersCount) {
		this.usersCount = usersCount;
	}

	protected void addConvoStartupTime(long timeMillis) {
		this.convoLoadTimes.add(timeMillis);
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

	public PerfReportModel() {
		// Keep this empty constructor for serialization purposes
	}

	public long getConvoLoadAverageTime() {
		if (this.convoLoadTimes.size() > 0) {
			return this.convoLoadTimes.stream().reduce(0L,
					(accumulator, _item) -> Math.addExact(accumulator, _item))
					/ this.convoLoadTimes.size();
		} else {
			return 0;
		}
	}

	public JSONObject asJSON() {
		final JSONObject result = new JSONObject();
		result.put("deviceName", this.getDeviceName());
		result.put("deviceOSVersion", this.getDeviceOSVersion());
		result.put("networkType", this.getNetworkType().toString());
		result.put("appStartupTimeMillis", this.getAppStartupTimeMillis());
		result.put("usersCount", this.getUsersCount());
		result.put("buildNumber", this.getBuildNumber());
		result.put("signInTime", this.getSignInTime());
		result.put("convoLoadTimes", new JSONArray(this.convoLoadTimes));
		return result;
	}

	protected void loadFromJSON(final JSONObject jsonObj) throws Exception {
		this.setDeviceName(jsonObj.getString("deviceName"));
		this.setDeviceOSVersion(jsonObj.getString("deviceOSVersion"));
		this.setNetworkType(NetworkType.fromString(jsonObj
				.getString("networkType")));
		this.setAppStartupTimeMillis(jsonObj.getLong("appStartupTimeMillis"));
		this.setUsersCount(jsonObj.getInt("usersCount"));
		this.setBuildNumber(jsonObj.getString("buildNumber"));
		this.setSignInTime(jsonObj.getLong("signInTime"));
		this.convoLoadTimes.clear();
		final JSONArray convoLoadTimes = jsonObj.getJSONArray("convoLoadTimes");
		for (int i = 0; i < convoLoadTimes.length(); i++) {
			this.addConvoStartupTime(convoLoadTimes.getLong(i));
		}
	}
}
