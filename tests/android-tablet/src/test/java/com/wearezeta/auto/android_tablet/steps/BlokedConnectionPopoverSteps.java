package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.popovers.BlockedConnectionPopover;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class BlokedConnectionPopoverSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private BlockedConnectionPopover getBlockedConnectionPopover()
			throws Exception {
		return (BlockedConnectionPopover) pagesCollection
				.getPage(BlockedConnectionPopover.class);
	}

	/**
	 * Verify whether Blocked connection popover is visible or not
	 * 
	 * @step. ^I( do not)? see Blocked Connection popover$
	 * @param shouldNotBeVisible
	 *            equals to null is "no not" part does not exist in the step
	 * 
	 * @throws Exception
	 */
	@Then("^I( do not)? see Blocked Connection popover$")
	public void ISeePopover(String shouldNotBeVisible) throws Exception {
		if (shouldNotBeVisible == null) {
			Assert.assertTrue("Blocked connection popover is not visible",
					getBlockedConnectionPopover().waitUntilVisible());
		} else {
			Assert.assertTrue("Blocked connection popover is still visible",
					getBlockedConnectionPopover().waitUntilInvisible());
		}
	}

	/**
	 * Tap the Unblock button on Blocked Connection popover
	 * 
	 * @step. ^I tap Unblock button on Blocked Connection popover$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Unblock button on Blocked Connection popover$")
	public void ITapUnblockButton() throws Exception {
		getBlockedConnectionPopover().tapUnblockButton();
	}

}
