package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.popovers.OutgoingConnectionPopover;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OutgoingConnectionPopoverSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private OutgoingConnectionPopover getOutgoingConnectionPopover()
			throws Exception {
		return (OutgoingConnectionPopover) pagesCollection
				.getPage(OutgoingConnectionPopover.class);
	}

	/**
	 * Verify whether Outgoing connection popover is visible or not
	 * 
	 * @step. ^I( do not)? see Outgoing Connection popover$
	 * @param shouldNotBeVisible
	 *            equals to null is "no not" part does not exist in the step
	 * 
	 * @throws Exception
	 */
	@Then("^I( do not)? see Outgoing Connection popover$")
	public void ISeePopover(String shouldNotBeVisible) throws Exception {
		if (shouldNotBeVisible == null) {
			Assert.assertTrue("Outgoing connection popover is not visible",
					getOutgoingConnectionPopover().waitUntilVisible());
		} else {
			Assert.assertTrue("Outgoing connection popover is still visible",
					getOutgoingConnectionPopover().waitUntilInvisible());
		}
	}

	/**
	 * Verifies whether the user name is visible on Outgoing Connection popover
	 * 
	 * @step. ^I see the name (.*) on Outgoing Connection popover$
	 * @param expectedName
	 *            name/alias, which we expect to see on the Outgoing connection
	 *            popover
	 * @throws Exception
	 */
	@When("^I see the name (.*) on Outgoing Connection popover$")
	public void ISeeTheName(String expectedName) throws Exception {
		expectedName = usrMgr.replaceAliasesOccurences(expectedName,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(
				String.format(
						"The actual name on the Outgoing Connection popover differs from the expected one '%s'",
						expectedName), getOutgoingConnectionPopover()
						.isNameVisible(expectedName));
	}

	/**
	 * Types the new connection message into Outgoing Connection popover
	 * 
	 * @step. ^I enter connection message \"(.*)\" on Outgoing Connection
	 *        popover$
	 * @param message
	 *            the invitation message to enter into outgoing connection
	 *            popover
	 * 
	 * @throws Exception
	 */
	@When("^I enter connection message \"(.*)\" on Outgoing Connection popover$")
	public void IEnterConnectionMessage(String message) throws Exception {
		getOutgoingConnectionPopover().setMessage(message);
	}

	/**
	 * Tap the Connect/Close button on Outgoing Connection popover
	 * 
	 * @step. ^I tap (Connect|Close) button on Outgoing Connection popover$
	 * 
	 * @param btnName
	 *            either 'Connect' or 'Close'
	 * 
	 * @throws Exception
	 */
	@When("^I tap (Connect|Close) button on Outgoing Connection popover$")
	public void ITapConnectButton(String btnName) throws Exception {
		switch (btnName) {
		case "Connect":
			getOutgoingConnectionPopover().tapConnectButton();
			break;
		case "Close":
			getOutgoingConnectionPopover().tapCloseButton();
			break;
		}
	}

}
