package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
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

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

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

	/**
	 * Tap the corresponding menu item
	 *
	 * @param name the name of the corresponding menu item
	 * @throws Exception
	 * @step. ^I select \"(.*)\" settings menu item$
	 */
	@When("^I select \"(.*)\" settings menu item$")
	public void ISelectSettingsMenuItem(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
		name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.EMAIL_ALIAS);
		name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
		getSettingsPage().selectMenuItem(name);
	}

	/**
	 * Verify whether the particular settings menu item is visible or not
	 *
	 * @param shouldNotSee equals to null if the corresponding menu item should be visible
	 * @param name         the name of the corresponding menu item
	 * @throws Exception
	 * @step. ^I (do not )?see \"(.*)\" settings menu item$
	 */
	@When("^I (do not )?see \"(.*)\" settings menu item$")
	public void ISeeSettingsMenuItem(String shouldNotSee, String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.NAME_ALIAS);
		name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.EMAIL_ALIAS);
		name = usrMgr.replaceAliasesOccurences(name, ClientUsersManager.FindBy.PHONENUMBER_ALIAS);
		if (shouldNotSee == null) {
			Assert.assertTrue(String.format("Settings menu item '%s' is not visible", name),
					getSettingsPage().waitUntilMenuItemVisible(name));
		} else {
			Assert.assertTrue(String.format("Settings menu item '%s' is visible, but should be hidden", name),
					getSettingsPage().waitUntilMenuItemInvisible(name));
		}
	}

	/**
	 * Type the password into the confirmation dialog
	 *
	 * @param passwordAlias password string or an alias
	 * @throws Exception
	 * @step. ^I enter (.*) into the device removal password confirmation dialog$
	 */
	@When("^I enter (.*) into the device removal password confirmation dialog$")
	public void IEnterPassword(String passwordAlias) throws Exception {
		final String password = usrMgr.replaceAliasesOccurences(passwordAlias,
				ClientUsersManager.FindBy.PASSWORD_ALIAS);
		getSettingsPage().enterConfirmationPassword(password);
	}

	/**
	 * Tap OK button on the device removal password confirmation dialog
	 *
	 * @throws Exception
	 * @step. ^I tap OK button on the device removal password confirmation dialog$
	 */
	@And("^I tap OK button on the device removal password confirmation dialog$")
	public void ITapOKButtonOnPasswordConfirmationDialog() throws Exception {
		getSettingsPage().tapOKButtonOnPasswordConfirmationDialog();
	}

}
