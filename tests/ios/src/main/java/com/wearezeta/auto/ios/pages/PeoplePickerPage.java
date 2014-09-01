package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class PeoplePickerPage extends IOSPage{
	
	@FindBy(how = How.NAME, using = IOSLocators.namePickerSearch)
	private WebElement peoplePickerSearch;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePickerClearButton)
	private WebElement peoplePickerClearBtn;
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameContactListNames)
	private List<WebElement> resultList;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathUnicUserPickerSearchResult)
	private WebElement userPickerSearchResult;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameKeyboardGoButton)
	private WebElement addToConversationsButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCreateConversationButton)
	private WebElement createConverstaionButton;
	
	private String url;
	private String path;
	
	public PeoplePickerPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		url = URL;
		this.path = path;
	}
	
	public Boolean isPeoplePickerPageVisible() {
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name(IOSLocators.namePickerClearButton)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(IOSLocators.namePickerClearButton)));
		return peoplePickerClearBtn.isDisplayed();
	}
	
	public void tapOnPeoplePickerSearch() { 
		
		driver.tap(1, peoplePickerSearch.getLocation().x + 20, peoplePickerSearch.getLocation().y + 20, 1);//workaround for people picker activation
		peoplePickerSearch.click();
	}
	
	public void tapOnPeoplePickerClearBtn() {
		peoplePickerClearBtn.click();
	}
	
	public void fillTextInPeoplePickerSearch(String text){
		peoplePickerSearch.sendKeys(text);
	}
	
	public void waitUserPickerFindUser(String user){

		wait.until(ExpectedConditions.presenceOfElementLocated(By.name(user)));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(user)));
		//DriverUtils.waitUntilElementAppears(driver, By.name(user));
	}
	
	public IOSPage clickOnFoundUser(String name) throws MalformedURLException{
		
		try{
			userPickerSearchResult.click();
		}
		catch(Exception ex){
			driver.findElement(By.name(name)).click();
		}
		
		IOSPage page = null;
		
		try {
			WebElement el = driver.findElement(By.name(IOSLocators.nameSendConnectButton));
			if( el.getText()==IOSLocators.nameSendConnectButton) {
				page = new ConnectToPage(url, path);
			}
			else {
				page = this;
			}
		}
		catch(NoSuchElementException ex)
		{
			page = this;
		}
		
		return page;
	}
	
	public  ConnectToPage pickUserAndTap(String name) throws MalformedURLException{
		PickUser(name).click();
		return new ConnectToPage(url, path);
	}
	
	public ContactListPage dismissPeoplePicker() throws IOException{
		peoplePickerClearBtn.click();
		return new ContactListPage(url, path);
	}
	
	public boolean isAddToConversationBtnVisible(){
		return addToConversationsButton.isDisplayed();
	}
	
	public GroupChatPage clickOnAddToCoversationButton() throws IOException{
		addToConversationsButton.click();
		return new GroupChatPage(url, path);
	}
	
	public GroupChatInfoPage clickOnUserToAddToExistingGroupChat(String name) throws Throwable{
		GroupChatInfoPage page = null;
		driver.findElement(By.name(name)).click();
		page = new GroupChatInfoPage(url, path);
		return page;
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
		WebElement user = null;
		fillTextInPeoplePickerSearch(name);
		waitUserPickerFindUser(name);
		user = driver.findElementByXPath(IOSLocators.xpathUnicUserPickerSearchResult);

		return user;
	}

}
