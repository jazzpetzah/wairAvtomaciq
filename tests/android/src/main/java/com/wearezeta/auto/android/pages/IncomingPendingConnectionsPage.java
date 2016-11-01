package com.wearezeta.auto.android.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class IncomingPendingConnectionsPage extends AndroidPage {
    private static final By idSendConnectionRequestButton = By.id("zb__send_connect_request__connect_button");

    private static final Function<String, String> xpathStrConnectToHeaderByText = text -> String.format
            ("//*[@id='taet__participants__header' and contains(@value, '%s')]", text);

    public static final String idStrConnectRequestAccept = "zb__connect_request__accept_button";
    public static final By idConnectRequestAccept = By.id(idStrConnectRequestAccept);
    private static final Function<String, String> xpathStrAcceptButtonByHeaderText = text -> String.format
            ("//*[@id='ll__connect_request__main_container' and .%s]//*[@id='%s']", xpathStrConnectToHeaderByText.apply(text)
                    , idStrConnectRequestAccept);

    private static final String idStrConnectRequestIgnore = "zb__connect_request__ignore_button";
    private static final By idConnectRequestIgnore = By.id(idStrConnectRequestIgnore);

    private static final Function<String, String> xpathStrIgnoreButtonByHeaderText = text -> String.format
            ("//*[@id='ll__connect_request__main_container' and .%s]//*[@id='%s']", xpathStrConnectToHeaderByText.apply(text)
                    , idStrConnectRequestIgnore);

    private static final Function<String, String> xpathStrUserDetailsLeftButtonByLabel = label -> String.format
            ("//*[@id='ttv__participants__left_label' and @value='%s']", label);

    private static final By idConnectToCharCounter = By.id("ttv__send_connect_request__connect_button__character_counter");

    private static final By xpathCloseButton = By.xpath
            ("//*[@id='fl__conversation_list__profile_overlay']//*[@id='gtv__participants__close']");

    private static final Function<String, String> xpathStrConnectMenuItemByText = text -> String.format
            ("//*[@id='ttv__settings_box__item' and @value='%s']/parent::*//*[@id='fl_options_menu_button']", text
                    .toUpperCase());

    private static final By xpathConfirmBlockButton = By.xpath("//*[@id='positive' and @value='BLOCK']");

    private static final By idPendingUserAvatar = By.id("iaiv__pending_connect");

    public IncomingPendingConnectionsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickEllipsisButton() throws Exception {
        getElement(OtherUserPersonalInfoPage.xpathRightActionButton).click();
    }

    public void tapBlockBtn() throws Exception {
        final By blockButtonLocator = By.xpath(xpathStrConnectMenuItemByText.apply("Block"));
        final WebElement blockButton = getElement(blockButtonLocator, "Block button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), blockButton)) {
            throw new IllegalStateException("Block button is not clickable");
        }
        blockButton.click();
    }

    public void tapUnblockConnectionButton() throws Exception {
        getElement(OtherUserPersonalInfoPage.xpathUnblockButton).click();
    }

    public boolean isConnectToHeaderVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrConnectToHeaderByText.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    private final static int MAX_USERS_TO_SCROLL = 5;

    public void scrollToInboxContact(String contactName) throws Exception {
        scrollToInboxContact(contactName, MAX_USERS_TO_SCROLL);
    }

    public void scrollToInboxContact(String contactName, final int maxUsersToScroll) throws Exception {
        final By locator = By.xpath(xpathStrAcceptButtonByHeaderText.apply(contactName));
        int ntry = 1;
        final int SCROLL_POS_START = 48;
        final int SCROLL_POS_END = 70;
        final int maxScrolls = maxUsersToScroll * (100 / (SCROLL_POS_END - SCROLL_POS_START) + 1);
        do {
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
                return;
            }
            this.swipeByCoordinates(500, 50, SCROLL_POS_END, 50, SCROLL_POS_START);
            ntry++;
        } while (ntry <= maxScrolls);
        throw new RuntimeException(String.format("Failed to find user %s in the inbox after scrolling %s times!",
                contactName, maxScrolls));
    }

    public void tapAcceptConnectButton() throws Exception {
        getElement(idConnectRequestAccept).click();
    }

    public void tapAcceptConnectionButton(String contactName) throws Exception {
        scrollToInboxContact(contactName);
        final By locator = By.xpath(xpathStrAcceptButtonByHeaderText.apply(contactName));
        getElement(locator, String.format("Connect button for user '%s' is not visible", contactName)).click();
    }

    public void tapIgnoreConnectionButton(String contactName) throws Exception {
        scrollToInboxContact(contactName);
        final By locator = By.xpath(xpathStrIgnoreButtonByHeaderText.apply(contactName));
        getElement(locator, String.format("Ignore button for user '%s' is not visible", contactName)).click();
    }

    public void tapIgnoreButton() throws Exception {
        getElement(idConnectRequestIgnore, "Ignore button is not visible").click();
    }

    public boolean isIgnoreConnectButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idConnectRequestIgnore);
    }

    public boolean isPending() throws Exception {
        return selectVisibleElements(idPendingUserAvatar).size() > 0;
    }

    public void tapLeftButtonByLabel(String label) throws Exception {
        getElement(By.xpath(xpathStrUserDetailsLeftButtonByLabel.apply(label)), label + " button is not visible").click();
    }

    public void tapConnectButton() throws Exception {
        getElement(idSendConnectionRequestButton, "Connect button is not visible").click();
    }

    public void waitUntilIgnoreButtonIsClickable() throws Exception {
        if (!DriverUtils.waitUntilElementClickable(getDriver(), getElement(idConnectRequestIgnore))) {
            throw new IllegalStateException("Ignore button is not clickable");
        }
    }

    public boolean getConnectButtonState() throws Exception {
        String state = getElement(idSendConnectionRequestButton).getAttribute("enabled");
        return Boolean.parseBoolean(state);
    }

    public int getCharCounterValue() throws Exception {
        return Integer.parseInt(getElement(idConnectToCharCounter).getText());
    }

    public void tapCloseButton() throws Exception {
        getElement(xpathCloseButton).click();
    }

    public void tapConfirmBlockButton() throws Exception {
        getElement(xpathConfirmBlockButton).click();
    }
}
