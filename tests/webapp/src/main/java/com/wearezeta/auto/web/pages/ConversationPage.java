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
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ConversationPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(ConversationPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathTextMessageEntry)
	private List<WebElement> textMessageEntries;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathImageMessageEntry)
	private List<WebElement> imageMessageEntries;

	@FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idConversationInput)
	private WebElement conversationInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathShowParticipantsButton)
	private WebElement showParticipants;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathSendImageLabel)
	private WebElement sendImageLabel;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathSendImageInput)
	private WebElement imagePathInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathPingButton)
	private WebElement pingButton;

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
		boolean isSend = false;
		final By locator = By
				.xpath(String
						.format(WebAppLocators.ConversationPage.xpathFormatSpecificTextMessageEntry,
								message));
		DriverUtils.waitUntilElementAppears(driver, locator);
		WebElement element = driver.findElement(locator);
		if (element != null) {
			isSend = true;
		}
		return isSend;
	}

	public ConversationPopupPage clickShowUserProfileButton(boolean isGroup)
			throws Exception {
		DriverUtils.waitUntilElementClickable(driver, showParticipants);
		if (WebAppExecutionContext.browserName
				.equals(WebAppConstants.Browser.INTERNET_EXPLORER)) {
			driver.executeScript(String
					.format("$('.%s').click();",
							WebAppLocators.ConversationPage.classNameShowParticipantsButton));
		} else {
			showParticipants.click();
		}
		if (isGroup) {
			return new ParticipantsPopupPage(this.getDriver(), this.getWait());
		} else {
			return new UserProfilePopupPage(this.getDriver(), this.getWait());
		}
	}

	public ParticipantsPopupPage clickShowParticipantsButton() throws Exception {
		showParticipants.click();
		return new ParticipantsPopupPage(this.getDriver(), this.getWait());
	}

	public void sendPicture(String pictureName, boolean isGroup)
			throws Exception {
		final String picturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		final String showImageLabelJScript = "$('"
				+ WebAppLocators.ConversationPage.cssRightControlsPanel
				+ "').css({'opacity': '100'});";
		driver.executeScript(showImageLabelJScript);
		final String showPathInputJScript = "$('"
				+ WebAppLocators.ConversationPage.cssSendImageLabel
				+ "').find('"
				+ WebAppLocators.ConversationPage.cssSendImageInput
				+ "').css({'left': '0'});";
		driver.executeScript(showPathInputJScript);
		// trying to wait for elements will appear on Safari
		Thread.sleep(3000);
		if (WebAppExecutionContext.browserName
				.equals(WebAppConstants.Browser.SAFARI)) {
			// sendKeys() call to file input element does nothing on safari
			// so instead of sendKeys() we are using AppleScript which chooses
			// required image in open file dialog
			String scriptDestination = WebAppExecutionContext.temporaryScriptsLocation
					+ "/" + WebAppConstants.Scripts.SAFARI_SEND_PICTURE_SCRIPT;
			WebCommonUtils
					.formatTextInFileAndSave(
							WebCommonUtils.getScriptsTemplatesPath()
									+ WebAppConstants.Scripts.SAFARI_SEND_PICTURE_SCRIPT,
							scriptDestination,
							new String[] { WebCommonUtils.getPicturesPath(),
									pictureName });
			WebCommonUtils.putFilesOnExecutionNode(
					WebAppExecutionContext.seleniumNodeIp,
					WebAppExecutionContext.temporaryScriptsLocation);
			WebCommonUtils.executeAppleScriptFromFile(scriptDestination);
		} else {
			assert DriverUtils
					.isElementDisplayed(
							driver,
							By.xpath(WebAppLocators.ConversationPage.xpathSendImageInput),
							10);
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

	public void clickPingButton() {
		try {
			DriverUtils.moveMouserOver(driver, conversationInput);
		} catch (WebDriverException e) {
			// do nothing (safari workaround)
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		pingButton.click();
	}

	public boolean isPingMessageVisible(String message) {
		String text = pingMessage.getText();
		if (text.toLowerCase().contains(message.toLowerCase())) {
			return pingMessage.isDisplayed();
		} else {
			return false;
		}
	}

	public int numberOfPingMessagesVisible() {

		return driver.findElementsByClassName(
				WebAppLocators.ConversationPage.classPingMessage).size() - 1;
	}
}
