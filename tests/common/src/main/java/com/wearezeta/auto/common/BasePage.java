package com.wearezeta.auto.common;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.locators.ZetaElementLocatorFactory;
import com.wearezeta.auto.common.locators.ZetaFieldDecorator;
import com.wearezeta.auto.common.locators.ZetaSearchContext;
import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class BasePage {
	private Future<? extends RemoteWebDriver> lazyDriver;

	protected Future<? extends RemoteWebDriver> getLazyDriver() {
		return this.lazyDriver;
	}

	protected RemoteWebDriver getDriver() throws Exception {
		return this.getLazyDriver().get(ZetaDriver.INIT_TIMEOUT_MILLISECONDS,
				TimeUnit.MILLISECONDS);
	}

	private WebDriverWait wait;

	private final Semaphore waitGuard = new Semaphore(1);

	protected WebDriverWait getWait() throws Exception {
		waitGuard.acquire();
		try {
			if (this.wait == null) {
				this.wait = PlatformDrivers.createDefaultExplicitWait(this
						.getDriver());
			}
		} finally {
			waitGuard.release();
		}
		return this.wait;
	}

	private static final Logger log = ZetaLogger.getLog(BasePage.class
			.getSimpleName());
	private ZetaElementLocatorFactory zetaLocatorFactory;

	public BasePage(Future<? extends RemoteWebDriver> lazyDriver)
			throws Exception {
		this.lazyDriver = lazyDriver;
		this.zetaLocatorFactory = new ZetaElementLocatorFactory(
				new ZetaSearchContext(lazyDriver), Long.parseLong(CommonUtils
						.getDriverTimeoutFromConfig(getClass())),
				AppiumFieldDecorator.DEFAULT_TIMEUNIT);
		PageFactory.initElements(new ZetaFieldDecorator(zetaLocatorFactory),
				this);
	}

	public void close() throws Exception {
	}

	public BufferedImage takeScreenshot() throws Exception {
		return DriverUtils.takeScreenshot((ZetaDriver) this.getDriver());
	}

	public BufferedImage getElementScreenshot(WebElement element)
			throws Exception {
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
			throws Exception {
		BufferedImage screenshot = takeScreenshot();
		try {
			screenshot = screenshot.getSubimage(x, y, w, h);
		} catch (Exception e) {
			log.debug("Screenshot object is out of borders");
		}
		return screenshot;
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

	public String getPageSource() throws Exception {
		return this.getDriver().getPageSource();
	}

	public void changeZetaLocatorTimeout(long seconds) {
		zetaLocatorFactory.resetImplicitlyWaitTimeOut(seconds,
				AppiumFieldDecorator.DEFAULT_TIMEUNIT);
	}

	/**
	 * This method can only instantiate pages, which don't support direct
	 * navigation and therefore accept only only one parameter of type 'Future<?
	 * extends RemoteWebDriver>'. Main purpose of this method is to encapsulate
	 * Selenium driver inside pages so no one can potentially break abstraction
	 * layers by using the driver directly from steps ;-)
	 * 
	 * @param newPageCls
	 *            page class to be instantiated
	 * @return newly created page object
	 * @throws Exception
	 */
	public BasePage instantiatePage(Class<? extends BasePage> newPageCls)
			throws Exception {
		final Constructor<?> ctor = newPageCls.getConstructor(Future.class);
		return (BasePage) ctor.newInstance(this.getLazyDriver());
	}
}
