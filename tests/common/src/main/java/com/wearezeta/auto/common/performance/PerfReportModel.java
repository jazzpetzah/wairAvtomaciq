package com.wearezeta.auto.common.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;

public abstract class PerfReportModel {
	private static final String UNKNOWN_VALUE = "Unknown";

	private static final Logger log = ZetaLogger.getLog(PerfReportModel.class
			.getSimpleName());

	/**
	 * All times are in milliseconds
	 */
	private String deviceName = UNKNOWN_VALUE;
	private String deviceOSVersion = UNKNOWN_VALUE;
	private NetworkType networkType;
	private long appStartupTime;
	private int contactsCount;
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

	public long getAppStartupTime() {
		return appStartupTime;
	}

	protected void setAppStartupTime(long appStartupTime) {
		this.appStartupTime = appStartupTime;
	}

	public int getContactsCount() {
		return contactsCount;
	}

	public void setContactsCount(int contactsCount) {
		this.contactsCount = contactsCount;
	}

	public void clearConvoStartupTimes() {
		this.convoLoadTimes.clear();
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
		result.put("appStartupTime", this.getAppStartupTime());
		result.put("contactsCount", this.getContactsCount());
		result.put("buildNumber", this.getBuildNumber());
		result.put("signInTime", this.getSignInTime());
		result.put("convoLoadTimes",
				new JSONArray(this.convoLoadTimes.toArray(new Long[0])));
		return result;
	}

	protected void loadFromJSON(final JSONObject jsonObj) throws Exception {
		this.setDeviceName(jsonObj.getString("deviceName"));
		this.setDeviceOSVersion(jsonObj.getString("deviceOSVersion"));
		this.setNetworkType(NetworkType.fromString(jsonObj
				.getString("networkType")));
		this.setAppStartupTime(jsonObj.getLong("appStartupTime"));
		this.setContactsCount(jsonObj.getInt("contactsCount"));
		this.setBuildNumber(jsonObj.getString("buildNumber"));
		this.setSignInTime(jsonObj.getLong("signInTime"));
		this.convoLoadTimes.clear();
		final JSONArray convoLoadTimes = jsonObj.getJSONArray("convoLoadTimes");
		for (int i = 0; i < convoLoadTimes.length(); i++) {
			this.addConvoStartupTime(convoLoadTimes.getLong(i));
		}
	}

	protected void loadValuesFromDeviceInfo(ClientDeviceInfo deviceInfo) {
		// FIXME: handle other network types
		this.setNetworkType(deviceInfo.isWifiEnabled() ? NetworkType.WiFi
				: NetworkType.FourG);
		this.setDeviceName(deviceInfo.getDeviceName());
		this.setDeviceOSVersion(deviceInfo.getOperatingSystemBuild());
	}

	protected long readLastLogValue(final String patternStr, final String output) {
		final Pattern pattern = Pattern.compile(patternStr);
		final Matcher matcher = pattern.matcher(output);
		long result = 0;
		while (matcher.find()) {
			try {
				result = Long.parseLong(matcher.group(1));
				log.debug("Found log entry with text: " + matcher.group());
			} catch (NumberFormatException e) {
				log.error(e);
			}
		}
		return result;
	}
	
	protected long readLogValue(final String patternStr, final String output) {
		final Pattern pattern = Pattern.compile(patternStr);
		final Matcher matcher = pattern.matcher(output);
		while (matcher.find()) {
			try {
				return Long.parseLong(matcher.group(1));
			} catch (NumberFormatException e) {
				log.error(e);
			}
		}
		return 0;
	}

	protected List<Long> readLogValues(final String patternStr,
			final String output) {
		final Pattern pattern = Pattern.compile(patternStr);
		final Matcher matcher = pattern.matcher(output);
		final List<Long> result = new ArrayList<>();
		while (matcher.find()) {
			try {
				result.add(Long.parseLong(matcher.group(1)));
			} catch (NumberFormatException e) {
				log.error(e);
			}
		}
		return result;
	}
}
