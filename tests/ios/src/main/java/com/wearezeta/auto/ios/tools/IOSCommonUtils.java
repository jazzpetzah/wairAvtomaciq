package com.wearezeta.auto.ios.tools;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;

public class IOSCommonUtils {
	private static Logger log = ZetaLogger.getLog(IOSCommonUtils.class.getSimpleName());
	
	public static BuildVersionInfo readClientVersionFromPlist() {
		String clientBuild = "no info";
		String zmessagingBuild = "no info";
		try {
			File file = new File(getIosClientInfoPlistFromConfig(IOSCommonUtils.class));
			NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(file);
			clientBuild = rootDict.objectForKey("CFBundleVersion").toString();
			NSDictionary zcBuildInfo = (NSDictionary)rootDict.objectForKey("ZCBuildInfo");
			NSDictionary zmessagingDict = (NSDictionary)zcBuildInfo.objectForKey("zmessaging");
			zmessagingBuild = zmessagingDict.objectForKey("version").toString();
		} catch(Exception ex) {
			 log.error("Failed to read iOS client properties.\n" + ex.getMessage());
		}
		return new BuildVersionInfo(clientBuild, zmessagingBuild);
	}
	
	public static String getIosClientInfoPlistFromConfig(Class<?> c)
			throws IOException {
		return CommonUtils.getValueFromConfig(c, "iosClientInfoPlist");
	}
	
	public static String getIosAppiumLogPathFromConfig(Class<?> c)
			throws IOException {
		return CommonUtils.getValueFromConfig(c, "iosAppiumLogPath");
	}
	
	public static void startActivityMonitoringRealDevice(String deviceID) throws Exception{
		CommonUtils.executeOsXCommand(new String[] {
				"/bin/bash",
				"-c",
				"instruments -t /Applications/Xcode.app/Contents/Applications/Instruments.app/Contents/Resources/templates/Activity\\ Monitor.tracetemplate -w " + deviceID});
	}
}
