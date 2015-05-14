package com.wearezeta.auto.osx.pages;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
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

import com.google.common.base.Function;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.common.ProblemReportPage;
import com.wearezeta.auto.osx.util.AccentColorUtil;
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

	public ContactListPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(OSXLocators.ContactListPage.idOpenSearchUIButton));
	}

	public PeoplePickerPage openPeoplePicker() throws Exception {
		openSearchUIButton.click();
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public SelfProfilePage openSelfProfile() throws Exception {
		selfProfileCLEntry.click();
		return new SelfProfilePage(this.getLazyDriver());
	}

	public ConnectionRequestsPage openConnectionRequests() throws Exception {
		connectionRequestsCLEntry.click();
		return new ConnectionRequestsPage(this.getLazyDriver());
	}

	public String readSelfProfileName() {
		return selfProfileCLEntry.getAttribute(OSXConstants.Attributes.AXVALUE);
	}

	public boolean waitUntilMainWindowAppears() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
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
					+ this.getDriver().getPageSource());
		} else {
			String xpath = String.format(
					OSXLocators.xpathFormatContactEntryWithName, name);
			return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
					By.xpath(xpath));
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
			return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
					By.xpath(xpath));
		}
		return true;
	}

	public WebElement getContactWithName(String name) throws Exception {
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
			result = getDriver().findElement(By.xpath(xpath));
			return result;
		}

		return result;
	}

	public boolean openConversation(String conversationName,
			boolean isUserProfile) throws Exception {

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
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(OSXLocators.idContactEntry));
	}

	public Boolean isSignOutFinished() throws Exception {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(this.getDriver())
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(2, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		WebElement el = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				try {
					return getDriver().findElement(
							By.name(OSXLocators.LoginPage.nameSignInButton));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		});
		return el != null;
	}

	public void pressLaterButton() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(OSXLocators.idShareContactsLaterButton), 5)) {
			int count = 0;
			DriverUtils.setImplicitWaitValue(this.getDriver(), 3);
			try {
				do {
					count++;
					shareContactsLaterButton.click();
					Thread.sleep(1000);
				} while (count < 10);
			} catch (NoSuchElementException e) {
				// pass silently
			} finally {
				DriverUtils.restoreImplicitWait(this.getDriver());
			}
		}
	}

	public int numberOfContacts() throws Exception {
		DriverUtils.setImplicitWaitValue(this.getDriver(), 3);
		try {
			return contactsTextFields.size();
		} finally {
			DriverUtils.restoreImplicitWait(this.getDriver());
		}
	}

	public List<WebElement> getContacts() {
		return contactsTextFields;
	}

	public void scrollToConversationInList(WebElement conversation)
			throws Exception {
		// get scrollbar for contact list
		WebElement peopleDecrementSB = null;
		WebElement peopleIncrementSB = null;

		WebElement scrollArea = getDriver().findElement(
				By.xpath(OSXLocators.xpathConversationListScrollArea));

		NSPoint mainPosition = NSPoint.fromString(scrollArea
				.getAttribute(OSXConstants.Attributes.AXPOSITION));
		NSPoint mainSize = NSPoint.fromString(scrollArea
				.getAttribute(OSXConstants.Attributes.AXSIZE));

		NSPoint latestPoint = new NSPoint(mainPosition.x() + mainSize.x(),
				mainPosition.y() + mainSize.y());

		WebElement userContact = conversation;

		NSPoint userPosition = NSPoint.fromString(userContact
				.getAttribute(OSXConstants.Attributes.AXPOSITION));
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
						.getAttribute(OSXConstants.Attributes.AXPOSITION));
				count++;
			}
			count = 0;
			while ((userPosition.y() < mainPosition.y()) && count < 10) {
				log.debug("User position: " + userPosition
						+ "; mainPosition point: " + mainPosition);
				peopleDecrementSB.click();
				userPosition = NSPoint.fromString(userContact
						.getAttribute(OSXConstants.Attributes.AXPOSITION));
				count++;
			}
		}
	}

	public void goToContactActionsMenu(String contact) throws Exception {
		try {
			clickToggleMenuButton();
			return;
		} catch (NoSuchElementException e) {
		}

		Actions builder = new Actions(this.getDriver());
		Action moveMouseToContact = builder.moveToElement(window)
				.moveToElement(getContactWithName(contact)).build();
		moveMouseToContact.perform();

		clickToggleMenuButton();
	}

	public void clickToggleMenuButton() throws Exception {
		WebElement toggleMenu = getDriver().findElement(
				By.id(OSXLocators.idShowMenuButton));
		toggleMenu.click();
	}

	public boolean isConversationMutedButtonVisible(String conversation)
			throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By
				.xpath(String.format(OSXLocators.xpathFormatMutedButton,
						conversation)));
	}

	public boolean isConversationMutedButtonNotVisible(String conversation)
			throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), By
				.xpath(String.format(OSXLocators.xpathFormatMutedButton,
						conversation)));
	}

	public void changeMuteStateForConversation(String conversation)
			throws Exception {
		goToContactActionsMenu(conversation);
		WebElement muteButton = getDriver().findElement(
				By.id(OSXLocators.idMuteButton));
		muteButton.click();
	}

	public void moveConversationToArchive(String conversation) throws Exception {
		goToContactActionsMenu(conversation);
		WebElement archiveButton = getDriver().findElement(
				By.id(OSXLocators.idArchiveButton));
		archiveButton.click();
	}

	public void showArchivedConversations() {
		showArchivedButton.click();
	}

	public PeoplePickerPage isHiddenByPeoplePicker() throws Exception {
		if (DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(OSXLocators.idShareContactsLaterButton), 3)) {
			return new PeoplePickerPage(this.getLazyDriver());
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

	public AccentColor selfNameEntryTextAccentColor() throws Exception {
		BufferedImage selfNameScreen = OSXCommonUtils.takeElementScreenshot(
				selfProfileCLEntry, this.getDriver());
		return AccentColorUtil
				.calculateAccentColorForForeground(selfNameScreen);
	}

	public void sendProblemReportIfAppears(ProblemReportPage reportPage)
			throws Exception {
		if (!isVisible()) {
			reportPage.sendReportIfAppears();
		}
	}
}
