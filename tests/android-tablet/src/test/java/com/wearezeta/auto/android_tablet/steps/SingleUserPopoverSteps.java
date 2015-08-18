package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.popovers.SingleUserPopover;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.And;
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
					"The Single user popover is not currently visible",
					getSingleUserPopover().waitUntilVisible());
		} else {
			Assert.assertTrue(
					"The Single user popover is still visible, but should be hidden",
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
	 * Verify whether the particular user email is visible on Signle user
	 * popover
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
		Assert.assertTrue(
				String.format(
						"The user email '%s' is not displayed on [Ss]imgle user popover",
						expectedEmail), getSingleUserPopover()
						.waitUntilUserEmailVisible(expectedEmail));
	}

	/**
	 * Tap the Add People button on Single user popover
	 * 
	 * @step. ^I tap Add People button on [Ss]ingle user popover$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Add People button on [Ss]ingle user popover$")
	public void ITapAddPeople() throws Exception {
		getSingleUserPopover().tapAddPeopleButton();
	}

	/**
	 * Tap Close button on the popover
	 * 
	 * @step. ^I tap Close button on [Ss]ingle user popover$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Close button on [Ss]ingle user popover$")
	public void ITapCloseButton() throws Exception {
		getSingleUserPopover().tapCloseButton();
	}

	/**
	 * Tap in the center of popover
	 * 
	 * @step. ^I tap in the center of [Ss]ingle user popover$
	 * 
	 * @throws Exception
	 */
	@When("^I tap in the center of [Ss]ingle user popover$")
	public void ITapInTheCenterOfPopover() throws Exception {
		getSingleUserPopover().tapInTheCenter();
	}

	/**
	 * Enter the given text into search input on Single user popover
	 * 
	 * @step. ^I enter \"(.*)\" into (?:the |\\s*)Search input on [Ss]ingle user
	 *        popover$
	 * 
	 * @param text
	 *            the text to enter into the search field. Could contain user
	 *            name/email aliases
	 * @throws Exception
	 */
	@When("^I enter \"(.*)\" into (?:the |\\s*)Search input on [Ss]ingle user popover$")
	public void IEnterSearchText(String text) throws Exception {
		text = usrMgr.replaceAliasesOccurences(text, FindBy.NAME_ALIAS);
		text = usrMgr.replaceAliasesOccurences(text, FindBy.EMAIL_ALIAS);
		getSingleUserPopover().enterSearchText(text);
	}

	/**
	 * Tap the avatar of user, who is found in search resulsts on Single user
	 * popover
	 * 
	 * @step. ^I tap (?:the |\\s*)avatar of (.*) in search results on [Ss]ingle
	 *        user popover$
	 * 
	 * @param name
	 *            user name/alias
	 * @throws Exception
	 */
	@And("^I tap (?:the |\\s*)avatar of (.*) in search results on [Ss]ingle user popover$")
	public void ITapAvatarFromSearch(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getSingleUserPopover().tapAvatarFromSearchResults(name);
	}

	/**
	 * Tap the Add To Conversation button on Single user popover. This step
	 * expectsm that you already have at least one avatar selected in search
	 * results
	 * 
	 * @step. ^I tap (?:the |\\s*)Add To Conversation button on [Ss]ingle user
	 *        popover$
	 * 
	 * @throws Exception
	 */
	@And("^I tap (?:the |\\s*)Add To Conversation button on [Ss]ingle user popover$")
	public void ITapAddToConversationButton() throws Exception {
		getSingleUserPopover().tapAddToConversationButton();
	}

	/**
	 * Tap outside the popover
	 * 
	 * @step. ^I tap outside of [Ss]ingle user popover$
	 * 
	 * @throws Exception
	 */
	@When("^I tap outside of [Ss]ingle user popover$")
	public void ITapOutside() throws Exception {
		getSingleUserPopover().tapOutside();
	}

}
