package com.wearezeta.auto.web.pages;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class ContactListPage extends WebPage {

	private static final Logger log = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathContactListEntry)
	private List<WebElement> contactListEntries;

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
			String xpath = String
					.format(WebAppLocators.ContactListPage.xpathFormatContactEntryWithName,
							name);
			return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
		}
	}

	public WebElement getContactWithName(String name) {
		WebElement result = null;

		if (name.contains(",")) {
			result = retrieveNoNameGroupContact(name);
		} else {
			String xpath = String
					.format(WebAppLocators.ContactListPage.xpathFormatContactEntryWithName,
							name);
			result = driver.findElement(By.xpath(xpath));
		}

		return result;
	}

	public boolean openConversation(String conversationName,
			boolean isUserProfile) {

		if (conversationName.contains(",")) {
			WebElement contact = retrieveNoNameGroupContact(conversationName);
			if (contact != null) {
				contact.click();
				return true;
			}
		} else {
			for (WebElement contact : this.contactListEntries) {
				if (contact.getText().equals(conversationName)) {
					contact.click();
					return true;
				}
			}
		}
		return false;
	}
}
