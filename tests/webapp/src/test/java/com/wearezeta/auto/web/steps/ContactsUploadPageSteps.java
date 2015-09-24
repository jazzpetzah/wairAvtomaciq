package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.ContactsUploadPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class ContactsUploadPageSteps {
	private static final int VISIBILITY_TIMEOUT = 15; // seconds

	/**
	 * Verify that Contacts Upload dialog is visible
	 * 
	 * @step. ^I see Contacts Upload dialog$
	 * @throws Exception
	 */
	@And("^I see Contacts Upload dialog$")
	public void ISeeContactsUploadDialog() throws Exception {
		if (WebappPagesCollection.contactsUploadPage == null) {
			WebappPagesCollection.contactsUploadPage = (ContactsUploadPage) WebappPagesCollection.loginPage
					.instantiatePage(ContactsUploadPage.class);
		}
		WebappPagesCollection.contactsUploadPage.waitUntilVisible(VISIBILITY_TIMEOUT);
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
		WebappPagesCollection.googleLoginPage = WebappPagesCollection.contactsUploadPage
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
		// sometimes Google already shows the email
		WebappPagesCollection.googleLoginPage.setEmail(email);
		// sometimes google shows a next button and you have to enter the
		// password separately
		if (WebappPagesCollection.googleLoginPage.hasNextButton()) {
			WebappPagesCollection.googleLoginPage.clickNext();
		}
		WebappPagesCollection.googleLoginPage.setPassword(password);
		WebappPagesCollection.peoplePickerPage = WebappPagesCollection.googleLoginPage
				.clickSignIn();
	}

	/**
	 * Close Contacts Upload dialog
	 * 
	 * @step. ^I close Contacts Upload dialog$
	 * 
	 */
	@And("^I close Contacts Upload dialog$")
	public void ICloseContactsUploadDialog() {
		WebappPagesCollection.contactsUploadPage.close();
	}

	/**
	 * Click Gmail import button on Contacts Upload dialog
	 * 
	 * @step. ^I click button to import Gmail Contacts$
	 * 
	 */
	@And("^I click button to import Gmail Contacts$")
	public void IClickButtonToImportGmailContacts() {
		WebappPagesCollection.contactsUploadPage.clickShareContactsButton();
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
		WebappPagesCollection.peoplePickerPage = WebappPagesCollection.contactsUploadPage
				.clickShowSearchButton();
	}
}
