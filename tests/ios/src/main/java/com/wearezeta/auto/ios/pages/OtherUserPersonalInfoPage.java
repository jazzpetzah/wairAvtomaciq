package com.wearezeta.auto.ios.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class OtherUserPersonalInfoPage extends IOSPage {
	public static final String nameOtherUserEmailField = "ProfileOtherEmailField";
	@FindBy(name = nameOtherUserEmailField)
	private WebElement otherUserEmail;

    public static final String nameRemoveFromConversation = "OtherUserMetaControllerRightButton";
    @FindBy(name = nameRemoveFromConversation)
	private WebElement removeFromChat;

    public static final String nameComfirmRemoveButton = "REMOVE";
    @FindBy(name = nameComfirmRemoveButton)
	private WebElement confirmRemove;

    public static final String nameOtherUserAddContactToChatButton = "OtherUserMetaControllerLeftButton";
    @FindBy(name = nameOtherUserAddContactToChatButton)
	private WebElement addOtherUserButton;

    public static final String nameContinueButton = "CONTINUE";
    @FindBy(name = nameContinueButton)
	private WebElement continueButton;

    public static final String nameExitOtherUserPersonalInfoPageButton = "OtherUserProfileCloseButton";
    @FindBy(name = nameExitOtherUserPersonalInfoPageButton)
	private WebElement exitOtherPersonalInfoPageButton;

    public static final String xpathArchiveButton = "//UIAButton[@name='ARCHIVE']";
    @FindBy(xpath = xpathArchiveButton)
	private WebElement archiveButton;

    public static final String xpathDeleteConversationButton = "//UIAButton[@name='DELETE' and @visible='true']";
    @FindBy(xpath = xpathDeleteConversationButton)
	private WebElement deleteButton;

    public static final String xpathConfirmDeleteButton =
            "//UIAButton[@name='CANCEL']/following-sibling::UIAButton[@name='DELETE']";
    @FindBy(xpath = xpathConfirmDeleteButton)
	private WebElement confirmDeleteButton;

    public static final String nameAlsoLeaveCheckerButton = "ALSO LEAVE THE CONVERSATION";
    @FindBy(name = nameAlsoLeaveCheckerButton)
	private WebElement alsoLeaveButton;

    public static final String xpathOtherPersonalInfoPageNameField =
            "//UIAWindow[@name='ZClientMainWindow']/UIAStaticText[@name='%s']";
    @FindBy(xpath = xpathOtherPersonalInfoPageNameField)
	private List<WebElement> nameField;

    public static final String xpathOtherPersonalInfoPageEmailField =
            "//UIAWindow[@name='ZClientMainWindow']/UIATextView[contains(@name, 'WIRE.COM')]";
    @FindBy(xpath = xpathOtherPersonalInfoPageEmailField)
	private WebElement emailField;

    public static final String nameAddContactToChatButton = "metaControllerLeftButton";
    @FindBy(name = nameAddContactToChatButton)
	private WebElement startDialogButton;
    @FindBy(name = nameAddContactToChatButton)
	private WebElement addButton;

    public static final String nameOtherUserConversationMenu = "OtherUserMetaControllerRightButton";
    @FindBy(name = nameOtherUserConversationMenu)
	private WebElement otherUserConversationMenuButton;

    public static final String nameConversationMenu = "metaControllerRightButton";
    @FindBy(name = nameConversationMenu)
	private WebElement conversationMenuButton;

    public static final String nameSilenceConversationButton = "SILENCE";
    @FindBy(name = nameSilenceConversationButton)
	private WebElement silenceMenuButton;

    public static final String xpathSilenceConversationButton =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAButton[@name='SILENCE']";
    @FindBy(xpath = xpathSilenceConversationButton)
	private WebElement menuSilenceButton;

    public static final String nameUnsilenceConversationButton = "NOTIFY";
    @FindBy(name = nameUnsilenceConversationButton)
	private WebElement notifyMenuButton;

    public static final String nameBlockMenuButton = "BLOCK";
    @FindBy(name = nameBlockMenuButton)
	private WebElement blockMenuButton;

    public static final String nameCancelButton = "CANCEL";
    @FindBy(name = nameCancelButton)
	private WebElement cancelButton;

    public static final String xpathActionMenu =
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
		DriverUtils.waitUntilElementClickable(getDriver(), confirmDeleteButton);
		confirmDeleteButton.click();
	}

	public void clickAlsoLeaveButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), alsoLeaveButton);
		alsoLeaveButton.click();
	}

	public IOSPage leavePageToGroupInfoPage() throws Exception {
		exitOtherPersonalInfoPageButton.click();
		return new GroupChatInfoPage(this.getLazyDriver());
	}

	public void clickCloseUserProfileButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(),
				exitOtherPersonalInfoPageButton);
		exitOtherPersonalInfoPageButton.click();
	}

	public PeoplePickerPage addContactToChat() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(nameAddContactToChatButton), 2)) {
			addButton.click();
		} else {
			addOtherUserButton.click();
		}
		catchContinueAlert();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public boolean isOtherUserProfileNameVisible(String name) throws Exception {
		WebElement otherUserName = getDriver().findElementByName(name);
		return otherUserName.isEnabled();
	}

	public void removeFromConversation() throws Exception {
		DriverUtils.tapByCoordinates(this.getDriver(), removeFromChat);
	}

	public boolean isRemoveFromConversationAlertVisible() {
		return confirmRemove.isDisplayed();
	}

	public void confirmRemove() {
		confirmRemove.click();
	}

	public String getNameFieldValue(String user) throws Exception {
		WebElement name = getDriver().findElement(
				By.xpath(String.format(xpathOtherPersonalInfoPageNameField, user)));
		return name.getAttribute("name");
	}

	public String getEmailFieldValue() throws Exception {
        if (DriverUtils.waitUntilLocatorAppears(getDriver(),
					By.xpath(xpathOtherPersonalInfoPageEmailField))) {
            return emailField.getAttribute("value");
        } else {
            return "";
		}
	}

	public void clickOnStartDialogButton() throws Throwable {
		this.getDriver().tap(1,
				this.getDriver().findElementByName(nameOtherUserAddContactToChatButton), 1);
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
		return DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(xpathActionMenu));
	}

}
