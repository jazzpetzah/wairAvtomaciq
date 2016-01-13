package com.wearezeta.auto.common.driver;

import java.net.Socket;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;

final class LazyDriverInitializer implements Callable<RemoteWebDriver> {
    private static final Logger log = ZetaLogger
            .getLog(LazyDriverInitializer.class.getSimpleName());

    private static final String APPIUM_EXECUTOR_APP_PATH = "/Applications/AutorunAppium.app";
    private static final int APPIUM_PORT = 4723;

    private String url;
    private DesiredCapabilities capabilities;
    private Platform platform;
    private int maxRetryCount;
    private Consumer<RemoteWebDriver> initCompletedCallback;
    private Supplier<Boolean> beforeInitCallback;

    public LazyDriverInitializer(Platform platform, String url,
                                 DesiredCapabilities capabilities, int maxRetryCount,
                                 Consumer<RemoteWebDriver> initCompletedCallback,
                                 Supplier<Boolean> beforeInitCallback) {
        this.url = url;
        this.capabilities = capabilities;
        this.platform = platform;
        this.maxRetryCount = maxRetryCount;
        this.initCompletedCallback = initCompletedCallback;
        this.beforeInitCallback = beforeInitCallback;
    }

    private static final int APPIUM_RESTART_TIMEOUT = 15000; // milliseconds

    private static boolean waitUntilAppiumPortOpened() throws InterruptedException {
        Socket s = null;
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted <= APPIUM_RESTART_TIMEOUT) {
            try {
                s = new Socket("127.0.0.1", APPIUM_PORT);
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

    private synchronized void resetAppiumServer() throws Exception {
        log.warn("Trying to restart Appium server on localhost...");
        Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c",
                String.format("open -a %s", APPIUM_EXECUTOR_APP_PATH)});
        Thread.sleep(5000);
        log.info(String.format("Waiting %s seconds for Appium port %s to be opened...",
                APPIUM_RESTART_TIMEOUT / 1000, APPIUM_PORT));
        if (!waitUntilAppiumPortOpened()) {
            throw new IllegalStateException(String.format(
                    "Appium server has failed to restart after %s seconds timeout",
                    APPIUM_RESTART_TIMEOUT / 1000));
        }
        log.info(String.format(
                "Appium server has been successfully restarted and now is listening on port %s",
                APPIUM_PORT));
    }

    @Override
    public RemoteWebDriver call() throws Exception {

        if (Thread.currentThread().isInterrupted()) {
            return null;
        }

        if (this.beforeInitCallback != null) {
            log.debug("Invoking driver pre-initialization callback...");
            if (!beforeInitCallback.get()) {
                log.error("Driver pre-initialization callback returned False. Will skip driver initialization!");
                return null;
            }
            log.debug("Driver pre-initialization callback has been successfully invoked");
        }
        int ntry = 1;
        do {
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }

            log.debug(String.format(
                    "Creating driver instance for platform '%s'...",
                    this.platform.name()));
            RemoteWebDriver platformDriver;
            try {
                switch (this.platform) {
                    case Mac:
                        platformDriver = new ZetaOSXDriver(new URL(url),
                                capabilities);
                        break;
                    case iOS:
                        platformDriver = new ZetaIOSDriver(new URL(url),
                                capabilities);
                        break;
                    case Android:
                        platformDriver = new ZetaAndroidDriver(new URL(url),
                                capabilities);
                        break;
                    case Web:
                        platformDriver = new ZetaWebAppDriver(new URL(url),
                                capabilities);
                        platformDriver.setFileDetector(new LocalFileDetector());
                        platformDriver.manage().window()
                                .setPosition(new Point(0, 0));
                        break;
                    default:
                        throw new RuntimeException(String.format(
                                "Platform '%s' is unknown", this.platform.name()));
                }

                if (Thread.currentThread().isInterrupted()) {
                    platformDriver.quit();
                    return null;
                }

                if (initCompletedCallback != null) {
                    log.debug("Invoking driver post-initialization callback...");
                    initCompletedCallback.accept(platformDriver);
                    log.debug("Driver post-initialization callback has been successfully invoked");
                }

                if (Thread.currentThread().isInterrupted()) {
                    platformDriver.quit();
                    return null;
                }

                log.debug(String
                        .format("Successfully created driver instance for platform '%s'",
                                this.platform.name()));
                return platformDriver;
            } catch (WebDriverException e) {
                log.debug(String
                        .format("Driver initialization failed. Trying to recreate (%d of %d)...",
                                ntry, this.maxRetryCount));
                e.printStackTrace();
                if (ntry >= this.maxRetryCount) {
                    throw e;
                } else {
                    ntry++;
                    if (this.platform == Platform.iOS) {
                        resetAppiumServer();
                    }
                }
            }
        } while (ntry <= this.maxRetryCount);
        throw new WebDriverException(
                "Selenium driver initialization failed. Please make sure that the corresponding node is up and running.");
    }
}
