package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.*;

public class DialogPage extends IOSPage{

	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathCursorInput)
	private WebElement cursorInput;
	
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
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathMediaConversationCell)
	private WebElement mediaLinkCell;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarPlayPauseButton)
	private WebElement mediabarPlayPauseButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathConversationPage)
	private WebElement conversationPage;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMediaBarCloseButton)
	private WebElement mediabarStopCloseButton;
	
	
	private String url;
	private String path;
	
	public DialogPage(String URL, String path) throws IOException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}

	public void waitForCursorInputVisible(){
		wait.until(ExpectedConditions.visibilityOf(cursorInput));
	}
	
	public void tapOnCursorInput(){
		cursorInput.click();
	}
	
	public void multiTapOnCursorInput() throws InterruptedException{
		
		DriverUtils.iOSMultiTap(driver, cursorInput, 3);
	}
	
	public void waitForTextMessageInputVisible(){
		wait.until(ExpectedConditions.visibilityOf(textInput));
	}
	
	public void typeMessage(String message)
	{
		textInput.sendKeys(message);
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
		DriverUtils.swipeRight(driver, cursorInput, 700);
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
	
	public void scrollDownTilMediaBarAppears() throws Exception{
		
		while(DriverUtils.isVisible(mediabarPlayPauseButton) == false){
			DriverUtils.swipeDown(driver, conversationPage, 500);
		}
		Assert.assertTrue(DriverUtils.isVisible(mediabarPlayPauseButton));
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
	
	@Override
	public IOSPage swipeUp(int time) throws IOException
	{
		WebElement element =  driver.findElement(By.name(IOSLocators.nameLoginPage));
		
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
	
	private String getConnectMessageLabel(){
		return connectMessageLabel.getText();
	}
	
	private String GetLastMessage(List<WebElement> chatList) {
		String lastMessageXPath = String.format(IOSLocators.xpathLastMessageFormat, chatList.size());
		WebElement el = driver.findElementByXPath(lastMessageXPath);
		String lastMessage = el.getText();
		return lastMessage;
	}

}