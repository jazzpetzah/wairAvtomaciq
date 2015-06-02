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
import com.wearezeta.auto.ios.locators.IOSLocators;

public class DialogPage extends IOSPage {
	private static final Logger log = ZetaLogger.getLog(DialogPage.class
			.getSimpleName());

	public static final String PING_LABEL = "PINGED";
	public static final String HOT_PING_LABEL = "PINGED AGAIN";
	private static final long PING_ANIMATION_TIME = 3000;

	final String[] scriptArr = new String[] {
			"property thisapp: \"iOS Simulator\"",
			"tell application \"System Events\"", " tell process thisapp",
			" click menu item \"Paste\" of menu \"Edit\" of menu bar 1",
			" end tell", "end tell" };

	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
	private WebElement dialogWindow;

	// @FindBy(how = How.XPATH, using = IOSLocators.xpathCursorInput)
	// private WebElement cursorInput;

	@FindBy(how = How.NAME, using = IOSLocators.nameConversationCursorInput)
	private WebElement conversationInput;

	@FindBy(how = How.NAME, using = IOSLocators.nameTextInput)
	private WebElement textInput;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPinged)
	private WebElement pinged;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPingedAgain)
	private WebElement pingedAgain;

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

	@FindBy(how = How.NAME, using = IOSLocators.nameAddPictureButton)
	private WebElement addPictureButton;

	@FindBy(how = How.XPATH, using = IOSLocators.DialogPage.xpathCallButton)
	private WebElement callButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherConversationCellFormat)
	private WebElement imageCell;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathNameMediaContainer)
	private WebElement mediaContainer;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathMediaConversationCell)
	private WebElement mediaLinkCell;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathYoutubeConversationCell)
	private WebElement youtubeCell;

	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarPlayPauseButton)
	private WebElement mediabarPlayPauseButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathConversationPage)
	private WebElement conversationPage;

	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarCloseButton)
	private WebElement mediabarStopCloseButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarTitle)
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

	public boolean isMessageVisible(String msg) throws Exception {

		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(msg));
	}

	public boolean isPingButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.namePingButton));
	}

	public void pressPingButton() {
		pingButton.click();
	}

	public StartedCallPage pressCallButton() throws Exception {
		callButton.click();
		return new StartedCallPage(getLazyDriver());
	}

	public boolean waitForCursorInputVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.name(IOSLocators.nameConversationCursorInput), 10);
	}

	public boolean waitForCursorInputNotVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.name(IOSLocators.nameConversationCursorInput), 3);
	}

	public void waitForYouAddedCellVisible() throws Exception {
		this.getWait().until(
				ExpectedConditions.visibilityOf(youAddedCell.get(0)));
	}

	public StartedCallPage clickOnCallButtonForContact(String contact)
			throws Exception {
		WebElement el = this.getDriver().findElementByXPath(
				String.format(IOSLocators.xpathUserMessageEntry, contact));
		el.findElement(By.className("UIAButton")).click();
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

	public void sendStringToInput(String message) throws InterruptedException {
		conversationInput.sendKeys(message);
	}

	public void scrollToTheEndOfConversation() throws Exception {
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

	public CameraRollPage pressAddPictureButton() throws Exception {
		CameraRollPage page;
		addPictureButton.click();
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathCameraLibraryButton));
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
				By.xpath(IOSLocators.xpathMediaConversationCell));
		if (flag) {
			mediaLinkCell.click();
		} else {
			String lastMessageXPath = String.format(
					IOSLocators.xpathLastMessageFormat, messagesList.size());
			WebElement el = this.getDriver().findElementByXPath(
					lastMessageXPath);
			this.getDriver().tap(
					1,
					10,
					el.getLocation().y + el.getSize().height
							+ (el.getSize().height / 2), 1);
		}
	}

	public DialogPage scrollDownTilMediaBarAppears() throws Exception {
		int count = 0;
		boolean buttonIsShown = false;
		if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
			DriverUtils.swipeDown(this.getDriver(), conversationPage, 1000);

		} else {
			swipeDownSimulator();

		}
		while (!buttonIsShown && (count < 20)) {
			if (mediaContainer.getLocation().y < dialogWindow.getSize().height) {
				Thread.sleep(1000);
			} else {
				buttonIsShown = true;
			}
			count++;
		}

		return this;
	}

	public void pauseMediaContent() throws Exception {
		this.getDriver().tap(
				1,
				mediabarPlayPauseButton.getLocation().x,
				mediabarPlayPauseButton.getLocation().y
						+ mediabarPlayPauseButton.getSize().getHeight(), 1);
	}

	public void playMediaContent() throws Exception {
		this.getDriver().tap(
				1,
				mediabarPlayPauseButton.getLocation().x,
				mediabarPlayPauseButton.getLocation().y
						+ mediabarPlayPauseButton.getSize().getHeight(), 1);
	}

	public void stopMediaContent() throws Exception {
		this.getDriver().tap(
				1,
				mediabarStopCloseButton.getLocation().x,
				mediabarStopCloseButton.getLocation().y
						+ mediabarStopCloseButton.getSize().getHeight(), 1);
	}

	public String getMediaState() {

		String mediaState = mediabarBarTitle.getAttribute("value");
		return mediaState;
	}

	public void tapOnMediaBar() {
		mediabarBarTitle.click();
	}

	private final int TEXT_INPUT_HEIGH = 150;
	private final int TOP_BORDER_WIDTH = 40;

	public IOSPage openConversationDetailsClick() throws Exception {

		for (int i = 0; i < 3; i++) {
			if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
					By.name(IOSLocators.nameOpenConversationDetails))) {
				openConversationDetails.click();
				DriverUtils.waitUntilLocatorAppears(this.getDriver(),
						By.name(IOSLocators.nameAddContactToChatButton), 5);
			}
			if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
					By.name(IOSLocators.nameAddContactToChatButton))) {
				break;
			} else {
				swipeUp(1000);
			}
		}

		return new OtherUserPersonalInfoPage(this.getLazyDriver());
	}

	public OtherUserOnPendingProfilePage clickConversationDeatailForPendingUser()
			throws Exception {
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

	public boolean isMediaContainerVisible() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathMediaConversationCell));
		return mediaLinkCell != null;
	}

	public VideoPlayerPage clickOnVideoContainerFirstTime() throws Exception {
		VideoPlayerPage page = new VideoPlayerPage(this.getLazyDriver());
		youtubeCell.click();
		if (!page.isVideoPlayerPageOpened()) {
			youtubeCell.click();
		}

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

	public boolean isMediaBarDisplayed() {
		boolean flag = mediabarPlayPauseButton.isDisplayed();
		return flag;
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

	private static final int IMAGE_CONTROL_IN_CONVERSATION_HEIGHT = 472;
	private static final int IMAGE_IN_CONVERSATION_HEIGHT = 427;

	public BufferedImage takeImageScreenshot() throws Throwable {
		BufferedImage image;
		image = getElementScreenshot(imageCell).orElseThrow(
				IllegalStateException::new);
		if (image.getHeight() > IMAGE_IN_CONVERSATION_HEIGHT) {
			image = image.getSubimage(0, image.getHeight()
					- IMAGE_CONTROL_IN_CONVERSATION_HEIGHT, image.getWidth(),
					IMAGE_IN_CONVERSATION_HEIGHT);
		}
		return image;
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
		String script = String.format(IOSLocators.scriptCursorInputPath
				+ ".setValue(\"%s\");", message);
		this.getDriver().executeScript(script);
		clickKeyboardReturnButton();
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

		scrollToTheEndOfConversation();
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

	public boolean isTitleBarDisplayed() throws InterruptedException {
		// wait for the title bar to animate onto the page
		Thread.sleep(1000);
		// check if the title bar is off the page or not
		return titleBar.getLocation().y >= 0;
	}

	public boolean isTitleBarNamed(String chatName) {
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
		if (el.size() > 0) {
			return true;
		} else {
			return false;
		}
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

	public boolean chatheadAvatarImageIsVisible() {
		if (chatheadAvatarImage.isDisplayed()) {
			return true;
		} else {
			return false;
		}
	}
}
