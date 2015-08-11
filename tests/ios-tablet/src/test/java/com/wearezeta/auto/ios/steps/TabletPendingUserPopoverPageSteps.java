package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.TabletPendingUserPopoverPage;

import cucumber.api.java.en.When;

public class TabletPendingUserPopoverPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private TabletPendingUserPopoverPage getTabletPendingUserPopoverPage()
			throws Exception {
		return (TabletPendingUserPopoverPage) pagesCollecton
				.getPage(TabletPendingUserPopoverPage.class);
	}

	/**
	 * Verifying pending profile popover on iPad
	 * 
	 * @step ^I see (.*) user pending profile popover on iPad$
	 * 
	 * @param user
	 *            usern from Examples
	 * 
	 * @throws NoSuchUserException
	 * @throws Exception
	 */
	@When("^I see (.*) user pending profile popover on iPad$")
	public void IseeUserPendingPopoverOnIpad(String user)
			throws NoSuchUserException, Exception {
		Assert.assertTrue(
				"User name is not displayed",
				getTabletPendingUserPopoverPage().isUserNameDisplayed(
						usrMgr.findUserByNameOrNameAlias(user).getName()));
		Assert.assertTrue("Pending label is not displayed",
				getTabletPendingUserPopoverPage().isPendingLabelVisible());
		// TODO Above method is about connection text verification. Currently
		// Pending label is missing(bug issue). Once it is back method should be
		// updated.
	}

	/**
	 * Verify presence of incoming pending popover for particular user
	 * 
	 * @step. I see incoming pending popover from user (.*) on iPad$
	 * 
	 * @param user
	 *            username String
	 * 
	 * @throws NoSuchUserException
	 * @throws Exception
	 */
	@When("^I see incoming pending popover from user (.*) on iPad$")
	public void ISeeIncomingPendingPopoverOnIpad(String user)
			throws NoSuchUserException, Exception {
		Assert.assertTrue(
				"User name is not displayed",
				getTabletPendingUserPopoverPage().isUserNameDisplayed(
						usrMgr.findUserByNameOrNameAlias(user).getName()));
		Assert.assertTrue("Connect button is not shown",
				getTabletPendingUserPopoverPage().isConnectButtonDisplayed());
	}

}
