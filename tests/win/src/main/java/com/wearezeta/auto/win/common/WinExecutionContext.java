package com.wearezeta.auto.win.common;

import com.wearezeta.auto.common.Platform;

public class WinExecutionContext {

    public static final Platform CURRENT_PLATFORM = Platform.Mac;
    public static final Platform CURRENT_SECONDARY_PLATFORM = Platform.Web;
    
    public static final boolean KEEP_DATABASE = Boolean.getBoolean("com.wire.database.keep");

    public static final String EXTENDED_LOGGING_LEVEL = System.getProperty(
            "extendedLoggingLevel", "SEVERE");

    public static final String WINIUM_URL = System.getProperty(
            "com.wire.winium.url", "http://localhost:9999/");
    
    public static final String WIRE_APP_VERSION = System.getProperty(
            "com.wire.app.version", "2.6.2599");

    public static final String WIRE_APP_FOLDER = System.getProperty(
            "com.wire.app.folder", 
            String.format("%s\\AppData\\Local\\wire\\app-%s\\", System.getProperty("user.home"), WIRE_APP_VERSION));

    public static final String WIRE_APP_EXECUTABLE = System.getProperty(
            "com.wire.app.executable", "Wire.exe");

    public static final String WIRE_APP_CACHE_FOLDER = System.getProperty(
            "com.wire.app.cache.folder", 
            String.format("%s\\AppData\\Roaming\\Wire\\", System.getProperty("user.home")));

    public static final String OS_NAME = System.getProperty("com.wire.os.name",
            "Win");

    public static final String OS_VERSION = System.getProperty(
            "com.wire.os.version", "7");

    public static final String BROWSER_NAME = System.getProperty(
            "com.wire.browser.name", "chrome");

    public static final String BROWSER_VERSION = System.getProperty(
            "com.wire.browser.version", "49");

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
}
