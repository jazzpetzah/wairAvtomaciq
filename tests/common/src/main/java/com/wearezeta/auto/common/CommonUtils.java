package com.wearezeta.auto.common;

import io.appium.java_client.AppiumDriver;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

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

	private static final Random rand = new Random();
	public static final int BACKEND_SYNC_TIMEOUT = 5000 + rand.nextInt(4000); // milliseconds

	public static final String PLATFORM_NAME_OSX = "Mac";
	public static final String PLATFORM_NAME_ANDROID = "Android";
	public static final String PLATFORM_NAME_IOS = "iOS";
	public static final String PLATFORM_NAME_WEB = "WebApp";
	
	private static final Logger log = ZetaLogger.getLog(CommonUtils.class
			.getSimpleName());

	private static final String TCPBLOCK_PREFIX_PATH = "/usr/local/bin/";
	
	public static String getOsName() {
		return System.getProperty("os.name");
	}

	public static void executeOsXCommand(String[] cmd) throws Exception {
		Process process = Runtime.getRuntime().exec(cmd);
		log.debug("Process started for cmdline " + Arrays.toString(cmd));
		outputErrorStreamToLog(process.getErrorStream());
		log.debug("Process exited with code " + process.waitFor());
	}

	public static String executeOsXCommandWithOutput(String[] cmd) throws Exception {
		Process process = Runtime.getRuntime().exec(cmd);
		log.debug("Process started for cmdline " + Arrays.toString(cmd));
		InputStream stream = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
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

	public static int getAndroidApiLvl(Class<?> c) {
		String androidVersion = "44";
		try {
			androidVersion = getValueFromConfig(c, "androidVersion");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Integer.parseInt(androidVersion);
	}
	
	public static String getBackendType(Class<?> c) throws IOException {
		return getValueFromCommonConfig(c, "backendType");
	}
	
	public static String getDeviceName(Class<?> c) throws IOException {
		return getValueFromConfig(c, "deviceName");
	}

	public static String getImagePath(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath") + USER_IMAGE;
		return path;
	}

	public static String getPingIconPath(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath") + PING_IMAGE;
		return path;
	}

	public static String getPingIconPathIOS(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "iosImagesPath") + IOS_PING_IMAGE;
		return path;
	}

	public static String getHotPingIconPath(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ HOT_PING_IMAGE;
		return path;
	}

	public static String getHotPingIconPathIOS(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "iosImagesPath")
				+ IOS_HOT_PING_IMAGE;
		return path;
	}

	public static String getImagesPath(Class<?> c) throws IOException {
		return getValueFromConfig(c, "defaultImagesPath");
	}

	public static String getResultImagePath(Class<?> c) throws IOException {
		String path = getValueFromConfig(c, "defaultImagesPath")
				+ RESULT_USER_IMAGE;
		return path;
	}

	public static String getPictureResultsPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "pictureResultsPath");
	}

	private static String getValueFromConfigFile(Class<?> c, String key,
			String resourcePath) throws IOException {
		String val = "";
		InputStream configFileStream = null;
		try {
			URL configFile = c.getClass().getResource("/" + resourcePath);
			configFileStream = configFile.openStream();
			Properties p = new Properties();
			p.load(configFileStream);
			val = (String) p.get(key);
		} finally {
			if (configFileStream != null) {
				configFileStream.close();
			}
		}
		return val;
	}

	public static String getValueFromConfig(Class<?> c, String key)
			throws IOException {
		return getValueFromConfigFile(c, key, "Configuration.cnf");
	}

	public static String getValueFromCommonConfig(Class<?> c, String key)
			throws IOException {
		return getValueFromConfigFile(c, key, "CommonConfiguration.cnf");
	}

	public static String getDefaultEmailFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "defaultEmail");
	}

	public static String getDefaultEmailServerFromConfig(Class<?> c)
			throws IOException {
		return getValueFromCommonConfig(c, "defaultEmailServer");
	}

	public static String getDriverTimeoutFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "driverTimeoutSeconds");
	}

	public static String getDefaultPasswordFromConfig(Class<?> c)
			throws IOException {
		return getValueFromCommonConfig(c, "defaultPassword");
	}

	public static String getDefaultBackEndUrlFromConfig(Class<?> c)
			throws IOException {
		return getValueFromCommonConfig(c, "defaultBackEndUrl");
	}

	public static String getOsxAppiumUrlFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "osxAppiumUrl");
	}

	public static String getAndroidAppiumUrlFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "androidAppiumUrl");
	}

	public static String getIosAppiumUrlFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "iosAppiumUrl");
	}

	public static String getWebAppAppiumUrlFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "webappAppiumUrl");
	}
	
	public static Boolean getIsSimulatorFromConfig(Class<?> c)
			throws IOException {
		return (getValueFromConfig(c, "isSimulator").equals("true"));
	}

	public static String getSwipeScriptPath(Class<?> c) throws IOException {
		return getValueFromConfig(c, "swipeScriptPath");
	}

	public static String getOsxApplicationPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "osxApplicationPath");
	}
	
	public static String getWebAppApplicationPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "webappApplicationPath");
	}
	
	public static String getAndroidApplicationPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "androidApplicationPath");
	}

	public static String getIosApplicationPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "iosApplicationPath");
	}

	public static String getAndroidActivityFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "activity");
	}

	public static String getSimulatorImagesPathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "iosImagesPath");
	}

	public static String getGenerateUsersFlagFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "generateUsers");
	}

	public static String getAndroidPackageFromConfig(Class<?> c) {
		try {
			return getValueFromConfig(c, "package");
		} catch (IOException e) {
			return null;
		}
	}

	public static String getUserPicturePathFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "pathToUserpic");
	}

	public static String generateGUID() {
		return UUID.randomUUID().toString();
	}

	public static String generateRandomString(int lengh) {
		return RandomStringUtils.randomAlphanumeric(lengh);
	}

	public static String getAndroidDeviceNameFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "deviceName");
	}
	
	public static String getJenkinsSuperUserPassword(Class<?> c)
			throws IOException {
		return getValueFromCommonConfig(c, "jenkinsSuPassword");
	}
	
	public static BufferedImage getElementScreenshot(WebElement element,
			AppiumDriver driver) throws IOException {
		BufferedImage screenshot = DriverUtils
				.takeScreenshot((ZetaDriver) driver);
		org.openqa.selenium.Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		return screenshot.getSubimage(elementLocation.x * 2,
				elementLocation.y * 2, elementSize.width * 2,
				elementSize.height * 2);
	}

	public static String getContactName(String login) {
		String[] firstParts = null;
		String[] secondParts = null;
		firstParts = login.split("\\+");
		secondParts = firstParts[1].split("@");
		return secondParts[0];
	}
	
	public static void blockTcpForAppName(String appName) throws IOException {
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

	public static void enableTcpForAppName(String appName) throws IOException {
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
}
