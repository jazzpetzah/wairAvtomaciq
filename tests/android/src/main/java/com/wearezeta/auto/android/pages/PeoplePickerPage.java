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
            .format("//*[@id='ttv_pickuser_searchconversation_name' and @value='%s']", name);

    public static final Function<String, String> xpathPeoplePickerContactByName = name -> String
            .format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']/parent::*/parent::*",
                    name);

    public static final String xpathTopPeopleAvatars = "//*[@id='cwtf__startui_top_user']";

    public static final Function<String, String> xpathTopPeopleAvatarByName = name -> String
            .format("//*[@id='cwtf__startui_top_user' and .//*[@value='%s']]", name.toUpperCase());

    public static final Function<String, String> xpathFoundAvatarByName = name -> String
            .format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']"
                    + "/preceding-sibling::*[@id='cv_pickuser__searchuser_chathead']", name);

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
        List<WebElement> pickerSearches = getDriver().findElements(By.id(idPickerSearch));
        for (WebElement candidate : pickerSearches) {
            if (DriverUtils.isElementPresentAndDisplayed(getDriver(), candidate)
                    && candidate.getLocation().getX() >= 0
                    && candidate.getLocation().getY() >= 0) {
                return candidate;
            }
        }
        throw new ElementNotVisibleException("People Picker input is not displayed");
    }

    public void tapPeopleSearch() throws Exception {
        findVisiblePickerSearch().click();
    }

    public void tapOnContactInTopPeoples(String name) throws Exception {
        final By locator = By.xpath(xpathTopPeopleAvatarByName.apply(name));
        getElement(locator, String.format("Top People item '%s' is not visible", name)).click();
    }

    public void typeTextInPeopleSearch(String text) throws Exception {
        final WebElement pickerSearch = findVisiblePickerSearch();
        pickerSearch.sendKeys(text);
    }

    public void addTextToPeopleSearch(String text) throws Exception {
        findVisiblePickerSearch().sendKeys(text);
    }

    public boolean isNoResultsFoundVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idNoResultsFound));
    }

    public boolean isTopPeopleHeaderVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathTopPeopleAvatars));
    }

    public boolean waitUntilTopPeopleHeaderInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.xpath(xpathTopPeopleAvatars));
    }

    private static final int MAX_SCROLLS = 3;

    private boolean scrollUntilLocatorVisible(By locator) throws Exception {
        this.hideKeyboard();
        int ntry = 0;
        while (ntry < MAX_SCROLLS) {
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
                return true;
            } else {
                DriverUtils.swipeElementPointToPoint(getDriver(), content,
                        500, 50, 90, 50, 10);
            }
            ntry++;
        }
        return false;
    }

    public void selectUser(String name) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerContactByName.apply(name));
        if (!scrollUntilLocatorVisible(locator)) {
            throw new IllegalStateException(
                    String.format("A user '%s' is not present on People Picker page", name));
        }
        getDriver().findElement(locator).click();
    }

    public void selectGroup(String name) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerGroupByName.apply(name));
        if (!scrollUntilLocatorVisible(locator)) {
            throw new IllegalStateException(
                    String.format("A group '%s' is not present on People Picker page", name));
        }
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
        getElement(By.id(idPeoplePickerClearbtn), "Clear button is not visible").click();
    }

    public boolean isUserVisible(String name) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerContactByName.apply(name));
        return scrollUntilLocatorVisible(locator);
    }

    public boolean isGroupVisible(String name) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerGroupByName.apply(name));
        return scrollUntilLocatorVisible(locator);
    }

    public boolean isUserInvisible(String name) throws Exception {
        return !DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpathPeoplePickerContactByName.apply(name)), 2);
    }

    public boolean isGroupInvisible(String name) throws Exception {
        return !DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpathPeoplePickerGroupByName.apply(name)), 2);
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
