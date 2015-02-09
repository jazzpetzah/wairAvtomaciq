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

	protected static HashMap<String, RemoteWebDriver> drivers = new HashMap<String, RemoteWebDriver>();
	protected static HashMap<String, WebDriverWait> waits = new HashMap<String, WebDriverWait>();
	private static final Logger log = ZetaLogger.getLog(BasePage.class
			.getSimpleName());

	private String pagePlatform;

	protected synchronized void InitConnection(String URL,
			DesiredCapabilities capabilities) throws Exception {

		final String platform = (String) capabilities
				.getCapability("platformName");
		if (null == drivers || drivers.isEmpty()
				|| drivers.get(platform) == null) {
			if (platform.equals(CommonUtils.PLATFORM_NAME_ANDROID)) {
				drivers.put(platform, new ZetaAndroidDriver(new URL(URL),
						capabilities));
			} else if (platform.equals(CommonUtils.PLATFORM_NAME_IOS)) {
				drivers.put(platform, new ZetaIOSDriver(new URL(URL),
						capabilities));
			} else if (platform.equals(CommonUtils.PLATFORM_NAME_OSX)) {
				drivers.put(platform, new ZetaOSXDriver(new URL(URL),
						capabilities));
			} else if (platform.equals(CommonUtils.PLATFORM_NAME_WEB)) {
				capabilities.setCapability("platformName", "ANY");
				drivers.put(platform, new ZetaWebAppDriver(new URL(URL),
						capabilities));
			} else {
				throw new RuntimeException(String.format(
						"Platform name '%s' is unknown", platform));
			}
			try {
				drivers.get(platform)
						.manage()
						.timeouts()
						.implicitlyWait(
								Integer.parseInt(CommonUtils
										.getDriverTimeoutFromConfig(getClass())),
								TimeUnit.SECONDS);

				waits.put(
						platform,
						new WebDriverWait(
								drivers.get(platform),
								Integer.parseInt(CommonUtils
										.getDriverTimeoutFromConfig(getClass()))));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		pagePlatform = platform;

		ZetaElementLocatorFactory zetaLocatorFactory = new ZetaElementLocatorFactory(
				drivers.get(platform), Long.parseLong(CommonUtils
						.getDriverTimeoutFromConfig(getClass())),
				AppiumFieldDecorator.DEFAULT_TIMEUNIT);
		FieldDecorator zetaFieldDecorator = new ZetaFieldDecorator(
				zetaLocatorFactory);
		PageFactory.initElements(zetaFieldDecorator, this);
	}

	public synchronized void Close() throws Exception {
		if (drivers.get(pagePlatform) != null) {
			drivers.get(pagePlatform).quit();
			drivers.put(pagePlatform, null);
		}
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot((ZetaDriver) drivers
				.get(pagePlatform));
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
			drivers.get(pagePlatform).getPageSource();
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
		return drivers.get(pagePlatform).getPageSource();
	}

	public static RemoteWebDriver getDriver(String id) {
		return drivers.get(id);
	}
}
