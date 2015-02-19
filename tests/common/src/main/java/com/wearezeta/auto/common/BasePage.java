package com.wearezeta.auto.common;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.locators.ZetaElementLocatorFactory;
import com.wearezeta.auto.common.locators.ZetaFieldDecorator;
import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class BasePage {

	protected static HashMap<Platform, RemoteWebDriver> drivers = new HashMap<Platform, RemoteWebDriver>();
	protected static HashMap<Platform, WebDriverWait> waits = new HashMap<Platform, WebDriverWait>();
	private static final Logger log = ZetaLogger.getLog(BasePage.class
			.getSimpleName());
	private static ZetaElementLocatorFactory zetaLocatorFactory;
	private Platform platform;

	protected synchronized void InitConnection(String URL,
			DesiredCapabilities capabilities) throws Exception {
		final Platform platformInCapabilities = Platform
				.getByName((String) capabilities.getCapability("platformName"));
		log.debug(String
				.format("Checking whether driver instance for platfrom '%s' already exists",
						platformInCapabilities.getName()));
		if (!drivers.containsKey(platformInCapabilities)) {
			log.debug(String.format(
					"Creating driver instance for platfrom '%s'",
					platformInCapabilities.getName()));
			switch (platformInCapabilities) {
			case Mac:
				drivers.put(platformInCapabilities, new ZetaOSXDriver(new URL(
						URL), capabilities));
				break;
			case iOS:
				drivers.put(platformInCapabilities, new ZetaIOSDriver(new URL(
						URL), capabilities));
				break;
			case Android:
				drivers.put(platformInCapabilities, new ZetaAndroidDriver(
						new URL(URL), capabilities));
				break;
			case Web:
				int tryNum = 0;
				final int maxTries = 3;
				WebDriverException savedException = null;
				do {
					// Try to reconnect WebDriver,
					// because sometimes Safari driver is non-responsive
					try {
						drivers.put(
								platformInCapabilities,
								new ZetaWebAppDriver(new URL(URL), capabilities));
						break;
					} catch (WebDriverException e) {
						if (e.getMessage().contains("Failed to connect")) {
							savedException = e;
							tryNum++;
						} else {
							throw e;
						}
					}
				} while (tryNum < maxTries);
				if (tryNum >= maxTries) {
					throw savedException;
				}
				break;
			default:
				throw new RuntimeException(
						String.format("Platform name '%s' is unknown",
								platformInCapabilities));
			}

			drivers.get(platformInCapabilities)
					.manage()
					.timeouts()
					.implicitlyWait(
							Integer.parseInt(CommonUtils
									.getDriverTimeoutFromConfig(getClass())),
							TimeUnit.SECONDS);
			waits.put(
					platformInCapabilities,
					new WebDriverWait(drivers.get(platformInCapabilities),
							Integer.parseInt(CommonUtils
									.getDriverTimeoutFromConfig(getClass()))));
		} else {
			log.debug(String.format(
					"Driver instance for platfrom '%s' already exists",
					platformInCapabilities.getName()));
		}

		this.platform = platformInCapabilities;

		zetaLocatorFactory = new ZetaElementLocatorFactory(
				drivers.get(platformInCapabilities), Long.parseLong(CommonUtils
						.getDriverTimeoutFromConfig(getClass())),
				AppiumFieldDecorator.DEFAULT_TIMEUNIT);
		FieldDecorator zetaFieldDecorator = new ZetaFieldDecorator(
				zetaLocatorFactory);
		PageFactory.initElements(zetaFieldDecorator, this);
	}

	public synchronized void close() throws Exception {
		log.debug(String.format(
				"Trying to quit driver instance for platfrom '%s'",
				platform.getName()));
		if (drivers.containsKey(platform) && drivers.get(platform) != null) {
			try {
				drivers.get(platform).quit();
				log.debug(String.format(
						"Successfully quit driver instance for platfrom '%s'",
						platform.getName()));
			} finally {
				drivers.remove(platform);
			}
		}
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot((ZetaDriver) drivers.get(platform));
	}

	public BufferedImage getElementScreenshot(WebElement element)
			throws IOException {
		BufferedImage screenshot = takeScreenshot();
		Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		int x = elementLocation.x;
		int y = elementLocation.y;
		int w = elementSize.width;
		int h = elementSize.height;
		return screenshot.getSubimage(x, y, w, h);
	}

	public BufferedImage getScreenshotByCoordinates(int x, int y, int w, int h)
			throws IOException {
		BufferedImage screenshot = takeScreenshot();
		try {
			screenshot = screenshot.getSubimage(x, y, w, h);
		} catch (Exception e) {
			log.debug("Screenshot object is out of borders");
		}
		return screenshot;
	}

	public void refreshUITree() {
		try {
			drivers.get(platform).getPageSource();
		} catch (WebDriverException ex) {

		}
	}

	public abstract BasePage swipeLeft(int time) throws IOException, Exception;;

	public abstract BasePage swipeRight(int time) throws IOException, Exception;

	public abstract BasePage swipeUp(int time) throws IOException, Exception;

	public abstract BasePage swipeDown(int time) throws IOException, Exception;

	protected static void clearPagesCollection(
			Class<? extends AbstractPagesCollection> collection,
			Class<? extends BasePage> baseClass)
			throws IllegalArgumentException, IllegalAccessException {
		for (Field f : collection.getFields()) {
			if (Modifier.isStatic(f.getModifiers())
					&& baseClass.isAssignableFrom(f.getType())) {
				f.set(null, null);
			}
		}
	}

	public String getPageSource() {
		return drivers.get(platform).getPageSource();
	}

	public static RemoteWebDriver getDriver(Platform id) {
		return drivers.get(id);
	}

	public static void changeZetaLocatorTimeout(long seconds) {
		zetaLocatorFactory.resetImplicitlyWaitTimeOut(seconds,
				AppiumFieldDecorator.DEFAULT_TIMEUNIT);
	}
}
