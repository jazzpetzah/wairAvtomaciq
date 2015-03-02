package com.wearezeta.auto.web.common;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public class WebAppExecutionContext {

	private static final Logger log = ZetaLogger.getLog(WebAppExecutionContext.class.getSimpleName());
	
	public static String seleniumNodeIp = null;
	
	public static String temporaryScriptsLocation = null;
	
	public static String browserName = null;
	
	static {
		try {
			browserName = WebCommonUtils.getWebAppBrowserNameFromConfig(WebAppExecutionContext.class);
		} catch (Exception e) {
			log.fatal("Failed to read browser name from config file.");
		}
	}
}
