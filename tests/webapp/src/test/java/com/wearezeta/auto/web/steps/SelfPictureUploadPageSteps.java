package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.SelfPictureUploadPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class SelfPictureUploadPageSteps {
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	/**
	 * Verify that Self Picture Upload dialog is visible or not
	 * 
	 * @step. ^I( do not)? see Self Picture Upload dialog$
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part does not exist
	 * @throws Exception
	 */
	@And("^I( do not)? see Self Picture Upload dialog$")
	public void ISeeSelfPictureUpload(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			webappPagesCollection.getPage(SelfPictureUploadPage.class)
					.waitUntilButtonsAreClickable();
		} else {
			webappPagesCollection.getPage(SelfPictureUploadPage.class)
					.waitUntilNotVisible();
		}
	}

	/**
	 * Upload a picture from local file system. The picture file itself should
	 * already exist on the local file system and should be located in the
	 * ~/Documents folder
	 * 
	 * @step. ^I choose (.*) as my self picture on Self Picture Upload dialog$
	 * 
	 * @param pictureName
	 *            existing picture name
	 * 
	 * @throws Exception
	 */
	@And("^I choose (.*) as my self picture on Self Picture Upload dialog$")
	public void IUploadMyPicture(String pictureName) throws Exception {
		webappPagesCollection.getPage(SelfPictureUploadPage.class)
				.uploadPicture(pictureName);
	}

	/**
	 * Confirm picture upload
	 * 
	 * @step. ^I confirm picture selection on Self Picture Upload dialog$
	 * 
	 * @throws Exception
	 */
	@And("^I confirm picture selection on Self Picture Upload dialog$")
	public void IConfirmPictureSelection() throws Exception {
		webappPagesCollection.getPage(SelfPictureUploadPage.class)
				.confirmPictureSelection();
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
		webappPagesCollection.getPage(SelfPictureUploadPage.class)
				.waitUntilNotVisible();
	}

	/**
	 * Force Carousel mode on Self Picture Upload dialog
	 * 
	 * @step. ^I force carousel mode on Self Picture Upload dialog$
	 * @throws Exception
	 * 
	 */
	@And("^I force carousel mode on Self Picture Upload dialog$")
	public void IForceCarouselMode() throws Exception {
		webappPagesCollection.getPage(SelfPictureUploadPage.class)
				.forceCarouselMode();
	}

	/**
	 * Select random picture from carousel on Self Picture Upload dialog
	 * 
	 * @step. ^I select random picture from carousel on Self Picture Upload
	 *        dialog$
	 * 
	 * @throws Exception
	 */
	@And("^I select random picture from carousel on Self Picture Upload dialog$")
	public void ISelectRandomPictureFromCariouusel() throws Exception {
		webappPagesCollection.getPage(SelfPictureUploadPage.class)
				.selectRandomPictureFromCarousel();
	}
}
