package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.*;

public class DialogPage extends AndroidPage{

	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classEditText)
	private WebElement cursorInput;
	
	@FindBy(how = How.ID, using = AndroidLocators.idMessage)
	private List<WebElement> messagesList;
	
	@FindBy(how = How.ID, using = AndroidLocators.idKnockAnimation)
	private List<WebElement> knockAnimation;
	
	@FindBy(how = How.ID, using = AndroidLocators.idDialogTakePhotoButton)
	private WebElement takePhotoButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConfirmButton)
	private WebElement okButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idDialogImages)
	private WebElement imagesList;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestDialog)
	private WebElement connectRequestDialog;
	
	@FindBy(how = How.ID, using = AndroidLocators.idMessage)
	private WebElement conversationMessage;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestMessage)
	private WebElement connectRequestMessage;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestConnectTo)
	private WebElement connectRequestConnectTo;
	
	@FindBy(how = How.ID, using = AndroidLocators.idDialogPageBottomFrameLayout)
	private WebElement dialogPageBottomFrameLayout;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classListView)
	private WebElement container;
	
	private String url;
	private String path;
	private int initMessageCount;
	
	public DialogPage(String URL, String path) throws IOException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}
	
	public void waitForCursorInputVisible(){
		
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
	
	public void tapAddPictureBtn(int index)
	{
		WebElement el = dialogPageBottomFrameLayout.findElement(By.className(AndroidLocators.classNameTextView));
		el.click();
	}
	
	public void typeMessage(String message)
	{
		cursorInput.sendKeys(message + "\n");
	}

	public String getLastMessageFromDialog()

	{
		return messagesList.get(messagesList.size()-1).getText();
	}
	
	public Boolean isknockAnimationExist(){
		return knockAnimation.size() > 0;
	}

	@Override
	public AndroidPage swipeUp(int time) throws IOException
	{
		dialogsPagesSwipeUp(time);
		return returnBySwipe(SwipeDirection.UP);
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws IOException {
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
		return DriverUtils.waitUntilElementAppears(driver,By.id(AndroidLocators.idDialogImages));
	}

	public void confirm() {
		okButton.click();
	}

	public void takePhoto() {
		takePhotoButton.click();
	}

	public boolean isConnectMessageVisible() {
		return conversationMessage.isDisplayed();
	}
	
	public boolean isConnectMessageValid(String message) {
		
		return conversationMessage.getText().toLowerCase().contains(message.toLowerCase());
	}

	
}
