package com.wearezeta.auto.web.pages;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ConversationPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(ConversationPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathMessageEntry)
	private List<WebElement> messageEntries;

	@FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idConversationInput)
	private WebElement conversationInput;
	
	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.ConversationPage.classNameShowParticipantsButton)
	private WebElement showParticipants;

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
		String xpath = String
				.format(WebAppLocators.ConversationPage.xpathActionMessageEntry,
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
				.format(WebAppLocators.ConversationPage.xpathFormatSpecificMessageEntry,
						message);
		DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
		WebElement element = driver.findElement(By.xpath(xpath));
		if (element != null) {
			isSend = true;
		}
		return isSend;
	}
	
	public UserProfilePopupPage clickShowUserProfileButton() throws Exception {
		showParticipants.click();
		return new UserProfilePopupPage(url, path);
	}
	
	public ParticipantsPopupPage clickShowParticipantsButton() throws Exception {
		showParticipants.click();
		return new ParticipantsPopupPage(url, path);
	}
}
