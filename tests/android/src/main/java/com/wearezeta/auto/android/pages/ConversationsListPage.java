package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.android.pages.registration.EmailSignInPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.Timedelta;
import io.appium.java_client.android.AndroidElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConversationsListPage extends AndroidPage {

    private static final String LOADING_CONVERSATION_NAME = "…";

    private static final String idStrConversationListFrame = "pfac__conversation_list";

    private static final String xpathStrConvoListNames =
            String.format("//*[@id='%s']/*/*/*[boolean(string(@value))]", idStrConversationListFrame);

    private static final By xpathLoadingContactListItem =
            By.xpath(String.format("%s[contains(@value, '%s')]", xpathStrConvoListNames, LOADING_CONVERSATION_NAME));

    public static final Function<String, String> xpathStrContactByName = name ->
            String.format("%s[@value='%s' and @shown='true']", xpathStrConvoListNames, name);

    public static final Function<String, String> xpathStrContactByExpr = expr ->
            String.format("%s[%s and @shown='true']", xpathStrConvoListNames, expr);

    private static final By xpathValidContact = By.xpath(String.format("%s[not(contains(@value,'%s'))]",
            xpathStrConvoListNames, LOADING_CONVERSATION_NAME));

    public static final Function<String, String> xpathStrMutedIconByConvoName = convoName -> String
            .format("%s/parent::*//*[@id='tv_conv_list_voice_muted']", xpathStrContactByName.apply(convoName));

    private static final Function<String, String> xpathStrPlayPauseButtonByConvoName = convoName -> String
            .format("%s/parent::*//*[@id='tv_conv_list_media_player']", xpathStrContactByName.apply(convoName));

    private static final Function<String, String> xpathStrMissedCallNotificationByConvoName = convoName -> String
            .format("%s/parent::*//*[@id='gtv__list__call_indicator']", xpathStrContactByName.apply(convoName));

    private static final By idConversationListFrame = By.id(idStrConversationListFrame);

    private static final By idListSettingsButton = By.id("gtv__list_actions__settings");

    public static final By idListActionsAvatar = By.id("gtv__list_actions__avatar");

    private static final By idConversationListHintContainer = By.id("ll__conversation_list__hint_container");

    private static final String xpathSpinnerConversationsListLoadingIndicator =
            "//*[@id='liv__conversations__loading_indicator']/*";

    private static final By idThreeDotsOptionMenuButton = By.id("v__row_conversation__menu_indicator__second_dot");

    private static final Logger log = ZetaLogger.getLog(ConversationsListPage.class.getSimpleName());

    public ConversationsListPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public BufferedImage getNewDeviceIndicatorState() throws Exception {
        return this.getElementScreenshot(getElement(idListSettingsButton)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of new device indicator")
        );
    }

    public void tapOnName(final String name) throws Exception {
        findConversationInList(name).orElseThrow(
                () -> new IllegalStateException(String.format(
                        "The conversation '%s' does not exist in the conversations list", name))
        ).click();
    }

    public void longTapOnName(final String name, int durationMilliseconds) throws Exception {
        WebElement webElement = findConversationInList(name).orElseThrow(
                () -> new IllegalStateException(String.format(
                        "The conversation '%s' does not exist in the conversations list", name))
        );
        getDriver().longTap(200, ((AndroidElement)webElement).getCenter().y, durationMilliseconds);
    }

    public void doLongSwipeUp() throws Exception {
        // FIXME: There is a bug in Android when swipe up does not work if there are no items in the list
        DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idConversationListFrame), 1000, 15, 20, 15, -80);
    }

    public Optional<WebElement> findConversationInList(String name) throws Exception {
        return getElementIfDisplayed(By.xpath(xpathStrContactByName.apply(name)));
    }

    public Optional<WebElement> findConversationInList(String name, Timedelta timeout) throws Exception {
        return getElementIfDisplayed(By.xpath(xpathStrContactByName.apply(name)), timeout);
    }

    public void swipeRightOnConversation(int durationMilliseconds, String name)
            throws Exception {
        final By locator = By.xpath(xpathStrContactByName.apply(name));
        DriverUtils.swipeRight(this.getDriver(),
                this.getDriver().findElement(locator), durationMilliseconds,
                20, 50, 90, 50);
    }

    public void swipeShortRightOnConversation(int durationMilliseconds, String name)
            throws Exception {
        final By locator = By.xpath(xpathStrContactByName.apply(name));
        DriverUtils.swipeRight(this.getDriver(),
                this.getDriver().findElement(locator), durationMilliseconds,
                20, 50, 50, 50);
    }

    public boolean isContactMuted(String name) throws Exception {
        final By locator = By.xpath(xpathStrMutedIconByConvoName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator);
    }

    public boolean waitUntilContactNotMuted(String name) throws Exception {
        final By locator = By.xpath(xpathStrMutedIconByConvoName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }

    public void tapListActionsAvatar() throws Exception {
        getElement(idListActionsAvatar).click();
    }

    public boolean isConversationVisible(String name, Timedelta timeout) throws Exception {
        return findConversationInList(name, timeout).isPresent();
    }

    public boolean isConversationVisible(String name) throws Exception {
        return findConversationInList(name).isPresent();
    }

    public boolean waitUntilConversationDisappears(String name) throws Exception {
        final By locator = By.xpath(xpathStrContactByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean waitUntilConversationDisappears(String name, Timedelta timeout) throws Exception {
        final By locator = By.xpath(xpathStrContactByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, timeout.asSeconds());
    }

    public boolean isPlayPauseMediaButtonVisible(String convoName) throws Exception {
        final By locator = By.xpath(xpathStrPlayPauseButtonByConvoName.apply(convoName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isContactsBannerVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idConversationListHintContainer);
    }

    public boolean isContactsBannerNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idConversationListHintContainer);
    }

    private static final int CONTACT_LIST_LOAD_TIMEOUT_SECONDS = 15;
    private static final int CONVERSATIONS_INFO_LOAD_TIMEOUT_SECONDS = CONTACT_LIST_LOAD_TIMEOUT_SECONDS * 2;

    public void verifyContactListIsFullyLoaded() throws Exception {
        Thread.sleep(1000);
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
                EmailSignInPage.idLoginButton,
                CONTACT_LIST_LOAD_TIMEOUT_SECONDS)) {
            throw new IllegalStateException(
                    String.format(
                            "It seems that conversations list has not been loaded within %s seconds (login button is " +
                                    "still visible)",
                            CONTACT_LIST_LOAD_TIMEOUT_SECONDS));
        }

        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idListSettingsButton, 5)) {
            log.warn("List actions gear is not detected on top of conversations list");
        }

        final By spinnerConvoListLoadingProgressLocator = By.xpath(xpathSpinnerConversationsListLoadingIndicator);
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
                spinnerConvoListLoadingProgressLocator,
                CONTACT_LIST_LOAD_TIMEOUT_SECONDS / 2)) {
            log.warn(String
                    .format("It seems that conversations list has not been loaded within %s seconds (the spinner is " +
                                    "still visible)",
                            CONTACT_LIST_LOAD_TIMEOUT_SECONDS / 2));
        }

        if (!this.waitUntilConversationsInfoIsLoaded()) {
            throw new IllegalStateException(String.format(
                    "Not all conversations list items were loaded within %s seconds",
                    CONVERSATIONS_INFO_LOAD_TIMEOUT_SECONDS));
        }
    }

    private boolean waitUntilConversationsInfoIsLoaded() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                xpathLoadingContactListItem, CONVERSATIONS_INFO_LOAD_TIMEOUT_SECONDS);
    }

    public void tapListSettingsButton() throws Exception {
        getElement(idListSettingsButton).click();
    }

    public boolean isAnyConversationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathValidContact,
                CONTACT_LIST_LOAD_TIMEOUT_SECONDS);
    }

    public boolean isNoConversationsVisible() throws Exception {
        return !DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathValidContact, 3);
    }


    public boolean waitUntilMissedCallNotificationVisible(String convoName) throws Exception {
        final By locator = By.xpath(xpathStrMissedCallNotificationByConvoName.apply(convoName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilMissedCallNotificationInvisible(String convoName) throws Exception {
        final By locator = By.xpath(xpathStrMissedCallNotificationByConvoName.apply(convoName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public Optional<BufferedImage> getScreenshotOfPlayPauseButtonNextTo(String convoName) throws Exception {
        final By locator = By.xpath(xpathStrPlayPauseButtonByConvoName.apply(convoName));
        return this.getElementScreenshot(getElement(locator,
                String.format("PlayPause button is not visible next to the '%s' conversation item", convoName)));
    }

    public void tapPlayPauseMediaButton(String convoName) throws Exception {
        final By locator = By.xpath(xpathStrPlayPauseButtonByConvoName.apply(convoName));
        getElement(locator, String
                .format("PlayPause button is not visible next to the '%s' conversation item", convoName)).click();
    }

    public BufferedImage getMessageIndicatorScreenshot(String name) throws Exception {
        final BufferedImage fullScreen = this.takeScreenshot().orElseThrow(
                () -> new IllegalStateException("Cannot take a screenshot")
        );
        final By locator = By.xpath(xpathStrContactByName.apply(name));
        final WebElement el = getElement(locator);
        final Point elLocation = el.getLocation();
        final Dimension elSize = el.getSize();
        final int x = elLocation.x >= 0 ? elLocation.x : 0;
        final int y = elLocation.y >= 0 ? elLocation.y : 0;
        final int w = elSize.width <= fullScreen.getWidth() ? elSize.width / 6 : fullScreen.getWidth() / 6;
        final int h = elSize.height;
        return fullScreen.getSubimage(x, y, w, h);
    }

    public boolean isThreeDotButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idThreeDotsOptionMenuButton);
    }

    public void tapThreeDotOptionMenuButton() throws Exception {
        getElement(idThreeDotsOptionMenuButton).click();
    }

    public boolean isConversationItemExist(List<String> users) throws Exception {
        final String xpathExpr = String.join(" and ", users.stream().map(
                x -> String.format("contains(@value, '%s')", x)
        ).collect(Collectors.toList()));
        final By locator = By.xpath(xpathStrContactByExpr.apply(xpathExpr));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isConversationItemNotExist(List<String> users) throws Exception {
        final String xpathExpr = String.join(" and ", users.stream().map(
                x -> String.format("contains(@value, '%s')", x)
        ).collect(Collectors.toList()));
        final By locator = By.xpath(xpathStrContactByExpr.apply(xpathExpr));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public BufferedImage getConversationsListScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idConversationListFrame)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of Conversations List background")
        );
    }
}
