package com.wearezeta.auto.android_tablet.steps;

import java.util.NoSuchElementException;

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

	private static enum IncomingConnectionAction {
		ACCEPT, IGNORE;
	}

	/**
	 * Accept/ignore incoming connection request from the particular user on the
	 * Incoming connections page
	 * 
	 * @step. ^I (accept|ignore) incoming connection request from (.*) on
	 *        Incoming connections page$
	 * @param actionStr
	 *            see the IncomingConnectionAction enum for more details about
	 *            possible values
	 * @param fromUser
	 *            user name/alias
	 * 
	 * @throws Exception
	 */
	@When("^I (accept|ignore) incoming connection request from (.*) on Incoming connections page$")
	public void IHandleIncomingConnectionRequest(String actionStr,
			String fromUser) throws Exception {
		fromUser = usrMgr.findUserByNameOrNameAlias(fromUser).getName();
		final IncomingConnectionAction action = IncomingConnectionAction
				.valueOf(actionStr.toUpperCase());
		switch (action) {
		case ACCEPT:
			getIncomingConnectionsPage().acceptIncomingConnectionFrom(fromUser);
			break;
		case IGNORE:
			getIncomingConnectionsPage().ignoreIncomingConnectionFrom(fromUser);
			break;
		default:
			throw new NoSuchElementException(String.format(
					"Action '%s' is not supported yet", actionStr));
		}
	}
}
