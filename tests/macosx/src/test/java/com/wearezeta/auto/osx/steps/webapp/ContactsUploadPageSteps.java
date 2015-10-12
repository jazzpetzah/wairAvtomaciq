package com.wearezeta.auto.osx.steps.webapp;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.pages.external.GoogleLoginPage;
import com.wearezeta.auto.web.pages.ContactsUploadPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;

public class ContactsUploadPageSteps {
	private static final int VISIBILITY_TIMEOUT = 15; // seconds

	@SuppressWarnings("unused")
	private static final Logger LOG = ZetaLogger
			.getLog(ContactsUploadPageSteps.class.getName());

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
	 * Enter gmail at google login
	 *
	 * @step. ^I enter email (.*) at google login$
	 *
	 * @param email
	 * @throws Exception
	 */
	@When("^I enter email \"(.*)\" at google login$")
	public void ISignUpAtGoogleWithEmail(String email) throws Exception {
		GoogleLoginPage googleLoginPage = webappPagesCollection
				.getPage(GoogleLoginPage.class);
		googleLoginPage.setEmail(email);
	}

	/**
	 * Enter password at google login
	 *
	 * @step. ^I enter password (.*) at google login$
	 *
	 * @param password
	 * @throws Exception
	 */
	@When("^I enter password \"(.*)\" at google login$")
	public void ISignUpAtGoogleWithPassword(String password) throws Exception {
		GoogleLoginPage googleLoginPage = webappPagesCollection
				.getPage(GoogleLoginPage.class);
		googleLoginPage.setPassword(password);
	}

	/**
	 * Clicks next at google login
	 *
	 * @step. ^I click next at google login if present$
	 *
	 * @throws Exception
	 */
	@When("^I click next at google login if present$")
	public void IClickNextGoogle() throws Exception {
		GoogleLoginPage googleLoginPage = webappPagesCollection
				.getPage(GoogleLoginPage.class);
		if (googleLoginPage.hasNextButton()) {
			googleLoginPage.clickNext();
		} else {
			LOG.info("No next button present");
		}
	}

	/**
	 * Clicks sign in at google login
	 *
	 * @step. ^I click sign in at google login$
	 *
	 * @throws Exception
	 */
	@When("^I click sign in at google login$")
	public void IClickSignInGoogle() throws Exception {
		GoogleLoginPage googleLoginPage = webappPagesCollection
				.getPage(GoogleLoginPage.class);
		googleLoginPage.clickSignIn();
	}

	/**
	 * Clicks approve at google login
	 *
	 * @step. ^I click approve at google login if present$
	 *
	 * @throws Exception
	 */
	@When("^I click approve at google login if present$")
	public void IClickApproveGoogle() throws Exception {
		GoogleLoginPage googleLoginPage = webappPagesCollection
				.getPage(GoogleLoginPage.class);
		if (googleLoginPage.hasApproveButton()) {
			googleLoginPage.clickApprove();
		} else {
			LOG.info("No approve button present");
		}
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
