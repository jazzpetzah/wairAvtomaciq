package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;

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

	@FindBy(how = How.ID, using = AndroidLocators.idConfirmCancelButton)
	private List<WebElement> laterBtn;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLoginPage)
	private WebElement mainControl;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConnectToHeader)
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
		if(connectToHeader.size() > 0 && connectToHeader.get(0).isDisplayed()){
			page = new ConnectToPage(url, path);
		}
		else if (selfUserName.size() > 0 && selfUserName.get(0).isDisplayed()) {
			page = new PersonalInfoPage(url, path);
		} else {
			page = new DialogPage(url, path);
			wait.until(ExpectedConditions.visibilityOf(cursorInput));
			PagesCollection.groupChatPage = new GroupChatPage(url, path);
		}
		return page;
	}

	private WebElement findInContactList(String name, int cyclesNumber) {
		WebElement contact = null;
		List<WebElement> contactsList = driver.findElements(By.xpath(String
				.format(AndroidLocators.xpathContacts, name)));
		if (contactsList.size() > 0) {
			contact = contactsList.get(0);
		} else {
			if (cyclesNumber > 0) {
				System.out.println(cyclesNumber);
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
				AndroidLocators.xpathContactFrame, contact));
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

	public ContactListPage pressLaterButton() {
		if(laterBtn.size()>0){
			laterBtn.get(0).click();
		}
		DriverUtils.waitUntilElementDissapear(driver, By.id(AndroidLocators.idSimpleDialogPageText));
		return this;
	}
	
	public Boolean isContactExists(String name){
		return findInContactList(name,0) != null;
	}
}
