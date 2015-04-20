package com.wearezeta.auto.web.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.common.WebAppConstants.Browser;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.PeoplePopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

public class ConversationPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(ConversationPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathImageMessageEntry)
	private List<WebElement> imageMessageEntries;

	@FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idConversationInput)
	private WebElement conversationInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathShowParticipantsButton)
	private WebElement showParticipants;

	@FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssSendImageInput)
	private WebElement imagePathInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathPingButton)
	private WebElement pingButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathCallButton)
	private WebElement callButton;

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.ConversationPage.classPingMessage)
	private WebElement pingMessage;

	public ConversationPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void writeNewMessage(String message) {
		conversationInput.sendKeys(message);
	}

	public void sendNewMessage() {
		conversationInput.sendKeys(Keys.ENTER);
	}

	public boolean isActionMessageSent(final String message) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathActionMessageEntries);
		assert DriverUtils.waitUntilElementAppears(this.getDriver(), locator);
		final List<WebElement> actionMessages = this.getDriver()
				.findElements(locator).stream().filter(x -> x.isDisplayed())
				.collect(Collectors.toList());
		// Get the most recent action message only
		final String actionMessageInUI = actionMessages.get(
				actionMessages.size() - 1).getText();
		return actionMessageInUI.toUpperCase().contains(message.toUpperCase());
	}

	public boolean isMessageSent(String message) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathMessageEntryByText
						.apply(message));
		return DriverUtils.isElementDisplayed(driver, locator, 5);
	}

	public PeoplePopoverContainer clickPeopleButton(boolean isGroup)
			throws Exception {
		DriverUtils.waitUntilElementClickable(driver, showParticipants);
		if (WebAppExecutionContext.currentBrowser == Browser.InternetExplorer) {
			driver.executeScript(String
					.format("$('.%s').click();",
							WebAppLocators.ConversationPage.classNameShowParticipantsButton));
		} else {
			showParticipants.click();
		}
		if (isGroup) {
			return new GroupPopoverContainer(this.getDriver(), this.getWait());
		} else {
			return new SingleUserPopoverContainer(this.getDriver(),
					this.getWait());
		}
	}

	public PeoplePickerPage clickShowParticipantsButton() throws Exception {
		showParticipants.click();
		return new PeoplePickerPage(this.getDriver(), this.getWait());
	}

	public void sendPicture(String pictureName) throws Exception {
		final String picturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		final String showImageLabelJScript = "$(\""
				+ WebAppLocators.ConversationPage.cssRightControlsPanel
				+ "\").css({'opacity': '100'});";
		driver.executeScript(showImageLabelJScript);
		final String showPathInputJScript = "$(\""
				+ WebAppLocators.ConversationPage.cssSendImageLabel
				+ "\").find(\""
				+ WebAppLocators.ConversationPage.cssSendImageInput
				+ "\").css({'left': '0'});";
		driver.executeScript(showPathInputJScript);
		assert DriverUtils
				.isElementDisplayed(
						driver,
						By.cssSelector(WebAppLocators.ConversationPage.cssSendImageInput),
						5);
		if (WebAppExecutionContext.currentBrowser == Browser.Safari) {
			WebCommonUtils.sendPictureInSafari(picturePath);
		} else {
			imagePathInput.sendKeys(picturePath);
		}
	}

	public boolean isPictureSent(String pictureName) throws Exception {
		@SuppressWarnings("unused")
		final String picturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		// TODO: Add comparison of the original and sent pictures
		final boolean isAnyPictureMsgFound = DriverUtils
				.waitUntilElementAppears(
						driver,
						By.xpath(WebAppLocators.ConversationPage.xpathImageMessageEntry),
						40);
		return isAnyPictureMsgFound && (imageMessageEntries.size() > 0);
	}

	public void clickPingButton() throws Exception {
		try {
			DriverUtils.moveMouserOver(driver, conversationInput);
		} catch (WebDriverException e) {
			// do nothing (safari workaround)
		}
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathPingButton);
		assert DriverUtils.isElementDisplayed(driver, locator, 2) : "Ping button has not been shown after 2 seconds";
		assert DriverUtils.waitUntilElementClickable(driver, pingButton) : "Ping button has to be clieckable";
		pingButton.click();
	}

	private static final int PING_MESSAGE_TIMEOUT = 3; // seconds

	public boolean isPingMessageVisible(String message) throws Exception {
		final By locator = By
				.className(WebAppLocators.ConversationPage.classPingMessage);
		assert DriverUtils.isElementDisplayed(driver, locator,
				PING_MESSAGE_TIMEOUT) : "Ping message has not been shown within "
				+ PING_MESSAGE_TIMEOUT + " second(s) timeout";
		return pingMessage.getText().toLowerCase()
				.contains(message.toLowerCase());
	}

	public int numberOfPingMessagesVisible() {

		return driver.findElementsByClassName(
				WebAppLocators.ConversationPage.classPingMessage).size() - 1;
	}

	public void clickCallButton() {
		try {
			DriverUtils.moveMouserOver(driver, conversationInput);
		} catch (WebDriverException e) {
			// do nothing (safari workaround)
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		callButton.click();
	}

	public boolean isCalleeAcceptingCall() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathTalkingHalo);
		return DriverUtils.isElementDisplayed(driver, locator, 30);
	}

	public void clickCloseButton() {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathCloseButton);
		driver.findElement(locator).click();
	}

	private static final int TEXT_MESSAGE_VISIBILITY_TIMEOUT_SECONDS = 5;

	public boolean isTextMessageVisible(String message) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.textMessageByText
						.apply(message));
		return DriverUtils.isElementDisplayed(driver, locator,
				TEXT_MESSAGE_VISIBILITY_TIMEOUT_SECONDS);
	}

	public String getMissedCallMessage() {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathMissedCallAction);
		return driver.findElement(locator).getText();
	}
}
