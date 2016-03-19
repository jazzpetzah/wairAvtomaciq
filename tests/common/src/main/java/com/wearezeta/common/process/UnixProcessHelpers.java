package com.wearezeta.common.process;

import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class UnixProcessHelpers {
    private static final Logger log = ZetaLogger.getLog(UnixProcessHelpers.class.getSimpleName());

    private static final int DEFAULT_PKILL_TIMEOUT_SECONDS = 5;

    public static void killProcessesGracefully(String... expectedNames) throws Exception {
        killProcessesGracefully(DEFAULT_PKILL_TIMEOUT_SECONDS, expectedNames);
    }

    public static void killProcessesGracefully(int timeoutSecondsUntilForceKill, String... expectedNames)
            throws Exception {
        new ProcessBuilder(ArrayUtils.addAll(new String[]{"/usr/bin/killall"}, expectedNames)).start().waitFor();
        final long millisecondsStarted = System.currentTimeMillis();
        boolean areAllProcessesTerminated = true;
        while (System.currentTimeMillis() - millisecondsStarted <= timeoutSecondsUntilForceKill * 1000) {
            final Process p = new ProcessBuilder("/bin/ps", "axco", "command").start();
            p.waitFor();
            final InputStream stream = p.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                for (String name : expectedNames) {
                    if (line.matches(".*\\b" + name.trim() + "\\b.*")) {
                        areAllProcessesTerminated = false;
                        break;
                    }
                }
                if (areAllProcessesTerminated) {
                    break;
                }
            }
            if (areAllProcessesTerminated) {
                break;
            } else {
                Thread.sleep(300);
            }
        }
        if (!areAllProcessesTerminated) {
            log.warn(String.format("Not all processes managed to stop gracefully in %s seconds. Force killing of %s...",
                    timeoutSecondsUntilForceKill, Arrays.toString(expectedNames)));
            new ProcessBuilder(ArrayUtils.addAll(new String[]{"/usr/bin/killall", "-9"}, expectedNames)).
                    start().waitFor(1, TimeUnit.SECONDS);
        }
    }
}
