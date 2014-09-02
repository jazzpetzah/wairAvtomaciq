package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class ContactListPage extends AndroidPage {

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idContactListNames")
	private List<WebElement> contactListNames;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classEditText)
	private WebElement cursorInput;

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameFrameLayout)
	private List<WebElement> frameLayout;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idSelfUserName")
	private List<WebElement> selfUserName;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idContactListMute")
	private WebElement muteBtn;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConfirmCancelButton")
	private List<WebElement> laterBtn;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement mainControl;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
	private List<WebElement> connectToHeader;
	

	private String url;
	private String path;

	public ContactListPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	public AndroidPage tapOnName(String name) throws Exception {
		AndroidPage page = null;
		refreshUITree();// TODO: workaround
		findInContactList(name, 5).click();
		DriverUtils.setImplicitWaitValue(driver, 5);
		if(connectToHeader.size() > 0 && connectToHeader.get(0).isDisplayed()){
			page = new ConnectToPage(url, path);
		}
		else if (selfUserName.size() > 0 && selfUserName.get(0).isDisplayed()) {
			page = new PersonalInfoPage(url, path);
		} else {
			page = new DialogPage(url, path);
			PagesCollection.groupChatPage = new GroupChatPage(url, path);
		}
		DriverUtils.setDefaultImplicitWait(driver);
		return page;
	}

	private WebElement findInContactList(String name, int cyclesNumber) {
		WebElement contact = null;
		List<WebElement> contactsList = driver.findElements(By.xpath(String
				.format(AndroidLocators.ContactListPage.xpathContacts, name)));
		if (contactsList.size() > 0) {
			contact = contactsList.get(0);
		} else {
			if (cyclesNumber > 0) {
				cyclesNumber--;
				DriverUtils.swipeUp(driver, mainControl, 500);
				contact = findInContactList(name, cyclesNumber);
			}
		}
		return contact;
	}

	public void swipeRightOnContact(int time, String contact)
			throws IOException {
		findInContactList(contact, 5);
		WebElement el = driver.findElementByXPath(String.format(
				AndroidLocators.ContactListPage.xpathContactFrame, contact));
		DriverUtils.swipeRight(driver, el, time);
	}

	public void clickOnMute() {
		muteBtn.click();
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {

		AndroidPage page = null;
		switch (direction) {
		case DOWN: {
			page = new PeoplePickerPage(url, path);
			break;
		}
		case UP: {
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			break;
		}
		}
		return page;
	}

	public ContactListPage pressLaterButton() throws Exception {
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.PersonalInfoPage.idProfileOptionsButton));
		
		if(laterBtn.size()>0){
			laterBtn.get(0).click();
		}
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.ContactListPage.idSimpleDialogPageText));
		//TODO: we need this as sometimes we see people picker after login
		PagesCollection.peoplePickerPage = new PeoplePickerPage (url, path);
		return this;
	}
	
	public Boolean isContactExists(String name){
		return findInContactList(name,0) != null;
	}
}
