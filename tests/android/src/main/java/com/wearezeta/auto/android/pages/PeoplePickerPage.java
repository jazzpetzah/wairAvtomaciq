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

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLinearLayout)
	private List<WebElement> linearLayout;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameTextView)
	private List<WebElement> textView;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classEditText)
	private WebElement editText;
	
	@FindBy(how = How.ID, using = AndroidLocators.idPickerSearchUsers)
	private List<WebElement> pickerSearchUsers;

	@FindBy(how = How.ID, using = AndroidLocators.idPeoplePickerClearbtn)
	private WebElement pickerClearBtn;

	@FindBy(how = How.ID, using = AndroidLocators.idPickerRows)
	private List<WebElement> pickerSearchRows;

	@FindBy(how = How.ID, using = AndroidLocators.idPickerUsersUnselected)
	private List<WebElement> pickerUsersUnselected;

	@FindBy(how = How.ID, using = AndroidLocators.idPickerSearch)
	private WebElement pickerSearch;

	@FindBy(how = How.ID, using = AndroidLocators.idPickerGrid)
	private WebElement pickerGrid;

	@FindBy(how = How.ID, using = AndroidLocators.idPickerBtnDone)
	private WebElement addToConversationsButton;

	@FindBy(how = How.ID, using = AndroidLocators.idCreateConversation)
	private WebElement createConversation;

	@FindBy(how = How.ID, using = AndroidLocators.idCreateConversationTitle)
	private WebElement createConversationTitle;

	public void tapPeopleSearch()
	{
		pickerSearch.click();
	}

	public void typeTextInPeopleSearch(String contactName) throws InterruptedException
	{
		pickerSearch.sendKeys(contactName);
		driver.navigate().back();
	}

	public AndroidPage selectContact(String contactName) throws IOException, InterruptedException
	{
		AndroidPage page = null;
		for(WebElement element : linearLayout)
		{
			List<WebElement> elementTexts = element.findElements(By.className(AndroidLocators.classNameTextView));
			for(WebElement el : elementTexts){
				String text = el.getText();
				if(text.equals(contactName)){
					DriverUtils.androidLongClick(driver,element);
					break;
				}
			}
		}
		if(isVisible(createConversation)){
			page = this;
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

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean isPeoplePickerPageVisible() throws InterruptedException, IOException {
		return DriverUtils.waitUntilElementAppears(driver,By.className(AndroidLocators.classEditText));
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

	public GroupChatPage clickOnAddToCoversationButton() throws IOException{
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

	public DialogPage tapCreateConversation() throws IOException {
		DriverUtils.swipeRight(driver,createConversation, 1000);
		return new DialogPage(url, path);
	}

}
