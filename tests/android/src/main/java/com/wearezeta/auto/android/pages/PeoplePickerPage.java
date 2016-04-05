package com.wearezeta.auto.android.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class PeoplePickerPage extends AndroidPage {
    public static final By idParticipantsClose = By.id("gtv__participants__close");

    public static final By xpathSearchField =  By.xpath("//*[@id='sbv__search_box']");

    public static final By idSuggestionUserName = By.xpath("//*[@id='ttv_pickuser__searchuser_name']");

    public static final By idSingleParticipantClose = By.id("gtv__single_participants__close");

    public static final By idPeoplePickerClearbtn = By.id("gtv_pickuser__clearbutton");

    public static final By idSendConnectionRequestButton = By.id("zb__send_connect_request__connect_button");

    public static final By idQuickMenuCameraButton = By.id("gtv__conversation_quick_menu__camera_button");

    public static final By idQuickMenuCallButton = By.id("gtv__conversation_quick_menu__call_button");

    public static final By idQuickMenuVideoCallButton = By.id("gtv__conversation_quick_menu__video_call_button");

    public static final By idPickUserToolbar = By.id("t_pickuser_toolbar");

    public static final By idPickUserToolbarTitle = By.id("ttv__pickuser__add_header");

    private static final Function<String, String> xpathStrPeoplePickerGroupByName = name -> String
            .format("//*[@id='ttv_pickuser_searchconversation_name' and @value='%s']", name);

    public static final Function<String, String> xpathStrPeoplePickerContactByName = name -> String
            .format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']/parent::*/parent::*", name);

    public static final By xpathTopPeopleAvatars = By.xpath("//*[@id='cwtf__startui_top_user']");

    public static final Function<String, String> xpathStrTopPeopleAvatarByName = name -> String
            .format("//*[@id='cwtf__startui_top_user' and .//*[@value='%s']]", name.toUpperCase());

    public static final Function<String, String> xpathStrFoundAvatarByName = name -> String
            .format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']"
                    + "/preceding-sibling::*[@id='cv_pickuser__searchuser_chathead']", name);

    public static final Function<String, String> xpathStrPeopleSearchFieldByName = name -> String
            .format("//*[@id='puet_pickuser__searchbox' and @value='%s']", name);

    public static final Function<String, String> xpathStrExistedContactUserByName = name -> String
            .format("//*[@id='ttv__contactlist__user__name' and @value='%s']", name);

    public static final Function<String, String> xpathStrToolbarTitleByName = name -> String
            .format("//*[@id='ttv__pickuser__add_header' and @value='%s']", name);

    public static final By idPickUserConfirmationBtn = By.id("zb__pickuser__confirmation_button");

    private static final String idStrCreateOrOpenConversationButton = "zb__conversation_quick_menu__conversation_button";
    private static final By xpathOpenConversationButton = By.xpath(String.format("//*[@id='%s' and @value='OPEN']",
            idStrCreateOrOpenConversationButton));
    private static final By xpathCreateConversationButton =
            By.xpath(String.format("//*[@id='%s' and @value='CREATE GROUP']", idStrCreateOrOpenConversationButton));
    private static final By idCreateOrOpenConversationButton = By.id(idStrCreateOrOpenConversationButton);

    private static final By idPickUserError = By.id("ttv_pickuser__error_header");

    private static final By idPickerListContainer = By.id("rv__pickuser__header_list_view");

    public PeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private WebElement getPickerEdit() throws Exception {
        return getElement(xpathSearchField, "Search field is not visible on the current page");
    }

    public void tapPeopleSearch() throws Exception {
        getPickerEdit().click();
    }

    public void tapOnContactInTopPeoples(String name) throws Exception {
        final By locator = By.xpath(xpathStrTopPeopleAvatarByName.apply(name));
        getElement(locator, String.format("Top People item '%s' is not visible", name)).click();
    }

    public void typeTextInPeopleSearch(String text) throws Exception {
        final WebElement pickerSearch = getPickerEdit();
        pickerSearch.click();
        pickerSearch.sendKeys(text);
    }

    public void addTextToPeopleSearch(String text) throws Exception {
        getPickerEdit().sendKeys(text);
    }

    public boolean isPeopleSearchTextEmpty() throws Exception {
        final By locator = By.xpath(xpathStrPeopleSearchFieldByName.apply(""));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isSuggestionVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idSuggestionUserName);
    }

    public boolean isSuggestionInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idSuggestionUserName);
    }

    public boolean isErrorVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idPickUserError);
    }

    public boolean isErrorInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idPickUserError);
    }

    public boolean isTopPeopleHeaderVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathTopPeopleAvatars);
    }

    public boolean waitUntilTopPeopleHeaderInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathTopPeopleAvatars);
    }

    private static final int MAX_SCROLLS = 3;

    private Optional<WebElement> scrollUntilLocatorVisible(By locator) throws Exception {
        this.hideKeyboard();
        int ntry = 0;
        while (ntry < MAX_SCROLLS) {
            final Optional<WebElement> result = getElementIfDisplayed(locator, 1);
            if (result.isPresent()) {
                return result;
            } else {
                DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idPickerListContainer), 500, 50, 90, 50, 10);
            }
            ntry++;
        }
        return Optional.empty();
    }

    public void selectUser(String name) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerContactByName.apply(name));
        scrollUntilLocatorVisible(locator).orElseThrow(() -> new IllegalStateException(
                String.format("A user '%s' is not present on People Picker page", name))).click();
    }

    public void selectGroup(String name) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerGroupByName.apply(name));
        scrollUntilLocatorVisible(locator).orElseThrow(() -> new IllegalStateException(
                String.format("A group '%s' is not present on People Picker page", name))).click();
    }

    public boolean isPeoplePickerPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSearchField);
    }

    public boolean isPickUserConfirmationBtnVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idPickUserConfirmationBtn);
    }

    public void tapPickUserConfirmationButton() throws Exception {
        getElement(idPickUserConfirmationBtn).click();
    }

    public void tapCreateConversation() throws Exception {
        getElement(idCreateOrOpenConversationButton,
                "Create/Open Conversation button is not visible in People Picker").click();
    }

    public void tapClearButton() throws Exception {
        getElement(idPeoplePickerClearbtn, "Clear button is not visible").click();
    }

    public boolean isUserVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerContactByName.apply(name));
        return scrollUntilLocatorVisible(locator).isPresent();
    }

    public boolean isGroupVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerGroupByName.apply(name));
        return scrollUntilLocatorVisible(locator).isPresent();
    }

    public boolean isContactVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrExistedContactUserByName.apply(name));
        return scrollUntilLocatorVisible(locator).isPresent();
    }

    public boolean isUserInvisible(String name) throws Exception {
        return !DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpathStrPeoplePickerContactByName.apply(name)), 2);
    }

    public boolean isGroupInvisible(String name) throws Exception {
        return !DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpathStrPeoplePickerGroupByName.apply(name)), 2);
    }

    public boolean isContactInvisible(String name) throws Exception {
        return !DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.xpath(xpathStrExistedContactUserByName.apply(name)), 2);
    }

    public boolean isToolbarVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idPickUserToolbar);
    }

    public boolean isToolbarTitleVisible(String name) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.xpath(xpathStrToolbarTitleByName.apply(name)));
    }

    public void swipeRightOnContactAvatar(String name) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerContactByName.apply(name));
        DriverUtils.swipeRight(getDriver(), getDriver().findElement(locator), 500);
    }

    public void typeBackspaceInSearchInput() throws Exception {
        getPickerEdit().click();
        AndroidCommonUtils.tapBackspaceButton();
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

    public boolean waitUntilActionButtonIsVisible(String name) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getActionButtonLocatorByName(name));
    }

    public boolean waitUntilActionButtonIsInvisible(String name) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), getActionButtonLocatorByName(name));
    }

    public void tapActionButton(String name) throws Exception {
        getElement(getActionButtonLocatorByName(name)).click();
    }
}
