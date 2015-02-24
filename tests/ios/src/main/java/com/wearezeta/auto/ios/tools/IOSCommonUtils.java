package com.wearezeta.auto.ios.tools;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
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

	public static void startActivityMonitoringRealDevice(String deviceID)
			throws Exception {
		CommonUtils
				.executeOsXCommand(new String[] {
						"/bin/bash",
						"-c",
						"instruments -t /Applications/Xcode.app/Contents/Applications/Instruments.app/Contents/Resources/templates/Activity\\ Monitor.tracetemplate -w "
								+ deviceID });
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
}
