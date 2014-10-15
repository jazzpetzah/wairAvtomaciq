package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;

public class DialogPage extends AndroidPage{
	private static final Logger log = ZetaLogger.getLog(DialogPage.class.getSimpleName());

	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classEditText)
	private WebElement cursorInput;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.DialogPage.xpathCloseCursor)
	private WebElement closeCursor;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idMessage")
	private List<WebElement> messagesList;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idCursorFrame")
	private WebElement cursurFrame;
	
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idKnockIcon")
	private WebElement knockIcon;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogTakePhotoButton")
	private WebElement takePhotoButton;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogChangeCameraButton")
	private WebElement changeCameraButton;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConfirmButton")
	private WebElement okButton;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogImages")
	private WebElement image;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConnectRequestDialog")
	private WebElement connectRequestDialog;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idMessage")
	private WebElement conversationMessage;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConnectRequestMessage")
	private WebElement connectRequestMessage;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConnectRequestConnectTo")
	private WebElement connectRequestConnectTo;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogPageBottomFrameLayout")
	private WebElement dialogPageBottomFrameLayout;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idBackgroundOverlay")
	private WebElement backgroundOverlay;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConnectRequestChatLabel")
	private WebElement connectRequestChatLabel;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConnectRequestChatUserName")
	private WebElement connectRequestChatUserName;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idGalleryBtn")
	private WebElement galleryBtn;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idSearchHintClose")
	private WebElement closeHintBtn;
	
	@FindBy(how = How.XPATH, using = AndroidLocators.DialogPage.xpathAddPicture)
	private WebElement addPictureBtn;
	
	@FindBy(how = How.XPATH, using = AndroidLocators.DialogPage.xpathPing)
	private WebElement pingBtn;
	
	private String url;
	private String path;
	private int initMessageCount;
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.95;
	private final String DIALOG_IMAGE = "android_dialog_sendpicture_result.png";
	
	public DialogPage(String URL, String path) throws Exception {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	public void waitForCursorInputVisible(){
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(cursorInput));
		initMessageCount = messagesList.size();
	}
	
	public void tapOnCursorInput() {
		cursorInput.click();
	}
	
	public void tapOnCursorFrame() {
		cursurFrame.click();
	}
	public void multiTapOnCursorInput() throws InterruptedException {
		DriverUtils.androidMultiTap(driver, cursorInput, 2,0.2);
	}
	
	public void SwipeOnCursorInput() {
		DriverUtils.swipeRight(driver, cursorInput, 1000);
	}
	
	public void SwipeLeftOnCursorInput() {
		DriverUtils.swipeLeft(driver, closeCursor, 1000);
	}
	
	public void tapAddPictureBtn() {
		refreshUITree();
		addPictureBtn.click();
	}
	
	public void tapPingBtn() {

		pingBtn.click();
	}
	
	public void typeMessage(String message) {
		refreshUITree();
		cursorInput.sendKeys(message + "\\n"); 
		DriverUtils.mobileTapByCoordinates(driver, backgroundOverlay);
	}

	public String getLastMessageFromDialog() {
		return messagesList.get(messagesList.size()-1).getText();
	}
	

	@Override
	public AndroidPage swipeUp(int time) throws Exception {
		dialogsPagesSwipeUp(time);//TODO workaround
		return returnBySwipe(SwipeDirection.UP);
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		AndroidPage page = null;
		switch (direction){
			case DOWN:
			{
				break;
			}
			case UP:
			{
				page = new OtherUserPersonalInfoPage(url, path);
				break;
			}
			case LEFT:
			{
				break;
			}
			case RIGHT:
			{
				page = new ContactListPage(url, path);
				break;
			}
		}	
		return page;
	}

	public void waitForMessage() throws InterruptedException {
		for(int i = 0; i < 10 ; i++) {
			if(initMessageCount < messagesList.size()) {
				break;
			}
			Thread.sleep(200);
		}
	}
	
	public boolean isImageExists() {
		refreshUITree();//TODO workaround
		return DriverUtils.waitUntilElementAppears(driver,By.id(AndroidLocators.DialogPage.idDialogImages));
	}

	public void confirm() {
		refreshUITree();//TODO workaround
		okButton.click();
	}

	public void takePhoto() {
		refreshUITree();//TODO workaround
		takePhotoButton.click();
	}
	
	public void changeCamera() {
		refreshUITree();//TODO workaround
		changeCameraButton.click();
	}

	public boolean isConnectMessageVisible() {
		return conversationMessage.isDisplayed();
	}
	
	public boolean isConnectMessageValid(String message) {
		
		return conversationMessage.getText().toLowerCase().contains(message.toLowerCase());
	}

	public Boolean isKnockIconVisible()
	{
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(knockIcon));
		return knockIcon.isDisplayed();
	}
	
	public String getConnectRequestChatLabel() {
		refreshUITree();
		return connectRequestChatLabel.getText().toLowerCase().trim();
	}

	public String getConnectRequestChatUserName() {

		return connectRequestChatUserName.getText().toLowerCase();
	}
	
	public ContactListPage navigateBack() throws Exception{
		driver.navigate().back();
		return new ContactListPage(url, path);
	}

	public boolean isHintVisible() throws InterruptedException, IOException {
		refreshUITree();//TODO workaround
		try {
			wait.until(ExpectedConditions.elementToBeClickable(closeHintBtn));
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
	public void sendFrontCameraImage() throws InterruptedException{
		SwipeOnCursorInput();
		tapAddPictureBtn();
		changeCamera();
		Thread.sleep(1000);
		takePhoto();
		Thread.sleep(1000);
		confirm();
	}
	public boolean dialogImageCompare() throws IOException
	{
		boolean flag = false;
		BufferedImage dialogImage = getElementScreenshot(image);
		BufferedImage realImage =  ImageUtil.readImageFromFile(CommonUtils.getImagesPath(CommonUtils.class) + DIALOG_IMAGE);
		double score = ImageUtil.getOverlapScore(realImage, dialogImage);
		if (score >= MIN_ACCEPTABLE_IMAGE_VALUE) {
			flag = true;
		}
		
		return flag;
	}
	
	private static final String TEXT_MESSAGE_PATTERN = "<android.widget.TextView[^>]*text=\"([^\"]*)\"[^>]*/>";
	private static final int TIMES_TO_SCROLL = 100;
	
	public boolean swipeAndCheckMessageFound(String direction, String pattern) {
		boolean result = false;
		
		Point coords = new Point(0, 0);
		Dimension elementSize = driver.manage().window().getSize();
		switch(direction) {
		case "up":
			driver.swipe(
					coords.x + elementSize.width / 2, coords.y + elementSize.height/2,
					coords.x + elementSize.width / 2, coords.y + 120,
					1000);
			break;
		case "down":
			driver.swipe(
					coords.x + elementSize.width / 2, coords.y + 150,
					coords.x + elementSize.width / 2, coords.y + elementSize.height - 200,
					1000);
			break;
		default:
			log.fatal("Unknown direction");
		}
		String source = driver.getPageSource();
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
	public ArrayList<MessageEntry> listAllMessages() {
		try {
			driver.hideKeyboard();
		} catch (WebDriverException e) { }
		
		String lastMessage = messagesList.get(messagesList.size()-1).getText();
		
		swipeTillTextMessageWithPattern("down", DIALOG_START_MESSAGE_PATTERN);

		LinkedHashMap<String, MessageEntry> messages = new LinkedHashMap<String, MessageEntry>();
		
		boolean lastMessageAppears = false;
		boolean temp = false;
		int i = 0;
		do {
			i++;
			lastMessageAppears = temp;
			Date receivedDate = new Date();
			boolean isPageSourceRetrieved = true;
			int tries = 0;
			String source = "";
			do {
				tries++;
				try {
					source = driver.getPageSource();
				} catch (WebDriverException e) {
					log.debug("Error while getting source code for Android. Trying again.");
					isPageSourceRetrieved = false;
				}
			} while (!isPageSourceRetrieved && tries < 5);
			Pattern pattern = Pattern.compile(UUID_TEXT_MESSAGE_PATTERN);
			Matcher matcher = pattern.matcher(source);
			while (matcher.find()) {
				if (messages.get(matcher.group(1)) == null) {
					messages.put(matcher.group(1), new MessageEntry("text", matcher.group(1), receivedDate));
				}
			}
			driver.getPageSource();
			if (!lastMessageAppears) {
				temp = swipeAndCheckMessageFound("up", lastMessage);
			}
		} while (!lastMessageAppears && i < TIMES_TO_SCROLL);
		

		ArrayList<MessageEntry> listResult = new ArrayList<MessageEntry>();
		
		for (Map.Entry<String, MessageEntry> mess: messages.entrySet()) {
			listResult.add(mess.getValue());
		}
		return listResult;
	}
	
	public MessageEntry receiveMessage(String message) {
		WebElement messageElement = driver.findElement(By.xpath(String.format(AndroidLocators.DialogPage.xpathFormatSpecificMessage, message)));
		if (messageElement != null) {
			return new MessageEntry("text", message, new Date());
		}
		return null;
	}
}
