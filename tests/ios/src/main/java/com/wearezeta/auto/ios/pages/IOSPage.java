package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.PagesCollection;
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

	@FindBy(how = How.NAME, using = IOSLocators.nameKeyboardReturnButton)
	private WebElement keyboardReturnBtn;

	private static String imagesPath = "";

	public IOSPage(ZetaIOSDriver driver, WebDriverWait wait) throws Exception {
		super(driver, wait);

		setImagesPath(CommonUtils.getSimulatorImagesPathFromConfig(this
				.getClass()));
	}

	@Override
	public ZetaIOSDriver getDriver() {
		return (ZetaIOSDriver) driver;
	}

	@Override
	public void close() throws Exception {
		super.close();
	}

	public abstract IOSPage returnBySwipe(SwipeDirection direction)
			throws Exception;

	@Override
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

	@Override
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

	@Override
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

	public void smallScrollUp() {
		this.getDriver().swipe(10, 220, 10, 200, 500);
	}

	public void smallScrollDown() {
		this.getDriver().swipe(20, 300, 20, 400, 500);
	}

	public static void clearPagesCollection() throws IllegalArgumentException,
			IllegalAccessException {
		clearPagesCollection(PagesCollection.class, IOSPage.class);
	}

	public static String getImagesPath() {
		return imagesPath;
	}

	public static void setImagesPath(String imagesPath) {
		IOSPage.imagesPath = imagesPath;
	}

	public void clickPopupSelectAllButton() {
		popupSelectAll.click();
	}

	public void clickPopupCopyButton() {
		popupCopy.click();
	}

	public void clickPopupPasteButton() {
		popupPaste.click();
	}

	@Override
	public BufferedImage getElementScreenshot(WebElement element)
			throws IOException {
		BufferedImage screenshot = takeScreenshot();
		Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		int x = elementLocation.x * 2;
		int y = elementLocation.y * 2;
		int w = elementSize.width * 2;
		if (x + w > screenshot.getWidth())
			w = screenshot.getWidth() - x;
		int h = elementSize.height * 2;
		if (y + h > screenshot.getHeight())
			h = screenshot.getHeight() - y;
		return screenshot.getSubimage(x, y, w, h);
	}

	public void pasteStringToInput(WebElement element, String text) {
		IOSCommonUtils.copyToSystemClipboard(text);
		DriverUtils.iOSLongTap(this.getDriver(), element);
		clickPopupPasteButton();
	}

	public void inputStringFromKeyboard(String returnKey)
			throws InterruptedException {
		IOSKeyboard keyboard = IOSKeyboard.getInstance();
		keyboard.typeString(returnKey, this.getDriver());
	}

	public boolean isKeyboardVisible() throws Exception {
		DriverUtils.waitUntilElementDissapear(driver,
				By.className(IOSLocators.classNameKeyboard));
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.className(IOSLocators.classNameKeyboard));
	}

	public void clickKeyboardDeleteButton() {
		keyboardDeleteBtn.click();
	}

	public void clickKeyboardReturnButton() {
		keyboardReturnBtn.click();
	}

	public static Object executeScript(String script) {
		return PlatformDrivers.getInstance().getDriver(Platform.iOS)
				.executeScript(script);
	}

	public boolean isSimulator() throws Throwable {
		return CommonUtils.getIsSimulatorFromConfig(IOSPage.class);
	}

	public void cmdVscript() throws ScriptException {
		final String[] scriptArr = new String[] {
				"property thisapp: \"iOS Simulator\"",
				"tell application \"System Events\"", " tell process thisapp",
				" click menu item \"Paste\" of menu \"Edit\" of menu bar 1",
				" end tell", "end tell" };

		final String script = StringUtils.join(scriptArr, "\n");
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("AppleScriptEngine");
		if (null != engine) {
			engine.eval(script);
		}
	}

	public void hideKeyboard() {
		this.getDriver().hideKeyboard();
	}

	public void acceptAlert() throws Exception {
		DriverUtils.waitUntilAlertAppears(this.getDriver());
		try {
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			// do nothing
		}
	}

	public void minimizeApplication(int time) {
		driver.executeScript("au.backgroundApp(" + Integer.toString(time) + ")");
	}

	public void dismissAlert() throws Exception {
		DriverUtils.waitUntilAlertAppears(this.getDriver());
		try {
			driver.switchTo().alert().dismiss();
		} catch (Exception e) {
			// do nothing
		}
	}
}
