package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletSelfProfilePage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

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

	/**
	 * Tap Options button on Self Profile page
	 * 
	 * @step. ^I tap Options button$"
	 * 
	 * @throws Exception
	 */
	@When("^I tap Options button$")
	public void ITapOptionsButton() throws Exception {
		getSelfProfilePage().tapOptionsButton();
	}

	/**
	 * Select the corresponding item from Options menu
	 * 
	 * @step. ^I select \"(.*)\" menu item$"
	 * @param itemName
	 *            the name of existing Options menu item
	 * 
	 * @throws Exception
	 */
	@When("^I select \"(.*)\" menu item$")
	public void ISelectOptionsMenuItem(String itemName) throws Exception {
		getSelfProfilePage().selectOptionsMenuItem(itemName);
	}

}
