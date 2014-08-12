package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.*;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.tools.IOSKeyboard;

public class DialogPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMainWindow)
	private WebElement dialogWindow;
	
//	@FindBy(how = How.XPATH, using = IOSLocators.xpathCursorInput)
//	private WebElement cursorInput;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConversationCursorInput)
	private WebElement conversationInput;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameTextInput)
	private WebElement textInput;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameDialogMessages)
	private List<WebElement> messagesList;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameConnectMessageLabel)
	private WebElement connectMessageLabel;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePendingButton)
	private WebElement pendingButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathHelloCellFormat)
	private WebElement helloCell;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathHeyCellFormat)
	private WebElement heyCell;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameAddPictureButton)
	private WebElement addPictureButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathOtherConversationCellFormat)
	private WebElement imageCell;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameMediaContainer)
	private WebElement mediaContainer;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathMediaConversationCell)
	private WebElement mediaLinkCell;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarPlayPauseButton)
	private WebElement mediabarPlayPauseButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathConversationPage)
	private WebElement conversationPage;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarCloseButton)
	private WebElement mediabarStopCloseButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarTitle)
	private WebElement mediabarBarTitle;
	
	
	private String url;
	private String path;
	
	public DialogPage(String URL, String path) throws IOException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}

	public void waitForCursorInputVisible(){
		wait.until(ExpectedConditions.visibilityOf(conversationInput));
	}
	
	public void tapOnCursorInput(){
		conversationInput.click();
	}
	
	public void multiTapOnCursorInput() throws InterruptedException{
		
		DriverUtils.iOSMultiTap(driver, conversationInput, 3);
	}
	
	public void typeMessage(String message) throws InterruptedException
	{
		conversationInput.sendKeys(message);
	}
	
	public void sendMessage(String returnKey) throws InterruptedException{
		IOSKeyboard keyboard = IOSKeyboard.getInstance();
		keyboard.typeString(returnKey, driver);
	}
		
	public void ScrollToLastMessage(){
		//DriverUtils.scrollToElement(driver, messagesList.get(messagesList.size()-1));
	}

	public String getLastMessageFromDialog()
	{
		return GetLastMessage(messagesList);
	}
		
	public boolean isConnectMessageValid(String username){
		return getConnectMessageLabel().equals("Connect to " + username);
	}
	
	public boolean isPendingButtonVisible(){
		return pendingButton.isDisplayed();
	}
	
	private String GetHelloCell(List<WebElement> chatList) {
		String helloCellText = helloCell.getAttribute("name");  
		return helloCellText;
	}

	public String getHelloCellFromDialog()
	{
		return GetHelloCell(messagesList);
	}
	
	private String GetHeyCell(List<WebElement> chatList) {
		String heyCellText = heyCell.getAttribute("name");  //I dont get HEY FROM PIOTR here
		return heyCellText;
	}

	public String getHeyCellFromDialog()
	{
		return GetHeyCell(messagesList);
	}
	
	public void swipeInputCursor() throws IOException, InterruptedException{
		DriverUtils.swipeRight(driver, conversationInput, 700);
	}
	
	public CameraRollPage pressAddPictureButton() throws IOException{
		
		CameraRollPage page;
		page = new CameraRollPage(url, path);
		addPictureButton.click();
		
		return page;
	}

	
	private String GetImageCell(List<WebElement> chatList) {
		String lastMessage = imageCell.getAttribute("name");
		return lastMessage;
	}

	public String getImageCellFromDialog()
	{
		return GetImageCell(messagesList);
	}
	
	public void startMediaContent(){
		mediaLinkCell.click();
	}
	
	public DialogPage scrollDownTilMediaBarAppears() throws Exception{
		DialogPage page = null;
		int count = 0;
		boolean buttonIsShown = mediabarPlayPauseButton.isDisplayed();
		while(!(buttonIsShown) & (count<5)){
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true){
				DriverUtils.swipeDown(driver, conversationPage, 500);
				page = this;
			}
			else {
				swipeDownSimulator();
				page = this;
			}
			buttonIsShown = mediabarPlayPauseButton.isDisplayed();
			count++;
		}
		
		Assert.assertTrue(mediabarPlayPauseButton.isDisplayed());
		return page;
	}

	public void pauseMediaContent(){
		mediabarPlayPauseButton.click();
	}
	
	public void playMediaContent(){
		mediabarPlayPauseButton.click();
	}
	
	public void stopMediaContent(){
		mediabarStopCloseButton.click();
	}
	
	public String getMediaState(){
		
		String mediaState = mediabarBarTitle.getAttribute("value");
		return mediaState;
	}
	
	public void tapOnMediaBar(){
		mediabarBarTitle.click();
	}
	
	@Override
	public IOSPage swipeUp(int time) throws IOException
	{
		WebElement element =  driver.findElement(By.name(IOSLocators.nameMainWindow));
		
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		driver.swipe(coords.x + elementSize.width / 2, coords.y + elementSize.height - 170, coords.x + elementSize.width / 2, coords.y + 40, time);
		return returnBySwipe(SwipeDirection.UP);
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		IOSPage page = null;
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
				page = new OtherUserPersonalInfoPage(url, path);
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
	
	public boolean isMediaContainerVisible(){
		return mediaLinkCell.isDisplayed();
	}
	
	public VideoPlayerPage clickOnVideoContainerFirstTime() throws IOException{
		VideoPlayerPage page = null;
		mediaContainer.click();
		page = new VideoPlayerPage(url, path);
		return page;
	}
	
	public void tapDialogWindow(){
		driver.tap(1, 1, 1, 500);
	}
	
	private String getConnectMessageLabel(){
		return connectMessageLabel.getText();
	}
	
	private String GetLastMessage(List<WebElement> chatList) {
		String lastMessageXPath = String.format(IOSLocators.xpathLastMessageFormat, chatList.size());
		WebElement el = driver.findElementByXPath(lastMessageXPath);
		String lastMessage = el.getText();
		return lastMessage;
	}

	public String getSendTime() {
		String formattedDate;
		DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy '∙' h:mm a");
		Date date = new Date();
		formattedDate=dateFormat.format(date);
		return formattedDate;
	}

	public boolean isMediaBarDisplayed() {
		boolean flag = mediabarPlayPauseButton.isDisplayed();
		return flag;
	}

	public DialogPage scollUpToMediaContainer() throws Throwable {
		DialogPage page = null;
		int count = 0;
		boolean mediaContainerShown = mediaContainer.isDisplayed();
		while(!(mediaContainerShown) & (count<5)){
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true){
				DriverUtils.swipeUp(driver, conversationPage, 500);
				page = this;
			}
			else {
				swipeUpSimulator();
				page = this;
			}
			mediaContainerShown = mediabarPlayPauseButton.isDisplayed();
			count++;
		}		
		Assert.assertTrue(mediabarPlayPauseButton.isDisplayed());
		return page;
	}

	public ImageFullScreenPage tapImageToOpen() throws Throwable {
		ImageFullScreenPage page = null;
		imageCell.click();
		page = new ImageFullScreenPage(url, path);
		return page;
	}

}