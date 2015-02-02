package com.wearezeta.auto.web.pages;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

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

	public ContactListPage(String URL, String path) throws IOException {
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

	public boolean isContactWithNameExists(String name) {
		log.debug("Looking for contact with name '" + name + "'");
		if (name.contains(",")) {
			return retrieveNoNameGroupContact(name) != null;
		} else {
			final String xpath = WebAppLocators.ContactListPage.xpathContactListEntryByName
					.apply(name);
			return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
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

	public SelfProfilePage openSelfProfile() throws Exception {
		selfName.click();
		return new SelfProfilePage(
				CommonUtils.getWebAppAppiumUrlFromConfig(ContactListPage.class),
				CommonUtils
						.getWebAppApplicationPathFromConfig(ContactListPage.class));
	}
}
