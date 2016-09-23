package com.wearezeta.auto.common.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public class AsyncProcess {
    private static final int IS_RUNNING_CHECK_INTERVAL = 100; // milliseconds
    private static final String STDOUT_LOG_PREFIX = "STDOUT: ";
    private static final String STDERR_LOG_PREFIX = "STDERR: ";

    private static final Logger log = ZetaLogger.getLog(AsyncProcess.class.getSimpleName());

    private final String[] cmd;
    private final boolean shouldLogStdOut;
    private Optional<StringBuilder> stdOut = Optional.empty();
    private Optional<StringBuilder> stdErr = Optional.empty();
    private final boolean shouldLogStdErr;
    private Optional<Process> process = Optional.empty();
    private Optional<Thread> stdOutMonitor = Optional.empty();
    private Optional<Thread> stdErrMonitor = Optional.empty();

    public AsyncProcess(String[] cmd, boolean shouldLogStdOut, boolean shouldLogStdErr) {
        this.cmd = cmd;
        this.shouldLogStdOut = shouldLogStdOut;
        this.shouldLogStdErr = shouldLogStdErr;
    }

    private Thread createListenerThread(final BufferedReader reader, final String logPrefix) {
        return new Thread() {
            @Override
            public void run() {
                do {
                    try {
                        if (reader.ready()) {
                            final String logLine = reader.readLine();
                            if (logLine != null) {
                                if (logPrefix.equals(STDOUT_LOG_PREFIX)) {
                                    if (AsyncProcess.this.shouldLogStdOut) {
                                        log.debug(String.format("%s%s", logPrefix, logLine));
                                    }
                                    if (AsyncProcess.this.stdOut.isPresent()) {
                                        AsyncProcess.this.stdOut.get().append(logLine).append("\n");
                                    }
                                }
                                if (logPrefix.equals(STDERR_LOG_PREFIX)) {
                                    if (AsyncProcess.this.shouldLogStdErr) {
                                        log.debug(String.format("%s%s", logPrefix, logLine));
                                    }
                                    if (AsyncProcess.this.stdErr.isPresent()) {
                                        AsyncProcess.this.stdErr.get().append(logLine).append("\n");
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        break;
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        break;
                    }
                } while (!this.isInterrupted());
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public synchronized AsyncProcess start() throws Exception {
        if (this.isRunning()) {
            this.stop(9, new int[]{this.getPid()}, 2000);
        }
        this.process = Optional.of(new ProcessBuilder(cmd).start());
        final BufferedReader stdout = new BufferedReader(new InputStreamReader(process.get().getInputStream()));
        this.stdOut = Optional.of(new StringBuilder());
        this.stdOutMonitor = Optional.of(createListenerThread(stdout, STDOUT_LOG_PREFIX));
        this.stdOutMonitor.get().start();
        final BufferedReader stderr = new BufferedReader(new InputStreamReader(process.get().getErrorStream()));
        this.stdErr = Optional.of(new StringBuilder());
        this.stdErrMonitor = Optional.of(createListenerThread(stderr, STDERR_LOG_PREFIX));
        this.stdErrMonitor.get().start();
        return this;
    }

    public boolean isRunning() {
        return this.process.isPresent() && this.process.get().isAlive();
    }

    /**
     * @param signal              system signal number, for example 2 is SIGINT. If null then
     *                            Java will try to kill the process using the standard lib
     * @param pids                list of system pids to kill
     * @param timeoutMilliseconds application termination timeout
     * @throws Exception
     */
    public synchronized void stop(Integer signal, int[] pids, long timeoutMilliseconds) throws Exception {
        try {
            if (!this.process.isPresent() || !this.isRunning()) {
                return;
            }
            if (signal == null) {
                process.get().destroy();
            } else {
                for (final int pid : pids) {
                    final String killCmd = String.format("kill -%s %s", signal, pid);
                    log.debug("Executing: " + killCmd);
                    Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", killCmd});
                }
            }
            long milliSecondsElapsed = 0;
            while (this.isRunning()) {
                Thread.sleep(IS_RUNNING_CHECK_INTERVAL);
                milliSecondsElapsed += IS_RUNNING_CHECK_INTERVAL;
                if (milliSecondsElapsed >= timeoutMilliseconds) {
                    throw new TimeoutException(
                            String.format("The application %s has not been stopped after %s milliseconds timeout",
                                    Arrays.toString(this.cmd), timeoutMilliseconds));
                }
            }
            log.debug(String.format("The application %s has been successfully stopped after %s millisecond(s)",
                    Arrays.toString(this.cmd), milliSecondsElapsed));
        } finally {
            if (stdOutMonitor.isPresent() && stdOutMonitor.get().isAlive()) {
                stdOutMonitor.get().interrupt();
            }
            if (stdErrMonitor.isPresent() && stdErrMonitor.get().isAlive()) {
                stdErrMonitor.get().interrupt();
            }
        }
    }

    public void stop(long timeoutMilliseconds) throws Exception {
        this.stop(null, null, timeoutMilliseconds);
    }

    public String getStdout() {
        if (this.stdOut.isPresent()) {
            return stdOut.get().toString();
        } else {
            return "";
        }
    }

    public void resetStdOut() {
        if (this.stdOut.isPresent()) {
            stdOut = Optional.of(new StringBuilder(""));
        }
    }

    public String getStderr() {
        if (this.stdErr.isPresent()) {
            return stdErr.get().toString();
        } else {
            return "";
        }
    }

    public void resetStdErr() {
        if (this.stdErr.isPresent()) {
            stdErr = Optional.of(new StringBuilder(""));
        }
    }

    /**
     * http://www.golesny.de/p/code/javagetpid
     *
     * @return process id
     * @throws Exception
     */
    public int getPid() throws Exception {
        if (!process.isPresent() || !this.isRunning()) {
            throw new IllegalStateException("PID is not available while the process is not running");
        }
        if (process.get().getClass().getName().equals("java.lang.UNIXProcess")) {
            Field f = process.get().getClass().getDeclaredField("pid");
            f.setAccessible(true);
            return f.getInt(process.get());
        } else {
            throw new UnsupportedOperationException("getPid implementation is not available for non-Unix systems");
        }
    }
}
