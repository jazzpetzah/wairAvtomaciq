package com.wearezeta.auto.web.steps;

import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.popovers.PeoplePopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SingleUserPopoverPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify that User profile popup is shown
	 * 
	 * @step. I see User Profile Popup Page
	 * @throws Exception
	 * 
	 */
	@When("I see User Profile Popup Page")
	public void ISeeUserProfilePopupPage() throws Exception {
		PagesCollection.popoverPage.waitUntilVisibleOrThrowException();
	}

	/**
	 * Creates conversation with selected users
	 * 
	 * @step. ^I choose to create conversation from People Picker$
	 * @throws Exception
	 */
	@When("^I choose to create conversation from People Picker$")
	public void IChooseToCreateConversationFromPeoplePicker() throws Exception {
		PagesCollection.peoplePickerPage.createConversation();
	}

	/**
	 * Verify if user is found by Search
	 * 
	 * @step. I see user (.*) found on Search
	 * 
	 * @param name
	 *            user name string
	 * @throws Exception
	 */
	@When("I see user (.*) found on Search")
	public void ISeeUserFoundOnSearch(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.peoplePickerPage.isUserFound(name));
	}

	/**
	 * Confirm removing from group chat by clicking REMOVE button
	 * 
	 * @step. ^I confirm remove from group chat$
	 * 
	 */
	@When("^I confirm remove from group chat$")
	public void IClickConfirmRemoveFromGroupChat() {
		PagesCollection.peoplePickerPage.confirmRemoveFromGroupChat();
	}

	/**
	 * Verifies that contact is displayed on Participant profile popup
	 * 
	 * @step. ^I see (.*) displayed on Participant Profile Page$
	 * 
	 * @param contactsAliases
	 * @throws Exception
	 */
	@When("^I see (.*) displayed on Participant Profile Page$")
	public void ISeeContactsDisplayedOnParticipantPopup(String contactsAliases)
			throws Exception {
		List<String> contacts = CommonSteps.splitAliases(contactsAliases);
		for (String s : contacts) {
			s = usrMgr.replaceAliasesOccurences(s, FindBy.NAME_ALIAS);
			Assert.assertTrue(PagesCollection.peoplePickerPage
					.isParticipantVisible(s));
		}
	}

	/**
	 * Click on add people button
	 * 
	 * @step. ^I click Add People button$
	 * @throws Exception
	 * 
	 */
	@When("^I click Add People button$")
	public void IClickAddPeopleButton() throws Exception {
		PagesCollection.peoplePickerPage.clickAddPeopleButton();
	}

	/**
	 * Verifies there is a question if you want to add people
	 * 
	 * @step. ^I see Add People message$
	 * 
	 */
	@When("^I see Add People message$")
	public void ISeeAddPeopleMessage() {
		Assert.assertTrue(PagesCollection.peoplePickerPage
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
		PagesCollection.peoplePickerPage.searchForUser(name);
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
		PagesCollection.peoplePickerPage.selectUserFromSearchResult(user);
	}

	/**
	 * Compares if name on User profile popup page is same as expected
	 * 
	 * @step. I see on User Profile Popup Page User username (.*)
	 * 
	 * @param name
	 *            user name string
	 */
	@When("I see on User Profile Popup Page User username (.*)")
	public void IseeUserNameOnUserProfilePage(String name) {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertEquals(name,
				PagesCollection.peoplePickerPage.getUserName());
	}

	/**
	 * Verify that Add people button is shown on User profile popup
	 * 
	 * @step. I see Add people button on User Profile Popup Page
	 * @throws Exception
	 * 
	 */
	@When("I see Add people button on User Profile Popup Page")
	public void ISeeAddPeopleButtonOnUserProfilePopupPage() throws Exception {
		Assert.assertTrue(((SingleUserPopoverContainer)PagesCollection.popoverPage)
				.isAddPeopleButtonVisible());
	}

	/**
	 * Verify that Block button is shown on User profile popup
	 * 
	 * @step. I see Block button on User Profile Popup Page
	 * @throws Exception
	 * 
	 */
	@When("I see Block button on User Profile Popup Page")
	public void ISeeBlockButtonOnUserProfilePopupPage() throws Exception {
		Assert.assertTrue(((SingleUserPopoverContainer)PagesCollection.popoverPage)
				.isBlockButtonVisible());
	}
}
