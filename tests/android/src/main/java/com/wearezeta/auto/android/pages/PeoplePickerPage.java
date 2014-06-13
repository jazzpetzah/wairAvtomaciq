package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import com.wearezeta.auto.common.*;

public class PeoplePickerPage extends AndroidPage {

	private String url;
	private String path;
	
	public PeoplePickerPage(String URL, String path) throws IOException {
		super(URL, path);
		
		this.url = URL;
		this.path = path;
	}

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameTextView)
	private List<WebElement> pickerTextViews;
	
	@FindBy(how = How.ID, using = AndroidLocators.idPickerUsersUnselected)
	private List<WebElement> pickerUsersUnselected;
	
	@FindBy(how = How.ID, using = AndroidLocators.idPickerSearch)
	private WebElement pickerSearch;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectToSend)
	private List<WebElement> sendRequests;
	
	public void waitForPickerSearch()
	{
		DriverUtils.waitUntilElementAppears(driver,By.id(AndroidLocators.idPickerSearch));
	}
	
	public void tapPeopleSearch()
	{
		pickerSearch.click();
	}

	public void typeTextInPeopleSearch(String contactName)
	{
		pickerSearch.sendKeys(contactName);
		driver.sendKeyEvent(62); 
	}
	
	public AndroidPage selectContact(String contactName) throws IOException
	{
		AndroidPage page = null;
		
		List<WebElement> users = pickerTextViews;
		
		for(WebElement element : users)
		 {
			 try{
				 if(element.getText().toLowerCase().equals(contactName.toLowerCase())){
					 element.click();
					 DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.idPickerSearch));
					 break;
				 }
			 }
			 catch(Exception ex){
				 	continue;
			 }
		 }
		
		if(sendRequests.size() > 0 && sendRequests.get(0).isDisplayed()){
			page = new ConnectToPage(url, path);
		}
		else{
			page = new DialogPage(url, path);
		}
		return page;
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public boolean isPeoplePickerPageVisible() {
		return pickerSearch.isDisplayed();
	}


	public void waitUserPickerFindUser(String contactName) throws InterruptedException {
		
		for(int i= 0; i<5; i++){
			List<WebElement> users = pickerTextViews;
			for(WebElement element : users)
			{
				try{
					if(element.getText().toLowerCase().equals(contactName.toLowerCase())){
					return;
				 }
			 }	
				catch(Exception ex){
				 	continue;
				}
			}
			Thread.sleep(500);
		}
	}
}
