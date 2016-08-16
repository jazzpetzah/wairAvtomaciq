package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DummyElement;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class OtherUserPersonalInfoPage extends IOSPage {

    private static final By nameRemoveFromConversation = MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final By nameConfirmRemoveButton = MobileBy.AccessibilityId("REMOVE");

    private static final By nameOtherUserAddContactToChatButton = MobileBy.AccessibilityId("OtherUserMetaControllerLeftButton");

    private static final By nameContinueButton = MobileBy.AccessibilityId("CONTINUE");

    private static final By nameExitOtherUserPersonalInfoPageButton = MobileBy.AccessibilityId("OtherUserProfileCloseButton");

    private static final By xpathConfirmDeleteButton = By
        .xpath("//UIAButton[@name='CANCEL']/following-sibling::UIAButton[@name='DELETE']");

    private static final By nameAlsoLeaveCheckerButton = MobileBy.AccessibilityId("ALSO LEAVE THE CONVERSATION");

    private static final Function<String, String> xpathStrOtherPersonalInfoPageNameFieldByName = name -> String.format(
        "%s/UIAStaticText[@name='%s']", xpathStrMainWindow, name);

    private static final Function<String, String> xpathStrOtherPersonalInfoPageEmailFieldByEmail = name -> String.format(
        "//UIAButton[@name='OtherUserProfileCloseButton']/following-sibling:: UIATextView[@name='%s']", name.toUpperCase());

    protected static final By xpathOtherPersonalInfoPageEmailField = By
        .xpath("//UIAButton[@name='OtherUserProfileCloseButton']/following-sibling:: UIATextView");

    private static final By nameAddContactToChatButton = MobileBy.AccessibilityId("metaControllerLeftButton");

    protected static final By nameOtherUserConversationMenu = MobileBy.AccessibilityId("OtherUserMetaControllerRightButton");

    private static final By nameConversationMenu = MobileBy.AccessibilityId("metaControllerRightButton");

    private static final By xpathActionMenu = By
        .xpath("//UIAStaticText[following-sibling::UIAButton[@name='CANCEL'] and @visible='true']");

    private static final String xpathStrDevicesList = xpathStrMainWindow + "/UIATableView[1]/UIATableCell";
    private static final By xpathDevicesList = By.xpath(xpathStrDevicesList);
    private static final Function<Integer, String> xpathStrDeviceByIndex = idx -> String.format("%s[%s]", xpathStrDevicesList,
        idx);

    private static final Function<String, String> xpathStrUserProfileNameByValue = value -> String.format(
        "//*[@name='%s' and @visible='true']", value);

    // FIXME: replace this with MobileBy.AccessibilityId("VerifiedShield") when available
    private static final By xpathVerifiedShield = By.xpath(xpathStrMainWindow + "/UIAImage[@width='16' and @height='16']");

    private static final Function<String, String> xpathStrDeviceId = id -> String.format(
        "//UIAStaticText[contains(@name, '%s')]", id);

    private static final Function<String, String> xpathStrLinkBlockByText = text -> String.format(
            "//*[contains(@name, '%s')]", text);

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
    }

    public void clickAlsoLeaveButton() throws Exception {
        getElement(nameAlsoLeaveCheckerButton, "'Also Leave' checkbox is not present").click();
    }

    public void clickCloseUserProfileButton() throws Exception {
        getElement(nameExitOtherUserPersonalInfoPageButton, "Close profile button is not visible").click();
    }

    public void addContactToChat() throws Exception {
        final Optional<WebElement> addButton = getElementIfDisplayed(nameAddContactToChatButton, 2);
        if (addButton.isPresent()) {
            addButton.get().click();
        } else {
            getElement(nameOtherUserAddContactToChatButton).click();
        }
        catchContinueAlert();
    }

    public boolean isOtherUserProfileNameVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrUserProfileNameByValue.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void removeFromConversation() throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(), getElement(nameRemoveFromConversation));
        // Wait for animation
        Thread.sleep(1000);
    }

    public void confirmRemove() throws Exception {
        final WebElement confirmBtn = getElement(nameConfirmRemoveButton);
        confirmBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameConfirmRemoveButton)) {
            confirmBtn.click();
        }
    }

    public boolean isUserNameVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageNameFieldByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isUserEmailVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageEmailFieldByEmail.apply(email));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isUserEmailNotVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageEmailFieldByEmail.apply(email));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapStartConversationButton() throws Exception {
        this.getDriver().tap(1, getElement(nameOtherUserAddContactToChatButton), 1);
    }

    public void openConversationMenu() throws Exception {
        final Optional<WebElement> conversationMenuButton = getElementIfDisplayed(nameConversationMenu, 2);
        if (conversationMenuButton.isPresent()) {
            conversationMenuButton.get().click();
        } else {
            getElement(nameOtherUserConversationMenu).click();
        }
    }

    public boolean isActionMenuVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathActionMenu);
    }

    public int getParticipantDevicesCount() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathDevicesList)) {
            return getElements(xpathDevicesList).size();
        }
        return 0;
    }

    public void openDeviceDetailsPage(int deviceIndex) throws Exception {
        final By locator = By.xpath(xpathStrDeviceByIndex.apply(deviceIndex));
        getElement(locator).click();
    }

    public boolean isShieldIconVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathVerifiedShield);
    }

    public boolean isShieldIconNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathVerifiedShield);
    }

    public void switchToTab(String tabName) throws Exception {
        getElement(MobileBy.AccessibilityId(tabName.toUpperCase())).click();
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
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(locator));
    }

    public void tapLink(String expectedLink) throws Exception {
        final By locator = By.xpath(xpathStrLinkBlockByText.apply(expectedLink));
        DriverUtils.tapOnPercentOfElement(getDriver(), getElement(locator), 15, 95);
    }
}
