package com.wearezeta.auto.web.pages;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ContactListPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathContactListEntries)
	private List<WebElement> contactListEntries;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathSelfProfileEntry)
	private WebElement selfName;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathOpenArchivedConvosList)
	private WebElement openArchivedConvosList;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathOpenPeoplePickerButton)
	private WebElement openPeoplePickerButton;

	public ContactListPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
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

	public PeoplePickerPage isHiddenByPeoplePicker() throws Exception {
		if (DriverUtils
				.waitUntilElementAppears(
						driver,
						By.className(WebAppLocators.ContactListPage.classNamePeoplePickerVisible),
						3)) {
			return new PeoplePickerPage(this.getDriver(), this.getWait());
		} else {
			return null;
		}
	}

	public boolean waitForContactListVisible() throws Exception {
		return DriverUtils
				.waitUntilElementAppears(
						driver,
						By.xpath(WebAppLocators.ContactListPage.xpathOpenPeoplePickerButton));
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

	public boolean contactWithNameNotVisible(String name) throws Exception {
		log.debug("Looking for contact with name '" + name + "'");
		if (name.contains(",")) {
			return retrieveNoNameGroupContact(name) != null;
		} else {
			final String xpath = WebAppLocators.ContactListPage.xpathContactListEntryByName
					.apply(name);
			return DriverUtils.waitUntilElementDissapear(driver,
					By.xpath(xpath), 20);
		}
	}

	public boolean checkNameInContactList(String name) throws Exception {
		DriverUtils.waitUntilElementAppears(driver,
				By.xpath(WebAppLocators.ContactListPage.xpathSelfProfileEntry));

		WebDriverWait wait = new WebDriverWait(driver, 10);

		return wait.until(new Function<WebDriver, Boolean>() {
			public Boolean apply(WebDriver driver) {
				if (selfName.getText().equals(name)) {
					return true;
				} else {
					return false;
				}
			}
		});
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
		this.getWait()
				.until(ExpectedConditions
						.elementToBeClickable(openArchivedConvosList));
		openArchivedConvosList.click();
	}

	public void clickArchiveConversationForContact(String conversationName)
			throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ContactListPage.xpathArchiveButtonByContactName
						.apply(conversationName));
		assert DriverUtils.isElementDisplayed(driver, locator, 5);
		final WebElement archiveButton = this.getDriver().findElement(locator);
		archiveButton.click();
	}

	public void clickMuteConversationForContact(String conversationName)
			throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ContactListPage.xpathMuteButtonByContactName
						.apply(conversationName));
		assert DriverUtils.isElementDisplayed(driver, locator, 5);
		final WebElement muteButton = this.getDriver().findElement(locator);
		muteButton.click();
	}

	public boolean isConversationMuted(String conversationName)
			throws Exception {
		// moving focus from contact - to now show ... button
		try {
			DriverUtils.moveMouserOver(driver, selfName);
		} catch (WebDriverException e) {
			// do nothing (safari workaround)
		}
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.className(WebAppLocators.ContactListPage.classMuteIcon), 5);
	}

	public void clickActionsButtonForContact(String conversationName)
			throws Exception {
		final WebElement contact = getContactWithName(conversationName);
		try {
			DriverUtils.moveMouserOver(driver, contact);
		} catch (WebDriverException e) {
			// do nothing (safari workaround)
		}
		final WebElement actionsButton = contact.findElement(By.xpath("."
				+ WebAppLocators.ContactListPage.xpathActionsButton));
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
				return new ConversationPage(this.getDriver(), this.getWait());
			}
		} else {
			for (WebElement contact : this.contactListEntries) {
				if (contact.getText().equals(conversationName)) {
					DriverUtils.waitUntilElementClickable(driver, contact);
					contact.click();
					return new ConversationPage(this.getDriver(),
							this.getWait());
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
		return new PendingConnectionsPage(this.getDriver(), this.getWait());
	}

	public SelfProfilePage openSelfProfile() throws Exception {
		DriverUtils.waitUntilElementAppears(this.getDriver(),
				By.xpath(WebAppLocators.ContactListPage.xpathSelfProfileEntry));
		selfName.click();
		return new SelfProfilePage(this.getDriver(), this.getWait());
	}

	public PeoplePickerPage openPeoplePicker() throws Exception {
		DriverUtils
				.waitUntilElementAppears(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathOpenPeoplePickerButton));
		DriverUtils.waitUntilElementClickable(driver, openPeoplePickerButton);
		if (WebAppExecutionContext.browserName
				.equals(WebAppConstants.Browser.INTERNET_EXPLORER)) {
			driver.executeScript(String.format(
					"$(document).find(\"%s\").click();",
					WebAppLocators.ContactListPage.cssOpenPeoplePickerButton));
		} else {
			openPeoplePickerButton.click();
		}
		return new PeoplePickerPage(this.getDriver(), this.getWait());
	}
}
