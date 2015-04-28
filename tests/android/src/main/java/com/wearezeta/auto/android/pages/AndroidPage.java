package com.wearezeta.auto.android.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.AndroidKeyEvent;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public abstract class AndroidPage extends BasePage {
	
	private static final Logger log = ZetaLogger.getLog(CommonUtils.class
			.getSimpleName());

	@AndroidFindBy(className = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement content;

	@AndroidFindBy(className = AndroidLocators.CommonLocators.classListView)
	private WebElement container;

	@FindBy(xpath = AndroidLocators.CommonLocators.xpathImagesFrameLayout)
	private List<WebElement> frameLayouts;

	@FindBy(xpath = AndroidLocators.CommonLocators.xpathImage)
	private List<WebElement> image;

	@Override
	public ZetaAndroidDriver getDriver() {
		return (ZetaAndroidDriver) this.driver;
	}

	public AndroidPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void selectPhoto() {
		refreshUITree();
		try {
			frameLayouts.get(0).click();
			return;
		} catch (Exception ex) {

		}
		try {
			image.get(0).click();
			return;
		} catch (Exception ex) {
		}
	}

	public void hideKeyboard() {
		this.getDriver().hideKeyboard();
	}

	public AndroidPage navigateBack() throws Exception {
		driver.navigate().back();
		return null;
	}

	public void rotateLandscape() throws Exception {
		this.getDriver().rotate(ScreenOrientation.LANDSCAPE);
	}

	public void rotatePortrait() throws Exception {
		this.getDriver().rotate(ScreenOrientation.PORTRAIT);
	}

	public ScreenOrientation getOrientation() throws Exception {
		return this.getDriver().getOrientation();
	}

	public CommonAndroidPage minimizeApplication() throws Exception {
		this.getDriver().sendKeyEvent(AndroidKeyEvent.KEYCODE_HOME);
		Thread.sleep(1000);
		return new CommonAndroidPage(this.getDriver(), this.getWait());
	}
	
	public void lockScreen() throws Exception {
		this.getDriver().sendKeyEvent(AndroidKeyEvent.KEYCODE_POWER);
	}

	public void restoreApplication() {
		try {
			this.getDriver().runAppInBackground(10);
			Thread.sleep(1000);
		} catch (WebDriverException ex) {
			// do nothing, sometimes after restoring the app we have this
			// exception, Appium bug
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws Exception {
		showLogs();
		try {
			AndroidCommonUtils.killAndroidClient();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.close();
	}

	public abstract AndroidPage returnBySwipe(SwipeDirection direction)
			throws Exception;

	@Override
	public AndroidPage swipeLeft(int time) throws Exception {
		DriverUtils.swipeLeft(this.getDriver(), content, time);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	@Override
	public AndroidPage swipeRight(int time) throws Exception {
		DriverUtils.swipeRight(this.getDriver(), content, time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	@Override
	public AndroidPage swipeUp(int time) throws Exception {
		DriverUtils.swipeUp(this.getDriver(), content, time);
		return returnBySwipe(SwipeDirection.UP);
	}

	public void elementSwipeRight(WebElement el, int time) {
		Point coords = el.getLocation();
		Dimension elementSize = el.getSize();
		try {
			this.getDriver().swipe(coords.x + 30,
					coords.y + elementSize.height / 2,
					coords.x + elementSize.width - 10,
					coords.y + elementSize.height / 2, time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void elementSwipeUp(WebElement el, int time) {
		Point coords = el.getLocation();
		Dimension elementSize = el.getSize();
		try {
			this.getDriver().swipe(coords.x + elementSize.width / 2,
					coords.y + elementSize.height - 50,
					coords.x + elementSize.width / 2, coords.y, time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void elementSwipeDown(WebElement el, int time) {
		Point coords = el.getLocation();
		Dimension elementSize = el.getSize();
		try {
			this.getDriver().swipe(coords.x + elementSize.width / 2,
					coords.y + 50, coords.x + elementSize.width / 2,
					coords.y + elementSize.height - 300, time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void dialogsPagesSwipeUp(int time) {
		Point coords = content.getLocation();
		Dimension elementSize = content.getSize();
		try {
			this.getDriver().swipe(coords.x + elementSize.width / 2,
					coords.y + elementSize.height - 300,
					coords.x + elementSize.width / 2, coords.y + 50, time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void dialogsPagesSwipeDown(int time) {
		Point coords = content.getLocation();
		Dimension elementSize = content.getSize();
		try {
			this.getDriver().swipe(coords.x + elementSize.width / 2,
					coords.y + 50, coords.x + elementSize.width / 2,
					coords.y + elementSize.height - 300, time);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public AndroidPage swipeDown(int time) throws Exception {
		DriverUtils.swipeDown(this.getDriver(), content, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public AndroidPage swipeRightCoordinates(int time) throws Exception {
		DriverUtils.swipeRightCoordinates(this.getDriver(), time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public AndroidPage swipeRightCoordinates(int time, int horizontalPercent)
			throws Exception {
		DriverUtils.swipeRightCoordinates(this.getDriver(), time,
				horizontalPercent);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public AndroidPage swipeLeftCoordinates(int time) throws Exception {
		DriverUtils.swipeLeftCoordinates(this.getDriver(), time);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	public AndroidPage swipeLeftCoordinates(int time, int horizontalPercent)
			throws Exception {
		DriverUtils.swipeLeftCoordinates(this.getDriver(), time,
				horizontalPercent);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	public AndroidPage swipeUpCoordinates(int time) throws Exception {
		DriverUtils.swipeUpCoordinates(this.getDriver(), time);
		return returnBySwipe(SwipeDirection.UP);
	}

	public AndroidPage swipeUpCoordinates(int time, int verticalPercent)
			throws Exception {
		DriverUtils.swipeUpCoordinates(this.getDriver(), time, verticalPercent);
		return returnBySwipe(SwipeDirection.UP);
	}

	public AndroidPage swipeByCoordinates(int time, int widthStartPercent, int hightStartPercent, int widthEndPercent, int hightEndPercent) throws Exception {
		DriverUtils.swipeByCoordinates(this.getDriver(), time, widthStartPercent, hightStartPercent, widthEndPercent, hightEndPercent);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public AndroidPage swipeDownCoordinates(int time)
			throws Exception {
		DriverUtils.swipeDownCoordinates(this.getDriver(), time);
		return returnBySwipe(SwipeDirection.DOWN);
	}
	
	public AndroidPage swipeDownCoordinates(int time, int verticalPercent)
			throws Exception {
		DriverUtils.swipeDownCoordinates(this.getDriver(), time,
				verticalPercent);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public void tapButtonByClassNameAndIndex(WebElement element,
			String className, int index) {
		List<WebElement> buttonsList = element.findElements(By
				.className(className));
		buttonsList.get(index).click();
	}
	
	public void tapByCoordinates(int widthPercent, int hightPercent) {
		DriverUtils.genericTap(this.getDriver(), widthPercent, hightPercent);
	}
	
	public void tapByCoordinates(int time, int widthPercent, int hightPercent) {
		DriverUtils.genericTap(this.getDriver(), time, widthPercent, hightPercent);
	}
	
	public void tapOnCenterOfScreen() {
		DriverUtils.genericTap(this.getDriver());
	}

	public static void clearPagesCollection() throws IllegalArgumentException,
			IllegalAccessException {
		clearPagesCollection(PagesCollection.class, AndroidPage.class);
	}

	public boolean isVisible(WebElement element) throws NumberFormatException,
			Exception {
		boolean value = false;
		try {
			changeZetaLocatorTimeout(3);
			element.isDisplayed();
			value = true;
		} catch (NoSuchElementException ex) {
			value = false;
		} finally {
			changeZetaLocatorTimeout(Long.parseLong(CommonUtils
					.getDriverTimeoutFromConfig(getClass())));
		}
		return value;

	}

	private void showLogs() throws Exception {
		if (CommonUtils.getAndroidLogs(AndroidPage.class)) {
			List<LogEntry> logEntries = this.getDriver().manage().logs()
					.get("logcat").getAll();
			for (LogEntry entry : logEntries) {
				log.error(entry.getMessage().toString());
			}
		}
	}
}
