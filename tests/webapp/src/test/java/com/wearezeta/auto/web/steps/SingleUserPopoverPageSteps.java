package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import cucumber.api.java.en.When;

public class SingleUserPopoverPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify that Single User Profile popover is visible
	 * 
	 * @step. I see Single User Profile popover
	 * @throws Exception
	 * 
	 */
	@When("I see Single User Profile popover")
	public void ISeeSingleUserPopup() throws Exception {
		PagesCollection.popoverPage.waitUntilVisibleOrThrowException();
	}

	/**
	 * Creates conversation with selected users from Single User Profile popover
	 * 
	 * @step. ^I choose to create conversation from Single User Profile popover$
	 * @throws Exception
	 */
	@When("^I choose to create conversation from Single User Profile popover$")
	public void IChooseToCreateConversationFromSingleUserPopover()
			throws Exception {
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.clickCreateConversation();
	}

	/**
	 * Click on add people button on Single User Profile popover
	 * 
	 * @step. ^I click Add People button on Single User Profile popover$
	 * @throws Exception
	 * 
	 */
	@When("^I click Add People button on Single User Profile popover$")
	public void IClickAddPeopleButton() throws Exception {
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.clickAddPeopleButton();
	}

	/**
	 * Input user name in search field on Single User Profile popover
	 * 
	 * @step. ^I input user name (.*) in search field on Single User Profile
	 *        popover$
	 * 
	 * @param name
	 * @throws Exception
	 */
	@When("^I input user name (.*) in search field on Single User Profile popover$")
	public void ISearchForUser(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.searchForUser(name);
	}

	/**
	 * Select user found in search results
	 * 
	 * @step. ^I select (.*) from Single User Profile popover search results$
	 * 
	 * @param user
	 * @throws Exception
	 */
	@When("^I select (.*) from Single User Profile popover search results$")
	public void ISelectUserFromSearchResults(String user) throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		((SingleUserPopoverContainer) PagesCollection.popoverPage)
				.selectUserFromSearchResult(user);
	}

	/**
	 * Compares if name on Single User Profile popover is same as expected
	 * 
	 * @step. ^I see username (.*) on Single User Profile popover$
	 * 
	 * @param name
	 *            user name string
	 */
	@When("^I see username (.*) on Single User Profile popover$")
	public void IseeUserNameOnUserProfilePage(String name) {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertEquals(name,
				((SingleUserPopoverContainer) PagesCollection.popoverPage)
						.getUserName());
	}
}
