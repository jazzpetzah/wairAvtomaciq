package com.wearezeta.auto.osx.common;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.osx.util.NSPoint;
import java.util.Arrays;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public class OSXCommonUtils extends CommonUtils {

	private static final int PREFS_DAEMON_RESTART_TIMEOUT = 1000;

	private static final Logger LOG = ZetaLogger.getLog(OSXCommonUtils.class
			.getName());

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
				LOG.debug("Request for osx version finished with code "
						+ process.waitFor());
			} catch (IOException e) {
				LOG.error(e.getMessage());
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

	public static void deletePreferencesFile() throws Exception {
		String command = String.format(
				"rm -rf \"%s/Library/Preferences/%s.plist\"",
				OSXExecutionContext.USER_HOME,
				OSXExecutionContext.CONFIG_DOMAIN);
		executeOsXCommand(new String[] { "/bin/bash", "-c", command });
	}

	public static void resetOSXPrefsDaemon() throws Exception {
		executeOsXCommand(new String[] { "/usr/bin/killall", "-SIGTERM",
				"cfprefsd" });
		Thread.sleep(PREFS_DAEMON_RESTART_TIMEOUT);
	}

	public static boolean isBackendTypeSet(String bt) throws Exception {
		if (!isBackendTypeSetForDomain(OSXExecutionContext.CONFIG_DOMAIN, bt)) {
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
			LOG.error("Failed to read OSX client properties.\n"
					+ ex.getMessage());
		}
		return new BuildVersionInfo(clientBuild, zmessagingBuild);
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

	public static NSPoint calculateScreenResolution(ZetaOSXDriver driver)
			throws Exception {
		BufferedImage im = DriverUtils.takeFullScreenShot(driver).orElseThrow(
				IllegalStateException::new);
		return new NSPoint(im.getWidth(), im.getHeight());
	}

	public static boolean isRetinaDisplay(ZetaOSXDriver driver)
			throws Exception {
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

	public static int screenPixelsMultiplier(ZetaOSXDriver driver)
			throws Exception {
		return (isRetinaDisplay(driver)) ? OSXConstants.Common.SIZE_MULTIPLIER_RETINA
				: OSXConstants.Common.SIZE_MULTIPLIER_NO_RETINA;
	}

	public static BufferedImage takeElementScreenshot(WebElement element,
			ZetaOSXDriver driver) throws Exception {
		int multiply = screenPixelsMultiplier(driver);

		BufferedImage screenshot = DriverUtils.takeFullScreenShot(
				(ZetaDriver) driver).orElseThrow(IllegalStateException::new);
		Point elPoint = element.getLocation();
		Dimension elSize = element.getSize();
		return screenshot.getSubimage(elPoint.x * multiply, elPoint.y
				* multiply, elSize.width * multiply, elSize.height * multiply);
	}

	public static String getOsxClientInfoPlistFromConfig(Class<?> c)
			throws Exception {
		return CommonUtils.getValueFromConfig(c, "osxClientInfoPlist");
	}

	public static String getWireConfigDomainFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "wireConfigDomain");
	}

	public static int clearAppData() throws Exception {
		final String[] commands = new String[] {
				"/bin/sh",
				"-c",
				"rm -rf"
						+ String.format(
								" \"%s/Library/Application Support/Wire/Cache\"",
								OSXExecutionContext.USER_HOME)
						+ String.format(
								" \"%s/Library/Application Support/Wire/Cookies\"",
								OSXExecutionContext.USER_HOME)
						+ String.format(
								" \"%s/Library/Application Support/Wire/GPUCache\"",
								OSXExecutionContext.USER_HOME)
						+ String.format(
								" \"%s/Library/Application Support/Wire/Local Storage\"",
								OSXExecutionContext.USER_HOME)
						+ String.format(
								" \"%s/Library/Application Support/Wire/WebRTCIdentityStore\"",
								OSXExecutionContext.USER_HOME)
						+ String.format(
								" \"%s/Library/Application Support/Wire/WebRTCIdentityStore-journal\"",
								OSXExecutionContext.USER_HOME)
						+ String.format(
								" \"%s/Library/Application Support/Wire/Cookies-journal\"",
								OSXExecutionContext.USER_HOME) };

		LOG.debug("executing command: " + Arrays.toString(commands));
		return executeOsXCommand(commands);
	}

	public static int killAllApps() throws Exception {
		final String[] commands = new String[] { "/bin/sh", "-c",
				String.format("killall %s", "Electron") };
		LOG.debug("executing command: " + Arrays.toString(commands));
		return executeOsXCommand(commands);
	}

	public static long getSizeOfAppInMB() throws Exception {
		final String[] commands = new String[] { "/bin/sh", "-c",
				String.format("du -sk %s", OSXExecutionContext.WIRE_APP_PATH) };
		LOG.debug("executing command: " + Arrays.toString(commands));
		String stringResult = executeOsXCommandWithOutput(commands);
		stringResult = stringResult.replace(OSXExecutionContext.WIRE_APP_PATH,
				"").trim();
		long longResult = Long.parseLong(stringResult) / 1024;
		LOG.debug("result: " + longResult);
		return longResult;
	}

	public static long startAppium4Mac() throws Exception {
		final String[] commands = new String[] { "/bin/sh", "-c",
				String.format("open %s", OSXExecutionContext.APPIUM_MAC_PATH) };
		LOG.debug("executing command: " + Arrays.toString(commands));
		return executeOsXCommand(commands);
	}
}
