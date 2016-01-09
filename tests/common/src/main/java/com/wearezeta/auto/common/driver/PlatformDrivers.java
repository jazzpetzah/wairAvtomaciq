package com.wearezeta.auto.common.driver;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;

public final class PlatformDrivers {
    private static final Logger log = ZetaLogger.getLog(PlatformDrivers.class
            .getSimpleName());

    private Map<Platform, Future<? extends RemoteWebDriver>> drivers = new ConcurrentHashMap<Platform, Future<? extends RemoteWebDriver>>();

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

    public Platform getDriverPlatform(Future<? extends RemoteWebDriver> drv) {
        for (Map.Entry<Platform, Future<? extends RemoteWebDriver>> entry : this.drivers
                .entrySet()) {
            if (entry.getValue() == drv) {
                return entry.getKey();
            }
        }
        throw new NoSuchElementException(
                "Platform is unknown for the passed driver element");
    }

    private final ExecutorService pool = Executors.newFixedThreadPool(Platform
            .values().length);

    public synchronized Future<? extends RemoteWebDriver> resetDriver(
            String url, DesiredCapabilities capabilities, int maxRetryCount,
            Consumer<RemoteWebDriver> initCompletedCallback,
            Supplier<Boolean> beforeInitCallback) throws Exception {
        final Platform platformInCapabilities = Platform.getByName(capabilities
                .getCapability("platformName").toString());
        if (this.hasDriver(platformInCapabilities)) {
            this.quitDriver(platformInCapabilities);
        }
        final LazyDriverInitializer initializer = new LazyDriverInitializer(
                platformInCapabilities, url, capabilities, maxRetryCount,
                initCompletedCallback, beforeInitCallback);
        Future<RemoteWebDriver> driverBeingCreated = pool.submit(initializer);
        drivers.put(platformInCapabilities, driverBeingCreated);
        return driverBeingCreated;
    }

    public synchronized Future<? extends RemoteWebDriver> resetDriver(
            String url, DesiredCapabilities capabilities, int maxRetryCount)
            throws Exception {
        return resetDriver(url, capabilities, maxRetryCount, null, null);
    }

    public Future<? extends RemoteWebDriver> resetDriver(String url, DesiredCapabilities capabilities) throws Exception {
        return resetDriver(url, capabilities, 1, null, null);
    }

    public static void setImplicitWaitTimeout(RemoteWebDriver driver,
                                              int count, TimeUnit unit) throws Exception {
        driver.manage().timeouts().implicitlyWait(count, unit);
    }

    public static void setDefaultImplicitWaitTimeout(RemoteWebDriver driver)
            throws Exception {
        setImplicitWaitTimeout(driver, Integer.parseInt(CommonUtils
                        .getDriverTimeoutFromConfig(PlatformDrivers.class)),
                TimeUnit.SECONDS);
    }

    public Future<? extends RemoteWebDriver> getDriver(Platform platform) {
        if (!drivers.containsKey(platform)) {
            throw new RuntimeException(String.format(
                    "Please initialize %s platform driver first",
                    platform.name()));
        }
        return drivers.get(platform);
    }

    public static WebDriverWait createDefaultExplicitWait(RemoteWebDriver driver)
            throws Exception {
        return new WebDriverWait(driver, Integer.parseInt(CommonUtils
                .getDriverTimeoutFromConfig(PlatformDrivers.class)));
    }

    private static final long DRIVER_CANCELLATION_TIMEOUT = 30000; // milliseconds

    public synchronized void quitDriver(Platform platform) throws Exception {
        try {
            final Future<? extends RemoteWebDriver> futureDriver = drivers.get(platform);
            if (!futureDriver.isCancelled()) {
                try {
                    final RemoteWebDriver driver = futureDriver.get(DRIVER_CANCELLATION_TIMEOUT,
                            TimeUnit.MILLISECONDS);
                    driver.quit();
                    log.debug(String.format(
                            "Successfully quit driver instance for platform '%s'", platform.name()));
                } catch (Exception e) {
                    e.printStackTrace();
                    futureDriver.cancel(true);
                    log.warn(String.format(
                            "Canceled driver creation for platform '%s'", platform.getName()));
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
                for (Future<? extends RemoteWebDriver> futureDriver : drivers
                        .values()) {
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
