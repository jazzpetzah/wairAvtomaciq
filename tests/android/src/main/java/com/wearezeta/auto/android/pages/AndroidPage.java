package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.uiautomation.UIAutomatorDriver;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.openqa.selenium.*;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

public abstract class AndroidPage extends BasePage {
    private static final Function<String, String> xpathStrAlertMessageByText =
            text -> String.format("//*[@id='message' and contains(@value, '%s')]", text);

    private static final Function<String, String>  xpathStrAlertTitleByTextPart =
            text -> String.format("//*[@id='alertTitle' and contains(@value, '%s')]", text);

    protected static final By idGiphyPreviewButton = By.id("cursor_button_giphy");

    protected static final By idCloseImageBtn = By.id("gtv__single_image_message__close");

    public static final By xpathDismissUpdateButton = By.xpath("//*[@value='Dismiss']");

    private static final By idChatheadNotification = By.id("va_message_notification_chathead__label_viewanimator");

    public static final long DRIVER_INIT_TIMEOUT_MILLIS = ZetaAndroidDriver.MAX_COMMAND_DURATION_MILLIS; // milliseconds

    protected static final Logger log = ZetaLogger.getLog(CommonUtils.class.getSimpleName());

    protected static final By idPager = By.id("conversation_pager");

    private static Function<String, String> xpathStrAlertButtonByCaption = caption ->
            String.format("//*[starts-with(@id, 'button') and @value='%s']", caption);

    @Override
    protected ZetaAndroidDriver getDriver() throws Exception {
        return (ZetaAndroidDriver) super.getDriver();
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
        return DRIVER_INIT_TIMEOUT_MILLIS;
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
        // AndroidCommonUtils.rotateLanscape();
        this.getDriver().rotate(ScreenOrientation.LANDSCAPE);
    }

    public void rotatePortrait() throws Exception {
        // AndroidCommonUtils.rotatePortrait();
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
        final Dimension screenDimension = getDriver().manage().window()
                .getSize();
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
    public void swipeDownCoordinates(int durationMilliseconds, int widthPercent)
            throws Exception {
        swipeByCoordinates(durationMilliseconds, widthPercent,
                SWIPE_DEFAULT_PERCENTAGE_START, widthPercent,
                SWIPE_DEFAULT_PERCENTAGE_END);
    }

    public void tapByCoordinates(int widthPercent, int heightPercent)
            throws Exception {
        int x = getDriver().manage().window().getSize().getWidth()
                * widthPercent / 100;
        int y = getDriver().manage().window().getSize().getHeight()
                * heightPercent / 100;
        AndroidCommonUtils.genericScreenTap(x, y);
    }

    public void tapOnCenterOfScreen() throws Exception {
        tapByCoordinates(50, 50);
    }

    public void tapChatheadNotification() throws Exception {
        getElement(idChatheadNotification).click();
    }

    private static final long CHATHEAD_VISIBILITY_TIMEOUT = 10000; //milliseconds

    public boolean waitUntilChatheadNotificationVisible() throws Exception {
        final By locator = idChatheadNotification;
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted < CHATHEAD_VISIBILITY_TIMEOUT) {
            final Optional<WebElement> chatheadNotification = getElementIfDisplayed(locator, 1);
            if (chatheadNotification.isPresent()) {
                if (chatheadNotification.get().getSize().width > 0) {
                    return true;
                }
            }
            Thread.sleep(500);
        }
        return false;
    }

    public boolean waitUntilChatheadNotificationInvisible() throws Exception {
        final By locator = idChatheadNotification;
        final long millisecondsStarted = System.currentTimeMillis();
        while (System.currentTimeMillis() - millisecondsStarted < CHATHEAD_VISIBILITY_TIMEOUT) {
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
     * @param elementA The element in the relative "bottom" position
     * @param elementB The element in the relative "top" position
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
}
