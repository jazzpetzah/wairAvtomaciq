package com.wearezeta.auto.osx.common;

import io.appium.java_client.AppiumDriver;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.osx.util.NSPoint;

public class OSXCommonUtils extends CommonUtils {
	
	private static final int PREFS_DAEMON_RESTART_TIMEOUT = 1000;
	
	private static final Logger log = ZetaLogger.getLog(OSXCommonUtils.class
			.getSimpleName());

	/*
	 * Retrieves Wire process name from config
	 */
	public static String getWireProcessName() throws Exception {
		String wireProcessName = OSXExecutionContext.wirePath;
		File file = new File(wireProcessName);
		wireProcessName = file.getName().replace(".app", "");
		return wireProcessName;
	}

	/*
	 * 
	 */
	public static String getOsXVersion() throws Exception {
		String command = "sw_vers -productVersion";

		Process process = Runtime.getRuntime().exec(
				new String[] { "/bin/bash", "-c", command });

		String result = "no info";

		if (process != null) {
			InputStream stream = null;
			InputStreamReader isReader = null;
			BufferedReader bufferedReader = null;
			try {
				stream = process.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						stream));
				String possibleVersion;

				while ((possibleVersion = br.readLine()) != null) {
					possibleVersion = possibleVersion.trim();
					if (!possibleVersion.isEmpty()) {
						result = possibleVersion;
						break;
					}
				}
				outputErrorStreamToLog(process.getErrorStream());
				log.debug("Request for osx version finished with code "
						+ process.waitFor());
			} catch (IOException e) {
				log.error(e.getMessage());
			} finally {
				if (bufferedReader != null)
					bufferedReader.close();
				if (isReader != null)
					isReader.close();
				if (stream != null)
					stream.close();
			}
		}
		return result;
	}

	public static void deleteCacheFolder() throws Exception {
		String command = String.format("rm -rf %s/Library/Containers/%s/Data/Library/Caches",
				System.getProperty("user.home"),
				OSXExecutionContext.wireConfigDomain);
		executeOsXCommand(new String[] { "/bin/bash", "-c", command });
	}

	public static void deletePreferencesFile() throws Exception {
		String command = String.format("rm -rf %s/Library/Preferences/%s.plist",
				System.getProperty("user.home"),
				OSXExecutionContext.wireConfigDomain);
		executeOsXCommand(new String[] { "/bin/bash", "-c", command });
	}
	
	public static void deleteWireLoginFromKeychain() throws Exception {
		String command = "security delete-generic-password -s \"zeta staging-nginz-https.zinfra.io\"";

		if (!getOsName().contains(OS_NAME_WINDOWS)) {
			executeOsXCommand(new String[] { "/bin/bash", "-c", command });
		}

		command = "security delete-generic-password -s \"Wire: Credentials for wire.com\"";

		if (!getOsName().contains(OS_NAME_WINDOWS)) {
			executeOsXCommand(new String[] { "/bin/bash", "-c", command });
		}
	}

	public static void removeAllZClientSettingsFromDefaults() throws Exception {
		resetOSXPrefsDaemon();
		removeZClientDomain(OSXExecutionContext.wireConfigDomain);
	}

	public static void setZClientBackendAndDisableStartUI(String bt)
			throws Exception {
		resetOSXPrefsDaemon();
		setZClientBackendForDomain(OSXExecutionContext.wireConfigDomain, bt);
		disableStartUIOnFirstLogin(OSXExecutionContext.wireConfigDomain);
	}

	public static void resetOSXPrefsDaemon() throws Exception {
		executeOsXCommand(new String[] { "/usr/bin/killall", "-SIGTERM",
				"cfprefsd" });
		Thread.sleep(PREFS_DAEMON_RESTART_TIMEOUT);
	}

	private static void removeZClientDomain(String domain) throws Exception {
		String command = "defaults delete " + domain;
		executeOsXCommand(new String[] { "/bin/bash", "-c", command });
	}

	private static void setZClientBackendForDomain(String domain, String bt)
			throws Exception {
		final String setBackendTypeCmd = String
				.format("defaults write ~/Containers/%s/Data/Library/Preferences/%s.plist ZMBackendEnvironmentType -string %s",
						domain, domain, bt);
		executeOsXCommand(new String[] { "/bin/bash", "-c", setBackendTypeCmd });
	}

	private static void disableStartUIOnFirstLogin(String domain)
			throws Exception {
		final String disableCmd = String
				.format("defaults write ~/Containers/%s/Data/Library/Preferences/%s.plist ZCSkipFirstTimeUseChecks -bool YES",
						domain, domain);
		executeOsXCommand(new String[] { "/bin/bash", "-c", disableCmd });
	}

	public static boolean isBackendTypeSet(String bt) throws Exception {
		if (!isBackendTypeSetForDomain(OSXExecutionContext.wireConfigDomain, bt)) {
			return false;
		}
		return true;
	}

	private static boolean isBackendTypeSetForDomain(String domain, String bt)
			throws Exception {
		String command = String.format(
				"defaults read %s ZMBackendEnvironmentType", domain);
		String result = executeOsXCommandWithOutput(new String[] { "/bin/bash",
				"-c", command });
		return result.contains(bt);
	}

	public static BuildVersionInfo readClientVersionFromPlist() {
		String clientBuild = "no info";
		String zmessagingBuild = "no info";
		try {
			File file = new File(
					getOsxClientInfoPlistFromConfig(OSXCommonUtils.class));
			NSDictionary rootDict = (NSDictionary) PropertyListParser
					.parse(file);
			clientBuild = rootDict.objectForKey("CFBundleVersion").toString();
			NSDictionary zcBuildInfo = (NSDictionary) rootDict
					.objectForKey("ZCBuildInfo");
			NSDictionary zmessagingDict = (NSDictionary) zcBuildInfo
					.objectForKey("zmessaging");
			zmessagingBuild = zmessagingDict.objectForKey("version").toString();
		} catch (Exception ex) {
			log.error("Failed to read OSX client properties.\n"
					+ ex.getMessage());
		}
		return new BuildVersionInfo(clientBuild, zmessagingBuild);
	}

	public static void sendTextIntoFocusedElement(RemoteWebDriver driver,
			String text) {
		driver.executeScript(String
				.format("tell application \"Wire\"\nactivate\nend tell\n"
						+ "tell application \"System Events\"\nkeystroke \"%s\"\nend tell",
						text));
	}

	public static ClientDeviceInfo readDeviceInfo() throws Exception {
		String osName = "Mac OS X";
		String osVersion = getOsXVersion();
		String deviceName = "no info";
		Boolean isWifiEnabled = null;

		ClientDeviceInfo result = new ClientDeviceInfo(osName, osVersion,
				deviceName, null, isWifiEnabled);
		return result;
	}

	public static void killWireIfStuck() {
		try {
			executeOsXCommand(new String[] { "/bin/bash", "-c",
					"kill -9 $(lsof -c Wire -t)" });
		} catch (Exception e) {
		}
	}

	public static NSPoint calculateScreenResolution(ZetaDriver driver)
			throws IOException {
		BufferedImage im = DriverUtils.takeScreenshot(driver);
		return new NSPoint(im.getWidth(), im.getHeight());
	}

	public static boolean isRetinaDisplay(ZetaDriver driver) throws IOException {
		NSPoint size = calculateScreenResolution(driver);
		return isRetinaDisplay(size.x(), size.y());
	}

	public static boolean isRetinaDisplay(int width, int height) {
		if (width == 2560 && height == 1600) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean osxAXValueToBoolean(String value) {
		return value.equals(OSXConstants.Common.AX_BOOLEAN_FALSE) ? false
				: true;
	}

	public static int screenPixelsMultiplier(AppiumDriver driver)
			throws IOException {
		return (isRetinaDisplay((ZetaDriver) driver)) ? OSXConstants.Common.SIZE_MULTIPLIER_RETINA
				: OSXConstants.Common.SIZE_MULTIPLIER_NO_RETINA;
	}

	public static BufferedImage takeElementScreenshot(WebElement element,
			AppiumDriver driver) throws IOException {
		int multiply = screenPixelsMultiplier(driver);

		BufferedImage screenshot = DriverUtils
				.takeScreenshot((ZetaDriver) driver);
		NSPoint elementLocation = NSPoint.fromString(element
				.getAttribute(OSXConstants.Attributes.AXPOSITION));
		NSPoint elementSize = NSPoint.fromString(element
				.getAttribute(OSXConstants.Attributes.AXSIZE));
		return screenshot.getSubimage(elementLocation.x() * multiply,
				elementLocation.y() * multiply, elementSize.x() * multiply,
				elementSize.y() * multiply);
	}

	private static final String LOG_FILTER_REGEX = "(wire|zclient|appium)";

	public static void collectSystemLogs(Date testStartedDate) throws Exception {
		log.debug("System Logs:");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		final String logStartTime = sdf.format(testStartedDate);
		final String logEndTime = sdf.format(new Date());
		final String collectedLogEntries = CommonUtils
				.executeOsXCommandWithOutput(new String[] {
						"/bin/bash",
						"-c",
						String.format(
								"awk -v start=%s -v stop=%s 'start <= $3 && $3 < stop' /private/var/log/system.log"
										+ " | grep -Ei '(%s)'", logStartTime,
								logEndTime, LOG_FILTER_REGEX) });
		log.debug(collectedLogEntries);
	}

	public static String getOsxClientInfoPlistFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c, "osxClientInfoPlist");
	}

	public static String getWireConfigDomainFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "wireConfigDomain");
	}
}
