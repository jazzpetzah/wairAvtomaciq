package com.wearezeta.auto.web.steps;

import java.util.Set;

import org.openqa.selenium.WebDriver;

import com.wearezeta.auto.web.pages.ContactsUploadPage;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.external.GoogleLoginPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class ContactsUploadPageSteps {
	private static final int VISIBILITY_TIMEOUT = 3; // seconds

	/**
	 * Verify that Contacts Upload dialog is visible
	 * 
	 * @step. ^I see Contacts Upload dialog$
	 * @throws Exception
	 */
	@And("^I see Contacts Upload dialog$")
	public void ISeeContactsUploadDialog() throws Exception {
		if (PagesCollection.contactsUploadPage == null) {
			PagesCollection.contactsUploadPage = new ContactsUploadPage(
					PagesCollection.loginPage.getDriver(),
					PagesCollection.loginPage.getWait());
		}
		PagesCollection.contactsUploadPage.waitUntilVisible(VISIBILITY_TIMEOUT);
	}

	@And("^I see Google login popup$")
	public void ISeeGoogleLoginPopup() throws Exception {
		WebDriver driver = PagesCollection.contactsUploadPage.getDriver();
		Set<String> handles = driver.getWindowHandles();
		handles.remove(driver.getWindowHandle());
		assert handles.size() > 0 : "No Popup for Google was found";
		driver.switchTo().window(handles.iterator().next());
		PagesCollection.googeLoginPage = new GoogleLoginPage(
				PagesCollection.contactsUploadPage.getDriver(),
				PagesCollection.contactsUploadPage.getWait());
	}

	@When("^I sign up at Google with email (.*) and password (.*)$")
	public void ISignUpAtGoogleWithEmail(String email, String password)
			throws Exception {
		PagesCollection.googeLoginPage.setEmail(email);
		PagesCollection.googeLoginPage.setPassword(password);
		PagesCollection.googeLoginPage.clickSignIn();
	}

	/**
	 * Close Contacts Upload dialog
	 * 
	 * @step. ^I close Contacts Upload dialog$
	 * 
	 */
	@And("^I close Contacts Upload dialog$")
	public void ICloseContactsUploadDialog() {
		PagesCollection.contactsUploadPage.close();
	}

	@And("^I click button to import Gmail Contacts$")
	public void IClickButtonToImportGmailContacts() {
		PagesCollection.contactsUploadPage.clickShareContactsButton();
	}
}
