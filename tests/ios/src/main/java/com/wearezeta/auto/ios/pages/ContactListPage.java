package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.*;

public class ContactListPage extends IOSPage {
	
	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameContactListNames)
	private List<WebElement> contactListNames;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameWelcomeLabel)
	private List<WebElement> welcomeLabel;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathFirstInContactList)
	private WebElement firstContactListDialog;

	private String url;
	private String path;
	
	public ContactListPage(String URL, String path) throws IOException {
		super(URL, path);
		url = URL;
		this.path = path;
	}

	public IOSPage tapOnName(String name) throws IOException {
		IOSPage page = null;
		findNameInContactList(name).click();
		if(welcomeLabel.size() > 0  && welcomeLabel.get(0).isDisplayed()){
			page = new WelcomePage(url, path);
		}
		else{
			page = new DialogPage(url, path);
		}
		return page;
	}
	
	public String getFirstDialogName(){
		return firstContactListDialog.getText();
	}
	
	private WebElement findNameInContactList(String name)
	 {
		 Boolean flag = true;
		 WebElement contact = null;
		 for(WebElement listName : contactListNames )
		 {
			 if(listName.getText().equals(name)) {
				 contact = listName;
				 flag = false;
				 break;
			 }
		 }
		 if(flag) {
			 DriverUtils.scrollToElement(driver,contactListNames.get(contactListNames.size() - 1));
			 findNameInContactList(name);
		 }
		return contact;
	 }	
	
	public boolean isGroupChatAvailableInContactList() {
		boolean flag = false;
		
		for (WebElement el: contactListNames) {
			if(el.getAttribute("name").contains(","))
			{
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	public GroupChatPage tapOnGroupChat(String contact1, String contact2) throws IOException {
		
		findChatInContactList(contact1, contact2).click();

		return new GroupChatPage(url, path);
	}
	
	private WebElement findChatInContactList(String contact1, String contact2) {

		String contact = "";
		WebElement el = null;
		
		for (WebElement element: contactListNames) {
			contact = element.getAttribute("name");
			if(contact.contains(",") && contact.contains(contact1) && contact.contains(contact2))
			{
				el = element;
				break;
			}
		}
		
		return el;
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		
		IOSPage page = null;
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