package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.ContactsUploadPage;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.And;

public class ContactsUploadPageSteps {
	private static final int VISIBILITY_TIMEOUT = 3; // seconds

	/**
	 * Verify that Contacts Upload dialog is visible
	 * 
	 * @step. ^I see Contacts Upload dialog$
	 * @throws Exception
	 */
	@And("^I see Contacts Upload dialog$")
	public void ISeeSelfPictureUpload() throws Exception {
		if (PagesCollection.contactsUploadPage == null) {
			PagesCollection.contactsUploadPage = new ContactsUploadPage(
					PagesCollection.loginPage.getDriver(),
					PagesCollection.loginPage.getWait());
		}
		PagesCollection.contactsUploadPage.waitUntilVisible(VISIBILITY_TIMEOUT);
	}

	/**
	 * Close Contacts Upload dialog
	 * 
	 * @step. ^I close Contacts Upload dialog$
	 * 
	 */
	@And("^I close Contacts Upload dialog$")
	public void IForceCarouselMode() {
		PagesCollection.contactsUploadPage.close();
	}
}
