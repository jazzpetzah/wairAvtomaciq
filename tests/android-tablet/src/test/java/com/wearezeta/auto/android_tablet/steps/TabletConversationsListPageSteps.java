package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationsListPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TabletConversationsListPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletConversationsListPage getConversationsListPage()
			throws Exception {
		return (TabletConversationsListPage) pagesCollection
				.getPage(TabletConversationsListPage.class);
	}

	/**
	 * Wait until conversations list is fully loaded and successful load
	 * 
	 * @step. ^I see ?(the )[Cc]onversations list$
	 * 
	 * @throws Exception
	 */
	@When("^I see ?(the )[Cc]onversations list$")
	public void ISeeConversationsList() throws Exception {
		getConversationsListPage().verifyConversationsListIsLoaded();
	}

	/**
	 * Tap my own avatar on top of conversations list
	 * 
	 * @step. ^I tap my avatar$
	 * 
	 * @throws Exception
	 */
	@When("^I tap my avatar$")
	public void ITapMyAvatar() throws Exception {
		getConversationsListPage().tapMyAvatar();
	}

	/**
	 * Tap Search input field on top of Conversations list
	 * 
	 * @step. ^I tap Search input$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Search input$")
	public void ITapSearchInput() throws Exception {
		getConversationsListPage().tapSearchInput();
	}

	/**
	 * Verify whether the particular conversation is visible in the
	 * conversations list or not
	 * 
	 * @step.^I (do not )?see the conversation (.*) in my conversations list$
	 * @param shouldNotSee
	 *            equals to null if "do not" part is not present
	 * @param name
	 *            conversation name
	 * 
	 * @throws Exception
	 */
	@Then("^I (do not )?see the conversation (.*) in my conversations list$")
	public void ISeeOrNotTheConversation(String shouldNotSee, String name)
			throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		if (shouldNotSee == null) {
			Assert.assertTrue(String.format(
					"There is no '%s' conversation in the conversations list",
					name), getConversationsListPage()
					.waitUntilConversationIsVisible(name));
		} else {
			Assert.assertTrue(
					String.format(
							"The conversation '%s' should not exist in the conversations list",
							name), getConversationsListPage()
							.waitUntilConversationIsInvisible(name));
		}
	}
}
