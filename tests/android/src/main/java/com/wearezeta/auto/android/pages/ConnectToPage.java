package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class ConnectToPage extends AndroidPage {

	@FindBy(how = How.ID, using = AndroidLocators.idContactListNames)
	private List<WebElement> contactListNames;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectToSend)
	private WebElement sendRequest;
	
	@FindBy(how = How.ID, using = AndroidLocators.idMessage)
	private WebElement message;
	
	private String url;
	private String path;
	
	public ConnectToPage(String URL, String path) throws IOException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}

	public ContactListPage tapSend() throws IOException{
		sendRequest.click();
		wait.until(ExpectedConditions.visibilityOfAllElements(contactListNames));
		return new ContactListPage(url, path);
	}
	
	public void waitForConnectToPage(){
		wait.until(ExpectedConditions.visibilityOf(sendRequest));
	}
	
	public boolean isSendRequestVisible()
	{
		return sendRequest.isDisplayed();
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void fillTextInConnectDialog(String messageText) {
		message.clear();
		message.sendKeys(messageText);
		
	}

}
