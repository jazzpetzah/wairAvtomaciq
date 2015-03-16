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

public class ParticipantsPopupPage extends ConversationPopupPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(ParticipantsPopupPage.class.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.ParticipantsProfilePopupPage.xpathLeaveGroupChat)
	private WebElement leaveButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ParticipantsProfilePopupPage.xpathConfirmLeaveButton)
	private WebElement confirmLeaveButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ParticipantsProfilePopupPage.xpathConfirmRemoveButton)
	private WebElement confirmRemoveButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ParticipantsProfilePopupPage.xpathRemoveFromGroupChat)
	private WebElement removeFromGroupChatButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ParticipantsProfilePopupPage.xpathAddPeopleButton)
	private WebElement addPeopleButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ParticipantsProfilePopupPage.xpathConversationTitle)
	private WebElement conversationTitle;

	@FindBy(how = How.XPATH, using = WebAppLocators.ParticipantsProfilePopupPage.xpathConversationTitleInput)
	private WebElement conversationTitleInput;

	public ParticipantsPopupPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isParticipantsProfilePopupPageVisible() throws Exception {
		return this.isConversationPopupPageVisible();
	}

	@Override
	public void clickAddPeopleButton() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ParticipantsProfilePopupPage.xpathAddPeopleButton);
		assert DriverUtils.isElementDisplayed(driver, locator, 5);
		assert DriverUtils.waitUntilElementClickable(driver, addPeopleButton);
		addPeopleButton.click();
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
		final By locator = By
				.xpath(String
						.format(WebAppLocators.UserProfilePopupPage.xpathParticipantName,
								name));
		return DriverUtils.isElementDisplayed(driver, locator, 5);
	}

	public void clickOnParticipant(String name) throws Exception {
		final By locator = By
				.xpath(String
						.format(WebAppLocators.UserProfilePopupPage.xpathParticipantName,
								name));
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
}
