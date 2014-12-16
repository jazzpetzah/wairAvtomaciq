package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.util.NSPoint;

public class PeoplePickerPage extends OSXPage {
	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class.getSimpleName());
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathMainWindow)
	private WebElement mainWindow;

//	@FindBy(how = How.ID, using = OSXLocators.idPeoplePickerDismissButton)
	private WebElement cancelButton;
	
	@FindBy(how = How.NAME, using = OSXLocators.namePeoplePickerAddToConversationButton)
	private WebElement addToConversationButton;
	
//	@FindBy(how = How.ID, using = OSXLocators.idPeoplePickerSearchField)
	private WebElement searchField;
	
	@FindBy(how = How.ID, using = OSXLocators.idPeoplePickerSearchResultTable)
	private WebElement peoplePickerSearchResultTable;
	
	@FindBy(how = How.ID, using = OSXLocators.idPeoplePickerTopContactsSectionHeader)
	private WebElement peoplePickerTopContactsSectionHeader;

	@FindBy(how = How.ID, using = OSXLocators.idPeoplePickerSearchResultEntry)
	private List<WebElement> searchResults;
	
	@FindBy(how = How.ID, using = OSXLocators.idPeoplePickerTopContactsGrid)
	private WebElement peoplePickerTopContactsList;
	
	
	
	public PeoplePickerPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}
	
	public WebElement findSearchField() {
		List<WebElement> textAreaCandidates = driver.findElements(By.className("AXTextArea"));
    	for (WebElement textArea: textAreaCandidates) {
    		if (textArea.getAttribute("AXIdentifier").equals(OSXLocators.idPeoplePickerSearchField)) {
    			return textArea;
    		}
    	}
        return null;
	}
	
	public WebElement findCancelButton() {
		List<WebElement> buttonCandidates = driver.findElements(By.className("AXButton"));
    	for (WebElement button: buttonCandidates) {
    		if (button.getAttribute("AXIdentifier").equals(OSXLocators.idPeoplePickerDismissButton)) {
    			return button;
    		}
    	}
        return null;
	}
	
	public void searchForText(String text) {
		int i = 0;
		searchField = null;
		while (searchField == null) {
			searchField = findSearchField();
			if (++i == 10) break;
			try { Thread.sleep(1000); } catch (InterruptedException e) { } 
		}
		searchField.sendKeys(text);
	}
	
	public boolean sendInvitationToUserIfRequested() {
        try {
        	WebElement sendButton = driver.findElement(By.id(OSXLocators.idSendInvitationButton));
        	sendButton.click();
       	} catch (NoSuchElementException e) {
       		return false;
       	}
        return true;
	}
	
	public boolean areSearchResultsContainUser(String username) {
		String xpath = String.format(OSXLocators.xpathFormatPeoplePickerSearchResultUser, username);
		
		DriverUtils.setImplicitWaitValue(driver, 60);
		boolean result = DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
		DriverUtils.setDefaultImplicitWait(driver);
		return result;
	}
	
	public void scrollToUserInSearchResults(String user) {
    	NSPoint mainPosition = NSPoint.fromString(mainWindow.getAttribute("AXPosition"));
    	NSPoint mainSize = NSPoint.fromString(mainWindow.getAttribute("AXSize"));
    	
    	NSPoint latestPoint =
    			new NSPoint(mainPosition.x() + mainSize.x(), mainPosition.y() + mainSize.y());

    	//get scrollbar for contact list
    	WebElement peopleDecrementSB = null;
    	WebElement peopleIncrementSB = null;

    	WebElement scrollArea = driver.findElement(By.xpath(OSXLocators.xpathSearchResultsScrollArea));
   
    	WebElement userContact = null;
    	boolean isFoundPeople = false;
		try {
			for (WebElement contact: searchResults) {
				if (contact.getText().equals(user)) {
					isFoundPeople = true;
					userContact = contact;
				}
			}
		} catch (NoSuchElementException e) {
			isFoundPeople = false;
		}
		
        NSPoint userPosition = NSPoint.fromString(userContact.getAttribute("AXPosition"));
        if (userPosition.y() > latestPoint.y() || userPosition.y() < mainPosition.y()) {
        	if (isFoundPeople) {
    			WebElement scrollBar = scrollArea.findElement(By.xpath("//AXScrollBar"));
    			List<WebElement> scrollButtons = scrollBar.findElements(By.xpath("//AXButton"));
    			for (WebElement scrollButton: scrollButtons) {
    				String subrole = scrollButton.getAttribute("AXSubrole");
    				if (subrole.equals("AXDecrementPage")) {
    					peopleDecrementSB = scrollButton;
    				}
    				if (subrole.equals("AXIncrementPage")) {
    					peopleIncrementSB = scrollButton;
    				}
    			}
    		}
        	
            while (userPosition.y() > latestPoint.y()) {
            	peopleIncrementSB.click();
            	userPosition = NSPoint.fromString(userContact.getAttribute("AXPosition"));
            }
            while (userPosition.y() < mainPosition.y()) {
            	peopleDecrementSB.click();
            	userPosition = NSPoint.fromString(userContact.getAttribute("AXPosition"));
            }
        }
		
	}
	
	public boolean isPeoplePickerPageVisible() throws InterruptedException, IOException {
		boolean flag = false;
		try {
			peoplePickerSearchResultTable.click();
			flag = true;
		} catch (NoSuchElementException e) {
		}
		try {
			peoplePickerTopContactsSectionHeader.click();
			flag = true;
		} catch (NoSuchElementException e) { }
		return flag;
	}
	
	public void closePeoplePicker() {
		findCancelButton().click();
	}
	
	public void selectUserInSearchResults(String user) {
		for (WebElement userEntry: searchResults) {
			if (userEntry.getText().equals(user)) {
				userEntry.click();
				break;
			}
		}
	}
	
	public void chooseUserInSearchResults(String user) {
		selectUserInSearchResults(user);
		DriverUtils.setImplicitWaitValue(driver, 3);
		try {
			addSelectedUsersToConversation();
		} catch (NoSuchElementException e) {
		} finally {
			DriverUtils.setDefaultImplicitWait(driver);
		}
	}
	
	public void addSelectedUsersToConversation() {
		addToConversationButton.click();
	}
	
	public boolean isTopPeopleVisible(){
		return DriverUtils.waitUntilElementAppears(driver,
				By.xpath(OSXLocators.xpathPeoplePickerTopContactsSectionHeader));	
	}
}
