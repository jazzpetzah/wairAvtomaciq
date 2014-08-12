package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;

public abstract class IOSPage extends BasePage {

	private static final int SWIPE_DELAY = 10 * 1000; //milliseconds
	
	DesiredCapabilities capabilities = new DesiredCapabilities();
	
	private String url = "";
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
	private WebElement content;

	private static String imagesPath = "";

	public IOSPage(String URL, String path) throws MalformedURLException {
		this (URL, path, true);
	}
	
	public IOSPage(String URL, String path, boolean acceptAlerts) throws MalformedURLException {

		try {
			setImagesPath(CommonUtils.getSimulatorImagesPathFromConfig(this
					.getClass()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		url = URL;
		capabilities.setCapability("platformName", "iOS");
		
		capabilities.setCapability("app", path);
		capabilities.setCapability("deviceName", "iPhone");
		if (false == acceptAlerts) {
			initWithoutAutoAccept();
		}
		else {
			initWithAutoAccept();
		}
	}
	
	private void initWithAutoAccept() throws MalformedURLException {
		capabilities.setCapability("autoAcceptAlerts", true);
		super.InitConnection(url, capabilities);
	}
	
	private void initWithoutAutoAccept() throws MalformedURLException {
		
		super.InitConnection(url, capabilities);
	}

	@Override
	public void Close() throws IOException {
		super.Close();
	}

	public abstract IOSPage returnBySwipe(SwipeDirection direction)
			throws IOException;

	@Override
	public IOSPage swipeLeft(int time) throws IOException {
		DriverUtils.swipeLeft(driver, content, time);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	@Override
	public IOSPage swipeRight(int time) throws IOException {
		DriverUtils.swipeRight(driver, content, time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	@Override
	public IOSPage swipeUp(int time) throws IOException {
		DriverUtils.swipeUp(driver, content, time);
		return returnBySwipe(SwipeDirection.UP);
	}

	public IOSPage swipeDownSimulator() throws Exception {
		DriverUtils.iOSSimulatorSwipeDown(CommonUtils
				.getSwipeScriptPath(IOSPage.class));
		Thread.sleep(SWIPE_DELAY);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public IOSPage swipeUpSimulator() throws Exception {
		DriverUtils.iOSSimulatorSwipeUp(CommonUtils
				.getSwipeScriptPath(IOSPage.class));
		Thread.sleep(SWIPE_DELAY);
		return returnBySwipe(SwipeDirection.UP);
	}

	@Override
	public IOSPage swipeDown(int time) throws IOException {
		DriverUtils.swipeDown(driver, content, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public static void clearPagesCollection() throws IllegalArgumentException, IllegalAccessException {
		clearPagesCollection(PagesCollection.class, IOSPage.class);
	}

	public static String getImagesPath() {
		return imagesPath;
	}

	public static void setImagesPath(String imagesPath) {
		IOSPage.imagesPath = imagesPath;
	}
}
