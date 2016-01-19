package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PeoplePickerPage extends IOSPage {
    private static final String xpathPickerSearch = "//UIATextView[@name='textViewSearch' and @visible='true']";
    @FindBy(xpath = xpathPickerSearch)
    private WebElement peoplePickerSearch;

    private static final String xpathPickerClearButton = "//*[@name='PeoplePickerClearButton' and @visible='true']";
    @FindBy(xpath = xpathPickerClearButton)
    private WebElement peoplePickerClearBtn;

    private static final String classNameContactListNames = "UIACollectionCell";
    @FindBy(className = classNameContactListNames)
    private List<WebElement> resultList;

    private static final String xpathUnicUserPickerSearchResult =
            xpathStrMainWindow + "/UIACollectionView[2]/UIACollectionCell[1]";
    @FindBy(xpath = xpathUnicUserPickerSearchResult)
    private WebElement userPickerSearchResult;

    private static final String nameKeyboardEnterButton = "Return";
    @FindBy(name = nameKeyboardEnterButton)
    private WebElement goButton;

    private static final String nameCreateConversationButton = "CREATE";
    @FindBy(name = nameCreateConversationButton)
    private WebElement createConverstaionButton;

    private static final String namePeoplePickerContactsLabel = "CONTACTS";
    @FindBy(name = namePeoplePickerContactsLabel)
    private WebElement contactsLabel;

    private static final String namePeoplePickerOtheraLabel = "OTHERS";
    @FindBy(name = namePeoplePickerOtheraLabel)
    private WebElement othersLabel;

    private static final String namePeoplePickerTopPeopleLabel = "TOP PEOPLE";
    @FindBy(name = namePeoplePickerTopPeopleLabel)
    private WebElement topPeopleLabel;

    private static final String namePeoplePickerAddToConversationButton = "ADD TO CONVERSATION";
    @FindBy(name = namePeoplePickerAddToConversationButton)
    private WebElement addToConversationBtn;

    private static final String nameShareButton = "SHARE CONTACTS";
    @FindBy(name = nameShareButton)
    private WebElement shareButton;

    private static final String nameNotNowButton = "NOT NOW";
    @FindBy(name = nameNotNowButton)
    private WebElement notNowButton;

    private static final String nameContinueUploadButton = "SHARE CONTACTS";
    @FindBy(name = nameContinueUploadButton)
    private WebElement continueButton;

    private static final String namePeopleYouMayKnowLabel = "CONNECT";
    @FindBy(name = namePeopleYouMayKnowLabel)
    private WebElement peopleYouMayKnowLabel;

    private static final String nameUnblockButton = "UNBLOCK";
    @FindBy(name = nameUnblockButton)
    private WebElement unblockButton;

    private static final String xpathPeoplePickerAllTopPeople =
            xpathStrMainWindow + "/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell";
    @FindBy(xpath = xpathPeoplePickerAllTopPeople)
    private List<WebElement> topPeopleList;

    public static final String xpathInviteCopyButton = "//UIACollectionCell[@name='Copy']";
    @FindBy(xpath = xpathInviteCopyButton)
    private WebElement inviteCopyButton;

    private static final String nameSendAnInviteButton = "INVITE MORE PEOPLE";
    @FindBy(name = nameSendAnInviteButton)
    private WebElement sendInviteButton;

    private static final String nameInstantConnectButton = "instantPlusConnectButton";
    @FindBy(name = nameInstantConnectButton)
    private WebElement instantConnectButton;

    private static final String xpathSearchResultCell =
            xpathStrMainWindow + "/UIACollectionView[1]/UIACollectionCell[1]";
    @FindBy(xpath = xpathSearchResultCell)
    private WebElement searchResultCell;

    private static final String xpathSearchResultCellAvatar =
            xpathStrMainWindow + "/UIACollectionView[1]/UIACollectionCell[1]/UIAStaticText";
    @FindBy(xpath = xpathSearchResultCellAvatar)
    private WebElement searchResultAvatar;

    private static final String xpathSearchResultContainer = xpathStrMainWindow + "/UIACollectionView[2]";
    @FindBy(xpath = xpathSearchResultContainer)
    private WebElement searchResultContainer;

    private static final String nameLaterButton = "MAYBE LATER";
    @FindBy(name = nameLaterButton)
    private WebElement maybeLaterButton;

    private static final String nameOpenConversationButton = "OPEN";
    @FindBy(name = nameOpenConversationButton)
    private WebElement openConversationButton;

    private static final String nameCallButton = "actionBarCallButton";
    @FindBy(name = nameCallButton)
    private WebElement callButton;

    private static final String nameSendImageButton = "actionBarCameraButton";
    @FindBy(name = nameSendImageButton)
    private WebElement sendImageButton;

    private static final String xpathContactViewCloseButton = "//*[@name='ContactsViewCloseButton' and @visible='true']";
    @FindBy(xpath = xpathContactViewCloseButton)
    private WebElement closeInviteButton;

    private static final Function<String, String> xpathFoundContactByName =
            name -> String.format("//UIAStaticText[@name='%s' and @visible='true']", name);

    private static final Function<String, String> xpathSuggestedContactToSwipeByName = name ->
            String.format("//UIACollectionCell[descendant::UIAStaticText[@name='%s']]", name);

    private static final Function<String, String> xpathHideButtonForContactByName = name ->
            String.format("//UIAButton[@name='HIDE'][ancestor::UIACollectionCell[descendant::UIAStaticText[@name='%s']]]",
                    name);

    private static final String nameHideSuggestedContactButton = "HIDE";

    private static final Function<String, String> xpathSuggestedContactByName = name ->
            String.format("//UIACollectionCell/UIAStaticText[@name='%s']", name);

    private static final Function<Integer, String> xpathPeoplePickerTopConnectionsAvatarByIdx = idx ->
            String.format("%s/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell[%s]",
                    xpathStrMainWindow, idx);

    private static final Function<String, String> xpathPeoplePickerSelectedCellByName = name ->
            String.format("%s/UIATableView[1]/UIATableCell[@name='%s']", xpathStrMainWindow, name);

    private static final Function<Integer, String> xpathPeoplePickerTopConnectionsItemByIdx = idx ->
            String.format(
                    "%s/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell[%d]/UIAStaticText[last()]",
                    xpathStrMainWindow, idx);

    private int numberTopSelected = 0;

    public PeoplePickerPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickMaybeLaterButton() {
        maybeLaterButton.click();
    }

    public void clickNotNowButton() {
        notNowButton.click();
    }

    public void closeShareContactsIfVisible() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameNotNowButton), 1)) {
            clickNotNowButton();
        }
    }

    public boolean isPeoplePickerPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.xpath(xpathPickerSearch));
    }

    public void tapOnPeoplePickerSearch() throws Exception {
        this.getDriver().tap(1, peoplePickerSearch.getLocation().x + 40,
                peoplePickerSearch.getLocation().y + 30, 1);
        // workaround for people picker activation
    }

    public void tapOnPeoplePickerClearBtn() throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), peoplePickerClearBtn);
    }

    public double checkAvatarClockIcon(String name) throws Exception {
        BufferedImage clockImage = getAvatarClockIconScreenShot(name);
        String path = CommonUtils.getAvatarWithClockIconPathIOS(GroupChatPage.class);
        BufferedImage templateImage = ImageUtil.readImageFromFile(path);
        return ImageUtil.getOverlapScore(clockImage, templateImage,
                ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
    }

    public BufferedImage getAvatarClockIconScreenShot(String name) throws Exception {
        int multiply = 1;
        String device = CommonUtils.getDeviceName(this.getClass());
        if (device.equalsIgnoreCase("iPhone 6")
                || device.equalsIgnoreCase("iPad Air")) {
            multiply = 2;
        } else if (device.equalsIgnoreCase("iPhone 6 Plus")) {
            multiply = 3;
        }

        int x = multiply * searchResultCell.getLocation().x;
        int y = multiply * searchResultCell.getLocation().y;
        int w = multiply
                * (searchResultCell.getLocation().x + searchResultCell
                .getSize().width / 4);
        int h = multiply * searchResultCell.getSize().height;
        return getScreenshotByCoordinates(x, y, w, h).orElseThrow(
                IllegalStateException::new);
    }

    public void fillTextInPeoplePickerSearch(String text) throws Exception {
        // FIXME: Optimize this flow
        final WebElement peoplePickerSearch = getElement(By.xpath(xpathPickerSearch),
                "Search UI input is not visible");
        try {
            sendTextToSearchInput(text);
        } catch (WebDriverException ex) {
            peoplePickerSearch.clear();
            sendTextToSearchInput(text);
        }
    }

    public void sendTextToSearchInput(String text) throws Exception {
        ((IOSElement) getDriver().findElementByXPath(xpathPickerSearch)).setValue(text);
    }

    public boolean waitUserPickerFindUser(String user) throws Exception {
        final By locator = By.xpath(xpathFoundContactByName.apply(user));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), locator, 5);
    }

    public void clickOnNotConnectedUser(String name) throws Exception {
        final By locator = By.xpath(xpathFoundContactByName.apply(name));
        if (this.waitUserPickerFindUser(name)) {
            WebElement foundContact = getDriver().findElement(locator);
            DriverUtils.waitUntilElementClickable(getDriver(), foundContact);
            foundContact.click();
        } else {
            throw new IllegalArgumentException(String.format("'%s' is not present in search results", name));
        }
    }

    public void pickUserAndTap(String name) throws Exception {
        PickUser(name).click();
    }

    public void pickIgnoredUserAndTap(String name) throws Exception {
        PickUser(name).click();
    }

    public void dismissPeoplePicker() throws Exception {
        getElement(By.xpath(xpathPickerClearButton), "Clear button is not visible in the search field").
                click();
    }

    public void swipeToRevealHideSuggestedContact(String contact) throws Exception {
        assert this.waitUserPickerFindUser(contact) :
                String.format("'%s' is not visible in People Picker", contact);
        final By locator = By.xpath(xpathSuggestedContactToSwipeByName.apply(contact));
        final WebElement contactToSwipe = this.getDriver().findElement(locator);
        int count = 0;
        do {
            DriverUtils.swipeRight(this.getDriver(), contactToSwipe, 500, 50, 50);
            count++;
        } while (!isHideButtonVisible() || count > 3);
    }

    public void swipeCompletelyToDismissSuggestedContact(String contact) throws Exception {
        assert this.waitUserPickerFindUser(contact) : String.format("'%s' is not visible in People Picker", contact);
        final By locator = By.xpath(xpathSuggestedContactToSwipeByName.apply(contact));
        DriverUtils.swipeRight(this.getDriver(), getDriver().findElement(locator), 1000, 100, 50);
    }

    public void tapHideSuggestedContact(String contact) throws Exception {
        final By locator = By.xpath(xpathHideButtonForContactByName.apply(contact));
        getElement(locator, String.format("Hide button is not visible for '%s'", contact)).click();
    }

    public boolean isHideButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameHideSuggestedContactButton));
    }

    public boolean isSuggestedContactVisible(String contact) throws Exception {
        final By locator = By.xpath(xpathSuggestedContactByName.apply(contact));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 2);
    }

    public boolean addToConversationNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.name(namePeoplePickerAddToConversationButton));
    }

    public IOSPage clickOnGoButton(boolean isGroupChat) throws Exception {
        goButton.click();
        if (numberTopSelected >= 2 || isGroupChat) {
            return new GroupChatPage(this.getLazyDriver());
        } else {
            return new DialogPage(this.getLazyDriver());
        }
    }

    private WebElement PickUser(String name) throws Exception {
        fillTextInPeoplePickerSearch(name);
        waitUserPickerFindUser(name);
        return getDriver().findElementByName(name);
    }

    public void selectUser(String name) throws Exception {
        List<WebElement> elements = getDriver().findElements(By.name(name));
        if (elements.size() == 0) {
            throw new NoSuchElementException("Element not found");
        }
        for (WebElement el : elements) {
            if (el.isDisplayed() && el.isEnabled()) {
                DriverUtils.tapByCoordinates(getDriver(), el);
                break;
            }
        }
    }

    public void tapNumberOfTopConnections(int numberToTap) throws Exception {
        numberTopSelected = 0;
        for (int i = 1; i < numberToTap + 1; i++) {
            numberTopSelected++;
            final By locator = By.xpath(xpathPeoplePickerTopConnectionsAvatarByIdx.apply(i));
            getDriver().findElement(locator).click();
        }
    }

    public boolean isCreateConversationButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameCreateConversationButton));
    }

    public void clickCreateConversationButton() throws Throwable {
        createConverstaionButton.click();
    }

    public boolean isTopPeopleLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(namePeoplePickerTopPeopleLabel), 2);
    }

    public boolean isConnectLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(namePeopleYouMayKnowLabel));
    }

    public boolean isUserSelected(String name) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerSelectedCellByName.apply(name));
        return getDriver().findElement(locator).getAttribute("value").equals("1");
    }

    public void clickConnectedUserAvatar(String name) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerSelectedCellByName.apply(name));
        getDriver().findElement(locator).click();
    }

    public void hitDeleteButton() {
        peoplePickerSearch.sendKeys(Keys.DELETE);
    }

    public void clickAddToCoversationButton() throws Exception {
        addToConversationBtn.click();
    }

    public void clickOnUserOnPending(String contact) throws Exception {
        WebElement el = getDriver().findElement(By.name(contact));
        DriverUtils.tapByCoordinates(getDriver(), el);
    }

    public boolean isUploadDialogShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameShareButton), 2);
    }

    public void clickContinueButton() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameContinueUploadButton))) {
            continueButton.click();
        }
    }

    public boolean isPeopleYouMayKnowLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(namePeopleYouMayKnowLabel));
    }

    private void unblockButtonDoubleClick() throws Exception {
        DriverUtils.multiTap(getDriver(), getDriver().findElement(By.name(nameUnblockButton)), 2);
    }

    public void unblockUser() throws Exception {
        unblockButton.click();
    }

    public void unblockUserOniPad() throws Exception {
        unblockButtonDoubleClick();
    }

    public int getNumberOfSelectedTopPeople() {
        int selectedPeople = 0;
        for (WebElement people : topPeopleList) {
            if (people.getAttribute("value").equals("1")) {
                selectedPeople++;
            }
        }
        return selectedPeople;
    }

    public void tapSendInviteButton() {
        sendInviteButton.click();
    }

    public void tapSendInviteCopyButton() throws Exception {
        inviteCopyButton.click();
    }

    public void pressInstantConnectButton() {
        instantConnectButton.click();
    }

    public String getNameOfNuser(int i) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerTopConnectionsItemByIdx.apply(i));
        return this.getDriver().findElement(locator).getAttribute("name");
    }

    public void tapNumberOfTopConnectionsButNotUser(int numberToTap,
                                                    String contact) throws Exception {
        numberTopSelected = 0;
        for (int i = 1; i < numberToTap + 1; i++) {
            if (!contact.equals(getNameOfNuser(i).toLowerCase())) {
                final By locator = By.xpath(xpathPeoplePickerTopConnectionsAvatarByIdx.apply(i));
                getDriver().findElement(locator).click();
                numberTopSelected++;
            } else {
                numberToTap++;
            }
        }
    }

    public void tapOnTopConnectionAvatarByOrder(int i) throws Exception {
        final By locator = By.xpath(xpathPeoplePickerTopConnectionsAvatarByIdx.apply(i));
        getDriver().findElement(locator).click();
    }

    public boolean isOpenConversationButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameOpenConversationButton));
    }

    public void clickOpenConversationButton() throws Exception {
        getElement(By.name(nameOpenConversationButton), "Open conversation button is not visible").click();
    }

    public boolean isCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameCallButton));
    }

    public void clickCallButton() throws Exception {
        getElement(By.name(nameCallButton), "Call button is not visible").click();
    }

    public boolean isSendImageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameSendImageButton));
    }

    public void clickSendImageButton() throws Exception {
        getElement(By.name(nameSendImageButton), "Send image button is not visible").click();
    }

    public void inputTextInSearch(String text) throws Exception {
        getElement(By.xpath(xpathPickerSearch), "Search input is not visible").sendKeys(text);
    }

    public void closeInviteList() {
        closeInviteButton.click();
    }
}
