package com.wearezeta.auto.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
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
import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class BasePage {

	protected static HashMap<String, ZetaDriver> drivers = new HashMap<String, ZetaDriver>();
	protected static HashMap<String, WebDriverWait> waits = new HashMap<String, WebDriverWait>();
	private static final Logger log = ZetaLogger.getLog(BasePage.class.getSimpleName());
	
	private String pagePlatform;
	
	protected synchronized void InitConnection(String URL, DesiredCapabilities capabilities)
			throws MalformedURLException {

		String platform = (String) capabilities.getCapability("platformName");
		if (null == drivers || drivers.isEmpty() || drivers.get(platform) == null || drivers.get(platform).isSessionLost()) {
			drivers.put(platform, new ZetaDriver(new URL(URL), capabilities));
			try {
				drivers.get(platform).manage()
						.timeouts()
						.implicitlyWait(
								Integer.parseInt(CommonUtils
										.getDriverTimeoutFromConfig(getClass())),
								TimeUnit.SECONDS);

				waits.put(platform, new WebDriverWait(drivers.get(platform), Integer.parseInt(CommonUtils
						.getDriverTimeoutFromConfig(getClass()))));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		pagePlatform = platform;
				
		ElementLocatorFactory zetaLocatorFactory = new ZetaElementLocatorFactory(drivers.get(platform));
		FieldDecorator zetaFieldDecorator = new ZetaFieldDecorator(zetaLocatorFactory);
		PageFactory.initElements(zetaFieldDecorator, this);
	}

	public synchronized void Close() throws IOException {
		if (drivers.get(pagePlatform) != null) {
			drivers.get(pagePlatform).quit();
			drivers.put(pagePlatform, null);
		}
	}

	public BufferedImage takeScreenshot() throws IOException {
		return DriverUtils.takeScreenshot(drivers.get(pagePlatform));
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
	
	public BufferedImage getScreenshotByCoordinates(int x, int y, int w, int h) throws IOException{
		BufferedImage screenshot = takeScreenshot();
		try {
			screenshot = screenshot.getSubimage(x, y, w, h);
		}
		catch (Exception e) {
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

	public String getPageSource() {
		return drivers.get(pagePlatform).getPageSource();
	}
	
	public static ZetaDriver getDriver(String id) {
		return drivers.get(id);
	}
}
