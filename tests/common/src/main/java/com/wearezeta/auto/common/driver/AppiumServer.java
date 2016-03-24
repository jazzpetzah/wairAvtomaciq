package com.wearezeta.auto.common.driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.common.process.AsyncProcess;
import com.wearezeta.common.process.UnixProcessHelpers;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.InetAddress;

public class AppiumServer {
    private static final Logger log = ZetaLogger.getLog(AppiumServer.class.getSimpleName());

    private static final int PORT = 4723;
    private static final int SELENDROID_PORT = 4444;
    private static final int RESTART_TIMEOUT = 30000; // milliseconds
    private static final int IS_RUNNING_RETCODE = 22;
    private static final String[] PING_CMD = new String[]{
            "/usr/bin/curl",
            "--output", "/dev/null",
            "--fail",
            "--silent",
            "--head",
            String.format("http://127.0.0.1:%s/wd/hub", PORT)
    };

    private static boolean waitUntilIsStopped(long millisecondsTimeout) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted <= millisecondsTimeout) {
            final int exitCode = Runtime.getRuntime().exec(PING_CMD).waitFor();
            if (exitCode != IS_RUNNING_RETCODE) {
                return true;
            }
            Thread.sleep(500);
        }
        return false;
    }

    private static boolean waitUntilIsRunning(long millisecondsTimeout) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted <= millisecondsTimeout) {
            final int exitCode = Runtime.getRuntime().exec(PING_CMD).waitFor();
            if (exitCode == IS_RUNNING_RETCODE) {
                return true;
            }
            Thread.sleep(1000);
        }
        return false;
    }

    private static final String MAIN_EXECUTABLE_PATH = "/usr/local/bin/appium";
    public static final int DEFAULT_COMMAND_TIMEOUT = 500; // in seconds
    private static final String LOG_PATH = "/usr/local/var/log/appium/appium.log";

    private static final String[] DEFAULT_CMDLINE = new String[]{
            MAIN_EXECUTABLE_PATH,
            "--port", Integer.toString(PORT),
            "--session-override",
            "--selendroid-port", Integer.toString(SELENDROID_PORT),
            "--log", LOG_PATH
    };

    private static void ensureParentDirExistence(String filePath) throws Exception {
        final File log = new File(filePath);
        if (!log.getParentFile().exists()) {
            if (!log.getParentFile().mkdirs()) {
                throw new RuntimeException(String.format("The script has failed to create '%s' folder for Appium logs. " +
                                "Please make sure your account has correct access permissions on the parent folder(s)",
                        log.getParentFile().getCanonicalPath()));
            }
        }
    }

    private static void ensureAppiumExecutableExistence() throws Exception {
        if (!new File(MAIN_EXECUTABLE_PATH).exists()) {
            throw new RuntimeException(
                    String.format("The script is unable to find main Appium executable at the path '%s'. " +
                                    "Please make sure it is properly installed (`npm install -g appium`)",
                            MAIN_EXECUTABLE_PATH));
        }
    }

    public static synchronized void resetIOSSimulator() throws Exception {
        UnixProcessHelpers.killProcessesGracefully(
                "Simulator", "configd_sim", "xpcproxy_sim", "backboardd",
                "platform_launch_", "companionappd", "ids_simd", "launchd_sim",
                "CoreSimulatorBridge", "SimulatorBridge", "SpringBoard",
                "locationd", "MobileGestaltHelper", "cfprefsd",
                "assetsd", "fileproviderd", "mediaremoted",
                "routined", "assetsd", "mstreamd", "healthd", "MobileCal",
                "callservicesd", "revisiond", "touchsetupd", "calaccessd",
                "ServerFileProvider", "mobileassetd", "IMDPersistenceAgent",
                "itunesstored", "profiled", "passd", "carkitd", "instruments");
        restart();
    }

    public static synchronized void resetIOSRealDevice() throws Exception {
        UnixProcessHelpers.killProcessesGracefully("instruments");
        restart();
    }

    public static void restart() throws Exception {
        restart(DEFAULT_CMDLINE);
    }

    private static void restart(String[] cmdLine) throws Exception {
        final String hostname = InetAddress.getLocalHost().getHostName();
        log.warn(String.format("Trying to (re)start Appium server on %s:%s...", hostname, PORT));

        UnixProcessHelpers.killProcessesGracefully("node");
        waitUntilIsStopped(RESTART_TIMEOUT / 2);

        ensureAppiumExecutableExistence();
        ensureParentDirExistence(LOG_PATH);

        final AsyncProcess appiumProcess = new AsyncProcess(cmdLine, false, false).start();
        log.info(String.format("Waiting for Appium to be (re)started on %s:%s...", hostname, PORT));
        final long msStarted = System.currentTimeMillis();
        if (!waitUntilIsRunning(RESTART_TIMEOUT)) {
            throw new IllegalStateException(String.format(
                    "Appium server has failed to start after %s seconds timeout on server '%s'.\n" +
                            "Please make sure that NodeJS and Appium packages are installed properly on this machine.\n" +
                            "Appium logs:\n\n%s\n\n%s\n\n\n",
                    RESTART_TIMEOUT / 1000, hostname, appiumProcess.getStderr(), appiumProcess.getStdout()));
        }

        log.info(String.format("Appium server has been successfully (re)started after %.1f seconds " +
                "and now is listening on %s:%s", (System.currentTimeMillis() - msStarted) / 1000.0, hostname, PORT));
    }

    public static boolean isRunning() throws Exception {
        return waitUntilIsRunning(1);
    }
}
