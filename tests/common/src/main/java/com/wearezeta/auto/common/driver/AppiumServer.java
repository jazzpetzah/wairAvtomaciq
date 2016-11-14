package com.wearezeta.auto.common.driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.process.UnixProcessHelpers;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.net.UrlChecker;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class AppiumServer {
    private static final Logger log = ZetaLogger.getLog(AppiumServer.class.getSimpleName());

    private static AppiumServer instance = null;

    private AppiumServer() {
        ensureAppiumExecutableExistence();
        ensureParentDirExistence(LOG_PATH);
    }

    public synchronized static AppiumServer getInstance() {
        if (instance == null) {
            instance = new AppiumServer();
        }
        return instance;
    }

    private static final int PORT = 4723;
    private static final int SELENDROID_PORT = 4444;
    public static final int RESTART_TIMEOUT_MILLIS = 90000; // milliseconds
    private static final String SERVER_URL = String.format("http://127.0.0.1:%d/wd/hub", PORT);

    private boolean waitUntilIsRunning(long millisecondsTimeout) throws Exception {
        final URL status = new URL(SERVER_URL + "/sessions");
        try {
            new UrlChecker().waitUntilAvailable(millisecondsTimeout, TimeUnit.MILLISECONDS, status);
            return true;
        } catch (UrlChecker.TimeoutException e) {
            return false;
        }
    }

    private static final String APPIUM_SCRIPT_PATH = "/usr/local/bin/appium";
    private static final String NODE_EXECUTABLE_PATH = "/usr/local/bin/node";
    public static final int DEFAULT_COMMAND_TIMEOUT = 500; // in seconds
    private static final String LOG_PATH = "/usr/local/var/log/appium/appium.log";

    private static final String[] DEFAULT_CMDLINE = new String[]{
            NODE_EXECUTABLE_PATH,
            APPIUM_SCRIPT_PATH,
            "--port", Integer.toString(PORT),
            "--session-override",
            "--selendroid-port", Integer.toString(SELENDROID_PORT),
            "--log", LOG_PATH,
            "--log-timestamp",
            "> /dev/null 2>&1",
            "&"
    };

    private static void ensureParentDirExistence(String filePath) {
        final File log = new File(filePath);
        if (!log.getParentFile().exists()) {
            if (!log.getParentFile().mkdirs()) {
                throw new IllegalStateException(String.format(
                        "The script has failed to create '%s' folder for Appium logs. " +
                                "Please make sure your account has correct access permissions on the parent folder(s)",
                        log.getParentFile().getAbsolutePath()));
            }
        }
    }

    private static void ensureAppiumExecutableExistence() {
        if (!new File(APPIUM_SCRIPT_PATH).exists()) {
            throw new IllegalStateException(
                    String.format("The script is unable to find main Appium executable at the path '%s'. " +
                                    "Please make sure it is properly installed (`npm install -g appium`)",
                            APPIUM_SCRIPT_PATH));
        }
    }

    public static void resetXCTest() throws Exception {
        UnixProcessHelpers.killProcessesGracefully("xcodebuild", "XCTRunner", "iproxy");
    }

    public synchronized void restart() throws Exception {
        final String hostname = InetAddress.getLocalHost().getHostName();
        log.info(String.format("Trying to (re)start Appium server on %s:%s...", hostname, PORT));
        UnixProcessHelpers.killProcessesGracefully("node");

        final File scriptFile = File.createTempFile("script", ".sh");
        try {
            final List<String> scriptContent = new ArrayList<>();
            scriptContent.add("#!/bin/bash");
            Collections.addAll(scriptContent, String.join(" ", DEFAULT_CMDLINE));
            try (Writer output = new BufferedWriter(new FileWriter(scriptFile))) {
                output.write(String.join("\n", scriptContent));
            }
            log.info(String.format("Waiting for Appium to be (re)started on %s:%s...", hostname, PORT));
            final long msStarted = System.currentTimeMillis();
            new ProcessBuilder("/bin/bash", scriptFile.getCanonicalPath()).
                    redirectErrorStream(true).start().waitFor(RESTART_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            if (!waitUntilIsRunning(RESTART_TIMEOUT_MILLIS)) {
                throw new WebDriverException(
                        String.format("Appium server has failed to start after %s seconds timeout on server '%s'.\n"
                                + "Please make sure that NodeJS and Appium packages are installed properly on this machine.\n"
                                + "Appium logs:\n\n%s\n\n\n", RESTART_TIMEOUT_MILLIS / 1000, hostname, getLog().orElse(""))
                );
            }

            log.info(String.format("Appium server has been successfully (re)started after %.1f seconds " +
                    "and now is listening on %s:%s", (System.currentTimeMillis() - msStarted) / 1000.0, hostname, PORT));
        } finally {
            scriptFile.delete();
        }
    }

    public boolean isRunning() throws Exception {
        return waitUntilIsRunning(1500);
    }

    public Optional<String> getLog() {
        final File logFile = new File(LOG_PATH);
        if (logFile.exists()) {
            try {
                return Optional.of(new String(Files.readAllBytes(logFile.toPath()), Charset.forName("UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public void resetLog() {
        final File logFile = new File(LOG_PATH);
        if (logFile.exists()) {
            try {
                final PrintWriter writer = new PrintWriter(logFile);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
