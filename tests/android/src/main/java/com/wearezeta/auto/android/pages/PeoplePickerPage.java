package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.common.KeyboardMapper;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class PeoplePickerPage extends AndroidPage {

	private String url;
	private String path;

	public PeoplePickerPage(String URL, String path) throws Exception {
		super(URL, path);

		this.url = URL;
		this.path = path;
	}

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLinearLayout)
	private List<WebElement> linearLayout;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameTextView)
	private List<WebElement> textView;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classEditText)
	private WebElement editText;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idPickerSearchUsers")
	private List<WebElement> pickerSearchUsers;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idPeoplePickerClearbtn")
	private WebElement pickerClearBtn;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idPickerRows")
	private List<WebElement> pickerSearchRows;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idPickerUsersUnselected")
	private List<WebElement> pickerUsersUnselected;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idPickerSearch")
	private WebElement pickerSearch;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idPickerGrid")
	private WebElement pickerGrid;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idPickerBtnDone")
	private WebElement addToConversationsButton;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idCreateConversationIcon")
	private WebElement createConversation;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idCreateConversationTitle")
	private WebElement createConversationTitle;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idConnectionRequiesMessage")
	private WebElement connectionRequestMessage;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idSendConnectionRequestButton")
	private WebElement sendConnectionRequestButton;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idConnectToHeader")
	private List<WebElement> connectToHeader;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idConnectToCharCounter")
	private WebElement connectCharCounter;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idParticipantsClose")
	private WebElement closeButton;	
	
	public void tapPeopleSearch()
	{
		pickerSearch.click();
	}

	public void typeTextInPeopleSearch(String contactName) throws InterruptedException
	{
		pickerSearch.sendKeys(contactName);
		driver.navigate().back();
	}
	
	public void addTextToPeopleSearch(String contactName) throws InterruptedException
	{
		for(char ch : contactName.toCharArray()){
			int keyCode = KeyboardMapper.getPrimaryKeyCode(ch);
			driver.sendKeyEvent(keyCode);
		}
		
	}
	public AndroidPage selectContact(String contactName) throws Exception
	{
		AndroidPage page = null;
		WebElement el = driver.findElementByXPath(String.format(AndroidLocators.xpathPeoplePickerContact,contactName));
		el.click();
		if(connectToHeader.size() > 0 && connectToHeader.get(0).isDisplayed()){
			page = new ConnectToPage(url, path);
		}
		else if(isVisible(addToConversationsButton))
		{
			page = this;
		}
		else{
			page = new DialogPage(url, path);
		}
		return page;
	}

	public AndroidPage selectGroup(String contactName) throws Exception
	{
		AndroidPage page = null;
		WebElement el = driver.findElementByXPath(String.format(AndroidLocators.xpathPeoplePickerGroup,contactName));
		el.click();
	
		if(isVisible(addToConversationsButton))
		{
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
		refreshUITree();//TODO workaround
		//return DriverUtils.waitUntilElementAppears(driver,By.className(AndroidLocators.classEditText));
		wait.until(ExpectedConditions.elementToBeClickable(pickerSearch));
		return pickerSearch.isEnabled();
	}


	public void waitUserPickerFindUser(String contactName){
		for(int i= 0; i<5; i++){
			List<WebElement> elements = pickerSearchUsers;
			for(WebElement element : elements)
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
		}
	}

	public boolean isAddToConversationBtnVisible(){
		return addToConversationsButton.isDisplayed();
	}

	public GroupChatPage clickOnAddToCoversationButton() throws Exception{
		addToConversationsButton.click();
		return new GroupChatPage(url, path);
	}

	private boolean isVisible(WebElement element)
	{
		boolean value = false;
		try{
			element.isDisplayed();
			value = true;
		}
		catch(NoSuchElementException ex)
		{
			value = false;
		}
		return value;

	}

	public List<AndroidPage> tapCreateConversation() throws Exception {
		refreshUITree();
		DriverUtils.androidMultiTap(driver, createConversation,1,0.3);
		List<AndroidPage>  pages = new LinkedList<AndroidPage>();
		pages.add(new DialogPage(url, path));
		pages.add(new GroupChatPage(url, path));
		return  pages;
	}

	public ContactListPage tapClearButton() throws Exception {
		pickerClearBtn.click();
		return new ContactListPage(url, path);
	}

	public void tapEditConnectionRequies() {
		refreshUITree();
		connectionRequestMessage.click();
	}

	public void typeConnectionRequies(String message) {
		connectionRequestMessage.sendKeys(message);
		
	}

	public ContactListPage pressConnectButton() throws Exception {
		sendConnectionRequestButton.click();
		return new ContactListPage(url, path);
	}

	public boolean userIsVisible(String contact) {
		return isVisible(driver.findElement(By.xpath(String.format(AndroidLocators.xpathPeoplePickerContact, contact))));	
	}
	
	public boolean groupIsVisible(String contact) {
		return isVisible(driver.findElement(By.xpath(String.format(AndroidLocators.xpathPeoplePickerGroup, contact))));	
	}

	public PeoplePickerPage selectContactByLongTap(String contact) {
		refreshUITree();
		WebElement el = driver.findElementByXPath(String.format(AndroidLocators.xpathPeoplePickerContact,contact));
		DriverUtils.androidLongClick(driver, el);
		return this;
		
	}
	
	public boolean getConnectButtonState() {
		String state =  sendConnectionRequestButton.getAttribute("enabled");
		return Boolean.parseBoolean(state);
	}

	public int getCharCounterValue() {
		return Integer.parseInt(connectCharCounter.getText());
	}

	public PeoplePickerPage clickCloseButton() throws Exception {
		closeButton.click();
		return this;
	}
}
