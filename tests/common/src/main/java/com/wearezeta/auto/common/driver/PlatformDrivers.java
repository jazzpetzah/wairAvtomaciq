package com.wearezeta.auto.common.driver;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;

public final class PlatformDrivers {
    private static final Logger log = ZetaLogger.getLog(PlatformDrivers.class.getSimpleName());

    private Map<Platform, Future<? extends RemoteWebDriver>> drivers = new ConcurrentHashMap<>();

    private static PlatformDrivers instance = null;

    private PlatformDrivers() {
    }

    public synchronized static PlatformDrivers getInstance() {
        if (instance == null) {
            instance = new PlatformDrivers();
        }
        return instance;
    }

    public boolean hasDriver(Platform platform) {
        return this.drivers.containsKey(platform);
    }

    public synchronized Future<? extends RemoteWebDriver> resetDriver(
            String url, DesiredCapabilities capabilities, int maxRetryCount,
            Consumer<RemoteWebDriver> initCompletedCallback,
            Supplier<Boolean> beforeInitCallback) throws Exception {
        final Platform platformInCapabilities = Platform.getByName(capabilities.getCapability("platformName").toString());
        if (this.hasDriver(platformInCapabilities)) {
            this.quitDriver(platformInCapabilities);
        }
        final LazyDriverInitializer initializer = new LazyDriverInitializer(
                platformInCapabilities, url, capabilities, maxRetryCount,
                initCompletedCallback, beforeInitCallback);
        final ExecutorService pool = Executors.newSingleThreadExecutor();
        Future<RemoteWebDriver> driverBeingCreated = pool.submit(initializer);
        drivers.put(platformInCapabilities, driverBeingCreated);
        pool.shutdown();
        return driverBeingCreated;
    }

    public synchronized Future<? extends RemoteWebDriver> resetDriver(
            String url, DesiredCapabilities capabilities, int maxRetryCount) throws Exception {
        return resetDriver(url, capabilities, maxRetryCount, null, null);
    }

    public Future<? extends RemoteWebDriver> resetDriver(String url, DesiredCapabilities capabilities)
            throws Exception {
        return resetDriver(url, capabilities, 1, null, null);
    }

    private static void setImplicitWaitTimeout(RemoteWebDriver driver, int count, TimeUnit unit) throws Exception {
        driver.manage().timeouts().implicitlyWait(count, unit);
    }

    public static boolean isMobileDriver(RemoteWebDriver driver) {
        return (driver instanceof ZetaIOSDriver) || (driver instanceof ZetaAndroidDriver) || (driver instanceof ZetaOSXDriver);
    }

    public static void setDefaultImplicitWaitTimeout(RemoteWebDriver driver) throws Exception {
        if (isMobileDriver(driver)) {
            setImplicitWaitTimeout(driver, 0, TimeUnit.SECONDS);
        } else {
            setImplicitWaitTimeout(driver,
                    Integer.parseInt(CommonUtils.getDriverTimeoutFromConfig(PlatformDrivers.class)), TimeUnit.SECONDS);
        }
    }

    public Future<? extends RemoteWebDriver> getDriver(Platform platform) {
        if (!drivers.containsKey(platform)) {
            throw new RuntimeException(String.format("Please initialize %s platform driver first", platform.name()));
        }
        return drivers.get(platform);
    }

    public static WebDriverWait createDefaultExplicitWait(RemoteWebDriver driver) throws Exception {
        return new WebDriverWait(driver, Integer.parseInt(CommonUtils.getDriverTimeoutFromConfig(PlatformDrivers.class)));
    }

    private static final long DRIVER_CANCELLATION_TIMEOUT = 60; // seconds

    public synchronized void quitDriver(Platform platform) throws Exception {
        try {
            final Future<? extends RemoteWebDriver> futureDriver = drivers.get(platform);
            if (!futureDriver.isCancelled()) {
                try {
                    final RemoteWebDriver driver = futureDriver.get(DRIVER_CANCELLATION_TIMEOUT, TimeUnit.SECONDS);
                    if (platform == Platform.iOS) {
                        // Do not keep non-closed alerts on iOS
                        try {
                            driver.switchTo().alert().accept();
                        } catch (Exception e) {
                            // just ignore it
                        }
                    }
                    driver.quit();
                    log.debug(String.format("Successfully quit driver instance for platform '%s'", platform.name()));
                } catch (Exception e) {
                    e.printStackTrace();
                    futureDriver.cancel(true);
                    log.warn(String.format("Canceled driver for platform '%s'", platform.getName()));
                }
            }
        } finally {
            drivers.remove(platform);
        }
    }

    public void pingDrivers() throws Exception {
        for (Future<? extends RemoteWebDriver> driver : drivers.values()) {
            if (driver.isDone() && !driver.isCancelled()) {
                driver.get().getPageSource();
            } else {
                log.warn("The driver was not pinged, because it is still being created");
            }
        }
    }

    public Map<Platform, Future<? extends RemoteWebDriver>> getDrivers() {
        return drivers;
    }

    {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (Future<? extends RemoteWebDriver> futureDriver : drivers.values()) {
                    try {
                        if (futureDriver.isDone() && !futureDriver.isCancelled()) {
                            futureDriver.get().quit();
                        } else if (!futureDriver.isCancelled() && !futureDriver.isDone()) {
                            futureDriver.cancel(true);
                            log.warn("Canceled driver creation for the current platform");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
