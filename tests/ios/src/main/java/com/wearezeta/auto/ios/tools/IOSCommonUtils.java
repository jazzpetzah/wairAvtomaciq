package com.wearezeta.auto.ios.tools;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;


import org.apache.log4j.Logger;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.ios.pages.IOSPage;

public class IOSCommonUtils {
    private static Logger log = ZetaLogger.getLog(IOSCommonUtils.class.getSimpleName());

    public static BuildVersionInfo readClientVersionFromPlist() {
        String clientBuild = "no info";
        String zmessagingBuild = "no info";
        try {
            File file = new File(getIosClientInfoPlistFromConfig(IOSCommonUtils.class));
            NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(file);
            clientBuild = rootDict.objectForKey("CFBundleVersion").toString();
            String trackInfo = rootDict.objectForKey("WireBundleId").toString();
            String majorVersion = rootDict.objectForKey("CFBundleShortVersionString").toString();
            trackInfo = trackInfo.substring(trackInfo.indexOf("-") + 1, trackInfo.length());
            clientBuild = majorVersion + "." + clientBuild + "-" + trackInfo;
            log.info("Got build number: " + clientBuild);
            NSDictionary zcBuildInfo = (NSDictionary) rootDict.objectForKey("ZCBuildInfo");
            NSDictionary zmessagingDict = (NSDictionary) zcBuildInfo.objectForKey("zmessaging");
            zmessagingBuild = zmessagingDict.objectForKey("version").toString();
        } catch (Exception ex) {
            log.error("Failed to read iOS client properties.\n"
                    + ex.getMessage());
        }
        return new BuildVersionInfo(clientBuild, zmessagingBuild);
    }

    public static ClientDeviceInfo readDeviceInfo() throws Exception {
        String os = "iOS";
        String osBuild = (String) IOSPage.executeScript("UIATarget.localTarget().systemVersion();");
        String deviceName = (String) IOSPage.executeScript("UIATarget.localTarget().model();");
        String gsmNetworkType = "";
        Boolean isWifiEnabled = true;

        return new ClientDeviceInfo(os, osBuild, deviceName, gsmNetworkType, isWifiEnabled);
    }

    public static String getIosClientInfoPlistFromConfig(Class<?> c) throws Exception {
        return CommonUtils.getValueFromConfig(c, "iosClientInfoPlist");
    }

    /**
     * @param deviceName           either iPhone or iPad
     * @param shouldThrowException set this to true if you want the RuntimeException to be thrown
     *                             if no device is found. Setting this to false will return null
     *                             value in case if no device is connected
     * @return UDID number of connected iDevice
     * @throws Exception
     */
    private static String getConnectediOSDeviceUDID(String deviceName,
                                                    boolean shouldThrowException) throws Exception {
        final String result = CommonUtils.executeOsXCommandWithOutput(new String[]{
                "/bin/bash",
                "-c",
                "system_profiler SPUSBDataType | sed -n '/"
                        + deviceName
                        + "/,/Serial/p' | grep 'Serial Number:' | awk -F ': ' '{print $2}'"});
        if (result.length() > 0) {
            return result.trim();
        } else {
            if (shouldThrowException) {
                throw new RuntimeException("Cannot detect any connected iPhone device");
            } else {
                return null;
            }
        }
    }

    public static String getConnectediPhoneUDID(boolean shouldThrowException)
            throws Exception {
        return getConnectediOSDeviceUDID("iPhone", shouldThrowException);
    }

    private static String getIOSSimulatorIdByDeviceName(String deviceName) throws Exception {
        return CommonUtils
                .executeOsXCommandWithOutput(
                        new String[]{
                                "/bin/bash",
                                "-c",
                                "xcrun simctl list devices | grep -v 'unavailable' | grep -i '"
                                        + deviceName
                                        + " (' | tail -n 1 | cut -d '(' -f2 | cut -d ')' -f1"})
                .trim();
    }

    public static void collectSimulatorLogs(String deviceName) throws Exception {
        log.debug("iOS Simulator Logs:");
        final String simId = getIOSSimulatorIdByDeviceName(deviceName);
        final File logFile = new File(String.format("%s/Library/Logs/CoreSimulator/%s/system.log",
                System.getProperty("user.home"), simId));
        if (logFile.exists()) {
            log.debug(new String(Files.readAllBytes(logFile.toPath()), Charset.forName("UTF-8")) +
                    "\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        } else {
            log.error(String.format("There is no log file at the expected path %s\n\n\n\n\n",
                    logFile.getCanonicalPath()));
        }
    }

    public static String getPerfReportPathFromConfig(Class<?> c) throws Exception {
        return CommonUtils.getValueFromConfig(c, "perfReportPath");
    }
}
