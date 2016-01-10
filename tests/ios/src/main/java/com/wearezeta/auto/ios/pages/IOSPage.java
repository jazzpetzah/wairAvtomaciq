package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.ios.tools.IOSKeyboard;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public abstract class IOSPage extends BasePage {
	public final static long IOS_DRIVER_INIT_TIMEOUT = 1000 * 60 * 3;

	private static final int SWIPE_DELAY = 10 * 1000; // milliseconds

	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
	protected WebElement content;

	@FindBy(how = How.NAME, using = IOSLocators.nameEditingItemSelect)
	private WebElement popupSelect;

	@FindBy(how = How.NAME, using = IOSLocators.nameEditingItemSelectAll)
	private WebElement popupSelectAll;

	@FindBy(how = How.NAME, using = IOSLocators.nameEditingItemCopy)
	private WebElement popupCopy;

	@FindBy(how = How.NAME, using = IOSLocators.nameEditingItemCut)
	private WebElement popupCut;

	@FindBy(how = How.NAME, using = IOSLocators.nameEditingItemPaste)
	private WebElement popupPaste;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameKeyboard)
	private WebElement keyboard;

	@FindBy(how = How.NAME, using = IOSLocators.nameKeyboardDeleteButton)
	private WebElement keyboardDeleteBtn;

	@FindBy(how = How.NAME, using = IOSLocators.nameKeyboardEnterButton)
	private WebElement keyboardEnterBtn;

	@FindBy(how = How.NAME, using = IOSLocators.nameKeyboardReturnButton)
	private WebElement keyboardReturnBtn;

	@FindBy(how = How.NAME, using = IOSLocators.KeyboardButtons.nameHideKeyboardButton)
	private WebElement keyboardHideBtn;

	@FindBy(how = How.NAME, using = IOSLocators.KeyboardButtons.nameSpaceButton)
	private WebElement keyboardSpaceBtn;

	@FindBy(how = How.NAME, using = IOSLocators.KeyboardButtons.nameDoneButton)
	private WebElement keyboardDoneBtn;

	private static String imagesPath = "";

    protected long getDriverInitializationTimeout() {
        return IOS_DRIVER_INIT_TIMEOUT;
    }

	public IOSPage(Future<ZetaIOSDriver> driver) throws Exception {
		super(driver);

		setImagesPath(CommonUtils.getSimulatorImagesPathFromConfig(this
				.getClass()));
	}

	@Override
	protected ZetaIOSDriver getDriver() throws Exception {
		return (ZetaIOSDriver) super.getDriver();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Future<ZetaIOSDriver> getLazyDriver() {
		return (Future<ZetaIOSDriver>) super.getLazyDriver();
	}

	@Override
	public void close() throws Exception {
		super.close();
	}

	public abstract IOSPage returnBySwipe(SwipeDirection direction)
			throws Exception;

	public IOSPage swipeLeft(int time) throws Exception {
		DriverUtils.swipeLeft(this.getDriver(), content, time);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	public IOSPage swipeLeft(int time, int percentX, int percentY)
			throws Exception {
		DriverUtils.swipeLeft(this.getDriver(), content, time, percentX,
				percentY);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	public IOSPage swipeRight(int time) throws Exception {
		DriverUtils.swipeRight(this.getDriver(), content, time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public IOSPage swipeRight(int time, int percentX, int percentY)
			throws Exception {
		DriverUtils.swipeRight(this.getDriver(), content, time, percentX,
				percentY);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public IOSPage swipeUp(int time) throws Exception {
		DriverUtils.swipeUp(this.getDriver(), content, time);
		return returnBySwipe(SwipeDirection.UP);
	}

	public IOSPage swipeUp(int time, int percentX, int percentY)
			throws Exception {
		DriverUtils
				.swipeUp(this.getDriver(), content, time, percentX, percentY);
		return returnBySwipe(SwipeDirection.UP);
	}

	public IOSPage swipeDownSimulator() throws Exception {
		IOSSimulatorHelper.swipeDown();
		Thread.sleep(SWIPE_DELAY);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public IOSPage swipeUpSimulator() throws Exception {
		IOSSimulatorHelper.swipeUp();
		Thread.sleep(SWIPE_DELAY);
		return returnBySwipe(SwipeDirection.UP);
	}

	public IOSPage swipeDown(int time) throws Exception {
		DriverUtils.swipeDown(this.getDriver(), content, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public IOSPage swipeDown(int time, int percentX, int percentY)
			throws Exception {
		DriverUtils.swipeDown(this.getDriver(), content, time, percentX,
				percentY);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public void smallScrollUp() throws Exception {
		this.getDriver().swipe(10, 220, 10, 200, 500);
	}

	public void smallScrollDown() throws Exception {
		this.getDriver().swipe(20, 300, 20, 400, 500);
	}

	public static String getImagesPath() {
		return imagesPath;
	}

	public static void setImagesPath(String imagesPath) {
		IOSPage.imagesPath = imagesPath;
	}

	public void clickPopupSelectAllButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), popupSelectAll);
		popupSelectAll.click();
	}

	public void clickPopupCopyButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), popupCopy);
		popupCopy.click();
	}

	public void clickPopupPasteButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), popupPaste);
		popupPaste.click();
	}

	@Override
	public Optional<BufferedImage> getElementScreenshot(WebElement element)
			throws Exception {
		Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		int x = elementLocation.x * 2;
		int y = elementLocation.y * 2;
		int w = elementSize.width * 2;
		Optional<BufferedImage> screenshot = takeScreenshot();
		if (screenshot.isPresent()) {
			if (x + w > screenshot.get().getWidth())
				w = screenshot.get().getWidth() - x;
			int h = elementSize.height * 2;
			if (y + h > screenshot.get().getHeight())
				h = screenshot.get().getHeight() - y;
			return Optional.of(screenshot.get().getSubimage(x, y, w, h));
		} else {
			return Optional.empty();
		}
	}

	public void pasteStringToInput(WebElement element, String text)
			throws Exception {
		IOSCommonUtils.copyToSystemClipboard(text);
		DriverUtils.longTap(this.getDriver(), element);
		clickPopupPasteButton();
	}

	public void inputStringFromKeyboard(String returnKey) throws Exception {
		IOSKeyboard keyboard = IOSKeyboard.getInstance();
		keyboard.typeString(returnKey, this.getDriver());
	}

	public boolean isKeyboardVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.className(IOSLocators.classNameKeyboard), 3)
				&& DriverUtils.waitUntilElementClickable(getDriver(), keyboard,
						3);
	}

	public void clickKeyboardDeleteButton() {
		keyboardDeleteBtn.click();
	}

	public void clickKeyboardEnterButton() {
		keyboardEnterBtn.click();
	}

	public void clickKeyboardReturnButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), keyboardReturnBtn);
		keyboardReturnBtn.click();
	}

	public void clickHideKeyboarButton() {
		keyboardHideBtn.click();
	}

	public void clickSpaceKeyboardButton() {
		keyboardSpaceBtn.click();
	}

	public void clickDoneKeyboardButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), keyboardDoneBtn);
		keyboardDoneBtn.click();
	}

	public static Object executeScript(String script) throws Exception {
		return PlatformDrivers.getInstance().getDriver(Platform.iOS).get()
				.executeScript(script);
	}

	public boolean isSimulator() throws Exception {
		return CommonUtils.getIsSimulatorFromConfig(IOSPage.class);
	}

	public void cmdVscript(String[] scriptString) throws ScriptException {
		// final String[] scriptArr = new String[] {
		// "property thisapp: \"Simulator\"",
		// "tell application \"System Events\"", " tell process thisapp",
		// " click menu item \"Paste\" of menu \"Edit\" of menu bar 1",
		// " end tell", "end tell" };

		final String script = StringUtils.join(scriptString, "\n");
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("AppleScriptEngine");
		if (null != engine) {
			engine.eval(script);
		}
	}

	public void hideKeyboard() throws Exception {
		this.getDriver().hideKeyboard();
	}

	public void acceptAlert() throws Exception {
		DriverUtils.waitUntilAlertAppears(this.getDriver());
		try {
			this.getDriver().switchTo().alert().accept();
		} catch (Exception e) {
			// do nothing
		}
	}

	public void minimizeApplication(int time) throws Exception {
		this.getDriver().executeScript(
				"au.backgroundApp(" + Integer.toString(time) + ")");
	}

	public void dismissAlert() throws Exception {
		DriverUtils.waitUntilAlertAppears(this.getDriver());
		try {
			this.getDriver().switchTo().alert().dismiss();
		} catch (Exception e) {
			// do nothing
		}
	}

	public void dismissAllAlerts() throws Exception {
		int count = 0;
		final int NUMBER_OF_RETRIES = 3;
		final int ALERT_WAITING_TIMEOUT = 3;
		do {
			try {
				this.getDriver().switchTo().alert().dismiss();
			} catch (Exception e) {
				// do nothing
			}
		} while (DriverUtils.waitUntilAlertAppears(this.getDriver(),
				ALERT_WAITING_TIMEOUT) && count++ < NUMBER_OF_RETRIES);
	}

	public void rotateScreen(ScreenOrientation orientation) throws Exception {
		switch (orientation) {
		case LANDSCAPE:
			rotateLandscape();
			break;
		case PORTRAIT:
			rotatePortrait();
			break;
		default:
			break;
		}

	}

	public void rotateDeviceToRefreshElementsTree() throws Exception {
		if (getOrientation() == ScreenOrientation.PORTRAIT) {
			rotateLandscape();
			rotatePortrait();
		} else {
			rotatePortrait();
			rotateLandscape();
		}
	}

	private void rotateLandscape() throws Exception {
		this.getDriver().rotate(ScreenOrientation.LANDSCAPE);
	}

	private void rotatePortrait() throws Exception {
		this.getDriver().rotate(ScreenOrientation.PORTRAIT);
	}

	public ScreenOrientation getOrientation() throws Exception {
		return this.getDriver().getOrientation();
	}

	public void tapOnCenterOfScreen() throws Exception {
		DriverUtils.genericTap(this.getDriver());
	}

	public void tapOnTopLeftScreen() throws Exception {
		DriverUtils.genericTap(this.getDriver(), 1, 1);
	}

	public void lockScreen(int seconds) throws Exception {
		this.getDriver().lockScreen(seconds);
		// check if the screen is unlocked
		if (!DriverUtils
				.waitUntilLocatorDissapears(
						getDriver(),
						By.name(IOSLocators.CommonIOSLocators.nameLockScreenMessage),
						5)) {

			IOSSimulatorHelper.swipeRight();
			Thread.sleep(SWIPE_DELAY);
		}
	}
}
