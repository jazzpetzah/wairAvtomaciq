package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationsListPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationsListPageSteps {
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
	 * @step. ^I see (?:the |\\s*)[Cc]onversations list$
	 * 
	 * @throws Exception
	 */
	@Given("^I see (?:the |\\s*)[Cc]onversations list$")
	public void ISeeConversationsList() throws Exception {
		getConversationsListPage().verifyConversationsListIsLoaded();
	}

	/**
	 * Wait until conversations list is fully loaded and there are (no)
	 * conversations
	 * 
	 * @step. ^I see (?:the |\\s*)[Cc]onversations list with (no
	 *        )?conversations?$
	 * @param shouldBeNoConversations
	 *            is set to null if "no" part does not exist in the step
	 * 
	 * @throws Exception
	 */
	@Given("^I see (?:the |\\s*)[Cc]onversations list with (no )?conversations?$")
	public void ISeeConversationsListPlusItem(String shouldBeNoConversations)
			throws Exception {
		if (shouldBeNoConversations == null) {
			Assert.assertTrue(
					"No conversations are visible in the conversations list, but some are expected",
					getConversationsListPage()
							.isAnyConversationIsVisible());
		} else {
			Assert.assertFalse(
					"Some conversations are visible in the conversations list, but zero is expected",
					getConversationsListPage()
							.isAnyConversationIsVisible());
		}
	}

	/**
	 * Tap my own avatar on top of conversations list
	 * 
	 * @step. ^I tap my avatar on top of (?:the |\\s*)[Cc]onversations list$
	 * 
	 * @throws Exception
	 */
	@When("^I tap my avatar on top of (?:the |\\s*)[Cc]onversations list$")
	public void ITapMyAvatar() throws Exception {
		getConversationsListPage().tapMyAvatar();
	}

	/**
	 * Tap Search input field on top of Conversations list
	 * 
	 * @step. ^I tap (?:the |\\s*)Search input$
	 * 
	 * @throws Exception
	 */
	@When("^I tap (?:the |\\s*)Search input$")
	public void ITapSearchInput() throws Exception {
		getConversationsListPage().tapSearchInput();
	}

	/**
	 * Tap the corresponding conversation is the list
	 * 
	 * @step. ^I tap conversation (.*)"
	 * 
	 * @param name
	 *            conversation name
	 * @throws Exception
	 */
	@When("^I tap (?:the )conversation (.*)")
	public void ITapConversation(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		getConversationsListPage().tapConversation(name);
	}

	/**
	 * Verify whether the particular conversation is visible in the
	 * conversations list or not
	 * 
	 * @step.^I (do not )?see (?:the |\\s*)conversation (.*) in my conversations
	 *          list$
	 * @param shouldNotSee
	 *            equals to null if "do not" part is not present
	 * @param name
	 *            conversation name
	 * 
	 * @throws Exception
	 */
	@Then("^I (do not )?see (?:the |\\s*)conversation (.*) in my conversations list$")
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

	/**
	 * Verify whether the particular conversation name is silenced or not
	 * 
	 * @step. ^I see (?:the) conversation (.*) in my conversations list is (not
	 *        )?silenced$
	 * 
	 * @param name
	 *            conversation name/alias
	 * @param shouldNotBeSilenced
	 *            equals to null if the "not" part does not exist in the step
	 * @throws Exception
	 */
	@Then("^I see (?:the) conversation (.*) in my conversations list is (not )?silenced$")
	public void ISeeConversationSilenced(String name, String shouldNotBeSilenced)
			throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		if (shouldNotBeSilenced == null) {
			Assert.assertTrue(
					String.format("The conversation '%s' is not silenced", name),
					getConversationsListPage().waitUntilConversationIsSilenced(
							name));
		} else {
			Assert.assertTrue(String.format(
					"The conversation '%s' should not be silenced", name),
					getConversationsListPage()
							.waitUntilConversationIsNotSilenced(name));
		}
	}

}
