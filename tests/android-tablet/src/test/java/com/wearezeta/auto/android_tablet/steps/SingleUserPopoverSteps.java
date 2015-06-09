package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.popovers.SingleUserPopover;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SingleUserPopoverSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private SingleUserPopover getSingleUserPopover() throws Exception {
		return (SingleUserPopover) pagesCollection
				.getPage(SingleUserPopover.class);
	}

	/**
	 * Verifies whether single user popover page is currently visible or not
	 * 
	 * @step. ^I (do not )?see (?:the |\\s*)[Ss]ingle user popover$
	 * @param shouldNotBeVisible
	 *            equals to null if "do not" part does not exist in the step
	 * 
	 * @throws Exception
	 */
	@When("^I (do not )?see (?:the |\\s*)[Ss]ingle user popover$")
	public void ISeeThePopover(String shouldNotBeVisible) throws Exception {
		if (shouldNotBeVisible == null) {
			Assert.assertTrue(
					"The single user popover is not currently visible",
					getSingleUserPopover().waitUntilVisible());
		} else {
			Assert.assertTrue(
					"The single user popover is still visible, but should be hidden",
					getSingleUserPopover().waitUntilInvisible());
		}
	}

	/**
	 * Tap Options button on Single user popover
	 * 
	 * @step. ^I tap Options button on [Ss]ingle user popover$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Options button on [Ss]ingle user popover$")
	public void ITapOptionsButton() throws Exception {
		getSingleUserPopover().tapOptionsButton();
	}

	/**
	 * Tap the corresponding menu item name on Single user popover (the menu has
	 * to be already opened)
	 * 
	 * @step. ^I select (.*) menu item on [Ss]ingle user popover$
	 * 
	 * @param itemName
	 *            the name of menu item
	 * @throws Exception
	 */
	@When("^I select (.*) menu item on [Ss]ingle user popover$")
	public void ISelectMenuItem(String itemName) throws Exception {
		getSingleUserPopover().selectMenuItem(itemName);
	}

	/**
	 * Verify the corresponding menu item name on Single user popover is visible
	 * 
	 * @param itemName
	 *            the name of menu item to check
	 * @throws Exception
	 */
	@Then("^I see (.*) menu item on Single user popover$")
	public void ISeeMenuItem(String itemName) throws Exception {
		Assert.assertTrue(
				String.format("Menu item '%s' is not displayed", itemName),
				getSingleUserPopover().isMenuItemVisible(itemName));
	}

	/**
	 * Verify whether the particular user name is visible on Signle user popover
	 * 
	 * @step. ^I see (?:the |\\s*)user name (.*) on [Ss]ingle user popover$"
	 * 
	 * @param expectedName
	 *            user name/alias
	 * @throws Exception
	 */
	@Then("^I see (?:the |\\s*)user name (.*) on [Ss]ingle user popover$")
	public void ISeeUserName(String expectedName) throws Exception {
		expectedName = usrMgr.findUserByNameOrNameAlias(expectedName).getName();
		Assert.assertTrue(String.format(
				"The user name '%s' is not displayed on Simgle user popover",
				expectedName),
				getSingleUserPopover().waitUntilUserNameVisible(expectedName));
	}

	/**
	 * Verify whether the particular user email is visible on Signle user popover
	 * 
	 * @step. ^I see (?:the |\\s*)user email (.*) on [Ss]ingle user popover$"
	 * 
	 * @param expectedEmail
	 *            user email/alias
	 * @throws Exception
	 */
	@Then("^I see (?:the |\\s*)user email (.*) on [Ss]ingle user popover$")
	public void ISeeUserEmail(String expectedEmail) throws Exception {
		expectedEmail = usrMgr.findUserByEmailOrEmailAlias(expectedEmail)
				.getEmail();
		Assert.assertTrue(String.format(
				"The user email '%s' is not displayed on [Ss]imgle user popover",
				expectedEmail),
				getSingleUserPopover().waitUntilUserEmailVisible(expectedEmail));
	}

}
