package com.wearezeta.auto.osx.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.osx.pages.ContactListPage;

public class OSXCommonUtils extends CommonUtils {
	private static final Logger log = ZetaLogger.getLog(OSXCommonUtils.class.getSimpleName());
	
	public static String getZClientProcessName() throws IOException {
		String getZClientProcess = CommonUtils.getOsxApplicationPathFromConfig(ContactListPage.class);
		File file = new File(getZClientProcess);
		getZClientProcess = file.getName().replace(".app", "");
		return getZClientProcess;
	}
	
	public static String getOsXVersion() throws Exception {
		String command = "sw_vers -productVersion";
		
		Process process = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", command });
	
		String result = "no info";
		
		if (process != null) {
			InputStream stream = null;
			InputStreamReader isReader = null;
			BufferedReader bufferedReader = null;
			try {
				stream = process.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));
				String possibleVersion;
		
				while (( possibleVersion = br.readLine() ) != null ) {
					possibleVersion = possibleVersion.trim();
					if (!possibleVersion.isEmpty()) {
						result = possibleVersion;
						break;
					}
				}
				outputErrorStreamToLog(process.getErrorStream());
				log.debug("Request for osx version finished with code " + process.waitFor());
			} catch (IOException e) {
				log.error(e.getMessage());
			} finally {
				if (bufferedReader != null) bufferedReader.close();
				if (isReader != null) isReader.close();
				if (stream != null) stream.close();
			}
		}
		return result;
	}
	
	public static void deleteZClientLoginFromKeychain() throws Exception {
		String command = "security delete-generic-password -s \"zeta dev-nginz-https.zinfra.io\"";

		if (!getOsName().contains(OS_NAME_WINDOWS)) {
			executeOsXCommand(new String[] { "/bin/bash", "-c", command });
		}
		
		command = "security delete-generic-password -s \"Wire: Credentials for wire.com\"";

		if (!getOsName().contains(OS_NAME_WINDOWS)) {
			executeOsXCommand(new String[] { "/bin/bash", "-c", command });
		}
	}
	
	public static void removeAllZClientSettingsFromDefaults() throws Exception {
		removeZClientDomain("com.wearezeta.zclient.mac.development");
		removeZClientDomain("com.wearezeta.zclient.mac.internal");
		removeZClientDomain("com.wearezeta.zclient.mac");
	}
	
	public static void setZClientBackend(String bt) throws Exception {
		setZClientBackendForDomain("com.wearezeta.zclient.mac.development", bt);
		setZClientBackendForDomain("com.wearezeta.zclient.mac.internal", bt);
		setZClientBackendForDomain("com.wearezeta.zclient.mac", bt);
	}

	public static void removeZClientDomain(String domain) throws Exception {
		String command = "defaults delete " + domain;
		executeOsXCommand(new String[] { "/bin/bash", "-c", command });
	}
	
	public static void setZClientBackendForDomain(String domain, String bt) throws Exception {
		String command = "defaults write " + domain + " ZMBackendEnvironmentType -string " + bt;
		executeOsXCommand(new String[] { "/bin/bash", "-c", command });
		for (int i = 0; i < 10; i++) {
			if (isBackendTypeSetForDomain(domain, bt)) break;
			try { Thread.sleep(500); } catch (InterruptedException e) { }
		}
	}
	
	public static boolean isBackendTypeSetForDomain(String domain, String bt) throws Exception {
		String command = "defaults read " + domain + " ZMBackendEnvironmentType";
		String result = executeOsXCommandWithOutput(new String[] { "/bin/bash", "-c", command });
		
		if (result.contains(bt)) return true;
		else return false;
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

	public static void sendTextIntoFocusedElement(RemoteWebDriver driver, String text) {
		driver.executeScript(String.format("tell application \"Wire\"\nactivate\nend tell\n" +
				"tell application \"System Events\"\nkeystroke \"%s\"\nend tell", text));
	}
	
	public static ClientDeviceInfo readDeviceInfo() throws Exception {
		String osName = "Mac OS X";
		String osVersion = getOsXVersion();
		String deviceName = "no info";
		Boolean isWifiEnabled = null;
		
		ClientDeviceInfo result = new ClientDeviceInfo(osName, osVersion, deviceName, null, isWifiEnabled);
		return result;
	}
	
	public static String getOsxClientInfoPlistFromConfig(Class<?> c)
			throws IOException {
		return CommonUtils.getValueFromConfig(c, "osxClientInfoPlist");
	}
	
	public static void startActivityMonitoringInstrument() throws Exception{
		CommonUtils.executeOsXCommand(new String[] {
				"/bin/bash",
				"-c",
				"instruments -t /Applications/Xcode.app/Contents/Applications/Instruments.app/Contents/Resources/templates/Activity\\ Monitor.tracetemplate"});
	}
}
