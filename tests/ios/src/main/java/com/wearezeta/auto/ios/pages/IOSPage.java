package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import com.wearezeta.auto.ios.tools.IRunnableWithException;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.ios.pages.keyboard.IOSKeyboard;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public abstract class IOSPage extends BasePage {
    private static final Logger log = ZetaLogger.getLog(IOSPage.class.getSimpleName());

    public final static long IOS_DRIVER_INIT_TIMEOUT = 1000 * 60 * 3;

    private static final int SWIPE_DELAY = 10 * 1000; // milliseconds
    private static final int DEFAULT_RETRY_COUNT = 2;

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

    private IOSKeyboard onScreenKeyboard;

    protected long getDriverInitializationTimeout() {
        return IOS_DRIVER_INIT_TIMEOUT;
    }

    public IOSPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);

        setImagesPath(CommonUtils.getSimulatorImagesPathFromConfig(this
                .getClass()));

        this.onScreenKeyboard = new IOSKeyboard(driver, getDriverInitializationTimeout());
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

    public void inputStringFromKeyboard(String str) throws Exception {
        this.onScreenKeyboard.typeString(str);
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
                    "do shell script \"/bin/sleep 3\"",
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

    public void workaroundUITreeRefreshIssue(IRunnableWithException f) throws Throwable {
        try {
            f.run();
            return;
        } catch (Throwable e) {
            log.warn("UI tree seems to be corrupted. Trying to refresh...");
            this.printPageSource();
            if (getOrientation() == ScreenOrientation.PORTRAIT) {
                rotateLandscape();
                rotatePortrait();
            } else {
                rotatePortrait();
                rotateLandscape();
            }
        }
        f.run();
    }

    private void rotateLandscape() throws Exception {
        this.getDriver().rotate(ScreenOrientation.LANDSCAPE);
    }

    private void rotatePortrait() throws Exception {
        this.getDriver().rotate(ScreenOrientation.PORTRAIT);
    }

    private ScreenOrientation getOrientation() throws Exception {
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
                    "do shell script \"/bin/sleep 3\"",
                    "tell application \"System Events\" to keystroke \"l\" using {command down}",
                    "end tell"
            }).get(IOSSimulatorHelper.SIMULATOR_INTERACTION_TIMEOUT, TimeUnit.SECONDS);
            Thread.sleep(timeSeconds * 1000);
            // this is to show the unlock label if not visible yet
            CommonUtils.executeUIAppleScript(new String[]{
                    "tell application \"System Events\"",
                    "tell application \"Simulator\" to activate",
                    "do shell script \"/bin/sleep 3\"",
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

    public void clickElementWithRetryIfStillDisplayed(By locator, int retryCount) throws Exception {
        WebElement el = getElement(locator);
        int counter = 0;
        boolean flag;
        do {
            el.click();
            counter++;
            flag = DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
            if (flag) {
                return;
            }
        } while (!flag && counter <= retryCount);
        throw new IllegalStateException(String.format("Locator %s is still displayed", locator));
    }
    
    public void clickElementWithRetryIfStillDisplayed(By locator) throws Exception {
        clickElementWithRetryIfStillDisplayed(locator, DEFAULT_RETRY_COUNT);
    }

    @Override
    protected WebElement getElement(By locator) throws Exception {
        try {
            return super.getElement(locator);
        } catch (Exception e) {
            log.debug(getDriver().getPageSource());
            throw e;
        }
    }

    @Override
    protected WebElement getElement(By locator, String message) throws Exception {
        try {
            return super.getElement(locator, message);
        } catch (Exception e) {
            log.debug(getDriver().getPageSource());
            throw e;
        }
    }

    @Override
    protected WebElement getElement(By locator, String message, int timeoutSeconds) throws Exception {
        try {
            return super.getElement(locator, message, timeoutSeconds);
        } catch (Exception e) {
            log.debug(getDriver().getPageSource());
            throw e;
        }
    }

    /**
     * We have to count the fact that Simulator window might be scaled
     *
     * @return optionally, full screen shot as BufferedImage
     * @throws Exception
     */
    @Override
    public Optional<BufferedImage> takeScreenshot() throws Exception {
        final BufferedImage screen = DriverUtils.takeFullScreenShot(this.getDriver()).orElseThrow(
                () -> new IllegalStateException("Appium has failed to take full screen shot"));
        final float currentScreenHeight = getDriver().manage().window().getSize().getHeight();
        final float realScreenHeight = screen.getHeight();
        if ((int)currentScreenHeight != (int)realScreenHeight) {
            return Optional.of(ImageUtil.resizeImage(screen, currentScreenHeight / realScreenHeight));
        }
        return Optional.of(screen);
    }
}
