package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.WarningPage;

import cucumber.api.java.en.Then;

public class WarningPageSteps {

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	/**
	 * Verifies whether the 'missing WebRTC support warning bar' is visible
	 *
	 * @param doNot
	 *            is null when 'do not' part is not present
	 * @step. ^I see a warning$
	 *
	 * @throws Exception
	 *             if the 'missing WebRTC support warning bar' is not currently
	 *             visible
	 */
	@Then("^I( do not)? see a warning$")
	public void ISeeWarning(String doNot) throws Exception {
		if (doNot == null) {
			Assert.assertTrue(
					"Missing WebRTC support warning bar is not visible",
					webappPagesCollection.getPage(WarningPage.class)
							.isMissingWebRTCSupportWarningBarVisible());
		} else {
			Assert.assertTrue("Missing WebRTC support warning bar is visible",
					webappPagesCollection.getPage(WarningPage.class)
							.isMissingWebRTCSupportWarningBarInvisible());
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
		webappPagesCollection.getPage(WarningPage.class)
				.clickCloseMissingWebRTCSupportWarningBar();
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
		webappPagesCollection.getPage(WarningPage.class)
				.isLinkWithCaptionInMissingWebRTCSupportWarningBarVisible(
						linkCaption);
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
		if (doNot == null) {
			Assert.assertTrue("Another call warning modal is not visible",
					webappPagesCollection.getPage(WarningPage.class)
							.isAnotherCallWarningModalVisible());
		} else {
			Assert.assertTrue("Another call warning modal is visible",
					webappPagesCollection.getPage(WarningPage.class)
							.isAnotherCallWarningModalInvisible());
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
		webappPagesCollection.getPage(WarningPage.class)
				.clickCloseAnotherCallWarningModal();
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
				webappPagesCollection.getPage(WarningPage.class)
						.clickButtonWithCaptionInAnotherCallWarningModal(
								buttonCaption));
	}

	/**
	 * Verifies whether the 'full call warning modal' is visible
	 *
	 * @param doNot
	 *            is null when 'do not' part is not present
	 * @step. ^I( do not)? see full call warning modal$
	 *
	 * @throws Exception
	 *             if the 'full call warning modal' is not currently visible
	 */
	@Then("^I( do not)? see full call warning modal$")
	public void ISeeFullCallWarningModal(String doNot) throws Exception {
		if (doNot == null) {
			Assert.assertTrue("Full call warning modal is not visible",
					webappPagesCollection.getPage(WarningPage.class)
							.isFullCallWarningModalVisible());
		} else {
			Assert.assertTrue("Full call warning modal is visible",
					webappPagesCollection.getPage(WarningPage.class)
							.isFullCallWarningModalInvisible());
		}
	}

	/**
	 * Closes the 'full call warning modal'.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I close the full call warning modal$
	 *
	 */
	@Then("^I close the full call warning modal$")
	public void IClickCloseFullCallWarningModal() throws Exception {
		webappPagesCollection.getPage(WarningPage.class)
				.clickCloseFullCallWarningModal();
	}

	/**
	 * Ends the current call, closes the 'full call warning modal' and initiates
	 * the new call.
	 *
	 * @param buttonCaption
	 *            the caption of the button to click
	 * @throws java.lang.Exception
	 * @step. ^I click on \"(.*)\" button in full call warning modal$
	 *
	 */
	@Then("^I click on \"(.*)\" button in full call warning modal$")
	public void IClickButtonInFullCallWarningModal(String buttonCaption)
			throws Exception {
		Assert.assertTrue(
				"Button '" + buttonCaption
						+ "' in full call warning modal was not clickable",
				webappPagesCollection.getPage(WarningPage.class)
						.clickButtonWithCaptionInFullCallWarningModal(
								buttonCaption));
	}

}
