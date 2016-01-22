package com.wearezeta.auto.ios.pages;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.driver.DummyElement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class OtherUserPersonalInfoPage extends IOSPage {

    private static final By nameRemoveFromConversation = By.name("OtherUserMetaControllerRightButton");

    private static final By nameComfirmRemoveButton = By.name("REMOVE");

    private static final By nameOtherUserAddContactToChatButton = By.name("OtherUserMetaControllerLeftButton");

    private static final By nameContinueButton = By.name("CONTINUE");

    private static final By nameExitOtherUserPersonalInfoPageButton = By.name("OtherUserProfileCloseButton");

    private static final By xpathArchiveButton = By.xpath("//UIAButton[@name='ARCHIVE']");

    private static final By xpathDeleteConversationButton = By.xpath("//UIAButton[@name='DELETE' and @visible='true']");

    private static final By xpathConfirmDeleteButton = By
        .xpath("//UIAButton[@name='CANCEL']/following-sibling::UIAButton[@name='DELETE']");

    private static final By nameAlsoLeaveCheckerButton = By.name("ALSO LEAVE THE CONVERSATION");

    private static final Function<String, String> xpathStrOtherPersonalInfoPageNameFieldByName = name -> String.format(
        "%s/UIAStaticText[@name='%s']", xpathStrMainWindow, name);
    
    private static final Function<String, String> xpathStrOtherPersonalInfoPageEmailFieldByEmail = name -> String.format(
        "//UIAButton[@name='OtherUserProfileCloseButton']/following-sibling:: UIATextView[@name='%s']", name);

    protected static final By xpathOtherPersonalInfoPageEmailField = By
        .xpath("//UIAButton[@name='OtherUserProfileCloseButton']/following-sibling:: UIATextView");

    private static final By nameAddContactToChatButton = By.name("metaControllerLeftButton");

    protected static final By nameOtherUserConversationMenu = By.name("OtherUserMetaControllerRightButton");

    private static final By nameConversationMenu = By.name("metaControllerRightButton");

    private static final By xpathSilenceConversationButton = By.xpath(xpathStrMainWindow + "/UIAButton[@name='SILENCE']");

    private static final By nameUnsilenceConversationButton = By.name("NOTIFY");

    private static final By nameBlockMenuButton = By.name("BLOCK");

    private static final By nameCancelButton = By.name("CANCEL");

    private static final By xpathActionMenu = By
        .xpath("//UIAStaticText[following-sibling::UIAButton[@name='CANCEL'] and @visible='true']");

    public OtherUserPersonalInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void catchContinueAlert() throws Exception {
        getElementIfDisplayed(nameContinueButton).orElseGet(DummyElement::new).click();
    }

    public void openEllipsisMenu() throws Exception {
        openConversationMenu();
    }

    public void clickArchiveMenuButton() throws Exception {
        getElement(xpathArchiveButton).click();
    }

    public void clickDeleteMenuButton() throws Exception {
        getElement(xpathDeleteConversationButton).click();
    }

    public void clickConfirmDeleteButton() throws Exception {
        getElement(xpathConfirmDeleteButton, "Confirm button is not visible").click();
    }

    public void clickAlsoLeaveButton() throws Exception {
        getElement(nameAlsoLeaveCheckerButton, "'Also Leave' checkbox is not present").click();
    }

    public void leavePageToGroupInfoPage() throws Exception {
        getElement(nameExitOtherUserPersonalInfoPageButton).click();
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
        return getElement(By.name(name)).isEnabled();
    }

    public void removeFromConversation() throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(), getElement(nameRemoveFromConversation));
    }

    public boolean isRemoveFromConversationAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameComfirmRemoveButton);
    }

    public void confirmRemove() throws Exception {
        getElement(nameComfirmRemoveButton).click();
    }

    public boolean isUserNameVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageNameFieldByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
    
    public boolean isUserEmailVisible(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageEmailFieldByEmail.apply(email.toUpperCase()));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
    
    public boolean userEmailIsNotDisplayed(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageEmailFieldByEmail.apply(email));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public Optional<String> getEmailFieldValue(String email) throws Exception {
        final By locator = By.xpath(xpathStrOtherPersonalInfoPageEmailFieldByEmail.apply(email));
        final Optional<WebElement> emailField = getElementIfDisplayed(locator);
        if (emailField.isPresent()) {
            return Optional.of(emailField.get().getAttribute("value"));
        } else {
            return Optional.empty();
        }
    }

    public boolean emailIsNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOtherPersonalInfoPageEmailField);
    }

    public void clickOnStartDialogButton() throws Exception {
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

    public void clickSilenceMenuButton() throws Exception {
        getElement(xpathSilenceConversationButton).click();
    }

    public void clickNotifyMenuButton() throws Exception {
        getElement(nameUnsilenceConversationButton).click();
    }

    public void clickBlockMenuButton() throws Exception {
        getElement(nameBlockMenuButton).click();
    }

    public void clickCancelButton() throws Exception {
        getElement(nameCancelButton).click();
    }

    public boolean isActionMenuVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathActionMenu);
    }

}
