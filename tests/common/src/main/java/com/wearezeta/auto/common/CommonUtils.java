package com.wearezeta.auto.common;

import io.appium.java_client.AppiumDriver;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.Semaphore;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class CommonUtils {
	public static final String OS_NAME_WINDOWS = "Windows";

	private static final String USER_IMAGE = "userpicture_landscape.jpg";
	private static final String RESULT_USER_IMAGE = "userpicture_mobile_check.jpg";
	private static final String PING_IMAGE = "ping_image.png";
	private static final String HOT_PING_IMAGE = "hot_ping_image.png";
	private static final String IOS_PING_IMAGE = "ios_ping_image.png";
	private static final String IOS_HOT_PING_IMAGE = "ios_hot_ping_image.png";
	private static final String CALLING_MUTE_BUTTON_IMAGE = "mutebtn_pressed.png";
	private static final String CALLING_SPEAKER_BUTTON_IMAGE = "speakerbtn_pressed.png";
	private static final String IOS_AVATAR_CLOCK_IMAGE = "new_avatarclock.png";
	private static final String MEDIABAR_PLAY_IMAGE = "android_mediabar_play_image.png";
	private static final String MEDIABAR_PAUSE_IMAGE = "android_mediabar_pause_image.png";
	private static final String MEDIA_PLAY_IMAGE = "android_media_play_image.png";
	private static final String MEDIA_PAUSE_IMAGE = "android_media_pause_image.png";

	private static final Random rand = new Random();
	public static final int BACKEND_SYNC_TIMEOUT = 5000 + rand.nextInt(4000); // milliseconds

	private static final Logger log = ZetaLogger.getLog(CommonUtils.class
			.getSimpleName());

	private static final String TCPBLOCK_PREFIX_PATH = "/usr/local/bin/";

	public static String getOsName() {
		return System.getProperty("os.name");
	}

	public static int executeOsXCommand(String[] cmd) throws Exception {
		Process process = Runtime.getRuntime().exec(cmd);
		log.debug("Process started for cmdline " + Arrays.toString(cmd));
		outputErrorStreamToLog(process.getErrorStream());
		log.debug("Process exited with code " + process.waitFor());
		return process.waitFor();
	}

	public static String executeOsXCommandWithOutput(String[] cmd)
			throws Exception {
		Process process = Runtime.getRuntime().exec(cmd);
		log.debug("Process started for cmdline " + Arrays.toString(cmd));
		InputStream stream = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		StringBuilder sb = new StringBuilder("\n");
		String s;
		while ((s = br.readLine()) != null) {
			sb.append("\t" + s + "\n");
		}
		String output = sb.toString();
		stream.close();
		outputErrorStreamToLog(process.getErrorStream());
		log.debug("Process exited with code " + process.waitFor());
		return output;
	}

	public static void outputErrorStreamToLog(InputStream stream)
			throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder("\n");
		String s;
		while ((s = br.readLine()) != null) {
			sb.append("\t" + s + "\n");
		}
		String output = sb.toString();
		if (!output.trim().isEmpty()) {
			log.debug(output);
		}
		stream.close();
	}

	public static int getAndroidApiLvl(Class<?> c) throws Exception {
		String androidVersion = "44";
		androidVersion = getValueFromConfig(c, "androidVersion");
		return Integer.parseInt(androidVersion);
	}

	public static Boolean getAndroidLogs(Class<?> c) throws Exception {
		Boolean androidlogs = false;
		androidlogs = Boolean.valueOf(getValueFromConfig(c, "androidLogs"));
		return androidlogs;
	}

	public static String getBackendType(Class<?> c) throws Exception {
		return getValueFromCommonConfig(c, "backendType");
	}

	public static String getDeviceName(Class<?> c) throws Exception {
		return getValueFromConfig(c, "deviceName");
	}

	public static String getImagePath(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "defaultImagesPath") + USER_IMAGE;
		return path;
	}

	public static String getPingIconPath(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "defaultImagesPath") + PING_IMAGE;
		return path;
	}

	public static String getPingIconPathIOS(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "iosImagesPath") + IOS_PING_IMAGE;
		return path;
	}

	public static String getHotPingIconPath(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ HOT_PING_IMAGE;
		return path;
	}

	public static String getHotPingIconPathIOS(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "iosImagesPath")
				+ IOS_HOT_PING_IMAGE;
		return path;
	}
	public static String getCallingMuteButtonPath(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ CALLING_MUTE_BUTTON_IMAGE;
		return path;
	}
	
	public static String getCallingSpeakerButtonPath(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ CALLING_SPEAKER_BUTTON_IMAGE;
		return path;
	}
	
	public static String getAvatarWithClockIconPathIOS(Class<?> c)
			throws Exception {
		String path = getValueFromConfig(c, "iosImagesPath")
				+ IOS_AVATAR_CLOCK_IMAGE;
		return path;
	}

	public static String getImagesPath(Class<?> c) throws Exception {
		return getValueFromConfig(c, "defaultImagesPath");
	}

	public static String getResultImagePath(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ RESULT_USER_IMAGE;
		return path;
	}

	public static String getMediaBarPlayIconPath(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ MEDIABAR_PLAY_IMAGE;
		return path;
	}

	public static String getMediaBarPauseIconPath(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ MEDIABAR_PAUSE_IMAGE;
		return path;
	}

	public static String getMediaPlayIconPath(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ MEDIA_PLAY_IMAGE;
		return path;
	}

	public static String getMediaPauseIconPath(Class<?> c) throws Exception {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ MEDIA_PAUSE_IMAGE;
		return path;
	}

	public static String getPictureResultsPathFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "pictureResultsPath");
	}

	private final static Semaphore sem = new Semaphore(1);
	private final static Map<String, String> cachedConfig = new HashMap<String, String>();

	private static String getValueFromConfigFile(Class<?> c, String key,
			String resourcePath) throws Exception {
		final String configKey = String.format("%s:%s", resourcePath, key);
		if (cachedConfig.containsKey(configKey)) {
			return cachedConfig.get(configKey);
		}

		final int maxTry = 5;
		int tryNum = 0;
		Exception savedException = null;
		do {
			InputStream configFileStream = null;
			sem.acquire();
			try {
				final ClassLoader classLoader = c.getClassLoader();
				configFileStream = classLoader
						.getResourceAsStream(resourcePath);
				Properties p = new Properties();
				p.load(configFileStream);
				cachedConfig.put(configKey, (String) p.get(key));
				return cachedConfig.get(configKey);
			} catch (Exception e) {
				savedException = e;
			} finally {
				if (configFileStream != null) {
					configFileStream.close();
				}
				sem.release();
			}
			Thread.sleep(100 + rand.nextInt(2000));
			tryNum++;
		} while (tryNum < maxTry);
		throw savedException;
	}

	public static String getValueFromConfig(Class<?> c, String key)
			throws Exception {
		return getValueFromConfigFile(c, key, "Configuration.cnf");
	}

	public static String getValueFromCommonConfig(Class<?> c, String key)
			throws Exception {
		return getValueFromConfigFile(c, key, "CommonConfiguration.cnf");
	}

	public static String getDefaultEmailFromConfig(Class<?> c) throws Exception {
		return getValueFromConfig(c, "defaultEmail");
	}

	public static String getDefaultEmailServerFromConfig(Class<?> c)
			throws Exception {
		return getValueFromCommonConfig(c, "defaultEmailServer");
	}

	public static String getDriverTimeoutFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "driverTimeoutSeconds");
	}

	public static String getDefaultPasswordFromConfig(Class<?> c)
			throws Exception {
		return getValueFromCommonConfig(c, "defaultPassword");
	}

	public static String getDefaultBackEndUrlFromConfig(Class<?> c)
			throws Exception {
		return getValueFromCommonConfig(c, "defaultBackEndUrl");
	}

	public static String getOsxAppiumUrlFromConfig(Class<?> c) throws Exception {
		return getValueFromConfig(c, "osxAppiumUrl");
	}

	public static String getAndroidAppiumUrlFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "androidAppiumUrl");
	}

	public static String getIosAppiumUrlFromConfig(Class<?> c) throws Exception {
		return getValueFromConfig(c, "iosAppiumUrl");
	}

	public static String getWebAppAppiumUrlFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "webappAppiumUrl");
	}

	public static Boolean getIsSimulatorFromConfig(Class<?> c) throws Exception {
		return (getValueFromConfig(c, "isSimulator").equals("true"));
	}

	public static String getSwipeScriptPath(Class<?> c) throws Exception {
		return getValueFromConfig(c, "swipeScriptPath");
	}

	public static String getOsxApplicationPathFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "osxApplicationPath");
	}

	public static String getWebAppApplicationPathFromConfig(Class<?> c)
			throws Exception {
		final String currentBackendType = getBackendType(c);
		switch (currentBackendType.toLowerCase()) {
		case "edge":
			return getValueFromConfig(c, "webappEdgeApplicationPath");
		case "staging":
			return getValueFromConfig(c, "webappStagingApplicationPath");
		case "production":
			return getValueFromConfig(c, "webappProductionApplicationPath");
		default:
			throw new RuntimeException(String.format(
					"Non supported backend type '%s'", currentBackendType));
		}
	}

	public static String getAndroidApplicationPathFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "androidApplicationPath");
	}

	public static String getIosApplicationPathFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "iosApplicationPath");
	}

	public static String getAndroidActivityFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "activity");
	}

	public static String getSimulatorImagesPathFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "iosImagesPath");
	}

	public static String getGenerateUsersFlagFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "generateUsers");
	}

	public static String getAndroidPackageFromConfig(Class<?> c) {
		try {
			return getValueFromConfig(c, "package");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getWebAppImagesPathFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "webappImagesPath");
	}

	public static String getUserPicturePathFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "pathToUserpic");
	}

	public static String generateGUID() {
		return UUID.randomUUID().toString();
	}

	public static String generateRandomString(int lengh) {
		return RandomStringUtils.randomAlphanumeric(lengh);
	}

	public static String getAndroidDeviceNameFromConfig(Class<?> c)
			throws Exception {
		return getValueFromConfig(c, "deviceName");
	}

	public static String getJenkinsSuperUserLogin(Class<?> c) throws Exception {
		return getValueFromCommonConfig(c, "jenkinsSuLogin");
	}

	public static String getJenkinsSuperUserPassword(Class<?> c)
			throws Exception {
		return getValueFromCommonConfig(c, "jenkinsSuPassword");
	}

	public static String getJenkinsProjectDir(Class<?> c) throws Exception {
		return getValueFromCommonConfig(c, "jenkinsProjectDir");
	}

	public static String getDefaultCallingServiceHostFromConfig(Class<?> c)
			throws Exception {
		return getValueFromCommonConfig(c, "defaultCallingServiceHost");
	}

	public static String getDefaultCallingServicePortFromConfig(Class<?> c)
			throws Exception {
		return getValueFromCommonConfig(c, "defaultCallingServicePort");
	}

	public static BufferedImage getElementScreenshot(WebElement element,
			AppiumDriver driver) throws IOException {
		return getElementScreenshot(element, driver, "iPhone 6");
	}

	public static BufferedImage getElementScreenshot(WebElement element,
			AppiumDriver driver, String deviceName) throws IOException {
		int multiply = 3;
		if (deviceName.equals("iPhone 6")) {
			multiply = 2;
		}

		BufferedImage screenshot = DriverUtils
				.takeScreenshot((ZetaDriver) driver);
		org.openqa.selenium.Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		return screenshot.getSubimage(elementLocation.x * multiply,
				elementLocation.y * multiply, elementSize.width * multiply,
				elementSize.height * multiply);
	}

	public static String getContactName(String login) {
		String[] firstParts = null;
		String[] secondParts = null;
		firstParts = login.split("\\+");
		secondParts = firstParts[1].split("@");
		return secondParts[0];
	}

	public static void blockTcpForAppName(String appName) throws Exception {
		final String blockTcpForAppCmd = "echo "
				+ getJenkinsSuperUserPassword(CommonUtils.class) + "| sudo -S "
				+ TCPBLOCK_PREFIX_PATH + "tcpblock -a " + appName;
		try {
			executeOsXCommand(new String[] { "/bin/bash", "-c",
					blockTcpForAppCmd });
			log.debug(executeOsXCommandWithOutput(new String[] { "/bin/bash",
					"-c", TCPBLOCK_PREFIX_PATH + "tcpblock -g" }));
		} catch (Exception e) {
			log.error("TCP connections for " + appName
					+ " were not blocked. Make sure tcpblock is installed.");
		}
	}

	public static void enableTcpForAppName(String appName) throws Exception {
		final String enableTcpForAppCmd = "echo "
				+ getJenkinsSuperUserPassword(CommonUtils.class) + "| sudo -S "
				+ TCPBLOCK_PREFIX_PATH + "tcpblock -r " + appName;
		try {
			executeOsXCommand(new String[] { "/bin/bash", "-c",
					enableTcpForAppCmd });
			log.debug(executeOsXCommandWithOutput(new String[] { "/bin/bash",
					"-c", TCPBLOCK_PREFIX_PATH + "tcpblock -g" }));
		} catch (Exception e) {
			log.error("TCP connections for " + appName
					+ " were not enabled. Make sure tcpblock is installed.");
		}
	}

	public static String readTextFileFromResources(String resourcePath)
			throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				CommonUtils.class.getResourceAsStream(resourcePath)));
		String full = "";
		String s;
		while ((s = in.readLine()) != null) {
			full += s + "\n";
		}
		return full;
	}

	public static void defineNoHeadlessEnvironment() {
		System.setProperty("java.awt.headless", "false");
	}

	public static void disableSeleniumLogs() {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	public static String encodeSHA256Base64(String item) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(item.getBytes("UTF-8"));
		final byte[] digest = md.digest();
		return Base64.encodeBase64String(digest);
	}
}
