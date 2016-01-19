package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class OtherUserPersonalInfoPage extends IOSPage {
    private static final String nameOtherUserEmailField = "ProfileOtherEmailField";
    @FindBy(name = nameOtherUserEmailField)
    private WebElement otherUserEmail;

    private static final String nameRemoveFromConversation = "OtherUserMetaControllerRightButton";
    @FindBy(name = nameRemoveFromConversation)
    private WebElement removeFromChat;

    private static final String nameComfirmRemoveButton = "REMOVE";
    @FindBy(name = nameComfirmRemoveButton)
    private WebElement confirmRemove;

    private static final String nameOtherUserAddContactToChatButton = "OtherUserMetaControllerLeftButton";
    @FindBy(name = nameOtherUserAddContactToChatButton)
    private WebElement addOtherUserButton;

    private static final String nameContinueButton = "CONTINUE";
    @FindBy(name = nameContinueButton)
    private WebElement continueButton;

    private static final String nameExitOtherUserPersonalInfoPageButton = "OtherUserProfileCloseButton";
    @FindBy(name = nameExitOtherUserPersonalInfoPageButton)
    private WebElement exitOtherPersonalInfoPageButton;

    private static final String xpathArchiveButton = "//UIAButton[@name='ARCHIVE']";
    @FindBy(xpath = xpathArchiveButton)
    private WebElement archiveButton;

    private static final String xpathDeleteConversationButton = "//UIAButton[@name='DELETE' and @visible='true']";
    @FindBy(xpath = xpathDeleteConversationButton)
    private WebElement deleteButton;

    private static final String xpathConfirmDeleteButton =
            "//UIAButton[@name='CANCEL']/following-sibling::UIAButton[@name='DELETE']";
    @FindBy(xpath = xpathConfirmDeleteButton)
    private WebElement confirmDeleteButton;

    private static final String nameAlsoLeaveCheckerButton = "ALSO LEAVE THE CONVERSATION";
    @FindBy(name = nameAlsoLeaveCheckerButton)
    private WebElement alsoLeaveButton;

    private static final Function<String, String> xpathOtherPersonalInfoPageNameFieldByName = name ->
            String.format("%s/UIAStaticText[@name='%s']", xpathStrMainWindow, name);

    protected static final String xpathOtherPersonalInfoPageEmailField =
            xpathStrMainWindow + "/UIATextView[contains(@name, 'WIRE.COM')]";
    @FindBy(xpath = xpathOtherPersonalInfoPageEmailField)
    private WebElement emailField;

    private static final String nameAddContactToChatButton = "metaControllerLeftButton";
    @FindBy(name = nameAddContactToChatButton)
    private WebElement startDialogButton;
    @FindBy(name = nameAddContactToChatButton)
    private WebElement addButton;

    protected static final String nameOtherUserConversationMenu = "OtherUserMetaControllerRightButton";
    @FindBy(name = nameOtherUserConversationMenu)
    private WebElement otherUserConversationMenuButton;

    private static final String nameConversationMenu = "metaControllerRightButton";
    @FindBy(name = nameConversationMenu)
    private WebElement conversationMenuButton;

    private static final String nameSilenceConversationButton = "SILENCE";
    @FindBy(name = nameSilenceConversationButton)
    private WebElement silenceMenuButton;

    private static final String xpathSilenceConversationButton = xpathStrMainWindow + "/UIAButton[@name='SILENCE']";
    @FindBy(xpath = xpathSilenceConversationButton)
    private WebElement menuSilenceButton;

    private static final String nameUnsilenceConversationButton = "NOTIFY";
    @FindBy(name = nameUnsilenceConversationButton)
    private WebElement notifyMenuButton;

    private static final String nameBlockMenuButton = "BLOCK";
    @FindBy(name = nameBlockMenuButton)
    private WebElement blockMenuButton;

    private static final String nameCancelButton = "CANCEL";
    @FindBy(name = nameCancelButton)
    private WebElement cancelButton;

    private static final String xpathActionMenu =
            "//UIAStaticText[following-sibling::UIAButton[@name='CANCEL'] and @visible='true']";
    @FindBy(xpath = xpathActionMenu)
    private WebElement actionMenu;

    public OtherUserPersonalInfoPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void catchContinueAlert() throws Exception {
        if (DriverUtils.waitUntilLocatorAppears(getDriver(),
                By.name(nameContinueButton), 5)) {
            WebElement el = this.getDriver().findElementByName(nameContinueButton);
            DriverUtils.waitUntilElementClickable(getDriver(), el);
            el.click();
        }
    }

    public void openEllipsisMenu() throws Exception {
        openConversationMenu();
    }

    public void clickArchiveMenuButton() {
        archiveButton.click();
    }

    public void clickDeleteMenuButton() {
        deleteButton.click();
    }

    public void clickConfirmDeleteButton() throws Exception {
        getElement(By.xpath(xpathConfirmDeleteButton), "Confirm button is not visible").click();
    }

    public void clickAlsoLeaveButton() throws Exception {
        getElement(By.name(nameAlsoLeaveCheckerButton), "'Also Leave' checkbox is not present").click();
    }

    public void leavePageToGroupInfoPage() throws Exception {
        exitOtherPersonalInfoPageButton.click();
    }

    public void clickCloseUserProfileButton() throws Exception {
        getElement(By.name(nameExitOtherUserPersonalInfoPageButton),
                "Close profile button is not visible").click();
    }

    public void addContactToChat() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameAddContactToChatButton), 2)) {
            addButton.click();
        } else {
            addOtherUserButton.click();
        }
        catchContinueAlert();
    }

    public boolean isOtherUserProfileNameVisible(String name) throws Exception {
        WebElement otherUserName = getDriver().findElementByName(name);
        return otherUserName.isEnabled();
    }

    public void removeFromConversation() throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(), removeFromChat);
    }

    public boolean isRemoveFromConversationAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameComfirmRemoveButton));
    }

    public void confirmRemove() {
        confirmRemove.click();
    }

    public String getNameFieldValue(String user) throws Exception {
        final By locator = By.xpath(xpathOtherPersonalInfoPageNameFieldByName.apply(user));
        return getDriver().findElement(locator).getAttribute("name");
    }

    public String getEmailFieldValue() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathOtherPersonalInfoPageEmailField))) {
            return emailField.getAttribute("value");
        } else {
            return "";
        }
    }

    public void clickOnStartDialogButton() throws Exception {
        this.getDriver().tap(1, this.getDriver().findElementByName(nameOtherUserAddContactToChatButton), 1);
    }

    public void openConversationMenu() throws Exception {
        if (DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(nameConversationMenu), 2)) {
            conversationMenuButton.click();
        } else {
            otherUserConversationMenuButton.click();
        }
        Thread.sleep(2000);
    }

    public void clickSilenceMenuButton() throws InterruptedException {
        menuSilenceButton.click();
    }

    public void clickNotifyMenuButton() throws InterruptedException {
        notifyMenuButton.click();
        Thread.sleep(2000);
    }

    public void clickBlockMenuButton() {
        blockMenuButton.click();
    }

    public void clickCancelButton() {
        cancelButton.click();
    }

    public boolean isActionMenuVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathActionMenu));
    }

}
