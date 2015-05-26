package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import android.graphics.Point;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
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

	@FindBy(id = AndroidLocators.CommonLocators.idEditText)
	private WebElement cursorInput;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.Browsers.xpathNativeBrowserURLBar)
	private WebElement nativeBrowserURL;

	@FindBy(id = AndroidLocators.DialogPage.idMessage)
	private List<WebElement> messagesList;

	@FindBy(id = AndroidLocators.DialogPage.idMissedCallMesage)
	private WebElement missedCallMessage;

	@FindBy(id = AndroidLocators.DialogPage.idCursorFrame)
	private WebElement cursurFrame;

	@FindBy(id = AndroidLocators.DialogPage.idPingMessage)
	private List<WebElement> pingMessages;

	@FindBy(xpath = AndroidLocators.DialogPage.xpathLastPingMessage)
	private WebElement lastPingMessage;

	@FindBy(id = AndroidLocators.DialogPage.idPingIcon)
	private WebElement pingIcon;

	@FindBy(id = AndroidLocators.DialogPage.idDialogTakePhotoButton)
	private WebElement takePhotoButton;

	@FindBy(id = AndroidLocators.DialogPage.idDialogChangeCameraButton)
	private WebElement changeCameraButton;

	@FindBy(xpath = AndroidLocators.DialogPage.xpathConfirmOKButton)
	private WebElement okButton;

	@FindBy(id = AndroidLocators.DialogPage.idDialogImages)
	private WebElement image;

	@FindBy(id = AndroidLocators.DialogPage.idDialogImages)
	private List<WebElement> imageList;

	@FindBy(id = AndroidLocators.DialogPage.idConnectRequestDialog)
	private WebElement connectRequestDialog;

	@FindBy(id = AndroidLocators.DialogPage.idAddParticipants)
	private WebElement addParticipant;

	@FindBy(id = AndroidLocators.DialogPage.idConnectRequestMessage)
	private WebElement connectRequestMessage;

	@FindBy(id = AndroidLocators.DialogPage.idConnectRequestConnectTo)
	private WebElement connectRequestConnectTo;

	@FindBy(id = AndroidLocators.DialogPage.idDialogPageBottomFrameLayout)
	private WebElement dialogPageBottomFrameLayout;

	@FindBy(id = AndroidLocators.DialogPage.idBackgroundOverlay)
	private WebElement backgroundOverlay;

	@FindBy(id = AndroidLocators.DialogPage.idConnectRequestChatLabel)
	private WebElement connectRequestChatLabel;

	@FindBy(id = AndroidLocators.DialogPage.idConnectRequestChatUserName)
	private WebElement connectRequestChatUserName;

	@FindBy(id = AndroidLocators.CommonLocators.idGalleryBtn)
	private WebElement galleryBtn;

	@FindBy(id = AndroidLocators.CommonLocators.idSearchHintClose)
	private WebElement closeHintBtn;

	@FindBy(id = AndroidLocators.CommonLocators.idCloseImageBtn)
	private WebElement closeImageBtn;

	@FindBy(id = AndroidLocators.DialogPage.idPlayPauseMedia)
	private WebElement playPauseBtn;

	@FindBy(id = AndroidLocators.DialogPage.idYoutubePlayButton)
	private WebElement playYoutubeBtn;

	@FindBy(id = AndroidLocators.DialogPage.idMediaBarControl)
	private WebElement mediaBarControl;

	@FindBy(id = AndroidLocators.DialogPage.idAddPicture)
	private WebElement addPictureBtn;

	@FindBy(id = AndroidLocators.DialogPage.idPing)
	private WebElement pingBtn;

	@FindBy(id = AndroidLocators.DialogPage.idCallingMessage)
	private WebElement callingMessageText;

	@FindBy(id = AndroidLocators.DialogPage.idCall)
	private WebElement callBtn;

	@FindBy(id = AndroidLocators.DialogPage.idMute)
	private WebElement muteBtn;

	@FindBy(id = AndroidLocators.DialogPage.idSpeaker)
	private WebElement speakerBtn;

	@FindBy(id = AndroidLocators.DialogPage.idCancelCall)
	private WebElement cancelCallBtn;

	@FindBy(id = AndroidLocators.DialogPage.idDialogPageBottom)
	private WebElement dialogPageBottom;

	@FindBy(id = AndroidLocators.DialogPage.idNewConversationNameMessage)
	private WebElement newConversationNameMessage;

	@FindBy(xpath = AndroidLocators.DialogPage.xpathLastConversationMessage)
	private WebElement lastConversationMessage;

	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.75;
	private final String DIALOG_IMAGE = "android_dialog_sendpicture_result.png";
	private static final int DEFAULT_SWIPE_TIME = 500;

	public DialogPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void waitForCursorInputVisible() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.CommonLocators.idEditText), 5);
	}

	public void tapOnCursorInput() {
		cursorInput.click();
	}

	public void tapOnCursorFrame() {
		cursurFrame.click();
	}

	public void sendMessageInInput() throws Exception {
		this.hideKeyboard();
		cursorInput.sendKeys("\\n");
	}

	public void multiTapOnCursorInput() throws Exception {
		DriverUtils.androidMultiTap(this.getDriver(), cursorInput, 2, 0.2);
	}

	public void swipeOnCursorInput() throws Exception {
		getWait().until(ExpectedConditions.elementToBeClickable(cursorInput));
		DriverUtils.swipeRight(this.getDriver(), cursorInput,
				DEFAULT_SWIPE_TIME);
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

	public void tapCallBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), callBtn);
		callBtn.click();
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

	public double getExpectedButtonStateOverlapScore(String label)
			throws Exception {
		String path = null;
		BufferedImage callingButtonImage = null;
		if (label.equals(MUTE_BUTTON_LABEL)) {
			callingButtonImage = getElementScreenshot(muteBtn);
			path = CommonUtils.getCallingMuteButtonPath(DialogPage.class);
		} else if (label.equals(SPEAKER_BUTTON_LABEL)) {
			callingButtonImage = getElementScreenshot(speakerBtn);
			path = CommonUtils.getCallingSpeakerButtonPath(DialogPage.class);
		}
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		return ImageUtil.getOverlapScore(callingButtonImage, templateImage,
				ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
	}

	public boolean checkNoCallingOverlay() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.DialogPage.idCallingMessage), 20);
	}

	public boolean checkCallingOverlay() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(callingMessageText);
	}

	public void typeAndSendMessage(String message) throws Exception {
		cursorInput.sendKeys(message);
		this.pressEnter();
		this.hideKeyboard();
	}

	public void typeMessage(String message) throws Exception {
		cursorInput.sendKeys(message);
		try {
			this.pressEnter();
		} catch (WebDriverException ex) {
			// don't fail if Enter is already pressed
		}
	}

	public void pressKeyboardSendButton() throws Exception {
		int sendKeyXPosPercentage = 95;
		int sendKeyYPosPercentage = 95;
		DriverUtils.genericTap(this.getDriver(), 200, sendKeyXPosPercentage,
				sendKeyYPosPercentage);
	}

	public void clickLastImageFromDialog() {
		imageList.get(imageList.size() - 1).click();
	}

	public String getChangedGroupNameMessage() {
		return newConversationNameMessage.getText();
	}

	@Override
	public AndroidPage swipeUp(int time) throws Exception {
		dialogsPagesSwipeUp(time);
		return returnBySwipe(SwipeDirection.UP);
	}

	@Override
	public AndroidPage swipeDown(int time) throws Exception {
		dialogsPagesSwipeDown(time);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		switch (direction) {
		case UP: {
			return new OtherUserPersonalInfoPage(this.getLazyDriver());
		}
		case RIGHT: {
			return new ContactListPage(this.getLazyDriver());
		}
		default:
			return null;
		}
	}

	private static final int MSG_DELIVERY_TIMEOUT_SECONDS = 4;

	public void waitForMessage(String text) throws Exception {
		final By locator = By
				.xpath(AndroidLocators.DialogPage.xpathConversationMessageByText
						.apply(text));
		if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator,
				MSG_DELIVERY_TIMEOUT_SECONDS)) {
			throw new RuntimeException(
					String.format(
							"Message '%s' has not been displayed after '%s' seconds timeout",
							text, MSG_DELIVERY_TIMEOUT_SECONDS));
		}
	}

	public boolean isImageExists() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AndroidLocators.DialogPage.idDialogImages));
	}

	public void confirm() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), okButton);
		okButton.click();
	}

	public void takePhoto() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.id(AndroidLocators.DialogPage.idDialogTakePhotoButton));
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				takePhotoButton);
		takePhotoButton.click();
	}

	public void changeCamera() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.id(AndroidLocators.DialogPage.idDialogChangeCameraButton));
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				changeCameraButton);
		changeCameraButton.click();
	}

	public boolean isConnectMessageVisible() {
		return DriverUtils
				.isElementPresentAndDisplayed(lastConversationMessage);
	}

	public boolean isConnectMessageValid(String message) {
		return lastConversationMessage.getText().toLowerCase()
				.contains(message.toLowerCase());
	}

	public Boolean isKnockIconVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.DialogPage.idPingIcon));
	}

	public String getConnectRequestChatLabel() throws Exception {
		if (isConnectRequestChatLabelVisible()) {
			this.getWait().until(
					ExpectedConditions.visibilityOf(connectRequestChatLabel));
			return connectRequestChatLabel.getText().toLowerCase().trim();
		} else {
			return "CHAT HEAD NOT FOUND";
		}
	}

	public boolean isConnectRequestChatLabelVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.DialogPage.idConnectRequestChatLabel));
	}

	public String getConnectRequestChatUserName() {
		return connectRequestChatUserName.getText().toLowerCase();
	}

	public ContactListPage navigateBack() throws Exception {
		swipeRightCoordinates(1000);
		return new ContactListPage(this.getLazyDriver());
	}

	public ContactListPage navigateBack(int time) throws Exception {
		swipeRightCoordinates(time);
		return new ContactListPage(this.getLazyDriver());
	}

	public boolean isHintVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.CommonLocators.idSearchHintClose));
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
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), closeImageBtn);
		closeImageBtn.click();
	}

	public OtherUserPersonalInfoPage tapConversationDetailsButton()
			throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				addParticipant);
		addParticipant.click();
		return new OtherUserPersonalInfoPage(this.getLazyDriver());
	}

	public void sendFrontCameraImage() throws Exception {
		if (DriverUtils.isElementPresentAndDisplayed(addParticipant)) {
			swipeOnCursorInput();
			tapAddPictureBtn();
			try {
				this.hideKeyboard();
				swipeOnCursorInput();
				tapAddPictureBtn();
				log.debug("Fix for opened keyboard #1");
			} catch (WebDriverException e) {
				log.debug("No keyboard visible. Nothing to hide #1");
			}
			changeCamera();
			takePhoto();
		} else {
			cursurFrame.click();
			Thread.sleep(1000); // fix for scrolling animation
			swipeOnCursorInput();
			tapAddPictureBtn();
			try {
				this.hideKeyboard();
				swipeOnCursorInput();
				tapAddPictureBtn();
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
		BufferedImage dialogImage = getElementScreenshot(image);
		BufferedImage realImage = ImageUtil.readImageFromFile(CommonUtils
				.getImagesPath(CommonUtils.class) + DIALOG_IMAGE);
		double score = ImageUtil.getOverlapScore(realImage, dialogImage,
				ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
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
		if (DriverUtils
				.waitUntilLocatorAppears(
						getDriver(),
						By.xpath(AndroidLocators.DialogPage.xpathConversationMessageByText
								.apply(message)))) {
			return new MessageEntry("text", message, new Date(), checkTime);
		}
		return null;
	}

	public double checkPingIcon(String label) throws Exception {
		String path = null;
		BufferedImage pingImage = getElementScreenshot(pingIcon);
		if (label.equals(PING_LABEL)) {
			path = CommonUtils.getPingIconPath(DialogPage.class);
		} else if (label.equals(HOT_PING_LABEL)) {
			path = CommonUtils.getHotPingIconPath(DialogPage.class);
		}
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		return ImageUtil.getOverlapScore(pingImage, templateImage,
				ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
	}

	public String getLastPingText() {
		return lastPingMessage.getText();
	}

	public boolean isGroupChatDialogContainsNames(List<String> names) throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.xpath(AndroidLocators.DialogPage.xpathLastConversationMessage));
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
				By.id(AndroidLocators.DialogPage.idMessage));
	}

	public void tapPlayPauseBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), playPauseBtn);
		playPauseBtn.click();
	}

	public void tapDialogPageBottom() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.DialogPage.idDialogPageBottom));
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				dialogPageBottom);
		dialogPageBottom.click();
	}

	public void tapYouTubePlay() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				playYoutubeBtn);
		playYoutubeBtn.click();
	}

	public double getMediaBarControlIconOverlapScore(String label)
			throws Exception {
		String path = null;
		BufferedImage mediaImage = getElementScreenshot(mediaBarControl);
		if (label.equals(MEDIA_PLAY)) {
			path = CommonUtils.getMediaBarPlayIconPath(DialogPage.class);
		} else if (label.equals(MEDIA_PAUSE)) {
			path = CommonUtils.getMediaBarPauseIconPath(DialogPage.class);
		}
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		return ImageUtil.getOverlapScore(mediaImage, templateImage,
				ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
	}

	public double getMediaControlIconOverlapScore(String label)
			throws Exception {
		getWait().until(ExpectedConditions.elementToBeClickable(playPauseBtn));
		String path = null;
		BufferedImage mediaImage = getElementScreenshot(playPauseBtn);
		if (label.equals(MEDIA_PLAY)) {
			path = CommonUtils.getMediaPlayIconPath(DialogPage.class);
		} else if (label.equals(MEDIA_PAUSE)) {
			path = CommonUtils.getMediaPauseIconPath(DialogPage.class);
		}
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		return ImageUtil.getOverlapScore(mediaImage, templateImage,
				ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
	}

	public void tapPlayPauseMediaBarBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				mediaBarControl);
		mediaBarControl.click();
	}

	public String getMissedCallMessage() throws Exception {
		return missedCallMessage.getText();
	}

	public boolean isNativeBrowserURLVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(AndroidLocators.Browsers.xpathNativeBrowserURLBar));
	}

	public String getLastMessageFromDialog() {
		return lastConversationMessage.getText();
	}
}
