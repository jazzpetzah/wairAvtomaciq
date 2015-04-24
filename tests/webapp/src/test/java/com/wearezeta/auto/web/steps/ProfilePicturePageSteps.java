package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ProfilePicturePageSteps {

	/**
	 * Emulates drop of existing picture into profile picture dialog
	 * 
	 * @step. ^I drop (.*) to profile picture dialog$
	 * 
	 * @param pictureName
	 *            the name of existing image in ~/Documents folder
	 * @throws Exception
	 */
	@When("^I drop (.*) to profile picture dialog$")
	public void IDropPicture(String pictureName) throws Exception {
		PagesCollection.profilePicturePage.dropPicture(pictureName);
	}

	/**
	 * Click confirm button on profile picture dialog
	 * 
	 * @step. ^I confirm picture selection on profile picture dialog$
	 * @throws Exception
	 */
	@And("^I confirm picture selection on profile picture dialog$")
	public void IConfirmImageSelection() throws Exception {
		PagesCollection.profilePicturePage.clickConfirmImageButton();
	}

	private static final int VISIBILITY_TIMEOUT = 3; // seconds

	/**
	 * Verifies that profile picture dialog is visible or not
	 * 
	 * @step. ^I see profile picture dialog$
	 * 
	 * @param shouldNotBeVisible
	 *            equals to null is the step does not contain "do not" part
	 * 
	 * @throws Exception
	 */
	@Then("^I( do not)? see profile picture dialog$")
	public void ISeeProfilePictureDialog(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			PagesCollection.profilePicturePage
					.waitUntilVisible(VISIBILITY_TIMEOUT);
		} else {
			PagesCollection.profilePicturePage
					.waitUntilNotVisible(VISIBILITY_TIMEOUT);
		}
	}
}
