package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.SettingsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class SelfProfilePageSteps {

	public SelfProfilePageSteps() {
	}

	/**
	 * Clicks the gear button on Self Profile page
	 * 
	 * @step. ^I click gear button on self profile page$
	 */
	@And("^I click gear button on self profile page$")
	public void IClickGearButton() {
		PagesCollection.selfProfilePage.clickGearButton();
	}

	/**
	 * Clicks the corresponding item from "gear" menu
	 * 
	 * @param name
	 *            the name of menu item
	 */
	@And("^I select (.*) menu item on self profile page$")
	public void ISelectGearMenuItem(String name) {
		PagesCollection.selfProfilePage.selectGearMenuItem(name);
	}

	/**
	 * Verifies whether settings dialog is visible
	 * 
	 * @throws AssertionError
	 *             if settings dialog is not currently visible
	 */
	@Then("^I see Settings dialog$")
	public void ISeeSetingsDialog() throws Exception {
		PagesCollection.settingsPage = new SettingsPage(
				CommonUtils
						.getWebAppAppiumUrlFromConfig(SelfProfilePageSteps.class),
				CommonUtils
						.getWebAppApplicationPathFromConfig(SelfProfilePageSteps.class));
		Assert.assertTrue(PagesCollection.settingsPage.isVisible());
	}
}
