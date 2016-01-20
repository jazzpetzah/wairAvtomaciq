package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

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

    protected static final String nameStrMainWindow = "ZClientMainWindow";
    protected static final By nameMainWindow = By.name(nameStrMainWindow);

    protected static final String xpathStrMainWindow =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']";

    protected static final By nameEditingItemSelectAll = By.name("Select All");

    protected static final By nameEditingItemCopy = By.name("Copy");

    protected static final By nameEditingItemPaste = By.name("Paste");

    protected static final By classNameKeyboard = By.className("UIAKeyboard");

    protected static final By nameKeyboardDeleteButton = By.name("delete");

    protected static final By nameKeyboardReturnButton = By.name("Return");

    protected static final By nameKeyboardSendButton = By.name("Send");

    protected static final By nameHideKeyboardButton = By.name("Hide keyboard");

    protected static final By nameSpaceButton = By.name("space");

    protected static final By nameDoneButton = By.name("Done");

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
        DriverUtils.swipeLeft(this.getDriver(), getElement(nameMainWindow), time);
    }

    public void swipeRight(int time) throws Exception {
        DriverUtils.swipeRight(this.getDriver(), getElement(nameMainWindow), time);
    }

    public void swipeRight(int time, int percentX, int percentY) throws Exception {
        DriverUtils.swipeRight(this.getDriver(), getElement(nameMainWindow), time, percentX,
                percentY);
    }

    public void swipeUp(int time) throws Exception {
        DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(nameMainWindow), time,
                50, 90, 50, 10);
    }

    public void swipeDownSimulator() throws Exception {
        IOSSimulatorHelper.swipeDown();
        Thread.sleep(SWIPE_DELAY);
    }

    public void swipeDown(int time) throws Exception {
        DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(nameMainWindow), time, 50, 10, 50, 90);
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
        getElement(nameEditingItemSelectAll, "Select All popup is not visible").click();
    }

    public void clickPopupCopyButton() throws Exception {
        getElement(nameEditingItemCopy, "Copy popup is not viisble").click();
    }

    public void clickPopupPasteButton() throws Exception {
        getElement(nameEditingItemPaste, "Paste popup is not visible").click();
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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), classNameKeyboard, 3);
    }

    public void clickKeyboardDeleteButton() throws Exception {
        getElement(nameKeyboardDeleteButton).click();
    }

    public void clickKeyboardReturnButton() throws Exception {
        getElement(nameKeyboardReturnButton).click();
    }

    public void clickKeyboardSendButton() throws Exception {
        getElement(nameKeyboardSendButton, "Keyboard Send button is not visible").click();
    }

    public void clickHideKeyboardButton() throws Exception {
        getElement(nameHideKeyboardButton).click();
    }

    public void clickSpaceKeyboardButton() throws Exception {
        getElement(nameSpaceButton).click();
    }

    public void clickDoneKeyboardButton() throws Exception {
        getElement(nameDoneButton, "Keyboard Done button is not visible").click();
    }

    public static Object executeScript(String script) throws Exception {
        return PlatformDrivers.getInstance().getDriver(Platform.iOS).get()
                .executeScript(script);
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


    public void minimizeApplication(int timeSeconds) throws Exception {
        assert getDriver() != null : "WebDriver is not ready";
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            CommonUtils.executeUIAppleScript(new String[]{
                    "tell application \"System Events\"",
                    "tell application \"Simulator\" to activate",
                    "tell application \"System Events\" to keystroke \"h\" using {command down, shift down}",
                    "tell application \"System Events\" to keystroke \"h\" using {command down, shift down}",
                    "end tell"}).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
            Thread.sleep(timeSeconds * 1000);
            final Dimension screenSize = getDriver().manage().window().getSize();
            getDriver().tap(1, screenSize.getWidth() / 3, screenSize.getHeight() / 2, DriverUtils.SINGLE_TAP_DURATION);
        } else {
            // https://discuss.appium.io/t/runappinbackground-does-not-work-for-ios9/6201
            this.getDriver().runAppInBackground(timeSeconds);
        }
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

    public void lockScreen(int timeSeconds) throws Exception {
        assert getDriver() != null : "WebDriver is not ready";
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            CommonUtils.executeUIAppleScript(new String[]{
                    "tell application \"System Events\"",
                    "tell application \"Simulator\" to activate",
                    "tell application \"System Events\" to keystroke \"l\" using {command down}",
                    "end tell"
            }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
            Thread.sleep(timeSeconds * 1000);
            // this is to show the unlock label if not visible yet
            CommonUtils.executeUIAppleScript(new String[]{
                    "tell application \"System Events\"",
                    "tell application \"Simulator\" to activate",
                    "tell application \"System Events\" to keystroke \"h\" using {command down, shift down}",
                    "end tell"
            }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
            IOSSimulatorHelper.swipeRight();
        } else {
            this.getDriver().lockScreen(timeSeconds);
        }
    }

    public void resetApplication() throws Exception {
        getDriver().resetApp();
    }
}
