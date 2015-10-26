package com.wearezeta.auto.win.common;

import com.wearezeta.auto.common.Platform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class WinExecutionContext {
	public static final Platform CURRENT_PLATFORM = Platform.Mac;
	public static final Platform CURRENT_SECONDARY_PLATFORM = Platform.Web;

	public static final String EXTENDED_LOGGING_LEVEL = System.getProperty(
			"extendedLoggingLevel", "SEVERE");

	public static final String WINIUM_URL = System.getProperty(
			"com.wire.winium.url", "http://localhost:9999/");

        public static final String WIRE_APP_FOLDER = System.getProperty(
			"com.wire.app.folder", "C:\\Users\\Michael\\AppData\\Local\\wire\\");
        
	public static final String WIRE_APP_PATH = System.getProperty(
			"com.wire.app.path", "app-2.0.2443\\Wire.exe");

	public static final String OS_NAME = System.getProperty("com.wire.os.name",
			"Win");

	public static final String OS_VERSION = System.getProperty(
			"com.wire.os.version", "7");

	public static final String BROWSER_NAME = System.getProperty(
			"com.wire.browser.name", "Chrome");

	public static final String BROWSER_VERSION = System.getProperty(
			"com.wire.browser.version", "45");

	public static final String WINIUM_PATH = System.getProperty(
			"com.wire.winium.path", "C:\\Users\\Michael\\Desktop\\Winium.Desktop.Driver.exe");
        
        public static final String CHROMEDRIVER_PATH = System.getProperty(
			"com.wire.chromedriver.path", "C:\\Users\\Michael\\Desktop\\chromedriver.exe");

	public static final String ENV = System.getProperty("com.wire.environment",
			"Staging");

	public static final String ENV_URL = System.getProperty(
			"com.wire.environment.url",
			"https://wire-webapp-staging.zinfra.io/");

	public static final String USER_HOME = System.getProperty("user.home");

	public static final String USERNAME = System.getProperty("user.name");

	public static final HashMap<String, BufferedImage> SCREENSHOTS = new HashMap<>();

}
