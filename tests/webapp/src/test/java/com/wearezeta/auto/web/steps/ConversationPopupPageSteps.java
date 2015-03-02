package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.When;

public class ConversationPopupPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Click on add people button
	 * 
	 * @step. ^I click Add People button$
	 * @throws Exception
	 * 
	 */
	@When("^I click Add People button$")
	public void IClickAddPeopleButton() throws Exception {
		PagesCollection.conversationPopupPage.clickAddPeopleButton();
	}

	/**
	 * Verifies there is a question if you want to add people
	 * 
	 * @step. ^I see Add People message$
	 * 
	 */
	@When("^I see Add People message$")
	public void ISeeAddPeopleMessage() {
		Assert.assertTrue(PagesCollection.conversationPopupPage
				.isAddPeopleMessageShown());
	}

	/**
	 * Input user name in search field
	 * 
	 * @step. ^I input user name (.*) in search field$
	 * 
	 * @param name
	 * @throws Exception
	 */
	@When("^I input user name (.*) in search field$")
	public void ISearchForUser(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		PagesCollection.conversationPopupPage.searchForUser(name);
	}

	/**
	 * Select user found in search results
	 * 
	 * @step. ^I select (.*) from Popup Page search results$
	 * 
	 * @param user
	 * @throws Exception
	 */
	@When("^I select (.*) from Popup Page search results$")
	public void ISelectUserFromPeoplePickerResults(String user)
			throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		PagesCollection.conversationPopupPage.selectUserFromSearchResult(user);
	}

	/**
	 * Creates conversation with selected users from Popup Page
	 * 
	 * @step. ^I choose to create conversation from Popup Page$
	 */
	@When("^I choose to create conversation from Popup Page$")
	public void IChooseToCreateConversationFromPopupPage() {
		PagesCollection.conversationPopupPage.clickCreateConversation();
	}

	/**
	 * Click on continue people button
	 * 
	 * @step. ^I confirm add to group chat$
	 * @throws Exception
	 * 
	 */
	@When("^I confirm add to chat$")
	public void IClickConfirmAddToChat() throws Exception {
		PagesCollection.conversationPopupPage.confirmAddPeople();
	}
}
