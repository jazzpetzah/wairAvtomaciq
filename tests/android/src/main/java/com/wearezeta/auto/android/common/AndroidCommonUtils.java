package com.wearezeta.auto.android.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;

public class AndroidCommonUtils extends CommonUtils {
    private static final Logger log = ZetaLogger
            .getLog(AndroidCommonUtils.class.getSimpleName());

    private static final String stagingBackend = "[\"https://staging-nginz-https.zinfra.io\", \"https://staging-nginz-ssl.zinfra.io/await\", \"723990470614\"]";
    private static final String edgeBackend = "[\"https://edge-nginz-https.zinfra.io\", \"https://edge-nginz-ssl.zinfra.io/await\", \"258787940529\"]";
    private static final String productionBackend = "[\"https://prod-nginz-https.wire.com\", \"https://prod-nginz-ssl.wire.com/await\", \"782078216207\"]";

    private static final String BACKEND_JSON = "customBackend.json";
    private static final String BACKEND_FILE_LOCATION = "/mnt/sdcard/customBackend.json";

    public static final String ADB_PREFIX = "";
    // public static final String ADB_PREFIX =
    // "/Applications/android-sdk/platform-tools/";

    public static void executeAdb(final String cmdline) throws Exception {
        executeOsXCommand(new String[]{"/bin/bash", "-c",
                ADB_PREFIX + "adb " + cmdline});
    }

    public static void uploadPhotoToAndroid(String photoPathOnDevice)
            throws Exception {
        executeAdb(String.format("push %s %s", getImagePath(CommonUtils.class),
                photoPathOnDevice));
        executeAdb("shell \"am broadcast -a android.intent.action.MEDIA_MOUNTED -d "
                + "file:///sdcard \"Broadcasting: Intent { act=android.intent.action.MEDIA_MOUNTED dat=file:///sdcard }");
    }

    public static void unlockScreen() throws Exception {
        executeAdb("shell am start -n io.appium.unlock/.Unlock");
    }

    public static void genericScreenTap(int x, int y) throws Exception {
        executeAdb(String.format("shell input tap %d %d", x, y));
    }

    public static void disableHints() throws Exception {
        executeAdb("shell touch /sdcard/disableOnBoardingHints");
    }

    public static void disableHockeyUpdates() throws Exception {
        executeAdb("shell touch /sdcard/disableHockeyUpdates");
    }

    private static String getAdbOutput(String cmdLine) throws Exception {
        String result = "";

        String adbCommand = ADB_PREFIX + "adb " + cmdLine;
        final Process process = Runtime.getRuntime().exec(
                new String[]{"/bin/bash", "-c", adbCommand});
        if (process == null) {
            throw new RuntimeException(String.format(
                    "Failed to execute command line '%s'", cmdLine));
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String s;
            while ((s = in.readLine()) != null) {
                result += s + "\n";
            }
            outputErrorStreamToLog(process.getErrorStream());
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return result.trim();
    }

    private static String getPropertyFromAdb(String propertyName)
            throws Exception {
        return getAdbOutput(String.format("shell getprop %s", propertyName));
    }

    public static String readClientVersionFromAdb() throws Exception {
        final String output = getAdbOutput(String.format(
                "shell dumpsys package %s | grep versionName", CommonUtils
                        .getAndroidPackageFromConfig(AndroidCommonUtils.class)));
        if (output.contains("=")) {
            return output.substring(output.indexOf("=") + 1, output.length());
        } else {
            return output;
        }
    }

    private static String capitalizeManufacturerName(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private static boolean isWifiEnabled() throws Exception {
        final String output = getAdbOutput("shell dumpsys wifi");

        if (output.contains("Wi-Fi is disabled")) {
            return false;
        } else if (output.contains("Wi-Fi is enabled")) {
            final Pattern pattern = Pattern
                    .compile("mNetworkInfo (NetworkInfo: |\\[)?type: [^,]*, state: ([^,]*),");
            final Matcher matcher = pattern.matcher(output);
            String state = "no info";
            while (matcher.find()) {
                state = matcher.group(2);
                log.debug("Retrieved wifi state: " + state);
            }
            if (state.contains("DISCONNECTED") || state.contains("SCANNING")) {
                return false;
            } else if (state.startsWith("CONNECTED")) {
                return true;
            }
        }
        return false;
    }

    public static ClientDeviceInfo readDeviceInfo() throws Exception {
        String os = "Android";
        String osBuild = getPropertyFromAdb("ro.build.version.release");
        String deviceName = capitalizeManufacturerName(getPropertyFromAdb("ro.product.manufacturer"))
                + " " + getPropertyFromAdb("ro.product.model");
        String gsmNetworkType = getPropertyFromAdb("gsm.network.type");
        Boolean isWifiEnabled = isWifiEnabled();

        return new ClientDeviceInfo(os, osBuild, deviceName, gsmNetworkType,
                isWifiEnabled);
    }

    public static String getPerfReportPathFromConfig(Class<?> c)
            throws Exception {
        return CommonUtils.getValueFromConfig(c, "perfReportPath");
    }

    public static String getAndroidToolsPathFromConfig(Class<?> c)
            throws Exception {
        return CommonUtils.getValueFromConfig(c, "androidToolsPath");
    }

    public static String getAndroidAddressBookMailAccountFromConfig(Class<?> c)
            throws Exception {
        return CommonUtils.getValueFromConfig(c,
                "androidAddressBookMailAccount");
    }

    public static void deployBackendFile(String fileName) throws Exception {
        executeAdb(String.format("push %s %s", fileName, BACKEND_FILE_LOCATION));
    }

    public static String createBackendJSON(String bt)
            throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(BACKEND_JSON);
        if (file.exists()) {
            FileUtils.deleteQuietly(file);
        }
        PrintWriter writer = new PrintWriter(file);

        switch (bt) {
            case "edge":
                writer.println(edgeBackend);
                break;
            case "production":
                writer.println(productionBackend);
                break;
            case "staging":
                writer.println(stagingBackend);
                break;
        }

        writer.close();
        return file.getAbsolutePath();
    }

    /**
     * http://android.stackexchange.com/questions/30157/how-to-return-to-the-
     * home-screen-with-a-terminal-command
     *
     * @throws Exception
     */
    public static void switchToHomeScreen() throws Exception {
        executeAdb("shell am start -a android.intent.action.MAIN -c android.intent.category.HOME");
    }

    /**
     * http://stackoverflow.com/questions/4567904/how-to-start-an-application-
     * using-android-adb-tools
     *
     * @param packageId
     * @param mainActivity
     * @throws Exception
     */
    public static void switchToApplication(String packageId, String mainActivity)
            throws Exception {
        executeAdb(String.format("shell am start -n %s/%s", packageId,
                mainActivity));
    }

    public static void rotateLanscape() throws Exception {
        executeAdb("shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:1");
    }

    public static void rotatePortrait() throws Exception {
        executeAdb("shell content insert --uri content://settings/system --bind name:s:user_rotation --bind value:i:0");
    }

    /**
     * http://stackoverflow.com/questions/13850192/how-to-lock-android-screen-
     * via-adb
     *
     * @throws Exception
     */
    public static void lockScreen() throws Exception {
        executeAdb("shell input keyevent 26");
    }

    public static void tapBackButton() throws Exception {
        executeAdb("shell input keyevent 4");
    }

    public static void tapHomeButton() throws Exception {
        executeAdb("shell input keyevent 3");
    }

    public static double getScreenDensity() throws Exception {
        String result = getAdbOutput("shell getprop ro.sf.lcd_density");
        double screenPixels = Integer.parseInt(result);
        double densityIndependentPixels = 160; // the number of dp in a screen
        // is constant
        return screenPixels / densityIndependentPixels;
    }

    public static void type(String message) throws Exception {
        executeAdb("shell input text " + message);
    }

    public static Dimension getScreenSize(final ZetaAndroidDriver drv)
            throws Exception {
        final String output = getAdbOutput("shell dumpsys window");
        final Pattern patt = Pattern.compile("init=(\\d+)x(\\d+)");
        final Matcher m = patt.matcher(output);
        if (m.find()) {
            if (drv.getOrientation() == ScreenOrientation.LANDSCAPE) {
                return new Dimension(Integer.parseInt(m.group(2)),
                        Integer.parseInt(m.group(1)));
            } else {
                return new Dimension(Integer.parseInt(m.group(1)),
                        Integer.parseInt(m.group(2)));
            }
        } else {
            throw new AssertionError(
                    String.format(
                            "Failed to get device screen dimensions from ADB output\n%s",
                            output));
        }
    }

    /**
     * Compares the Android of the plugged-in device with the input (target)
     * version that you wish to check for. For exmaple, if you want to check
     * that the plugged in device is 4.4 or higher, you need to supply "4.4" as
     * the target version
     *
     * @param targetVersion the Android version you wish to check for
     * @return a negative int, 0, or a positive int if the targetVersion is less
     * than, equal to or greater than the current device's version
     * @throws Exception
     */
    public static int compareAndroidVersion(String targetVersion)
            throws Exception {
        String deviceVersion = readDeviceInfo().getOperatingSystemBuild();
        return deviceVersion.compareTo(targetVersion);
    }

    /**
     * The method uses dirty Appium hack to unlock the screen
     *
     * @throws Exception
     */
    public static void unlockDevice() throws Exception {
        executeAdb("shell am start -n io.appium.unlock/.Unlock");
    }

    public static boolean isAirplaneModeEnabled() throws Exception {
        return getAdbOutput("shell settings get global airplane_mode_on")
                .trim().equals("1");
    }

    public static void setAirplaneMode(boolean expectedState) throws Exception {
        if (isAirplaneModeEnabled() == expectedState) {
            return;
        }
        executeAdb(String.format(
                "shell settings put global airplane_mode_on %d",
                expectedState ? 1 : 0));
        executeAdb(String
                .format("shell am broadcast -a android.intent.action.AIRPLANE_MODE --ez state %s",
                        expectedState ? "true" : "false"));
        assert (isAirplaneModeEnabled() == expectedState) : "ADB has failed to "
                + (expectedState ? "enable" : "disable")
                + " airplane mode on the device";
        // Let the app to understand that connectivity state has been changed
        Thread.sleep(3000);
    }

    public static boolean isPackageInstalled(String androidPackage)
            throws Exception {
        String output = getAdbOutput("shell pm list packages -3 "
                + androidPackage);
        return output.contains(androidPackage);
    }

    private static final String TESTING_GALLERY_PACKAGE_ID = "com.wire.testinggallery";

    public static void installTestingGalleryApp(Class<?> c) throws Exception {
        if (!isPackageInstalled(TESTING_GALLERY_PACKAGE_ID)) {
            executeAdb(String.format("install %s/testing_gallery-debug.apk",
                    getAndroidToolsPathFromConfig(c)));
        }
    }

    public static boolean isAppInForeground(String packageId) throws Exception {
        final String output = getAdbOutput("shell dumpsys window windows");
        for (String line : output.split("\n")) {
            if ((line.contains("mCurrentFocus") || line.contains("mFocusedApp"))
                    && line.contains(packageId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * http://stackoverflow.com/questions/28150650/open-chrome-with-adb
     *
     * @param url
     * @throws Exception
     */
    public static void openLinkInChrome(String url) throws Exception {
        executeAdb(String
                .format("shell am start -a android.intent.action.VIEW "
                        + "-n com.android.chrome/com.google.android.apps.chrome.Main "
                        + "-d \"%s\"", url));
    }

    public static int getBatteryCapacity() throws Exception {
        final String output = getAdbOutput(
                "shell cat /sys/class/power_supply/battery/capacity").trim();
        return Integer.parseInt(output);
    }

    @SuppressWarnings("unused")
    private static String getUidForPackage(String packageId) throws Exception {
        final String output = getAdbOutput(
                String.format("shell dumpsys package %s", packageId)).trim();
        final String[] lines = output.split("\n");
        boolean isPackageSignatureFound = false;
        for (String line : lines) {
            if (line.trim().startsWith("Package [")) {
                if (line.contains("[" + packageId + "]")) {
                    isPackageSignatureFound = true;
                } else {
                    isPackageSignatureFound = false;
                }
            }
            if (isPackageSignatureFound) {
                if (line.trim().startsWith("userId=")) {
                    final Pattern pattern = Pattern.compile("userId=([0-9]+)");
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        return matcher.group(1);
                    }
                }
            }
        }
        throw new RuntimeException(String.format(
                "UserId for the package '%s' cannot be found. Adb output:\n%s",
                packageId, output));
    }

    private static int getPackagePid(String packageId) throws Exception {
        final String output = getAdbOutput("shell ps").trim();
        final String[] lines = output.split("\n");
        for (String line : lines) {
            line = line.trim();
            final String[] values = line.split("\\s+");
            if (values.length >= 9) {
                if (values[8].equals(packageId)) {
                    return Integer.parseInt(values[1]);
                }
            }
        }
        throw new RuntimeException(String.format(
                "PID for the package '%s' cannot be found. Adb output:\n%s",
                packageId, output));
    }

    /**
     * Return the corresponding network stat value for a package
     *
     * @param packageId
     * @param columnNumber starts from 0
     * @return
     * @throws Exception
     */
    private static long getNetworkStatValue(final String packageId,
                                            final int columnNumber) throws Exception {
        final String output = getAdbOutput(
                String.format("shell cat /proc/%d/net/dev",
                        getPackagePid(packageId))).trim();
        final String[] lines = output.split("\n");
        long result = 0;
        for (String line : lines) {
            line = line.trim();
            final String[] values = line.split("\\s+");
            if (values.length > columnNumber) {
                if (values[0].endsWith(":") && !values[0].startsWith("lo")) {
                    result += Long.parseLong(values[columnNumber]);
                }
            }
        }
        return result;
    }

    public static long getRxBytes(String packageId) throws Exception {
        return getNetworkStatValue(packageId, 1);
    }

    public static long getTxBytes(String packageId) throws Exception {
        return getNetworkStatValue(packageId, 9);
    }

    public static String getBundleIdFromAPK(String path) throws Exception {
        assert new File(path).exists() : String.format(
                "The file %s does not exist on local file system", path);
        final String cmdLine = String.format("aapt dump badging \"%s\"", path);
        final Process process = Runtime.getRuntime().exec(
                new String[]{"/bin/bash", "-c", cmdLine});
        if (process == null) {
            throw new RuntimeException(String.format(
                    "Failed to execute command line '%s'", cmdLine));
        }
        String output = "";
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            final Pattern pattern = Pattern.compile("versionName='(.*)'");
            Matcher matcher = null;
            String s;
            while ((s = in.readLine()) != null) {
                output += s + '\n';
                if (s.contains("r'versionName")) {
                    matcher = pattern.matcher(s);
                    if (matcher.find()) {
                        return matcher.group(1);
                    }
                }
            }
            outputErrorStreamToLog(process.getErrorStream());
        } finally {
            if (in != null) {
                in.close();
            }
        }
        throw new AssertionError(String.format(
                "Package id cannot be parsed from aapt output:\n%s",
                output.trim()));
    }

    /**
     * This method will hide the current activity
     *
     * @throws Exception
     */
    public static void clearAllContacts() throws Exception {
        executeAdb("shell content delete "
                + "--uri content://com.android.contacts/raw_contacts");
    }

    /**
     * This method will hide the current activity
     *
     * @throws Exception
     */
    public static void insertContact(String name, String email, String phoneNumber) throws Exception {
        executeAdb(
                String.format("shell am start -a android.intent.action.INSERT -t vnd.android.cursor.dir/contact -e name '%s' -e email %s -e phone %s",
                name, email, phoneNumber));
        // To save changes
        tapHomeButton();
    }
}
