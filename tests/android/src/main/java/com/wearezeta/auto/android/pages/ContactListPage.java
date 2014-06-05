package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.*;

public class ContactListPage extends AndroidPage {
	
	@FindBy(how = How.ID, using = AndroidLocators.idContactListNames)
	private List<WebElement> contactListNames;
	
	@FindBy(how = How.ID, using = AndroidLocators.idInstructions)
	private List<WebElement> instructions;
	
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
		if(instructions.size() > 0 && instructions.get(0).isDisplayed()){
			page = new InstructionsPage(url, path);
		}
		else{
			page = new DialogPage(url, path);
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

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) {
		return null;
	}
}
