package com.wearezeta.auto.web.common;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public class WebAppExecutionContext {

    @SuppressWarnings("unused")
    private static final Logger LOG = ZetaLogger.getLog(WebAppExecutionContext.class.getSimpleName());

    private static final String OS_NAME;
    private static final String OS_VERSION;
    private static Browser BROWSER = null;
    private static final String BROWSER_NAME;
    private static final String BROWSER_VERSION;

    static {
        OS_NAME = System.getProperty("com.wire.os.name");
        OS_VERSION = System.getProperty("com.wire.os.version");
        BROWSER_NAME = System.getProperty("com.wire.browser.name");
        BROWSER_VERSION = System.getProperty("com.wire.browser.version");
        BROWSER = Browser.fromString(BROWSER_NAME);
    }

    public static String getOsName() {
        return OS_NAME;
    }

    public static String getOsVersion() {
        return OS_VERSION;
    }

    public static String getPlatform() {
        String platform = OS_NAME;
        if (OS_VERSION != null && !OS_VERSION.equals("")) {
            platform = platform + " " + OS_VERSION;
        }
        return platform;
    }

    public static boolean isCurrentPlatformWindows() {
        return getPlatform().toLowerCase().contains("win");
    }

    public static Browser getBrowser() {
        return BROWSER;
    }

    public static String getBrowserName() {
        return BROWSER_NAME;
    }

    public static String getBrowserVersion() {
        return BROWSER_VERSION;
    }
}
