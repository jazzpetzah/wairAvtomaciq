package com.wearezeta.auto.android.pages;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.logging.AndroidLogListener;
import com.wearezeta.auto.android.common.logging.AndroidLogListener.ListenerType;
import com.wearezeta.auto.android.common.uiautomation.UIAutomatorDriver;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.AppiumServer;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.touch.TouchActions;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public abstract class AndroidPage extends BasePage {
    private static final Function<String, String> xpathStrAlertMessageByText =
            text -> String.format("//*[@id='message' and contains(@value, '%s')]", text);

    private static final Function<String, String> xpathStrAlertTitleByTextPart =
            text -> String.format("//*[@id='alertTitle' and contains(@value, '%s')]", text);

    protected static final By idGiphyPreviewButton = By.id("cursor_button_giphy");

    protected static final By idCloseImageBtn = By.id("gtv__single_image_message__close");

    public static final By xpathDismissUpdateButton = By.xpath("//*[@value='Dismiss']");

    private static final By idChatheadNotification = By.id("va_message_notification_chathead__label_viewanimator");

    public static final int DRIVER_CREATION_RETRIES_COUNT = 2;

    protected static final Logger log = ZetaLogger.getLog(CommonUtils.class.getSimpleName());

    private static final By xpathInternetIndicator =
            By.xpath("//*[@id='civ__connectivity_indicator' and //*[@value='NO INTERNET']]");

    private static Function<String, String> xpathStrAlertButtonByCaption = caption ->
            String.format("//*[starts-with(@id, 'button') and @value='%s']", caption);

    @Override
    protected ZetaAndroidDriver getDriver() throws Exception {
        try {
            return (ZetaAndroidDriver) super.getDriver();
        } catch (ExecutionException e) {
            if ((e.getCause() instanceof java.util.concurrent.TimeoutException) ||
                    ((e.getCause() instanceof WebDriverException) &&
                            (e.getCause().getCause() instanceof java.util.concurrent.TimeoutException))) {
                throw new java.util.concurrent.TimeoutException((AppiumServer.getInstance().getLog()
                        .orElse("Appium log is empty")) + "\n" + ExceptionUtils.getStackTrace(e));
            } else {
                throw e;
            }
        }
    }

    /**
     * I expect this driver to be stateless
     */
    private static final UIAutomatorDriver UI_AUTOMATOR_DRIVER = new UIAutomatorDriver();

    protected UIAutomatorDriver getUIAutomatorDriver() throws Exception {
        return UI_AUTOMATOR_DRIVER;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Future<ZetaAndroidDriver> getLazyDriver() {
        return (Future<ZetaAndroidDriver>) super.getLazyDriver();
    }

    @Override
    protected long getDriverInitializationTimeout() {
        return (ZetaAndroidDriver.MAX_COMMAND_DURATION_MILLIS + AppiumServer.RESTART_TIMEOUT_MILLIS)
                * DRIVER_CREATION_RETRIES_COUNT;
    }

    public AndroidPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void hideKeyboard() throws Exception {
        this.getDriver().hideKeyboard();
    }

    public void pressKeyboardSendButton() throws Exception {
        tapByCoordinates(94, 96);
    }

    /**
     * Navigates back by BACK button
     *
     * @throws Exception
     */
    public void navigateBack() throws Exception {
        AndroidCommonUtils.tapBackButton();
        // Wait for animation
        Thread.sleep(1000);
    }

    public void rotateLandscape() throws Exception {
        this.getDriver().rotate(ScreenOrientation.LANDSCAPE);
    }

    public void rotatePortrait() throws Exception {
        this.getDriver().rotate(ScreenOrientation.PORTRAIT);
    }

    public void dialogsPagesSwipeUp(int durationMilliseconds) throws Exception {
        swipeByCoordinates(durationMilliseconds, 50, 80, 50, 30);
    }

    public void dialogsPagesSwipeDown(int durationMilliseconds)
            throws Exception {
        swipeByCoordinates(durationMilliseconds, 50, 30, 50, 80);
    }

    public void swipeByCoordinates(int durationMilliseconds,
                                   int widthStartPercent, int heightStartPercent, int widthEndPercent,
                                   int heightEndPercent) throws Exception {
        final Dimension screenDimension = getDriver().manage().window().getSize();
        this.getDriver().swipe(screenDimension.width * widthStartPercent / 100,
                screenDimension.height * heightStartPercent / 100,
                screenDimension.width * widthEndPercent / 100,
                screenDimension.height * heightEndPercent / 100,
                durationMilliseconds);
    }

    public static final int SWIPE_DEFAULT_PERCENTAGE_START = 10;
    public static final int SWIPE_DEFAULT_PERCENTAGE_END = 90;
    public static final int SWIPE_DEFAULT_PERCENTAGE = 50;

    /**
     * Swipe from x = 90% of width to x = 10% of width. y = height/2
     */
    public void swipeRightCoordinates(int durationMilliseconds)
            throws Exception {
        swipeRightCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE);
    }

    /**
     * Swipe from x = 10% of width to x = 90% of width. y = heightPercent
     */
    public void swipeRightCoordinates(int durationMilliseconds,
                                      int heightPercent) throws Exception {
        swipeByCoordinates(durationMilliseconds,
                SWIPE_DEFAULT_PERCENTAGE_START, heightPercent,
                SWIPE_DEFAULT_PERCENTAGE_END, heightPercent);
    }

    /**
     * Swipe from x = 90% of width to x = 10% of width. y = height/2
     */
    public void swipeLeftCoordinates(int durationMilliseconds) throws Exception {
        swipeLeftCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE);
    }

    /**
     * Swipe from x = 90% of width to x = 10% of width. y = heightPercent
     */
    public void swipeLeftCoordinates(int durationMilliseconds, int heightPercent)
            throws Exception {
        swipeByCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE_END,
                heightPercent, SWIPE_DEFAULT_PERCENTAGE_START, heightPercent);
    }

    /**
     * Swipe from y = 90% of height to y = 10% of height. x = width/2
     */
    public void swipeUpCoordinates(int durationMilliseconds) throws Exception {
        swipeUpCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE);
    }

    /**
     * Swipe from y = 90% of height to y = 10% of height. x = widthPercent
     */
    public void swipeUpCoordinates(int durationMilliseconds, int widthPercent)
            throws Exception {
        swipeByCoordinates(durationMilliseconds, widthPercent,
                SWIPE_DEFAULT_PERCENTAGE_END, widthPercent,
                SWIPE_DEFAULT_PERCENTAGE_START);
    }

    /**
     * Swipe from y = 10% of height to y = 90% of height. x = width/2
     */
    public void swipeDownCoordinates(int durationMilliseconds) throws Exception {
        swipeDownCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE);
    }

    /**
     * Swipe from y = 10% of height to y = 90% of height. x = widthPercent
     */
    public void swipeDownCoordinates(int durationMilliseconds, int widthPercent) throws Exception {
        swipeByCoordinates(durationMilliseconds, widthPercent,
                SWIPE_DEFAULT_PERCENTAGE_START, widthPercent,
                SWIPE_DEFAULT_PERCENTAGE_END);
    }

    public void tapByCoordinates(int widthPercent, int heightPercent) throws Exception {
        int x = getDriver().manage().window().getSize().getWidth() * widthPercent / 100;
        int y = getDriver().manage().window().getSize().getHeight() * heightPercent / 100;
        AndroidCommonUtils.genericScreenTap(x, y);
    }

    public void tapOnCenterOfScreen() throws Exception {
        tapByCoordinates(50, 50);
    }

    public void tapChatheadNotification() throws Exception {
        waitForChatheadNotification().orElseThrow(() ->
                new IllegalStateException(String.format("Chathead notification has not been shown after %s seconds",
                        CHATHEAD_VISIBILITY_TIMEOUT_MS / 1000))
        ).click();
    }

    private static final long CHATHEAD_VISIBILITY_TIMEOUT_MS = 10000;

    public Optional<WebElement> waitForChatheadNotification() throws Exception {
        final By locator = idChatheadNotification;
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted < CHATHEAD_VISIBILITY_TIMEOUT_MS) {
            final Optional<WebElement> chatheadNotification = getElementIfDisplayed(locator, 1);
            if (chatheadNotification.isPresent()) {
                if (chatheadNotification.get().getSize().width > 0) {
                    return chatheadNotification;
                }
            }
            Thread.sleep(500);
        }
        return Optional.empty();
    }

    public boolean waitUntilChatheadNotificationInvisible() throws Exception {
        final By locator = idChatheadNotification;
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted < CHATHEAD_VISIBILITY_TIMEOUT_MS) {
            final Optional<WebElement> chatheadNotification = getElementIfDisplayed(locator, 1);
            if (chatheadNotification.isPresent()) {
                if (chatheadNotification.get().getSize().width == 0) {
                    return true;
                }
                Thread.sleep(500);
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean isAlertMessageVisible(String expectedMsg) throws Exception {
        final By locator = By.xpath(xpathStrAlertMessageByText.apply(expectedMsg));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 15);
    }

    /**
     * Check whether Element A is below Element B
     * The distance percentage(based on screen hight) between B.Y and (A.Y + A.Height)
     * should small than <locationDifferencePercentage>
     *
     * @param elementA                     The element in the relative "bottom" position
     * @param elementB                     The element in the relative "top" position
     * @param locationDifferencePercentage [0, n), n belong any integer,
     *                                     if equal 0, means A is below B, and A is close to B
     *                                     if in (0,1), means A is below B, means the distance percentage
     *                                     if equal [1, ...), means cannot see A and B in the same view.
     * @return true if the condition success
     * @throws Exception
     */
    public boolean isElementABelowElementB(WebElement elementA, WebElement elementB, double locationDifferencePercentage)
            throws Exception {
        int difference = elementA.getLocation().getY() - elementB.getSize().getHeight() - elementB.getLocation().getY();
        return difference >= 0
                && difference / getDriver().manage().window().getSize().getHeight() <= locationDifferencePercentage;
    }

    public void tapAlertButton(String caption) throws Exception {
        final By locator = By.xpath(xpathStrAlertButtonByCaption.apply(caption));
        getElement(locator).click();
    }

    public boolean isAlertTitleVisible(String expectedMsg) throws Exception {
        final By locator = By.xpath(xpathStrAlertTitleByTextPart.apply(expectedMsg));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public DefaultArtifactVersion getOSVersion() throws Exception {
        return getDriver().getOSVersion();
    }

    public boolean waitUntilNoInternetBarVisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathInternetIndicator, timeoutSeconds);
    }

    public boolean waitUntilNoInternetBarInvisible(int timeoutSeconds) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathInternetIndicator, timeoutSeconds);
    }

    /**
     * Long tap on an element for several seconds, then move it to the end position
     *
     * @param longTapElement            the start element
     * @param getEndElement             the function to retrieve end element durint Runtime
     * @param swipeDurationMilliseconds the duration of swipe
     * @param tapDurationMilliseconds   the duration of long tap
     * @throws Exception
     */
    public void longTapAndSwipe(WebElement longTapElement,
                                FunctionalInterfaces.ISupplierWithException<WebElement> getEndElement,
                                int swipeDurationMilliseconds, int tapDurationMilliseconds) throws Exception {
        longTapAndSwipe(longTapElement, getEndElement, swipeDurationMilliseconds, tapDurationMilliseconds,
                Optional.empty());
    }

    /**
     * Long tap on an element for several seconds, then move it to the end position
     * However the end position could be an element which will be visible after you tap on the start element,
     * Thus the end element will be passed by FunctionalInterface, will be called between "long tap" and "swipe"
     *
     * @param longTapElement            the start element
     * @param getEndElement             the function to retrieve end element durint Runtime
     * @param swipeDurationMilliseconds the duration of swipe
     * @param tapDurationMilliseconds   the duration of long tap
     * @param callback                  the callback during long tap, cannbe null if no callback
     * @throws Exception
     */
    public void longTapAndSwipe(WebElement longTapElement,
                                FunctionalInterfaces.ISupplierWithException<WebElement> getEndElement,
                                int swipeDurationMilliseconds, int tapDurationMilliseconds,
                                Optional<FunctionalInterfaces.ISupplierWithException> callback) throws Exception {
        final Point fromPoint = longTapElement.getLocation();
        final Dimension fromElementSize = longTapElement.getSize();

        final int startX = fromPoint.x + fromElementSize.width / 2;
        final int startY = fromPoint.y + fromElementSize.height / 2;

        touchAndSwipe(startX, startY, getEndElement, swipeDurationMilliseconds, tapDurationMilliseconds, callback);
    }

    /**
     * Touch is used for touch on an element for several seconds, then move it to the end position
     * However the end position could be an element which be presented after you touch,
     * Thus the end element should be located after long tap.
     *
     * @param startX                    start X
     * @param startY                    start Y
     * @param getEndElement             the functional interface to get end element, called after long tap
     * @param swipeDurationMilliseconds swipe duration
     * @param tapDurationMilliseconds   tap duration
     * @param callback                  callback during the long tap, can be null if nothing want to to do
     * @throws Exception
     */
    private void touchAndSwipe(int startX, int startY, FunctionalInterfaces.ISupplierWithException<WebElement> getEndElement,
                               int swipeDurationMilliseconds, int tapDurationMilliseconds,
                               Optional<FunctionalInterfaces.ISupplierWithException> callback) throws Exception {
        int duration = 1;
        if (swipeDurationMilliseconds > ZetaAndroidDriver.SWIPE_STEP_DURATION_MILLISECONDS) {
            duration = (swipeDurationMilliseconds % ZetaAndroidDriver.SWIPE_STEP_DURATION_MILLISECONDS == 0)
                    ? (swipeDurationMilliseconds / ZetaAndroidDriver.SWIPE_STEP_DURATION_MILLISECONDS)
                    : (swipeDurationMilliseconds / ZetaAndroidDriver.SWIPE_STEP_DURATION_MILLISECONDS + 1);
        }
        int current = 1;
        final TouchActions ta = new TouchActions(getDriver());
        ta.down(startX, startY).perform();

        try {
            if (callback.isPresent()) {
                callback.get().call();
            }

            Thread.sleep(tapDurationMilliseconds);
            WebElement element = getEndElement.call();
            Dimension dimension = element.getSize();
            Point point = element.getLocation();

            final int endx = point.x + dimension.width / 2;
            final int endy = point.y + dimension.height / 2;

            do {
                Thread.sleep(ZetaAndroidDriver.SWIPE_STEP_DURATION_MILLISECONDS);
                ta.move(getNextCoord(startX, endx, current, duration),
                        getNextCoord(startY, endy, current, duration)).perform();
                current++;
            } while (current <= duration);
            ta.up(endx, endy).perform();
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    private static int getNextCoord(double startC, double endC, double current, double duration) {
        return (int) Math.round(startC + (endC - startC) / duration * current);
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

    public boolean isLogContain(ListenerType listenerType, String expectedString) throws Exception {
        final AndroidLogListener dstListener = AndroidLogListener.getInstance(listenerType);
        return dstListener.getStdOut().matches("(?s).*\\b" + Pattern.quote(expectedString) + "\\b.*");
    }

    public void logUIAutomatorSource() throws Exception {
        log.info(UIAutomatorDriver.getUIDump());
    }
}
