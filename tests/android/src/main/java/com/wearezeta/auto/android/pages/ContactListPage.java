package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;

import com.wearezeta.auto.android.pages.registration.EmailSignInPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class ContactListPage extends AndroidPage {

    private static final String LOADING_CONVERSATION_NAME = "…";

    private static final String idStrConversationListFrame = "pfac__conversation_list";

    private static final String xpathStrConvoListNames =
            String.format("//*[@id='%s']/*/*/*[boolean(string(@value))]", idStrConversationListFrame);
    private static final By xpathContactListNames = By.xpath(xpathStrConvoListNames);

    private static final By xpathLoadingContactListItem =
            By.xpath(String.format("%s[contains(@value, '%s')]", xpathStrConvoListNames, LOADING_CONVERSATION_NAME));

    public static final Function<String, String> xpathStrContactByName = name ->
            String.format("%s[@value='%s' and @shown='true']", xpathStrConvoListNames, name);

    private static final Function<Integer, String> xpathStrContactByIndex = index ->
            String.format("(%s)[%s]", xpathStrConvoListNames, index);

    private static final By xpathLastContact = By.xpath(String.format("(%s)[last()]", xpathStrConvoListNames));

    public static final Function<String, String> xpathStrMutedIconByConvoName = convoName -> String
            .format("%s/parent::*//*[@id='tv_conv_list_voice_muted']", xpathStrContactByName.apply(convoName));

    private static final Function<String, String> xpathStrPlayPauseButtonByConvoName = convoName -> String
            .format("%s/parent::*//*[@id='tv_conv_list_media_player']", xpathStrContactByName.apply(convoName));

    private static final Function<String, String> xpathStrMissedCallNotificationByConvoName = convoName -> String
            .format("%s/parent::*//*[@id='sci__list__missed_call']", xpathStrContactByName.apply(convoName));

    private static final By idConversationListFrame = By.id(idStrConversationListFrame);

    private static final String xpathStrNonEmptyContacts =
            String.format("%s[@value and string-length(@value) > 0 and not(starts-with(@value, '%s'))]",
                    xpathStrConvoListNames, LOADING_CONVERSATION_NAME);

    private static final Function<Integer, String> xpathStrNonEmptyContactByIdx = idx -> String
            .format("(%s)[%d]", xpathStrNonEmptyContacts, idx);

    private static final By idListSettingsButton = By.id("gtv__list_actions__settings");

    public static final By idListActionsAvatar = By.id("gtv__list_actions__avatar");

    private static final By idConversationListHintContainer = By.id("ll__conversation_list__hint_container");

    private static final By xpathConfirmDeleteConversationButton = By.xpath("//*[@id='positive' and @value='DELETE']");

    private static final By xpathLeaveCheckbox = By.xpath("//*[@id='gtv__checkbox_icon']");

    private static final Function<String, String> xpathStrConvoSettingsMenuItemByName = name -> String
            .format("//*[@id='ttv__settings_box__item' and @value='%s']" +
                    "/parent::*//*[@id='fl_options_menu_button']", name.toUpperCase());

    private static final String xpathSpinnerConversationsListLoadingIndicator =
            "//*[@id='liv__conversations__loading_indicator']/*";

    private static final By idThreeDotsOptionMenuButton = By.id("v__row_conversation__menu_indicator__second_dot");

    private static final Logger log = ZetaLogger.getLog(ContactListPage.class.getSimpleName());

    public ContactListPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public String getFirstVisibleConversationName() throws Exception {
        final int maxTries = 20;
        final long millisecondsDelay = 20000;
        int ntry = 1;
        do {
            try {
                final int itemsCount = getDriver().findElements(By.xpath(xpathStrNonEmptyContacts)).size();
                for (int i = 1; i <= itemsCount; i++) {
                    final By locator = By.xpath(xpathStrNonEmptyContactByIdx.apply(i));
                    if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
                        final WebElement elem = getDriver().findElement(locator);
                        final String name = elem.getText();
                        if (name != null && name.length() > 0) {
                            if (DriverUtils.waitUntilElementClickable(getDriver(), elem)) {
                                return name;
                            } else {
                                break;
                            }
                        }
                    }
                }
            } catch (WebDriverException e) {
                e.printStackTrace();
                // Ignore silently
            }
            Thread.sleep(millisecondsDelay);
            ntry++;
        } while (ntry <= maxTries);
        throw new AssertionError("There are no visible conversations in the list after "
                + millisecondsDelay * maxTries / 1000 + " seconds");
    }

    public void tapOnName(final String name) throws Exception {
        findInContactList(name).orElseThrow(
                () -> new IllegalStateException(String.format(
                        "The conversation '%s' does not exist in the conversations list", name))
        ).click();
    }

    public void doLongSwipeUp() throws Exception {
        // FIXME: There is a bug in Android when swipe up does not work if there are no items in the list
        DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idConversationListFrame), 1000, 15, 20, 15, -80);
    }

    public Optional<WebElement> findInContactList(String name) throws Exception {
        return getElementIfDisplayed(By.xpath(xpathStrContactByName.apply(name)));
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

    public boolean isContactExists(String name) throws Exception {
        return findInContactList(name).isPresent();
    }

    public boolean waitUntilContactDisappears(String name) throws Exception {
        final By nameLocator = By.xpath(xpathStrContactByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameLocator);
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

    private static final int CONTACT_LIST_LOAD_TIMEOUT_SECONDS = 60;
    private static final int CONVERSATIONS_INFO_LOAD_TIMEOUT_SECONDS = CONTACT_LIST_LOAD_TIMEOUT_SECONDS * 2;

    public void verifyContactListIsFullyLoaded() throws Exception {
        Thread.sleep(1000);
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
                EmailSignInPage.idLoginButton,
                CONTACT_LIST_LOAD_TIMEOUT_SECONDS)) {
            throw new IllegalStateException(
                    String.format(
                            "It seems that conversations list has not been loaded within %s seconds (login button is still visible)",
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
                    .format("It seems that conversations list has not been loaded within %s seconds (the spinner is still visible)",
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
        for (int i = getElements(xpathContactListNames).size(); i >= 1; i--) {
            final By locator = By.xpath(xpathStrContactByIndex.apply(i));
            final Optional<WebElement> contactEl = getElementIfDisplayed(locator);
            try {
                if (contactEl.isPresent() && !contactEl.get().getText().equals(LOADING_CONVERSATION_NAME)) {
                    return true;
                }
            } catch (NoSuchElementException e) {
                // pass silently
            }
        }
        final Optional<WebElement> lastEl = getElementIfDisplayed(xpathLastContact, CONTACT_LIST_LOAD_TIMEOUT_SECONDS);
        return lastEl.isPresent() && !lastEl.get().getText().equals(LOADING_CONVERSATION_NAME);
    }

    public boolean isNoConversationsVisible() throws Exception {
        Assert.assertTrue("Conversations list frame is not visible",
                DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idConversationListFrame));
        for (int i = getElements(xpathContactListNames).size(); i >= 1; i--) {
            final By locator = By.xpath(xpathStrContactByIndex.apply(i));
            if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), locator)) {
                return false;
            }
        }
        return true;
    }

    public void selectConvoSettingsMenuItem(String itemName) throws Exception {
        final By locator = By.xpath(xpathStrConvoSettingsMenuItemByName.apply(itemName));
        getElement(locator, String
                .format("Conversation menu item '%s' could not be found on the current screen", itemName)).click();
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

    public void confirmDeleteConversationAlert() throws Exception {
        getElement(xpathConfirmDeleteConversationButton).click();
    }

    public void checkLeaveWhileDeleteCheckbox() throws Exception {
        getElement(xpathLeaveCheckbox).click();
    }

    public boolean isConvSettingsMenuItemVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrConvoSettingsMenuItemByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isLeaveCheckBoxVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathLeaveCheckbox);
    }

    public boolean isThreeDotButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idThreeDotsOptionMenuButton);
    }

    public void tapThreeDotOptionMenuButton() throws Exception {
        getElement(idThreeDotsOptionMenuButton).click();
    }
}
