package com.wearezeta.auto.android.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;

import static com.wearezeta.auto.common.driver.ZetaAndroidDriver.ADB_PREFIX;

public class AndroidCommonUtils extends CommonUtils {

    private static final Logger log = ZetaLogger.getLog(AndroidCommonUtils.class.getSimpleName());

    private static final String stagingBackend =
            "[\"https://staging-nginz-https.zinfra.io\", \"https://staging-nginz-ssl.zinfra.io/await\", \"723990470614\"]";
    private static final String edgeBackend =
            "[\"https://edge-nginz-https.zinfra.io\", \"https://edge-nginz-ssl.zinfra.io/await\", \"258787940529\"]";
    private static final String productionBackend =
            "[\"https://prod-nginz-https.wire.com\", \"https://prod-nginz-ssl.wire.com/await\", \"782078216207\"]";

    private static final String BACKEND_JSON = "customBackend.json";
    private static final String BACKEND_FILE_LOCATION = "/mnt/sdcard/customBackend.json";
    private static final String FILE_TRANSFER_SOURCE_LOCATION = "/mnt/sdcard/Download/";

    private static final String IMAGE_FOR_VIDEO_GENERATION = "about_page_logo_iPad.png";

    public static final String[] STANDARD_WIRE_PERMISSIONS = new String[]{
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_CONTACTS",
            "android.permission.RECORD_AUDIO",
            "android.permission.CAMERA",
            "android.permission.READ_PHONE_STATE",
            "android.permission.ACCESS_FINE_LOCATION"
    };

    public static void executeAdb(final String cmdline) throws Exception {
        executeOsXCommand(new String[]{"/bin/bash", "-c",
                ADB_PREFIX + "adb " + cmdline});
    }

    public static void uploadPhotoToAndroid(String photoPathOnDevice)
            throws Exception {
        executeAdb(String.format("push %s %s", getDefaultUserImagePath(CommonUtils.class), photoPathOnDevice));
        executeAdb("shell \"am broadcast -a android.intent.action.MEDIA_MOUNTED -d "
                + "file:///sdcard \"Broadcasting: Intent { act=android.intent.action.MEDIA_MOUNTED dat=file:///sdcard }");
    }

    public static void unlockScreen() throws Exception {
        executeAdb("shell am start -n io.appium.unlock/.Unlock");
    }

    public static void genericScreenTap(int x, int y) throws Exception {
        executeAdb(String.format("shell input tap %d %d", x, y));
    }

    public static void disableHockeyUpdates() throws Exception {
        executeAdb("shell touch /sdcard/disableHockeyUpdates");
    }

    public static String getAdbOutput(String cmdLine) throws Exception {
        final StringBuilder result = new StringBuilder();
        String adbCommand = ADB_PREFIX + "adb " + cmdLine;
        final Process process = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", adbCommand});
        if (process == null) {
            throw new RuntimeException(String.format("Failed to execute command line '%s'", cmdLine));
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s;
            while ((s = in.readLine()) != null) {
                result.append(s).append("\n");
            }
            outputErrorStreamToLog(process.getErrorStream());
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return result.toString().trim();
    }

    private static String getPropertyFromAdb(String propertyName) throws Exception {
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

    public static String getPerfReportPathFromConfig(Class<?> c) throws Exception {
        return CommonUtils.getValueFromConfig(c, "perfReportPath");
    }

    public static String getAndroidToolsPathFromConfig(Class<?> c) throws Exception {
        return CommonUtils.getValueFromConfig(c, "androidToolsPath");
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
     * http://stackoverflow.com/questions/29931318/launch-app-via-adb-without-knowing-activity-name
     */
    public static void switchToApplication(String packageId) throws Exception {
        executeAdb(String.format("shell monkey -p %s -c android.intent.category.LAUNCHER 1", packageId));
    }

    /**
     * http://stackoverflow.com/questions/13850192/how-to-lock-android-screen- via-adb
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

    public static void tapBackspaceButton() throws Exception {
        executeAdb("shell input keyevent 67");
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
     * Compares the Android of the plugged-in device with the input (target) version that you wish to check for. For exmaple, if
     * you want to check that the plugged in device is 4.4 or higher, you need to supply "4.4" as the target version
     *
     * @param targetVersion the Android version you wish to check for
     * @return a negative int, 0, or a positive int if the targetVersion is less than, equal to or greater than the current
     * device's version
     * @throws Exception
     */
    public static int compareAndroidVersion(String targetVersion) throws Exception {
        final DefaultArtifactVersion deviceVersion =
                new DefaultArtifactVersion(getPropertyFromAdb("ro.build.version.release"));
        return deviceVersion.compareTo(new DefaultArtifactVersion(targetVersion));
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

    public static boolean isPackageInstalled(String androidPackage) throws Exception {
        String output = getAdbOutput("shell pm list packages -3 " + androidPackage);
        return output.contains(androidPackage);
    }

    /**
     * Install Testing Gallery, if SDK Version <= 4.3, which will not support Catching Notification Service
     */
    public static void installTestingGalleryApp(Class<?> c) throws Exception {
        final DefaultArtifactVersion deviceVersion =
                new DefaultArtifactVersion(getPropertyFromAdb("ro.build.version.release"));
        if (deviceVersion.compareTo(new DefaultArtifactVersion("4.3")) <= 0) {
            executeAdb(String.format("install -r %s/testing_gallery-debug.apk", getAndroidToolsPathFromConfig(c)));
        } else {
            executeAdb(String.format("install -r %s/testing_gallery-debug19.apk", getAndroidToolsPathFromConfig(c)));
            if (deviceVersion.compareTo(new DefaultArtifactVersion("6.0")) >= 0) {
                grantPermissionsTo("com.wire.testinggallery", "android.permission.READ_EXTERNAL_STORAGE");
            }
        }
    }

    public static void enableAutoAnswerCall(Class<?> c) throws Exception {
        executeAdb("shell am broadcast -a com.waz.zclient.intent.action.AUTO_ANSWER_CALL " +
                "--ez AUTO_ANSWER_CALL_EXTRA_KEY true");
    }

    public static boolean isAppInForeground(String packageId, long timeoutMillis) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        do {
            Thread.sleep(100);
            final String output = getAdbOutput("shell dumpsys window windows");
            for (String line : output.split("\n")) {
                if ((line.contains("mCurrentFocus") || line.contains("mFocusedApp")) && line.contains(packageId)) {
                    return true;
                }
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= timeoutMillis);
        return false;
    }

    public static boolean isAppNotInForeground(String packageId, long timeoutMillis) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        do {
            Thread.sleep(100);
            boolean isInForeground = false;
            final String output = getAdbOutput("shell dumpsys window windows");
            for (String line : output.split("\n")) {
                if ((line.contains("mCurrentFocus") || line.contains("mFocusedApp")) && line.contains(packageId)) {
                    isInForeground = true;
                    break;
                }
            }
            if (!isInForeground) {
                return true;
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= timeoutMillis);
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

    public static void clearAllContacts() throws Exception {
        executeAdb("shell content delete "
                + "--uri content://com.android.contacts/raw_contacts");
    }

    /**
     * We try to insert contacts in different groups to make them detectable by Wire. Anyway, the created contact will not be
     * visible in Wire invitations list if current Google account on device under test is not set to
     * MessagingUtils.getAccountName()
     *
     * @return
     * @throws Exception
     */
    private static List<Integer> insertContactAndGetIds() throws Exception {
        final List<Integer> result = new ArrayList<>();
        final Map<String, String> accountTypes = new HashMap<>();
        accountTypes.put("vnd.sec.contact.phone", "vnd.sec.contact.phone");
        accountTypes.put("com.google", MessagingUtils.getDefaultAccountName());
        for (Map.Entry<String, String> accountInfo : accountTypes.entrySet()) {
            executeAdb(String.format("shell content insert "
                    + "--uri content://com.android.contacts/raw_contacts "
                    + "--bind account_type:s:%s "
                    + "--bind account_name:s:%s", accountInfo.getKey(), accountInfo.getValue()));
            String idsList = getAdbOutput("shell content query "
                    + "--uri content://com.android.contacts/raw_contacts "
                    + "--projection _id");
            Pattern pattern = Pattern.compile("_id=(\\d+)");
            Matcher matcher = pattern.matcher(idsList);
            int value = 0;
            while (matcher.find()) {
                try {
                    value = Integer.parseInt(matcher.group(1));
                } catch (NumberFormatException e) {
                    // Ignore silently
                }
            }
            result.add(value);
        }
        return result;
    }

    private static void bindContactNameById(List<Integer> ids, String name) throws Exception {
        for (int id : ids) {
            executeAdb(String.format("shell content insert "
                    + "--uri content://com.android.contacts/data "
                    + "--bind raw_contact_id:i:%s "
                    + "--bind mimetype:s:vnd.android.cursor.item/name "
                    + "--bind data1:s:'%s'", id, name));
        }
    }

    private static void bindContactEmailById(List<Integer> ids, String email) throws Exception {
        for (int id : ids) {
            executeAdb(String.format("shell content insert "
                    + "--uri content://com.android.contacts/data "
                    + "--bind raw_contact_id:i:%s "
                    + "--bind mimetype:s:vnd.android.cursor.item/email_v2 "
                    + "--bind data1:s:'%s'", id, email));
        }
    }

    private static void bindContactPhoneNumberById(List<Integer> ids, PhoneNumber phoneNumber) throws Exception {
        for (int id : ids) {
            executeAdb(String.format("shell content insert "
                    + "--uri content://com.android.contacts/data "
                    + "--bind raw_contact_id:i:%s "
                    + "--bind mimetype:s:vnd.android.cursor.item/phone_v2 "
                    + "--bind data1:s:%s", id, phoneNumber.toString()));
        }
    }

    public static void insertContact(String name) throws Exception {
        final List<Integer> ids = insertContactAndGetIds();
        bindContactNameById(ids, name);
    }

    public static void insertContact(String name, String email,
                                     PhoneNumber phoneNumber) throws Exception {
        final List<Integer> ids = insertContactAndGetIds();
        bindContactNameById(ids, name);
        bindContactEmailById(ids, email);
        bindContactPhoneNumberById(ids, phoneNumber);
    }

    public static void insertContact(String name, String email) throws Exception {
        final List<Integer> ids = insertContactAndGetIds();
        bindContactNameById(ids, name);
        bindContactEmailById(ids, email);
    }

    public static void insertContact(String name, PhoneNumber phoneNumber) throws Exception {
        final List<Integer> ids = insertContactAndGetIds();
        bindContactNameById(ids, name);
        bindContactPhoneNumberById(ids, phoneNumber);
    }

    public static void broadcastInvitationCode(String code) throws Exception {
        executeAdb(String.format("shell am broadcast -a com.android.vending.INSTALL_REFERRER "
                + "-n \"%s/com.waz.zclient.broadcast.ReferralBroadcastReceiver\" "
                + "--es referrer \"invite-%s\"", getAndroidPackageFromConfig(AndroidCommonUtils.class), code));
    }

    public static void installApp(File path) throws Exception {
        if (!path.isFile()) {
            throw new IllegalArgumentException(String.format(
                    "Please make sure that the file '%s' exists and is accessible", path.getCanonicalPath()));
        }
        executeAdb(String.format("install -r %s", path.getCanonicalPath()));
    }

    public static void stopPackage(String packageName) throws Exception {
        executeAdb(String.format("shell am force-stop %s", packageName));
    }

    public static void uninstallPackage(String packageName) throws Exception {
        executeAdb(String.format("uninstall %s", packageName));
    }

    /**
     * Create random access file with predefined size and name , and push it into sdcard
     *
     * @param fileFullName the name with extension
     * @param size         the expected size of file
     * @throws Exception
     */
    public static void pushRandomFileToSdcardDownload(String fileFullName, String size, boolean isVideo) throws Exception {
        String basePath = getBuildPathFromConfig(AndroidCommonUtils.class);
        String extension = FilenameUtils.getExtension(fileFullName);
        String fileName = FilenameUtils.getBaseName(fileFullName);

        if (isVideo) {
            String imagesDirectoryPath = getImagesPathFromConfig(CommonUtils.class);
            CommonUtils.generateVideoFile(basePath + File.separator + fileFullName, size, imagesDirectoryPath
                    + IMAGE_FOR_VIDEO_GENERATION);
        } else {
            CommonUtils.createRandomAccessFile(basePath + File.separator + fileFullName, size);
        }

        AndroidCommonUtils.pushFileToSdcardDownload(basePath, fileName, extension);
    }

    /**
     * Push the file from defaultImagesPath (/tools/img/)
     *
     * @param fileFullName fileName should be the name of file which located in Android Tools Path
     * @throws Exception
     */
    public static void pushLocalFileToSdcardDownload(String fileFullName) throws Exception {
        String basePath = getImagesPathFromConfig(AndroidCommonUtils.class);
        String extension = FilenameUtils.getExtension(fileFullName);
        String fileName = FilenameUtils.getBaseName(fileFullName);

        AndroidCommonUtils.pushFileToSdcardDownload(basePath, fileName, extension);
    }

    public static void pushFileToSdcardDownload(String basePath, String fileName, String extension) throws Exception {
        String fileFullName = String.format("%s.%s", fileName, extension);
        String sourceFilePath = basePath + File.separator + fileFullName;
        String destinationFilePath = FILE_TRANSFER_SOURCE_LOCATION + fileFullName;

        Date futureDate = new Date(Calendar.getInstance().getTimeInMillis() + (10 * 60000));
        String futureTimestamp = String.valueOf(futureDate.getTime()).substring(0, 10);

        executeAdb(String.format("shell rm %s", destinationFilePath));
        executeAdb(String.format("push %s %s", sourceFilePath, destinationFilePath));
        executeAdb(String.format("shell am broadcast -a android.intent.action.MEDIA_MOUNTED -d file://%s",
                destinationFilePath));

        // The reason way do 2 times(workaround), is because in different env of adb, it handle the '"' in different way
        // Need to handle all conditions
        executeAdb(String.format("shell content update " +
                "--uri content://media/external/file " +
                "--bind _display_name:s:'%s' " +
                "--bind date_added:i:%s " +
                "--where 'title=\\\"%s\\\"'", fileFullName, futureTimestamp, fileName));
        executeAdb(String.format("shell content update " +
                "--uri content://media/external/file " +
                "--bind _display_name:s:'%s' " +
                "--bind date_added:i:%s " +
                "--where 'title=\"%s\"'", fileFullName, futureTimestamp, fileName));
    }

    public static void pullFileFromSdcardDownload(String fileFullName) throws Exception {
        pullFileFromSdcard(FILE_TRANSFER_SOURCE_LOCATION, fileFullName);
    }

    public static void removeFileFromSdcardDownload(String fileFullName) throws Exception {
        removeFileFromSdcard(FILE_TRANSFER_SOURCE_LOCATION, fileFullName);
    }

    private static void pullFileFromSdcard(String sdcardBasePath, String fileFullName) throws Exception {
        String sourceFilePath = sdcardBasePath + fileFullName;
        String destinationFilePath = getBuildPathFromConfig(AndroidCommonUtils.class) + File.separator + fileFullName;
        File destinationFile = new File(destinationFilePath);
        if (destinationFile.exists()) {
            destinationFile.delete();
        }
        executeAdb(String.format("pull %s %s", sourceFilePath, destinationFilePath));
    }

    private static void removeFileFromSdcard(String sdcardBasePath, String fileFullName) throws Exception {
        String sourceFilePath = sdcardBasePath + fileFullName;
        executeAdb(String.format("shell rm %s", sourceFilePath));
        executeAdb(String.format("shell am broadcast -a android.intent.action.MEDIA_MOUNTED -d file://%s",
                sourceFilePath));
    }

    // *** Read https://github.com/majido/clipper for more details

    public static void installClipperApp(Class<?> c) throws Exception {
        executeAdb(String.format("install -r %s/clipper.apk", getAndroidToolsPathFromConfig(c)));
    }

    private static void ensureClipperServiceIsRunning() throws Exception {
        final DefaultArtifactVersion deviceVersion =
                new DefaultArtifactVersion(getPropertyFromAdb("ro.build.version.release"));

        // Related issue for Android 4.2: http://stackoverflow
        // .com/questions/13588668/adb-throws-securityexception-while-starting-service-after-system-update-to-nexus

        if (deviceVersion.compareTo(new DefaultArtifactVersion("4.3")) <= 0) {
            executeAdb("shell am startservice --user 0 -n ca.zgrs.clipper/.ClipboardService");
        } else {
            executeAdb("shell am startservice ca.zgrs.clipper/.ClipboardService");
        }
    }

    public static Optional<String> getClipboardContent() throws Exception {
        ensureClipperServiceIsRunning();
        final String output = getAdbOutput("shell am broadcast -a clipper.get");
        final Pattern pattern = Pattern.compile("data=\"(.*)\"$");
        final Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    public static void setClipboardContent(String content) throws Exception {
        ensureClipperServiceIsRunning();
        executeAdb(String.format("shell am broadcast -a clipper.set -e text \"%s\"", content));
    }

    // ***

    /**
     * this method requires TestingGallery app to be installed on the target device
     * See https://github.com/wearezeta/zclient-android/pull/3317/ for more details
     */
    public static String getWirePushNotifications() throws Exception {
        return getAdbOutput("shell am broadcast -a com.wire.testinggallery.notification --es command get");
    }

    public static String clearWirePushNotifications() throws Exception {
        return getAdbOutput("shell am broadcast -a com.wire.testinggallery.notification --es command clear");
    }

    // ***

    // ***
    // http://stackoverflow.com/questions/11420617/android-emulator-screen-rotation/14253321#14253321

    public static void changeAccelerometerState(boolean isEnabled) throws Exception {
        executeAdb(String.format("shell content insert --uri content://settings/system " +
                "--bind name:s:accelerometer_rotation --bind value:i:%s", isEnabled ? "1" : "0"));
    }

    // ***

    // ***

    public enum PadButton {
        RIGHT(22), LEFT(21), UP(19), DOWN(20), CENTER(23);

        private int code;

        PadButton(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    public static void pressPadButton(PadButton button) throws Exception {
        executeAdb(String.format("shell input keyevent %s", button.getCode()));
        Thread.sleep(300);
    }

    /**
     * Grant permission to the particular application with bundleId identifier
     *
     * @param bundleId app identifier, dor example com.wire.x
     * @param perms    array of permission name. See https://developer.android.com/reference/android/Manifest.permission.html
     *                 for more details
     * @throws Exception
     */
    public static void grantPermissionsTo(String bundleId, String... perms) throws Exception {
        for (String permissionName : perms) {
            executeAdb(String.format("shell pm grant %s %s", bundleId, permissionName));
        }
    }

    public static void revokePermissionsFrom(String bundleId, String... perms) throws Exception {
        for (String permissionName : perms) {
            executeAdb(String.format("shell pm revoke %s %s", bundleId, permissionName));
        }
    }

    public static void verifyWireIsInForeground() throws Exception {
        final String packageId = CommonUtils.getAndroidPackageFromConfig(AndroidCommonUtils.class);
        if (!AndroidCommonUtils.isAppInForeground(packageId, 1000)) {
            throw new IllegalStateException("Wire appears to have crashed");
        }
    }

    public static boolean isWireDebugModeEnabled(boolean checkSupport) throws Exception {
        final String packageName = CommonUtils.getAndroidPackageFromConfig(AndroidCommonUtils.class);
        final String output = AndroidCommonUtils.getAdbOutput(String.format("shell run-as %s ls", packageName));
        final Pattern pattern = (checkSupport) ? Pattern.compile("\\b" + Pattern.quote("is unknown") + "\\b") :
                Pattern.compile("\\b" + Pattern.quote("not debuggable") + "\\b");
        return !pattern.matcher(output).find();
    }

    public static boolean verifyGoogleLocationServiceInstalled() throws Exception {
        String output = AndroidCommonUtils.getAdbOutput("shell 'pm list packages'");
        final Pattern pattern = Pattern.compile("\\b" + Pattern.quote("com.google.android.location") + "\\b");
        return pattern.matcher(output).find();
    }

    public static boolean verifyGoogleGCMServiceInstalled() throws Exception {
        String output = AndroidCommonUtils.getAdbOutput("shell 'pm list packages'");
        final Pattern pattern = Pattern.compile("\\b" + Pattern.quote("com.google.android.gms") + "\\b");
        return pattern.matcher(output).find();
    }

    public static void openWebsiteFromADB(String url) throws Exception {
        AndroidCommonUtils.executeAdb(String.
                format("shell am start -a android.intent.action.VIEW -d %s", url));
    }

    public static boolean isKeyboardVisible() throws Exception {
        String output = AndroidCommonUtils.getAdbOutput("shell dumpsys input_method | grep mInputShown");
        final Pattern pattern = Pattern.compile("\\b" + Pattern.quote("mInputShown=true") + "\\b");
        return pattern.matcher(output).find();
    }
}
