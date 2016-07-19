package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

public abstract class AbstractPickUserPage extends AndroidPage {

    private static final String idStrCreateOrOpenConversationButton = "zb__conversation_quick_menu__conversation_button";

    public static final By xpathSearchField = By.xpath("//*[@id='puet_pickuser__searchbox']");

    private static final By idListContainer = By.id("rv__pickuser__header_list_view");

    private static final By idCreateOrOpenConversationButton = By.id(idStrCreateOrOpenConversationButton);

    private static final By idPeoplePickerClearbtn = By.id("gtv_pickuser__clearbutton");

    public static final By idQuickMenuCameraButton = By.id("gtv__conversation_quick_menu__camera_button");

    public static final By idQuickMenuCallButton = By.id("gtv__conversation_quick_menu__call_button");

    public static final By idQuickMenuVideoCallButton = By.id("gtv__conversation_quick_menu__video_call_button");

    private static final By idError = By.id("ttv_pickuser__error_header");

    public static final By idPickUserConfirmationBtn = By.id("zb__pickuser__confirmation_button");

    private static final By xpathOpenConversationButton = By.xpath(String.format("//*[@id='%s' and @value='OPEN']",
            idStrCreateOrOpenConversationButton));

    private static final By xpathCreateConversationButton =
            By.xpath(String.format("//*[@id='%s' and @value='CREATE GROUP']", idStrCreateOrOpenConversationButton));

    private static final Function<String, String> xpathStrInvitationAlertItemByValue = value
            -> String.format("//*[starts-with(@id,'text') and @value='%s']", value);

    public static final Function<String, String> xpathStrSearchFieldByName = name -> String
            .format("//*[@id='puet_pickuser__searchbox' and @value='%s']", name);


    public AbstractPickUserPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void selectEmailOnAlert(String email) throws Exception {
        final By locator = By.xpath(xpathStrInvitationAlertItemByValue.apply(email));
        getElement(locator, String.format("Email address '%s' is not visible on the alert", email)).click();
    }

    public void tapPeopleSearch() throws Exception {
        getPickerEdit().click();
    }

    public void typeTextInPeopleSearch(String text) throws Exception {
        final WebElement pickerSearch = getPickerEdit();
        pickerSearch.click();
        pickerSearch.sendKeys(text);
    }

    public boolean isPeopleSearchTextEmpty() throws Exception {
        final By locator = By.xpath(xpathStrSearchFieldByName.apply(""));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSearchField);
    }

    public boolean waitUntilPageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathSearchField);
    }

    public void typeBackspaceInSearchInput() throws Exception {
        getPickerEdit().click();
        AndroidCommonUtils.tapBackspaceButton();
    }

    public void tapCreateConversation() throws Exception {
        getElement(idCreateOrOpenConversationButton,
                "Create/Open Conversation button is not visible in People Picker").click();
    }

    public void tapClearButton() throws Exception {
        getElement(idPeoplePickerClearbtn, "Clear button is not visible").click();
    }

    public boolean waitUntilNameVisible(boolean isGroup, String name) throws Exception {
        By locator = getNameLocator(isGroup, name);
        return scrollUntilLocatorVisible(locator).isPresent();
    }

    public boolean waitUntilNameInvisible(boolean isGroup, String name) throws Exception {
        By locator = getNameLocator(isGroup, name);
        return !DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator, 2);
    }

    public boolean waitUntilActionButtonIsVisible(String name) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getActionButtonLocatorByName(name));
    }

    public boolean waitUntilActionButtonIsInvisible(String name) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), getActionButtonLocatorByName(name));
    }

    public void tapActionButton(String name) throws Exception {
        getElement(getActionButtonLocatorByName(name)).click();
    }

    public Optional<BufferedImage> getUserAvatarScreenshot(String name) throws Exception {
        assert waitUntilNameVisible(false, name) : String.format("User '%s' is not visible in the list", name);
        return this.getElementScreenshot(getElement(getUserAvatarLocator(name)));
    }

    public void tapOnUserAvatar(String name) throws Exception {
        getElement(getUserAvatarLocator(name)).click();
    }

    public void tapInviteButtonFor(String name) throws Exception {
        assert waitUntilNameVisible(false, name) : String.format(
                "User '%s' is not visible in the invites list", name);
        getElement(getInviteButtonLocator(name)).click();
    }

    public boolean isErrorVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idError);
    }

    public boolean isErrorInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idError);
    }

    /// Add People to conversation / Create group popover
    public boolean isPickUserConfirmationBtnVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idPickUserConfirmationBtn);
    }

    public void tapPickUserConfirmationButton() throws Exception {
        getElement(idPickUserConfirmationBtn).click();
    }
    ////

    protected void swipeRightOnContactAvatar(By locator) throws Exception {
        DriverUtils.swipeRight(getDriver(), getDriver().findElement(locator), 500);
    }

    protected void tapOnUserName(By locator, String name) throws Exception {
        scrollUntilLocatorVisible(locator).orElseThrow(() -> new IllegalStateException(
                String.format("A user '%s' is not present on People Picker page", name))).click();
    }

    protected void tapOnGroupName(By locator, String name) throws Exception {
        scrollUntilLocatorVisible(locator).orElseThrow(() -> new IllegalStateException(
                String.format("A group '%s' is not present on People Picker page", name))).click();
    }

    protected abstract By getNameLocator(boolean isGroup, String name);

    protected abstract By getUserAvatarLocator(String name);

    protected abstract By getInviteButtonLocator(String name);

    private static final int MAX_SCROLLS = 3;

    private WebElement getPickerEdit() throws Exception {
        return getElement(xpathSearchField, "Search field is not visible on the current page");
    }

    private Optional<WebElement> scrollUntilLocatorVisible(By locator) throws Exception {
        this.hideKeyboard();
        int ntry = 0;
        while (ntry < MAX_SCROLLS) {
            final Optional<WebElement> result = getElementIfDisplayed(locator, 1);
            if (result.isPresent()) {
                return result;
            } else {
                DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idListContainer), 500, 50, 90, 50, 10);
            }
            ntry++;
        }
        return Optional.empty();
    }

    private By getActionButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "open conversation":
                return xpathOpenConversationButton;
            case "create conversation":
                return xpathCreateConversationButton;
            case "send image":
                return idQuickMenuCameraButton;
            case "call":
                return idQuickMenuCallButton;
            case "video call":
                return idQuickMenuVideoCallButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown action button name '%s'", name));
        }
    }


}
