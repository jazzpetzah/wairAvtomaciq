package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.ContactsUploadPage;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class ContactsUploadPageSteps {
	private static final int VISIBILITY_TIMEOUT = 5; // seconds

	/**
	 * Verify that Contacts Upload dialog is visible
	 * 
	 * @step. ^I see Contacts Upload dialog$
	 * @throws Exception
	 */
	@And("^I see Contacts Upload dialog$")
	public void ISeeContactsUploadDialog() throws Exception {
		if (PagesCollection.contactsUploadPage == null) {
			PagesCollection.contactsUploadPage = (ContactsUploadPage) PagesCollection.loginPage
					.instantiatePage(ContactsUploadPage.class);
		}
		PagesCollection.contactsUploadPage.waitUntilVisible(VISIBILITY_TIMEOUT);
	}

	/**
	 * Verifies whether Google login prompt is visible
	 * 
	 * @step. ^I see Google login popup$
	 * 
	 * @throws Exception
	 */
	@And("^I see Google login popup$")
	public void ISeeGoogleLoginPopup() throws Exception {
		PagesCollection.googeLoginPage = PagesCollection.contactsUploadPage
				.switchToGooglePopup();
	}

	/**
	 * Enter gmail login and password into corresponding window
	 * 
	 * @step. ^I sign up at Google with email (.*) and password (.*)$"
	 * 
	 * @param email
	 * @param password
	 * @throws Exception
	 */
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

	/**
	 * Click Gmail inport button on Contacts Upload dialog
	 * 
	 * @step. ^I click button to import Gmail Contacts$
	 * 
	 */
	@And("^I click button to import Gmail Contacts$")
	public void IClickButtonToImportGmailContacts() {
		PagesCollection.contactsUploadPage.clickShareContactsButton();
	}

	/**
	 * Click Show Search button on Contacts Upload dialog
	 * 
	 * @step. ^I click Show Search button on Contacts Upload dialog$
	 * 
	 * @throws Exception
	 */
	@And("^I click Show Search button on Contacts Upload dialog$")
	public void IClickShowSearchButton() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.contactsUploadPage
				.clickShowSearchButton();
	}
}
