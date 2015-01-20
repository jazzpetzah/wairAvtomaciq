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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.ios.tools.IOSKeyboard;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public abstract class IOSPage extends BasePage {
	protected static ZetaIOSDriver driver;
	protected static WebDriverWait wait;

	private static final int SWIPE_DELAY = 10 * 1000; // milliseconds

	DesiredCapabilities capabilities = new DesiredCapabilities();

	private String url = "";

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

	public IOSPage(String URL, String path) throws IOException {
		this(URL, path, true);
	}

	public IOSPage(String URL, String path, boolean acceptAlerts)
			throws IOException {
		String bt = "staging";
		String deviceName = "";

		try {
			setImagesPath(CommonUtils.getSimulatorImagesPathFromConfig(this
					.getClass()));
			bt = CommonUtils.getBackendType(this.getClass());
			deviceName = CommonUtils.getDeviceName(this.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
		url = URL;
		capabilities.setCapability("platformName", "iOS");

		capabilities.setCapability("app", path);
		capabilities.setCapability("deviceName", deviceName);
		capabilities.setCapability("platformVersion", "8.1");
		capabilities.setCapability("processArguments",
				"--args -TutorialOverlaysEnabled 0 -ZMBackendEnvironmentType "
						+ bt);
		if (false == acceptAlerts) {
			initWithoutAutoAccept();
		} else {
			initWithAutoAccept();
		}
	}

	private void initWithAutoAccept() throws IOException {
		capabilities.setCapability("autoAcceptAlerts", true);
		super.InitConnection(url, capabilities);

		storeDriverAndWait();
	}

	private void initWithoutAutoAccept() throws IOException {

		super.InitConnection(url, capabilities);

		storeDriverAndWait();
	}

	private void storeDriverAndWait() {
		driver = (ZetaIOSDriver) drivers.get(CommonUtils.PLATFORM_NAME_IOS);
		wait = (WebDriverWait) waits.get(CommonUtils.PLATFORM_NAME_IOS);
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

	public void smallScrollUp() {
		driver.swipe(10, 220, 10, 200, 500);
	}

	public void smallScrollDown() {
		driver.swipe(20, 300, 20, 400, 500);
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
		DriverUtils.iOSLongTap(driver, element);
		clickPopupPasteButton();
	}

	public void inputStringFromKeyboard(String returnKey)
			throws InterruptedException {
		IOSKeyboard keyboard = IOSKeyboard.getInstance();
		keyboard.typeString(returnKey, driver);
	}

	public boolean isKeyboardVisible() {
		DriverUtils.waitUntilElementDissapear(driver,
				By.className(IOSLocators.classNameKeyboard));
		return DriverUtils.isElementDisplayed(keyboard);
	}

	public void clickKeyboardDeleteButton() {
		keyboardDeleteBtn.click();
	}

	public void clickKeyboardReturnButton() {
		keyboardReturnBtn.click();
	}

	public static Object executeScript(String script) {
		return driver.executeScript(script);
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
		driver.hideKeyboard();
	}

	public void acceptAlert() {
		DriverUtils.waitUntilAlertAppears(driver);
		try {
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			// do nothing
		}
	}

	public void dismissAlert() {
		DriverUtils.waitUntilAlertAppears(driver);
		try {
			driver.switchTo().alert().dismiss();
		} catch (Exception e) {
			// do nothing
		}
	}
}
