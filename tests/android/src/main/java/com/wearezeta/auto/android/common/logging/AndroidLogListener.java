package com.wearezeta.auto.android.common.logging;

import java.util.*;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.common.process.AsyncProcess;

public final class AndroidLogListener {
    private static final Logger log = ZetaLogger
            .getLog(AndroidLogListener.class.getSimpleName());

    public static final String ADB_PREFIX = "";

    public enum ListenerType {
        DEFAULT(null), PERF("LoadTimeLoggerController");

        private final String tags;

        ListenerType(String tags) {
            this.tags = tags;
        }

        public String getTags() {
            return this.tags;
        }
    }

    private static Map<ListenerType, AndroidLogListener> instances = new HashMap<>();

    public static synchronized AndroidLogListener getInstance(
            ListenerType listenerType) {
        if (instances.isEmpty()) {
            for (ListenerType type : ListenerType.values()) {
                instances.put(type, new AndroidLogListener(type.getTags()));
            }
        }
        return instances.get(listenerType);
    }

    private String tags;

    private AndroidLogListener(String tags) {
        this.tags = tags;
    }

    public boolean isRunning() {
        if (listener == null) {
            return false;
        }
        return listener.isRunning();
    }

    private AsyncProcess listener;

    public void start() throws Exception {
        if (this.isRunning()) {
            log.warn("Restarting Android log listener...");
            this.stop();
        }
        String adbCmd;
        if (this.tags == null) {
            adbCmd = ADB_PREFIX + "adb logcat -v time";
        } else {
            adbCmd = ADB_PREFIX
                    + String.format("adb logcat -v time -s %s", this.tags);
        }
        final String[] cmd = new String[]{"/bin/bash", "-c", adbCmd};
        listener = new AsyncProcess(cmd, (this.tags != null),
                (this.tags != null));
        listener.start();
    }

    public void stop() throws Exception {
        if (!this.isRunning()) {
            throw new IllegalStateException(
                    "The listener has to be started first");
        }
        this.listener.stop(2, new int[]{this.listener.getPid() + 1,
                this.listener.getPid()}, 5000);
    }

    public String getStdOut() {
        if (this.listener == null) {
            throw new IllegalStateException(
                    "The listener has to be started first");
        }
        return this.listener.getStdout();
    }

    public String getStdErr() {
        if (this.listener == null) {
            throw new IllegalStateException(
                    "The listener has to be started first");
        }
        return this.listener.getStderr();
    }

    public static void writeDeviceLogsToConsole(
            final AndroidLogListener listener, final LoggingProfile loggingProfile) throws Exception {
        final String stderr = listener.getStdErr();
        if (stderr.length() > 0) {
            log.debug("=== CAPTURED STDERR LOGS ===\n");
            System.out.println(stderr.trim());
            log.debug("=== END OF CAPTURED STDERR LOGS ===\n\n\n");
        }

        final StringBuilder stdout = new StringBuilder();
        for (String line : listener.getStdOut().trim().split("\n")) {
            final String lineWithTerminator = line + "\n";
            final Optional<List<String>> stdoutIncludePatterns = loggingProfile.getIncludePatterns();
            final Optional<List<String>> stdoutExcludePatterns = loggingProfile.getExcludePatterns();
            if (stdoutIncludePatterns.isPresent()) {
                boolean isLineAccepted = false;
                for (String incPatt : stdoutIncludePatterns.get()) {
                    if (line.contains(incPatt)) {
                        if (stdoutExcludePatterns.isPresent()) {
                            for (String excPatt : stdoutExcludePatterns.get()) {
                                if (!line.contains(excPatt)) {
                                    stdout.insert(0, lineWithTerminator);
                                    isLineAccepted = true;
                                    break;
                                }
                            }
                        } else {
                            isLineAccepted = true;
                        }
                    }
                    if (isLineAccepted) {
                        break;
                    }
                }
            } else {
                if (stdoutExcludePatterns.isPresent()) {
                    for (String excPatt : stdoutExcludePatterns.get()) {
                        if (!line.contains(excPatt)) {
                            stdout.insert(0, lineWithTerminator);
                            break;
                        }
                    }
                } else {
                    stdout.insert(0, lineWithTerminator);
                }
            }
        }
        if (stdout.toString().length() > 0) {
            log.debug("=== CAPTURED STDOUT LOGS ===\n");
            System.out.println(stdout.toString().trim());
            log.debug("=== END OF CAPTURED STDOUT LOGS ===" +
                    "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }
    }

    public static void forceStopAll() {
        if (!instances.isEmpty()) {
            for (AndroidLogListener listener : instances.values()) {
                if (listener.isRunning()) {
                    try {
                        listener.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
