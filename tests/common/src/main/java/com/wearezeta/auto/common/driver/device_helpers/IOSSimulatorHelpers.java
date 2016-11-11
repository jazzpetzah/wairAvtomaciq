package com.wearezeta.auto.common.driver.device_helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.common.process.UnixProcessHelpers;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import static com.wearezeta.auto.common.CommonUtils.getDeviceName;
import static com.wearezeta.auto.common.CommonUtils.getIOSToolsRoot;
import static com.wearezeta.auto.common.CommonUtils.getImagesPathFromConfig;

public class IOSSimulatorHelpers {
    public static final int SIMULATOR_INTERACTION_TIMEOUT = 3 * 60; //seconds

    private static final String TESTING_IMAGE_NAME = "testing.jpg";

    private static final long INSTALL_SYNC_TIMEOUT_MS = 3000;

    private static final long SIMULATOR_BOOT_TIMEOUT_MS = 80000;
    private static final long SIMULATOR_BOOTING_INTERVAL_CHECK_MS = SIMULATOR_BOOT_TIMEOUT_MS / 20;

    private static final String SIMULATOR_PROCESS_NAME = "Simulator";

    private static final String XCRUN_PATH = "/usr/bin/xcrun";

    private static Logger log = ZetaLogger.getLog(IOSSimulatorHelpers.class.getSimpleName());

    public IOSSimulatorHelpers() {
    }

    private static final String SWIPE_SCRIPT_NAME = "swipeInWindow.py";
    private static final String CLICK_SCRIPT_NAME = "clickInWindow.py";
    private static final String DOUBLE_CLICK_SCRIPT_NAME = "doubleClickInWindow.py";

    /**
     * @param startX 0 <= startX <= 1
     * @param startY 0 <= startY <= 1
     * @param endX   0 <= endX <= 1
     * @param endY   0 <= endY <= 1
     * @throws Exception
     */
    public static void swipe(double startX, double startY, double endX, double endY) throws Exception {
        CommonUtils.executeUIShellScript(new String[]{
                String.format("/usr/bin/python '%s/%s' %.2f %.2f %.2f %.2f",
                        getIOSToolsRoot(IOSSimulatorHelpers.class), SWIPE_SCRIPT_NAME,
                        startX, startY, endX, endY)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * @param startX         0 <= startX <= 1
     * @param startY         0 <= startY <= 1
     * @param endX           0 <= endX <= 1
     * @param endY           0 <= endY <= 1
     * @param durationMillis swipe duration in milliseconds
     * @throws Exception
     */
    public static void swipe(double startX, double startY, double endX, double endY,
                             long durationMillis) throws Exception {
        CommonUtils.executeUIShellScript(new String[]{
                String.format("/usr/bin/python '%s/%s' %.2f %.2f %.2f %.2f %d",
                        getIOSToolsRoot(IOSSimulatorHelpers.class), SWIPE_SCRIPT_NAME,
                        startX, startY, endX, endY, durationMillis)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void swipeDown() throws Exception {
        swipe(0.8, 0.2, 0.8, 0.95);
    }

    public static void swipeRight() throws Exception {
        swipe(0.2, 0.8, 0.9, 0.8);
    }

    public static void doubleClickAt(String relativeX, String relativeY) throws Exception {
        CommonUtils.executeUIShellScript(new String[]{
                String.format("/usr/bin/python '%s/%s' %s %s",
                        getIOSToolsRoot(IOSSimulatorHelpers.class), DOUBLE_CLICK_SCRIPT_NAME, relativeX, relativeY)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void clickAt(String relativeX, String relativeY, String durationSeconds) throws Exception {
        CommonUtils.executeUIShellScript(new String[]{
                String.format("/usr/bin/python '%s/%s' %s %s %s",
                        getIOSToolsRoot(IOSSimulatorHelpers.class), CLICK_SCRIPT_NAME, relativeX, relativeY, durationSeconds)
        }).get(SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void activateWindow() throws Exception {
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to tell application process \"Simulator\"",
                "set frontmost to false",
                "set frontmost to true",
                "end tell"
        }).get(IOSSimulatorHelpers.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
        // To make sure the window is really activated
        Thread.sleep(4000);
    }

    public static void switchAppsList() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\"",
                "keystroke \"h\" using {command down, shift down}",
                "keystroke \"h\" using {command down, shift down}",
                "end tell"
        }).get(IOSSimulatorHelpers.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void lock() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke \"l\" using {command down}"
        }).get(IOSSimulatorHelpers.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void goHome() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke \"h\" using {command down, shift down}"
        }).get(IOSSimulatorHelpers.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void pressEnterKey() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke return"
        }).get(IOSSimulatorHelpers.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void toggleSoftwareKeyboard() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\" to keystroke \"k\" using {command down}"
        }).get(IOSSimulatorHelpers.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    public static void selectPasteMenuItem() throws Exception {
        activateWindow();
        CommonUtils.executeUIAppleScript(new String[]{
                "tell application \"System Events\"",
                "  tell process \"Simulator\"",
                "    tell menu bar item \"Edit\" of menu bar 1",
                "      click",
                "      click (menu item \"Paste\") of menu 1",
                "    end tell",
                "  end tell",
                "end tell",
        }).get(IOSSimulatorHelpers.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
    }

    private static final String SUBSECTION_MARKER = "--";
    private static final String SECTION_MARKER = "==";
    private static final String DEVICES_SECTION_START = SECTION_MARKER + " Devices " + SECTION_MARKER;
    private static final Function<String, String> IOS_SECTION_START_TEMPLATE =
            version -> String.format(SUBSECTION_MARKER + " iOS %s " + SUBSECTION_MARKER, version);
    private static final Pattern ID_PATTERN =
            Pattern.compile(".*([\\w]{8}\\-[\\w]{4}\\-[\\w]{4}\\-[\\w]{4}\\-[\\w]{12}).*");

    private static Optional<String> parseSimulatorId(String simctlOutput, String deviceName, String platformVersion) {
        final String subSectionStartMarker = IOS_SECTION_START_TEMPLATE.apply(platformVersion);
        boolean isInDeviceSection = false;
        boolean isInIOSSubsection = false;
        for (String line : simctlOutput.split("\n")) {
            if (line.startsWith(DEVICES_SECTION_START)) {
                isInDeviceSection = true;
            } else if (isInDeviceSection && line.startsWith(SECTION_MARKER)) {
                isInDeviceSection = false;
            }
            if (isInDeviceSection && line.startsWith(subSectionStartMarker)) {
                isInIOSSubsection = true;
            } else if (isInIOSSubsection && (line.startsWith(SUBSECTION_MARKER) || line.startsWith(SECTION_MARKER))) {
                isInIOSSubsection = false;
            }
            if (isInIOSSubsection && line.trim().startsWith(deviceName)) {
                final Matcher m = ID_PATTERN.matcher(line);
                if (m.find()) {
                    return Optional.of(m.group(1));
                }
            }
        }
        return Optional.empty();
    }

    private static Map<String, String> simIdsMapping = new HashMap<>();

    public static String getId() throws Exception {
        final String deviceName = getDeviceName(IOSSimulatorHelpers.class);
        final String platformVersion = CommonUtils.getPlatformVersionFromConfig(IOSSimulatorHelpers.class);
        if (!simIdsMapping.containsKey(deviceName)) {
            final String output = executeSimctl("list");
            simIdsMapping.put(deviceName, parseSimulatorId(output, deviceName, platformVersion).orElseThrow(
                    () -> new IllegalStateException(
                            String.format("Cannot get an id for %s (%s) Simulator from simctl output:\n%s",
                                    deviceName, platformVersion, output)
                    )
            ));
        }
        return simIdsMapping.get(deviceName);
    }

    private final static String APP_CRASHES_MARKER = "Wire_";
    private final static String USED_APP_CRASHES_MARKER = "_";

    private static String getRecentWireCrashReports() throws Exception {
        final StringBuilder result = new StringBuilder();
        final File reportsRoot = new File(String.format("%s/Library/Logs/DiagnosticReports",
                System.getProperty("user.home")));
        if (reportsRoot.exists() && reportsRoot.isDirectory()) {
            final File[] allFiles = reportsRoot.listFiles();
            if (allFiles != null) {
                for (File f : allFiles) {
                    if (f.isFile() && f.getName().endsWith(".crash") && f.getName().startsWith(APP_CRASHES_MARKER)) {
                        log.debug(String.format(">>> Collecting new crash report: %s", f.getCanonicalPath()));
                        result.append(
                                new String(Files.readAllBytes(f.toPath()), Charset.forName("UTF-8"))).append("\n\n");
                        final File newPath = new File(
                                String.format("%s/%s%s", f.getParent(), USED_APP_CRASHES_MARKER, f.getName()));
                        log.debug(String.format(">>> Collection completed. Renaming the crash log to %s",
                                newPath.getCanonicalPath()));
                        if (newPath.exists()) {
                            newPath.delete();
                        }
                        f.renameTo(newPath);
                    }
                }
            }
        }
        return result.toString();
    }

    public static String getLogsAndCrashes() throws Exception {
        final File logFile = new File(String.format("%s/Library/Logs/CoreSimulator/%s/system.log",
                System.getProperty("user.home"), getId()));
        final StringBuilder result = new StringBuilder();
        if (logFile.exists()) {
            result.append(new String(Files.readAllBytes(logFile.toPath()), Charset.forName("UTF-8")));
        }
        final String crashReports = getRecentWireCrashReports();
        if (crashReports.isEmpty()) {
            result.append("\n\n\n\n\n").append("------- No new crashes were detected so far --------");
        } else {
            result.append("\n\n\n\n\n").append(crashReports);
        }
        return result.toString();
    }

    private static String getCommandOutput(String... cmd) throws Exception {
        log.debug(String.format("Executing: %s", Arrays.toString(cmd)));
        final Process process = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        final StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        final String output = builder.toString().trim();
        log.debug(String.format("Command output: %s", output));
        return output;
    }

    private static String executeXcRun(String verb, String... cmd) throws Exception {
        final String[] firstCmdPart = new String[]{XCRUN_PATH, verb};
        final String[] fullCmd = ArrayUtils.addAll(firstCmdPart, cmd);
        return getCommandOutput(fullCmd);
    }

    private static String executeSimctl(String... cmd) throws Exception {
        return executeXcRun("simctl", cmd);
    }

    private static final String[] DEPENDENT_PROCESSES_NAMES = new String[]{
            "Simulator", "osascript", "configd_sim", "xpcproxy_sim",
            "ids_simd", "launchd_sim", "xcrun"
    };

    private static void kill() throws Exception {
        log.debug("Force killing Simulator app...");
        UnixProcessHelpers.killProcessesGracefully(DEPENDENT_PROCESSES_NAMES);
    }

    public static void shutdown() throws Exception {
        if (!UnixProcessHelpers.isProcessRunning(SIMULATOR_PROCESS_NAME)) {
            return;
        }
        try {
            executeSimctl("shutdown", getId());
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (UnixProcessHelpers.isProcessRunning(SIMULATOR_PROCESS_NAME)) {
            kill();
        }
    }

    public static void reset() throws Exception {
        shutdown();
        executeSimctl("erase", getId());
    }

    private static String retryUntilSimulatorBooted(FunctionalInterfaces.ISupplierWithException<String> f)
            throws Exception {
        final long msStarted = System.currentTimeMillis();
        do {
            try {
                final String output = f.call();
                if (!output.contains("No devices are booted")) {
                    return output;
                }
            } catch (InterruptedException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(SIMULATOR_BOOTING_INTERVAL_CHECK_MS);
        } while (System.currentTimeMillis() - msStarted <= SIMULATOR_BOOT_TIMEOUT_MS);
        throw new IllegalStateException(String.format("Cannot apply simctl command after %s seconds timeout",
                SIMULATOR_BOOT_TIMEOUT_MS / 1000));
    }

    public static void installApp(File appPath) throws Exception {
        retryUntilSimulatorBooted(
                () -> executeSimctl("install", "booted", appPath.getCanonicalPath())
        );
        log.debug(String.format("Sleeping %s seconds to sync application install...", INSTALL_SYNC_TIMEOUT_MS / 1000));
        Thread.sleep(INSTALL_SYNC_TIMEOUT_MS);
    }

    public static void uninstallApp(String bundleId) throws Exception {
        retryUntilSimulatorBooted(
                () -> executeSimctl("uninstall", "booted", bundleId)
        );
    }

    public static void launchApp(String bundleId) throws Exception {
        retryUntilSimulatorBooted(
                () -> executeSimctl("launch", "booted", bundleId)
        );
    }

    public static void uploadImage(File img) throws Exception {
        if (!img.exists()) {
            throw new IllegalArgumentException(String.format(
                    "Please make sure the image %s exists and is accessible", img.getCanonicalPath()
            ));
        }
        retryUntilSimulatorBooted(
                () -> executeSimctl("addphoto", "booted", img.getCanonicalPath())
        );
        // Let Simulator to update the lib
        Thread.sleep(3000);
    }

    public static void uploadImage() throws Exception {
        uploadImage(new File(getImagesPathFromConfig(IOSSimulatorHelpers.class) + File.separator + TESTING_IMAGE_NAME));
    }

    public static void copySystemClipboardToSimulatorClipboard() throws Exception {
        activateWindow();
        CommonUtils.pressCmdVByAppleScript();
    }

    private static void findFiles(String name, File root, List<File> resultList) {
        final File[] list = root.listFiles();
        if (list == null) {
            return;
        }
        for (File fil : list) {
            if (fil.isDirectory()) {
                findFiles(name, fil, resultList);
            } else if (name.equals(fil.getName())) {
                resultList.add(fil);
            }
        }
    }

    public static List<File> getInternalApplicationsRoots() throws Exception {
        final File internalApplicationsRoot = new File(
                String.format("%s/data/Containers/Data/Application", getInternalFSRoot())
        );
        final File[] appRoots = internalApplicationsRoot.listFiles();
        if (appRoots == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(appRoots);
    }

    public static String getInternalFSRoot() throws Exception {
        return String.format("%s/Library/Developer/CoreSimulator/Devices/%s",
                System.getProperty("user.home"), getId());
    }

    public static List<File> locateFilesOnInternalFS(String name) throws Exception {
        final File root = new File(getInternalFSRoot());
        if (!root.exists()) {
            return Collections.emptyList();
        }
        final List<File> resultList = new ArrayList<>();
        findFiles(name, root, resultList);
        return resultList;
    }

    private static Optional<String> applicationPath = Optional.empty();

    // https://github.com/plu/simctl/blob/master/lib/simctl/command/launch.rb

    public static String getApplicationPath() throws Exception {
        if (!applicationPath.isPresent()) {
            final String xCodeRoot = getCommandOutput("/usr/bin/xcode-select", "-p").trim();
            applicationPath = Optional.of(String.format("%s/Applications/Simulator.app", xCodeRoot));
        }
        return applicationPath.get();
    }

    private static String getDefaultScaleFactor() throws Exception {
        // Available scale factors: 1.0, 0.75, 0.5, 0.33 and 0.25
        final String model = CommonUtils.getDeviceName(IOSSimulatorHelpers.class).toLowerCase();
        if (model.contains("ipad")) {
            return "0.33";
        }
        return "0.5";
    }

    private static String getInternalDeviceType() throws Exception {
        final String model = CommonUtils.getDeviceName(IOSSimulatorHelpers.class);
        return String.format("com.apple.CoreSimulator.SimDeviceType.%s", model.replace(" ", "-"));
    }

    public static void start() throws Exception {
        if (isBooted() && isRunning()) {
            return;
        }
        if (UnixProcessHelpers.isProcessRunning(SIMULATOR_PROCESS_NAME)) {
            // Kill other simulator if running
            kill();
        }
        log.debug(getCommandOutput("/usr/bin/open", "-Fn", getApplicationPath(),
                "--args",
                "-ConnectHardwareKeyboard", "1",
                "-CurrentDeviceUDID", getId(),
                String.format("-SimulatorWindowLastScale-%s", getInternalDeviceType()), getDefaultScaleFactor())
        );
        long msStarted = System.currentTimeMillis();
        do {
            if (isBooted()) {
                return;
            }
            Thread.sleep(SIMULATOR_BOOTING_INTERVAL_CHECK_MS);
        } while (System.currentTimeMillis() - msStarted <= SIMULATOR_BOOT_TIMEOUT_MS);
        if (!isBooted()) {
            throw new IllegalStateException(String.format("iOS Simulator booting failed after %s seconds timeout",
                    SIMULATOR_BOOT_TIMEOUT_MS / 1000));
        }
    }

    public static boolean isBooted() throws Exception {
        return UnixProcessHelpers.isProcessRunning("nsurlstoraged", Optional.of("Simulator"));
    }

    public static boolean isRunning() throws Exception {
        if (!UnixProcessHelpers.isProcessRunning(SIMULATOR_PROCESS_NAME)) {
            return false;
        }
        final String id = getId();
        final String output = executeSimctl("list");
        for (String line : output.split("\n")) {
            if (line.contains(id) && line.contains("Booted")) {
                return true;
            }
        }
        return false;
    }
}
