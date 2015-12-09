package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletSettingsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class SettingsPageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletSettingsPage getSettingsPage() throws Exception {
		return pagesCollection.getPage(TabletSettingsPage.class);
	}

	/**
	 * Verify whether Settings page is visible
	 * 
	 * @step. ^I see [Ss]ettings page$
	 * @throws Exception
	 */
	@Given("^I see [Ss]ettings page$")
	public void GivenISeeSettingsPage() throws Exception {
		Assert.assertTrue("Settings page is not shown", getSettingsPage()
				.waitUntilVisible());
	}

	/**
	 * Select the corresponding item from the menu
	 * 
	 * @step. ^I select \"(.*)\" menu item on (?:the |\\s*)[Ss]ettings page$
	 * @param itemName
	 *            the name of existing menu item
	 * 
	 * @throws Exception
	 */
	@When("^I select \"(.*)\" menu item on (?:the |\\s*)[Ss]ettings page$")
	public void ISelectMenuItem(String itemName) throws Exception {
		getSettingsPage().selectMenuItem(itemName);
	}

	/**
	 * Confirm logout alert
	 * 
	 * @step. ^I confirm logout on (?:the |\\s*)[Ss]ettings page$
	 * 
	 * @throws Exception
	 */
	@And("^I confirm logout on (?:the |\\s*)[Ss]ettings page$")
	public void IConfirmLogout() throws Exception {
		getSettingsPage().confirmLogout();
	}

}
