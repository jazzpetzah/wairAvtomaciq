package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
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
	private WebElement goButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCreateConversationButton)
	private WebElement createConverstaionButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePeoplePickerContactsLabel)
	private WebElement contactsLabel;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePeoplePickerOtheraLabel)
	private WebElement othersLabel;
	
	@FindBy(how = How.NAME, using = IOSLocators.NamePeoplePickerTopPeopleLabel)
	private WebElement topPeopleLabel;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePeoplePickerAddToConversationButton)
	private WebElement addToConversationBtn;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameLaterButton)
	private WebElement laterButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameContinueUploadButton)
	private WebElement continueButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePeopleYouMayKnowLabel)
	private WebElement peopleYouMayKnowLabel;
	
	
	private String url;
	private String path;
	
	private int numberTopSelected = 0;
	
	public PeoplePickerPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		url = URL;
		this.path = path;
	}
	
	public void clickLaterButton() {
		if(DriverUtils.isElementDisplayed(laterButton)) {
			laterButton.click();
		}
	}
	
	public Boolean isPeoplePickerPageVisible() {	
		 
		boolean result = DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.namePickerClearButton));
		clickLaterButton();
		return result;
	}
	
	public void tapOnPeoplePickerSearch() { 
		
		driver.tap(1, peoplePickerSearch.getLocation().x + 40, peoplePickerSearch.getLocation().y + 30, 1);//workaround for people picker activation
	}
	
	public void tapOnPeoplePickerClearBtn() {
		peoplePickerClearBtn.click();
	}
	
	public void fillTextInPeoplePickerSearch(String text){
		peoplePickerSearch.sendKeys(text);
	}
	
	public boolean waitUserPickerFindUser(String user){
		return DriverUtils.waitUntilElementAppears(driver, By.name(user));
	}
	
	public ConnectToPage clickOnNotConnectedUser(String name) throws MalformedURLException{
		ConnectToPage page;
		driver.findElement(By.name(name)).click();
		page = new ConnectToPage(url, path);
		return page;
	}
	
	public  ConnectToPage pickUserAndTap(String name) throws MalformedURLException{
		PickUser(name).click();
		return new ConnectToPage(url, path);
	}
	
	public  PendingRequestsPage pickIgnoredUserAndTap(String name) throws MalformedURLException{
		PickUser(name).click();
		return new PendingRequestsPage(url, path);
	}
	
	public ContactListPage dismissPeoplePicker() throws IOException{
		peoplePickerClearBtn.click();
		return new ContactListPage(url, path);
	}
	
	public boolean isAddToConversationBtnVisible(){
		return DriverUtils.isElementDisplayed(addToConversationBtn);
	}
	
	public boolean addToConversationNotVisible(){
		boolean flag;
		try{
			addToConversationBtn.click();
			flag = false;
		}
		catch(Exception e){
			flag = true;
		}
		return flag;
	}
	
	public GroupChatPage clickOnGoButton() throws IOException{
		goButton.click();
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
				page = this;
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
		user = driver.findElementByName(name);

		return user;
	}
	
	public void clearInputField(){
		peoplePickerSearch.clear();
	}
	
	public boolean isContactsLabelVisible(){
		return DriverUtils.isElementDisplayed(contactsLabel);
	}

	public void selectUser(String name) {
		driver.findElement(By.name(name)).click();
	}
	
	public void tapNumberOfTopConnections(int numberToTap){
		numberTopSelected = 0;
		for(int i=1;i<numberToTap+1;i++){
			numberTopSelected++;
			driver.findElement(By.xpath(String.format(IOSLocators.xpathPeoplePickerTopConnectionsAvatar, i))).click();
		}
	}
	
	public boolean isCreateConversationButtonVisible(){
		return DriverUtils.isElementDisplayed(createConverstaionButton);
	}
	
	public IOSPage clickCreateConversationButton() throws Throwable{
		createConverstaionButton.click();
		if (numberTopSelected >= 2) {
			return new GroupChatPage(url, path);
		}
		else {
			return new DialogPage(url, path);
		}
	}
	
	public boolean isTopPeopleLabelVisible(){
		return DriverUtils.isElementDisplayed(topPeopleLabel);
	}

	public boolean isUserSelected(String name) {
		WebElement el = driver.findElement(By.xpath(String.format(IOSLocators.xpathPeoplePickerUserAvatar, name)));
		boolean flag = el.getAttribute("value").equals("1");
		return flag;
	}
	
	public void clickConnectedUserAvatar(String name){
		WebElement el = driver.findElement(By.xpath(String.format(IOSLocators.xpathPeoplePickerUserAvatar, name)));
		el.click();
	}
	
	public void hitDeleteButton(){
		peoplePickerSearch.sendKeys(Keys.DELETE);
	}
	
	public void goIntoConversation(){
		peoplePickerSearch.sendKeys("\n");
	}
	
	public GroupChatPage clickAddToCoversationButton() throws Exception{
		addToConversationBtn.click();
		return new GroupChatPage(url, path);
	}

	public OtherUserOnPendingProfilePage clickOnUserOnPending(String contact) throws Exception {
		OtherUserOnPendingProfilePage page;
		driver.findElement(By.name(contact)).click();
		page = new OtherUserOnPendingProfilePage(url, path);
		return page;
	}

	public boolean isUploadDialogShown() {
		boolean isLaterBtnVisible = DriverUtils.isElementDisplayed(laterButton);
		return isLaterBtnVisible;
	}

	public void clickContinueButton() {
		continueButton.click();
	}

	public boolean isPeopleYouMayKnowLabelVisible() {
		return DriverUtils.isElementDisplayed(peopleYouMayKnowLabel);
	}

}
