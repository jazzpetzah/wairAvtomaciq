package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.*;

public class ContactListPage extends AndroidPage {
	
	@FindBy(how = How.ID, using = AndroidLocators.idContactListNames)
	private List<WebElement> contactListNames;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classEditText)
	private WebElement cursorInput;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameFrameLayout)
	private List<WebElement> frameLayout;
	
	@FindBy(how = How.ID, using = AndroidLocators.idSelfUserName)
	private List<WebElement> selfUserName;
	
	@FindBy(how = How.ID, using = AndroidLocators.idContactListMute)
	private WebElement muteBtn;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLoginPage)
	private WebElement mainControl;
	
	private String url;
	private String path;
	
	public ContactListPage(String URL, String path) throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	public AndroidPage tapOnName(String name) throws IOException{
		AndroidPage page = null;
		
		findNameInContactList(name, contactListNames).click();
		if(selfUserName.size() > 0 && selfUserName.get(0).isDisplayed()){
			page = new PersonalInfoPage(url, path);
		}
		else{
			page = new DialogPage(url, path);
			wait.until(ExpectedConditions.visibilityOf(cursorInput));
			PagesCollection.groupChatPage = new GroupChatPage(url, path);
		}
		return page;
	}
	
	
	private WebElement findNameInContactList(String name, List<WebElement> contacts)
	 {
		 Boolean flag = true;
		 WebElement contact = null;
		 for(WebElement listName : contacts )
		 {
			 if(listName.getText().equals(name)){
				 contact = listName;
				 flag = false;
				 break;
			 }
		 }
		 if(flag){
			 DriverUtils.swipeUp(driver, mainControl, 500);
			 contact = findNameInContactList(name,contactListNames);
		 }
		return contact;
	 }

	public void swipeRightOnContact(int time, String contact) throws IOException
	{
		findNameInContactList(contact,contactListNames);
		WebElement el = driver.findElementByXPath(String.format(AndroidLocators.xpathContactFrame,contact));
		DriverUtils.swipeRight(driver, el, time);					
	}
	
	public void clickOnMute()
	{
		muteBtn.click();
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws IOException {
		
		AndroidPage page = null;
		switch (direction){
			case DOWN:
			{
				page = new PeoplePickerPage(url, path);
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
				break;
			}
		}	
		return page;
	}
}
