package com.wearezeta.auto.web.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class PeoplePickerPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.PeoplePickerPage.classNameSearchInput)
	private WebElement searchInput;

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.PeoplePickerPage.classNameCreateConversationButton)
	private WebElement createConversationButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathCloseSearchButton)
	private WebElement closeSearchButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathLeaveGroupChat)
	private WebElement leaveButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathConfirmLeaveButton)
	private WebElement confirmLeaveButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathConfirmRemoveButton)
	private WebElement confirmRemoveButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathRemoveFromGroupChat)
	private WebElement removeFromGroupChatButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathConversationTitle)
	private WebElement conversationTitle;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathConversationTitleInput)
	private WebElement conversationTitleInput;

	@FindBy(how = How.ID, using = WebAppLocators.PeoplePickerPage.idConversationPopupPage)
	protected WebElement conversationPopup;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathAddPeopleMessage)
	private WebElement addPeopleMessage;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathConfirmAddButton)
	private WebElement confirmAddButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathProfilePageSearchField)
	private WebElement profilePageSearchField;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathUserName)
	private WebElement userName;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathAddPeopleButton)
	private WebElement addPeopleButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.PeoplePickerPage.xpathNameBlockButton)
	private WebElement blockButton;

	public PeoplePickerPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void clickAddPeopleButton() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathAddPeopleButton);
		assert DriverUtils.isElementDisplayed(driver, locator, 5);
		assert DriverUtils.waitUntilElementClickable(driver, addPeopleButton);
		addPeopleButton.click();
	}

	public void createConversation() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, createConversationButton);
		createConversationButton.click();
	}

	private void clickNotConnectedUser(String name) {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		WebElement foundUserElement = driver.findElement(By
				.xpath(foundUserXpath));
		foundUserElement.click();
	}

	public ConnectToPopupPage clickNotConnectedUserName(String name)
			throws Exception {
		clickNotConnectedUser(name);
		return new ConnectToPopupPage(this.getDriver(), this.getWait());
	}

	public boolean isUserFound(String name) throws Exception {
		String foundUserXpath = WebAppLocators.PeoplePickerPage.xpathSearchResultByName
				.apply(name);
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(foundUserXpath));
	}

	public void closeSearch() throws Exception {
		assert DriverUtils.isElementDisplayed(driver, By
				.xpath(WebAppLocators.PeoplePickerPage.xpathCloseSearchButton),
				5);
		assert DriverUtils.waitUntilElementClickable(driver, closeSearchButton);
		closeSearchButton.click();
	}

	public boolean isParticipantsProfilePopupPageVisible() throws Exception {
		return this.isConversationPopupPageVisible();
	}

	public void leaveGroupChat() {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(leaveButton));
		leaveButton.click();
	}

	public void confirmLeaveGroupChat() {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(confirmLeaveButton));
		confirmLeaveButton.click();
	}

	public void confirmRemoveFromGroupChat() {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(confirmRemoveButton));
		confirmRemoveButton.click();
	}

	public void removeFromGroupChat() {
		removeFromGroupChatButton.click();
	}

	public boolean isParticipantVisible(String name) throws Exception {
		final By locator = By.xpath(String.format(
				WebAppLocators.PeoplePickerPage.xpathParticipantName, name));
		return DriverUtils.isElementDisplayed(driver, locator, 5);
	}

	public void clickOnParticipant(String name) throws Exception {
		final By locator = By.xpath(String.format(
				WebAppLocators.PeoplePickerPage.xpathParticipantName, name));
		assert DriverUtils.isElementDisplayed(driver, locator, 3);
		WebElement participant = driver.findElement(locator);
		assert DriverUtils.waitUntilElementClickable(driver, participant);
		participant.click();
	}

	public void setConversationTitle(String title) {
		conversationTitle.click();
		conversationTitleInput.clear();
		conversationTitleInput.sendKeys(title + "\n");
	}

	public String getConversationTitle() {
		return conversationTitle.getText();
	}

	public boolean isConversationPopupPageVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.id(WebAppLocators.PeoplePickerPage.idConversationPopupPage),
				10);
	}

	public boolean isAddPeopleMessageShown() {
		return addPeopleMessage.isDisplayed();
	}

	public void confirmAddPeople() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, confirmAddButton);
		confirmAddButton.click();
	}

	public void searchForUser(String searchText) throws Exception {
		DriverUtils.waitUntilElementClickable(driver, profilePageSearchField);
		profilePageSearchField.clear();
		profilePageSearchField.sendKeys(searchText);
	}

	public void selectUserFromSearchResult(String user) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.PeoplePickerPage.xpathSearchResultByName
						.apply(user));
		assert DriverUtils.isElementDisplayed(driver, locator,
				DriverUtils.DEFAULT_VISIBILITY_TIMEOUT);
		WebElement userEl = driver.findElement(locator);
		assert DriverUtils.waitUntilElementClickable(driver, userEl);
		userEl.click();
	}

	public void clickCreateConversation() {
		createConversationButton.click();
	}

	public boolean isUserProfilePopupPageVisible() throws Exception {
		return this.isConversationPopupPageVisible();
	}

	public String getUserName() {
		return userName.getText();
	}

	public boolean isAddPeopleButtonVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(WebAppLocators.PeoplePickerPage.xpathAddPeopleButton));
	}

	public boolean isBlockButtonVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(WebAppLocators.PeoplePickerPage.xpathNameBlockButton));
	}
}
