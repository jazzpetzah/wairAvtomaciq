package com.wearezeta.auto.osx.common;

import com.wearezeta.auto.common.Platform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class OSXExecutionContext {
	public static final Platform CURRENT_PLATFORM = Platform.Mac;
	public static final Platform CURRENT_SECONDARY_PLATFORM = Platform.Web;

	public static final String APPIUM_HUB_URL = System.getProperty(
			"com.wire.appium.hub.url", "http://localhost:4622/wd/hub");

	public static final String WIRE_APP_PATH = System.getProperty(
			"com.wire.app.path", "/Applications/WireInternal.app");

	public static final String CONFIG_DOMAIN = System.getProperty(
			"wireConfigDomain", "com.wearezeta.zclient.mac.development");

	public static final String OS_NAME = System.getProperty("com.wire.os.name",
			"Mac");

	public static final String OS_VERSION = System.getProperty(
			"com.wire.os.version", "10");

	public static final String BROWSER_NAME = System.getProperty(
			"com.wire.browser.name", "Chrome");

	public static final String BROWSER_VERSION = System.getProperty(
			"com.wire.browser.version", "45");

	public static final String ELECTRON_SUFFIX = System.getProperty(
			"com.wire.electron.path.suffix", "/Contents/MacOS/Electron");

	public static final String CHROMEDRIVER_PATH = System.getProperty(
			"com.wire.chromedriver.path", "/Applications/chromedriver");

	public static final String ENV = System.getProperty("com.wire.environment",
			"Staging");

	public static final String USER_HOME = System.getProperty("user.home");

	public static final String USERNAME = System.getProperty("user.name");

	public static final HashMap<String, BufferedImage> SCREENSHOTS = new HashMap<>();

}
