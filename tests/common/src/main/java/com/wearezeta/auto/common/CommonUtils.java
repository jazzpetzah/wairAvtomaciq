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
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class CommonUtils {

	public static final int MAX_PARALLEL_USER_CREATION_TASKS = 25;

	private static final String USER_IMAGE = "userpicture_landscape.jpg";
	private static final String IOS_PING_IMAGE = "ios_ping_image.png";
	private static final String IOS_HOT_PING_IMAGE = "ios_hot_ping_image.png";
	private static final String IOS_AVATAR_CLOCK_IMAGE = "new_avatarclock.png";
	private static final String MEDIABAR_PLAY_IMAGE = "android_mediabar_play_image_(white).png";
	private static final String MEDIABAR_PAUSE_IMAGE = "android_mediabar_pause_image_(white).png";
	private static final String ALPHANUMERIC_PLUS_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJLMNOPQRSTUVWXYZ1234567890!@#$%^&*()";

	private static final Random rand = new Random();

	private static final Logger log = ZetaLogger.getLog(CommonUtils.class
		.getSimpleName());

	private static final String TCPBLOCK_PREFIX_PATH = "/usr/local/bin/";

	public static boolean trueInPercents(int percent) {
		Random rand = new Random();
		int nextInt = rand.nextInt(100);
		return nextInt < percent;
	}

	public static boolean executeOsCommandWithTimeout(String[] cmd,
		long timeoutSeconds) throws Exception {
		Process process = Runtime.getRuntime().exec(cmd);
		log.debug("Process started for cmdline " + Arrays.toString(cmd));
		outputErrorStreamToLog(process.getErrorStream());
		return process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
	}

	public static int executeOsXCommand(String[] cmd) throws Exception {
		Process process = Runtime.getRuntime().exec(cmd);
		log.debug("Process started for cmdline " + Arrays.toString(cmd));
		outputErrorStreamToLog(process.getErrorStream());
		return process.waitFor();
	}

	public static String executeOsXCommandWithOutput(String[] cmd)
		throws Exception {
		Process process = Runtime.getRuntime().exec(cmd);
		log.debug("Process started for cmdline " + Arrays.toString(cmd));
		String output;
		try (InputStream stream = process.getInputStream()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			StringBuilder sb = new StringBuilder("\n");
			String s;
			while ((s = br.readLine()) != null) {
				sb.append("\t").append(s).append("\n");
			}
			output = sb.toString();
		}
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
			sb.append("\t").append(s).append("\n");
		}
		String output = sb.toString();
		if (!output.trim().isEmpty()) {
			log.debug(output);
		}
		stream.close();
	}

	public static String getBackendType(Class<?> c) throws Exception {
		return getValueFromConfig(c, "backendType");
	}

	public static String getDeviceName(Class<?> c) throws Exception {
		return getValueFromConfig(c, "deviceName");
	}

	public static String getImagePath(Class<?> c) throws Exception {
		return getValueFromConfig(c, "defaultImagesPath") + USER_IMAGE;
	}

	public static String getPingIconPathIOS(Class<?> c) throws Exception {
		return getValueFromConfig(c, "iosImagesPath") + IOS_PING_IMAGE;
	}

	public static String getHotPingIconPathIOS(Class<?> c) throws Exception {
		return getValueFromConfig(c, "iosImagesPath") + IOS_HOT_PING_IMAGE;
	}

	public static String getAvatarWithClockIconPathIOS(Class<?> c)
		throws Exception {
		return getValueFromConfig(c, "iosImagesPath") + IOS_AVATAR_CLOCK_IMAGE;
	}

	public static String getImagesPath(Class<?> c) throws Exception {
		return getValueFromConfig(c, "defaultImagesPath");
	}

	public static String getMediaBarPlayIconPath(Class<?> c) throws Exception {
		return getValueFromConfig(c, "defaultImagesPath") + MEDIABAR_PLAY_IMAGE;
	}

	public static String getMediaBarPauseIconPath(Class<?> c) throws Exception {
		return getValueFromConfig(c, "defaultImagesPath")
			+ MEDIABAR_PAUSE_IMAGE;
	}

	public static String getPictureResultsPathFromConfig(Class<?> c)
		throws Exception {
		return getValueFromConfig(c, "pictureResultsPath");
	}

	private final static Semaphore sem = new Semaphore(1);
	private final static Map<String, Optional<String>> cachedConfig = new HashMap<>();

	private static Optional<String> getValueFromConfigFile(Class<?> c,
		String key, String resourcePath) throws Exception {
		final String configKey = String.format("%s:%s", resourcePath, key);
		if (cachedConfig.containsKey(configKey)) {
			return cachedConfig.get(configKey);
		}

		final int maxTry = 3;
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
				if (p.containsKey(key)) {
					cachedConfig.put(configKey,
						Optional.of((String) p.get(key)));
				} else {
					cachedConfig.put(configKey, Optional.empty());
				}
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

	private static final String PROJECT_CONFIG = "Configuration.cnf";

	public static Optional<String> getOptionalValueFromConfig(Class<?> c,
		String key) throws Exception {
		return getValueFromConfigFile(c, key, PROJECT_CONFIG);
	}

	public static String getValueFromConfig(Class<?> c, String key)
		throws Exception {
		final Optional<String> value = getValueFromConfigFile(c, key,
			PROJECT_CONFIG);
		if (value.isPresent()) {
			return value.get();
		} else {
			throw new RuntimeException(String.format(
				"There is no '%s' property in the '%s' configuration file",
				key, PROJECT_CONFIG));
		}
	}

	private static final String COMMON_CONFIG = "CommonConfiguration.cnf";

	public static Optional<String> getOptionalValueFromCommonConfig(Class<?> c,
		String key) throws Exception {
		return getValueFromConfigFile(c, key, COMMON_CONFIG);
	}

	public static String getValueFromCommonConfig(Class<?> c, String key)
		throws Exception {
		final Optional<String> value = getValueFromConfigFile(c, key,
			COMMON_CONFIG);
		if (value.isPresent()) {
			return value.get();
		} else {
			throw new RuntimeException(
				String.format(
					"There is no '%s' property in the '%s' common configuration file",
					key, COMMON_CONFIG));
		}
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
		final String currentBackendType = getBackendType(c);
		switch (currentBackendType.toLowerCase()) {
			case "edge":
				return getValueFromCommonConfig(c, "edgeBackendUrl");
			case "staging":
			case "benny":
			case "gregor":
			case "rene":
			case "lipis":
			case "chris":
				return getValueFromCommonConfig(c, "stagingBackendUrl");
			case "production":
				return getValueFromCommonConfig(c, "productionBackendUrl");
			default:
				throw new RuntimeException(String.format(
					"Non supported backend type '%s'", currentBackendType));
		}
	}

	public static String getAndroidAppiumUrlFromConfig(Class<?> c)
		throws Exception {
		return getValueFromConfig(c, "androidAppiumUrl");
	}

	public static String getIosAppiumUrlFromConfig(Class<?> c) throws Exception {
		return getValueFromConfig(c, "iosAppiumUrl");
	}

	public static Boolean getIsSimulatorFromConfig(Class<?> c) throws Exception {
		return (getValueFromConfig(c, "isSimulator").equals("true"));
	}

	public static String getSwipeScriptPathPrefix(Class<?> c) throws Exception {
		return getValueFromConfig(c, "swipeScriptPathPrefix");
	}

	public static String getWebAppApplicationPathFromConfig(Class<?> c)
		throws Exception {
		String applicationPath = getValueFromConfig(c, "webappApplicationPath");
		if (!applicationPath.equals("")) {
			return applicationPath;
		} else {
			final String currentBackendType = getBackendType(c);
			switch (currentBackendType.toLowerCase()) {
				case "edge":
					return getValueFromConfig(c, "webappEdgeApplicationPath");
				case "staging":
					return getValueFromConfig(c, "webappStagingApplicationPath");
				case "production":
					return getValueFromConfig(c, "webappProductionApplicationPath");
				case "benny":
					return getValueFromConfig(c, "webappBennyApplicationPath");
				case "gregor":
					return getValueFromConfig(c, "webappGregorApplicationPath");
				case "rene":
					return getValueFromConfig(c, "webappReneApplicationPath");
				case "lipis":
					return getValueFromConfig(c, "webappLipisApplicationPath");
				case "chris":
					return getValueFromConfig(c, "webappChrisApplicationPath");
				default:
					throw new RuntimeException(String.format(
						"Non supported backend type '%s'", currentBackendType));
			}
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

	public static String getAndroidWaitActivitiesFromConfig(Class<?> c)
		throws Exception {
		return getValueFromConfig(c, "waitActivities");
	}

	public static String getSimulatorImagesPathFromConfig(Class<?> c)
		throws Exception {
		return getValueFromConfig(c, "iosImagesPath");
	}

	public static String getAndroidPackageFromConfig(Class<?> c) {
		try {
			return getValueFromConfig(c, "package");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getUserPicturePathFromConfig(Class<?> c)
		throws Exception {
		return getValueFromConfig(c, "pathToUserpic");
	}

	public static String getUserAddressBookFromConfig(Class<?> c)
		throws Exception {
		return getValueFromConfig(c, "pathToAddressBook");
	}

	public static String generateGUID() {
		return UUID.randomUUID().toString();
	}

	public static String generateRandomString(int lengh) {
		return RandomStringUtils.randomAlphanumeric(lengh);
	}

	public static String generateRandomNumericString(int length) {
		return RandomStringUtils.randomNumeric(length);
	}

	private static String generateRandomStringFromTargetStringSetWithLengh(
		int numberOfCharacters, String characters) {
		StringBuilder result = new StringBuilder();
		while (numberOfCharacters > 0) {
			Random rand = new Random();
			result.append(characters.charAt(rand.nextInt(characters.length())));
			numberOfCharacters--;
		}
		return result.toString();
	}

	public static String generateRandomStringFromAlphanumericPlusSymbolsWithLengh(
		int numberOfCharacters) {
		return generateRandomStringFromTargetStringSetWithLengh(
			numberOfCharacters, ALPHANUMERIC_PLUS_SYMBOLS);
	}

	public static boolean getIsTabletFromConfig(Class<?> c) throws Exception {
		final Optional<String> value = getOptionalValueFromConfig(c, "isTablet");
		return value.isPresent() ? Boolean.valueOf(value.get()) : false;
	}

	public static String getJenkinsSuperUserLogin(Class<?> c) throws Exception {
		return getValueFromCommonConfig(c, "jenkinsSuLogin");
	}

	public static Optional<String> getRCNotificationsRecepients(Class<?> c)
		throws Exception {
		return getOptionalValueFromConfig(c, "rcNotificationsRecepients");
	}

	public static String getJenkinsSuperUserPassword(Class<?> c)
		throws Exception {
		return getValueFromCommonConfig(c, "jenkinsSuPassword");
	}

	public static String getDefaultCallingServiceUrlFromConfig(Class<?> c)
		throws Exception {
		return getValueFromCommonConfig(c, "defaultCallingServiceUrl");
	}

	public static Optional<BufferedImage> getElementScreenshot(
		WebElement element, AppiumDriver<? extends WebElement> driver)
		throws Exception {
		return getElementScreenshot(element, driver, "iPhone 6");
	}

	public static Optional<BufferedImage> getElementScreenshot(
		WebElement element, AppiumDriver<? extends WebElement> driver,
		String deviceName) throws Exception {
		int multiply = 3;
		if (deviceName.equals("iPhone 6") || deviceName.equals("iPad Air")) {
			multiply = 2;
		} else if (deviceName.equals("Android Device")) {
			multiply = 1;
		}
		org.openqa.selenium.Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		final Optional<BufferedImage> screenshot = DriverUtils
			.takeFullScreenShot((ZetaDriver) driver);
		if (screenshot.isPresent()) {
			return Optional.of(screenshot.get()
				.getSubimage(elementLocation.x * multiply,
					elementLocation.y * multiply,
					elementSize.width * multiply,
					elementSize.height * multiply));
		} else {
			return Optional.empty();
		}
	}

	public static Optional<BufferedImage> getElementScreenshot(
		WebElement element, ZetaWebAppDriver driver) throws Exception {
		org.openqa.selenium.Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		final Optional<BufferedImage> screenshot = DriverUtils
			.takeFullScreenShot(driver);
		if (screenshot.isPresent()) {
			return Optional.of(screenshot.get().getSubimage(elementLocation.x,
				elementLocation.y, elementSize.width, elementSize.height));
		} else {
			return Optional.empty();
		}
	}

	public static void blockTcpForAppName(String appName) throws Exception {
		final String blockTcpForAppCmd = "echo "
			+ getJenkinsSuperUserPassword(CommonUtils.class) + "| sudo -S "
			+ TCPBLOCK_PREFIX_PATH + "tcpblock -a " + appName;
		try {
			executeOsXCommand(new String[]{"/bin/bash", "-c",
				blockTcpForAppCmd});
			log.debug(executeOsXCommandWithOutput(new String[]{"/bin/bash",
				"-c", TCPBLOCK_PREFIX_PATH + "tcpblock -g"}));
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
			executeOsXCommand(new String[]{"/bin/bash", "-c",
				enableTcpForAppCmd});
			log.debug(executeOsXCommandWithOutput(new String[]{"/bin/bash",
				"-c", TCPBLOCK_PREFIX_PATH + "tcpblock -g"}));
		} catch (Exception e) {
			log.error("TCP connections for " + appName
				+ " were not enabled. Make sure tcpblock is installed.");
		}
	}

	public static void defineNoHeadlessEnvironment() {
		System.setProperty("java.awt.headless", "false");
	}

	public static String encodeSHA256Base64(String item) throws Exception {
		final MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(item.getBytes("UTF-8"));
		final byte[] digest = md.digest();
		return Base64.encodeBase64String(digest);
	}

	public static String getDefaultEmailListenerServiceHostFromConfig(Class<?> c)
		throws Exception {
		return getValueFromCommonConfig(c, "defaultEmailListenerServiceHost");
	}

	public static String getDefaultEmailListenerServicePortFromConfig(Class<?> c)
		throws Exception {
		return getValueFromCommonConfig(c, "defaultEmailListenerServicePort");
	}

	public static boolean getMakeScreenshotsFromConfig(Class<?> c)
		throws Exception {
		return Boolean.valueOf(getValueFromCommonConfig(c, "makeScreenshots"));
	}

	public static boolean getInitNoteIpFromConfig(Class<?> c) throws Exception {
		return Boolean.valueOf(getValueFromCommonConfig(c, "initNodeIp"));
	}

	public static String getTestrailServerUrlFromConfig(Class<?> c) throws Exception {
		return getValueFromCommonConfig(c, "testrailServerUrl");
	}

	public static String getTestrailUsernameFromConfig(Class<?> c) throws Exception {
		return getValueFromConfig(c, "testrailUser");
	}

	public static String getTestrailTokenFromConfig(Class<?> c) throws Exception {
		return getValueFromConfig(c, "testrailToken");
	}

	public static Optional<String> getTestrailProjectNameFromConfig(Class<?> c)
		throws Exception {
		return getOptionalValueFromCommonConfig(c, "testrailProjectName");
	}

	public static Optional<String> getTestrailPlanNameFromConfig(Class<?> c)
		throws Exception {
		return getOptionalValueFromCommonConfig(c, "testrailPlanName");
	}

	public static Optional<String> getTestrailRunNameFromConfig(Class<?> c)
		throws Exception {
		return getOptionalValueFromCommonConfig(c, "testrailRunName");
	}

	public static Optional<String> getTestrailRunConfigNameFromConfig(Class<?> c)
		throws Exception {
		return getOptionalValueFromCommonConfig(c, "testrailRunConfigName");
	}

	public static String getNativeLibsRootFromConfig(Class<?> c) throws Exception {
		return getValueFromCommonConfig(c, "nativeLibsRoot");
	}

	public static boolean getSyncIsAutomated(Class<?> c) throws Exception {
		return getValueFromCommonConfig(c, "syncIsAutomated").toLowerCase().equals("true");
	}

	public static boolean getSyncIsMuted(Class<?> c) throws Exception {
		return getValueFromCommonConfig(c, "syncIsMuted").toLowerCase().equals("true");
	}

	public static String generateRandomXdigits(int i) {
		Random rand = new Random();
		long random = (long) (Math.pow(10, i - 1)) * (rand.nextInt(8) + 1)
			+ (long) rand.nextInt((int) (Math.pow(10, i - 1)));
		return Long.toString(Math.abs(random));
	}

	public static String getPlatformVersionFromConfig(Class<?> cls)
		throws Exception {
		return getValueFromConfig(cls, "platformVersion");
	}

}
