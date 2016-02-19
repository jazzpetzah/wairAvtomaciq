package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DummyElement;
import org.openqa.selenium.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PeoplePickerPage extends IOSPage {
    private static final By xpathPickerSearch = By.xpath("//UIATextView[@name='textViewSearch' and @visible='true']");

    private static final By xpathPickerClearButton =
            By.xpath("//*[@name='PeoplePickerClearButton' or @name='ContactsViewCloseButton']");

    private static final By nameKeyboardEnterButton = By.name("Return");

    private static final By nameCreateConversationButton = By.name("CREATE");

    private static final By namePeoplePickerTopPeopleLabel = By.name("TOP PEOPLE");

    private static final By namePeoplePickerAddToConversationButton = By.name("ADD TO CONVERSATION");

    private static final By nameContinueUploadButton = By.name("SHARE CONTACTS");

    private static final By namePeopleYouMayKnowLabel = By.name("CONNECT");

    private static final By nameUnblockButton = By.name("UNBLOCK");

    private static final By xpathSelectedTopPeople =
            By.xpath("//UIACollectionCell/UIACollectionView/UIACollectionCell[@value='1']");

    public static final By xpathInviteCopyButton = By.xpath("//UIACollectionCell[@name='Copy']");

    private static final By nameSendAnInviteButton = By.name("INVITE MORE PEOPLE");

    private static final Function<String, String> xpathStrInstantConnectButtonByUserName = name -> String.format(
            "//UIACollectionCell[ ./UIAStaticText[@name='%s'] ]/UIAButton[@name='instantPlusConnectButton']", name);

    private static final By nameLaterButton = By.name("MAYBE LATER");

    private static final By nameOpenConversationButton = By.name("OPEN");

    private static final By nameCallButton = By.name("actionBarCallButton");

    private static final By nameSendImageButton = By.name("actionBarCameraButton");

    private static final By xpathContactViewCloseButton =
            By.xpath("//*[@name='ContactsViewCloseButton' and @visible='true']");

    private static final Function<String, String> xpathStrFoundContactByName =
            name -> String.format("//*[@name='%s' and @visible='true']", name);

    private static final Function<Integer, String> xpathStrPeoplePickerTopConnectionsAvatarByIdx = idx ->
            String.format("%s/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell[%s]",
                    xpathStrMainWindow, idx);

    private static final Function<String, String> xpathStrPeoplePickerSelectedCellByName = name ->
            String.format("%s/UIATableView[1]/UIATableCell[@name='%s']", xpathStrMainWindow, name);

    private static final Function<Integer, String> xpathStrPeoplePickerTopConnectionsItemByIdx = idx ->
            String.format(
                    "%s/UIACollectionView/UIACollectionCell/UIACollectionView/UIACollectionCell[%d]/UIAStaticText[last()]",
                    xpathStrMainWindow, idx);

    public PeoplePickerPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickMaybeLaterButton() throws Exception {
        getElement(nameLaterButton).click();
    }

    public boolean isPeoplePickerPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathPickerSearch);
    }

    public void tapOnPeoplePickerSearch() throws Exception {
        getElement(xpathPickerSearch).click();
    }

    public void tapOnPeoplePickerClearBtn() throws Exception {
        DriverUtils.tapByCoordinates(getDriver(), getElement(xpathPickerClearButton));
    }

    public void fillTextInPeoplePickerSearch(String text) throws Exception {
        final WebElement searchInput = getElement(xpathPickerSearch);
        if (CommonUtils.getIsSimulatorFromConfig(this.getClass()) && text.matches(".*\\W+.*")) {
            inputStringFromKeyboard(searchInput, text + " ", true, true);
        } else {
            searchInput.sendKeys(text + " ");
        }
    }

    public Optional<WebElement> getSearchResultsElement(String user) throws Exception {
        final By locator = By.xpath(xpathStrFoundContactByName.apply(user));
        return getElementIfDisplayed(locator);
    }

    public boolean isElementNotFoundInSearch(String name) throws Exception {
        final By locator = By.xpath(xpathStrFoundContactByName.apply(name));
        return !DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 2);
    }

    public void selectElementInSearchResults(String name) throws Exception {
        getSearchResultsElement(name).orElseThrow(
                () -> new IllegalStateException(String.format("User '%s' is not visible in people picker", name))).
                click();
    }

    public void dismissPeoplePicker() throws Exception {
        getElement(xpathPickerClearButton, "Clear button is not visible in the search field").click();
    }

    public boolean addToConversationNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), namePeoplePickerAddToConversationButton);
    }

    public void clickOnGoButton() throws Exception {
        getElement(nameKeyboardEnterButton).click();
    }

    public void tapNumberOfTopConnections(int numberToTap) throws Exception {
        for (int i = 1; i <= numberToTap; i++) {
            final By locator = By.xpath(xpathStrPeoplePickerTopConnectionsAvatarByIdx.apply(i));
            getElement(locator).click();
        }
    }

    public boolean isCreateConversationButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameCreateConversationButton);
    }

    public void clickCreateConversationButton() throws Throwable {
        getElement(nameCreateConversationButton).click();
    }

    public boolean isTopPeopleLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), namePeoplePickerTopPeopleLabel, 2);
    }

    public boolean isConnectLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), namePeopleYouMayKnowLabel);
    }

    public boolean isUserSelected(String name) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerSelectedCellByName.apply(name));
        return getElement(locator).getAttribute("value").equals("1");
    }

    public void hitDeleteButton() throws Exception {
        getElement(xpathPickerSearch).sendKeys(Keys.DELETE);
    }

    public void clickAddToConversationButton() throws Exception {
        getElement(namePeoplePickerAddToConversationButton).click();
    }

    public void clickContinueButton() throws Exception {
        getElementIfDisplayed(nameContinueUploadButton).orElseGet(DummyElement::new).click();
    }

    public boolean isPeopleYouMayKnowLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), namePeopleYouMayKnowLabel);
    }

    private void unblockButtonDoubleClick() throws Exception {
        DriverUtils.multiTap(getDriver(), getDriver().findElement(nameUnblockButton), 2);
    }

    public void unblockUser() throws Exception {
        getElement(nameUnblockButton).click();
    }

    public void unblockUserOniPad() throws Exception {
        unblockButtonDoubleClick();
    }

    public int getNumberOfSelectedTopPeople() throws Exception {
        return getElements(xpathSelectedTopPeople).size();
    }

    public void tapSendInviteButton() throws Exception {
        getElement(nameSendAnInviteButton).click();
    }

    public void tapSendInviteCopyButton() throws Exception {
        getElement(xpathInviteCopyButton).click();
    }

    public void pressInstantConnectButton(String forName) throws Exception {
        final By locator = By.xpath(xpathStrInstantConnectButtonByUserName.apply(forName));
        getElement(locator).click();
    }

    public void tapNumberOfTopConnectionsButNotUser(int numberToTap, String contact) throws Exception {
        for (int i = 1; i <= numberToTap; i++) {
            final By locator = By.xpath(xpathStrPeoplePickerTopConnectionsItemByIdx.apply(i));
            final WebElement el = getElement(locator);
            if (!contact.equalsIgnoreCase(el.getAttribute("name"))) {
                el.click();
            } else {
                numberToTap++;
            }
        }
    }

    public void tapOnTopConnectionAvatarByOrder(int i) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerTopConnectionsAvatarByIdx.apply(i));
        getElement(locator).click();
    }

    public boolean isOpenConversationButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameOpenConversationButton);
    }

    public void clickOpenConversationButton() throws Exception {
        getElement(nameOpenConversationButton, "Open conversation button is not visible").click();
    }

    public boolean isCallButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameCallButton);
    }

    public void clickCallButton() throws Exception {
        getElement(nameCallButton, "Call button is not visible").click();
    }

    public boolean isSendImageButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameSendImageButton);
    }

    public void clickSendImageButton() throws Exception {
        getElement(nameSendImageButton, "Send image button is not visible").click();
    }

    public void closeInviteList() throws Exception {
        getElement(xpathContactViewCloseButton).click();
    }

    public boolean isPeopleYouMayKnowLabelInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), namePeopleYouMayKnowLabel);
    }

    public boolean isOpenConversationButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameOpenConversationButton);
    }

    public boolean isCallButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameCallButton);
    }

    public boolean isSendImageButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), nameSendImageButton);
    }
}
