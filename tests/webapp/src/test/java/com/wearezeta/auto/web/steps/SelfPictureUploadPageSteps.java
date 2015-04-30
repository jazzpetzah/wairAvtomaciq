package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.SelfPictureUploadPage;

import cucumber.api.java.en.And;

public class SelfPictureUploadPageSteps {
	private static final int VISIBILITY_TIMEOUT = 10; // seconds

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
		if (PagesCollection.selfPictureUploadPage == null) {
			PagesCollection.selfPictureUploadPage = new SelfPictureUploadPage(
					PagesCollection.registrationPage.getDriver(),
					PagesCollection.registrationPage.getWait());
		}
		if (shouldNotBeVisible == null) {
			PagesCollection.selfPictureUploadPage
					.waitUntilButtonsAreClickable(VISIBILITY_TIMEOUT);
		} else {
			PagesCollection.selfPictureUploadPage
					.waitUntilNotVisible(VISIBILITY_TIMEOUT);
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
		PagesCollection.selfPictureUploadPage.uploadPicture(pictureName);
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
		PagesCollection.contactsUploadPage = PagesCollection.selfPictureUploadPage
				.confirmPictureSelection();
	}

	/**
	 * Force Carousel mode on Self Picture Upload dialog
	 * 
	 * @step. ^I force carousel mode on Self Picture Upload dialog$
	 * 
	 */
	@And("^I force carousel mode on Self Picture Upload dialog$")
	public void IForceCarouselMode() {
		PagesCollection.selfPictureUploadPage.forceCarouselMode();
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
		PagesCollection.selfPictureUploadPage.selectRandomPictureFromCarousel();
	}
}
