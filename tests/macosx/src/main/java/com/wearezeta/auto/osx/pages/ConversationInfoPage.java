package com.wearezeta.auto.osx.pages;

import java.awt.HeadlessException;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.sikuli.script.App;
import org.sikuli.script.Env;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.locators.PopoverLocators;
import com.wearezeta.auto.osx.pages.popovers.ConnectToPopover;
import com.wearezeta.auto.osx.util.NSPoint;

@SuppressWarnings("deprecation")
public class ConversationInfoPage extends OSXPage {

	private static final Logger log = ZetaLogger
			.getLog(ConversationInfoPage.class.getSimpleName());

	@FindBy(how = How.XPATH, using = PopoverLocators.Common.xpathPopoverWindow)
	private WebElement peoplePopover;

	@FindBy(how = How.ID, using = OSXLocators.idSingleChatUserNameField)
	private WebElement singleChatUserNameField;

	@FindBy(how = How.ID, using = OSXLocators.idAddPeopleButtonSingleChat)
	private WebElement singleChatAddPeopleButton;

	@FindBy(how = How.ID, using = OSXLocators.idBlockUserButtonSingleChat)
	private WebElement singleChatBlockUserButton;

	@FindBy(how = How.ID, using = OSXLocators.idAddPeopleButtonGroupChat)
	private WebElement groupChatAddPeopleButton;

	@FindBy(how = How.ID, using = OSXLocators.idConfirmationViewConfirmButton)
	private WebElement confirmationViewConfirmButton;

	@FindBy(how = How.ID, using = OSXLocators.idRemoveUserFromConversation)
	private WebElement removeUserFromConversationButton;

	@FindBy(how = How.ID, using = OSXLocators.idLeaveConversationButton)
	private WebElement leaveConversationButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathOpenSingleChatButton)
	private WebElement openSingleChatButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathConversationNameEdit)
	private WebElement conversationNameEdit;

	@FindBy(how = How.ID, using = OSXLocators.idUserProfileViewBackButton)
	private WebElement userProfileViewBackButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathImagePopupCloseButton)
	private WebElement imagePopupCloseButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPendingButton)
	private WebElement pendingButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathConnectToUserButton)
	private WebElement connectToUserButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathAvatarFullScreenWindow)
	private WebElement avatarFullScreenWindow;

	private ConversationPage parent;

	public ConversationPage getParent() {
		return parent;
	}

	public void setParent(ConversationPage parent) {
		this.parent = parent;
	}

	public ConversationInfoPage(Future<ZetaOSXDriver> lazyDriver)
			throws Exception {
		this(lazyDriver, null);
	}

	public ConversationInfoPage(Future<ZetaOSXDriver> lazyDriver,
			ConversationPage parent) throws Exception {
		super(lazyDriver);
		this.parent = parent;
	}

	public boolean userIsNotExistInConversation(String user) throws Exception {
		String xpath = String.format(
				OSXLocators.xpathFormatPeoplePickerUserCell, user);
		return DriverUtils.waitUntilElementDissapear(this.getDriver(),
				By.xpath(xpath));
	}

	public void selectUser(String user) throws Exception {
		String xpath = String.format(
				OSXLocators.xpathFormatPeoplePickerUserCell, user);
		try {
			WebElement el = getDriver().findElement(By.xpath(xpath));
			el.click();
		} catch (NoSuchElementException e) {
			log.debug("Can't find user cell. Page source: "
					+ this.getDriver().getPageSource());
		}
	}

	public void selectUserIfNotSelected(String user) throws Exception {
		Screen s = new Screen();
		try {
			App.focus(CommonUtils
					.getOsxApplicationPathFromConfig(ConversationInfoPage.class));
			if (!userIsNotExistInConversation(user))
				s.click(Env.getMouseLocation());
			if (!userIsNotExistInConversation(user))
				s.click(Env.getMouseLocation());
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (FindFailed e) {
			e.printStackTrace();
		}
	}

	private void confirmIfRequested() throws Exception {
		try {
			DriverUtils.setImplicitWaitValue(this.getDriver(), 3);
			confirmationViewConfirmButton.click();
		} catch (NoSuchElementException e) {
		} finally {
			DriverUtils.setDefaultImplicitWait(this.getDriver());
		}
	}

	public boolean isPeoplePopoverDisplayed() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(PopoverLocators.Common.xpathPopoverWindow));
	}

	public PeoplePickerPage openPeoplePicker() throws Exception {
		try {
			singleChatAddPeopleButton.click();
		} catch (NoSuchElementException e) {
			groupChatAddPeopleButton.click();
		}
		confirmIfRequested();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public void removeUser() throws Exception {
		removeUserFromConversationButton.click();
		confirmIfRequested();
		parent.focusOnConversation();
	}

	public void tryRemoveUser() {
		removeUserFromConversationButton.click();
	}

	public boolean isRemoveUserConfirmationAppear() {
		try {
			confirmationViewConfirmButton.click();
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void leaveConversation() throws Exception {
		leaveConversationButton.click();
		confirmIfRequested();
	}

	private String latestSetConversationName;

	public void setNewConversationName(String name) {
		latestSetConversationName = name;
		conversationNameEdit.clear();
		conversationNameEdit.sendKeys(latestSetConversationName + "\\n");
	}

	public String getlatestSetConversationName() {
		return latestSetConversationName;
	}

	public int numberOfPeopleInConversation() throws Exception {
		int result = -1;
		List<WebElement> elements = getDriver().findElements(
				By.xpath(OSXLocators.xpathNumberOfPeopleInChat));
		for (WebElement element : elements) {
			String value = element.getText();
			if (value.contains(OSXLocators.peopleCountTextSubstring)) {
				result = Integer.parseInt(value.substring(0,
						value.indexOf(OSXLocators.peopleCountTextSubstring)));
			}
		}
		return result;
	}

	public int numberOfParticipantsAvatars() throws Exception {
		List<WebElement> elements = getDriver().findElements(
				By.xpath(OSXLocators.xpathUserAvatar));
		return elements.size();
	}

	public boolean isConversationNameEquals(String expectedName) {
		boolean result = false;
		String actualName = conversationNameEdit.getText()
				.replaceAll("\uFFFC", "").trim();
		if (expectedName.contains(",")) {
			String[] exContacts = expectedName.split(",");

			boolean isFound = true;
			for (String exContact : exContacts) {
				if (!actualName.contains(exContact.trim())) {
					isFound = false;
				}
				if (isFound) {
					result = true;
				}
			}
		} else {
			result = (actualName.equals(expectedName));
		}
		return result;
	}

	public void goBackFromUserProfileView() {
		userProfileViewBackButton.click();
	}

	public boolean isContactPersonalInfoAppear(String contact) throws Exception {
		String xpath = String.format(
				OSXLocators.xpathFormatUserProfileViewContactName, contact);
		WebElement el = getDriver().findElement(By.xpath(xpath));
		if (el != null)
			return true;
		else
			return false;
	}

	public boolean isAddPeopleButtonExists() {
		return singleChatAddPeopleButton.isDisplayed();
	}

	public boolean isBlockUserButtonExists() {
		return singleChatBlockUserButton.isDisplayed();
	}

	public boolean isOpenConversationButtonExists() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(OSXLocators.xpathOpenSingleChatButton), 5);
	}

	public boolean isRemoveUserFromConversationButtonExists() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.id(OSXLocators.idRemoveUserFromConversation), 5);
	}

	public boolean isConnectButtonExists() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(OSXLocators.xpathConnectToUserButton), 5);
	}

	public boolean isUserNameDisplayed(String name) {
		return singleChatUserNameField.getAttribute(
				OSXConstants.Attributes.AXVALUE).equals(name);
	}

	public boolean isPendingButtonExists() {
		return pendingButton.isDisplayed();
	}

	public boolean isSentConnectionRequestMessageExists(String message)
			throws Exception {
		String xpath = String.format(
				OSXLocators.xpathFormatSentConnectionRequestMessage, message);
		WebElement el = getDriver().findElement(By.xpath(xpath));
		return el.isDisplayed();
	}

	public boolean isEmailButtonExists(String email) throws Exception {
		return DriverUtils.waitUntilElementAppears(this.getDriver(), By
				.xpath(String.format(
						OSXLocators.xpathSingleChatUserEmailButton, email)));
	}

	public NSPoint retrieveAvatarFullScreenWindowSize() {
		NSPoint result = NSPoint.fromString(avatarFullScreenWindow
				.getAttribute(OSXConstants.Attributes.AXSIZE));
		return result;
	}

	public void openImageInPopup() {
		peoplePopover.click();
	}

	public void closeImagePopup() {
		imagePopupCloseButton.click();
	}

	public ConnectToPopover connectToUser() throws Exception {
		connectToUserButton.click();
		return new ConnectToPopover(this.getLazyDriver());
	}
}
