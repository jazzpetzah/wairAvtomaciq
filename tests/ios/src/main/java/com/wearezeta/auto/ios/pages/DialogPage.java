package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.ScreenOrientation;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.ios.IOSConstants;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class DialogPage extends IOSPage {
	private static final Logger log = ZetaLogger.getLog(DialogPage.class
			.getSimpleName());

	public static final String PING_LABEL = "PINGED";
	public static final String HOT_PING_LABEL = "PINGED AGAIN";
	private static final long PING_ANIMATION_TIME = 3000;

	final String[] scriptArr = new String[] {
			"property thisapp: \"Simulator\"",
			"tell application \"System Events\"", " tell process thisapp",
			" click menu item \"Paste\" of menu \"Edit\" of menu bar 1",
			" end tell", "end tell" };

	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
	private WebElement dialogWindow;

	@FindBy(how = How.XPATH, using = IOSLocators.DialogPage.xpathConversationWindow)
	private WebElement conversationWindow;

	@FindBy(how = How.NAME, using = IOSLocators.nameConversationBackButton)
	private WebElement conversationBackButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameConversationCursorInput)
	private WebElement conversationInput;

	@FindBy(how = How.NAME, using = IOSLocators.nameTextInput)
	private WebElement textInput;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPinged)
	private WebElement pinged;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPingedAgain)
	private WebElement pingedAgain;

	@FindBy(how = How.NAME, using = IOSLocators.namePlusButton)
	protected WebElement plusButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameOpenConversationDetails)
	protected WebElement openConversationDetails;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameDialogMessages)
	private List<WebElement> messagesList;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathConnectMessageLabel)
	private WebElement connectMessageLabel;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathConnectionMessage)
	private WebElement connectionMessage;

	@FindBy(how = How.NAME, using = IOSLocators.nameYouRenamedConversation)
	private WebElement youRenamedConversation;

	@FindBy(how = How.NAME, using = IOSLocators.namePendingButton)
	private WebElement pendingButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathLastChatMessage)
	private WebElement lastMessage;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathStartedConversationMessage)
	private WebElement startedConversationMessage;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathAddedToConversationMessage)
	private WebElement addedToConversationMessage;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddPictureButton)
	private WebElement addPictureButton;

	@FindBy(how = How.NAME, using = IOSLocators.DialogPage.nameCallButton)
	private WebElement callButton;

	@FindBy(how = How.NAME, using = IOSLocators.DialogPage.nameCloseButton)
	private WebElement closeButton;

	@FindBy(how = How.XPATH, using = IOSLocators.DialogPage.xpathMessageEntries)
	private List<WebElement> messageEntries;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherConversationCellFormat)
	private WebElement imageCell;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathNameMediaContainer)
	private WebElement mediaContainer;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSoundCloudCellPlayButton)
	private WebElement mediaLinkCell;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathYoutubeVimeoConversationCell)
	private WebElement youtubeCell;

	@FindBy(how = How.NAME, using = IOSLocators.MediaBar.namePlayButton)
	private WebElement mediabarPlayButton;

	@FindBy(how = How.NAME, using = IOSLocators.MediaBar.namePauseButton)
	private WebElement mediabarPauseButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathConversationPage)
	private WebElement conversationPage;

	@FindBy(how = How.NAME, using = IOSLocators.MediaBar.nameCloseButton)
	private WebElement mediabarStopCloseButton;

	@FindBy(how = How.NAME, using = IOSLocators.MediaBar.nameTitle)
	private WebElement mediabarBarTitle;

	@FindBy(how = How.NAME, using = IOSLocators.namePingButton)
	private WebElement pingButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathYouAddedMessageCellFormat)
	private List<WebElement> youAddedCell;

	@FindBy(how = How.NAME, using = IOSLocators.nameAddContactToChatButton)
	protected WebElement addInfoPage;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathDialogTitleBar)
	private WebElement titleBar;

	@FindBy(how = How.NAME, using = IOSLocators.nameSoundCloudPause)
	private WebElement soundCloudPause;

	@FindBy(how = How.NAME, using = IOSLocators.nameChatheadAvatarImage)
	private WebElement chatheadAvatarImage;

	@FindBy(how = How.NAME, using = IOSLocators.DialogPage.nameGifButton)
	private WebElement openGifPreviewButton;

	@FindBy(how = How.NAME, using = IOSLocators.DialogPage.nameCursorSketchButton)
	private WebElement openSketchButton;

	@FindBy(how = How.XPATH, using = IOSLocators.DialogPage.xpathGiphyImage)
	private WebElement giphyImage;

	@FindBy(how = How.NAME, using = IOSLocators.DialogPage.nameSoundCloudButton)
	private WebElement soundCloudButton;

	@FindBy(how = How.XPATH, using = IOSLocators.DialogPage.xpathUserAvatarNextToInput)
	private WebElement userAvatarNextToInput;

	private String connectMessage = "Hi %s, let’s connect on wire. %s";
	private String connectingLabel = "CONNECTING TO %s.";

	public DialogPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public String getLastChatMessage() {
		return lastMessage.getText();
	}

	public String getStartedtChatMessage() {
		return startedConversationMessage.getText();
	}

	public String getAddedtoChatMessage() {
		return startedConversationMessage.getText();
	}

	public boolean isMessageVisible(String msg) throws Exception {

		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(msg));
	}

	public boolean isPingButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(this.getDriver(),
				pingButton);
	}

	public void pressPingButton() {
		pingButton.click();
	}

	public ContactListPage returnToContactList() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(),
				conversationBackButton);
		conversationBackButton.click();
		return new ContactListPage(getLazyDriver());
	}

	public StartedCallPage pressCallButton() throws Exception {
		callButton.click();
		return new StartedCallPage(getLazyDriver());
	}

	public int getNumberOfMessageEntries() {
		return messageEntries.size();
	}

	public boolean waitForCursorInputVisible() throws Exception {
		if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.name(IOSLocators.DialogPage.nameCloseButton), 2)) {
			closeButton.click();
		}
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.name(IOSLocators.nameConversationCursorInput), 10);
	}

	public boolean waitForCursorInputNotVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.name(IOSLocators.nameConversationCursorInput), 3);
	}

	public boolean isCursorInputVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				conversationInput);
	}

	public void waitForYouAddedCellVisible() throws Exception {
		this.getWait().until(
				ExpectedConditions.visibilityOf(youAddedCell.get(0)));
	}

	public StartedCallPage clickOnCallButtonForContact(String contact)
			throws Exception {
		this.getDriver()
				.findElement(
						By.xpath(String
								.format(IOSLocators.xpathFormatMissedCallButtonForContact,
										contact.toUpperCase()))).click();
		return new StartedCallPage(getLazyDriver());
	}

	public void tapOnCursorInput() throws Exception {
		try {
			conversationInput.click();
		} catch (NoSuchElementException e) {
			log.debug(this.getDriver().getPageSource());
			throw e;
		}
	}

	public void multiTapOnCursorInput() throws Exception {
		DriverUtils.iOSMultiTap(this.getDriver(), conversationInput, 3);
	}

	public void sendStringToInput(String message) throws Exception {
		waitForCursorInputVisible();
		try {
			conversationInput.sendKeys(message);
		} catch (WebDriverException ex) {
			clearTextInput();
			conversationInput.sendKeys(message);
		}
	}

	public void clearTextInput() {
		conversationInput.clear();
	}

	public String getStringFromInput() throws Exception {
		return conversationInput.getText();
	}

	public void scrollToTheEndOfConversationByTapOnCursorInput()
			throws Exception {
		String script = IOSLocators.scriptCursorInputPath + ".tap();";
		this.getDriver().executeScript(script);
	}

	public String getConnectionMessage() {

		return connectionMessage.getText();
	}

	public String getRenamedMessage() {

		return youRenamedConversation.getText();
	}

	public String getLastMessageFromDialog() {
		return getLastMessage(messagesList);
	}

	public String getExpectedConnectMessage(String contact, String user) {
		return String.format(connectMessage, contact, user);
	}

	public String getExpectedConnectingLabel(String name) {
		return String.format(connectingLabel, name.toUpperCase());
	}

	public boolean isPendingButtonVisible() {
		return pendingButton.isDisplayed();
	}

	public void swipeInputCursor() throws Exception {
		DriverUtils.swipeRight(this.getDriver(), conversationInput, 1000);
	}

	public void swipeLeftOptionsButtons() throws Exception {
		int inputMiddle = conversationInput.getLocation().y
				+ conversationInput.getSize().height / 2;
		int windowSize = dialogWindow.getSize().height;
		int swipeLocation = inputMiddle * 100 / windowSize;
		DriverUtils.swipeLeftCoordinates(getDriver(), 1000, swipeLocation);
	}

	public CameraRollPage pressAddPictureButton() throws Exception {
		CameraRollPage page;
		addPictureButton.click();
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameCameraLibraryButton));
		page = new CameraRollPage(this.getLazyDriver());
		return page;
	}

	private String GetImageCell(List<WebElement> chatList) throws Exception {
		this.getWait().until(
				ExpectedConditions.presenceOfElementLocated(By
						.xpath(IOSLocators.xpathOtherConversationCellFormat)));
		String lastMessage = imageCell.getAttribute("name");
		return lastMessage;
	}

	public String getImageCellFromDialog() throws Exception {
		return GetImageCell(messagesList);
	}

	public int getNumberOfImages() throws Exception {
		List<WebElement> conversationImages = this.getDriver().findElements(
				By.xpath(IOSLocators.xpathOtherConversationCellFormat));
		return conversationImages.size();
	}

	public void startMediaContent() throws Exception {
		boolean flag = DriverUtils.waitUntilLocatorIsDisplayed(
				this.getDriver(),
				By.xpath(IOSLocators.xpathMediaConversationCell), 3);
		if (flag) {
			mediaLinkCell.click();
		} else {
			this.getDriver().tap(1, soundCloudButton.getLocation().x + 200,
					soundCloudButton.getLocation().y + 200, 1);
		}
	}

	public DialogPage scrollDownTilMediaBarAppears() throws Exception {
		int count = 0;
		while ((count < 3) && !isMediaBarDisplayed()) {
			swipeDialogPageDown(2000);
			count++;
		}

		return this;
	}

	private boolean isMediaBarPauesButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.name(IOSLocators.MediaBar.namePauseButton), 3);
	}

	private void clickMediaBarPauseButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), mediabarPauseButton);
		mediabarPauseButton.click();
	}

	public void pauseMediaContent() throws Exception {
		clickMediaBarPauseButton();
	}

	private boolean isMediaBarPlayButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.name(IOSLocators.MediaBar.namePlayButton), 3);
	}

	private void clickMediaBarPlayButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), mediabarPlayButton);
		mediabarPlayButton.click();
	}

	public void playMediaContent() throws Exception {
		clickMediaBarPlayButton();
	}

	private void clickMediaBarCloseButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(),
				mediabarStopCloseButton);
		mediabarStopCloseButton.click();
	}

	public void stopMediaContent() throws Exception {
		clickMediaBarCloseButton();
	}

	public String getMediaState() throws Exception {
		if (isMediaBarPlayButtonVisible()) {
			return IOSConstants.MEDIA_STATE_PAUSED;
		} else if (isMediaBarPauesButtonVisible()) {
			return IOSConstants.MEDIA_STATE_PLAYING;
		}
		return IOSConstants.MEDIA_STATE_STOPPED;
	}

	public void tapOnMediaBar() {
		mediabarBarTitle.click();
	}

	private final int TEXT_INPUT_HEIGH = 150;
	private final int TOP_BORDER_WIDTH = 40;

	public IOSPage openConversationDetailsClick() throws Exception {
		if (DriverUtils.isElementPresentAndDisplayed(getDriver(),
				openConversationDetails)) {
			openConversationDetails.click();
		} else {
			for (int i = 0; i < 3; i++) {
				if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
						By.name(IOSLocators.namePlusButton))) {
					plusButton.click();
					openConversationDetails.click();
				}
				if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
						By.name(IOSLocators.nameAddContactToChatButton), 2)
						|| DriverUtils
								.waitUntilLocatorIsDisplayed(
										this.getDriver(),
										By.name(IOSLocators.nameOtherUserAddContactToChatButton),
										2)) {
					break;
				} else {
					swipeUp(1000);
				}
			}

		}

		return new OtherUserPersonalInfoPage(this.getLazyDriver());
	}

	public OtherUserOnPendingProfilePage clickConversationDeatailForPendingUser()
			throws Exception {
		plusButton.click();
		openConversationDetails.click();
		return new OtherUserOnPendingProfilePage(this.getLazyDriver());
	}

	@Override
	public IOSPage swipeUp(int time) throws Exception {
		WebElement element = this.getDriver().findElement(
				By.name(IOSLocators.nameMainWindow));

		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		this.getDriver().swipe(coords.x + elementSize.width / 2,
				coords.y + elementSize.height - TEXT_INPUT_HEIGH,
				coords.x + elementSize.width / 2, coords.y + TOP_BORDER_WIDTH,
				time);
		return returnBySwipe(SwipeDirection.UP);
	}

	public DialogPage swipeDialogPageDown(int time) throws Exception {
		DialogPage page = null;
		if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
			DriverUtils.swipeDown(this.getDriver(), conversationPage, time);
			page = this;
		} else {
			swipeDownSimulator();
			page = this;
		}
		return page;
	}

	public DialogPage swipeDialogPageUp(int time) throws Throwable {
		DialogPage page = null;
		if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
			DriverUtils.swipeUp(this.getDriver(), conversationPage, time);
			page = this;
		} else {
			swipeUpSimulator();
			page = this;
		}
		return page;
	}

	public OtherUserOnPendingProfilePage swipePendingDialogPageUp(int time)
			throws Throwable {
		WebElement element = this.getDriver().findElement(
				By.name(IOSLocators.nameMainWindow));

		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		this.getDriver().swipe(coords.x + elementSize.width / 2,
				coords.y + elementSize.height - TEXT_INPUT_HEIGH,
				coords.x + elementSize.width / 2, coords.y + TOP_BORDER_WIDTH,
				time);
		return new OtherUserOnPendingProfilePage(this.getLazyDriver());
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			page = new DialogPage(this.getLazyDriver());
			break;
		}
		case UP: {
			page = new OtherUserPersonalInfoPage(this.getLazyDriver());
			break;
		}
		case LEFT: {
			page = new OtherUserPersonalInfoPage(this.getLazyDriver());
			break;
		}
		case RIGHT: {
			page = new ContactListPage(this.getLazyDriver());
			break;
		}
		}
		return page;
	}

	public boolean isYoutubeContainerVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathYoutubeVimeoConversationCell), 10);
	}

	public boolean isMediaContainerVisible() throws Exception {
		boolean isVisible = DriverUtils.waitUntilLocatorAppears(
				this.getDriver(),
				By.xpath(IOSLocators.xpathMediaConversationCell), 10);
		if (!isVisible) {
			rotateDeviceToRefreshElementsTree();
		}
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathMediaConversationCell), 10);
	}

	public VideoPlayerPage clickOnVideoContainerFirstTime() throws Exception {
		VideoPlayerPage page = new VideoPlayerPage(this.getLazyDriver());
		youtubeCell.click();

		return page;
	}

	public void tapDialogWindow() throws Exception {
		this.getDriver().tap(1, 1, 1, 500);
	}

	public String getConnectMessageLabel() {
		return connectMessageLabel.getText();
	}

	private String getLastMessage(List<WebElement> chatList) {
		String lastMessage = null;
		if (chatList.size() > 0) {
			try {
				String lastMessageXPath = String.format(
						IOSLocators.xpathLastMessageFormat, chatList.size());
				WebElement el = this.getDriver().findElementByXPath(
						lastMessageXPath);
				lastMessage = el.getText();
			} catch (Exception e) {
				lastMessage = "Last message is image";
			}
		} else {
			lastMessage = "Empty chat";
		}
		return lastMessage;
	}

	public long getSendTime() {
		long currentTime;
		Date date = new Date();
		currentTime = date.getTime();
		return currentTime;
	}

	public boolean isMediaBarDisplayed() throws Exception {
		boolean flag = DriverUtils.isElementPresentAndDisplayed(getDriver(),
				mediabarBarTitle);
		return flag;
	}

	public boolean waitMediabarClose() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.name(IOSLocators.MediaBar.nameTitle));
	}

	public DialogPage scrollUpToMediaContainer() throws Throwable {
		DialogPage page = null;
		int count = 0;
		boolean mediaContainerShown = mediaContainer.isDisplayed();
		while (!(mediaContainerShown) & (count < 3)) {
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
				DriverUtils.swipeUp(this.getDriver(), conversationPage, 500);
				page = this;
			} else {
				swipeUpSimulator();
				page = this;
			}
			mediaContainerShown = mediaContainer.isDisplayed();
			count++;
		}

		return page;
	}

	public ImageFullScreenPage tapImageToOpen() throws Throwable {
		ImageFullScreenPage page = null;
		imageCell.click();
		page = new ImageFullScreenPage(this.getLazyDriver());
		return page;
	}

	public void tapHoldTextInput() throws Exception {
		try {
			cmdVscript(scriptArr);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getDriver()
				.tap(1,
						this.getDriver()
								.findElement(
										By.name(IOSLocators.nameConversationCursorInput)),
						1000);
	}

	public DialogPage scrollToBeginningOfConversation() throws Throwable,
			Exception {
		DialogPage page = null;
		int count = 0;
		if (youAddedCell.size() > 0) {
			boolean beginningConversation = youAddedCell.get(0).isDisplayed();
			while (!(beginningConversation) & (count < 5)) {
				if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
					DriverUtils.swipeDown(this.getDriver(), conversationPage,
							500);
					page = this;
				} else {
					swipeDownSimulator();
					page = this;
				}
				beginningConversation = youAddedCell.get(0).isDisplayed();
				count++;
			}
		}
		Assert.assertTrue(youAddedCell.get(0).isDisplayed());
		return page;
	}

	private static final int IMAGE_IN_CONVERSATION_HEIGHT = 510;
	private static final int IMAGE_IN_IPAD_CONVERSATION_HEIGHT = 1020;

	public BufferedImage takeImageScreenshot() throws Throwable {

		BufferedImage image;

		image = getElementScreenshot(imageCell).orElseThrow(
				IllegalStateException::new);

		String deviceType = CommonUtils.getDeviceName(this.getClass());

		if (deviceType.equals("iPhone 6")) {

			image = image.getSubimage(0, image.getHeight()
					- IMAGE_IN_CONVERSATION_HEIGHT, image.getWidth(),
					IMAGE_IN_CONVERSATION_HEIGHT);

		} else {

			image = image.getSubimage(0, image.getHeight()
					- IMAGE_IN_IPAD_CONVERSATION_HEIGHT, image.getWidth(),
					IMAGE_IN_IPAD_CONVERSATION_HEIGHT);
		}

		return image;
	}

	public double isLastImageSameAsTemplate(String filename) throws Throwable {

		BufferedImage templateImage = takeImageScreenshot();
		BufferedImage referenceImage = ImageUtil.readImageFromFile(IOSPage
				.getImagesPath() + filename);

		double score = ImageUtil.getOverlapScore(referenceImage, templateImage,
				ImageUtil.RESIZE_TEMPLATE_TO_RESOLUTION);

		log.debug("SCORE: " + score);

		return score;

	}

	public DialogPage scrollToImage() throws Throwable {
		WebElement el = this.getDriver().findElement(
				By.xpath(IOSLocators.xpathOtherConversationCellFormat));
		DriverUtils.scrollToElement(this.getDriver(), el);
		DialogPage page = new DialogPage(this.getLazyDriver());
		return page;
	}

	private static final String TEXT_MESSAGE_PATTERN = "<UIATextView[^>]*value=\"([^\"]*)\"[^>]*>\\s*</UIATextView>";
	private static final int TIMES_TO_SCROLL = 100;

	public boolean swipeAndCheckMessageFound(String direction, String pattern)
			throws Exception {
		boolean result = false;

		Point coords = new Point(0, 0);
		Dimension elementSize = this.getDriver().manage().window().getSize();

		switch (direction) {
		case "up":
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
				this.getDriver().swipe(coords.x + elementSize.width / 2,
						coords.y + elementSize.height / 2,
						coords.x + elementSize.width / 2, coords.y + 120, 500);
			} else {
				DriverUtils.iOSSimulatorSwipeDialogPageUp(CommonUtils
						.getSwipeScriptPath(IOSPage.class));
			}

			break;
		case "down":
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
				this.getDriver().swipe(coords.x + elementSize.width / 2,
						coords.y + 50, coords.x + elementSize.width / 2,
						coords.y + elementSize.height - 100, 500);
			} else {
				DriverUtils.iOSSimulatorSwipeDialogPageDown(CommonUtils
						.getSwipeScriptPath(IOSPage.class));
			}

			break;
		default:
			log.fatal("Unknown direction");
		}
		String source = this.getDriver().getPageSource();
		Pattern messagesPattern = Pattern.compile(TEXT_MESSAGE_PATTERN);
		Matcher messagesMatcher = messagesPattern.matcher(source);
		while (messagesMatcher.find()) {
			String message = messagesMatcher.group(1);
			Pattern messagePattern = Pattern.compile(pattern);
			Matcher messageMatcher = messagePattern.matcher(message);
			if (messageMatcher.find()) {
				result = true;
			}
		}
		return result;
	}

	public void swipeTillTextMessageWithPattern(String direction, String pattern)
			throws IOException, Exception {
		boolean isAddedMessage = false;
		int count = 0;
		do {
			isAddedMessage = swipeAndCheckMessageFound(direction, pattern);
			count++;
		} while (!isAddedMessage && count < TIMES_TO_SCROLL);
	}

	private static final String UUID_TEXT_MESSAGE_PATTERN = "<UIATextView[^>]*value=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*>\\s*</UIATextView>";
	private static final String DIALOG_START_MESSAGE_PATTERN = "^(.*)\\sADDED\\s(.*)$";

	public ArrayList<MessageEntry> listAllMessages(boolean checkTime)
			throws Exception, Throwable {
		try {
			log.debug("Trying to close keyboard");
			this.getDriver().hideKeyboard();
		} catch (WebDriverException e) {
		}

		String lastMessage = messagesList.get(messagesList.size() - 1)
				.getText();

		swipeTillTextMessageWithPattern("down", DIALOG_START_MESSAGE_PATTERN);

		LinkedHashMap<String, MessageEntry> messages = new LinkedHashMap<String, MessageEntry>();

		boolean lastMessageAppears = false;
		boolean temp = false;
		int i = 0;
		do {
			i++;
			lastMessageAppears = temp;
			Date receivedDate = new Date();
			String source = this.getDriver().getPageSource();
			Pattern pattern = Pattern.compile(UUID_TEXT_MESSAGE_PATTERN);
			Matcher matcher = pattern.matcher(source);
			while (matcher.find()) {
				if (messages.get(matcher.group(1)) == null) {
					messages.put(matcher.group(1), new MessageEntry("text",
							matcher.group(1), receivedDate, checkTime));
				}
			}
			this.getDriver().getPageSource();
			if (!lastMessageAppears) {
				temp = swipeAndCheckMessageFound("up", lastMessage);
			}
		} while (!lastMessageAppears && i < TIMES_TO_SCROLL);

		ArrayList<MessageEntry> listResult = new ArrayList<MessageEntry>();

		for (Map.Entry<String, MessageEntry> mess : messages.entrySet()) {
			listResult.add(mess.getValue());
		}
		return listResult;
	}

	public MessageEntry receiveMessage(String message, boolean checkTime)
			throws Exception {
		WebElement messageElement = null;
		try {
			String messageXpath = String.format(
					IOSLocators.xpathFormatSpecificMessageContains, message);
			Date receivedDate = new Date();
			long startDate = new Date().getTime();
			messageElement = this.getDriver().findElement(
					By.xpath(messageXpath));
			long endDate = new Date().getTime();
			long time = endDate - startDate;
			if (messageElement != null) {
				return new MessageEntry("text", message, new Date(
						receivedDate.getTime() + time / 2), checkTime);
			}
		} catch (NoSuchElementException e) {
			log.debug(this.getDriver().getPageSource());
			throw e;
		}
		return null;
	}

	public void sendMessageUsingScript(String message) throws Exception {
		fillInMessageUsingScript(message);
		clickKeyboardReturnButton();
	}

	public void fillInMessageUsingScript(String message) throws Exception {
		DriverUtils.sendTextToInputByScript(getDriver(),
				IOSLocators.scriptCursorInputPath, message);
	}

	public void waitLoremIpsumText() throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.xpath(IOSLocators.DialogPage.xpathLoremIpsumText), 10);
	}

	public void waitSoundCloudLoad() throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.DialogPage.nameSoundCloudContainer));
	}

	public void sendMessagesUsingScript(String[] messages) throws Exception {
		// swipe down workaround
		try {
			Point coords = new Point(0, 0);
			Dimension elementSize = this.getDriver().manage().window()
					.getSize();
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
				this.getDriver().swipe(coords.x + elementSize.width / 2,
						coords.y + 50, coords.x + elementSize.width / 2,
						coords.y + elementSize.height - 100, 500);
			} else {
				DriverUtils.iOSSimulatorSwipeDialogPageDown(CommonUtils
						.getSwipeScriptPath(IOSPage.class));
			}
		} catch (Exception e) {
		}

		scrollToTheEndOfConversationByTapOnCursorInput();
		String script = "";
		for (int i = 0; i < messages.length; i++) {
			script += String.format(IOSLocators.scriptCursorInputPath
					+ ".setValue(\"%s\");"
					+ IOSLocators.scriptKeyboardReturnKeyPath + ".tap();",
					messages[i]);
		}
		this.getDriver().executeScript(script);
	}

	public void takeCameraPhoto() throws Exception {
		swipeInputCursor();
		CameraRollPage page = pressAddPictureButton();
		page.pressSelectFromLibraryButton();
		page.pressConfirmButton();
	}

	public DialogPage sendImageFromAlbum() throws Exception {
		swipeInputCursor();
		Thread.sleep(1000);
		CameraRollPage page = pressAddPictureButton();
		page.pressSelectFromLibraryButton();
		page.clickFirstLibraryFolder();
		page.clickFirstImage();
		page.pressConfirmButton();
		return new DialogPage(this.getLazyDriver());
	}

	public void pasteTextToInput(String text) throws Throwable {
		WebElement el = this.getDriver().findElement(
				By.name(IOSLocators.nameConversationCursorInput));
		if (isSimulator()) {
			cmdVscript(scriptArr);
			pasteStringToInput(el, text);
		} else {
			pasteStringToInput(el, text);
		}
	}

	public double checkPingIcon(String label) throws Exception {
		String path = null;
		BufferedImage pingImage = null;
		ScreenOrientation orient = getOrientation();
		if (label.equals(PING_LABEL)) {
			pingImage = getPingIconScreenShot();
			path = CommonUtils.getPingIconPathIOS(GroupChatPage.class);
			if (orient == ScreenOrientation.LANDSCAPE) {
				path = path.replace(".png", "_landscape.png");
			}
		} else if (label.equals(HOT_PING_LABEL)) {
			pingImage = getPingAgainIconScreenShot();
			path = CommonUtils.getHotPingIconPathIOS(GroupChatPage.class);
			if (orient == ScreenOrientation.LANDSCAPE) {
				path = path.replace(".png", "_landscape.png");
			}
		}
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		return ImageUtil.getOverlapScore(pingImage, templateImage);
	}

	private static final int PING_ICON_WIDTH = 72;
	private static final int PING_ICON_HEIGHT = 60;
	private static final int PING_ICON_Y_OFFSET = 7;

	private BufferedImage getPingIconScreenShot() throws Exception {
		Point elementLocation = pinged.getLocation();
		Dimension elementSize = pinged.getSize();
		int x = elementLocation.x * 2 + elementSize.width * 2;
		int y = (elementLocation.y - PING_ICON_Y_OFFSET) * 2;
		int w = PING_ICON_WIDTH;
		int h = PING_ICON_HEIGHT;
		return getScreenshotByCoordinates(x, y, w, h).orElseThrow(
				IllegalStateException::new);
	}

	private BufferedImage getPingAgainIconScreenShot() throws Exception {
		Point elementLocation = pingedAgain.getLocation();
		Dimension elementSize = pingedAgain.getSize();
		int x = elementLocation.x * 2 + elementSize.width * 2;
		int y = (elementLocation.y - PING_ICON_Y_OFFSET) * 2;
		int w = PING_ICON_WIDTH;
		int h = PING_ICON_HEIGHT;
		return getScreenshotByCoordinates(x, y, w, h).orElseThrow(
				IllegalStateException::new);
	}

	public void waitPingAnimation() throws InterruptedException {
		Thread.sleep(PING_ANIMATION_TIME);
	}

	public int getNumberOfPingedMessages(String xpath) throws Exception {
		List<WebElement> pingedMessages = this.getDriver().findElements(
				By.xpath(xpath));
		log.debug("Retrieved number of Pings in conversation: "
				+ pingedMessages.size());
		return pingedMessages.size();
	}

	public void scrollToEndOfConversation() throws Exception {
		WebElement el = this.getDriver().findElement(
				By.xpath(IOSLocators.xpathLastChatMessage));
		try {
			DriverUtils.scrollToElement(this.getDriver(), el);
		} catch (WebDriverException e) {

		}
	}

	public boolean isTitleBarDisplayed(String name) throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(String.format(IOSLocators.xpathDialogTitleBar, name)));
	}

	public boolean isTitleBarNamed(String chatName) {
		log.debug("Title bar name is : " + titleBar.getAttribute("name"));
		return titleBar.getAttribute("name").equals(chatName.toUpperCase());
	}

	public boolean isTypeOrSlideExists(String msg) throws Exception {
		return DriverUtils
				.waitUntilLocatorAppears(getDriver(), By.name(msg), 5);
	}

	public boolean chatheadIsVisible(String contact) throws Exception {

		List<WebElement> el = this.getDriver()
				.findElements(
						By.xpath(String.format(IOSLocators.xpathChatheadName,
								contact)));
		for (WebElement element : el) {
			if (DriverUtils.isElementPresentAndDisplayed(getDriver(), element)) {
				return true;
			}
		}
		return false;
	}

	public boolean chatheadMessageIsVisible(String message) throws Exception {
		WebElement el = this.getDriver().findElement(
				By.xpath(String.format(IOSLocators.xpathChatheadMessage,
						message)));
		if (el.isDisplayed()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean chatheadAvatarImageIsVisible() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.nameChatheadAvatarImage))) {
			return true;
		} else {
			return false;
		}
	}

	public void clickOnPlayVideoButton() throws Exception {
		youtubeCell.click();
	}

	public void openGifPreviewPage() {
		openGifPreviewButton.click();
	}

	public void openSketch() {
		openSketchButton.click();
	}

	public boolean isMyNameInDialogDisplayed(String name) throws Exception {
		WebElement el = getDriver().findElement(
				By.xpath(String.format(
						IOSLocators.DialogPage.xpathMyNameInDialog,
						name.toUpperCase())));
		return DriverUtils.isElementPresentAndDisplayed(getDriver(), el);
	}

	public boolean isConnectedToUserStartedConversationLabelVisible(
			String username) throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(String
				.format(IOSLocators.DialogPage.xpathConnectedToUserLabel,
						username.toUpperCase())), 5);
	}

	/**
	 * Navigates back by swipe and initialize ContactListPage
	 * 
	 * @throws Exception
	 */
	public ContactListPage navigateBack(int timeMilliseconds) throws Exception {
		swipeRight(timeMilliseconds,
				DriverUtils.SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL, 30);
		return new ContactListPage(this.getLazyDriver());
	}

	public void clickPlusButton() {
		plusButton.click();
	}

	public boolean isPlusButtonVisible() throws Exception {
		return DriverUtils
				.isElementPresentAndDisplayed(getDriver(), plusButton);
	}

	public boolean waitPlusButtonNotVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.name(IOSLocators.namePlusButton));
	}

	public boolean isOpenConversationDetailsButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				openConversationDetails);
	}

	public boolean isCallButtonVisible() throws Exception {
		return DriverUtils
				.isElementPresentAndDisplayed(getDriver(), callButton);
	}

	public boolean isCameraButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				addPictureButton);
	}

	public boolean isOpenScetchButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				openSketchButton);
	}

	public boolean isCloseButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				closeButton);
	}

	public void clickCloseButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), closeButton);
		closeButton.click();
	}

	public boolean isGiphyImageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(IOSLocators.DialogPage.xpathGiphyImage));
	}

	public void tapOnLink() throws Exception {
		WebElement tapLink = this.getDriver().findElement(
				By.xpath(IOSLocators.DialogPage.xpathSimpleMessageLink));
		DriverUtils.mobileTapByCoordinates(getDriver(), tapLink);
	}

	public void tapOnLinkWithinAMessage() throws Exception {
		WebElement tapLink = this.getDriver().findElement(
				By.xpath(IOSLocators.DialogPage.xpathSimpleMessageLink));
		DriverUtils.mobileTapByCoordinates(getDriver(), tapLink,
				-(tapLink.getSize().width / 4), 0);
	}

	public boolean isTherePossibilityControllerButtonsToBeDisplayed() {
		int pingX = pingButton.getLocation().x;
		int conversationX = conversationWindow.getLocation().x;
		return pingX > conversationX;
	}

	public void tapHoldImage() {
		try {
			this.getDriver().tap(
					1,
					this.getDriver().findElement(
							By.xpath(IOSLocators.DialogPage.xpathImage)), 1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isUserAvatarNextToInputVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				userAvatarNextToInput);
	}

}
