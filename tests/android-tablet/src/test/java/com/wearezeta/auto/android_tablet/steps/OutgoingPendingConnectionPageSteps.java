package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletOutgoingPendingConnectionPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class OutgoingPendingConnectionPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletOutgoingPendingConnectionPage getOutgoingPendingConnectionPage()
			throws Exception {
		return (TabletOutgoingPendingConnectionPage) pagesCollection
				.getPage(TabletOutgoingPendingConnectionPage.class);
	}

	/**
	 * Verify whether Incoming connections page is visible
	 * 
	 * @step. ^I see (?:the |\\s*)Outgoing Pending Connection page$
	 * @throws Exception
	 */
	@Given("^I see (?:the |\\s*)Outgoing Pending Connection page$")
	public void ISeeOutgoingPendingConnectionPage() throws Exception {
		Assert.assertTrue("Outgoing pending connection page is not shown",
				getOutgoingPendingConnectionPage().waitUntilVisible());
	}

	/**
	 * Verify whether the particular name is visible on Outgoing Pending
	 * Connection page
	 * 
	 * @step. ^I see name (.*) on (?:the |\\s*)Outgoing Pending Connection page$
	 * 
	 * @param expectedName
	 *            name or alias
	 * @throws Exception
	 */
	@Then("^I see (.*) name on (?:the |\\s*)Outgoing Pending Connection page$")
	public void ISeeNamel(String expectedName) throws Exception {
		expectedName = usrMgr.findUserByNameOrNameAlias(expectedName).getName();
		Assert.assertTrue(
				String.format(
						"The name '%s' is not visible on Outgoing Pending Connection page",
						expectedName), getOutgoingPendingConnectionPage()
						.waitUntilNameVisible(expectedName));
	}
}
