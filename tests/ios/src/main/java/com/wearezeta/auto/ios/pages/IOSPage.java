package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.driver.*;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;

import com.wearezeta.auto.ios.pages.keyboard.IOSKeyboard;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.UnreachableBrowserException;


public abstract class IOSPage extends BasePage {
    private static final Logger log = ZetaLogger.getLog(IOSPage.class.getSimpleName());

    public static final int DRIVER_CREATION_RETRIES_COUNT = 2;

    private static final int DEFAULT_RETRY_COUNT = 2;

    protected static final String nameStrMainWindow = "ZClientMainWindow";
    protected static final By nameMainWindow = MobileBy.AccessibilityId(nameStrMainWindow);

    protected static final String xpathStrMainWindow = "//XCUIElementTypeWindow[@name='ZClientMainWindow']";

    private static final By nameBadgeItemSelectAll = MobileBy.AccessibilityId("Select All");
    private static final By nameBadgeItemCopy = MobileBy.AccessibilityId("Copy");
    private static final By nameBadgeItemDelete = MobileBy.AccessibilityId("Delete");
    private static final By nameBadgeItemPaste = MobileBy.AccessibilityId("Paste");
    private static final By nameBadgeItemSave = MobileBy.AccessibilityId("Save");
    private static final By nameBadgeItemEdit = MobileBy.AccessibilityId("Edit");
    private static final By nameBadgeItemLike = MobileBy.AccessibilityId("Like");
    private static final By nameBadgeItemUnlike = MobileBy.AccessibilityId("Unlike");

    private static final Function<String, String> xpathStrAlertByText = text ->
            String.format("//XCUIElementTypeAlert[ .//*[contains(@name, '%s')] or contains(@name, '%s')]", text, text);

    protected static final By xpathBrowserURLButton = By.xpath("//XCUIElementTypeButton[@name='URL']");

    protected static final By nameBackToWireBrowserButton = MobileBy.AccessibilityId("Back to Wire");

    protected static final By xpathConfirmButton = By.xpath("//XCUIElementTypeButton[@name='OK' and @visible='true']");

    protected static final By xpathCancelButton = By.xpath("//XCUIElementTypeButton[@name='Cancel' and @visible='true']");

    private static final By nameDoneButton = MobileBy.AccessibilityId("Done");

    private static final By classAlert = By.className("XCUIElementTypeAlert");

    protected static final Function<String, String> xpathStrAlertButtonByCaption = caption ->
            String.format("//XCUIElementTypeAlert//XCUIElementTypeButton[@label='%s']", caption);

    private IOSKeyboard onScreenKeyboard;

    protected long getDriverInitializationTimeout() {
        return (ZetaIOSDriver.MAX_SESSION_INIT_DURATION_MILLIS + AppiumServer.RESTART_TIMEOUT_MILLIS)
                * DRIVER_CREATION_RETRIES_COUNT;
    }

    public IOSPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);

        this.onScreenKeyboard = new IOSKeyboard(driver);
    }

    @Override
    protected ZetaIOSDriver getDriver() throws Exception {
        try {
            return (ZetaIOSDriver) super.getDriver();
        } catch (ExecutionException e) {
            if ((e.getCause() instanceof UnreachableBrowserException) ||
                    (e.getCause() instanceof TimeoutException) ||
                    ((e.getCause() instanceof WebDriverException) &&
                            (e.getCause().getCause() instanceof TimeoutException))) {
                throw new TimeoutException((AppiumServer.getInstance().getLog().orElse("Appium log is empty")) +
                        "\n" + ExceptionUtils.getStackTrace(e));
            } else {
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Future<ZetaIOSDriver> getLazyDriver() {
        return (Future<ZetaIOSDriver>) super.getLazyDriver();
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

    private By getBadgeLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "select all":
                return nameBadgeItemSelectAll;
            case "edit":
                return nameBadgeItemEdit;
            case "copy":
                return nameBadgeItemCopy;
            case "delete":
                return nameBadgeItemDelete;
            case "paste":
                return nameBadgeItemPaste;
            case "save":
                return nameBadgeItemSave;
            case "like":
                return nameBadgeItemLike;
            case "unlike":
                return nameBadgeItemUnlike;
            default:
                throw new IllegalArgumentException(String.format("Unknown badge name: '%s'", name));
        }
    }

    private static final int MAX_BADGE_VISIBILITY_TIMEOUT = 10; // seconds

    public void tapBadgeItem(String name) throws Exception {
        final By locator = getBadgeLocatorByName(name);
        getElement(locator).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, MAX_BADGE_VISIBILITY_TIMEOUT)) {
            log.warn(String.format("%s badge still appears to be visible after %s seconds timeout", name,
                    MAX_BADGE_VISIBILITY_TIMEOUT));
        }
    }

    public boolean isBadgeItemVisible(String name) throws Exception {
        final By locator = getBadgeLocatorByName(name);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isBadgeItemInvisible(String name) throws Exception {
        final By locator = getBadgeLocatorByName(name);
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void clickAtSimulator(int x, int y) throws Exception {
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

    public void inputStringFromPasteboard(WebElement dstElement, boolean shouldCommitInput) throws Exception {
        final Dimension elSize = dstElement.getSize();
        final Point elLocation = dstElement.getLocation();
        final int tapX = elLocation.x + elSize.width / 2;
        final int tapY = elLocation.y + elSize.height / 2;
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            longClickAtSimulator(tapX, tapY);
            getElement(nameBadgeItemPaste).click();
            if (shouldCommitInput) {
                IOSSimulatorHelper.pressEnterKey();
            }
        } else {
            getDriver().tap(1, tapX, tapY, DriverUtils.LONG_TAP_DURATION);
            getElement(nameBadgeItemPaste, "Paste item is not visible", 15).click();
            if (shouldCommitInput) {
                this.tapKeyboardCommitButton();
            }
        }
    }

    public boolean isKeyboardVisible() throws Exception {
        return this.onScreenKeyboard.isVisible();
    }

    public boolean isKeyboardInvisible(int timeoutSeconds) throws Exception {
        return this.onScreenKeyboard.isInvisible(timeoutSeconds);
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

    public void tapKeyboardCommitButton() throws Exception {
        this.onScreenKeyboard.pressCommitButton();
    }

    public static Object executeScript(String script) throws Exception {
        return PlatformDrivers.getInstance().getDriver(Platform.iOS).get()
                .executeScript(script);
    }

    public boolean acceptAlertIfVisible() throws Exception {
        return acceptAlertIfVisible(DriverUtils.getDefaultLookupTimeoutSeconds());
    }

    public boolean acceptAlertIfVisible(int timeoutSeconds) throws Exception {
        if (waitUntilAlertAppears(timeoutSeconds)) {
            if (getDriver().isXCUIModeEnabled()) {
                getDriver().acceptAlert();
            } else {
                getDriver().switchTo().alert().accept();
            }
            return true;
        }
        return false;
    }

    public boolean dismissAlertIfVisible() throws Exception {
        return dismissAlertIfVisible(DriverUtils.getDefaultLookupTimeoutSeconds());
    }

    public boolean dismissAlertIfVisible(int timeoutSeconds) throws Exception {
        if (waitUntilAlertAppears(timeoutSeconds)) {
            if (getDriver().isXCUIModeEnabled()) {
                getDriver().dismissAlert();
            } else {
                getDriver().switchTo().alert().dismiss();
            }
            return true;
        }
        return false;
    }

    public boolean isAlertContainsText(String expectedText) throws Exception {
        final By locator = By.xpath(xpathStrAlertByText.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void minimizeApplication(int timeSeconds) throws Exception {
        this.getDriver().runAppInBackground(timeSeconds);
    }

    protected void doubleClickAt(WebElement el, int percentX, int percentY) throws Exception {
        if (!CommonUtils.getIsSimulatorFromConfig(this.getClass())) {
            throw new IllegalStateException("This method works for iOS Simulator only");
        }
        final Dimension elSize = el.getSize();
        final Point elLocation = el.getLocation();
        final Dimension windowSize = getDriver().manage().window().getSize();
        IOSSimulatorHelper.doubleClickAt(
                String.format("%.2f", (elLocation.x + elSize.width * percentX / 100.0) / windowSize.width),
                String.format("%.2f", (elLocation.y + elSize.height * percentY / 100.0) / windowSize.height));
    }

    protected void doubleClickAt(WebElement el) throws Exception {
        doubleClickAt(el, 50, 50);
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
            this.getDriver().lockDevice(timeSeconds);
        }
    }

    public Future<?> lockScreenOnRealDevice() throws Exception {
        /*
        this method can return the future itself, so you have more control over execution.
        Also, it might come in handy to pass timeout as a parameter.
        This can be done more efficiently with Java8:

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
        String threadName = Thread.currentThread().getName();
        System.out.println("Hello " + threadName);
        });
         */
        final ZetaIOSDriver driver = this.getDriver();
        final Callable callable = () -> {
            driver.lockDevice(20);
            return true;
        };
        return Executors.newSingleThreadExecutor().submit(callable);
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

    private static final int ALERT_VISIBILITY_TIMEOUT_SECONDS = 2;

    @Override
    protected WebElement getElement(By locator) throws Exception {
        try {
            return super.getElement(locator);
        } catch (Exception e) {
            log.debug(getDriver().getPageSource());
            if (getDriver().getCapabilities().is(ZetaIOSDriver.AUTO_ACCEPT_ALERTS_CAPABILITY_NAME)) {
                acceptAlertIfVisible(ALERT_VISIBILITY_TIMEOUT_SECONDS);
                return super.getElement(locator);
            }
            throw e;
        }
    }

    @Override
    protected WebElement getElement(By locator, String message) throws Exception {
        try {
            return super.getElement(locator, message);
        } catch (Exception e) {
            log.debug(getDriver().getPageSource());
            if (getDriver().getCapabilities().is(ZetaIOSDriver.AUTO_ACCEPT_ALERTS_CAPABILITY_NAME)) {
                acceptAlertIfVisible(ALERT_VISIBILITY_TIMEOUT_SECONDS);
                return super.getElement(locator, message);
            }
            throw e;
        }
    }

    @Override
    protected WebElement getElement(By locator, String message, int timeoutSeconds) throws Exception {
        try {
            return super.getElement(locator, message, timeoutSeconds);
        } catch (Exception e) {
            log.debug(getDriver().getPageSource());
            if (getDriver().getCapabilities().is(ZetaIOSDriver.AUTO_ACCEPT_ALERTS_CAPABILITY_NAME)) {
                acceptAlertIfVisible(ALERT_VISIBILITY_TIMEOUT_SECONDS);
                return super.getElement(locator, message, timeoutSeconds);
            }
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

    public void tapConfirmButton() throws Exception {
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

    public void tapCancelButton() throws Exception {
        getElement(xpathCancelButton).click();
    }

    public void tapDoneButton() throws Exception {
        getElement(nameDoneButton).click();
    }

    public void installApp(File appFile) throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.installApp(appFile);
        } else {
            throw new NotImplementedException("Application install is only available for Simulator");
        }
    }

    public void tapByPercentOfElementSize(FBElement el, int percentX, int percentY) throws Exception {
        final Dimension size = el.getSize();
        el.tap(percentX * size.getWidth() / 100, percentY * size.getHeight() / 100);
    }

    public void tapAtTheCenterOfElement(FBElement el) throws Exception {
        tapByPercentOfElementSize(el, 50, 50);
    }

    public boolean waitUntilAlertAppears() throws Exception {
        return waitUntilAlertAppears(DriverUtils.getDefaultLookupTimeoutSeconds());
    }

    public boolean waitUntilAlertAppears(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), classAlert, timeoutSeconds);
    }

    public void tapAlertButton(String caption) throws Exception {
        final By locator = By.xpath(xpathStrAlertButtonByCaption.apply(caption));
        getElement(locator).click();
    }

    public boolean isKeyboardInvisible() throws Exception {
        return this.onScreenKeyboard.isInvisible();
    }
}
