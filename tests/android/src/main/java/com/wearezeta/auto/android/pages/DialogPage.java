package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.*;

public class DialogPage extends AndroidPage{

	
	@FindBy(how = How.ID, using = AndroidLocators.idCursorInput)
	private WebElement cursorInput;
	
	@FindBy(how = How.ID, using = AndroidLocators.idDialogMessages)
	private List<WebElement> messagesList;
	
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
	
	public void tapOnCursorInput()
	{
		cursorInput.click();
	}
	
	public void typeMessage(String message)
	{
		cursorInput.sendKeys(message);
	}

	public String getLastMessageFromDialog()
	{
		return messagesList.get(messagesList.size()-1).getText();
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

}
