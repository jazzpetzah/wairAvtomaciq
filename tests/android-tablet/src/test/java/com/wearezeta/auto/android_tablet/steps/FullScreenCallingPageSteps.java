package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletFullScreenCallingOverlayPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class FullScreenCallingPageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletFullScreenCallingOverlayPage getFullScreenCallingOverlayPage()
			throws Exception {
		return (TabletFullScreenCallingOverlayPage) pagesCollection
				.getPage(TabletFullScreenCallingOverlayPage.class);
	}

	/**
	 * Check whether full screen calling overlay is visible
	 * 
	 * @step. ^I see full screen calling overlay$
	 * 
	 * @throws Exception
	 */
	@When("I see (?:\\s*|the )full screen calling overlay$")
	public void WhenISeeOverlay() throws Exception {
		Assert.assertTrue("Full screen calling overlay is not visible",
				getFullScreenCallingOverlayPage().waitUntilVisible());
	}

	/**
	 * Tap the corresponding button on call overlay
	 * 
	 * @step. ^I tap (.*) button on (?:\\s*|the )calling overlay$
	 * 
	 * @param buttonName
	 *            button name to tap. See OverlayButton enum for possible values
	 * @throws Exception
	 */
	@When("^I tap (.*) button on (?:\\s*|the )full screen calling overlay$")
	public void ITapButton(String buttonName) throws Exception {
		final OverlayButton dstButton = OverlayButton.valueOf(buttonName
				.toUpperCase());
		switch (dstButton) {
		default:
			throw new IllegalStateException(String.format(
					"This step is not implemented for '%s' button", buttonName));
		}
	}

	private static enum OverlayButton {
		ACCEPT, DISMISS, MUTE, SPEAKER;
	}

	/**
	 * Verify whether the particular button is present on full screen call
	 * overlay
	 * 
	 * @step. ^I (do not )?see (.*) button on (?:\\s*|the )full screen calling
	 *        overlay$
	 * 
	 * @param shouldNotSee
	 *            this equals to null is "not " part is not present
	 * @param buttonName
	 *            the name of a particular button. See the OverlayButton enum
	 *            for possible values
	 * @throws Exception
	 */
	@Then("^I (do not )?see (.*) button on (?:\\s*|the )full screen calling overlay$")
	public void ISeeButton(String shouldNotSee, String buttonName)
			throws Exception {
		final OverlayButton dstButton = OverlayButton.valueOf(buttonName
				.toUpperCase());
		boolean result = false;
		switch (dstButton) {
		case DISMISS:
			result = (shouldNotSee == null) ? getFullScreenCallingOverlayPage()
					.waitUntilDismissButtonVisible()
					: getFullScreenCallingOverlayPage()
							.waitUntilDismissButtonInvisible();
			break;
		default:
			throw new IllegalStateException(String.format(
					"This step is not implemented for '%s' button", buttonName));
		}
		Assert.assertTrue(String.format("The button '%s' is %svisible",
				buttonName, (shouldNotSee == null) ? "" : "in"), result);
	}

	/**
	 * Accept an incoming call from lockscreen overlay
	 * 
	 * @step. ^I accept call on (?:\\s*|the )full screen calling overlay$
	 * 
	 * @throws Exception
	 */
	@When("^I accept call on (?:\\s*|the )full screen calling overlay$")
	public void IAcceptCall() throws Exception {
		getFullScreenCallingOverlayPage().acceptCall();
	}
}
