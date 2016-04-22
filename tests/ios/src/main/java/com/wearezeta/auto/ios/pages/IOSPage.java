package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.*;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;

import com.wearezeta.auto.ios.pages.keyboard.IOSKeyboard;

public abstract class IOSPage extends BasePage {
    private static final Logger log = ZetaLogger.getLog(IOSPage.class.getSimpleName());

    public final static long IOS_DRIVER_INIT_TIMEOUT_MILLIS = ZetaIOSDriver.MAX_COMMAND_DURATION_MILLIS;
    public static final int DRIVER_CREATION_RETRIES_COUNT = 2;

    private static final int DEFAULT_RETRY_COUNT = 2;

    protected static final String nameStrMainWindow = "ZClientMainWindow";
    protected static final By nameMainWindow = MobileBy.AccessibilityId(nameStrMainWindow);

    protected static final String xpathStrMainWindow =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']";

    protected static final By nameEditingItemSelectAll = MobileBy.AccessibilityId("Select All");

    protected static final By nameEditingItemCopy = MobileBy.AccessibilityId("Copy");

    protected static final By nameEditingItemPaste = MobileBy.AccessibilityId("Paste");

    private static final Function<String, String> xpathStrAlertByText = text ->
            String.format("//UIAAlert[ .//*[contains(@name, '%s')] or contains(@name, '%s')]", text, text);

    protected static final By xpathBrowserURLButton = By.xpath("//UIAButton[@name='URL']");

    protected static final By nameBackToWireBrowserButton = MobileBy.AccessibilityId("Back to Wire");

    private static final By xpathConfirmButton = By.xpath("//UIAButton[@name='OK' and @visible='true']");

    private IOSKeyboard onScreenKeyboard;

    protected long getDriverInitializationTimeout() {
        return IOS_DRIVER_INIT_TIMEOUT_MILLIS * DRIVER_CREATION_RETRIES_COUNT +
                ZetaDriver.RECREATE_DELAY_SECONDS * (DRIVER_CREATION_RETRIES_COUNT - 1);
    }

    public IOSPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);

        this.onScreenKeyboard = new IOSKeyboard(driver);
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
            log.warn((String.format(
                    "Paste popup is still appears to be visible after %s seconds timeout",
                    popupVisibilityTimeoutSeconds)));
        }
        Thread.sleep(2000);
    }

    private void clickAtSimulator(int x, int y) throws Exception {
        final Dimension windowSize = getDriver().manage().window().getSize();
        IOSSimulatorHelper.clickAt(String.format("%.2f", x * 1.0 / windowSize.width),
                String.format("%.2f", y * 1.0 / windowSize.height),
                String.format("%.3f", DriverUtils.SINGLE_TAP_DURATION / 1000.0));
    }

    private void longClickAtSimulator(int x, int y) throws Exception {
        final Dimension windowSize = getDriver().manage().window().getSize();
        IOSSimulatorHelper.clickAt(String.format("%.2f", x * 1.0 / windowSize.width),
                String.format("%.2f", y * 1.0 / windowSize.height), "2");
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
                                        String str, boolean shouldCommitInput) throws Exception {
        final Dimension elSize = dstElement.getSize();
        final Point elLocation = dstElement.getLocation();
        final int tapX = elLocation.x + (relativeClickPointX * elSize.width) / 100;
        final int tapY = elLocation.y + (relativeClickPointY * elSize.height) / 100;
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            CommonUtils.setStringValueInSystemClipboard(str);
            // FIXME: Paste menu will not be shown without this
            IOSSimulatorHelper.selectPasteMenuItem();
            longClickAtSimulator(tapX, tapY);
            getElement(nameEditingItemPaste).click();
            if (shouldCommitInput) {
                IOSSimulatorHelper.pressEnterKey();
            }
        } else {
            getDriver().tap(1, tapX, tapY, DriverUtils.SINGLE_TAP_DURATION);
            this.onScreenKeyboard.typeString(str);
            if (shouldCommitInput) {
                this.clickKeyboardCommitButton();
            }
        }
    }

    public void inputStringFromPasteboard(WebElement dstElement, boolean shouldCommitInput) throws Exception {
        final Dimension elSize = dstElement.getSize();
        final Point elLocation = dstElement.getLocation();
        final int tapX = elLocation.x + elSize.width / 2;
        final int tapY = elLocation.y + elSize.height / 2;
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            longClickAtSimulator(tapX, tapY);
            getElement(nameEditingItemPaste).click();
            if (shouldCommitInput) {
                IOSSimulatorHelper.pressEnterKey();
            }
        } else {
            getDriver().tap(1, tapX, tapY, DriverUtils.LONG_TAP_DURATION);
            getElement(nameEditingItemPaste, "Paste item is not visible", 15).click();
            if (shouldCommitInput) {
                this.clickKeyboardCommitButton();
            }
        }
    }

    public void inputStringFromKeyboard(WebElement dstElement, String str, boolean shouldCommitInput) throws Exception {
        inputStringFromKeyboard(dstElement, 50, 50, str, shouldCommitInput);
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
        try {
            final Optional<Alert> alert = DriverUtils.getAlertIfDisplayed(getDriver());
            if (alert.isPresent()) {
                alert.get().accept();
            }
        } catch (WebDriverException e) {
            // ignore
        }
    }

    public void acceptAlertIfVisible(int timeoutSeconds) throws Exception {
        try {
            final Optional<Alert> alert = DriverUtils.getAlertIfDisplayed(getDriver(), timeoutSeconds);
            if (alert.isPresent()) {
                alert.get().accept();
            }
        } catch (WebDriverException e) {
            // ignore
        }
    }

    public void dismissAlertIfVisible() throws Exception {
        try {
            final Optional<Alert> alert = DriverUtils.getAlertIfDisplayed(getDriver());
            if (alert.isPresent()) {
                alert.get().dismiss();
            }
        } catch (WebDriverException e) {
            // ignore
        }
    }

    public boolean isAlertContainsText(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrAlertByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void minimizeApplication(int timeSeconds) throws Exception {
        assert getDriver() != null : "WebDriver is not ready";
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            IOSSimulatorHelper.goHome();
            Thread.sleep(timeSeconds * 1000);
            final String autPath = (String) getDriver().getCapabilities().getCapability("app");
            String bundleId;
            if (autPath.endsWith(".app")) {
                bundleId = IOSCommonUtils.getBundleId(new File(autPath + "/Info.plist"));
            } else {
                final File appPath = IOSCommonUtils.extractAppFromIpa(new File(autPath));
                try {
                    bundleId = IOSCommonUtils.getBundleId(new File(appPath.getCanonicalPath() + "/Info.plist"));
                } finally {
                    FileUtils.deleteDirectory(appPath);
                }
            }
            IOSSimulatorHelper.launchApp(bundleId);
            Thread.sleep(1000);
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
            Thread.sleep(2000);
        } else {
            this.getDriver().lockScreen(timeSeconds);
        }
    }

    public void lockScreenOnRealDevice() throws Exception {
        final ZetaIOSDriver driver = this.getDriver();
        final Callable callable = new Callable<Boolean>(){

            @Override
            public Boolean call() throws Exception {
                driver.lockScreen(20);
                return true;
            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(callable);
    }

    public void clickElementWithRetryIfStillDisplayed(By locator, int retryCount) throws Exception {
        WebElement el = getElement(locator);
        int counter = 0;
        do {
            el.click();
            counter++;
            if (DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator, 4)) {
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
            clickAtSimulator(elLocation.x + elSize.width / 6, elLocation.y + elSize.height / 2);
            Thread.sleep(1000);
        } else {
            urlBar.click();
        }
        return DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.xpath(String.format("//*[starts-with(@name, '%s')]", expectedUrl)));
    }

    public void tapBackToWire() throws Exception {
        final WebElement backToWireButton = getElement(nameBackToWireBrowserButton);
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            final Dimension elSize = backToWireButton.getSize();
            final Point elLocation = backToWireButton.getLocation();
            clickAtSimulator(elLocation.x + elSize.width / 2, elLocation.y + elSize.height / 2);
            Thread.sleep(1000);
        } else {
            backToWireButton.click();
        }
    }

    public void installIpa(File ipaFile) throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.installIpa(ipaFile);
        } else {
            throw new NotImplementedException("Application install is only available for Simulator");
        }
    }

    public void pressConfirmButton() throws Exception {
        getElement(xpathConfirmButton).click();
    }

    /**
     * fixes taking tablet simulator screenshots via simshot
     *
     * @return Optinal screenshot image
     * @throws Exception
     */
    @Override
    public Optional<BufferedImage> takeScreenshot() throws Exception {
        Optional<BufferedImage> result = super.takeScreenshot();
        if (CommonUtils.getIsSimulatorFromConfig(getClass()) && result.isPresent()) {
            final Dimension screenSize = getDriver().manage().window().getSize();
            final double scaleX = 1.0 * result.get().getWidth() / screenSize.getWidth();
            final double scaleY = 1.0 * result.get().getHeight() / screenSize.getHeight();
            if (scaleX < 1 || scaleY < 1) {
                final double scale = (scaleX > scaleY) ? scaleY : scaleX;
                result = Optional.of(ImageUtil.resizeImage(result.get(), (float) (1.0 / scale)));
            }
        }
        return result;
    }
}
