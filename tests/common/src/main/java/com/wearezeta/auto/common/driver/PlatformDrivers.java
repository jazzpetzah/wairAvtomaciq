package com.wearezeta.auto.common.driver;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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

	private HashMap<Platform, Future<RemoteWebDriver>> drivers = new HashMap<Platform, Future<RemoteWebDriver>>();

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

	private final ExecutorService pool = Executors.newFixedThreadPool(Platform
			.values().length);

	public synchronized Future<RemoteWebDriver> resetDriver(String url,
			DesiredCapabilities capabilities) throws Exception {
		final Platform platformInCapabilities = Platform
				.getByName((String) capabilities.getCapability("platformName"));
		if (this.hasDriver(platformInCapabilities)) {
			this.quitDriver(platformInCapabilities);
		}
		final LazyDriverInitializer initializer = new LazyDriverInitializer(
				platformInCapabilities, url, capabilities);
		Future<RemoteWebDriver> driverBeingCreated = pool.submit(initializer);
		drivers.put(platformInCapabilities, driverBeingCreated);
		return driverBeingCreated;
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

	public Future<RemoteWebDriver> getDriver(Platform platform) {
		if (!drivers.containsKey(platform)) {
			throw new RuntimeException(String.format(
					"Please initialize %s platform driver first",
					platform.name()));
		}
		return drivers.get(platform);
	}

	public Collection<Future<RemoteWebDriver>> getRegisteredDrivers() {
		return drivers.values();
	}

	public static WebDriverWait createDefaultExplicitWait(RemoteWebDriver driver)
			throws Exception {
		return new WebDriverWait(driver, Integer.parseInt(CommonUtils
				.getDriverTimeoutFromConfig(PlatformDrivers.class)));
	}

	public synchronized void quitDriver(Platform platform) throws Exception {
		try {
			drivers.get(platform).get().quit();
			log.debug(String.format(
					"Successfully quit driver instance for platfrom '%s'",
					platform.name()));
		} finally {
			drivers.remove(platform);
		}
	}

	{
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (Entry<Platform, Future<RemoteWebDriver>> entry : drivers
						.entrySet()) {
					try {
						entry.getValue().get().quit();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
