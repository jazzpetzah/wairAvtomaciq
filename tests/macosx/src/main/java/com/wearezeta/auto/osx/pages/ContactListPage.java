package com.wearezeta.auto.osx.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.common.ProblemReportPage;
import com.wearezeta.auto.osx.util.NSPoint;

public class ContactListPage extends MainWirePage {

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	@FindBy(how = How.ID, using = OSXLocators.ContactListPage.idOpenSearchUIButton)
	private WebElement openSearchUIButton;

	@FindBy(how = How.XPATH, using = OSXLocators.ContactListPage.xpathSelfProfileCLEntry)
	private WebElement selfProfileCLEntry;

	@FindBy(how = How.XPATH, using = OSXLocators.ContactListPage.xpathConnectionRequestsCLEntry)
	private WebElement connectionRequestsCLEntry;

	@FindBy(how = How.ID, using = OSXLocators.idContactEntry)
	private List<WebElement> contactsTextFields;

	@FindBy(how = How.ID, using = OSXLocators.idShowArchivedButton)
	private WebElement showArchivedButton;

	@FindBy(how = How.ID, using = OSXLocators.idShareContactsLaterButton)
	private WebElement shareContactsLaterButton;

	public static HashMap<String, Boolean> shareContactsProcessedUsers = new HashMap<String, Boolean>();

	public ContactListPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.xpath(OSXLocators.ContactListPage.idOpenSearchUIButton));
	}

	public PeoplePickerPage openPeoplePicker() throws Exception {
		openSearchUIButton.click();
		return new PeoplePickerPage(this.getDriver(), this.getWait());
	}

	public SelfProfilePage openSelfProfile() throws Exception {
		selfProfileCLEntry.click();
		return new SelfProfilePage(this.getDriver(), this.getWait());
	}

	public ConnectionRequestsPage openConnectionRequests() throws Exception {
		connectionRequestsCLEntry.click();
		return new ConnectionRequestsPage(this.getDriver(), this.getWait());
	}

	public String readSelfProfileName() {
		return selfProfileCLEntry.getAttribute(OSXConstants.Attributes.AXVALUE);
	}

	public boolean waitUntilMainWindowAppears() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.xpath(OSXLocators.MainWirePage.xpathWindow));
	}

	public boolean isContactWithNameExists(String name) throws Exception {
		log.debug("Looking for contact with name '" + name + "'");
		if (name.contains(",")) {
			String[] exContacts = name.split(",");

			for (WebElement contact : this.contactsTextFields) {
				boolean isFound = true;
				String realContact = contact.getText();
				for (String exContact : exContacts) {
					if (!realContact.contains(exContact.trim())) {
						isFound = false;
					}
				}
				if (isFound) {
					return true;
				}
			}
			log.debug("Can't find correct contact list entry. Page source: "
					+ driver.getPageSource());
		} else {
			String xpath = String.format(
					OSXLocators.xpathFormatContactEntryWithName, name);
			return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
		}
		return false;
	}

	public boolean isContactWithNameDoesNotExist(String name) throws Exception {
		if (name.contains(",")) {
			String[] exContacts = name.split(",");

			for (WebElement contact : this.contactsTextFields) {
				boolean isFound = true;
				String realContact = contact.getText();
				for (String exContact : exContacts) {
					if (!realContact.contains(exContact.trim())) {
						isFound = false;
					}
				}
				if (isFound) {
					return false;
				}
			}
		} else {
			String xpath = String.format(
					OSXLocators.xpathFormatContactEntryWithName, name);
			return DriverUtils.waitUntilElementDissapear(driver,
					By.xpath(xpath));
		}
		return true;
	}

	public WebElement getContactWithName(String name) {
		WebElement result = null;

		if (name.contains(",")) {
			String[] exContacts = name.split(",");

			for (WebElement contact : this.contactsTextFields) {
				boolean isFound = true;
				String realContact = contact.getText();
				for (String exContact : exContacts) {
					if (!realContact.contains(exContact.trim())) {
						isFound = false;
					}
				}
				if (isFound) {
					return contact;
				}
			}
		} else {
			String xpath = String.format(
					OSXLocators.xpathFormatContactEntryWithName, name);
			result = driver.findElement(By.xpath(xpath));
			return result;
		}

		return result;
	}

	public boolean openConversation(String conversationName,
			boolean isUserProfile) {

		if (conversationName.contains(",")) {
			String[] exContacts = conversationName.split(",");

			for (WebElement contact : this.contactsTextFields) {
				boolean isFound = true;
				String realContact = contact.getText();
				for (String exContact : exContacts) {
					if (!realContact.contains(exContact.trim())) {
						isFound = false;
					}
				}
				if (isFound) {
					if (!isUserProfile)
						scrollToConversationInList(contact);
					contact.click();
					return true;
				}
			}
		} else {
			for (WebElement contact : this.contactsTextFields) {
				if (contact.getText().replaceAll("\uFFFC", "").trim()
						.equals(conversationName)) {
					if (!isUserProfile)
						scrollToConversationInList(contact);
					contact.click();
					return true;
				}
			}
		}
		return false;
	}

	public boolean waitForSignOut() throws Exception {
		DriverUtils.setImplicitWaitValue(driver, 1);
		boolean noContactList = DriverUtils.waitUntilElementDissapear(driver,
				By.id(OSXLocators.idContactEntry));
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
				return driver.findElement(By
						.name(OSXLocators.LoginPage.nameSignInButton));
			}
		});
		return el != null;
	}

	public void pressLaterButton() throws Exception {
		if (DriverUtils.waitUntilElementAppears(driver,
				By.id(OSXLocators.idShareContactsLaterButton), 5)) {
			int count = 0;
			try {
				DriverUtils.setImplicitWaitValue(driver, 3);
				do {
					count++;
					shareContactsLaterButton.click();
					Thread.sleep(1000);
				} while (count < 10);
			} catch (NoSuchElementException e) {
			} finally {
				DriverUtils.setDefaultImplicitWait(driver);
			}
		}
	}

	public int numberOfContacts() throws Exception {
		DriverUtils.setImplicitWaitValue(driver, 3);
		int result = contactsTextFields.size();
		DriverUtils.setDefaultImplicitWait(driver);
		return result;
	}

	public List<WebElement> getContacts() {
		return contactsTextFields;
	}

	public void scrollToConversationInList(WebElement conversation) {
		// get scrollbar for contact list
		WebElement peopleDecrementSB = null;
		WebElement peopleIncrementSB = null;

		WebElement scrollArea = driver.findElement(By
				.xpath(OSXLocators.xpathConversationListScrollArea));

		NSPoint mainPosition = NSPoint.fromString(scrollArea
				.getAttribute("AXPosition"));
		NSPoint mainSize = NSPoint
				.fromString(scrollArea.getAttribute("AXSize"));

		NSPoint latestPoint = new NSPoint(mainPosition.x() + mainSize.x(),
				mainPosition.y() + mainSize.y());

		WebElement userContact = conversation;

		NSPoint userPosition = NSPoint.fromString(userContact
				.getAttribute("AXPosition"));
		if (conversation.isDisplayed()) {
			return;
		}
		if (userPosition.y() > latestPoint.y()
				|| userPosition.y() < mainPosition.y()) {
			WebElement scrollBar = scrollArea.findElement(By
					.xpath("//AXScrollBar"));
			List<WebElement> scrollButtons = scrollBar.findElements(By
					.xpath("//AXButton"));
			for (WebElement scrollButton : scrollButtons) {
				String subrole = scrollButton.getAttribute("AXSubrole");

				if (subrole.equals("AXDecrementPage")) {
					peopleDecrementSB = scrollButton;
				}
				if (subrole.equals("AXIncrementPage")) {
					peopleIncrementSB = scrollButton;
				}
			}

			int count = 0;
			while ((userPosition.y() > latestPoint.y()) && count < 10) {
				log.debug("User position: " + userPosition + "; latest point: "
						+ latestPoint);
				peopleIncrementSB.click();
				userPosition = NSPoint.fromString(userContact
						.getAttribute("AXPosition"));
				count++;
			}
			count = 0;
			while ((userPosition.y() < mainPosition.y()) && count < 10) {
				log.debug("User position: " + userPosition
						+ "; mainPosition point: " + mainPosition);
				peopleDecrementSB.click();
				userPosition = NSPoint.fromString(userContact
						.getAttribute("AXPosition"));
				count++;
			}
		}
	}

	public void goToContactActionsMenu(String contact) {
		try {
			clickToggleMenuButton();
			return;
		} catch (NoSuchElementException e) {
		}

		Actions builder = new Actions(driver);
		Action moveMouseToContact = builder.moveToElement(window)
				.moveToElement(getContactWithName(contact)).build();
		moveMouseToContact.perform();

		clickToggleMenuButton();
	}

	public void clickToggleMenuButton() {
		WebElement toggleMenu = driver.findElement(By
				.id(OSXLocators.idShowMenuButton));
		toggleMenu.click();
	}

	public boolean isConversationMutedButtonVisible(String conversation)
			throws Exception {
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(String
				.format(OSXLocators.xpathFormatMutedButton, conversation)));
	}

	public boolean isConversationMutedButtonNotVisible(String conversation)
			throws Exception {
		return DriverUtils.waitUntilElementDissapear(driver, By.xpath(String
				.format(OSXLocators.xpathFormatMutedButton, conversation)));
	}

	public void changeMuteStateForConversation(String conversation) {
		goToContactActionsMenu(conversation);
		WebElement muteButton = driver.findElement(By
				.id(OSXLocators.idMuteButton));
		muteButton.click();
	}

	public void moveConversationToArchive(String conversation) {
		goToContactActionsMenu(conversation);
		WebElement archiveButton = driver.findElement(By
				.id(OSXLocators.idArchiveButton));
		archiveButton.click();
	}

	public void showArchivedConversations() {
		showArchivedButton.click();
	}

	public PeoplePickerPage isHiddenByPeoplePicker() throws Exception {
		if (DriverUtils.waitUntilElementAppears(driver,
				By.id(OSXLocators.idShareContactsLaterButton), 3)) {
			return new PeoplePickerPage(this.getDriver(), this.getWait());
		} else {
			return null;
		}
	}

	public ArrayList<String> listContacts() {
		ArrayList<String> contacts = new ArrayList<String>();

		for (WebElement contactEl : contactsTextFields) {
			contacts.add(contactEl
					.getAttribute(OSXConstants.Attributes.AXVALUE));
		}
		return contacts;
	}

	public void sendProblemReportIfAppears(ProblemReportPage reportPage)
			throws Exception {
		if (!isVisible()) {
			reportPage.sendReportIfAppears();
		}
	}
}
