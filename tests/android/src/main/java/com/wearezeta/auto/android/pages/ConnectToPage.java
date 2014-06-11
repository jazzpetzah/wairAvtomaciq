package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class ConnectToPage extends AndroidPage {

	@FindBy(how = How.ID, using = AndroidLocators.idConnectToSend)
	private WebElement sendRequest;
	
	private String url;
	private String path;
	
	public ConnectToPage(String URL, String path) throws IOException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}

	public ContactListPage tapSend() throws IOException{
		sendRequest.click();
		return new ContactListPage(url, path);
	}
	
	public void waitForConnectToPage(){
		wait.until(ExpectedConditions.visibilityOf(sendRequest));
	}
	
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
