package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppConstants.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;

import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.PeoplePopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import java.util.List;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ConversationPage extends WebPage {
	private static final Logger log = ZetaLogger.getLog(ConversationPage.class
			.getSimpleName());

	private static final String TOOLTIP_PEOPLE = "People";

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

	public ConversationPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void writeNewMessage(String message) {
		conversationInput.sendKeys(message);
	}

	public void sendNewMessage() {
		conversationInput.sendKeys(Keys.ENTER);
	}

	public boolean isActionMessageSent(final Set<String> parts)
			throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathActionMessageEntries);
		assert DriverUtils.waitUntilElementAppears(this.getDriver(), locator);
		final List<WebElement> actionMessages = this.getDriver()
				.findElements(locator).stream().filter(x -> x.isDisplayed())
				.collect(Collectors.toList());
		// Get the most recent action message only
		final String actionMessageInUI = actionMessages.get(
				actionMessages.size() - 1).getText();
		for (String part : parts) {
			if (!actionMessageInUI.toUpperCase().contains(part.toUpperCase())) {
				log.error(String
						.format("'%s' substring has not been found in '%s' action message",
								part, actionMessageInUI));
				return false;
			}
		}
		return true;
	}

	public boolean isActionMessageSent(String message) throws Exception {
		Set<String> parts = new HashSet<String>();
		parts.add(message);
		return isActionMessageSent(parts);
	}

	public boolean isMessageSent(String message) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathMessageEntryByText
						.apply(message));
		return DriverUtils.isElementDisplayed(this.getDriver(), locator, 5);
	}

	public boolean isYoutubeVideoEmbedded(String url) throws Exception {
		String pattern = "[\\w\\-\\_]{10,12}";
		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(url);
		assert matcher.find() : "Could not find Youtube id in URL: " + url;
		final String id = matcher.group();

		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathEmbeddedYoutubeVideoById
						.apply(id));
		return DriverUtils.isElementDisplayed(this.getDriver(), locator, 5);
	}

	public PeoplePopoverContainer clickPeopleButton(boolean isGroup)
			throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				showParticipants);
		if (WebAppExecutionContext.getCurrentBrowser() == Browser.InternetExplorer) {
			this.getDriver()
					.executeScript(
							String.format(
									"$('.%s').click();",
									WebAppLocators.ConversationPage.classNameShowParticipantsButton));
		} else {
			showParticipants.click();
		}
		if (isGroup) {
			return new GroupPopoverContainer(this.getLazyDriver());
		} else {
			return new SingleUserPopoverContainer(this.getLazyDriver());
		}
	}

	public PeoplePickerPage clickShowParticipantsButton() throws Exception {
		showParticipants.click();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public boolean isPeopleButtonToolTipCorrect() {
		return TOOLTIP_PEOPLE.equals(showParticipants
				.getAttribute(TITLE_ATTRIBUTE_LOCATOR));
	}

	public void sendPicture(String pictureName) throws Exception {
		final String picturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		final String showImageLabelJScript = "$(\""
				+ WebAppLocators.ConversationPage.cssRightControlsPanel
				+ "\").css({'opacity': '100'});";
		this.getDriver().executeScript(showImageLabelJScript);
		final String showPathInputJScript = "$(\""
				+ WebAppLocators.ConversationPage.cssSendImageLabel
				+ "\").find(\""
				+ WebAppLocators.ConversationPage.cssSendImageInput
				+ "\").css({'left': '0'});";
		this.getDriver().executeScript(showPathInputJScript);
		assert DriverUtils
				.isElementDisplayed(
						this.getDriver(),
						By.cssSelector(WebAppLocators.ConversationPage.cssSendImageInput),
						5);
		if (WebAppExecutionContext.getCurrentBrowser() == Browser.Safari) {
			WebCommonUtils.sendPictureInSafari(picturePath, this.getDriver()
					.getNodeIp());
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
						this.getDriver(),
						By.xpath(WebAppLocators.ConversationPage.xpathImageMessageEntry),
						40);
		return isAnyPictureMsgFound && (imageMessageEntries.size() > 0);
	}

	public void clickPingButton() throws Exception {
		try {
			DriverUtils.moveMouserOver(this.getDriver(), conversationInput);
		} catch (WebDriverException e) {
			// (safari workaround)
			final String showImageLabelJScript = "$(\""
					+ WebAppLocators.ConversationPage.cssRightControlsPanel
					+ "\").css({'opacity': '100'});";
			this.getDriver().executeScript(showImageLabelJScript);
		}
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathPingButton);
		assert DriverUtils.isElementDisplayed(this.getDriver(), locator, 2) : "Ping button has not been shown after 2 seconds";
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				pingButton) : "Ping button has to be clickable";
		pingButton.click();
	}

	private static final int PING_MESSAGE_TIMEOUT = 3; // seconds

	public boolean isPingMessageVisible(String message) throws Exception {
		final By locator = By
				.className(WebAppLocators.ConversationPage.classPingMessage);
		assert DriverUtils.isElementDisplayed(this.getDriver(), locator,
				PING_MESSAGE_TIMEOUT) : "Ping message has not been shown within "
				+ PING_MESSAGE_TIMEOUT + " second(s) timeout";
		return pingMessage.getText().toLowerCase()
				.contains(message.toLowerCase());
	}

	public int numberOfPingMessagesVisible() throws Exception {
		return getDriver().findElementsByClassName(
				WebAppLocators.ConversationPage.classPingMessage).size() - 1;
	}

	public void clickCallButton() throws Exception {
		DriverUtils.moveMouserOver(this.getDriver(), conversationInput);
		assert DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(WebAppLocators.ConversationPage.xpathCallButton), 5);
		callButton.click();
	}

	private static final int TEXT_MESSAGE_VISIBILITY_TIMEOUT_SECONDS = 5;

	public boolean isTextMessageVisible(String message) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.textMessageByText
						.apply(message));
		return DriverUtils.isElementDisplayed(this.getDriver(), locator,
				TEXT_MESSAGE_VISIBILITY_TIMEOUT_SECONDS);
	}

	private static final int MISSED_CALL_MSG_TIMOEUT = 15;

	public String getMissedCallMessage() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathMissedCallAction);
		assert DriverUtils.isElementDisplayed(this.getDriver(), locator,
				MISSED_CALL_MSG_TIMOEUT) : "Missed call message is not visible after "
				+ MISSED_CALL_MSG_TIMOEUT + " second(s) timeout";
		return getDriver().findElement(locator).getText();
	}

	private static final int MAX_CALLING_BAR_VISIBILITY_TIMEOUT = 5; // seconds

	public void waitForCallingBarToBeDisplayed() throws Exception {
		assert DriverUtils.isElementDisplayed(this.getDriver(),
				By.xpath(WebAppLocators.ConversationPage.xpathCallingBarRoot),
				MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Calling bar has not been shown within "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " second(s)";
	}

	public void clickAcceptCallButton() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathAcceptCallButton);
		assert DriverUtils.isElementDisplayed(this.getDriver(), locator,
				MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Accept call button has not been shown after "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " seconds";
		getDriver().findElement(locator).click();
	}

	public void clickEndCallButton() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathEndCallButton);
		assert DriverUtils.isElementDisplayed(this.getDriver(), locator,
				MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "End call button has not been shown after "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " seconds";
		getDriver().findElement(locator).click();
	}

	public void clickSilenceCallButton() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathSilenceIncomingCallButton);
		assert DriverUtils.isElementDisplayed(this.getDriver(), locator,
				MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Silence call button has not been shown after "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " seconds";
		getDriver().findElement(locator).click();
	}

	public void verifyCallingBarIsNotVisible() throws Exception {
		assert DriverUtils.waitUntilElementDissapear(this.getDriver(),
				By.xpath(WebAppLocators.ConversationPage.xpathCallingBarRoot),
				MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Calling bar has not been hidden within "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " second(s)";
	}
}
