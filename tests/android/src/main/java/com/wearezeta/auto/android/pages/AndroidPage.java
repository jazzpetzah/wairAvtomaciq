package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import android.view.KeyEvent;

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

	@FindBy(id = AndroidLocators.CommonLocators.idPager)
	private WebElement content;

	@FindBy(className = AndroidLocators.CommonLocators.classListView)
	private WebElement container;

	@Override
	protected ZetaAndroidDriver getDriver() throws Exception {
		return (ZetaAndroidDriver) super.getDriver();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Future<ZetaAndroidDriver> getLazyDriver() {
		return (Future<ZetaAndroidDriver>) super.getLazyDriver();
	}

	public AndroidPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void selectFirstGalleryPhoto() throws Exception {
		final Dimension screenDimension = getDriver().manage().window()
				.getSize();
		// Selendroid workaround
		// Cannot handle external apps properly :-(
		AndroidCommonUtils.genericScreenTap(screenDimension.width / 4,
				screenDimension.height / 4);
	}

	public void hideKeyboard() throws Exception {
		try {
			this.getDriver().hideKeyboard();
		} catch (WebDriverException e) {
			log.debug("The keyboard seems to be already hidden.");
		}
	}

	protected void pressEnter() throws Exception {
		this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_ENTER);
	}

	public AndroidPage navigateBack() throws Exception {
		this.getDriver().navigate().back();
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
		return new CommonAndroidPage(this.getLazyDriver());
	}

	public void lockScreen() throws Exception {
		this.getDriver().sendKeyEvent(AndroidKeyEvent.KEYCODE_POWER);
	}

	public void restoreApplication() throws Exception {
		try {
			this.getDriver().runAppInBackground(10);
			Thread.sleep(1000);
		} catch (WebDriverException ex) {
			// do nothing, sometimes after restoring the app we have this
			// exception, Appium bug
		}
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

	public void dialogsPagesSwipeUp(int durationMilliseconds) throws Exception {
		Point coords = content.getLocation();
		Dimension elementSize = content.getSize();
		this.getDriver().swipe(coords.x + elementSize.width / 2,
				coords.y + elementSize.height - 300,
				coords.x + elementSize.width / 2, coords.y + 50,
				durationMilliseconds);
	}

	public void dialogsPagesSwipeDown(int durationMilliseconds)
			throws Exception {
		Point coords = content.getLocation();
		Dimension elementSize = content.getSize();
		this.getDriver().swipe(coords.x + elementSize.width / 2, coords.y + 50,
				coords.x + elementSize.width / 2,
				coords.y + elementSize.height - 300, durationMilliseconds);
	}

	@Override
	public AndroidPage swipeDown(int durationMilliseconds) throws Exception {
		DriverUtils.swipeDown(this.getDriver(), content, durationMilliseconds);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public AndroidPage swipeRightCoordinates(int durationMilliseconds)
			throws Exception {
		DriverUtils.swipeRightCoordinates(this.getDriver(),
				durationMilliseconds);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public AndroidPage swipeRightCoordinates(int durationMilliseconds,
			int horizontalPercent) throws Exception {
		DriverUtils.swipeRightCoordinates(this.getDriver(),
				durationMilliseconds, horizontalPercent);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public AndroidPage swipeLeftCoordinates(int durationMilliseconds)
			throws Exception {
		DriverUtils
				.swipeLeftCoordinates(this.getDriver(), durationMilliseconds);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	public AndroidPage swipeLeftCoordinates(int durationMilliseconds,
			int horizontalPercent) throws Exception {
		DriverUtils.swipeLeftCoordinates(this.getDriver(),
				durationMilliseconds, horizontalPercent);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	public AndroidPage swipeUpCoordinates(int durationMilliseconds)
			throws Exception {
		DriverUtils.swipeUpCoordinates(this.getDriver(), durationMilliseconds);
		return returnBySwipe(SwipeDirection.UP);
	}

	public AndroidPage swipeUpCoordinates(int durationMilliseconds,
			int verticalPercent) throws Exception {
		DriverUtils.swipeUpCoordinates(this.getDriver(), durationMilliseconds,
				verticalPercent);
		return returnBySwipe(SwipeDirection.UP);
	}

	public AndroidPage swipeByCoordinates(int durationMilliseconds,
			int widthStartPercent, int hightStartPercent, int widthEndPercent,
			int hightEndPercent) throws Exception {
		DriverUtils.swipeByCoordinates(this.getDriver(), durationMilliseconds,
				widthStartPercent, hightStartPercent, widthEndPercent,
				hightEndPercent);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public AndroidPage swipeDownCoordinates(int durationMilliseconds)
			throws Exception {
		DriverUtils
				.swipeDownCoordinates(this.getDriver(), durationMilliseconds);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public AndroidPage swipeDownCoordinates(int durationMilliseconds,
			int verticalPercent) throws Exception {
		DriverUtils.swipeDownCoordinates(this.getDriver(),
				durationMilliseconds, verticalPercent);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public void tapButtonByClassNameAndIndex(WebElement element,
			String className, int index) {
		List<WebElement> buttonsList = element.findElements(By
				.className(className));
		buttonsList.get(index).click();
	}

	public void tapByCoordinates(int widthPercent, int hightPercent)
			throws Exception {
		DriverUtils.genericTap(this.getDriver(), widthPercent, hightPercent);
	}

	public void tapByCoordinates(int time, int widthPercent, int hightPercent)
			throws Exception {
		DriverUtils.genericTap(this.getDriver(), time, widthPercent,
				hightPercent);
	}

	public void tapOnCenterOfScreen() throws Exception {
		DriverUtils.genericTap(this.getDriver());
	}

	public static void clearPagesCollection() throws IllegalArgumentException,
			IllegalAccessException {
		clearPagesCollection(PagesCollection.class, AndroidPage.class);
	}
}
