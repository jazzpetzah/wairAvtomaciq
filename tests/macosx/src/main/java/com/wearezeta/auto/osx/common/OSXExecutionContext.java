package com.wearezeta.auto.osx.common;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class OSXExecutionContext {

	private static final Logger log = ZetaLogger
			.getLog(OSXExecutionContext.class.getSimpleName());

	public static String appiumUrl;

	public static String wirePath;

	public static String userDocuments = System.getProperty("user.home")
			+ "/Documents/";

	public static HashMap<String, BufferedImage> screenshots = new HashMap<String, BufferedImage>();

	static {
		try {
			appiumUrl = CommonUtils
					.getOsxAppiumUrlFromConfig(OSXExecutionContext.class);
		} catch (Exception e) {
			log.debug("Failed to read Appium URL from config file. "
					+ "Setting default value: http://localhost:4622/wd/hub");
			appiumUrl = "http://localhost:4622/wd/hub";
		}

		try {
			wirePath = CommonUtils
					.getOsxApplicationPathFromConfig(OSXExecutionContext.class);
		} catch (Exception e) {
			log.debug("Failed to read Wire path from config file. "
					+ "Setting default value: Wire");
			wirePath = "Wire";
		}
	}
}
