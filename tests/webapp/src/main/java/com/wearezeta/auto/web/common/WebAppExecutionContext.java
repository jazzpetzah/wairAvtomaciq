package com.wearezeta.auto.web.common;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppConstants.Browser;

public class WebAppExecutionContext {

	private static final Logger log = ZetaLogger
			.getLog(WebAppExecutionContext.class.getSimpleName());

	private static String currentPlatform = "";
	static {
		try {
			currentPlatform = WebCommonUtils
					.getPlatformNameFromConfig(WebAppExecutionContext.class);
		} catch (Exception e) {
			e.printStackTrace();
			log.fatal("Failed to read platform name from config file.");
		}
	}

	public static String getCurrentPlatform() {
		return currentPlatform;
	}

	private static Browser currentBrowser = null;
	static {
		try {
			if (currentPlatform.toLowerCase().contains(
					Browser.Opera.toString().toLowerCase())) {
				currentBrowser = Browser.Opera;
			} else {
				currentBrowser = Browser
						.fromString(WebCommonUtils
								.getWebAppBrowserNameFromConfig(WebAppExecutionContext.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.fatal("Failed to read browser name from config file.");
		}
	}

	public static Browser getCurrentBrowser() {
		return currentBrowser;
	}

	public static final class Calling {
		private static final Browser[] BROWSERS_WITH_CALLING_SUPPORT = new Browser[] {
				Browser.Chrome, Browser.Firefox, Browser.Opera };

		public static boolean isSupportedInCurrentBrowser() {
			return Browser.isSubSetContains(BROWSERS_WITH_CALLING_SUPPORT,
					getCurrentBrowser());
		}
	}

	public static final class ProfileManagement {
		private static final Browser[] BROWSERS_WITH_PROFILE_MANAGEMENT_SUPPORT = new Browser[] {
				Browser.Chrome, Browser.Firefox };

		public static boolean isSupportedInCurrentBrowser() {
			return Browser.isSubSetContains(
					BROWSERS_WITH_PROFILE_MANAGEMENT_SUPPORT,
					getCurrentBrowser());
		}
	}

	public static final class LoggingManagement {
		// Logging feature crashes IE }:@
		private static final Browser[] BROWSERS_WITH_LOGGING_SUPPORT = new Browser[] {
				Browser.Chrome, Browser.Firefox, Browser.Opera, Browser.Safari };

		public static boolean isSupportedInCurrentBrowser() {
			return Browser.isSubSetContains(BROWSERS_WITH_LOGGING_SUPPORT,
					getCurrentBrowser());
		}
	}
}
