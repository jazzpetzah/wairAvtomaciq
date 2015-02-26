package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
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
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.util.NSPoint;

public class ContactListPage extends OSXPage {

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = OSXLocators.xpathMainWindow)
	private WebElement mainWindow;

	@FindBy(how = How.ID, using = OSXLocators.idAcceptConnectionRequestButton)
	private WebElement acceptInvitationButton;

	@FindBy(how = How.ID, using = OSXLocators.idContactEntry)
	private List<WebElement> contactsTextFields;

	@FindBy(how = How.ID, using = OSXLocators.idAddConversationButton)
	private WebElement addConversationButton;

	@FindBy(how = How.ID, using = OSXLocators.idShowArchivedButton)
	private WebElement showArchivedButton;

	@FindBy(how = How.ID, using = OSXLocators.idShareContactsLaterButton)
	private WebElement shareContactsLaterButton;

	@FindBy(how = How.ID, using = OSXLocators.idMainWindowMinimizeButton)
	private WebElement minimizeWindowButton;

	@FindBy(how = How.ID, using = OSXLocators.idMainWindowCloseButton)
	private WebElement closeWindowButton;

	public static HashMap<String, Boolean> shareContactsProcessedUsers = new HashMap<String, Boolean>();

	public ContactListPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public PeoplePickerPage openPeoplePicker() throws Exception {
		addConversationButton.click();
		return new PeoplePickerPage(this.getDriver(), this.getWait());
	}

	public boolean waitUntilMainWindowAppears() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.xpath(OSXLocators.xpathMainWindow));
	}

	public void minimizeZClient() {
		minimizeWindowButton.click();
	}

	public void restoreZClient() throws InterruptedException, ScriptException,
			IOException {
		final String[] scriptArr = new String[] {
				"property bi : \"com.wearezeta.zclient.mac\"",
				"property thisapp: \"ZClient\"",
				"tell application id bi to activate",
				"tell application \"System Events\"", " tell process thisapp",
				" click last menu item of menu \"Window\" of menu bar 1",
				" end tell", "end tell" };

		final String script = StringUtils.join(scriptArr, "\n");
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("AppleScript");
		engine.eval(script);
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

	public boolean isInvitationExist() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.id(OSXLocators.idAcceptConnectionRequestButton));
	}

	public void acceptAllInvitations() {
		List<WebElement> connectButtons = driver.findElements(By
				.id(OSXLocators.idAcceptConnectionRequestButton));
		for (WebElement connectButton : connectButtons) {
			try {
				connectButton.click();
			} catch (NoSuchElementException e) {
				log.error(e.getMessage());
			}
		}
	}

	public void ignoreAllInvitations() {
		List<WebElement> connectButtons = driver.findElements(By
				.id(OSXLocators.idIgnoreConnectionRequestButton));
		for (WebElement connectButton : connectButtons) {
			try {
				connectButton.click();
			} catch (NoSuchElementException e) {
				log.error(e.getMessage());
			}
		}
	}

	public void pressLaterButton() throws Exception {
		if (DriverUtils.waitUntilElementAppears(driver,
				shareContactsLaterButton, 5)) {
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
		Action moveMouseToContact = builder.moveToElement(mainWindow)
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
}
