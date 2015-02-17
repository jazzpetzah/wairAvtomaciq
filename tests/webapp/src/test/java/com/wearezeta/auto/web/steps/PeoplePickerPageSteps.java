package com.wearezeta.auto.web.steps;

import org.junit.Assert;

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
		
		// waiting till status of found contact will be changed to connection
		// requires further investigation (possible defect)
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
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

	/**
	 * Clicks on user found by search to open connect dialog
	 * 
	 * @step. I click on not connected user (.*) found by Search
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@When("I click on not connected user (.*) found by Search")
	public void IClickNotConnecteUserOnSearch(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		PagesCollection.connectToPopupPage = PagesCollection.peoplePickerPage
				.clickNotConnectedUserName(name);
	}

	/**
	 * Verify if user is found by Search
	 * 
	 * @step. I see user (.*) found on Search
	 * 
	 * @param name
	 *            user name string
	 */
	@When("I see user (.*) found on Search")
	public void ISeeUserFoundOnSearch(String name) {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.peoplePickerPage.isUserFound(name));
	}
}
