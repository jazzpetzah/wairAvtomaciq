package com.wearezeta.auto.osx.common;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class OSXExecutionContext {

	private static final Logger LOG = ZetaLogger
			.getLog(OSXExecutionContext.class.getName());

	public static String appiumUrl;

	public static String wirePath;

	public static String wireConfigDomain;

	public static final String USER_HOME = System.getProperty("user.home");

	public static final String USERNAME = System.getProperty("user.name");

	public static HashMap<String, BufferedImage> screenshots = new HashMap<String, BufferedImage>();

	static {
		try {
			appiumUrl = CommonUtils
					.getOsxAppiumUrlFromConfig(OSXExecutionContext.class);
		} catch (Exception e) {
			LOG.debug("Failed to read Appium URL from config file. "
					+ "Setting default value: http://localhost:4622/wd/hub");
			appiumUrl = "http://localhost:4622/wd/hub";
		}

		try {
			wirePath = CommonUtils
					.getOsxApplicationPathFromConfig(OSXExecutionContext.class);
		} catch (Exception e) {
			LOG.debug("Failed to read Wire path from config file. "
					+ "Setting default value: Wire");
			wirePath = "Wire";
		}

		try {
			wireConfigDomain = OSXCommonUtils
					.getWireConfigDomainFromConfig(OSXExecutionContext.class);
		} catch (Exception e) {
			LOG.debug("Failed to read config domain for Wire. "
					+ "Setting default value: com.wearezeta.zclient.mac.development");
			wireConfigDomain = ConfigurationDomainEnum.DEVELOPMENT.getDomain();
		}
	}
}
