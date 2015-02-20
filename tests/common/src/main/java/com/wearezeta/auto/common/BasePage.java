package com.wearezeta.auto.common;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.locators.ZetaElementLocatorFactory;
import com.wearezeta.auto.common.locators.ZetaFieldDecorator;
import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class BasePage {
	protected RemoteWebDriver driver;

	public RemoteWebDriver getDriver() {
		return this.driver;
	}

	private WebDriverWait wait;

	public WebDriverWait getWait() {
		return this.wait;
	}

	private static final Logger log = ZetaLogger.getLog(BasePage.class
			.getSimpleName());
	private static ZetaElementLocatorFactory zetaLocatorFactory;

	public BasePage(RemoteWebDriver driver, WebDriverWait wait)
			throws Exception {
		this.driver = driver;
		this.wait = wait;

		zetaLocatorFactory = new ZetaElementLocatorFactory(this.driver,
				Long.parseLong(CommonUtils
						.getDriverTimeoutFromConfig(getClass())),
				AppiumFieldDecorator.DEFAULT_TIMEUNIT);
		FieldDecorator zetaFieldDecorator = new ZetaFieldDecorator(
				zetaLocatorFactory);
		PageFactory.initElements(zetaFieldDecorator, this);
	}

	public void close() throws Exception {
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot((ZetaDriver) this.getDriver());
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
			this.getDriver().getPageSource();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
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
		return this.getDriver().getPageSource();
	}

	public static void changeZetaLocatorTimeout(long seconds) {
		zetaLocatorFactory.resetImplicitlyWaitTimeOut(seconds,
				AppiumFieldDecorator.DEFAULT_TIMEUNIT);
	}
}
