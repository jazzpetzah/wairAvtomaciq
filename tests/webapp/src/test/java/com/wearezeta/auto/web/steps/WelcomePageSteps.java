package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.WelcomePage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class WelcomePageSteps {
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	/**
	 * Verify that Welcome page with Picture upload is visible or not
	 * 
	 * @step. ^I( do not)? see Welcome page$
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part does not exist
	 * @throws Exception
	 */
	@And("^I( do not)? see Welcome page$")
	public void ISeeWelcomePage(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			webappPagesCollection.getPage(WelcomePage.class)
					.waitUntilButtonsAreClickable();
		} else {
			webappPagesCollection.getPage(WelcomePage.class)
					.waitUntilNotVisible();
		}
	}

	/**
	 * Upload a picture from local file system. The picture file itself should
	 * already exist on the local file system and should be located in the
	 * ~/Documents folder
	 * 
	 * @step. ^I choose (.*) as my self picture on Welcome page$
	 * 
	 * @param pictureName
	 *            existing picture name
	 * 
	 * @throws Exception
	 */
	@And("^I choose (.*) as my self picture on Welcome page$")
	public void IUploadMyPicture(String pictureName) throws Exception {
		webappPagesCollection.getPage(WelcomePage.class)
				.uploadPicture(pictureName);
	}

	/**
	 * Confirm current picture
	 * 
	 * @step. ^I confirm keeping picture on Welcome page$
	 * 
	 * @throws Exception
	 */
	@And("^I confirm keeping picture on Welcome page$")
	public void IConfirmPicture() throws Exception {
		webappPagesCollection.getPage(WelcomePage.class)
				.keepPicture();
	}

	/**
	 * Wait for Self Picture Upload dialog to vanish
	 * 
	 * @step. ^I wait for Self Picture Upload dialog to vanish$
	 * 
	 * @throws Exception
	 */
	@When("^I wait for Self Picture Upload dialog to vanish$")
	public void IWaitForPictureDialogToVanish() throws Exception {
		webappPagesCollection.getPage(WelcomePage.class)
				.waitUntilNotVisible();
	}
}
