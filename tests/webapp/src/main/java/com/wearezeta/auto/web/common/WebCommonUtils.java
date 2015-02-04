package com.wearezeta.auto.web.common;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class WebCommonUtils extends CommonUtils {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(WebCommonUtils.class
			.getSimpleName());

	public static String getWebAppBrowserNameFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "browserName");
	}

	public static String getPlatformNameFromConfig(Class<?> c)
			throws IOException {
		return getValueFromConfig(c, "platformName");
	}
}
