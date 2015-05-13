package com.wearezeta.auto.common.driver;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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
			Consumer<RemoteWebDriver> initCompletedCallback) throws Exception {
		final Platform platformInCapabilities = Platform
				.getByName((String) capabilities.getCapability("platformName"));
		if (this.hasDriver(platformInCapabilities)) {
			this.quitDriver(platformInCapabilities);
		}
		final LazyDriverInitializer initializer = new LazyDriverInitializer(
				platformInCapabilities, url, capabilities, maxRetryCount,
				initCompletedCallback);
		Future<RemoteWebDriver> driverBeingCreated = pool.submit(initializer);
		drivers.put(platformInCapabilities, driverBeingCreated);
		return driverBeingCreated;
	}

	public synchronized Future<? extends RemoteWebDriver> resetDriver(
			String url, DesiredCapabilities capabilities, int maxRetryCount)
			throws Exception {
		return resetDriver(url, capabilities, maxRetryCount, null);
	}

	public Future<? extends RemoteWebDriver> resetDriver(String url,
			DesiredCapabilities capabilities) throws Exception {
		return resetDriver(url, capabilities, 1, null);
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

	public Collection<Future<? extends RemoteWebDriver>> getRegisteredDrivers() {
		return drivers.values();
	}

	public static WebDriverWait createDefaultExplicitWait(RemoteWebDriver driver)
			throws Exception {
		return new WebDriverWait(driver, Integer.parseInt(CommonUtils
				.getDriverTimeoutFromConfig(PlatformDrivers.class)));
	}

	public synchronized void quitDriver(Platform platform) throws Exception {
		try {
			drivers.get(platform)
					.get(ZetaDriver.INIT_TIMEOUT_MILLISECONDS,
							TimeUnit.MILLISECONDS).quit();
			log.debug(String.format(
					"Successfully quit driver instance for platfrom '%s'",
					platform.name()));
		} finally {
			drivers.remove(platform);
		}
	}

	public void pingDrivers() throws Exception {
		for (Future<? extends RemoteWebDriver> driver : drivers.values()) {
			driver.get(ZetaDriver.INIT_TIMEOUT_MILLISECONDS,
					TimeUnit.MILLISECONDS).getCurrentUrl();
		}
	}

	{
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (Future<? extends RemoteWebDriver> driver : getRegisteredDrivers()) {
					try {
						driver.get(ZetaDriver.INIT_TIMEOUT_MILLISECONDS,
								TimeUnit.MILLISECONDS).quit();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
