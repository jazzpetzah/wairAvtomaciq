package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.*;

public class DialogPage extends IOSPage{

	
	@FindBy(how = How.NAME, using = IOSLocators.nameCursorInput)
	private WebElement cursorInput;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameTextInput)
	private WebElement textInput;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameDialogMessages)
	private List<WebElement> messagesList;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameConnectMessageLabel)
	private WebElement connectMessageLabel;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePendingButton)
	private WebElement pendingButton;
	
	private String url;
	private String path;
	
	public DialogPage(String URL, String path) throws IOException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}

	public void waitForCursorInputVisible(){
		ScrollToLastMessage();
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
		DriverUtils.scrollToElement(driver, messagesList.get(messagesList.size()-1));
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