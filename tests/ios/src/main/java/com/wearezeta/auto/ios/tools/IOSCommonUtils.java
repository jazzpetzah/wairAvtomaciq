package com.wearezeta.auto.ios.tools;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;

public class IOSCommonUtils {
    private static Logger log = ZetaLogger.getLog(IOSCommonUtils.class.getSimpleName());

    public static BuildVersionInfo readClientVersionFromPlist() {
        String clientBuild = "no info";
        String zmessagingBuild = "no info";
        try {
            // FIXME: Extract plist file directly from ipa
//            File file = new File(getIosClientInfoPlistFromConfig(IOSCommonUtils.class));
//            NSDictionary rootDict = parsePlist(file);
//            clientBuild = rootDict.objectForKey("CFBundleVersion").toString();
//            String trackInfo = rootDict.objectForKey("WireBundleId").toString();
//            String majorVersion = rootDict.objectForKey("CFBundleShortVersionString").toString();
//            trackInfo = trackInfo.substring(trackInfo.indexOf("-") + 1, trackInfo.length());
//            clientBuild = majorVersion + "." + clientBuild + "-" + trackInfo;
//            log.info("Got build number: " + clientBuild);
//            NSDictionary zcBuildInfo = (NSDictionary) rootDict.objectForKey("ZCBuildInfo");
//            NSDictionary zmessagingDict = (NSDictionary) zcBuildInfo.objectForKey("zmessaging");
//            zmessagingBuild = zmessagingDict.objectForKey("version").toString();
        } catch (Exception ex) {
            log.error("Failed to read iOS client properties.\n"
                    + ex.getMessage());
        }
        return new BuildVersionInfo(clientBuild, zmessagingBuild);
    }

    public static ClientDeviceInfo readDeviceInfo() throws Exception {
        String os = "iOS";
        // FIXME: get the necessary information
        String osBuild = "";
        String deviceName = "";
        String gsmNetworkType = "";
        Boolean isWifiEnabled = true;

        return new ClientDeviceInfo(os, osBuild, deviceName, gsmNetworkType, isWifiEnabled);
    }

    public static String getPerfReportPathFromConfig(Class<?> c) throws Exception {
        return CommonUtils.getValueFromConfig(c, "perfReportPath");
    }
}
