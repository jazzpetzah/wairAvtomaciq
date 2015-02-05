package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Search for user in People Picker
	 * 
	 * @step. ^I search for (.*) in People Picker$
	 * 
	 * @param user
	 *            user name or email
	 */
	@When("^I search for (.*) in People Picker$")
	public void ISearchForUserInPeoplePicker(String user) {
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getEmail();
		} catch (NoSuchUserException e) {
		}
		PagesCollection.peoplePickerPage.searchForUser(user);
	}

	/**
	 * Selects user from search results in People Picker
	 * 
	 * @step. ^I select (.*) from People Picker results$
	 * 
	 * @param user
	 *            user name or email
	 */
	@When("^I select (.*) from People Picker results$")
	public void ISelectUserFromPeoplePickerResults(String user) {
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		PagesCollection.peoplePickerPage.selectUserFromSearchResult(user);
	}

	/**
	 * Creates conversation with selected users
	 * 
	 * @step. ^I choose to create conversation from People Picker$
	 */
	@When("^I choose to create conversation from People Picker$")
	public void IChooseToCreateConversationFromPeoplePicker() {
		PagesCollection.peoplePickerPage.createConversation();
	}
}
