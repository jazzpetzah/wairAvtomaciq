package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.*;

public class DialogPage extends AndroidPage{

	
	@FindBy(how = How.ID, using = AndroidLocators.idCursorInput)
	private WebElement cursorInput;
	
	@FindBy(how = How.ID, using = AndroidLocators.idDialogMessages)
	private List<WebElement> messagesList;
	
	@FindBy(how = How.ID, using = AndroidLocators.idKnockAnimation)
	private List<WebElement> knockAnimation;
	
	@FindBy(how = How.ID, using = AndroidLocators.idDialogTakePhotoButton)
	private WebElement takePhotoButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idDialogOkButton)
	private WebElement okButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idDialogImages)
	private WebElement imagesList;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestDialog)
	private WebElement connectRequestDialog;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestPending)
	private WebElement connectRequestPending;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestMessage)
	private WebElement connectRequestMessage;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectRequestConnectTo)
	private WebElement connectRequestConnectTo;
	
	
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
		DriverUtils.androidMultiTap(driver, cursorInput, 3);
	}
	
	public void SwipeOnCursorInput()
	{
		DriverUtils.swipeRight(driver, cursorInput, 1000);
	}
	
	public void tapAddPictureBtn(int index)
	{
		tapButtonByClassNameAndIndex(cursorInput,AndroidLocators.classNameTextView,0);
	}
	
	public void typeMessage(String message)
	{
		cursorInput.sendKeys(message);
	}

	public String getLastMessageFromDialog()

	{
		return messagesList.get(messagesList.size()-1).getText();
	}
	
	public Boolean isknockAnimationExist(){
		return knockAnimation.size() > 0;
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
		return connectRequestDialog.isDisplayed();
	}

	public boolean isPendingButtonVisible() {
		return connectRequestPending.isDisplayed();
	}

	public boolean isConnectMessageValid(String message) {
		
		return connectRequestMessage.getText().toLowerCase().contains(message.toLowerCase());
	}

	public boolean isConnectUserValid(String user) {
		return connectRequestConnectTo.getText().toLowerCase().contains(user.toLowerCase());
	}
	
}
