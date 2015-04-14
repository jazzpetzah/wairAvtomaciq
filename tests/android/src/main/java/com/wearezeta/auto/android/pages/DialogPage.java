package com.wearezeta.auto.android.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;

public class DialogPage extends AndroidPage {
	private static final Logger log = ZetaLogger.getLog(DialogPage.class
			.getSimpleName());
	public static final String MEDIA_PLAY = "PLAY";
	public static final String MEDIA_PAUSE = "PAUSE";
	public static final String PING_LABEL = "PINGED";
	public static final String HOT_PING_LABEL = "PINGED AGAIN";
	public static final String I_LEFT_CHAT_MESSAGE = "YOU HAVE LEFT";

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classEditText)
	private WebElement cursorInput;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.DialogPage.xpathCloseCursor)
	private WebElement closeCursor;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idMessage")
	private List<WebElement> messagesList;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idMessage")
	private WebElement messageInList;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idMissedCallMesage")
	private WebElement missedCallMessage;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idCursorFrame")
	private WebElement cursurFrame;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idKnockMessage")
	private WebElement knockMessage;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idKnockAction")
	private WebElement knockAction;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idKnockIcon")
	private WebElement knockIcon;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogTakePhotoButton")
	private WebElement takePhotoButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogChangeCameraButton")
	private WebElement changeCameraButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConfirmButton")
	private WebElement okButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogImages")
	private WebElement image;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogImages")
	private List<WebElement> imageList;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConnectRequestDialog")
	private WebElement connectRequestDialog;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idAddParticipants")
	private WebElement addParticipant;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idMessage")
	private WebElement conversationMessage;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConnectRequestMessage")
	private WebElement connectRequestMessage;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConnectRequestConnectTo")
	private WebElement connectRequestConnectTo;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogPageBottomFrameLayout")
	private WebElement dialogPageBottomFrameLayout;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idBackgroundOverlay")
	private WebElement backgroundOverlay;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConnectRequestChatLabel")
	private WebElement connectRequestChatLabel;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConnectRequestChatUserName")
	private WebElement connectRequestChatUserName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idGalleryBtn")
	private WebElement galleryBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idSearchHintClose")
	private WebElement closeHintBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idCloseImageBtn")
	private WebElement closeImageBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idPlayPauseMedia")
	private WebElement playPauseBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idMediaBarControl")
	private WebElement mediaBarControl;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idAddPicture")
	private WebElement addPictureBtn;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idPing")
	private WebElement pingBtn;

	@AndroidFindBy(xpath = AndroidLocators.OtherUserPersonalInfoPage.xpathGroupChatInfoLinearLayout)
	private List<WebElement> linearLayout;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogPageBottom")
	private WebElement dialogPageBottom;

	private int initMessageCount = 0;
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.75;
	private final String DIALOG_IMAGE = "android_dialog_sendpicture_result.png";

	public DialogPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void waitForCursorInputVisible() throws Exception {
		refreshUITree();
		int counter = 0;
		while (!isVisible(cursorInput)) {
			Thread.sleep(500);
			counter++;
			if (counter == 10) {
				break;
			}
		}
		if( isVisible(messageInList)) {
			initMessageCount = messagesList.size();
		}
	}

	public void tapOnCursorInput() {
		cursorInput.click();
	}

	public void tapOnCursorFrame() {
		cursurFrame.click();
	}

	public void tapOnCenterOfScreen() {
		DriverUtils.genericTap(this.getDriver());
	}

	public void multiTapOnCursorInput() throws InterruptedException {
		DriverUtils.androidMultiTap(this.getDriver(), cursorInput, 2, 0.2);
	}

	public void SwipeOnCursorInput() {
		getWait().until(ExpectedConditions.elementToBeClickable(cursorInput));
		DriverUtils.swipeRight(this.getDriver(), cursorInput, 1000);
	}

	public void SwipeLeftOnCursorInput() {
		DriverUtils.swipeLeft(this.getDriver(), closeCursor, 1000);
	}

	public void tapAddPictureBtn() {
		refreshUITree();
		this.getWait().until(ExpectedConditions.visibilityOf(addPictureBtn));
		addPictureBtn.click();
	}

	public void tapPingBtn() throws InterruptedException {
		refreshUITree();
		pingBtn.click();
		Thread.sleep(1000);
	}

	public void typeMessage(String message) {
		refreshUITree();
		cursorInput.sendKeys(message + "\\n");
		// DriverUtils.mobileTapByCoordinates(driver, backgroundOverlay);
		this.getDriver().hideKeyboard();
	}

	public String getLastMessageFromDialog() {
		return messagesList.get(messagesList.size() - 1).getText();
	}

	public void clickLastImageFromDialog() {
		imageList.get(imageList.size() - 1).click();
	}

	@Override
	public AndroidPage swipeUp(int time) throws Exception {
		dialogsPagesSwipeUp(time);// TODO workaround
		Thread.sleep(1000);
		return returnBySwipe(SwipeDirection.UP);
	}

	@Override
	public AndroidPage swipeDown(int time) throws Exception {
		dialogsPagesSwipeDown(time);// TODO workaround
		Thread.sleep(1000);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		AndroidPage page = null;
		switch (direction) {
		case DOWN: {
			page = this;
			break;
		}
		case UP: {
			page = new OtherUserPersonalInfoPage(this.getDriver(),
					this.getWait());
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			page = new ContactListPage(this.getDriver(), this.getWait());
			break;
		}
		}
		return page;
	}

	public void waitForMessage() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			if (initMessageCount < messagesList.size()) {
				break;
			}
			Thread.sleep(200);
		}
	}

	public boolean isImageExists() throws Exception {
		refreshUITree();// TODO workaround
		return DriverUtils.waitUntilElementAppears(driver,
				AndroidLocators.DialogPage.getByForDialogPageImage());
	}

	public void confirm() {
		refreshUITree();// TODO workaround
		okButton.click();
	}

	public void takePhoto() {
		refreshUITree();// TODO workaround
		takePhotoButton.click();
	}

	public void changeCamera() throws Exception {
		refreshUITree();// TODO workaround
		if (DriverUtils.isElementDisplayed(this.getDriver(),
				By.id(AndroidLocators.DialogPage.idDialogChangeCameraButton))) {
			changeCameraButton.click();
		}
	}

	public boolean isConnectMessageVisible() {
		return conversationMessage.isDisplayed();
	}

	public boolean isConnectMessageValid(String message) {

		return conversationMessage.getText().toLowerCase()
				.contains(message.toLowerCase());
	}

	public Boolean isKnockIconVisible() {
		refreshUITree();
		this.getWait().until(ExpectedConditions.visibilityOf(knockIcon));
		return knockIcon.isDisplayed();
	}

	public String getConnectRequestChatLabel() throws Exception {
		if (isConnectRequestChatLabelVisible()) {
			refreshUITree();
			this.getWait().until(ExpectedConditions.visibilityOf(connectRequestChatLabel));
			return connectRequestChatLabel.getText().toLowerCase().trim();
		} else {
			return "CHAT HEAD NOT FOUND";
		}
	}

	public boolean isConnectRequestChatLabelVisible() throws Exception {
		refreshUITree();
		this.getWait().until(
				ExpectedConditions.visibilityOf(connectRequestChatLabel));
		return isVisible(connectRequestChatLabel);
	}

	public String getConnectRequestChatUserName() {

		return connectRequestChatUserName.getText().toLowerCase();
	}

	public ContactListPage navigateBack() throws Exception {
		swipeRightCoordinates(1000);
		return new ContactListPage(this.getDriver(), this.getWait());
	}

	public boolean isHintVisible() throws InterruptedException, IOException {
		refreshUITree();// TODO workaround
		try {
			this.getWait().until(
					ExpectedConditions.elementToBeClickable(closeHintBtn));
		} catch (NoSuchElementException e) {
			return false;
		}
		return closeHintBtn.isEnabled();
	}

	public void closeHint() {
		closeHintBtn.click();
	}

	public void openGallery() {
		refreshUITree();
		galleryBtn.click();
	}

	public void closeFullScreenImage() {
		refreshUITree();
		closeImageBtn.click();
	}
	
	public void tapConversationDetailsButton() {
		addParticipant.click();
	}

	public void sendFrontCameraImage() throws Exception {
		if (isVisible(addParticipant)) {
			cursorInput.click();
			navigateBack();
			SwipeOnCursorInput();
			tapAddPictureBtn();
			changeCamera();
			Thread.sleep(1000);
			takePhoto();
		} else {
			cursurFrame.click();
			Thread.sleep(500);
			SwipeOnCursorInput();
			tapAddPictureBtn();
			changeCamera();
			takePhoto();
		}
		Thread.sleep(1000);
		confirm();
	}

	public boolean dialogImageCompare() throws Exception {
		boolean flag = false;
		BufferedImage dialogImage = getElementScreenshot(image);
		BufferedImage realImage = ImageUtil.readImageFromFile(CommonUtils
				.getImagesPath(CommonUtils.class) + DIALOG_IMAGE);
		double score = ImageUtil.getOverlapScore(realImage, dialogImage,
				ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
		if (score >= MIN_ACCEPTABLE_IMAGE_VALUE) {
			flag = true;
		}

		return flag;
	}

	private static final String TEXT_MESSAGE_PATTERN = "<android.widget.TextView[^>]*text=\"([^\"]*)\"[^>]*/>";
	private static final int TIMES_TO_SCROLL = 100;

	private String tryGetPageSourceFewTimes(int times) {
		String source = null;
		int tries = 0;
		boolean isPageSourceRetrieved = true;
		do {
			tries++;
			try {
				source = driver.getPageSource();
			} catch (WebDriverException e) {
				log.debug("Error while getting source code for Android. Trying again.");
				isPageSourceRetrieved = false;
			}
		} while (!isPageSourceRetrieved && tries < times);
		return source;
	}

	public boolean swipeAndCheckMessageFound(String direction, String pattern) {
		boolean result = false;

		Point coords = new Point(0, 0);
		Dimension elementSize = driver.manage().window().getSize();
		switch (direction) {
		case "up":
			this.getDriver().swipe(coords.x + elementSize.width / 2,
					coords.y + elementSize.height / 2,
					coords.x + elementSize.width / 2, coords.y + 120, 1000);
			break;
		case "down":
			this.getDriver().swipe(coords.x + elementSize.width / 2,
					coords.y + 150, coords.x + elementSize.width / 2,
					coords.y + elementSize.height - 200, 1000);
			break;
		default:
			log.fatal("Unknown direction");
		}
		String source = tryGetPageSourceFewTimes(5);
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

	public void swipeTillTextMessageWithPattern(String direction, String pattern) {
		boolean isAddedMessage = false;
		int i = 0;
		do {
			i++;
			isAddedMessage = swipeAndCheckMessageFound(direction, pattern);
		} while (!isAddedMessage && i < TIMES_TO_SCROLL);
	}

	private static final String UUID_TEXT_MESSAGE_PATTERN = "<android.widget.TextView[^>]*text=\"([a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12})\"[^>]*/>";
	private static final String DIALOG_START_MESSAGE_PATTERN = "^(.*)\\sADDED\\s(.*)$";

	public ArrayList<MessageEntry> listAllMessages(boolean checkTime) {
		try {
			this.getDriver().hideKeyboard();
		} catch (WebDriverException e) {
			log.debug("No keyboard visible. Nothing to hide.");
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
			String source = tryGetPageSourceFewTimes(5);
			Pattern pattern = Pattern.compile(UUID_TEXT_MESSAGE_PATTERN);
			Matcher matcher = pattern.matcher(source);
			while (matcher.find()) {
				if (messages.get(matcher.group(1)) == null) {
					messages.put(matcher.group(1), new MessageEntry("text",
							matcher.group(1), receivedDate, checkTime));
				}
			}
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

	public MessageEntry receiveMessage(String message, boolean checkTime) {
		WebElement messageElement = driver
				.findElement(By.xpath(String.format(
						AndroidLocators.DialogPage.xpathFormatSpecificMessage,
						message)));
		if (messageElement != null) {
			return new MessageEntry("text", message, new Date(), checkTime);
		}
		return null;
	}

	public double checkPingIcon(String label) throws Exception {
		refreshUITree();
		String path = null;
		BufferedImage pingImage = getElementScreenshot(knockIcon);
		if (label.equals(PING_LABEL)) {
			path = CommonUtils.getPingIconPath(DialogPage.class);
		} else if (label.equals(HOT_PING_LABEL)) {
			path = CommonUtils.getHotPingIconPath(DialogPage.class);
		}
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		return ImageUtil.getOverlapScore(pingImage, templateImage);
	}

	public String getKnockText() {
		refreshUITree();
		return knockMessage.getText() + " " + knockAction.getText();
	}

	public Boolean isKnockText(String message, String action) {
		Boolean flag = false;
		refreshUITree();
		List<WebElement> messageElement = driver
				.findElements(By.xpath(String.format(
						AndroidLocators.DialogPage.xpathMessage, message.trim())));
		List<WebElement> actionElement = driver
				.findElements(By.xpath(String.format(
						AndroidLocators.DialogPage.xpathMessage, action.trim())));
		if (!messageElement.isEmpty() && !actionElement.isEmpty()) {
			flag = true;
		}
		return flag;
	}

	public boolean isMessageExists(String messageText) {
		boolean flag = false;
		refreshUITree();
		for (WebElement element : messagesList) {
			String text = element.getText();
			if (text.equals(messageText)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public boolean isGroupChatDialogContainsNames(List<String> names) {
		final String convoText = conversationMessage.getText();
		for (String name : names) {
			if (!convoText.toLowerCase().contains(name.toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	public boolean isDialogVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.id(AndroidLocators.DialogPage.idMessage));
	}

	public void tapPlayPauseBtn() {
		refreshUITree();
		playPauseBtn.click();
	}

	public void tapDialogPageBottom() throws NumberFormatException,
			Exception {
		refreshUITree();
		if (!isVisible(addParticipant)) {
			dialogPageBottom.click();
		}
	}

	public double checkMediaBarControlIcon(String label) throws Exception {
		refreshUITree();
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

	public double checkMediaControlIcon(String label) throws Exception {
		refreshUITree();
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

	public void tapPlayPauseMediaBarBtn() {
		refreshUITree();
		mediaBarControl.click();

	}

	public String getMissedCallMessage() {
		refreshUITree();
		return missedCallMessage.getText();
	}
}
