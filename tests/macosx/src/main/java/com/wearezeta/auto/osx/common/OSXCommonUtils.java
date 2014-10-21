package com.wearezeta.auto.osx.common;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;

public class OSXCommonUtils extends CommonUtils {
	private static final Logger log = ZetaLogger.getLog(OSXCommonUtils.class.getSimpleName());
	
	public static void removeAllZClientSettingsFromDefaults() throws Exception {
		String command = "defaults delete com.wearezeta.zclient.mac";

		if (getOsName().contains(OS_NAME_WINDOWS)) {
			Runtime.getRuntime().exec("cmd /C " + command);
		} else {
			executeOsXCommand(new String[] { "/bin/bash", "-c", command });
		}
	}
	
	public static BuildVersionInfo readClientVersionFromPlist() {
		String clientBuild = "no info";
		String zmessagingBuild = "no info";
		try {
			File file = new File(getOsxClientInfoPlistFromConfig(OSXCommonUtils.class));
			NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(file);
			clientBuild = rootDict.objectForKey("CFBundleVersion").toString();
			NSDictionary zcBuildInfo = (NSDictionary)rootDict.objectForKey("ZCBuildInfo");
			NSDictionary zmessagingDict = (NSDictionary)zcBuildInfo.objectForKey("zmessaging");
			zmessagingBuild = zmessagingDict.objectForKey("version").toString();
		} catch(Exception ex) {
			 log.error("Failed to read OSX client properties.\n" + ex.getMessage());
		}
		return new BuildVersionInfo(clientBuild, zmessagingBuild);
	}
	
	public static String getOsxClientInfoPlistFromConfig(Class<?> c)
			throws IOException {
		return CommonUtils.getValueFromConfig(c, "osxClientInfoPlist");
	}
}
