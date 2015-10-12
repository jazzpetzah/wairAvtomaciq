package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import android.graphics.Point;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;

public class DialogPage extends AndroidPage {

	private static final Logger log = ZetaLogger.getLog(DialogPage.class
			.getSimpleName());

	public static final String MEDIA_PLAY = "PLAY";
	public static final String MEDIA_PAUSE = "PAUSE";
	public static final String PING_LABEL = "PINGED";
	public static final String HOT_PING_LABEL = "PINGED AGAIN";
	public static final String MUTE_BUTTON_LABEL = "MUTE";
	public static final String SPEAKER_BUTTON_LABEL = "SPEAKER";
	public static final String I_LEFT_CHAT_MESSAGE = "YOU HAVE LEFT";

	public static final String xpathConfirmOKButton = "//*[@id='ttv__confirmation__confirm' and @value='OK']";

	public static final String idDialogImages = "iv__row_conversation__message_image";
	private static final String xpathLastPicture = String.format(
			"(//*[@id='%s'])[last()]", idDialogImages);

	public static final String idAddPicture = "cursor_menu_item_camera";

	public static final String idSelfAvatar = "civ__cursor__self_avatar";
	@FindBy(id = idSelfAvatar)
	private WebElement selfAvatar;
	final By selfAvatarLocator = By.id(idSelfAvatar);

	private static final Function<String, String> xpathConversationMessageByText = text -> String
			.format("//*[@id='ltv__row_conversation__message' and @value='%s']",
					text);

	private static final Function<String, String> xpathUnsentIndicatorByText = text -> String
			.format("%s/parent::*/parent::*//*[@id='v__row_conversation__error']",
					xpathConversationMessageByText.apply(text));

	private static final String xpathUnsentIndicatorForImage = "//*[@id='"
			+ idDialogImages
			+ "']/parent::*/parent::*//*[@id='v__row_conversation__error']";

	@FindBy(id = giphyPreviewButtonId)
	private WebElement giphyPreviewButton;
	final By giphyPreviewButtonLocator = By.id(giphyPreviewButtonId);

	@FindBy(id = idEditText)
	private WebElement cursorInput;

	@FindBy(id = idCursorArea)
	private WebElement cursorArea;

	private static final String idCursorBtn = "typing_indicator_button";
	@FindBy(id = idCursorBtn)
	private WebElement cursorBtn;

	private static final String idCursorBtnImg = "typing_indicator_imageview";
	@FindBy(id = idCursorBtnImg)
	private WebElement cursorBtnImg;

	private static final String idMessage = "ltv__row_conversation__message";
	@FindBy(id = idMessage)
	private List<WebElement> messagesList;

	private static final String idMissedCallMesage = "ttv__row_conversation__missed_call";

	private static final Function<String, String> xpathMissedCallMesageByText = text -> String
			.format("//*[@id='%s' and @value='%s']", idMissedCallMesage, text);

	private static final String idCursorFrame = "cursor_layout";
	@FindBy(id = idCursorFrame)
	public WebElement cursorFrame;

	public static final Function<String, String> xpathPingMessageByText = text -> String
			.format("//*[@id='ttv__row_conversation__ping_message' and @value='%s']",
					text);

	private static final String idPingIcon = "gtv__knock_icon";
	@FindBy(id = idPingIcon)
	private WebElement pingIcon;

	private static final String xpathDialogTakePhotoButton = "//*[@id='gtv__camera_control__take_a_picture' and @shown='true']";
	@FindBy(xpath = xpathDialogTakePhotoButton)
	private WebElement takePhotoButton;

	private static final String idDialogChangeCameraButton = "gtv__camera__top_control__back_camera";
	@FindBy(id = idDialogChangeCameraButton)
	private WebElement changeCameraButton;

	@FindBy(xpath = xpathConfirmOKButton)
	private WebElement okButton;

	private static final String idSketchImagePaintButton = "gtv__sketch_image_paint_button";
	@FindBy(id = idSketchImagePaintButton)
	private WebElement sketchImagePaintButton;

	@FindBy(id = idDialogImages)
	private WebElement image;

	private static final String idFullScreenImage = "tiv__single_image_message__image";
	@FindBy(id = idFullScreenImage)
	private WebElement fullScreenImage;

	@FindBy(id = idDialogImages)
	private List<WebElement> imageList;

	private static final String idConnectRequestDialog = "connect_request_root";
	@FindBy(id = idConnectRequestDialog)
	private WebElement connectRequestDialog;

	public static final String idParticipantsBtn = "cursor_menu_item_participant";
	@FindBy(id = idParticipantsBtn)
	private WebElement participantsButton;

	private static final String idConnectRequestMessage = "contact_request_message";
	@FindBy(id = idConnectRequestMessage)
	private WebElement connectRequestMessage;

	private static final String idConnectRequestConnectTo = "user_name";
	@FindBy(id = idConnectRequestConnectTo)
	private WebElement connectRequestConnectTo;

	private static final String idBackgroundOverlay = "v_background_dark_overlay";
	@FindBy(id = idBackgroundOverlay)
	private WebElement backgroundOverlay;

	private static final String idConnectRequestChatLabel = "ttv__row_conversation__connect_request__chathead_footer__label";
	@FindBy(id = idConnectRequestChatLabel)
	private WebElement connectRequestChatLabel;

	private static final String idConnectRequestChatUserName = "ttv__row_conversation__connect_request__chathead_footer__username";
	@FindBy(id = idConnectRequestChatUserName)
	private WebElement connectRequestChatUserName;

	@FindBy(id = idGalleryBtn)
	private WebElement galleryBtn;

	@FindBy(id = idSearchHintClose)
	private WebElement closeHintBtn;

	@FindBy(id = idCloseImageBtn)
	private WebElement closeImageBtn;

	private static final String idPlayPauseMedia = "gtv__media_play";
	@FindBy(id = idPlayPauseMedia)
	private WebElement playPauseBtn;

	private static final String idYoutubePlayButton = "gtv__youtube_message__play";
	@FindBy(id = idYoutubePlayButton)
	private WebElement playYoutubeBtn;

	private static final String idMediaBarControl = "gtv__conversation_header__mediabar__control";
	@FindBy(id = idMediaBarControl)
	private WebElement mediaBarControl;

	@FindBy(id = idAddPicture)
	private WebElement addPictureBtn;

	private static final String idPing = "cursor_menu_item_ping";
	@FindBy(id = idPing)
	private WebElement pingBtn;

	private static final String idSketch = "cursor_menu_item_draw";
	@FindBy(id = idSketch)
	private WebElement sketchBtn;

	private static final String idCall = "cursor_menu_item_calling";
	@FindBy(id = idCall)
	private WebElement callBtn;

	public static final String idCursorCloseButton = "cursor_button_close";
	@FindBy(id = idCursorCloseButton)
	private WebElement closeBtn;

	private static final String idMute = "cib__calling__mic_mute";
	@FindBy(id = idMute)
	private WebElement muteBtn;

	private static final String idSpeaker = "cib__calling__speaker";
	@FindBy(id = idSpeaker)
	private WebElement speakerBtn;

	private static final String idCancelCall = "cib__calling__dismiss";
	@FindBy(id = idCancelCall)
	private WebElement cancelCallBtn;

	private static final String idNewConversationNameMessage = "ttv__row_conversation__new_conversation_name";
	@FindBy(id = idNewConversationNameMessage)
	private WebElement newConversationNameMessage;

	private static final String xpathLastConversationMessage = "(//*[@id='ltv__row_conversation__message'])[last()]";
	@FindBy(xpath = xpathLastConversationMessage)
	private WebElement lastConversationMessage;

	private static final String idFullScreenImageImage = "tiv__single_image_message__animating_image";
	@FindBy(id = idFullScreenImageImage)
	private WebElement fullScreenImageImage;

	public static Function<String, String> xpathInputFieldByValue = value -> String
			.format("//*[@value='%s']", value);

	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.75;
	private final String DIALOG_IMAGE = "android_dialog_sendpicture_result.png";
	private static final int DEFAULT_SWIPE_TIME = 500;
	private static final int MAX_SWIPE_RETRIES = 5;
	private static final int MAX_CLICK_RETRIES = 5;

	public DialogPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean waitForCursorInputVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCursorArea));
	}

	public boolean waitForCursorInputNotVisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idCursorArea));
	}

	public void tapOnCursorInput() throws Exception {
		// FIXME: Scroll to the bottom if cursor input is not visible
		if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCursorBtnImg), 2)) {
			tapOnCursorFrame();
			this.hideKeyboard();
		}
		cursorArea.click();
	}

	public void tapOnCursorFrame() {
		cursorFrame.click();
	}

	public void tapOnTextInputIfVisible() throws Exception {
		if (waitForCursorInputVisible()) {
			cursorArea.click();
		}
	}

	public void sendMessageInInput() throws Exception {
		this.hideKeyboard();
		cursorInput.sendKeys("\\n");
	}

	public void multiTapOnCursorInput() throws Exception {
		DriverUtils.androidMultiTap(this.getDriver(), cursorArea, 2, 500);
	}

	public void swipeOnCursorInput() throws Exception {
		// FIXME: Scroll to the bottom if cursor input is not visible
		tapOnCursorFrame();
		this.hideKeyboard();

		getWait().until(ExpectedConditions.elementToBeClickable(cursorArea));
		final By cursorLocator = By.id(idCursorArea);
		int ntry = 1;
		do {
			DriverUtils.swipeRight(this.getDriver(), cursorArea,
					DEFAULT_SWIPE_TIME);
			final int currentCursorOffset = getDriver()
					.findElement(cursorLocator).getLocation().getX();
			if (currentCursorOffset > getDriver().manage().window().getSize()
					.getWidth() / 2) {
				Thread.sleep(500);
				return;
			}
			log.debug(String.format(
					"Failed to swipe the text cursor. Retrying (%s of %s)...",
					ntry, MAX_SWIPE_RETRIES));
			ntry++;
			tapOnTextInputIfVisible();
			this.hideKeyboard();
			Thread.sleep(1000);
		} while (ntry <= MAX_SWIPE_RETRIES);
		throw new RuntimeException(
				String.format(
						"Failed to swipe the text cursor on input field after %s retries!",
						MAX_SWIPE_RETRIES));
	}

	public void tapAddPictureBtn() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), addPictureBtn);
		addPictureBtn.click();
	}

	public void tapPingBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), pingBtn);
		pingBtn.click();
	}

	public void tapSketchBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), sketchBtn);
		sketchBtn.click();
	}

	public void tapCallBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), callBtn);
		callBtn.click();
	}

	public void closeInputOptions() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), closeBtn);
		closeBtn.click();
	}

	public void tapMuteBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), muteBtn);
		muteBtn.click();
	}

	public void tapSpeakerBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), speakerBtn);
		speakerBtn.click();
	}

	public void tapCancelCallBtn() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), cancelCallBtn);
		cancelCallBtn.click();
	}

	private WebElement getButtonElementByName(String name) {
		final String uppercaseName = name.toUpperCase();
		switch (uppercaseName) {
		case "MUTE":
			return muteBtn;
		case "SPEAKER":
			return speakerBtn;
		default:
			throw new NoSuchElementException(String.format(
					"Button '%s' is unknown", name));
		}
	}

	public BufferedImage getCurrentButtonStateScreenshot(String name)
			throws Exception {
		final WebElement dstButton = getButtonElementByName(name);
		assert DriverUtils.waitUntilElementClickable(getDriver(), dstButton);
		return getElementScreenshot(dstButton).orElseThrow(
				IllegalStateException::new);
	}

	public void typeAndSendMessage(String message) throws Exception {
		// FIXME: Find a better solution for text autocorrection issues
		final int maxTries = 5;
		int ntry = 0;
		do {
			cursorInput.clear();
			cursorInput.sendKeys(message);
			ntry++;
		} while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathInputFieldByValue.apply(message)), 2)
				&& ntry < maxTries);
		if (ntry >= maxTries) {
			throw new IllegalStateException(
					String.format(
							"The string '%s' was autocorrected. Please disable autocorrection on the device and restart the test.",
							message));
		}
		if (DriverUtils.waitUntilLocatorAppears(getDriver(), selfAvatarLocator)
				&& (selfAvatar.getLocation().y * 100
						/ getDriver().manage().window().getSize().getHeight() < 90)) {
			log.info("Press keyboard send button by coordinates");
			pressKeyboardSendButton();
			this.hideKeyboard();
		} else {
			// FIXME: Enter does not send text anymore, unicode tests affected
			this.pressEnter();
			this.hideKeyboard();
		}
	}

	public void typeMessage(String message) throws Exception {
		cursorInput.sendKeys(message);
		if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathInputFieldByValue.apply(message)), 2)) {
			log.warn(String
					.format("The message '%s' was autocorrected. This might cause unpredicted test results",
							message));
		}
	}

	public void pressKeyboardSendButton() throws Exception {
		tapByCoordinates(94, 96);
	}

	public void clickLastImageFromDialog() throws Exception {
		final By locator = By.xpath(xpathLastPicture);
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : "No pictures are visible in the conversation view";
		getDriver().findElement(locator).click();
	}

	public String getChangedGroupNameMessage() {
		return newConversationNameMessage.getText();
	}

	public boolean waitForMessage(String text) throws Exception {
		tapDialogPageBottom();
		final By locator = By.xpath(xpathConversationMessageByText.apply(text));
		return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
	}

	public boolean waitForUnsentIndicator(String text) throws Exception {
		final By locator = By.xpath(xpathUnsentIndicatorByText.apply(text));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean isImageExists() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(idDialogImages));
	}

	public Optional<BufferedImage> getLastImageInConversation()
			throws Exception {
		return getElementScreenshot(imageList.get(imageList.size() - 1));
	}

	public Optional<BufferedImage> getLastImageInFullScreen() throws Exception {
		return getElementScreenshot(fullScreenImageImage);
	}

	public void confirm() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), okButton);
		okButton.click();
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(xpathConfirmOKButton));
	}

	public void drawSketchOnImage() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				sketchImagePaintButton);
		sketchImagePaintButton.click();
	}

	public void takePhoto() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(xpathDialogTakePhotoButton));
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				takePhotoButton);
		takePhotoButton.click();
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.xpath(xpathDialogTakePhotoButton));
	}

	public void changeCamera() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.id(idDialogChangeCameraButton));
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				changeCameraButton);
		changeCameraButton.click();
		Thread.sleep(2000); // We must wait after camera switch
	}

	public boolean isConnectMessageVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				lastConversationMessage);
	}

	public boolean isConnectMessageValid(String message) {
		return lastConversationMessage.getText().toLowerCase()
				.contains(message.toLowerCase());
	}

	public Boolean isKnockIconVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idPingIcon));
	}

	public String getConnectRequestChatLabel() throws Exception {
		assert isConnectRequestChatLabelVisible();
		return connectRequestChatLabel.getText();
	}

	public boolean isConnectRequestChatLabelVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idConnectRequestChatLabel));
	}

	public String getConnectRequestChatUserName() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idConnectRequestChatUserName)) : "The username is not visible on the connection request screen";
		return connectRequestChatUserName.getText().toLowerCase();
	}

	/**
	 * Navigates back by swipe and initialize ContactListPage
	 * 
	 * @throws Exception
	 */
	public void navigateBack(int timeMilliseconds) throws Exception {
		swipeRightCoordinates(timeMilliseconds);
	}

	public boolean isHintVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idSearchHintClose));
	}

	public void closeHint() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), closeHintBtn);
		closeHintBtn.click();
	}

	public void openGallery() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), galleryBtn);
		galleryBtn.click();
	}

	public void closeFullScreenImage() throws Exception {
		// Sometimes X button is opened automatically after some timeout
		final int MAX_TRIES = 4;
		int ntry = 1;
		while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCloseImageBtn), 4)
				&& ntry <= MAX_TRIES) {
			this.tapOnCenterOfScreen();
			ntry++;
		}
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), closeImageBtn);
		closeImageBtn.click();
	}

	public OtherUserPersonalInfoPage tapConversationDetailsButton()
			throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				participantsButton);
		participantsButton.click();
		final long millisecondsStarted = System.currentTimeMillis();
		final long maxAnimationDurationMillis = 2000;
		do {
			Thread.sleep(200);
		} while (participantsButton.getLocation().getY() > 0
				&& System.currentTimeMillis() - millisecondsStarted < maxAnimationDurationMillis);
		assert participantsButton.getLocation().getY() < 0;
		return new OtherUserPersonalInfoPage(this.getLazyDriver());
	}

	public void sendFrontCameraImage() throws Exception {
		if (DriverUtils.isElementPresentAndDisplayed(getDriver(),
				participantsButton)) {
			swipeOnCursorInput();
			tapAddPictureBtn();
			try {
				this.hideKeyboard();
				log.debug("Fix for opened keyboard #1");
			} catch (WebDriverException e) {
				log.debug("No keyboard visible. Nothing to hide #1");
			}
			changeCamera();
			takePhoto();
		} else {
			this.hideKeyboard();
			tapDialogPageBottom();
			swipeOnCursorInput();
			tapAddPictureBtn();
			try {
				this.hideKeyboard();
				log.debug("Fix for opened keyboard #2");
			} catch (WebDriverException e) {
				log.debug("No keyboard visible. Nothing to hide #2");
			}
			changeCamera();
			takePhoto();
		}
		confirm();
	}

	public boolean dialogImageCompare() throws Exception {
		BufferedImage dialogImage = getElementScreenshot(image).orElseThrow(
				IllegalStateException::new);
		BufferedImage realImage = ImageUtil.readImageFromFile(CommonUtils
				.getImagesPath(CommonUtils.class) + DIALOG_IMAGE);
		double score = ImageUtil.getOverlapScore(realImage, dialogImage,
				ImageUtil.RESIZE_TO_MAX_SCORE);
		return (score >= MIN_ACCEPTABLE_IMAGE_VALUE);
	}

	private static final String TEXT_MESSAGE_PATTERN = "<android.widget.TextView[^>]*text=\"([^\"]*)\"[^>]*/>";
	private static final int TIMES_TO_SCROLL = 100;

	private String tryGetPageSourceFewTimes(int times) throws Exception {
		int tries = 0;
		WebDriverException savedException = null;
		do {
			try {
				return this.getDriver().getPageSource();
			} catch (WebDriverException e) {
				savedException = e;
				log.debug("Error while getting source code for Android. Trying again.");
				tries++;
			}
		} while (tries < times);
		throw savedException;
	}

	public boolean swipeAndCheckMessageFound(String direction, String pattern)
			throws Exception {
		Point coords = new Point(0, 0);
		Dimension elementSize = this.getDriver().manage().window().getSize();
		switch (direction) {
		case "up":
			this.getDriver().swipe(coords.x + elementSize.width / 2,
					coords.y + elementSize.height / 2,
					coords.x + elementSize.width / 2, coords.y + 120,
					DEFAULT_SWIPE_TIME);
			break;
		case "down":
			this.getDriver().swipe(coords.x + elementSize.width / 2,
					coords.y + 150, coords.x + elementSize.width / 2,
					coords.y + elementSize.height - 200, DEFAULT_SWIPE_TIME);
			break;
		default:
			throw new RuntimeException(String.format("Unknown direction '%s'",
					direction));
		}
		String source = tryGetPageSourceFewTimes(5);
		Pattern messagesPattern = Pattern.compile(TEXT_MESSAGE_PATTERN);
		Matcher messagesMatcher = messagesPattern.matcher(source);
		while (messagesMatcher.find()) {
			String message = messagesMatcher.group(1);
			Pattern messagePattern = Pattern.compile(pattern);
			Matcher messageMatcher = messagePattern.matcher(message);
			if (messageMatcher.find()) {
				return true;
			}
		}
		return false;
	}

	// FIXME: Handle situation when message is not reached
	public void swipeTillTextMessageWithPattern(String direction, String pattern)
			throws Exception {
		boolean isAddedMessage = false;
		int i = 0;
		do {
			i++;
			isAddedMessage = swipeAndCheckMessageFound(direction, pattern);
		} while (!isAddedMessage && i < TIMES_TO_SCROLL);
	}

	private static final String UUID_TEXT_MESSAGE_PATTERN = "<android.widget.TextView[^>]*text=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*/>";
	private static final String DIALOG_START_MESSAGE_PATTERN = "^(.*)\\sADDED\\s(.*)$";

	public List<MessageEntry> listAllMessages(boolean checkTime)
			throws Exception {
		this.hideKeyboard();

		String lastMessage = messagesList.get(messagesList.size() - 1)
				.getText();

		swipeTillTextMessageWithPattern("down", DIALOG_START_MESSAGE_PATTERN);

		Map<String, MessageEntry> messages = new LinkedHashMap<String, MessageEntry>();

		boolean lastMessageAppears = false;
		boolean temp = false;
		int i = 0;
		do {
			i++;
			lastMessageAppears = temp;
			Date receivedDate = new Date();
			String source = tryGetPageSourceFewTimes(5);
			Pattern pattern = Pattern.compile(UUID_TEXT_MESSAGE_PATTERN);
			Matcher matcher = pattern.matcher(source);
			while (matcher.find()) {
				if (!messages.containsKey(matcher.group(1))) {
					messages.put(matcher.group(1), new MessageEntry("text",
							matcher.group(1), receivedDate, checkTime));
				}
			}
			if (!lastMessageAppears) {
				temp = swipeAndCheckMessageFound("up", lastMessage);
			}
		} while (!lastMessageAppears && i < TIMES_TO_SCROLL);

		List<MessageEntry> listResult = new ArrayList<MessageEntry>();
		for (Map.Entry<String, MessageEntry> mess : messages.entrySet()) {
			listResult.add(mess.getValue());
		}
		return listResult;
	}

	public MessageEntry receiveMessage(String message, boolean checkTime)
			throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.xpath(xpathConversationMessageByText.apply(message)))) {
			return new MessageEntry("text", message, new Date(), checkTime);
		}
		return null;
	}

	public double checkPingIcon(String label) throws Exception {
		String path = null;
		BufferedImage pingImage = getElementScreenshot(pingIcon).orElseThrow(
				IllegalStateException::new);
		if (label.equals(PING_LABEL)) {
			path = CommonUtils.getPingIconPath(DialogPage.class);
		} else if (label.equals(HOT_PING_LABEL)) {
			path = CommonUtils.getHotPingIconPath(DialogPage.class);
		}
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		return ImageUtil.getOverlapScore(pingImage, templateImage,
				ImageUtil.RESIZE_TO_MAX_SCORE);
	}

	public boolean waitForPingMessageWithText(String expectedText)
			throws Exception {
		final By locator = By.xpath(xpathPingMessageByText.apply(expectedText));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitForPingMessageWithTextDisappears(String expectedText)
			throws Exception {
		final By locator = By.xpath(xpathPingMessageByText.apply(expectedText));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public boolean isGroupChatDialogContainsNames(List<String> names)
			throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathLastConversationMessage));
		final String convoText = lastConversationMessage.getText();
		for (String name : names) {
			if (!convoText.toLowerCase().contains(name.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	public boolean isDialogVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(idMessage));
	}

	private static final double MAX_BUTTON_STATE_OVERLAP = 0.5;

	public void tapPlayPauseBtn() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idPlayPauseMedia));
		assert DriverUtils.waitUntilElementClickable(getDriver(), playPauseBtn);
		final BufferedImage initialState = getElementScreenshot(playPauseBtn)
				.orElseThrow(
						() -> new AssertionError(
								"Failed to get a screenshot of Play/Pause button"));
		playPauseBtn.click();
		Thread.sleep(2000);
		int clickTry = 1;
		do {
			final BufferedImage currentState = getElementScreenshot(
					playPauseBtn).orElseThrow(
					() -> new AssertionError(
							"Failed to get a screenshot of Play/Pause button"));
			final double overlapScore = ImageUtil.getOverlapScore(currentState,
					initialState, ImageUtil.RESIZE_TO_MAX_SCORE);
			if (overlapScore < MAX_BUTTON_STATE_OVERLAP) {
				return;
			} else {
				playPauseBtn.click();
				Thread.sleep(2000);
			}
			clickTry++;
		} while (clickTry <= MAX_CLICK_RETRIES);
		assert (clickTry > MAX_CLICK_RETRIES) : "Media playback state has not been changed after "
				+ MAX_CLICK_RETRIES + " retries";
	}

	// NOTE: This method is required to scroll conversation to the end.
	// NOTE: Click happens on the text input area if participants button is not
	// NOTE: visible
	public void tapDialogPageBottom() throws Exception {
		this.hideKeyboard();
		this.swipeByCoordinates(1000, 50, 80, 50, 60);
		DriverUtils.waitUntilLocatorDissapears(getDriver(), By.id(idCursorBtn),
				5);
		DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCursorFrame));
		tapOnCursorFrame();
		this.hideKeyboard();
		if (!DriverUtils
				.isElementPresentAndDisplayed(getDriver(), cursorBtnImg)) {
			tapOnCursorFrame();
			this.hideKeyboard();
		}
		Thread.sleep(500); // fix for scrolling animation
	}

	public boolean waitUntilYoutubePlayButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idYoutubePlayButton));
	}

	public double getMediaBarControlIconOverlapScore(String label)
			throws Exception {
		String path = null;
		BufferedImage mediaImage = getElementScreenshot(mediaBarControl)
				.orElseThrow(IllegalStateException::new);
		if (label.equals(MEDIA_PLAY)) {
			path = CommonUtils.getMediaBarPlayIconPath(DialogPage.class);
		} else if (label.equals(MEDIA_PAUSE)) {
			path = CommonUtils.getMediaBarPauseIconPath(DialogPage.class);
		}
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		return ImageUtil.getOverlapScore(mediaImage, templateImage,
				ImageUtil.RESIZE_TO_MAX_SCORE);
	}

	public BufferedImage getMediaControlButtonScreenshot() throws Exception {
		return getElementScreenshot(playPauseBtn).orElseThrow(
				IllegalStateException::new);
	}

	public void tapPlayPauseMediaBarBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				mediaBarControl);
		mediaBarControl.click();
	}

	public boolean waitUntilMediaBarVisible(int timeoutSeconds)
			throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idMediaBarControl), timeoutSeconds);
	}

	public boolean waitUntilMissedCallMessageIsVisible(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathMissedCallMesageByText
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public String getLastMessageFromDialog() {
		return lastConversationMessage.getText();
	}

	public void swipeLeftOnCursorInput() throws Exception {
		final By fakeCursorLocator = By.id(idCursorArea);
		int ntry = 1;
		do {
			DriverUtils.swipeLeft(this.getDriver(), cursorArea,
					DEFAULT_SWIPE_TIME);
			final int currentCursorOffset = getDriver()
					.findElement(fakeCursorLocator).getLocation().getX();
			if (currentCursorOffset < getDriver().manage().window().getSize()
					.getWidth() / 2) {
				return;
			}
			log.debug(String
					.format("Failed to swipe left the text cursor. Retrying (%s of %s)...",
							ntry, MAX_SWIPE_RETRIES));
			ntry++;
			Thread.sleep(1000);
		} while (ntry <= MAX_SWIPE_RETRIES);
		throw new RuntimeException(
				String.format(
						"Failed to swipe left the text cursor on input field after %s retries!",
						MAX_SWIPE_RETRIES));
	}

	public Optional<BufferedImage> getRecentPictureScreenshot()
			throws Exception {
		return this.getElementScreenshot(image);
	}

	public Optional<BufferedImage> getPreviewPictureScreenshot()
			throws Exception {
		return this.getElementScreenshot(fullScreenImage);
	}

	public boolean isImageInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(idDialogImages));
	}

	public boolean waitForAPictureWithUnsentIndicator() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(xpathUnsentIndicatorForImage));
	}
}
