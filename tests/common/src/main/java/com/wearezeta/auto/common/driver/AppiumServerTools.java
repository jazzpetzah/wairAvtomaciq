package com.wearezeta.auto.common.driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;

import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class AppiumServerTools {
    private static final Logger log = ZetaLogger
            .getLog(AppiumServerTools.class.getSimpleName());

    private static final String EXECUTOR_APP = "AutorunAppium";
    private static final int PORT = 4723;
    private static final int RESTART_TIMEOUT = 10000; // milliseconds

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

    public static synchronized void resetIOSSimulator() throws Exception {
        Runtime.getRuntime().exec(new String[]{"/usr/bin/killall", "-9",
                "Simulator", "configd_sim", "ids_simd", "launchd_sim", "instruments"}).waitFor(2, TimeUnit.SECONDS);
        reset();
    }

    public static synchronized void resetIOSRealDevice() throws Exception {
        Runtime.getRuntime().exec(new String[]{"/usr/bin/killall", "-9", "instruments"}).waitFor(2, TimeUnit.SECONDS);
        reset();
    }

    private static void reset() throws Exception {
        log.warn("Trying to restart Appium server on localhost...");
        Runtime.getRuntime().exec(new String[]{"/usr/bin/open", "-a", EXECUTOR_APP}).
                waitFor(RESTART_TIMEOUT, TimeUnit.MILLISECONDS);
        Thread.sleep(RESTART_TIMEOUT);
        log.info(String.format("Waiting %s seconds for Appium port %s to be opened...", RESTART_TIMEOUT / 1000, PORT));
        if (!waitUntilPortOpened()) {
            throw new IllegalStateException(String.format(
                    "Appium server has failed to restart after %s seconds timeout", RESTART_TIMEOUT / 1000));
        }
        log.info(String.format("Appium server has been successfully restarted and now is listening on port %s", PORT));
    }
}
