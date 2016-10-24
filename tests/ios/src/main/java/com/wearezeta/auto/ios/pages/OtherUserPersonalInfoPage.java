package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DummyElement;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class OtherUserPersonalInfoPage extends IOSPage {
    private static final By fbNameRemoveFromConversation =
            FBBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final By nameConfirmRemoveButton = MobileBy.AccessibilityId("REMOVE");

    private static final By fbNameOtherUserAddContactToChatButton =
            FBBy.AccessibilityId("OtherUserMetaControllerLeftButton");

    private static final By nameContinueButton = MobileBy.AccessibilityId("CONTINUE");

    private static final By nameExitOtherUserPersonalInfoPageButton =
            MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    private static final By xpathConfirmDeleteButton =
            By.xpath("//XCUIElementTypeButton[@name='CANCEL']/following::XCUIElementTypeButton[@name='DELETE']");

    private static final By nameAlsoLeaveCheckerButton = MobileBy.AccessibilityId("ALSO LEAVE THE CONVERSATION");

    private static final Function<String, String> xpathStrOtherPersonalInfoPageNameFieldByName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", name);

    private static final Function<String, String> xpathStrOtherPersonalInfoPageAddressBookNameFieldByName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", name);

    private static final Function<String, String> xpathStrOtherPersonalInfoPageEmailFieldByEmail = email ->
            String.format("//XCUIElementTypeTextView[@value='%s']", email.toUpperCase());

    private static final By nameAddContactToChatButton = MobileBy.AccessibilityId("metaControllerLeftButton");

    protected static final By fbNameOtherUserConversationMenu =
            FBBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final By nameConversationMenu = MobileBy.AccessibilityId("metaControllerRightButton");

    private static final By xpathActionsMenu = By.xpath("//XCUIElementTypeButton[@name='CANCEL']");

    private static final String xpathStrDevicesList = "//XCUIElementTypeTable/XCUIElementTypeCell";
    private static final Function<Integer, String> xpathStrDevicesByCount = count ->
            String.format("//XCUIElementTypeTable[count(XCUIElementTypeCell)=%s]", count);
    private static final Function<Integer, String> xpathStrDeviceByIndex =
            idx -> String.format("%s[%s]", xpathStrDevicesList, idx);

    private static final Function<String, String> xpathStrUserProfileNameByValue = value ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", value);

    private static final By xpathVerifiedShield = MobileBy.AccessibilityId("VerifiedShield");

    private static final Function<String, String> xpathStrDeviceId = id ->
            String.format("//XCUIElementTypeStaticText[contains(@name, '%s')]", id);

    private static final Function<String, String> xpathStrLinkBlockByText = text ->
            String.format("//*[contains(@value, '%s')]", text);

    public OtherUserPersonalInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void catchContinueAlert() throws Exception {
        getElementIfDisplayed(nameContinueButton).orElseGet(DummyElement::new).click();
    }

    public void openEllipsisMenu() throws Exception {
        openConversationMenu();
    }

    public void clickConfirmDeleteButton() throws Exception {
        getElement(xpathConfirmDeleteButton, "Confirm button is not visible").click();
        // Wait for animation
        Thread.sleep(2000);
    }

    public void clickAlsoLeaveButton() throws Exception {
        getElement(nameAlsoLeaveCheckerButton, "'Also Leave' checkbox is not present").click();
    }

    public void tapCloseUserProfileButton() throws Exception {
        getElement(nameExitOtherUserPersonalInfoPageButton, "Close profile button is not visible").click();
        Thread.sleep(1000);
    }

    public void addContactToChat() throws Exception {
        final Optional<WebElement> addButton = getElementIfDisplayed(nameAddContactToChatButton, 2);
        if (addButton.isPresent()) {
            addButton.get().click();
        } else {
            getElement(fbNameOtherUserAddContactToChatButton).click();
        }
        catchContinueAlert();
    }

    public boolean isOtherUserProfileNameVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserProfileNameByValue.apply(name));
        return isLocatorDisplayed(locator);
    }

    public void removeFromConversation() throws Exception {
        this.tapAtTheCenterOfElement((FBElement) getElement(fbNameRemoveFromConversation));
        // Wait for animation
        Thread.sleep(1000);
    }

    public void confirmRemove() throws Exception {
        final WebElement confirmBtn = getElement(nameConfirmRemoveButton);
        confirmBtn.click();
        if (!isLocatorInvisible(nameConfirmRemoveButton)) {
            confirmBtn.click();
        }
    }

    public boolean isUserNameVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageNameFieldByName.apply(name));
        return isLocatorDisplayed(locator);
    }

    public boolean isUserAddressBookNameVisible(String addressbookName) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageAddressBookNameFieldByName.apply(addressbookName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isUserEmailVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageEmailFieldByEmail.apply(email));
        return isLocatorDisplayed(locator);
    }

    public boolean isUserEmailNotVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageEmailFieldByEmail.apply(email));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapStartConversationButton() throws Exception {
        this.tapAtTheCenterOfElement((FBElement) getElement(fbNameOtherUserAddContactToChatButton));
    }

    public void openConversationMenu() throws Exception {
        final Optional<WebElement> conversationMenuButton = getElementIfDisplayed(nameConversationMenu, 2);
        if (conversationMenuButton.isPresent()) {
            conversationMenuButton.get().click();
        } else {
            getElement(fbNameOtherUserConversationMenu).click();
        }
        // Wait for animation
        Thread.sleep(1000);
    }

    public boolean isActionMenuVisible() throws Exception {
        return isLocatorDisplayed(xpathActionsMenu);
    }

    public boolean isParticipantDevicesCountEqualTo(int expectedCount) throws Exception {
        final By locator = By.xpath(xpathStrDevicesByCount.apply(expectedCount));
        return isLocatorDisplayed(locator);
    }

    public void openDeviceDetailsPage(int deviceIndex) throws Exception {
        final By locator = By.xpath(xpathStrDeviceByIndex.apply(deviceIndex));
        getElement(locator).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    public boolean isShieldIconVisible() throws Exception {
        return isLocatorDisplayed(xpathVerifiedShield);
    }

    public boolean isShieldIconNotVisible() throws Exception {
        return isLocatorInvisible(xpathVerifiedShield);
    }

    public void switchToTab(String tabName) throws Exception {
        getElement(MobileBy.AccessibilityId(tabName.toUpperCase())).click();
        // Wait for animation
        Thread.sleep(1000);
    }

    private String convertStringIDtoLocatorTypeID(String id) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < id.length(); i += 2) {
            sb.append(id.substring(i, i + 2)).append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    public boolean isUserDeviceIdVisible(String deviceId) throws Exception {
        String locator = xpathStrDeviceId.apply(convertStringIDtoLocatorTypeID(deviceId));
        return isLocatorExist(By.xpath(locator));
    }

    public void tapLink(String expectedLink) throws Exception {
        final By locator = By.xpath(xpathStrLinkBlockByText.apply(expectedLink));
        DriverUtils.tapOnPercentOfElement(getDriver(), getElement(locator), 15, 95);
    }
}
