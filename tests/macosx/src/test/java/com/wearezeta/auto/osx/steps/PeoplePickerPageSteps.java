package com.wearezeta.auto.osx.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.osx.common.SearchResultTypeEnum;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.pages.popovers.ConnectToPopover;
import com.wearezeta.auto.osx.pages.popovers.PopoverPage;
import com.wearezeta.auto.osx.pages.popovers.UnblockPopover;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;

public class PeoplePickerPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Searches for user in People Picker by his name
	 * 
	 * @step. I search for user (.*)
	 * 
	 * @param user
	 *            search string (usually user name or email)
	 * @throws Exception 
	 */
	@When("I search for user (.*)")
	public void ISearchForUser(String user) throws Exception {
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.peoplePickerPage.searchForText(user);
	}

	/**
	 * Searches for user in People Picker by his email
	 * 
	 * @step. I search by email for user (.*)
	 * 
	 * @param user
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@When("I search by email for user (.*)")
	public void ISearchByEmailForUser(String user) throws Exception {
		String email = usrMgr.findUserByNameOrNameAlias(user).getEmail();
		PagesCollection.peoplePickerPage.searchForText(email);
	}

	/**
	 * Checks that searched user appears in search results
	 * 
	 * @step. I see user (.*) in search results
	 * 
	 * @param user
	 *            user name string
	 * 
	 * @throws AssertionError
	 *             if there is no user in search results
	 */
	@When("I see user (.*) in search results")
	public void ISeeUserFromSearchResults(String user) throws Exception {
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}

		Assert.assertTrue("User " + user + " not found in results",
				PagesCollection.peoplePickerPage
						.areSearchResultsContainUser(user));
	}

	/**
	 * Selects user from search results and creates conversation with him
	 * 
	 * @step. I add user (.*) from search results
	 * 
	 * @param user
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@When("I add user (.*) from search results")
	public void IAddUserFromSearchResults(String user) throws Exception {
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}

		if (PagesCollection.peoplePickerPage.isRequiredScrollToUser(user)) {
			PagesCollection.peoplePickerPage.scrollToUserInSearchResults(user);
		}
		PagesCollection.peoplePickerPage.chooseUserInSearchResults(user,
				SearchResultTypeEnum.CONNECTION);
	}

	/**
	 * Selects user from search result for future use (connect | block | create
	 * conversation | etc)
	 * 
	 * @step. ^I select (connected|group|not connected|blocked) contact (.*)
	 *        from search results$
	 * 
	 * @param contactState
	 *            select type of search result entry: connected, group, not
	 *            connected, blocked
	 * @param user
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@Given("^I select (connected|group|not connected|blocked) contact (.*) from search results$")
	public void ISelectUserFromSearchResults(String contactState, String user)
			throws Exception {
		SearchResultTypeEnum resultType = SearchResultTypeEnum
				.getTypeByState(contactState);
		try {
			user = usrMgr.findUserByNameOrNameAlias(user).getName();
		} catch (NoSuchUserException e) {
			// ignore silently
		}
		OSXPage page = PagesCollection.peoplePickerPage
				.selectContactInSearchResults(user, resultType);
		if (page instanceof ConnectToPopover) {
			PagesCollection.popover = (PopoverPage) page;
		} else if (page instanceof UnblockPopover) {
			PagesCollection.popover = (PopoverPage) page;
		}
	}

	/**
	 * Sends connection request when connect dialog appears after user selected
	 * in search results
	 * 
	 * @step. ^I send connection request to selected user$
	 */
	@When("^I send connection request to selected user$")
	public void WhenISendInvitationToUser() {
		((ConnectToPopover) PagesCollection.popover).sendConnectionRequest();
	}

	/**
	 * Unblocks user selected in search results
	 * 
	 * @step. I unblock selected user
	 */
	@When("^I unblock selected user$")
	public void IUnblockUserInPeoplePicker() {
		((UnblockPopover) PagesCollection.popover).unblock();
	}

	/**
	 * Checks that TOP PEOPLE list shown in People Picker
	 * 
	 * @step. ^I see Top People list in People Picker$
	 * 
	 * @throws AssertionError
	 *             if TOP PEOPLE list did not appear
	 */
	@Then("^I see Top People list in People Picker$")
	public void ISeeTopPeopleListInPeoplePicker() throws Throwable {
		boolean topPeopleisVisible = PagesCollection.peoplePickerPage
				.isTopPeopleVisible();
		if (!topPeopleisVisible) {
			PagesCollection.peoplePickerPage.closePeoplePicker();
			// waiting till People Picker disappearance animation done
			Thread.sleep(1000);
			PagesCollection.contactListPage.openPeoplePicker();
			topPeopleisVisible = PagesCollection.peoplePickerPage
					.isTopPeopleVisible();
		}
		Assert.assertTrue("Top People not shown", topPeopleisVisible);
	}

	/**
	 * Selects first user from TOP PEOPLE list
	 * 
	 * @step. ^I choose person from Top People$
	 * 
	 * @throws Throwable
	 */
	@Then("^I choose person from Top People$")
	public void IChoosePersonFromTopPeople() throws Throwable {
		PagesCollection.peoplePickerPage.selectUserFromTopPeople();
	}

	/**
	 * Clicks on CREATE CONVERSATION button to create or open conversation with
	 * selected users
	 * 
	 * @step. ^I press create conversation to enter conversation$
	 * 
	 * @throws Throwable
	 */
	@Then("^I press create conversation to enter conversation$")
	public void IPressCreateConversationToEnterConversation() throws Throwable {
		PagesCollection.conversationPage = PagesCollection.peoplePickerPage
				.addSelectedUsersToConversation();
	}
}
