package com.wearezeta.auto.osx.pages;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.util.NSPoint;

public class ContactListPage extends OSXPage {
	
	@FindBy(how = How.ID, using = OSXLocators.idMainWindow)
	private WebElement mainWindow;

	@FindBy(how = How.ID, using = OSXLocators.idAcceptConnectionRequestButton)
	private WebElement acceptInvitationButton;
	
	@FindBy(how = How.ID, using = OSXLocators.idContactEntry)
	private List<WebElement> contactsTextFields;
	
	@FindBy(how = How.ID, using = OSXLocators.idAddConversationButton)
	private WebElement addConversationButton;
	
	@FindBy(how = How.ID, using = OSXLocators.idShowArchivedButton)
	private WebElement showArchivedButton;

	public ContactListPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}
	
	public Boolean isVisible() {
		return mainWindow != null;
	}
	
	public void openPeoplePicker() {
		addConversationButton.click();
	}
	
	public boolean isContactWithNameExists(String name) {
		if (name.contains(",")) {
			String[] exContacts = name.split(",");

			for (WebElement contact: this.contactsTextFields) {
				boolean isFound = true;
				String realContact = contact.getText();
				for (String exContact: exContacts) {
					if (!realContact.contains(exContact.trim())) {
						isFound = false;
					}
				}
				if (isFound) {
					return true;
				}
			}
		} else {
			String xpath = String.format(OSXLocators.xpathFormatContactEntryWithName, name);
			return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
		}
		return false;
	}
	
	public boolean isContactWithNameDoesNotExist(String name) {
		if (name.contains(",")) {
			String[] exContacts = name.split(",");

			for (WebElement contact: this.contactsTextFields) {
				boolean isFound = true;
				String realContact = contact.getText();
				for (String exContact: exContacts) {
					if (!realContact.contains(exContact.trim())) {
						isFound = false;
					}
				}
				if (isFound) {
					return false;
				}
			}
		} else {
			String xpath = String.format(OSXLocators.xpathFormatContactEntryWithName, name);
			return DriverUtils.waitUntilElementDissapear(driver, By.xpath(xpath));
		}
		return true;
	}
	
	public boolean openConversation(String conversationName) {

		if (conversationName.contains(",")) {
			String[] exContacts = conversationName.split(",");

			for (WebElement contact: this.contactsTextFields) {
				boolean isFound = true;
				String realContact = contact.getText();
				for (String exContact: exContacts) {
					if (!realContact.contains(exContact.trim())) {
						isFound = false;
					}
				}
				if (isFound) {
					scrollToConversationInList(contact);

					contact.click();
					return true;
				}
			}
		} else {
			for (WebElement contact: this.contactsTextFields) {
				if (contact.getText().equals(conversationName)) {
					scrollToConversationInList(contact);
					contact.click();
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean waitForSignOut() {
		DriverUtils.setImplicitWaitValue(driver, 1);
		boolean noContactList = DriverUtils.waitUntilElementDissapear(driver, By.id(OSXLocators.idContactEntry));
		DriverUtils.setDefaultImplicitWait(driver);
		return noContactList;
	}
	
	public Boolean isSignOutFinished() {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(30, TimeUnit.SECONDS)
			       .pollingEvery(2, TimeUnit.SECONDS)
			       .ignoring(NoSuchElementException.class);
		 
		 WebElement el = wait.until(new Function<WebDriver, WebElement>() {
			 
			 public WebElement apply(WebDriver driver) {
				 return driver.findElement(By.name(OSXLocators.nameSignInButton));
			 }
		 });
		return el != null;
	}

	public boolean isInvitationExist() {
		return DriverUtils.waitUntilElementAppears(driver, By.id(OSXLocators.idAcceptConnectionRequestButton));
	}
	
	public void acceptAllInvitations() {
		boolean isInivitationExist = true;
		while (isInivitationExist) {
			try {
				acceptInvitationButton.click();
			} catch (NoSuchElementException e) {
				isInivitationExist = false;
			}
		}
	}
	
	public int numberOfContacts() {
		DriverUtils.setImplicitWaitValue(driver, 3);
		int result = contactsTextFields.size();
		DriverUtils.setDefaultImplicitWait(driver);
		return result;
	}
	
	public void scrollToConversationInList(WebElement conversation) {
    	NSPoint mainPosition = NSPoint.fromString(mainWindow.getAttribute("AXPosition"));
    	NSPoint mainSize = NSPoint.fromString(mainWindow.getAttribute("AXSize"));
    	
    	NSPoint latestPoint =
    			new NSPoint(mainPosition.x() + mainSize.x(), mainPosition.y() + mainSize.y());

    	//get scrollbar for contact list
    	WebElement peopleDecrementSB = null;
    	WebElement peopleIncrementSB = null;

    	WebElement scrollArea = driver.findElement(By.xpath(OSXLocators.xpathConversationListScrollArea));
    
    	WebElement userContact = conversation;
    	boolean isFoundPeople = false;

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
	
	public void goToContactActionsMenu() {
		WebElement toggleMenu = driver.findElement(By.id(OSXLocators.idToggleMenu));
		toggleMenu.click();
	}
	
	public boolean isConversationMutedButtonExist(String conversation) {
		boolean isExist = false;
		try {
			driver.findElement(By.xpath(String.format(OSXLocators.xpathFormatMutedButton, conversation)));
			isExist = true;
		} catch (Exception e) {
			isExist = false;
		}
		return isExist;
	}
	
	public void changeMuteStateForSelectedConversation() {
		goToContactActionsMenu();
		WebElement muteButton = driver.findElement(By.id(OSXLocators.idMuteButton));
		muteButton.click();
	}
	
	public void moveSelectedConversationToArchive() {
		goToContactActionsMenu();
		WebElement archiveButton = driver.findElement(By.id(OSXLocators.idArchiveButton));
		archiveButton.click();
	}
	
	public void showArchivedConversations() {
		showArchivedButton.click();
	}
}
