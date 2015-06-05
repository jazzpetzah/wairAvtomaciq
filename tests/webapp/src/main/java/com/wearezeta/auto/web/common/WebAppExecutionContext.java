package com.wearezeta.auto.web.common;

import java.util.Optional;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public class WebAppExecutionContext {

	private static final Logger log = ZetaLogger
			.getLog(WebAppExecutionContext.class.getSimpleName());

	private static String platform = "";
	static {
		String osName = System.getProperty("com.wire.os.name");
		Optional<String> osVersion = Optional.of(System
				.getProperty("com.wire.os.version"));

		platform = osName;
		if (osVersion.isPresent()) {
			platform = platform + " " + osVersion.get();
		}
	}

	public static String getPlatform() {
		log.debug("Platform is: " + platform);
		return platform;
	}

	public static boolean isCurrentPlatfromWindows() {
		return getPlatform().toLowerCase().contains("win");
	}

	private static Browser browser = null;
	private static String browserName;
	private static String browserVersion;

	static {
		browserName = System.getProperty("com.wire.browser.name");
		browserVersion = System.getProperty("com.wire.browser.version");
		browser = Browser.fromString(browserName);
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
