package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.common.KeyboardMapper;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class PeoplePickerPage extends AndroidPage {
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearchUsers")
	private List<WebElement> pickerSearchUsers;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearchUsers")
	private WebElement pickerSearchUser;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPeoplePickerSerchConversations")
	private List<WebElement> pickerSearchConversations;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPeoplePickerClearbtn")
	private WebElement pickerClearBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerRows")
	private List<WebElement> pickerSearchRows;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerUsersUnselected")
	private List<WebElement> pickerUsersUnselected;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearch")
	private WebElement pickerSearch;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearch")
	private List<WebElement> pickerSearchList;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerGrid")
	private WebElement pickerGrid;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerBtnDone")
	private WebElement addToConversationsButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idCreateConversationIcon")
	private WebElement createConversation;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idNoResultsFound")
	private WebElement noResults;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
	private List<WebElement> connectToHeader;
	
	private String url;
	private String path;

	public PeoplePickerPage(String URL, String path) throws Exception {
		super(URL, path);

		this.url = URL;
		this.path = path;
	}
	
	
	public void tapPeopleSearch() {
		pickerSearch.click();
	}

	public void tapOnContactInTopPeoples(String name) throws Exception{
		WebElement el = driver.findElement(By.xpath(String.format(AndroidLocators.PeoplePickerPage.xpathTopConversationContact, name.toUpperCase())));
		el.click();
	}
	
	public void typeTextInPeopleSearch(String contactName) throws InterruptedException
	{
		pickerSearch.sendKeys(contactName);
		driver.sendKeyEvent(66);
	}
	
	public void addTextToPeopleSearch(String contactName) throws InterruptedException
	{
		for(char ch : contactName.toCharArray()) {
			int keyCode = KeyboardMapper.getPrimaryKeyCode(ch);
			driver.sendKeyEvent(keyCode);
		}
		
	}
	
	public boolean isNoResultsFoundVisible() {
		
		refreshUITree();
		return noResults.isDisplayed();
	}
	
	public AndroidPage selectContact(String contactName) throws Exception {
		AndroidPage page = null;
		refreshUITree();
		pickerSearchUser.click();
		if(CommonUtils.getAndroidApiLvl(PeoplePickerPage.class) > 42){
			DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PeoplePickerPage.idPickerSearchUsers));
		}
		else{
			DriverUtils.waitUntilElementDissapear(driver, By.xpath(AndroidLocators.PeoplePickerPage.xpathOtherText));
		}
		refreshUITree();
		if(driver.findElements(AndroidLocators.OtherUserPersonalInfoPage.getByForOtherUserPersonalInfoUnlockButton()).size() > 0) {
			page = new OtherUserPersonalInfoPage(url, path);
		}
		else if(driver.findElements(AndroidLocators.ConnectToPage.getByForConnectToPageHeader()).size() > 0) {
			page = new ConnectToPage(url, path);
		}
		else if(isVisible(addToConversationsButton)) {
			page = this;
		}
		else {
			page = new DialogPage(url, path);
		}
		return page;
	}

	public AndroidPage selectGroup(String contactName) throws Exception {
		AndroidPage page = null;
		WebElement el = driver.findElementByXPath(String.format(AndroidLocators.PeoplePickerPage.xpathPeoplePickerGroup,contactName));
		el.click();
	
		if(isVisible(addToConversationsButton)) {
			page = this;
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


	public boolean isPeoplePickerPageVisible() throws InterruptedException, IOException {
		Boolean flag = false;
		refreshUITree();//TODO workaround
		try {
			wait.until(ExpectedConditions.visibilityOf(pickerSearch));
		} catch (NoSuchElementException e) {
			return false;
		} catch (TimeoutException e) {
			return false;
		}
		if(pickerSearchList.size() > 0){
			flag = true;
		}
		return flag;
	}


	public void waitUserPickerFindUser(String contactName){
		for(int i = 0; i < 5; i++){
			List<WebElement> elements = pickerSearchUsers;
			for(WebElement element : elements) {
				try{
					if(element.getText().toLowerCase().equals(contactName.toLowerCase())){
						return;
					}
				}	
				catch(Exception ex){
					continue;
				}
			}
		}
	}

	public boolean isAddToConversationBtnVisible(){
		return addToConversationsButton.isDisplayed();
	}

	public DialogPage clickOnAddToCoversationButton() throws Exception{
		driver.navigate().back();
		addToConversationsButton.click();
		return new DialogPage(url, path);
	}
	//TODO: move this to some base page

	public AndroidPage tapCreateConversation() throws Exception {
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(createConversation));
		createConversation.click();
		
		return  new DialogPage(url, path);
	}

	public ContactListPage tapClearButton() throws Exception {
		refreshUITree();
		pickerClearBtn.click();
		//DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PeoplePickerPage.idPeoplePickerClearbtn));
		refreshUITree();
		return new ContactListPage(url, path);
	}



	public boolean userIsVisible(String contact) throws Exception {
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PeoplePickerPage.idNoResultsFound));
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOfAllElements(pickerSearchUsers));
		return isVisible(driver.findElement(By.xpath(String.format(AndroidLocators.PeoplePickerPage.xpathPeoplePickerContact, contact))));	
	}
	
	public boolean groupIsVisible(String contact) throws Exception {
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PeoplePickerPage.idNoResultsFound));
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOfAllElements(pickerSearchConversations));
		return isVisible(driver.findElement(By.xpath(String.format(AndroidLocators.PeoplePickerPage.xpathPeoplePickerGroup, contact))));	
	}

	public PeoplePickerPage selectContactByLongTap(String contact) {
		refreshUITree();
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PeoplePickerPage.idNoResultsFound));
		refreshUITree();
		WebElement el = driver.findElementByXPath(String.format(AndroidLocators.PeoplePickerPage.xpathPeoplePickerContact,contact));
		DriverUtils.androidLongClick(driver, el);
		return this;
		
	}
	
}
