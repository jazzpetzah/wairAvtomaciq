package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.locators.ZetaElementLocatorFactory;
import com.wearezeta.auto.common.locators.ZetaFieldDecorator;

public abstract class BasePage {

	protected static ZetaDriver driver = null;
	protected static WebDriverWait wait;

	protected synchronized void InitConnection(String URL, DesiredCapabilities capabilities)
			throws MalformedURLException {
		if (null == driver) {
			driver = new ZetaDriver(new URL(URL), capabilities);
			try {
				driver.manage()
						.timeouts()
						.implicitlyWait(
								Integer.parseInt(CommonUtils
										.getDriverTimeoutFromConfig(getClass())),
								TimeUnit.SECONDS);

				wait = new WebDriverWait(driver, Integer.parseInt(CommonUtils
						.getDriverTimeoutFromConfig(getClass())));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ElementLocatorFactory zetaLocatorFactory = new ZetaElementLocatorFactory(driver);
		FieldDecorator zetaFieldDecorator = new ZetaFieldDecorator(zetaLocatorFactory);
		PageFactory.initElements(zetaFieldDecorator, this);
	}

	public synchronized void Close() throws IOException {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot(driver);
	}

	public BufferedImage getElementScreenshot(WebElement element)
			throws IOException {
		BufferedImage screenshot = takeScreenshot();
		Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		return screenshot.getSubimage(elementLocation.x, elementLocation.y,
				elementSize.width, elementSize.height);
	}

	public void refreshUITree() {
		driver.getPageSource();
	}

	public abstract BasePage swipeLeft(int time) throws IOException, Exception;;

	public abstract BasePage swipeRight(int time) throws IOException, Exception;

	public abstract BasePage swipeUp(int time) throws IOException, Exception;

	public abstract BasePage swipeDown(int time) throws IOException, Exception;

	protected static void clearPagesCollection(Class<? extends AbstractPagesCollection> collection,
			Class<? extends BasePage> baseClass)
			throws IllegalArgumentException, IllegalAccessException {
		for (Field f : collection.getFields()) {
			if (Modifier.isStatic(f.getModifiers())
					&& baseClass.isAssignableFrom(f.getType())) {
				f.set(null, null);
			}
		}
	}

}
