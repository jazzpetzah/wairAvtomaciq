package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.misc.MessageEntry;

public class DialogPage extends AndroidPage{

	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classEditText)
	private WebElement cursorInput;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.DialogPage.xpathCloseCursor)
	private WebElement closeCursor;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idMessage")
	private List<WebElement> messagesList;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idKnockMessage")
	private WebElement knockMessages;
	
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
	
	public void tapOnCursorInput()
	{
		cursorInput.click();
	}
	
	public void multiTapOnCursorInput() throws InterruptedException
	{
		DriverUtils.androidMultiTap(driver, cursorInput, 2,0.2);
	}
	
	public void SwipeOnCursorInput()
	{
		DriverUtils.swipeRight(driver, cursorInput, 1000);
	}
	
	public void SwipeLeftOnCursorInput()
	{
		DriverUtils.swipeLeft(driver, closeCursor, 1000);
	}
	
	public void tapAddPictureBtn(int index)
	{
		WebElement el = driver.findElement(By.xpath(AndroidLocators.CommonLocators.classNameTextView));
		el.click();
	}
	
	public void typeMessage(String message)
	{
		cursorInput.sendKeys(message + "\\n"); 
		DriverUtils.mobileTapByCoordinates(driver, backgroundOverlay);
	}

	public String getLastMessageFromDialog()

	{
		return messagesList.get(messagesList.size()-1).getText();
	}
	

	@Override
	public AndroidPage swipeUp(int time) throws Exception
	{
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

	public void waitForMessage() throws InterruptedException
	{
		for(int i = 0; i < 10 ; i++)
		{
			if(initMessageCount < messagesList.size())
			{
				break;
			}
			Thread.sleep(200);
		}
	}
	
	public boolean isImageExists()
	{
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

	public String getKnockMessageText() {
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(knockMessages));
		return knockMessages.getText().toUpperCase();
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
	
	public ArrayList<MessageEntry> listAllMessages() {
		Pattern messagesPattern = Pattern.compile("[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}");
		ArrayList<MessageEntry> listResult = new ArrayList<MessageEntry>();
		List<WebElement> elements = driver.findElements(By.id(AndroidLocators.DialogPage.idMessage));
		Date receivedDate = new Date();
		for (WebElement element: elements) {
			String messageText = element.getText();
			if (messagesPattern.matcher(messageText).matches()) {
				listResult.add(new MessageEntry("text", messageText, CommonUtils.PLATFORM_NAME_ANDROID, receivedDate));
			}
		}
		return listResult;
	}	
}
