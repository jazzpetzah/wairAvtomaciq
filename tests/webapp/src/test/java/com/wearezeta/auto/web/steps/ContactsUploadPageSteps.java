package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.ContactsUploadPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.GoogleLoginPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class ContactsUploadPageSteps {
	private static final int VISIBILITY_TIMEOUT = 35; // seconds

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	/**
	 * Verify that Contacts Upload dialog is visible
	 * 
	 * @step. ^I see Contacts Upload dialog$
	 * @throws Exception
	 */
	@And("^I see Contacts Upload dialog$")
	public void ISeeContactsUploadDialog() throws Exception {
		webappPagesCollection.getPage(ContactsUploadPage.class)
				.waitUntilVisible(VISIBILITY_TIMEOUT);
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
		webappPagesCollection.getPage(ContactsUploadPage.class)
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
		GoogleLoginPage googleLoginPage = webappPagesCollection
				.getPage(GoogleLoginPage.class);
		// sometimes Google already shows the email
		googleLoginPage.setEmail(email);
		// sometimes google shows a next button and you have to enter the
		// password separately
		if (googleLoginPage.hasNextButton()) {
			googleLoginPage.clickNext();
		}
		googleLoginPage.setPassword(password);
		googleLoginPage.clickSignIn();
	}

	/**
	 * Close Contacts Upload dialog
	 * 
	 * @step. ^I close Contacts Upload dialog$
	 * 
	 */
	@And("^I close Contacts Upload dialog$")
	public void ICloseContactsUploadDialog() throws Exception {
		webappPagesCollection.getPage(ContactsUploadPage.class).close();
	}

	/**
	 * Click Gmail import button on Contacts Upload dialog
	 * 
	 * @step. ^I click button to import Gmail Contacts$
	 * 
	 */
	@And("^I click button to import Gmail Contacts$")
	public void IClickButtonToImportGmailContacts() throws Exception {
		webappPagesCollection.getPage(ContactsUploadPage.class)
				.clickShareContactsButton();
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
		webappPagesCollection.getPage(ContactsUploadPage.class)
				.clickShowSearchButton();
	}
}
