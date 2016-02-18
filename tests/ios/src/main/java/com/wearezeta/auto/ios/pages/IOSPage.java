package com.wearezeta.auto.ios.pages;

import java.util.Optional;
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

    protected static final By xpathBrowserURLButton = By.xpath("//UIAButton[@name='URL']");

    protected static final By nameBackToWireBrowserButton = By.name("Back to Wire");

    private IOSKeyboard onScreenKeyboard;

    protected long getDriverInitializationTimeout() {
        return IOS_DRIVER_INIT_TIMEOUT * DRIVER_CREATION_RETRIES_COUNT;
    }

    public IOSPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);

        setImagesPath(CommonUtils.getSimulatorImagesPathFromConfig(this.getClass()));

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
//        if (drv.findElements(By.className("UIAWindow")).size() > 0) {
//            return drv;
//        }
//        log.warn("Detected Appium UI tree corruption. Trying to fix...");
//        try {
//            if (drv.getOrientation() == ScreenOrientation.PORTRAIT) {
//                drv.rotate(ScreenOrientation.LANDSCAPE);
//                drv.rotate(ScreenOrientation.PORTRAIT);
//            } else {
//                drv.rotate(ScreenOrientation.PORTRAIT);
//                drv.rotate(ScreenOrientation.LANDSCAPE);
//            }
//            Thread.sleep(500);
//        } catch (WebDriverException e) {
//            // pass silently
//        }
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
        getElement(nameEditingItemCopy, "Copy popup is not visible").click();
    }

    public void clickPopupPasteButton() throws Exception {
        getElement(nameEditingItemPaste, "Paste popup is not visible").click();
        final int popupVisibilityTimeoutSeconds = 10;
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameEditingItemPaste, popupVisibilityTimeoutSeconds)) {
            throw new IllegalStateException(String.format(
                    "Paste popup is still visible after %s seconds timeout", popupVisibilityTimeoutSeconds));
        }
        Thread.sleep(2000);
    }

    private void clickAtSimulator(int x, int y) throws Exception {
        final Dimension windowSize = getDriver().manage().window().getSize();
        IOSSimulatorHelper.clickAt(String.format("%.2f", x * 1.0 / windowSize.width),
                String.format("%.2f", y * 1.0 / windowSize.height),
                String.format("%.3f", DriverUtils.SINGLE_TAP_DURATION / 1000.0));
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
    public void inputStringFromKeyboard(WebElement dstElement, int relativeClickPointX, int relativeClickPointY,
                                        String str, boolean useAutocompleteWorkaround, boolean shouldCommitInput)
            throws Exception {
        final Dimension elSize = dstElement.getSize();
        final Point elLocation = dstElement.getLocation();
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            Thread.sleep(1000);
            clickAtSimulator(elLocation.x + (relativeClickPointX * elSize.width) / 100,
                    elLocation.y + (relativeClickPointY * elSize.height) / 100);
            Thread.sleep(2000);
            if (shouldCommitInput) {
                IOSSimulatorHelper.typeStringAndPressEnter(str, useAutocompleteWorkaround);
            } else {
                IOSSimulatorHelper.typeString(str, useAutocompleteWorkaround);
            }
        } else {
            getDriver().tap(1,
                    elLocation.x + (relativeClickPointX * elSize.width) / 100,
                    elLocation.y + (relativeClickPointY * elSize.height) / 100,
                    DriverUtils.SINGLE_TAP_DURATION);
            this.onScreenKeyboard.typeString(str);
            if (shouldCommitInput) {
                this.clickKeyboardCommitButton();
            }
        }
    }

    public void inputStringFromKeyboard(WebElement dstElement, String str, boolean useAutocompleteWorkaround,
                                        boolean shouldCommitInput) throws Exception {
        inputStringFromKeyboard(dstElement, 50, 50, str, useAutocompleteWorkaround, shouldCommitInput);
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

    public void acceptAlertIfVisible() throws Exception {
        final Optional<Alert> alert = DriverUtils.getAlertIfDisplayed(getDriver());
        if (alert.isPresent()) {
            alert.get().accept();
        }
    }

    public void acceptAlertIfVisible(int timeoutSeconds) throws Exception {
        final Optional<Alert> alert = DriverUtils.getAlertIfDisplayed(getDriver(), timeoutSeconds);
        if (alert.isPresent()) {
            alert.get().accept();
        }
    }

    public void dismissAlertIfVisible() throws Exception {
        final Optional<Alert> alert = DriverUtils.getAlertIfDisplayed(getDriver());
        if (alert.isPresent()) {
            alert.get().dismiss();
        }
    }

    public boolean isAlertContainsText(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrAlertByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void minimizeApplication(int timeSeconds) throws Exception {
        assert getDriver() != null : "WebDriver is not ready";
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            final long millisecondsStarted = System.currentTimeMillis();
            IOSSimulatorHelper.switchToAppsList();
            final long clickAtHelperDuration = (System.currentTimeMillis() - millisecondsStarted) / 1000; // seconds
            if (timeSeconds > clickAtHelperDuration + 1) {
                Thread.sleep((timeSeconds - clickAtHelperDuration) * 1000);
            } else {
                Thread.sleep(2000);
            }
            IOSSimulatorHelper.clickAt("0.3", "0.5",
                    String.format("%.3f", DriverUtils.SINGLE_TAP_DURATION / 1000.0));
            // Wait for transition animation
            Thread.sleep(2000);
        } else {
            // https://discuss.appium.io/t/runappinbackground-does-not-work-for-ios9/6201
            this.getDriver().runAppInBackground(timeSeconds);
        }
    }

    @SuppressWarnings("unused")
    protected void longClickAt(WebElement el) throws Exception {
        final Dimension elSize = el.getSize();
        final Point elLocation = el.getLocation();
        final Dimension windowSize = getDriver().manage().window().getSize();
        IOSSimulatorHelper.clickAt(
                String.format("%.2f", (elLocation.x + elSize.width / 2) * 1.0 / windowSize.width),
                String.format("%.2f", (elLocation.y + elSize.height / 2) * 1.0 / windowSize.height),
                String.format("%.3f", DriverUtils.LONG_TAP_DURATION / 1000.0));
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
            final long millisecondsStarted = System.currentTimeMillis();
            IOSSimulatorHelper.lock();
            final long lockDuration = (System.currentTimeMillis() - millisecondsStarted) / 1000;
            if (timeSeconds > lockDuration + 1) {
                Thread.sleep((timeSeconds - lockDuration) * 1000);
            } else {
                Thread.sleep(2000);
            }
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

    public boolean isWebPageVisible(String expectedUrl) throws Exception {
        final WebElement urlBar = getElement(xpathBrowserURLButton, "The address bar of web browser is not visible");
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            final Dimension elSize = urlBar.getSize();
            final Point elLocation = urlBar.getLocation();
            clickAtSimulator(elLocation.x + (elSize.width / 6) / 100, elLocation.y + (elSize.height / 2) / 100);
            Thread.sleep(1000);
        } else {
            urlBar.click();
        }
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(expectedUrl));
    }

    public void tapBackToWire() throws Exception {
        final WebElement backToWireButton = getElement(nameBackToWireBrowserButton);
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            final Dimension elSize = backToWireButton.getSize();
            final Point elLocation = backToWireButton.getLocation();
            clickAtSimulator(elLocation.x + (elSize.width / 2) / 100, elLocation.y + (elSize.height / 2) / 100);
            Thread.sleep(1000);
        } else {
            backToWireButton.click();
        }
    }
}
