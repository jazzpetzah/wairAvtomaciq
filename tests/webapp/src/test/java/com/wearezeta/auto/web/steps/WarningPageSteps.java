package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import org.junit.Assert;

import com.wearezeta.auto.web.pages.WarningPage;

import cucumber.api.java.en.Then;

public class WarningPageSteps {

        private final TestContext context;

    public WarningPageSteps() {
        this.context = new TestContext();
    }
        
    public WarningPageSteps(TestContext context) {
        this.context = context;
    }

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
					context.getPagesCollection().getPage(WarningPage.class)
							.isMissingWebRTCSupportWarningBarVisible());
		} else {
			Assert.assertTrue("Missing WebRTC support warning bar is visible",
					context.getPagesCollection().getPage(WarningPage.class)
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
		context.getPagesCollection().getPage(WarningPage.class)
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
		context.getPagesCollection().getPage(WarningPage.class)
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
					context.getPagesCollection().getPage(WarningPage.class)
							.isAnotherCallWarningModalVisible());
		} else {
			Assert.assertTrue("Another call warning modal is visible",
					context.getPagesCollection().getPage(WarningPage.class)
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
		context.getPagesCollection().getPage(WarningPage.class)
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
				context.getPagesCollection().getPage(WarningPage.class)
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
					context.getPagesCollection().getPage(WarningPage.class)
							.isFullCallWarningModalVisible());
		} else {
			Assert.assertTrue("Full call warning modal is visible",
					context.getPagesCollection().getPage(WarningPage.class)
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
		context.getPagesCollection().getPage(WarningPage.class)
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
				context.getPagesCollection().getPage(WarningPage.class)
						.clickButtonWithCaptionInFullCallWarningModal(
								buttonCaption));
	}
        
        /**
	 * Verifies whether the 'full call conversation warning modal' is visible
	 *
	 * @param doNot
	 *            is null when 'do not' part is not present
	 * @step. ^I( do not)? see full call conversation warning modal$
	 *
	 * @throws Exception
	 *             if the 'full call conversation warning modal' is not currently visible
	 */
	@Then("^I( do not)? see full call conversation warning modal$")
	public void ISeeFullCallConversationWarningModal(String doNot) throws Exception {
		if (doNot == null) {
			Assert.assertTrue("Full call conversation warning modal is not visible",
					context.getPagesCollection().getPage(WarningPage.class)
							.isFullConversationWarningModalVisible());
		} else {
			Assert.assertTrue("Full call conversation warning modal is visible",
					context.getPagesCollection().getPage(WarningPage.class)
							.isFullConversationWarningModalInvisible());
		}
	}

	/**
	 * Closes the 'full call conversation warning modal'.
	 *
	 * @throws java.lang.Exception
	 * @step. ^I close the full call conversation warning modal$
	 *
	 */
	@Then("^I close the full call conversation warning modal$")
	public void IClickCloseFullCallConversationWarningModal() throws Exception {
		context.getPagesCollection().getPage(WarningPage.class)
				.clickCloseFullConversationWarningModal();
	}

	/**
	 * Ends the current call, closes the 'full call conversation warning modal' and initiates
	 * the new call.
	 *
	 * @param buttonCaption
	 *            the caption of the button to click
	 * @throws java.lang.Exception
	 * @step. ^I click on \"(.*)\" button in full call conversation warning modal$
	 *
	 */
	@Then("^I click on \"(.*)\" button in full call conversation warning modal$")
	public void IClickButtonInFullCallConversationWarningModal(String buttonCaption)
			throws Exception {
		Assert.assertTrue(
				"Button '" + buttonCaption
						+ "' in full call conversation warning modal was not clickable",
				context.getPagesCollection().getPage(WarningPage.class)
						.clickButtonWithCaptionInFullConversationWarningModal(buttonCaption));
	}

	@Then("^I( do not)? see file transfer limit warning modal$")
	public void ISeeFileTransferLimitWarningModal(String doNot) throws Exception {
		if (doNot == null) {
			Assert.assertTrue("File transfer limit warning modal is not visible",
					context.getPagesCollection().getPage(WarningPage.class)
							.isFileTransferLimitWarningModalVisible());
		} else {
			Assert.assertTrue("File transfer limit warning modal is visible",
					context.getPagesCollection().getPage(WarningPage.class)
							.isFileTransferLimitWarningModalInvisible());
		}
	}

	@Then("^I click on \"(.*)\" button in file transfer limit warning modal$")
	public void IClickButtonInFileTransferLimitWarningModal(String buttonCaption)
			throws Exception {
		context.getPagesCollection().getPage(WarningPage.class).clickOKInFileTransferLimitWarningModal();
	}

	@Then("^I( do not)? see full house warning modal$")
	public void ISeeFullHouseWarningModal(String doNot) throws Exception {
		if (doNot == null) {
			Assert.assertTrue("Full house warning modal is not visible",
					context.getPagesCollection().getPage(WarningPage.class).isFullHouseWarningModalVisible());
		} else {
			Assert.assertTrue("Full house warning modal is visible",
					context.getPagesCollection().getPage(WarningPage.class).isFullHouseWarningModalInvisible());
		}
	}

	@Then("^I click on close button in full house warning modal$")
	public void IClickButtonInFileTransferLimitWarningModal() throws Exception {
		context.getPagesCollection().getPage(WarningPage.class).clickCloseOnFullHouseWarningModal();
	}
}
