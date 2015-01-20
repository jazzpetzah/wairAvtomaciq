package com.wearezeta.auto.osx.pages;

import java.awt.HeadlessException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

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
import com.wearezeta.auto.osx.locators.OSXLocators;

@SuppressWarnings("deprecation")
public class ConversationInfoPage extends OSXPage {
	@FindBy(how = How.XPATH, using = OSXLocators.xpathPeoplePopover)
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
	
	@FindBy(how = How.ID, using = OSXLocators.idConversationScrollArea)
	private WebElement conversationScrollArea;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathConversationNameEdit)
	private WebElement conversationNameEdit;
	
	@FindBy(how = How.ID, using = OSXLocators.idUserProfileViewBackButton)
	private WebElement userProfileViewBackButton;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathImagePopupCloseButton)
	private WebElement imagePopupCloseButton;
	
	public String currentConversationName;
	
	private String url;
	private String path;
	
	public ConversationInfoPage(String URL, String path) throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public boolean userIsNotExistInConversation(String user) {
		String xpath = String.format(OSXLocators.xpathFormatPeoplePickerUserCell, user);
		return DriverUtils.waitUntilElementDissapear(driver, By.xpath(xpath));
	}
	
	public void selectUser(String user) {
		String xpath = String.format(OSXLocators.xpathFormatPeoplePickerUserCell, user);
		WebElement el = driver.findElement(By.xpath(xpath));
		el.click();
	}
	
	public void selectUserIfNotSelected(String user) throws IOException {
		Screen s = new Screen();
		try {
    		App.focus(CommonUtils.getOsxApplicationPathFromConfig(ConversationInfoPage.class));
			s.click(Env.getMouseLocation());
			s.click(Env.getMouseLocation());
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (FindFailed e) {
			e.printStackTrace();
		}
	}
	                
	private void confirmIfRequested() {
		try {
			DriverUtils.setImplicitWaitValue(driver, 3);
			confirmationViewConfirmButton.click();
		} catch (NoSuchElementException e) { 
		} finally {
			DriverUtils.setDefaultImplicitWait(driver);
		}
	}
	
	public boolean isPeoplePopoverDisplayed() {
		return DriverUtils.isElementDisplayed(peoplePopover);
	}
	
	public PeoplePickerPage openPeoplePicker() throws IOException {		
		try {
			singleChatAddPeopleButton.click();
		} catch (NoSuchElementException e) {
			groupChatAddPeopleButton.click();
		}
		confirmIfRequested();
		return new PeoplePickerPage(url, path);
	}
	
	public void removeUser() {
		removeUserFromConversationButton.click();
		confirmIfRequested();
		conversationScrollArea.click();
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
	
	public void leaveConversation() {
		leaveConversationButton.click();
		confirmIfRequested();
	}
	
	private String latestSetConversationName;
	
	public void setNewConversationName(String name) {
		latestSetConversationName = name;
		conversationNameEdit.sendKeys(latestSetConversationName + "\\n");
	}
	
	public String getlatestSetConversationName() {
		return latestSetConversationName;
	}
	
	public int numberOfPeopleInConversation() {
		int result = -1;
		List<WebElement> elements = driver.findElements(By.xpath(OSXLocators.xpathNumberOfPeopleInChat));
		for (WebElement element: elements) {
			String value = element.getText();
			if (value.contains(OSXLocators.peopleCountTextSubstring)) {
				result = Integer.parseInt(value.substring(0, value.indexOf(OSXLocators.peopleCountTextSubstring)));
			}
		}
		return result;
	}
	
	public int numberOfParticipantsAvatars() {
		List<WebElement> elements = driver.findElements(By.xpath(OSXLocators.xpathUserAvatar));
		return elements.size();
	}
	
	public boolean isConversationNameEquals(String expectedName) {
		boolean result = false;
		String actualName = conversationNameEdit.getText().replaceAll("\uFFFC", "").trim();
		if (expectedName.contains(",")) {
			String[] exContacts = expectedName.split(",");

			boolean isFound = true;
			for (String exContact: exContacts) {
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
	
	public boolean isContactPersonalInfoAppear(String contact) {
		String xpath = String.format(OSXLocators.xpathFormatUserProfileViewContactName, contact);
		WebElement el = driver.findElement(By.xpath(xpath));
		if (el != null) return true;
		else return false;
	}

	public boolean isAddPeopleButtonExists() {
		return singleChatAddPeopleButton.isDisplayed();
	}
	
	public boolean isBlockUserButtonExists() {
		return singleChatBlockUserButton.isDisplayed();
	}
	
	public boolean isUserNameDisplayed(String name) {
		return singleChatUserNameField.getAttribute("AXValue").equals(name);
	}
	
	public boolean isEmailButtonExists(String email) {
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(
				String.format(OSXLocators.xpathSingleChatUserEmailButton, email)));
	}
	
	public String getCurrentConversationName() {
		return currentConversationName;
	}
	
	public void setCurrentConversationName(String name) {
		currentConversationName = name;
	}
	
	public void openImageInPopup() {
		peoplePopover.click();
	}
	
	public void closeImagePopup() {
		imagePopupCloseButton.click();
	}
}
