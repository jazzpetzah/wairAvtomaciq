package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.wearezeta.auto.common.driver.device_helpers.IOSSimulatorHelpers;
import com.wearezeta.auto.common.process.UnixProcessHelpers;
import org.apache.log4j.Logger;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;

import static com.wearezeta.auto.common.driver.ZetaAndroidDriver.ADB_PREFIX;

final class LazyDriverInitializer implements Callable<RemoteWebDriver> {
    private static final Logger log = ZetaLogger.getLog(LazyDriverInitializer.class.getSimpleName());


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

    private static final class UnknownPlatformError extends Exception {
        public UnknownPlatformError(String message) {
            super(message);
        }
    }

    @Override
    public RemoteWebDriver call() throws Exception {
        if (this.beforeInitCallback != null) {
            log.debug("Invoking driver pre-initialization callback...");
            if (!beforeInitCallback.get()) {
                log.error("Driver pre-initialization callback returned False. Will skip driver initialization!");
                return null;
            }
            log.debug("Driver pre-initialization callback has been successfully invoked");
        }
        int ntry = 1;
        final AppiumServer appiumServer = AppiumServer.getInstance();
        do {
            log.debug(String.format("Creating driver instance for platform '%s'...", this.platform.name()));
            RemoteWebDriver platformDriver;
            try {
                switch (this.platform) {
                    case Mac:
                        platformDriver = new ZetaOSXDriver(new URL(url), capabilities);
                        break;
                    case iOS:
                        UnixProcessHelpers.killProcessesGracefully("xcodebuild", "XCTRunner");
                        if (!appiumServer.isRunning() || ntry > 1) {
                            appiumServer.restart();
                            if (ntry > 1 && capabilities.getCapability("udid") == null) {
                                IOSSimulatorHelpers.shutdown();
                                IOSSimulatorHelpers.start();
                            }
                        }
                        platformDriver = new ZetaIOSDriver(new URL(url), capabilities);
                        platformDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                        break;
                    case Android:
                        if (!appiumServer.isRunning() || ntry > 1) {
                            new ProcessBuilder("/bin/bash", "-c",
                                    ADB_PREFIX + "adb shell am start -n io.appium.unlock/.Unlock")
                                    .start()
                                    .waitFor(10, TimeUnit.SECONDS);
                            appiumServer.restart();
                        }
                        platformDriver = new ZetaAndroidDriver(new URL(url), capabilities);
                        platformDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                        break;
                    case Web:
                        platformDriver = new ZetaWebAppDriver(new URL(url), capabilities);
                        platformDriver.setFileDetector(new LocalFileDetector());
                        platformDriver.manage().window().setPosition(new Point(0, 0));
                        break;
                    default:
                        throw new UnknownPlatformError(String.format("Platform '%s' is unknown", this.platform.name()));
                }

                if (initCompletedCallback != null) {
                    log.debug("Invoking driver post-initialization callback...");
                    initCompletedCallback.accept(platformDriver);
                    log.debug("Driver post-initialization callback has been successfully invoked");
                }

                log.debug(String.format("Successfully created driver instance for platform '%s'", this.platform.name()));
                return platformDriver;
            } catch (WebDriverException e) {
                if (ntry >= this.maxRetryCount) {
                    throw e;
                }
                ntry++;
                e.printStackTrace();
                log.debug(String.format("Driver initialization failed. Trying to recreate (%d of %d)...",
                        ntry, this.maxRetryCount));
            }
        } while (ntry <= this.maxRetryCount);
        throw new WebDriverException(
                "Selenium driver initialization failed. Please make sure that the corresponding node is up and running.");
    }
}
