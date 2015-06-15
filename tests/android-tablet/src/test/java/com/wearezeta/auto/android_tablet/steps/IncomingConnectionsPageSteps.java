package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletIncomingConnectionsPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class IncomingConnectionsPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletIncomingConnectionsPage getIncomingConnectionsPage()
			throws Exception {
		return (TabletIncomingConnectionsPage) pagesCollection
				.getPage(TabletIncomingConnectionsPage.class);
	}

	/**
	 * Verify whether Incoming connections page is visible
	 * 
	 * @step. ^I see the [Ii]ncoming [Cc]onnections page$
	 * @throws Exception
	 */
	@Given("^I see the [Ii]ncoming [Cc]onnections page$")
	public void ISeeIncomingConnectionsPage() throws Exception {
		Assert.assertTrue("Incoming connections page is not shown",
				getIncomingConnectionsPage().waitUntilVisible());
	}

	/**
	 * Accept incoming connection request from the particular user on the
	 * Incoming connections page
	 * 
	 * @step. ^I accept incoming connection request from (.*) on Incoming
	 *        connections page$
	 * @param name
	 *            user name/alias
	 * 
	 * @throws Exception
	 */
	@When("^I accept incoming connection request from (.*) on Incoming connections page$")
	public void ISwitchToEmailSignIn(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getIncomingConnectionsPage().acceptIncomingConnectionFrom(name);
	}
}
