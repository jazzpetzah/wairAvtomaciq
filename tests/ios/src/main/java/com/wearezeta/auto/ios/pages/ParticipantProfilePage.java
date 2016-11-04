package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class ParticipantProfilePage extends IOSPage {
    private static final By nameRightActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final By nameLeftActionButton = MobileBy.AccessibilityId("OtherUserMetaControllerLeftButton");

    private static final By nameExitOtherUserPersonalInfoPageButton =
            MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    private static final Function<String, String> xpathStrOtherPersonalInfoPageNameFieldByName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", name);

    private static final Function<String, String> xpathStrOtherPersonalInfoPageAddressBookNameFieldByName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']", name);

    private static final Function<String, String> xpathStrOtherPersonalInfoPageEmailFieldByEmail = email ->
            String.format("//XCUIElementTypeTextView[@value='%s']", email.toUpperCase());

    private static final By xpathActionsMenu = By.xpath("//XCUIElementTypeButton[@name='CANCEL']");

    private static final String xpathStrDevicesList = "//XCUIElementTypeTable/XCUIElementTypeCell";
    private static final Function<Integer, String> xpathStrDevicesByCount = count ->
            String.format("//XCUIElementTypeTable[count(XCUIElementTypeCell)=%s]", count);
    private static final Function<Integer, String> xpathStrDeviceByIndex =
            idx -> String.format("%s[%s]", xpathStrDevicesList, idx);

    private static final By xpathVerifiedShield = MobileBy.AccessibilityId("VerifiedShield");

    private static final Function<String, String> xpathStrDeviceId = id ->
            String.format("//XCUIElementTypeStaticText[contains(@name, '%s')]", id);

    private static final Function<String, String> xpathStrLinkBlockByText = text ->
            String.format("//*[contains(@value, '%s')]", text);

    public ParticipantProfilePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isOtherUserProfileNameVisible(String name) throws Exception {
        final By locator = MobileBy.AccessibilityId(name);
        return selectVisibleElements(locator).size() > 0;
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

    private static By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "create group":
            case "open conversation":
                return nameLeftActionButton;
            case "remove from conversation":
            case "open menu":
                return nameRightActionButton;
            case "confirm removal":
                return xpathConfirmRemoveButton;
            case "confirm deletion":
                return xpathConfirmDeleteButton;
            case "x":
                return nameExitOtherUserPersonalInfoPageButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        getElement(locator).click();
        // Wait for animation
        Thread.sleep(1000);
    }
}
