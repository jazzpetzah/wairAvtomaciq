package com.wearezeta.auto.web.pages;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ContactListPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathContactListEntries)
	private List<WebElement> contactListEntries;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathSelfProfileEntry)
	private WebElement selfName;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathArchive)
	private WebElement archive;

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.ContactListPage.classNameOpenPeoplePickerButton)
	private WebElement openPeoplePickerButton;

	public ContactListPage(String URL, String path) throws Exception {
		super(URL, path);
	}

	private WebElement retrieveNoNameGroupContact(String name) {
		String[] exContacts = name.split(",");

		for (WebElement contact : this.contactListEntries) {
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

		return null;
	}

	public boolean waitForContactListVisible() throws Exception {
		return DriverUtils.waitUntilElementVisible(driver,
				openPeoplePickerButton);
	}

	public boolean isContactWithNameExists(String name) throws Exception {
		log.debug("Looking for contact with name '" + name + "'");
		if (name.contains(",")) {
			return retrieveNoNameGroupContact(name) != null;
		} else {
			final String xpath = WebAppLocators.ContactListPage.xpathContactListEntryByName
					.apply(name);
			return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath),
					20);
		}
	}

	public String getSelfName() {
		return selfName.getText();
	}

	public WebElement getContactWithName(String name) {
		WebElement result = null;

		if (name.contains(",")) {
			result = retrieveNoNameGroupContact(name);
		} else {
			final String xpath = WebAppLocators.ContactListPage.xpathContactListEntryByName
					.apply(name);
			result = driver.findElement(By.xpath(xpath));
		}

		return result;
	}

	public void openArchive() {
		wait.until(ExpectedConditions.elementToBeClickable(archive));
		archive.click();
	}

	public void clickArchiveConversationForContact(String conversationName)
			throws Exception {

		WebElement contact = getContactWithName(conversationName);

		WebElement archiveButton = contact.findElement(By
				.className(WebAppLocators.ContactListPage.classArchiveButton));
		DriverUtils.waitUntilElementClickable(driver, archiveButton);

		archiveButton.click();
	}

	public void clickMuteConversationForContact(String conversationName)
			throws Exception {

		WebElement contact = getContactWithName(conversationName);

		List<WebElement> muteButtons = contact.findElements(By
				.className(WebAppLocators.ContactListPage.classMuteButton));
		for (WebElement e : muteButtons) {
			if (e.isDisplayed()) {
				DriverUtils.waitUntilElementClickable(driver, e);

				e.click();
			}
		}
	}

	public boolean isConversationMuted(String conversationName) throws Exception {
		// moving focus from contact - to now show ... button
		try {
			DriverUtils.moveMouserOver(driver, selfName);
		} catch (WebDriverException e) {
			// do nothing (safari workaround)
		}
		WebElement contact = getContactWithName(conversationName);
		boolean result = false;

		try {
			WebElement muteIcon = contact.findElement(By
					.className(WebAppLocators.ContactListPage.classMuteIcon));
			DriverUtils.waitUntilElementAppears(driver, muteIcon, 5);
			result = muteIcon.isDisplayed();
		} catch (NoSuchElementException ex) {

		}

		return result;
	}

	public void clickActionsButtonForContact(String conversationName) throws Exception {

		WebElement contact = getContactWithName(conversationName);
		try {
			DriverUtils.moveMouserOver(driver, contact);
		} catch (WebDriverException e) {
			// do nothing (safari workaround)
		}
		WebElement actionsButton = contact.findElement(By
				.className(WebAppLocators.ContactListPage.classActionsButton));

		DriverUtils.waitUntilElementClickable(driver, actionsButton, 5);

		actionsButton.click();
	}

	public ConversationPage openConversation(String conversationName)
			throws Exception {
		DriverUtils.waitUntilElementAppears(driver, By
				.xpath(WebAppLocators.ContactListPage.xpathContactListEntries));

		if (conversationName.contains(",")) {
			WebElement contact = retrieveNoNameGroupContact(conversationName);
			if (contact != null) {
				contact.click();
				return new ConversationPage(
						CommonUtils
								.getWebAppAppiumUrlFromConfig(ContactListPage.class),
						CommonUtils
								.getWebAppApplicationPathFromConfig(ContactListPage.class));
			}
		} else {
			for (WebElement contact : this.contactListEntries) {
				if (contact.getText().equals(conversationName)) {
					DriverUtils.waitUntilElementClickable(driver, contact);
					contact.click();
					return new ConversationPage(
							CommonUtils
									.getWebAppAppiumUrlFromConfig(ContactListPage.class),
							CommonUtils
									.getWebAppApplicationPathFromConfig(ContactListPage.class));
				}
			}
		}
		throw new RuntimeException(String.format(
				"Conversation '%s' does not exist in the conversations list",
				conversationName));
	}

	public PendingConnectionsPage openConnectionRequestsList(String listAlias)
			throws Exception {
		openConversation(listAlias);
		return new PendingConnectionsPage(
				CommonUtils.getWebAppAppiumUrlFromConfig(ContactListPage.class),
				CommonUtils
						.getWebAppApplicationPathFromConfig(ContactListPage.class));
	}

	public SelfProfilePage openSelfProfile() throws Exception {
		selfName.click();
		return new SelfProfilePage(
				CommonUtils.getWebAppAppiumUrlFromConfig(ContactListPage.class),
				CommonUtils
						.getWebAppApplicationPathFromConfig(ContactListPage.class));
	}

	public PeoplePickerPage openPeoplePicker() throws Exception {
		openPeoplePickerButton.click();
		return new PeoplePickerPage(
				CommonUtils.getWebAppAppiumUrlFromConfig(ContactListPage.class),
				CommonUtils
						.getWebAppApplicationPathFromConfig(ContactListPage.class));
	}
}
