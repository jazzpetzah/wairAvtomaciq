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
        boolean areAllProcessesTerminated = false;
        Set<String> runningProcesses = new HashSet<>();
        while (System.currentTimeMillis() - millisecondsStarted <= timeoutSecondsUntilForceKill * 1000) {
            final Process p = new ProcessBuilder("/bin/ps", "axco", "command").start();
            final InputStream stream = p.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            runningProcesses.clear();
            while ((line = reader.readLine()) != null) {
                runningProcesses.add(line);
            }
            runningProcesses.retainAll(new HashSet<>(Arrays.asList(expectedNames)));
            if (runningProcesses.isEmpty()) {
                areAllProcessesTerminated = true;
                break;
            } else {
                Thread.sleep(100);
            }
        }
        if (!areAllProcessesTerminated) {
            log.warn(String.format("Not all processes managed to stop gracefully in %s seconds. Force killing of %s...",
                    timeoutSecondsUntilForceKill, runningProcesses));
            new ProcessBuilder(ArrayUtils.addAll(new String[]{"/usr/bin/killall", "-9"}, expectedNames)).
                    start().waitFor(1, TimeUnit.SECONDS);
        }
    }
}
