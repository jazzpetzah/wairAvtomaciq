package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.*;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class SearchUIPage extends IOSPage {

    private static final String nameStrSearchInput = "textViewSearch";
    private static final By xpathSearchInput = By.xpath(
            String.format("(//XCUIElementTypeTextView[@name='%s'])[last()]", nameStrSearchInput));

    private static final By fbNameXButton = FBBy.AccessibilityId("PeoplePickerClearButton");

    private static final By xpathCreateConversationButton =
            By.xpath("//XCUIElementTypeButton[@name='CREATE GROUP']");

    private static final By nameTopPeopleLabel = MobileBy.AccessibilityId("TOP PEOPLE");

    private static final By nameUnblockButton = MobileBy.AccessibilityId("UNBLOCK");

    private static final By nameInviteCopyButton = MobileBy.AccessibilityId("Copy");

    private static final By nameInviteMorePeopleButton = MobileBy.AccessibilityId("INVITE MORE PEOPLE");

    private static final Function<String, String> xpathStrInstantConnectButtonByUserName = name -> String.format(
            "//XCUIElementTypeCell[ .//XCUIElementTypeStaticText[@name='%s'] ]" +
                    "//XCUIElementTypeButton[@name='instantPlusConnectButton']", name);

    private static final By nameLaterButton = MobileBy.AccessibilityId("MAYBE LATER");

    private static final By nameOpenConversationButton = MobileBy.AccessibilityId("OPEN");

    private static final By nameCallButton = MobileBy.AccessibilityId("actionBarCallButton");

    private static final By nameSendImageButton = MobileBy.AccessibilityId("actionBarCameraButton");

    private static final By nameContactViewCloseButton = MobileBy.AccessibilityId("ContactsViewCloseButton");

    private static final Function<String, String> xpathStrFoundContactByName =
            name -> String.format("//XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s'] ]", name);

    private static final Function<Integer, String> xpathStrTopPeopleAvatarByIdx = idx ->
            String.format("//XCUIElementTypeCollectionView/XCUIElementTypeCell" +
                    "//XCUIElementTypeCollectionView/XCUIElementTypeCell[%s]", idx);

    private static final Function<Integer, String> xpathStrTopPeopleItemByIdx = idx ->
            String.format("//XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeCollectionView/" +
                    "XCUIElementTypeCell[%d]/XCUIElementTypeStaticText[last()]", idx);

    private static final Function<String, String> xpathStrFirstSearchResultEntryByName = name -> String.format(
            "//XCUIElementTypeCollectionView/XCUIElementTypeCell//XCUIElementTypeStaticText[@name='%s']", name);

    private static final By nameNoResults = MobileBy.AccessibilityId("No results.");

    private static final By nameVideoCallButton = MobileBy.AccessibilityId("actionBarVideoCallButton");

    private static final BiFunction<String, String, String> xpathStrFoundUserDetailsByName =
            (name, expectedDetails) -> {
                if (expectedDetails.trim().isEmpty()) {
                    return String.format(
                            "//XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s'] and " +
                                    "not(./XCUIElementTypeStaticText[@name='additionalUserInfo']) ]",
                            name
                    );
                }
                return String.format(
                        "//XCUIElementTypeCell[ ./XCUIElementTypeStaticText[@name='%s'] ]" +
                                "/XCUIElementTypeStaticText[@name='additionalUserInfo' and @value='%s']",
                        name, expectedDetails
                );
            };

    public SearchUIPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapSearchInput() throws Exception {
        getElement(xpathSearchInput).click();
        if (!this.isKeyboardVisible()) {
            throw new IllegalStateException(
                    "On-screen keyboard is expected to be shown after click on search input field"
            );
        }
    }

    public boolean isVisible() throws Exception {
        return isLocatorDisplayed(xpathSearchInput);
    }

    public void typeText(String text) throws Exception {
        final WebElement searchInput = getElement(xpathSearchInput);
        searchInput.sendKeys(text + " ");
        // Wait for a user to be found
        Thread.sleep(2000);
    }

    private static By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "x":
                return fbNameXButton;
            case "unblock":
                return nameUnblockButton;
            case "send invite":
                return nameInviteMorePeopleButton;
            case "copy invite":
                return nameInviteCopyButton;
            case "close group participants picker":
            case "clear group participants picker":
                return nameContactViewCloseButton;
            default:
                throw new IllegalArgumentException(String.format("There is no '%s' button on Search UI page", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        getElement(locator).click();
    }

    public boolean isButtonVisible(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        return isLocatorDisplayed(locator);
    }

    public Optional<WebElement> getSearchResultsElement(String user) throws Exception {
        final By locator = By.xpath(xpathStrFoundContactByName.apply(user));
        return getElementIfDisplayed(locator);
    }

    public boolean isElementNotFoundInSearch(String name) throws Exception {
        final By locator = By.xpath(xpathStrFoundContactByName.apply(name));
        return !isLocatorDisplayed(locator, 2);
    }

    public void selectElementInSearchResults(String name) throws Exception {
        getSearchResultsElement(name).orElseThrow(
                () -> new IllegalStateException(String.format("User '%s' is not visible in people picker", name))).
                click();
        // Wait for the animation
        Thread.sleep(1000);
    }

    public void tapNumberOfTopConnections(int numberToTap) throws Exception {
        for (int i = 1; i <= numberToTap; i++) {
            final By locator = By.xpath(xpathStrTopPeopleAvatarByIdx.apply(i));
            getElement(locator).click();
            // Wait for animation
            Thread.sleep(1000);
        }
    }

    public boolean isTopPeopleLabelVisible() throws Exception {
        return isLocatorDisplayed(nameTopPeopleLabel, 2);
    }

    public void pressBackspaceKeyboardButton() throws Exception {
        getElement(xpathSearchInput).sendKeys(Keys.BACK_SPACE);
    }

    public void tapInstantConnectButton(String forName) throws Exception {
        final By locator = By.xpath(xpathStrInstantConnectButtonByUserName.apply(forName));
        tapElementWithRetryIfStillDisplayed(locator);
        // Wait for animation
        Thread.sleep(1000);
    }

    public void tapNumberOfTopConnectionsButNotUser(int numberToTap, String contact) throws Exception {
        for (int i = 1; i <= numberToTap; i++) {
            final By locator = By.xpath(xpathStrTopPeopleItemByIdx.apply(i));
            final WebElement el = getElement(locator);
            if (!contact.equalsIgnoreCase(el.getAttribute("name"))) {
                el.click();
            } else {
                numberToTap++;
            }
        }
    }

    public void tapOnTopConnectionAvatarByOrder(int i) throws Exception {
        final By locator = By.xpath(xpathStrTopPeopleAvatarByIdx.apply(i));
        getElement(locator).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    public boolean waitUntilNoResultsLabelIsVisible() throws Exception {
        return isLocatorDisplayed(nameNoResults);
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

    public void tapActionButton(String actionButtonName) throws Exception {
        getElement(getActionButtonByName(actionButtonName)).click();
    }

    public boolean isActionButtonVisible(String actionButtonName) throws Exception {
        return isLocatorDisplayed(getActionButtonByName(actionButtonName));
    }

    public boolean isActionButtonInvisible(String actionButtonName) throws Exception {
        return isLocatorInvisible(getActionButtonByName(actionButtonName));
    }

    public boolean isShareContactsSettingsWarningShown() throws Exception {
        return isLocatorDisplayed(nameLaterButton);
    }

    public boolean isFirstItemInSearchResult(String name) throws Exception {
        final By locator = By.xpath(xpathStrFirstSearchResultEntryByName.apply(name));
        return isLocatorDisplayed(locator);
    }

    public boolean isButtonInvisible(String btnName) throws Exception {
        final By locator = getButtonLocatorByName(btnName);
        return isLocatorInvisible(locator);
    }

    public void clearSearchInput() throws Exception {
        getElement(xpathSearchInput).clear();
    }

    public boolean isSearchResultDetailsVisible(String textToEnter, String expectedDetails) throws Exception {
        final By locator = By.xpath(xpathStrFoundUserDetailsByName.apply(textToEnter, expectedDetails));
        return isLocatorDisplayed(locator);
    }
}
