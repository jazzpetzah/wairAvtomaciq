package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Selects user from search results in People Picker
	 * 
	 * @step. ^I select (.*) from People Picker results$
	 * 
	 * @param user
	 *            user name or email
	 * @throws Exception
	 */
	@When("^I select (.*) from People Picker results$")
	public void ISelectUserFromPeoplePickerResults(String user)
			throws Exception {
		user = usrMgr.replaceAliasesOccurences(user, FindBy.NAME_ALIAS);
		PagesCollection.peoplePickerPage.selectUserFromSearchResult(user);
	}

	/**
	 * Input user name in search field of People Picker
	 * 
	 * @step. ^I input user name (.*) in search field of People Picker$
	 * 
	 * @param name
	 * @throws Exception
	 */
	@When("^I input user name (.*) in search field of People Picker$")
	public void ISearchForUser(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		PagesCollection.peoplePickerPage.searchForUser(name);
	}

	/**
	 * Verify if user is found by Search in People Picker
	 * 
	 * @step. I see user (.*) found in People Picker
	 * 
	 * @param name
	 *            user name string
	 * @throws Exception
	 */
	@When("I see user (.*) found in People Picker")
	public void ISeeUserFoundInPeoplePicker(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.peoplePickerPage.isUserFound(name));
	}

	/**
	 * Clicks on user found by search to open connect dialog
	 * 
	 * @step. I click on not connected user (.*) found in People Picker
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@When("I click on not connected user (.*) found in People Picker")
	public void IClickNotConnecteUserFoundInPeoplePicker(String name)
			throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		PagesCollection.popoverPage = PagesCollection.peoplePickerPage
				.clickNotConnectedUserName(name);
	}

	/**
	 * Creates conversation with users selected in People Picker
	 * 
	 * @step. ^I choose to create conversation from People Picker$
	 * @throws Exception
	 */
	@When("^I choose to create conversation from People Picker$")
	public void IChooseToCreateConversationFromPeoplePicker() throws Exception {
		PagesCollection.peoplePickerPage.createConversation();
	}
}
