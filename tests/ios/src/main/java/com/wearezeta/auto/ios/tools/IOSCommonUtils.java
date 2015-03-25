package com.wearezeta.auto.ios.tools;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.ios.pages.IOSPage;

public class IOSCommonUtils {
	private static Logger log = ZetaLogger.getLog(IOSCommonUtils.class
			.getSimpleName());

	public static BuildVersionInfo readClientVersionFromPlist() {
		String clientBuild = "no info";
		String zmessagingBuild = "no info";
		try {
			File file = new File(
					getIosClientInfoPlistFromConfig(IOSCommonUtils.class));
			NSDictionary rootDict = (NSDictionary) PropertyListParser
					.parse(file);
			clientBuild = rootDict.objectForKey("CFBundleVersion").toString();
			NSDictionary zcBuildInfo = (NSDictionary) rootDict
					.objectForKey("ZCBuildInfo");
			NSDictionary zmessagingDict = (NSDictionary) zcBuildInfo
					.objectForKey("zmessaging");
			zmessagingBuild = zmessagingDict.objectForKey("version").toString();
		} catch (Exception ex) {
			log.error("Failed to read iOS client properties.\n"
					+ ex.getMessage());
		}
		return new BuildVersionInfo(clientBuild, zmessagingBuild);
	}

	public static ClientDeviceInfo readDeviceInfo() {
		String os = "iOS";
		String osBuild = (String) IOSPage
				.executeScript("UIATarget.localTarget().systemVersion();");
		String deviceName = (String) IOSPage
				.executeScript("UIATarget.localTarget().model();");
		String gsmNetworkType = "";
		Boolean isWifiEnabled = null;

		return new ClientDeviceInfo(os, osBuild, deviceName, gsmNetworkType,
				isWifiEnabled);
	}

	public static String getIosClientInfoPlistFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c, "iosClientInfoPlist");
	}

	public static String getIosAppiumLogPathFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c, "iosAppiumLogPath");
	}

	/**
	 * 
	 * @param deviceName
	 *            either iPhone or iPad
	 * @param shouldThrowException
	 *            set this to true if you want the RuntimeException to be thrown
	 *            if no device is found. Setting this to false will return null
	 *            value in case if no device is connected
	 * @return UDID number of connected iDevice
	 * @throws Exception
	 */
	private static String getConnectediOSDeviceUDID(String deviceName,
			boolean shouldThrowException) throws Exception {
		final String result = CommonUtils
				.executeOsXCommandWithOutput(new String[] {
						"/bin/bash",
						"-c",
						"system_profiler SPUSBDataType | sed -n '/"
								+ deviceName
								+ "/,/Serial/p' | grep 'Serial Number:' | awk -F ': ' '{print $2}'" });
		if (result.length() > 0) {
			return result.trim();
		} else {
			if (shouldThrowException) {
				throw new RuntimeException(
						"Cannot detect any connected iPhone device");
			} else {
				return null;
			}
		}
	}

	public static String getConnectediPhoneUDID(boolean shouldThrowException)
			throws Exception {
		return getConnectediOSDeviceUDID("iPhone", shouldThrowException);
	}

	public static void copyToSystemClipboard(String text) {
		StringSelection str = new StringSelection(text);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
	}

	public static long stringToTime(String time) {
		long resultTime = 0;
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEEE, MMMM d, yyyy '∙' h:mm a");
		try {
			Date date = sdf.parse(time);
			resultTime = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultTime;
	}

	private static String getIOSSimulatorIdByDeviceName(String deviceName)
			throws Exception {
		return CommonUtils
				.executeOsXCommandWithOutput(
						new String[] {
								"/bin/bash",
								"-c",
								"xcrun simctl list devices | grep -i '"
										+ deviceName
										+ "' | tail -n 1 | cut -d '(' -f2 | cut -d ')' -f1" })
				.trim();
	}

	public static void collectSimulatorLogs(String deviceName,
			Date testStartedDate) throws Exception {
		log.debug("iOS Simulator Logs:");
		final String simId = getIOSSimulatorIdByDeviceName(deviceName);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		final String logStartTime = sdf.format(testStartedDate);
		final String logEndTime = sdf.format(new Date());
		final String collectedLogEntries = CommonUtils
				.executeOsXCommandWithOutput(new String[] {
						"/bin/bash",
						"-c",
						String.format(
								"awk -v start=%s -v stop=%s 'start <= $3 && $3 < stop' $HOME/Library/Logs/CoreSimulator/%s/system.log"
										+ " | grep -Ei '(wire|zclient|CoreSimulator)'",
								logStartTime, logEndTime, simId) });
		log.debug(collectedLogEntries);
	}
}
