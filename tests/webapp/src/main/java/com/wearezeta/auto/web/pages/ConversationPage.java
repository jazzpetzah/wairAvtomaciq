package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.Browser;
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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ConversationPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(ConversationPage.class
			.getSimpleName());

	private static final String TOOLTIP_PEOPLE = "People";

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathLastImageEntry)
	private WebElement lastImageEntry;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathImageEntries)
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

	@FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idGIFButton)
	private WebElement gifButton;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathCallButton)
	private WebElement callButton;

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.ConversationPage.classPingMessage)
	private WebElement pingMessage;

	@FindBy(css = WebAppLocators.ConversationPage.cssLastTextMessage)
	private WebElement lastTextMessage;

	@FindBy(css = WebAppLocators.ConversationPage.cssSecondLastTextMessage)
	private WebElement secondLastTextMessage;

	@FindBy(xpath = WebAppLocators.ConversationPage.xpathPictureFullscreen)
	private WebElement pictureFullscreen;

	@FindBy(xpath = WebAppLocators.ConversationPage.xpathXButton)
	private WebElement xButton;

	@FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idBlackBorder)
	private WebElement blackBorder;

	public ConversationPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void writeNewMessage(String message) throws Exception {
		if (WebAppExecutionContext.getBrowser()
				.equals(Browser.InternetExplorer)) {
			// IE11 has a bug that sends the form when pressing SHIFT+ENTER
			message = message
					.replace(Keys.chord(Keys.SHIFT, Keys.ENTER), "\\n");
			String addMessageToInput = "var a=arguments[0];a.value=a.value+'"
					+ message + "';";
			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			js.executeScript(addMessageToInput, conversationInput);
			// since we did not press any keys, we fake input by sending a space
			// and then removing it again
			conversationInput.sendKeys(" " + Keys.BACK_SPACE);
		} else {
			conversationInput.sendKeys(message);
		}
	}

	public void sendNewMessage() {
		conversationInput.sendKeys(Keys.ENTER);
	}

	public boolean isActionMessageSent(final Set<String> parts)
			throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathActionMessageEntries);
		assert DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
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
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 5);
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
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 5);
	}

	public PeoplePopoverContainer clickPeopleButton(boolean isGroup)
			throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(),
				showParticipants);
		showParticipants.click();
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
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.cssSelector(WebAppLocators.ConversationPage.cssSendImageInput),
						5);
		if (WebAppExecutionContext.getBrowser() == Browser.Safari) {
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
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ConversationPage.xpathLastImageEntry),
						40);
		return isAnyPictureMsgFound && (imageEntries.size() > 0);
	}

	public void clickPingButton() throws Exception {
		if (WebAppExecutionContext.getBrowser()
				.isSupportingNativeMouseActions()) {
			DriverUtils.moveMouserOver(this.getDriver(), conversationInput);
		} else {
			DriverUtils.addClass(this.getDriver(), conversation, "hover");
		}
		final By locator = By
				.cssSelector(WebAppLocators.ConversationPage.cssPingButton);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, 2) : "Ping button has not been shown after 2 seconds";
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				pingButton) : "Ping button has to be clickable";
		pingButton.click();
	}

	private static final int PING_MESSAGE_TIMEOUT = 3; // seconds

	public boolean isPingMessageVisible(String message) throws Exception {
		final By locator = By
				.className(WebAppLocators.ConversationPage.classPingMessage);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, PING_MESSAGE_TIMEOUT) : "Ping message has not been shown within "
				+ PING_MESSAGE_TIMEOUT + " second(s) timeout";
		return pingMessage.getText().toLowerCase()
				.contains(message.toLowerCase());
	}

	public int numberOfPingMessagesVisible() throws Exception {
		return getDriver().findElementsByClassName(
				WebAppLocators.ConversationPage.classPingMessage).size() - 1;
	}

	public boolean isCallButtonVisible() throws Exception {
		if (WebAppExecutionContext.getBrowser()
				.isSupportingNativeMouseActions()) {
			DriverUtils.moveMouserOver(this.getDriver(), conversationInput);
		} else {
			// safari workaround
			DriverUtils.addClass(this.getDriver(), conversation, "hover");
		}
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(WebAppLocators.ConversationPage.xpathCallButton), 5);
	}

	public void clickCallButton() throws Exception {
		if (WebAppExecutionContext.getBrowser()
				.isSupportingNativeMouseActions()) {
			DriverUtils.moveMouserOver(this.getDriver(), conversationInput);
		} else {
			// safari workaround
			DriverUtils.addClass(this.getDriver(), conversation, "hover");
		}
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(WebAppLocators.ConversationPage.xpathCallButton), 5);
		callButton.click();
	}

	private static final int TEXT_MESSAGE_VISIBILITY_TIMEOUT_SECONDS = 5;

	public boolean isTextMessageVisible(String message) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.textMessageByText
						.apply(message));
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, TEXT_MESSAGE_VISIBILITY_TIMEOUT_SECONDS);
	}

	private static final int MISSED_CALL_MSG_TIMOEUT = 15;

	public String getMissedCallMessage() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathMissedCallAction);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, MISSED_CALL_MSG_TIMOEUT) : "Missed call message is not visible after "
				+ MISSED_CALL_MSG_TIMOEUT + " second(s) timeout";
		return getDriver().findElement(locator).getText();
	}

	private static final int MAX_CALLING_BAR_VISIBILITY_TIMEOUT = 5; // seconds

	public void waitForCallingBarToBeDisplayed() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(WebAppLocators.ConversationPage.xpathCallingBarRoot),
				MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Calling bar has not been shown within "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " second(s)";
	}

	public void waitForCallingBarToBeDisplayedWithName(String name)
			throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ConversationPage.xpathCallingBarRootByName
								.apply(name)),
						MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Calling bar with name "
				+ name
				+ " has not been shown within "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " second(s)";
	}

	public void clickAcceptCallButton() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathAcceptCallButton);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Accept call button has not been shown after "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " seconds";
		getDriver().findElement(locator).click();
	}

	public void clickEndCallButton() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathEndCallButton);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "End call button has not been shown after "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " seconds";
		getDriver().findElement(locator).click();
	}

	public void clickSilenceCallButton() throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathSilenceIncomingCallButton);
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				locator, MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Silence call button has not been shown after "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " seconds";
		getDriver().findElement(locator).click();
	}

	public void verifyCallingBarIsNotVisible() throws Exception {
		assert DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.xpath(WebAppLocators.ConversationPage.xpathCallingBarRoot),
				MAX_CALLING_BAR_VISIBILITY_TIMEOUT) : "Calling bar has not been hidden within "
				+ MAX_CALLING_BAR_VISIBILITY_TIMEOUT + " second(s)";
	}

	public String getLastTextMessage() throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(WebAppLocators.ConversationPage.cssLastTextMessage));
		return lastTextMessage.getText();
	}

	public String getSecondLastTextMessage() throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(WebAppLocators.ConversationPage.cssSecondLastTextMessage));
		return secondLastTextMessage.getText();
	}

	public void clickOnPicture() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By
				.xpath(WebAppLocators.ConversationPage.xpathPictureFullscreen));
		pictureFullscreen.click();
	}

	public boolean isPictureInFullscreen() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ConversationPage.xpathPictureIsFullscreen));
	}

	public void clickXButton() throws Exception {
		xButton.click();
	}

	public void clickOnBlackBorder() throws Exception {
		blackBorder.click();
	}

	public GiphyPage clickGIFButton() throws Exception {
		gifButton.click();
		return new GiphyPage(getLazyDriver());
	}

	public boolean isGifVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ConversationPage.xpathLastImageEntry),
						40);

	}

	public boolean isLastTextMessage(String expectedMessage) throws Exception {
		return DriverUtils
		.waitUntilLocatorIsDisplayed(
				getDriver(),
				By.cssSelector(WebAppLocators.ConversationPage.cssLastTextMessage));
	}
}
