package com.wearezeta.auto.common.driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.common.process.UnixProcessHelpers;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Optional;

public class AppiumServer {
    private static final Logger log = ZetaLogger.getLog(AppiumServer.class.getSimpleName());

    private AppiumDriverLocalService service;

    private static final int PORT = 4723;
    public static final int DEFAULT_COMMAND_TIMEOUT = 500; // in seconds
    private static final int SELENDROID_PORT = 4444;
    private static final String NODE_EXECUTABLE = "/usr/local/bin/node";
    private static final String APPIUM_EXECUTABLE = "/usr/local/lib/node_modules/appium/build/lib/main.js";
    private static final String LOG_PATH = "/usr/local/var/log/appium/appium.log";
    private static final String LOG_LEVEL = "warn";

    private static AppiumServer instance = null;

    private AppiumServer() {
        try {
            // This is to make sure there are no extra existing running instances
            UnixProcessHelpers.killProcessesGracefully("node");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.service = AppiumDriverLocalService.buildService(
                new AppiumServiceBuilder()
                        .usingDriverExecutable(new File(NODE_EXECUTABLE))
                        .withAppiumJS(new File(APPIUM_EXECUTABLE))
                        .usingPort(PORT)
                        .withLogFile(new File(LOG_PATH))
                        .withArgument(() -> "--session-override")
                        .withArgument(() -> "--selendroid-port", Integer.toString(SELENDROID_PORT))
                        .withArgument(() -> "--log-level", LOG_LEVEL)
                        .withArgument(() -> "--log-timestamp")
        );
    }

    public synchronized static AppiumServer getInstance() {
        if (instance == null) {
            instance = new AppiumServer();
        }
        return instance;
    }

    public synchronized void resetIOS() throws Exception {
        UnixProcessHelpers.killProcessesGracefully("osascript",
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

    public void restart() throws Exception {
        final String hostname = InetAddress.getLocalHost().getHostName();
        log.info(String.format("Trying to (re)start Appium server on %s:%s...", hostname, PORT));
        log.info(String.format("Waiting for Appium to be (re)started on %s:%s...", hostname, PORT));
        final long msStarted = System.currentTimeMillis();
        try {
            service.stop();
        } catch (Throwable e) {
            UnixProcessHelpers.killProcessesGracefully("node");
            e.printStackTrace();
        }
        service.start();
        log.info(String.format("Appium server has been successfully (re)started after %.1f seconds " +
                "and now is listening on %s:%s", (System.currentTimeMillis() - msStarted) / 1000.0, hostname, PORT));
    }

    public boolean isRunning() throws Exception {
        return service.isRunning();
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
}
