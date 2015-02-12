package com.wearezeta.auto.web.pages;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppConstants;
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

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.ConversationPage.classNameShowParticipantsButton)
	private WebElement showParticipants;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathSendImageInput)
	private WebElement imagePathInput;

	private String url;
	private String path;

	public ConversationPage(String URL, String path) throws Exception {

		super(URL, path);

		this.url = URL;
		this.path = path;
	}

	public void writeNewMessage(String message) {
		conversationInput.sendKeys(message);
	}

	public void sendNewMessage() {
		conversationInput.sendKeys(Keys.ENTER);
	}

	public boolean isActionMessageSent(String message) {
		boolean isSend = false;
		String xpath = String.format(
				WebAppLocators.ConversationPage.xpathActionMessageEntry,
				message);
		DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
		WebElement element = driver.findElement(By.xpath(xpath));
		if (element != null) {
			isSend = true;
		}
		return isSend;
	}

	public boolean isMessageSent(String message) {
		boolean isSend = false;
		String xpath = String
				.format(WebAppLocators.ConversationPage.xpathFormatSpecificTextMessageEntry,
						message);
		DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
		WebElement element = driver.findElement(By.xpath(xpath));
		if (element != null) {
			isSend = true;
		}
		return isSend;
	}

	public UserProfilePopup clickShowUserProfileButton() throws Exception {
		showParticipants.click();
		return new UserProfilePopup(url, path);
	}

	public ParticipantsPopupPage clickShowParticipantsButton() throws Exception {
		showParticipants.click();
		return new ParticipantsPopupPage(url, path);
	}

	public void sendPicture(String pictureName) throws Exception {
		final String picturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		final String showPathInputJScript = "$('"
				+ WebAppLocators.ConversationPage.cssSendImageLabel
				+ "').css({'opacity': '100'});" + "\n" + "$('"
				+ WebAppLocators.ConversationPage.cssSendImageLabel
				+ "').find('"
				+ WebAppLocators.ConversationPage.cssSendImageInput
				+ "').css({'left': '0'});";
		driver.executeScript(showPathInputJScript);
		if (WebCommonUtils.getWebAppBrowserNameFromConfig(
				ConversationPage.class).equals(WebAppConstants.Browser.SAFARI)) {
			// sendKeys() call to file input element does nothing on safari
			// so instead of sendKeys() we are using AppleScript which chooses
			// required image in open file dialog
			imagePathInput.click();
			String script = String
					.format(CommonUtils
							.readTextFileFromResources(WebAppConstants.Scripts.SAFARI_SEND_PICTURE_SCRIPT),
							WebCommonUtils.getPicturesPath(), pictureName);
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("AppleScript");
			engine.eval(script);
		} else {
			if (DriverUtils.waitUntilElementVisible(driver, imagePathInput)) {
				imagePathInput.sendKeys(picturePath + "\n");
			} else {
				throw new TimeoutException(
						"Image input is still not visible after timeout");
			}
		}
	}

	public boolean isPictureSent(String pictureName) {
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
}
