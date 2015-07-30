package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.WarningPage;

import cucumber.api.java.en.Then;

public class WarningPageSteps {

	/**
	 * Verifies whether the 'missing WebRTC support warning bar' is visible
	 * 
	 * @param doNot
	 *            is null when 'do not' part is not present
	 * @step. ^I see a warning$
	 * 
	 * @throws Exception
	 *             if the 'missing WebRTC support warning bar' is not currently
	 *             * visible
	 */
	@Then("^I( do not)? see a warning$")
	public void ISeeWarning(String doNot) throws Exception {
		PagesCollection.warningPage = (WarningPage) PagesCollection.loginPage
				.instantiatePage(WarningPage.class);
		if (doNot == null) {
			Assert.assertTrue(
					"Missing WebRTC support warning bar is not visible",
					PagesCollection.warningPage
							.isMissingWebRTCSupportWarningBarVisible());
		} else {
			Assert.assertFalse("Missing WebRTC support warning bar is visible",
					PagesCollection.warningPage
							.isMissingWebRTCSupportWarningBarVisible());
		}
	}

	/**
	 * Closes the 'missing WebRTC support warning bar'
	 *
	 * @throws java.lang.Exception
	 * @step. ^I close the warning$
	 *
	 */
	@Then("^I close the warning$")
	public void IClickCloseMissingWebRTCSupportWarning() throws Exception {
		PagesCollection.warningPage.clickCloseMissingWebRTCSupportWarningBar();
	}

	/**
	 * Verifies presence of a link with the given caption in 'the missing WebRTC
	 * support warning bar'.
	 *
	 * @param linkCaption
	 *            the caption of the link to look for
	 * @throws java.lang.Exception
	 * @step. ^I see \"(.*)\" link in warning$
	 *
	 */
	@Then("^I see \"(.*)\" link in warning$")
	public void ISeeLinkInMessage(String linkCaption) throws Exception {
		PagesCollection.warningPage
				.isLinkWithCaptionInMissingWebRTCSupportWarningBarVisible(linkCaption);
	}

	/**
	 * Verifies whether the 'another call warning modal' is visible
	 *
	 * @param doNot
	 *            is null when 'do not' part is not present
	 * @step. ^I( do not)? see another call warning modal$
	 *
	 * @throws Exception
	 *             if the 'another call warning modal' is not currently visible
	 */
	@Then("^I( do not)? see another call warning modal$")
	public void ISeeAnotherCallWarningModal(String doNot) throws Exception {
		PagesCollection.warningPage = (WarningPage) PagesCollection.loginPage
				.instantiatePage(WarningPage.class);
		if (doNot == null) {
			Assert.assertTrue("Another call warning modal is not visible",
					PagesCollection.warningPage
							.isAnotherCallWarningModalVisible());
		} else {
			Assert.assertFalse("Another call warning modal is visible",
					PagesCollection.warningPage
							.isAnotherCallWarningModalVisible());
		}
	}

	/**
	 * Closes the 'another call warning modal'.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I close the another call warning modal$
	 *
	 */
	@Then("^I close the another call warning modal$")
	public void IClickCloseAnotherCallWarningModal() throws Exception {
		PagesCollection.warningPage.clickCloseAnotherCallWarningModal();
	}

	/**
	 * Ends the current call, closes the 'another call warning modal' and
	 * initiates the new call.
	 *
	 * @param buttonCaption
	 *            the caption of the button to click
	 * @throws java.lang.Exception
	 * @step. ^I click on \"(.*)\" button in another call warning modal$
	 *
	 */
	@Then("^I click on \"(.*)\" button in another call warning modal$")
	public void IClickButtonInAnotherCallWarningModal(String buttonCaption)
			throws Exception {
		Assert.assertTrue(
				"Button '" + buttonCaption
						+ "' in another call warning modal was not clickable",
				PagesCollection.warningPage
						.clickButtonWithCaptionInAnotherCallWarningModal(buttonCaption));
	}

}
