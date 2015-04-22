package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.SelfPictureUploadPage;

import cucumber.api.java.en.And;

public class SelfPictureUploadPageSteps {
	private static final int VISIBILITY_TIMEOUT = 10; // seconds

	/**
	 * Verify that Self Picture Upload dialog is visible
	 * 
	 * @step. ^I see Self Picture Upload dialog$
	 * @throws Exception
	 */
	@And("^I see Self Picture Upload dialog$")
	public void ISeeSelfPictureUpload() throws Exception {
		if (PagesCollection.selfPictureUploadPage == null) {
			PagesCollection.selfPictureUploadPage = new SelfPictureUploadPage(
					PagesCollection.loginPage.getDriver(),
					PagesCollection.loginPage.getWait());
		}
		PagesCollection.selfPictureUploadPage
				.waitUntilVisible(VISIBILITY_TIMEOUT);
	}

	/**
	 * Verify that Self Picture Upload dialog is not visible
	 * 
	 * @step. ^I do not see Self Picture Upload dialog$
	 * @throws Exception
	 */
	@And("^I do not see Self Picture Upload dialog$")
	public void IDontSeeSelfPictureUpload() throws Exception {
		try {
			PagesCollection.selfPictureUploadPage
					.waitUntilVisible(VISIBILITY_TIMEOUT);
		} catch (AssertionError e) {
			// Everything is ok, the page is not visible
			return;
		}
		throw new AssertionError(
				"Self Picture Upload page should noit be visible");
	}

	/**
	 * Upload a picture from local file system. The picture file itself should
	 * already exist on the local file system and should be located in the
	 * ~/Documents folder
	 * 
	 * @step. ^I choose (.*) as my self picture on Self Picture Upload dialog$
	 * 
	 * @param name
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
	 * @param name
	 *            existing picture name
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
