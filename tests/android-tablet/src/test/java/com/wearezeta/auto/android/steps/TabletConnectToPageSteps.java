package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ConnectToPage;
import com.wearezeta.auto.android.pages.TabletPagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;

public class TabletConnectToPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify that I see tablet connect to contact dialog
	 * 
	 * @step. ^I see tablet connect to (.*) dialog$
	 * 
	 * @param contact
	 *            contact name
	 * @throws Throwable
	 */
	@When("^I see tablet connect to (.*) dialog$")
	public void WhenISeeTabletConnectToUserDialog(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		AndroidTabletPagesCollection.connectToPage = (ConnectToPage) AndroidTabletPagesCollection.currentPage;
		Assert.assertTrue(
				String.format(
						"Connect To header with text '%s' is not visible, but should be",
						contact), AndroidTabletPagesCollection.connectToPage
						.isConnectToHeaderVisible(contact));
	}

}
