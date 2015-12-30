package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class PeoplePickerPage extends AndroidPage {

    private static final Function<String, String> xpathTopConversationContactByName = name -> String
            .format("//*[@value='%s']", name.toUpperCase());

    public static final String idParticipantsClose = "gtv__participants__close";

    public static final String idPickerSearch = "puet_pickuser__searchbox";

    public static final String idPeoplePickerClearbtn = "gtv_pickuser__clearbutton";

    public static final String idSendConnectionRequestButton = "zb__send_connect_request__connect_button";

    public static final String idQuickMenuCameraButton = "gtv__conversation_quick_menu__camera_button";
    @FindBy(id = idQuickMenuCameraButton)
    private WebElement quickMenuCameraButton;

    public static final String idQuickMenuCallButton = "gtv__conversation_quick_menu__call_button";
    @FindBy(id = idQuickMenuCallButton)
    private WebElement quickMenuCallButton;

    private static final Function<String, String> xpathPeoplePickerGroupByName = name -> String
            .format("//*[@id='ttv_pickuser_searchconversation_name' and @value='%s']",
                    name);

    public static final Function<String, String> xpathPeoplePickerContactByName = name -> String
            .format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']/parent::*/parent::*",
                    name);

    private static final String idTopPeopleRoot = "rv_top_users";

    @FindBy(id = idPeoplePickerClearbtn)
    private WebElement pickerClearBtn;

    private static final String idPickerRows = "ll_pickuser__rowview_searchuser";
    @FindBy(id = idPickerRows)
    private List<WebElement> pickerSearchRows;

    private static final String idPickerUsersUnselected = "pick_user_chathead_unselected";
    @FindBy(id = idPickerUsersUnselected)
    private List<WebElement> pickerUsersUnselected;

    private static final String idPickerGrid = "gv_pickuser__topresult__gridview";
    @FindBy(id = idPickerGrid)
    private WebElement pickerGrid;

    public static final String idPickerBtnDone = "zb__pickuser__confirmation_button";
    @FindBy(id = idPickerBtnDone)
    private WebElement addToConversationsButton;

    private static final String idCreateOrOpenConversationButton = "zb__conversation_quick_menu__conversation_button";
    @FindBy(id = idCreateOrOpenConversationButton)
    private WebElement createOrOpenConversation;

    private static final Function<String, String> xpathCreateOrOpenConversationButtonByCaption = caption -> String
            .format("//*[@id='%s' and @value='%s']",
                    idCreateOrOpenConversationButton, caption.toUpperCase());

    private static final String idNoResultsFound = "ttv_pickuser__error_header";
    @FindBy(id = idNoResultsFound)
    private WebElement noResults;

    private static final String idPickerListContainer = "pfac__pickuser__header_list_view";
    @FindBy(id = idPickerListContainer)
    private WebElement content;

    @FindBy(id = IncomingPendingConnectionsPage.idConnectToHeader)
    private List<WebElement> connectToHeader;

    private static final String idPickerRecomendedName = "ttv_pickuser__recommended_name";
    @FindBy(id = idPickerRecomendedName)
    private WebElement recommendedName;

    public PeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private WebElement findVisiblePickerSearch() throws Exception {
        DriverUtils.waitUntilLocatorAppears(getDriver(), By.id(idPickerSearch));
        List<WebElement> pickerSearches = getDriver().findElements(
                By.id(idPickerSearch));
        for (WebElement candidate : pickerSearches) {
            if (DriverUtils
                    .isElementPresentAndDisplayed(getDriver(), candidate)
                    && candidate.getLocation().getX() >= 0
                    && candidate.getLocation().getY() >= 0) {
                return candidate;
            }
        }
        throw new ElementNotVisibleException(
                "People Picker input is not displayed");
    }

    public void tapPeopleSearch() throws Exception {
        findVisiblePickerSearch().click();
    }

    public void tapOnContactInTopPeoples(String name) throws Exception {
        final By locator = By.xpath(xpathTopConversationContactByName
                .apply(name));
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
        this.getDriver().findElement(locator).click();
    }

    public void typeTextInPeopleSearch(String text) throws Exception {
        final WebElement pickerSearch = findVisiblePickerSearch();
        pickerSearch.sendKeys(text);
    }

    public void addTextToPeopleSearch(String text) throws Exception {
        findVisiblePickerSearch().sendKeys(text);
    }

    public boolean isNoResultsFoundVisible() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(), noResults);
    }

    public boolean isTopPeopleHeaderVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idTopPeopleRoot));
    }

    public boolean waitUntilTopPeopleHeaderInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.id(idTopPeopleRoot));
    }

    public void selectContact(String contactName) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerContactByName.apply(contactName));
        this.hideKeyboard();
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) :
                String.format(
                        "The user '%s' has not been found in People Picker",
                        contactName);
        getDriver().findElement(locator).click();
    }

    public void selectGroup(String name) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerGroupByName
                .apply(name));
        this.hideKeyboard();
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) :
                String.format("The group '%s' is not present on People Picker page", name);
        this.getDriver().findElement(locator).click();
    }

    public boolean isPeoplePickerPageVisible() throws Exception {
        try {
            findVisiblePickerSearch();
            return true;
        } catch (ElementNotVisibleException e) {
            return false;
        }
    }

    public void waitUserPickerFindUser(String contactName) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerContactByName.apply(contactName));
        this.hideKeyboard();
        assert DriverUtils.waitUntilLocatorAppears(getDriver(), locator) : String
                .format("User '%s' does not exist in the People Picker list",
                        contactName);
    }

    public void navigateBack() throws Exception {
        pickerClearBtn.click();
    }

    public boolean isAddToConversationBtnVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idPickerBtnDone));
    }

    public void clickOnAddToCoversationButton() throws Exception {
        addToConversationsButton.click();
    }

    public void tapCreateConversation() throws Exception {
        assert waitUntilOpenOrCreateConversationButtonIsVisible() :
                "Create/Open Conversation button is not visible in People Picker";
        createOrOpenConversation.click();
    }

    public void tapClearButton() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(),
                pickerClearBtn);
        pickerClearBtn.click();
    }

    public boolean isUserVisible(String contact) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.xpath(xpathPeoplePickerContactByName.apply(contact)));
    }

    public boolean isGroupVisible(String contact) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.xpath(xpathPeoplePickerGroupByName.apply(contact)));
    }

    public boolean isUserInvisible(String contact) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                By.xpath(xpathPeoplePickerContactByName.apply(contact)));
    }

    public boolean isGroupInvisible(String contact) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                By.xpath(xpathPeoplePickerGroupByName.apply(contact)));
    }

    public void doShortSwipeDown() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), content, 500, 15, 20,
                15, 30);
    }

    public void doLongSwipeDown() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), content, 1000, 15,
                15, 15, 180);
    }

    public void tapOpenConversationButton() {
        createOrOpenConversation.click();
    }

    public boolean waitUntilOpenOrCreateConversationButtonIsVisible()
            throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idCreateOrOpenConversationButton));
    }

    public boolean waitUntilOpenOrCreateConversationButtonIsVisible(
            String expectedCaption) throws Exception {
        final By locator = By
                .xpath(xpathCreateOrOpenConversationButtonByCaption
                        .apply(expectedCaption));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilOpenOrCreateConversationButtonIsInvisible()
            throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.id(idCreateOrOpenConversationButton));
    }

    public AndroidPage swipeDown(int durationMilliseconds) throws Exception {
        DriverUtils.swipeByCoordinates(getDriver(), durationMilliseconds, 50,
                20, 50, 90);
        return new ContactListPage(getLazyDriver());
    }

    public void tapCallButton() {
        quickMenuCallButton.click();
    }

    public void tapCameraButton() {
        quickMenuCameraButton.click();
    }

    public boolean isCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idQuickMenuCallButton));
    }

    public boolean isSendImageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idQuickMenuCameraButton));
    }

    public void swipeRightOnContactAvatar(String name) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerContactByName.apply(name));
        DriverUtils.swipeRight(getDriver(), getDriver().findElement(locator), 500);
    }
}
