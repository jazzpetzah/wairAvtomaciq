package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DummyElement;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PeoplePickerPage extends IOSPage {
    private static final By fbNamePickerSearch = FBBy.AccessibilityId("textViewSearch");

    public static final By xpathPickerClearButton =
            By.xpath("//*[@name='PeoplePickerClearButton' or @name='ContactsViewCloseButton']");

    public static final By fbXpathPickerClearButton =
            FBBy.xpath("//*[@name='PeoplePickerClearButton' or @name='ContactsViewCloseButton']");

    private static final By nameKeyboardEnterButton = MobileBy.AccessibilityId("Return");

    private static final By xpathCreateConversationButton = By.xpath("//XCUIElementTypeButton[@name='CREATE GROUP']");

    private static final By namePeoplePickerTopPeopleLabel = MobileBy.AccessibilityId("TOP PEOPLE");

    private static final By namePeoplePickerAddToConversationButton = MobileBy.AccessibilityId("ADD");

    private static final By nameContinueUploadButton = MobileBy.AccessibilityId("SHARE CONTACTS");

    private static final By namePeopleYouMayKnowLabel = MobileBy.AccessibilityId("CONNECT");

    private static final By nameUnblockButton = MobileBy.AccessibilityId("UNBLOCK");

    private static final By xpathSelectedTopPeople =
            By.xpath("//XCUIElementTypeCell/XCUIElementTypeCollectionView/XCUIElementTypeCell[@value='1']");

    public static final By xpathInviteCopyButton = By.xpath("//XCUIElementTypeCell[@name='Copy']");

    private static final By xpathInviteMorePeopleButton = By.xpath("//XCUIElementTypeButton[@name='INVITE MORE PEOPLE']");

    private static final Function<String, String> xpathStrInstantConnectButtonByUserName = name -> String.format(
            "//XCUIElementTypeCell[ .//XCUIElementTypeStaticText[@name='%s'] ]" +
                    "//XCUIElementTypeButton[@name='instantPlusConnectButton']", name);

    private static final By nameLaterButton = MobileBy.AccessibilityId("MAYBE LATER");

    private static final By nameOpenConversationButton = MobileBy.AccessibilityId("OPEN");

    private static final By nameCallButton = MobileBy.AccessibilityId("actionBarCallButton");

    private static final By nameSendImageButton = MobileBy.AccessibilityId("actionBarCameraButton");

    private static final By xpathContactViewCloseButton =
            By.xpath("//*[@name='ContactsViewCloseButton']");

    private static final Function<String, String> xpathStrFoundContactByName =
            name -> String.format("//*[@name='%s']", name);

    private static final Function<Integer, String> xpathStrPeoplePickerTopConnectionsAvatarByIdx = idx ->
            String.format("%s//XCUIElementTypeCollectionView/XCUIElementTypeCell" +
                    "//XCUIElementTypeCollectionView/XCUIElementTypeCell[%s]", xpathStrMainWindow, idx);

    private static final Function<String, String> xpathStrPeoplePickerSelectedCellByName = name ->
            String.format("(%s//XCUIElementTypeTable)[1]/XCUIElementTypeCell[@name='%s']", xpathStrMainWindow, name);

    private static final Function<Integer, String> xpathStrPeoplePickerTopConnectionsItemByIdx = idx ->
            String.format("%s//XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeCollectionView/" +
                    "XCUIElementTypeCell[%d]/XCUIElementTypeStaticText[last()]", xpathStrMainWindow, idx);

    private static final Function<String, String> xpathStrFirstSearchResultEntryByName = name ->
            String.format("%s//XCUIElementTypeCollectionView/XCUIElementTypeCell//XCUIElementTypeStaticText[@name='%s']",
                    xpathStrMainWindow, name);

    private static final By nameNoResults = MobileBy.AccessibilityId("No results.");

    private static final By nameVideoCallButton = MobileBy.AccessibilityId("actionBarVideoCallButton");

    public PeoplePickerPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickMaybeLaterButton() throws Exception {
        getElement(nameLaterButton).click();
    }

    public boolean isPeoplePickerPageVisible() throws Exception {
        return isElementDisplayed(fbNamePickerSearch);
    }

    public void tapOnPeoplePickerSearch() throws Exception {
        this.tapAtTheCenterOfElement((FBElement) getElement(fbNamePickerSearch));
    }

    public void tapOnPeoplePickerClearBtn() throws Exception {
        final FBElement closeButton = (FBElement) getElement(fbXpathPickerClearButton);
        this.tapAtTheCenterOfElement(closeButton);
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathPickerClearButton, 5)) {
            this.tapAtTheCenterOfElement(closeButton);
        }
    }

    public void fillTextInPeoplePickerSearch(String text) throws Exception {
        final WebElement searchInput = getElement(fbNamePickerSearch);
        searchInput.sendKeys(text + " ");
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
        // Wait for the animation
        Thread.sleep(1000);
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

    public boolean isTopPeopleLabelVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), namePeoplePickerTopPeopleLabel, 2);
    }

    public boolean isUserSelected(String name) throws Exception {
        final By locator = By.xpath(xpathStrPeoplePickerSelectedCellByName.apply(name));
        return getElement(locator).getAttribute("value").equals("1");
    }

    public void pressBackspaceButton() throws Exception {
        getElement(fbNamePickerSearch).sendKeys(Keys.DELETE);
    }

    public void clickAddToConversationButton() throws Exception {
        getElement(namePeoplePickerAddToConversationButton).click();
    }

    public void clickContinueButton() throws Exception {
        getElementIfDisplayed(nameContinueUploadButton).orElseGet(DummyElement::new).click();
    }

    public boolean isPeopleYouMayKnowLabelVisible() throws Exception {
        return isElementDisplayed(namePeopleYouMayKnowLabel);
    }

    private void unblockButtonDoubleClick() throws Exception {
        final WebElement dstElement = getElement(nameUnblockButton);
        for (int nClicks = 0; nClicks < 2; nClicks++) {
            dstElement.click();
            Thread.sleep(1000);
        }
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
        getElement(xpathInviteMorePeopleButton).click();
    }

    public void tapSendInviteCopyButton() throws Exception {
        final WebElement copyButton = getElement(xpathInviteCopyButton);
        copyButton.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathInviteCopyButton)) {
            copyButton.click();
        }
    }

    public void pressInstantConnectButton(String forName) throws Exception {
        final By locator = By.xpath(xpathStrInstantConnectButtonByUserName.apply(forName));
        tapElementWithRetryIfStillDisplayed(locator);
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

    public void closeInviteList() throws Exception {
        getElement(xpathContactViewCloseButton).click();
    }

    public boolean isPeopleYouMayKnowLabelInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), namePeopleYouMayKnowLabel);
    }

    public boolean waitUntilNoResultsLabelIsVisible() throws Exception {
        return isElementDisplayed(nameNoResults);
    }

    private By getActionButtonByName(String name) {
        switch (name) {
            case "Create conversation":
                return xpathCreateConversationButton;
            case "Open conversation":
                return nameOpenConversationButton;
            case "Video call":
                return nameVideoCallButton;
            case "Call":
                return nameCallButton;
            case "Send image":
                return nameSendImageButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown action button name: '%s'", name));
        }
    }

    public void clickActionButton(String actionButtonName) throws Exception {
        getElement(getActionButtonByName(actionButtonName)).click();
    }

    public boolean isActionButtonVisible(String actionButtonName) throws Exception {
        return isElementDisplayed(getActionButtonByName(actionButtonName));
    }

    public boolean isActionButtonInvisible(String actionButtonName) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), getActionButtonByName(actionButtonName));
    }

    public boolean isShareContactsSettingsWarningShown() throws Exception {
        return isElementDisplayed(nameLaterButton);
    }

    public boolean isFirstItemInSearchResult(String name) throws Exception {
        final By locator = By.xpath(xpathStrFirstSearchResultEntryByName.apply(name));
        return isElementDisplayed(locator);
    }
}
