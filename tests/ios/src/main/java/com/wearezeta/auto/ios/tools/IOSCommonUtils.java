package com.wearezeta.auto.ios.tools;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    private static NSDictionary parsePlist(File plist) throws Exception {
        if (!plist.exists()) {
            throw new IllegalArgumentException(String.format(
                    "Please make sure the file %s exists and is accessible", plist.getCanonicalPath())
            );
        }
        return (NSDictionary) PropertyListParser.parse(plist);
    }

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
        String osBuild = (String) IOSPage.executeScript("UIATarget.localTarget().systemVersion();");
        String deviceName = (String) IOSPage.executeScript("UIATarget.localTarget().model();");
        String gsmNetworkType = "";
        Boolean isWifiEnabled = true;

        return new ClientDeviceInfo(os, osBuild, deviceName, gsmNetworkType, isWifiEnabled);
    }

    /**
     * @param deviceName           either iPhone or iPad
     * @param shouldThrowException set this to true if you want the RuntimeException to be thrown
     *                             if no device is found. Setting this to false will return null
     *                             value in case if no device is connected
     * @return UDID number of connected iDevice
     * @throws Exception
     */
    private static String getConnectedIOSDeviceUDID(String deviceName,
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

    public static String getConnectediPhoneUDID(boolean shouldThrowException) throws Exception {
        return getConnectedIOSDeviceUDID("iPhone", shouldThrowException);
    }

    public static String getPerfReportPathFromConfig(Class<?> c) throws Exception {
        return CommonUtils.getValueFromConfig(c, "perfReportPath");
    }

    private static final int BUFFER_SIZE = 4096;

    private static void extractFile(ZipInputStream zipIn, File resultFile) throws IOException {
        byte[] bytesIn = new byte[BUFFER_SIZE];
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(resultFile))) {
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }

    public static File extractAppFromIpa(File ipaFile) throws Exception {
        if (!ipaFile.exists()) {
            throw new IllegalArgumentException(String.format(
                    "Please make sure the file %s exists and is accessible", ipaFile.getCanonicalPath())
            );
        }
        final Path root = Files.createTempDirectory(null);
        Optional<File> result = Optional.empty();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(ipaFile))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                try {
                    final String entryName = zipEntry.getName();
                    final File currentPath = new File(root.toString() + File.separator + entryName);
                    if (!result.isPresent() && entryName.endsWith(".app/")) {
                        result = Optional.of(currentPath);
                    }
                    if (entryName.contains(".app")) {
                        if (zipEntry.isDirectory()) {
                            if (!currentPath.mkdirs()) {
                                throw new IllegalStateException(String.format(
                                        "Cannot create %s output folder", currentPath.getCanonicalPath())
                                );
                            }
                        } else {
                            extractFile(zis, currentPath);
                        }
                    }
                } finally {
                    zis.closeEntry();
                }
            }
        }
        return result.orElseThrow(
                () -> new IllegalArgumentException(String.format("Cannot find a compressed .app inside %s",
                        ipaFile.getAbsolutePath()))
        );
    }

    public static String getBundleId(File plist) throws Exception {
        final NSDictionary rootDict = parsePlist(plist);
        return rootDict.objectForKey("WireBundleId").toString();
    }
}
