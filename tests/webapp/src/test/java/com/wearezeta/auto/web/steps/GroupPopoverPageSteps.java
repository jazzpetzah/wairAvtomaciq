package com.wearezeta.auto.web.steps;

import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupPopoverPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Search for user in People Picker
	 * 
	 * @step. ^I search for (.*) in People Picker$
	 * 
	 * @param user
	 *            user name or email
	 * @throws Exception
	 */
	@When("^I search for (.*) in People Picker$")
	public void ISearchForUserInPeoplePicker(String user) throws Exception {
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
	 * @throws Exception
	 */
	@When("I see user (.*) found on Search")
	public void ISeeUserFoundOnSearch(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.peoplePickerPage.isUserFound(name));
	}

	/**
	 * Verify that participant profile popup is shown
	 * 
	 * @step. ^I see Participant Profile Popup Page$
	 * @throws Exception
	 * 
	 */
	@When("^I see Participant Profile Popup Page$")
	public void ISeeUserProfilePopupPage() throws Exception {
		PagesCollection.popoverPage.waitUntilVisibleOrThrowException();
	}

	/**
	 * Click on leave group chat button in participant profile popup
	 * 
	 * @step. ^I click leave group chat$
	 * 
	 */
	@When("^I click leave group chat$")
	public void IClickLeaveGroupChat() {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickLeaveGroupChat();
	}

	/**
	 * Confirm leaving group chat by clicking LEAVE button
	 * 
	 * @step. ^I confirm leave group chat$
	 * @throws Exception
	 * 
	 */
	@When("^I confirm leave group chat$")
	public void IClickConfirmLeaveGroupChat() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.confirmLeaveGroupChat();
	}

	/**
	 * Click on a participant in participant profile popup
	 * 
	 * @step. ^I click on participant (.*)$
	 * 
	 * @param name
	 *            user name string
	 * @throws Exception
	 */
	@When("^I click on participant (.*)$")
	public void IClickOnParticipant(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickOnParticipant(name);
	}

	/**
	 * Remove participant from group chat by clicking - button
	 * 
	 * @step. ^I remove user from group chat$
	 * @throws Exception
	 * 
	 */
	@When("^I remove user from group chat$")
	public void IRemoveUserFromGroupChat() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.clickRemoveFromGroupChat();
	}

	/**
	 * Confirm removing from group chat by clicking REMOVE button
	 * 
	 * @step. ^I confirm remove from group chat$
	 * @throws Exception 
	 * 
	 */
	@When("^I confirm remove from group chat$")
	public void IClickConfirmRemoveFromGroupChat() throws Exception {
		((GroupPopoverContainer) PagesCollection.popoverPage)
				.confirmRemoveFromGroupChat();
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
	 * Set new title for converstaion in participants popup
	 * 
	 * @step. I change group conversation title to (.*)
	 * 
	 * @param title
	 *            new conversation title string
	 */
	@When("I change group conversation title to (.*)")
	public void IChangeGroupChatTitleTo(String title) {
		PagesCollection.peoplePickerPage.setConversationTitle(title);
	}

	/**
	 * Verify conversation title in participants popup
	 * 
	 * @step. ^I see conversation title (.*) in Participants profile$
	 * 
	 * @param title
	 *            expected title string
	 */
	@Then("^I see conversation title (.*) in Participants profile$")
	public void ISeeConversationTitle(String title) {
		Assert.assertEquals(title,
				PagesCollection.peoplePickerPage.getConversationTitle());
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
	 * Creates conversation with selected users from Popup Page
	 * 
	 * @step. ^I choose to create conversation from Popup Page$
	 */
	@When("^I choose to create conversation from Popup Page$")
	public void IChooseToCreateConversationFromPopupPage() {
		PagesCollection.peoplePickerPage.clickCreateConversation();
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
		PagesCollection.peoplePickerPage.confirmAddPeople();
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
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.isBlockButtonVisible());
	}
}
