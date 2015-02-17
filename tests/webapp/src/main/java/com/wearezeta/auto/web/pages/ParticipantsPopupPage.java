package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ParticipantsPopupPage extends WebPage {
	
	private static final Logger log = ZetaLogger.getLog(ParticipantsPopupPage.class
			.getSimpleName());
	
	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathLeaveGroupChat)
	private WebElement leaveButton;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathConfirmLeaveButton)
	private WebElement confirmLeaveButton;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathConfirmRemoveButton)
	private WebElement confirmRemoveButton;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathConfirmAddButton)
	private WebElement confirmAddButton;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathRemoveFromGroupChat)
	private WebElement removeFromGroupChatButton;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathProfilePageSearchField)
	private WebElement profilePageSearchField;
	
	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.PeoplePickerPage.classNameCreateConversationButton)
	private WebElement createConversationButton;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathGroupAddPeopleButton)
	private WebElement groupAddPeopleButton;
	
	@FindBy(how = How.ID, using = WebAppLocators.UserProfilePopupPage.idUserProfilePage)
	private WebElement userProfilePopup;
	
	@FindBy(how = How.XPATH, using = WebAppLocators.UserProfilePopupPage.xpathAddPeopleMessage)
	private WebElement addPeopleMessage;
	

	public ParticipantsPopupPage(String URL, String path) throws Exception {
		super(URL, path);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isParticipantsProfilePopupPageVisible() {
		return DriverUtils.isElementDisplayed(userProfilePopup);
	}
	
	public boolean isAddPeopleMessageShown() {
		return addPeopleMessage.isDisplayed();
	}

	public void clickCreateConversation() {
		createConversationButton.click();
	}
	
	public void searchForUser(String searchText) {
		DriverUtils.waitUntilElementClickable(driver, profilePageSearchField);
		profilePageSearchField.clear();
		profilePageSearchField.sendKeys(searchText);
	}
	
	public void leaveGroupChat() {
		wait.until(ExpectedConditions.elementToBeClickable(leaveButton));
		leaveButton.click();
	}
	
	public void confirmLeaveGroupChat() {
		wait.until(ExpectedConditions.elementToBeClickable(confirmLeaveButton));
		confirmLeaveButton.click();
	}
	
	public void confirmRemoveFromGroupChat() {
		wait.until(ExpectedConditions.elementToBeClickable(confirmRemoveButton));
		confirmRemoveButton.click();
	}

	public void removeFromGroupChat() {
		removeFromGroupChatButton.click();
	}
	
	public void confirmAddPeople() {
		wait.until(ExpectedConditions.elementToBeClickable(confirmAddButton));
		confirmAddButton.click();
	}
	
	public void clickAddPeopleButton() {
		groupAddPeopleButton.click();
	}
	
	public void selectUserFromSearchResult(String user) {
		String xpath = String
				.format(WebAppLocators.PeoplePickerPage.xpathFormatSearchListItemWithName,
						user);
		WebElement userEl = driver.findElement(By.xpath(xpath));
		boolean isClickable = DriverUtils.waitUntilElementClickable(driver,
				userEl);
		boolean isVisible = DriverUtils.waitUntilElementVisible(driver, userEl);
		log.debug("Found user element is clickable: " + isClickable
				+ ", isVisible: " + isVisible);
		userEl.click();
	}
	
	public boolean isParticipantVisible(String name) {
		String xpath = String.format(WebAppLocators.UserProfilePopupPage.xpathParticipantName, name);
		return driver.findElementByXPath(xpath).isDisplayed();
	}
	
	public void clickOnParticipant(String name) {
		String xpath = String.format(WebAppLocators.UserProfilePopupPage.xpathParticipantName, name);
		WebElement participant = driver.findElementByXPath(xpath);
		participant.click();
	}
}
