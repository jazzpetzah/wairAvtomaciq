package com.wearezeta.auto.web.common;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public class WebAppExecutionContext {

	private static final Logger log = ZetaLogger
			.getLog(WebAppExecutionContext.class.getSimpleName());

	private static String osName;
	private static String osVersion;
	private static Browser browser = null;
	private static String browserName;
	private static String browserVersion;

	static {
		osName = System.getProperty("com.wire.os.name");
		osVersion = System.getProperty("com.wire.os.version");
		browserName = System.getProperty("com.wire.browser.name");
		browserVersion = System.getProperty("com.wire.browser.version");
		browser = Browser.fromString(browserName);
	}

	public static String getOsName() {
		return osName;
	}

	public static String getOsVersion() {
		return osVersion;
	}

	public static String getPlatform() {
		String platform = osName;
		if (osVersion != null && !osVersion.equals("")) {
			platform = platform + " " + osVersion;
		}
		log.debug("Platform is: " + platform);
		return platform;
	}

	public static boolean isCurrentPlatformWindows() {
		return getPlatform().toLowerCase().contains("win");
	}

	public static Browser getBrowser() {
		log.debug("Browser is: " + browser);
		return browser;
	}

	public static String getBrowserName() {
		return browserName;
	}

	public static String getBrowserVersion() {
		return browserVersion;
	}
}
