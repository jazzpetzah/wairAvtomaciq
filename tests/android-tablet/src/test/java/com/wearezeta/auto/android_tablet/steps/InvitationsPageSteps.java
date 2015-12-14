package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletInvitationsPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class InvitationsPageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private TabletInvitationsPage getInvitationsPage() throws Exception {
		return pagesCollection.getPage(TabletInvitationsPage.class);
	}

	/**
	 * Verify that a particular user is visible or not in the invites list
	 *
	 * @param alias user alias
	 * @param shouldNotSee equals to null if the user should be visible
	 * @throws Exception
	 * @step. ^I see (.*) in the invites list$
	 */
	@Then("^I( do not)? see (.*) in the invites list$")
	public void ISeeUser(String shouldNotSee, String alias) throws Exception {
		final String name = usrMgr.findUserByNameOrNameAlias(alias).getName();
		if (shouldNotSee == null) {
			Assert.assertTrue(String.format("User '%s' is not visible on invites page", name),
					getInvitationsPage().waitUntilUserNameIsVisible(name));
		} else {
			Assert.assertTrue(String.format("User '%s' is visible on invites page", name),
					getInvitationsPage().waitUntilUserNameIsInvisible(name));
		}
	}
}
