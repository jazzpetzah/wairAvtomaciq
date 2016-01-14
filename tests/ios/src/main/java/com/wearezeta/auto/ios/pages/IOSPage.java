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

import com.wearezeta.auto.ios.tools.IOSKeyboard;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public abstract class IOSPage extends BasePage {
    public final static long IOS_DRIVER_INIT_TIMEOUT = 1000 * 60 * 3;

    private static final int SWIPE_DELAY = 10 * 1000; // milliseconds

    protected static final String nameMainWindow = "ZClientMainWindow";
    @FindBy(name = nameMainWindow)
    protected WebElement mainWindow;

    protected static final String xpathMainWindow =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']";

    protected static final String nameEditingItemSelect = "Select";
    @FindBy(name = nameEditingItemSelect)
    private WebElement popupSelect;

    protected static final String nameEditingItemSelectAll = "Select All";
    @FindBy(xpath = nameEditingItemSelectAll)
    private WebElement popupSelectAll;

    protected static final String nameEditingItemCopy = "Copy";
    @FindBy(name = nameEditingItemCopy)
    private WebElement popupCopy;

    protected static final String nameEditingItemCut = "Cut";
    @FindBy(name = nameEditingItemCut)
    private WebElement popupCut;

    protected static final String nameEditingItemPaste = "Paste";
    @FindBy(name = nameEditingItemPaste)
    private WebElement popupPaste;

    protected static final String classNameKeyboard = "UIAKeyboard";
    @FindBy(className = classNameKeyboard)
    private WebElement keyboard;

    protected static final String nameKeyboardDeleteButton = "delete";
    @FindBy(name = nameKeyboardDeleteButton)
    private WebElement keyboardDeleteBtn;

    protected static final String nameKeyboardEnterButton = "Return";
    @FindBy(name = nameKeyboardEnterButton)
    private WebElement keyboardEnterBtn;

    protected static final String nameKeyboardReturnButton = "Send";
    @FindBy(name = nameKeyboardReturnButton)
    private WebElement keyboardReturnBtn;

    protected static final String nameHideKeyboardButton = "Hide keyboard";
    @FindBy(name = nameHideKeyboardButton)
    private WebElement keyboardHideBtn;

    protected static final String nameSpaceButton = "space";
    @FindBy(name = nameSpaceButton)
    private WebElement keyboardSpaceBtn;

    protected static final String nameDoneButton = "Done";
    @FindBy(name = nameDoneButton)
    private WebElement keyboardDoneBtn;

    protected static final String nameLockScreenMessage = "SlideToUnlock";

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

    public void swipeLeft(int time) throws Exception {
        DriverUtils.swipeLeft(this.getDriver(), mainWindow, time);
    }

    public void swipeRight(int time) throws Exception {
        DriverUtils.swipeRight(this.getDriver(), mainWindow, time);
    }

    public void swipeRight(int time, int percentX, int percentY) throws Exception {
        DriverUtils.swipeRight(this.getDriver(), mainWindow, time, percentX,
                percentY);
    }

    public void swipeUp(int time) throws Exception {
        DriverUtils.swipeElementPointToPoint(this.getDriver(), mainWindow, time,
                50, 90, 50, 10);
    }

    public void swipeDownSimulator() throws Exception {
        IOSSimulatorHelper.swipeDown();
        Thread.sleep(SWIPE_DELAY);
    }

    public void swipeDown(int time) throws Exception {
        DriverUtils.swipeElementPointToPoint(this.getDriver(), mainWindow, time,
                50, 10, 50, 90);
    }

    public void smallScrollUp() throws Exception {
        this.getDriver().swipe(10, 220, 10, 200, 500);
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

    public void inputStringFromKeyboard(String returnKey) throws Exception {
        IOSKeyboard keyboard = IOSKeyboard.getInstance();
        keyboard.typeString(returnKey, this.getDriver());
    }

    public boolean isKeyboardVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.className(classNameKeyboard), 3)
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

    public void cmdVscript(String[] scriptString) throws ScriptException {
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
                throw new IllegalArgumentException(String.format("Unknown orientation '%s'",
                        orientation));
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
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), By.name(nameLockScreenMessage), 5)) {
            IOSSimulatorHelper.swipeRight();
            Thread.sleep(SWIPE_DELAY);
        }
    }
}
