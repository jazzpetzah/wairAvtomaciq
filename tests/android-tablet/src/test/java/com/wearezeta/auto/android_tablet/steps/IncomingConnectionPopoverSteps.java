package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.popovers.IncomingConnectionPopover;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class IncomingConnectionPopoverSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private IncomingConnectionPopover getIncomingConnectionPopover()
			throws Exception {
		return (IncomingConnectionPopover) pagesCollection
				.getPage(IncomingConnectionPopover.class);
	}

	/**
	 * Verify whether Incoming connection popover is visible or not
	 * 
	 * @step. ^I( do not)? see Incoming Connection popover$
	 * @param shouldNotBeVisible
	 *            equals to null is "no not" part does not exist in the step
	 * 
	 * @throws Exception
	 */
	@Then("^I( do not)? see Incoming Connection popover$")
	public void ISeePopover(String shouldNotBeVisible) throws Exception {
		if (shouldNotBeVisible == null) {
			Assert.assertTrue("Incoming connection popover is not visible",
					getIncomingConnectionPopover().waitUntilVisible());
		} else {
			Assert.assertTrue("Incoming connection popover is still visible",
					getIncomingConnectionPopover().waitUntilInvisible());
		}
	}

	/**
	 * Verifies whether the user name is visible on Incoming Connection popover
	 * 
	 * @step. ^I see the name (.*) on Incoming Connection popover$
	 * @param expectedName
	 *            name/alias, which we expect to see on the Outgoing connection
	 *            popover
	 * @throws Exception
	 */
	@When("^I see the name (.*) on Incoming Connection popover$")
	public void ISeeTheName(String expectedName) throws Exception {
		expectedName = usrMgr.replaceAliasesOccurences(expectedName,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(
				String.format(
						"The actual name on the Incoming Connection popover differs from the expected one '%s'",
						expectedName), getIncomingConnectionPopover()
						.isNameVisible(expectedName));
	}

	/**
	 * Tap the Connect button on Incoming Connection popover
	 * 
	 * @step. ^I tap Connect button on Incoming Connection popover$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Accept button on Incoming Connection popover$")
	public void ITapConnectButton() throws Exception {
		getIncomingConnectionPopover().tapAcceptButton();
	}

}
