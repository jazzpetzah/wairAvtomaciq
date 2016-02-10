package com.wearezeta.auto.common.driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;

import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class AppiumServerTools {
    private static final Logger log = ZetaLogger.getLog(AppiumServerTools.class.getSimpleName());

    private static final int PORT = 4723;
    private static final int RESTART_TIMEOUT = 20000; // milliseconds

    private static boolean waitUntilPortOpened() throws InterruptedException {
        Socket s = null;
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted <= RESTART_TIMEOUT) {
            try {
                s = new Socket("127.0.0.1", PORT);
                return true;
            } catch (Exception e) {
                Thread.sleep(500);
            } finally {
                if (s != null) {
                    try {
                        s.close();
                    } catch (Exception e) {
                        // Ignore silently
                    }
                }
            }
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

    public static synchronized void resetIOSSimulator() throws Exception {
        Runtime.getRuntime().exec(new String[]{"/usr/bin/killall", "-9",
                "Simulator", "configd_sim", "ids_simd", "launchd_sim", "instruments", "node"}).
                waitFor(2, TimeUnit.SECONDS);
        log.warn("Trying to restart Appium server on localhost...");
        Runtime.getRuntime().exec(CMDLINE_IOS);
        waitForAppiumRestart();
    }

    public static synchronized void resetIOSRealDevice() throws Exception {
        Runtime.getRuntime().exec(new String[]{"/usr/bin/killall", "-9", "instruments", "node"}).
                waitFor(2, TimeUnit.SECONDS);
        log.warn("Trying to restart Appium server on localhost...");
        Runtime.getRuntime().exec(CMDLINE_IOS);
        waitForAppiumRestart();
    }

    private static void waitForAppiumRestart() throws Exception {
        log.info(String.format("Waiting %s seconds for Appium port %s to be opened...", RESTART_TIMEOUT / 1000, PORT));
        Thread.sleep(1000);
        if (!waitUntilPortOpened()) {
            throw new IllegalStateException(String.format(
                    "Appium server has failed to restart after %s seconds timeout", RESTART_TIMEOUT / 1000));
        }
        log.info(String.format("Appium server has been successfully restarted and now is listening on port %s", PORT));
    }
}
