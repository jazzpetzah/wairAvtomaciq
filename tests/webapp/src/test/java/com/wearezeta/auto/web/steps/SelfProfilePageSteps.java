package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.SettingsPage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class SelfProfilePageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

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
	 * @step. ^I select (.*) menu item on self profile page$
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
	 * @step. ^I see Settings dialog$
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

	/**
	 * Verifies that correct user name is shown on self profile page
	 * 
	 * @step. I see user name on self profile page (.*)
	 * 
	 * @param name
	 *            name of the user
	 * 
	 * @throws NoSuchUserException
	 */
	@And("I see user name on self profile page (.*)")
	public void ISeeUserNameOnSelfProfilePage(String name)
			throws NoSuchUserException {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		String actualName = PagesCollection.selfProfilePage.getUserName();
		Assert.assertEquals(name, actualName);
	}

	/**
	 * Verifies that correct user email is shown on self profile page
	 * 
	 * @step. I see user email on self profile page (.*)
	 * 
	 * @param email
	 *            email of the user
	 * 
	 * @throws NoSuchUserException
	 */
	@And("I see user email on self profile page (.*)")
	public void ISeeUserEmailOnSelfProfilePage(String email)
			throws NoSuchUserException {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {

		}

		String actualEmail = PagesCollection.selfProfilePage.getUserMail();
		Assert.assertEquals(email, actualEmail);
	}

	/**
	 * Set new username on self profile page
	 * 
	 * @step. I change username to (.*)
	 * 
	 * @param name
	 *            new username string
	 */
	@And("I change username to (.*)")
	public void IChangeUserNameTo(String name) {
		PagesCollection.selfProfilePage.setUserName(name);
	}
}
