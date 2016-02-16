package com.wearezeta.auto.common.driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.common.process.AsyncProcess;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

public class AppiumServerTools {
    private static final Logger log = ZetaLogger.getLog(AppiumServerTools.class.getSimpleName());

    private static final int PORT = 4723;
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

    private static boolean waitUnlessIsStopped(long millisecondsTimeout) throws Exception {
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

    private static boolean waitUnlessIsRunning(long millisecondsTimeout) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted <= millisecondsTimeout) {
            final int exitCode = Runtime.getRuntime().exec(PING_CMD).waitFor();
            if (exitCode == IS_RUNNING_RETCODE) {
                return true;
            }
            Thread.sleep(500);
        }
        return false;
    }

    private static final String MAIN_EXECUTABLE_PATH = "/usr/local/bin/appium";
    private static final String COMMAND_TIMEOUT = "500"; // in seconds
    private static final String LOG_PATH = "/usr/local/var/log/appium/appium.log";

    private static final String[] CMDLINE_IOS = new String[]{
            MAIN_EXECUTABLE_PATH,
            "--command-timeout", COMMAND_TIMEOUT,
            "--port", Integer.toString(PORT),
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
        Runtime.getRuntime().exec(new String[]{"/usr/bin/killall", "-9",
                "Simulator", "configd_sim", "ids_simd", "launchd_sim", "instruments"}).waitFor(2, TimeUnit.SECONDS);
        restartAppium(CMDLINE_IOS);
    }

    public static synchronized void resetIOSRealDevice() throws Exception {
        Runtime.getRuntime().exec(new String[]{"/usr/bin/killall", "-9", "instruments"}).waitFor(2, TimeUnit.SECONDS);
        restartAppium(CMDLINE_IOS);
    }

    private static void restartAppium(String[] cmdLine) throws Exception {
        final String hostname = InetAddress.getLocalHost().getHostName();
        log.warn(String.format("Trying to restart Appium server on %s:%s...", hostname, PORT));

        Runtime.getRuntime().exec(new String[]{"/usr/bin/killall", "-9", "node"}).waitFor(2, TimeUnit.SECONDS);
        waitUnlessIsStopped(RESTART_TIMEOUT / 2);

        ensureAppiumExecutableExistence();
        ensureParentDirExistence(LOG_PATH);

        final AsyncProcess appiumProcess = new AsyncProcess(cmdLine, false, false).start();
        log.info(String.format(
                "Waiting %s seconds for Appium to be restarted on %s:%s...", hostname, RESTART_TIMEOUT / 1000, PORT));
        if (!waitUnlessIsRunning(RESTART_TIMEOUT)) {
            throw new IllegalStateException(String.format(
                    "Appium server has failed to start after %s seconds timeout on server '%s'.\n" +
                            "Appium logs:\n\n%s\n\n%s\n\n\n",
                    RESTART_TIMEOUT / 1000, hostname, appiumProcess.getStderr(), appiumProcess.getStdout()));
        }

        log.info(String.format("Appium server has been successfully restarted and now is listening on %s:%s",
                hostname, PORT));
    }
}
