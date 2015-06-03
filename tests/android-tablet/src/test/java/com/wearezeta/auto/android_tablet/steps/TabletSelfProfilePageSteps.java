package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletSelfProfilePage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;

public class TabletSelfProfilePageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletSelfProfilePage getSelfProfilePage() throws Exception {
		return (TabletSelfProfilePage) pagesCollection
				.getPage(TabletSelfProfilePage.class);
	}

	/**
	 * Verify that self name is visible on Self Profile page
	 * 
	 * @step. ^I see my name on Self Profile page$"
	 * 
	 * @throws Exception
	 */
	@Then("^I see my name on Self Profile page$")
	public void ISeeMyName() throws Exception {
		final String name = usrMgr.getSelfUserOrThrowError().getName();
		Assert.assertTrue(String.format(
				"Self name '%s' is not visible on Self Profile page", name),
				getSelfProfilePage().isNameVisible(name));
	}
}
