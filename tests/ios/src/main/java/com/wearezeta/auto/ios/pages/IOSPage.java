package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;

import com.wearezeta.auto.ios.pages.keyboard.IOSKeyboard;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public abstract class IOSPage extends BasePage {
    private static final Logger log = ZetaLogger.getLog(IOSPage.class.getSimpleName());

    public final static long IOS_DRIVER_INIT_TIMEOUT = 1000 * 30 * 5;
    public static final int DRIVER_CREATION_RETRIES_COUNT = 2;

    private static final int DEFAULT_RETRY_COUNT = 2;

    protected static final String nameStrMainWindow = "ZClientMainWindow";
    protected static final By nameMainWindow = By.name(nameStrMainWindow);

    protected static final String xpathStrMainWindow =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']";

    protected static final By nameEditingItemSelectAll = By.name("Select All");

    protected static final By nameEditingItemCopy = By.name("Copy");

    protected static final By nameEditingItemPaste = By.name("Paste");

    private static String imagesPath = "";

    private static final Function<String, String> xpathStrAlertByText = text ->
            String.format("//UIAAlert[ .//*[contains(@name, '%s')] or contains(@name, '%s')]", text, text);

    private IOSKeyboard onScreenKeyboard;

    protected long getDriverInitializationTimeout() {
        return IOS_DRIVER_INIT_TIMEOUT * DRIVER_CREATION_RETRIES_COUNT;
    }

    public IOSPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);

        setImagesPath(CommonUtils.getSimulatorImagesPathFromConfig(this
                .getClass()));

        this.onScreenKeyboard = new IOSKeyboard(driver);
    }

    /**
     * Ugly workaround for random Appium bug when UI tree is sometimes not refreshed and is empty
     *
     * @param drv Appium driver instance
     * @return the same driver instance
     * @throws Exception
     */
    private static ZetaIOSDriver fixUITreeIfBroken(final ZetaIOSDriver drv) throws Exception {
        if (drv.findElements(By.name("//UIAWindow")).size() > 0) {
            return drv;
        }
        log.warn("Detected Appium UI tree corruption. Trying to fix...");
        try {
            if (drv.getOrientation() == ScreenOrientation.PORTRAIT) {
                drv.rotate(ScreenOrientation.LANDSCAPE);
                drv.rotate(ScreenOrientation.PORTRAIT);
            } else {
                drv.rotate(ScreenOrientation.PORTRAIT);
                drv.rotate(ScreenOrientation.LANDSCAPE);
            }
        } catch (WebDriverException e) {
            // pass silently
        }
        return drv;
    }

    @Override
    protected ZetaIOSDriver getDriver() throws Exception {
        return fixUITreeIfBroken((ZetaIOSDriver) super.getDriver());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Future<ZetaIOSDriver> getLazyDriver() {
        return (Future<ZetaIOSDriver>) super.getLazyDriver();
    }

    public void swipeRight(int time, int percentX, int percentY) throws Exception {
        DriverUtils.swipeRight(this.getDriver(), getElement(nameMainWindow), time, percentX, percentY);
    }

    public void swipeUp(int time) throws Exception {
        DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(nameMainWindow), time,
                50, 55, 50, 10);
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

    private void clickAtSimulator(int x, int y) throws Exception {
        final Dimension windowSize = getDriver().manage().window().getSize();
        IOSSimulatorHelper.clickAt(String.format("%.2f", x * 1.0 / windowSize.width),
                String.format("%.2f", y * 1.0 / windowSize.height));
    }

    /**
     * !!! this method is not able to enter line breaks !!!
     *
     * @param dstElement          the destination eit field
     * @param relativeClickPointX where to click the element before type, 0% <= X <= 100%
     * @param relativeClickPointY where to click the element before type, 0% <= Y <= 100%
     * @param str                 string to enter
     * @throws Exception
     */
    public void inputStringFromKeyboardAndCommit(WebElement dstElement, int relativeClickPointX, int relativeClickPointY,
                                                 String str, boolean useAutocompleteWorkaround) throws Exception {
        final Dimension elSize = dstElement.getSize();
        final Point elLocation = dstElement.getLocation();
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            Thread.sleep(1000);
            clickAtSimulator(elLocation.x + (relativeClickPointX * elSize.width) / 100,
                    elLocation.y + (relativeClickPointY * elSize.height) / 100);
            Thread.sleep(1000);
            IOSSimulatorHelper.typeStringAndPressEnter(str, useAutocompleteWorkaround);
        } else {
            getDriver().tap(1,
                    elLocation.x + (relativeClickPointX * elSize.width) / 100,
                    elLocation.y + (relativeClickPointY * elSize.height) / 100,
                    DriverUtils.SINGLE_TAP_DURATION);
            this.onScreenKeyboard.typeString(str);
            this.clickKeyboardCommitButton();
        }
    }

    public void inputStringFromKeyboardAndCommit(WebElement dstElement, String str, boolean useAutocompleteWorkaround)
            throws Exception {
        inputStringFromKeyboardAndCommit(dstElement, 50, 50, str, useAutocompleteWorkaround);
    }

    public boolean isKeyboardVisible() throws Exception {
        return this.onScreenKeyboard.isVisible();
    }

    public void clickKeyboardDeleteButton() throws Exception {
        this.onScreenKeyboard.pressDeleteButton();
    }

    public void clickHideKeyboardButton() throws Exception {
        this.onScreenKeyboard.pressHideButton();
    }

    public void clickSpaceKeyboardButton() throws Exception {
        this.onScreenKeyboard.pressSpaceButton();
    }

    public void clickKeyboardCommitButton() throws Exception {
        this.onScreenKeyboard.pressCommitButton();
    }

    public static Object executeScript(String script) throws Exception {
        return PlatformDrivers.getInstance().getDriver(Platform.iOS).get()
                .executeScript(script);
    }

    public void hideKeyboard() throws Exception {
        this.getDriver().hideKeyboard();
    }

    public void acceptAlert() throws Exception {
        if (DriverUtils.waitUntilAlertAppears(this.getDriver())) {
            this.getDriver().switchTo().alert().accept();
        }
    }

    public void dismissAlert() throws Exception {
        if (DriverUtils.waitUntilAlertAppears(this.getDriver())) {
            this.getDriver().switchTo().alert().dismiss();
        }
    }

    public boolean isAlertContainsText(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrAlertByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void minimizeApplication(int timeSeconds) throws Exception {
        assert getDriver() != null : "WebDriver is not ready";
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            IOSSimulatorHelper.switchToAppsList();
            final int clickAtHelperDuration = 4; // seconds
            if (timeSeconds >= clickAtHelperDuration) {
                Thread.sleep((timeSeconds - clickAtHelperDuration) * 1000);
            } else {
                Thread.sleep(timeSeconds * 1000);
            }
            IOSSimulatorHelper.clickAt("0.3", "0.5");
        } else {
            // https://discuss.appium.io/t/runappinbackground-does-not-work-for-ios9/6201
            this.getDriver().runAppInBackground(timeSeconds);
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

    private void rotateLandscape() throws Exception {
        this.getDriver().rotate(ScreenOrientation.LANDSCAPE);
    }

    private void rotatePortrait() throws Exception {
        this.getDriver().rotate(ScreenOrientation.PORTRAIT);
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
            IOSSimulatorHelper.lock();
            Thread.sleep(timeSeconds * 1000);
            // this is to show the unlock label if not visible yet
            IOSSimulatorHelper.goHome();
            IOSSimulatorHelper.swipeRight();
        } else {
            this.getDriver().lockScreen(timeSeconds);
        }
    }

    public void clickElementWithRetryIfStillDisplayed(By locator, int retryCount) throws Exception {
        WebElement el = getElement(locator);
        int counter = 0;
        do {
            el.click();
            counter++;
            if (DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator)) {
                return;
            }
        } while (counter < retryCount);
        throw new IllegalStateException(String.format("Locator %s is still displayed", locator));
    }

    public void clickElementWithRetryIfStillDisplayed(By locator) throws Exception {
        clickElementWithRetryIfStillDisplayed(locator, DEFAULT_RETRY_COUNT);
    }

    public void clickElementWithRetryIfNextElementNotAppears(By locator, By nextLocator, int retryCount)
            throws Exception {
        WebElement el = getElement(locator);
        int counter = 0;
        do {
            el.click();
            counter++;
            if (DriverUtils.waitUntilLocatorAppears(this.getDriver(), nextLocator)) {
                return;
            }
        } while (counter < retryCount);
        throw new IllegalStateException(String.format("Locator %s did't appear", nextLocator));
    }

    public void clickElementWithRetryIfNextElementAppears(By locator, By nextLocator) throws Exception {
        clickElementWithRetryIfNextElementNotAppears(locator, nextLocator, DEFAULT_RETRY_COUNT);
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
}
