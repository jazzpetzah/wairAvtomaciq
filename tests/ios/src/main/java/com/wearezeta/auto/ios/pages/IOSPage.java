package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.tools.IOSKeyboard;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaDriver;

public abstract class IOSPage extends BasePage {
	protected static ZetaDriver driver;
	protected static WebDriverWait wait;
	
	private static final int SWIPE_DELAY = 7 * 1000; //milliseconds
	
	DesiredCapabilities capabilities = new DesiredCapabilities();
	
	private String url = "";
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
	private WebElement content;
	
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
		capabilities.setCapability("deviceName", "iPhone 6");
		capabilities.setCapability("platformVersion", "8.0");
		capabilities.setCapability("newCommandTimeout", "300");
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

        storeDriverAndWait();
	}
	
	private void initWithoutAutoAccept() throws MalformedURLException {
		
		super.InitConnection(url, capabilities);
		
        storeDriverAndWait();
	}

	private void storeDriverAndWait() {
        driver = drivers.get(CommonUtils.PLATFORM_NAME_IOS);
        wait = waits.get(CommonUtils.PLATFORM_NAME_IOS);
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
		driver.swipe(10, 200, 10, 220, 500);
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
	
	public void clickPopupSelectAllButton(){
		popupSelectAll.click();
	}
	
	public void clickPopupCopyButton(){
		popupCopy.click();
	}
	
	public void clickPopupPasteButton(){
		popupPaste.click();
	}
	
	@Override
	public BufferedImage getElementScreenshot(WebElement element)
			throws IOException {
		BufferedImage screenshot = takeScreenshot();
		Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		int x = elementLocation.x*2;
		int y = elementLocation.y*2;
		int w = elementSize.width*2; if (x+w>screenshot.getWidth()) w = screenshot.getWidth()-x;
		int h = elementSize.height*2; if (y+h>screenshot.getHeight()) h = screenshot.getHeight()-y;
		return screenshot.getSubimage(x, y, w, h);
	}
	
	public void pasteStringToInput(WebElement element, String text){
		DriverUtils.iOSLongTap(driver, element);
		CommonUtils.putStringToClipboard(text);
		clickPopupPasteButton();
	}
	
	public void inputStringFromKeyboard(String returnKey) throws InterruptedException{
		IOSKeyboard keyboard = IOSKeyboard.getInstance();
		keyboard.typeString(returnKey, driver);
	}
	
	public boolean isKeyboardVisible(){
		return DriverUtils.isElementDisplayed(keyboard);
	}
	
	public void clickKeyboardDeleteButton(){
		keyboardDeleteBtn.click();
	}

}
