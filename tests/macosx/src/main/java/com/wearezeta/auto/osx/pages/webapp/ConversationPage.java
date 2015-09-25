package com.wearezeta.auto.osx.pages.webapp;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

import com.wearezeta.auto.web.pages.WebPage;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ConversationPage extends WebPage {

	private static final int TIMEOUT_IMAGE_MESSAGE_UPLOAD = 40; // seconds

	private static final Logger log = ZetaLogger.getLog(ConversationPage.class
			.getSimpleName());

	private static final String TOOLTIP_PEOPLE = "People";

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathLastImageEntry)
	private WebElement lastImageEntry;

	@FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssImageEntries)
	private List<WebElement> imageEntries;

	@FindBy(id = WebAppLocators.ConversationPage.idConversation)
	private WebElement conversation;

	@FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idConversationInput)
	private WebElement conversationInput;

	@FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssShowParticipantsButton)
	private WebElement showParticipants;

	@FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssSendImageInput)
	private WebElement imagePathInput;

	@FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssPingButton)
	private WebElement pingButton;

	@FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssGIFButton)
	private WebElement gifButton;

	@FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssCallButton)
	private WebElement callButton;

	@FindBy(how = How.CSS, using = WebAppLocators.ConversationPage.cssPingMessage)
	private WebElement pingMessage;

	@FindBy(css = WebAppLocators.ConversationPage.cssLastTextMessage)
	private WebElement lastTextMessage;

	@FindBy(css = WebAppLocators.ConversationPage.cssSecondLastTextMessage)
	private WebElement secondLastTextMessage;

	@FindBy(css = WebAppLocators.ConversationPage.cssImageEntries)
	private WebElement lastPicture;

	@FindBy(css = WebAppLocators.ConversationPage.cssFullscreenImage)
	private WebElement fullscreenImage;

	@FindBy(xpath = WebAppLocators.ConversationPage.xpathXButton)
	private WebElement xButton;

	@FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idBlackBorder)
	private WebElement blackBorder;

	@FindBy(css = WebAppLocators.ConversationPage.cssUserAvatar)
	private WebElement userAvatar;

	@FindBy(xpath = WebAppLocators.ConversationPage.xpathJoinCallBar)
	private WebElement joinCallBar;

	public ConversationPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

}
