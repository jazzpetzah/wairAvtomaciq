package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class PeoplePickerPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.namePickerSearch)
	private WebElement peoplePickerSearch;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePickerClearButton)
	private WebElement peoplePickerClearBtn;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameContactListNames)
	private List<WebElement> resultList;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathUnicUserPickerSearchResult)
	private WebElement userPickerSearchResult;
	
	private String url;
	private String path;
	
	public PeoplePickerPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		url = URL;
		this.path = path;
	}
	
	public Boolean isPeoplePickerPageVisible(){
		return peoplePickerClearBtn.isDisplayed();
	}
	
	public void tapOnPeoplePickerSearch(){
		peoplePickerSearch.click();
	}
	
	public void tapOnPeoplePickerClearBtn(){
		peoplePickerClearBtn.click();
	}
	
	public void fillTextInPeoplePickerSearch(String text){
		peoplePickerSearch.sendKeys(text);
	}
	
	public void waitUserPickerFindUser(String user){
		DriverUtils.waitUntilElementAppears(driver, By.name(user));
	}
	
	public ConnectToPage clickOnFoundUser() throws MalformedURLException{
		userPickerSearchResult.click();
		return new ConnectToPage(url, path);
	}
	
	public void pickUserAndTap(String name){
		PickUser(name).click();
	}
	
	public ContactListPage dismissPeoplePicker() throws IOException{
		peoplePickerClearBtn.click();
		return new ContactListPage(url, path);
	}
		
	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		
		IOSPage page = null;
		switch (direction){
			case DOWN:
			{
				page = new ContactListPage(url, path);
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
	
	private WebElement PickUser(String name){
		WebElement user=null;
		fillTextInPeoplePickerSearch(name);
		waitUserPickerFindUser(name);
		user = driver.findElementByXPath(IOSLocators.xpathUnicUserPickerSearchResult);

		return user;
	}

}
