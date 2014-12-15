package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class ContactListPage extends IOSPage {
	private static final Logger log = ZetaLogger.getLog("iOS:" + ContactListPage.class.getSimpleName());
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathContactListNames)
	private List<WebElement> contactListNames;

	@FindBy(how = How.NAME, using = IOSLocators.nameProfileName)
	private WebElement profileName;

	@FindBy(how = How.NAME, using = IOSLocators.nameMuteButton)
	private List<WebElement> muteButtons;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathMyUserInContactList)
	private WebElement myUserNameInContactList;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathFirstChatInChatListTextField)
	private WebElement firstChatInChatListTextField;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameContactListLoadBar)
	private WebElement loadBar;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameMediaCellPlayButton)
	private WebElement playPauseButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathPendingRequest)
	private WebElement pendingRequest;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameTutorialView)
	private WebElement tutorialView;
	
	private String url;
	private String path;
	private int oldLocation = 0;


	public ContactListPage(String URL, String path) throws IOException {
		super(URL, path);
		url = URL;
		this.path = path;
	}

	public boolean isMyUserNameDisplayedFirstInContactList(String name) {
		return myUserNameInContactList.getText().equals(name);
	}

	public void muteConversation() {

		for (WebElement el : muteButtons) {
			if (el.isDisplayed()) {
				el.click();
			}
		}
	}

	public boolean isContactMuted(String contact) {

		// potential floating bug, the icon is always visible, but simply
		// changes x coordinate
		return driver.findElementByXPath(
				String.format(IOSLocators.xpathMutedIcon, contact))
				.getLocation().x < oldLocation;
	}
	
	public boolean isPlayPauseButtonVisible(String contact){
		boolean flag = false;
		flag = DriverUtils.waitUntilElementAppears(driver, By.xpath(String.format(IOSLocators.xpathContactListPlayPauseButton, contact)));
		return flag;
	}
	
	public void tapPlayPauseButton(){
		playPauseButton.click();
	}

	private boolean isProfilePageVisible() {
		boolean result = false;

		try {
			result = profileName.isDisplayed();
		} catch (org.openqa.selenium.NoSuchElementException ex) {
			// do nothing
		}

		return result;
	}

	public IOSPage tapOnName(String name) throws IOException {
		IOSPage page = null;
		WebElement el = findNameInContactList(name);
		wait.until(ExpectedConditions.elementToBeClickable(el));
		el.click();
		if (isProfilePageVisible()) {
			page = new PersonalInfoPage(url, path);
		} else {
			page = new DialogPage(url, path);
		}
		return page;
	}

	public String getFirstDialogName(String name) {

		DriverUtils.waitUntilElementAppears(driver, By.xpath(String.format(
				IOSLocators.xpathFirstInContactList, name)));
		WebElement contact = driver.findElement(By.xpath(String.format(
				IOSLocators.xpathFirstInContactList, name)));
		return contact.getText();
	}

	private WebElement findNameInContactList(String name) {
		Boolean flag = true;
		WebElement contact = null;
		for (int i = 0; i < 5; i++) {
			for (WebElement listName : contactListNames) {
				if (listName.getText().equals(name)) {
					contact = listName;
					flag = false;
					break;
				}
			}
			if (flag) {
				WebElement el = contactListNames.get(contactListNames.size() - 1);
				wait.until(ExpectedConditions.visibilityOf(el));
				wait.until(ExpectedConditions.elementToBeClickable(el));
				DriverUtils.scrollToElement(driver, el);
			}
			else {
				break;
			}
		}
		return contact;
	}
	
	public boolean isChatInContactList(String name){
		boolean flag = findNameInContactList(name) != null;
		return flag;
	}

	public boolean isGroupChatAvailableInContactList() {
		boolean flag = false;

		for (WebElement el : contactListNames) {
			if (el.getAttribute("name").contains(",")) {
				flag = true;
				break;
			}
		}

		return flag;
	}

	public IOSPage swipeRightOnContact(int time, String contact)
			throws IOException {
		oldLocation = driver.findElementByXPath(
				String.format(IOSLocators.xpathMutedIcon, contact))
				.getLocation().x;
		DriverUtils.swipeRight(driver, findNameInContactList(contact), time);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public boolean verifyChangedGroupNameInChatList() {
		return firstChatInChatListTextField.getText().equals(
				PagesCollection.groupChatInfoPage.getConversationName());
	}

	public GroupChatPage tapOnUnnamedGroupChat(String contact1, String contact2)
			throws IOException {

		findChatInContactList(contact1, contact2).click();

		return new GroupChatPage(url, path);
	}

	public IOSPage tapOnGroupChat(String chatName) throws IOException {
		findNameInContactList(chatName).click();
		return new GroupChatPage(url, path);
	}

	public boolean waitForContactListToLoad() {
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(IOSLocators.xpathMyUserInContactList));
	}

	private WebElement findChatInContactList(String contact1, String contact2) {

		String contact = "";
		WebElement el = null;

		for (WebElement element : contactListNames) {
			contact = element.getAttribute("name");
			if (contact.contains(",") && contact.contains(contact1)
					&& contact.contains(contact2)) {
				el = element;
				break;
			}
		}

		return el;
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {

		IOSPage page = null;
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
	
	public boolean isPendingRequestInContactList(){
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(IOSLocators.xpathPendingRequest));
	}
	
	public PendingRequestsPage clickPendingRequest() throws Throwable{
		pendingRequest.click();
		return new PendingRequestsPage(url, path);
	}
	
	public boolean isDisplayedInContactList(String name){
		return DriverUtils.waitUntilElementAppears(driver, By.name(name));
	}
	
	public boolean isTutorialShown(){
		//this.refreshUITree();
		DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameTutorialView));
		boolean tutorialShown = DriverUtils.isElementDisplayed(tutorialView);
		return tutorialShown;
	}
	
	public void dismissTutorial(){
		
		WebElement tutorialView = driver.findElement(By.name(IOSLocators.nameTutorialView));
		DriverUtils.iOS3FingerTap(driver, tutorialView, 3);
	}
	
	public List<WebElement> GetVisibleContacts(){
		return contactListNames;
	}
	
	public IOSPage tapOnContactByIndex(List<WebElement> contacts, int index) throws Exception{
		IOSPage page = null;
		DriverUtils.waitUntilElementClickable(driver, contacts.get(index));
		contacts.get(index).click();
		page = new DialogPage(url, path);
		return page;
	}
	
	@Override
	public IOSPage swipeDown(int time) throws IOException {
		Point coords = content.getLocation();
		Dimension elementSize = content.getSize();
		driver.swipe(coords.x + elementSize.width / 2, coords.y + 50, coords.x + elementSize.width / 2, coords.y + elementSize.height - 150, time);
		return returnBySwipe(SwipeDirection.DOWN);
	}
	
}