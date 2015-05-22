package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Close People Picker and open contact list without contacts (Should these
	 * two pieces of behaviour be different steps?)
	 * 
	 * @step. ^I see Contact list with no contacts$
	 * 
	 * @throws Exception
	 */
	@Given("^I see Contact list with no contacts$")
	public void GivenISeeContactListWithNoContacts() throws Exception {
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished());
	}

	/**
	 * After login checks to see that a conversation list exists with the
	 * current user name visible at the top (Must log in first for this step to
	 * work - seems like it should be independent of the step before it)
	 * 
	 * @step. ^I see Contact list$
	 * 
	 * @throws Exception
	 */
	@Given("^I see Contact list$")
	public void GivenISeeContactList() throws Exception {
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished());
		PagesCollection.contactListPage.verifyContactListIsFullyLoaded();
	}

	/**
	 * Taps on a given contact name
	 * 
	 * @step. ^I tap on contact name (.*)$
	 * @param contactName
	 *            the contact to tap on
	 * @throws Exception
	 */
	@When("^I tap on contact name (.*)$")
	public void WhenITapOnContactName(String contactName) throws Exception {
		try {
			contactName = usrMgr.findUserByNameOrNameAlias(contactName)
					.getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.androidPage = PagesCollection.contactListPage
				.tapOnName(contactName);
	}

	/**
	 * Taps on the currently logged-in user's avatar
	 * 
	 * @step. ^I tap on my avatar$
	 * 
	 * @throws Exception
	 */
	@When("^I tap on my avatar$")
	public void WhenITapOnMyAvatar() throws Exception {
		PagesCollection.personalInfoPage = (PersonalInfoPage) PagesCollection.contactListPage
				.tapOnMyAvatar();
	}

	/**
	 * Swipes down on the contact list to return the search list page
	 * 
	 * @step. ^I swipe down contact list$
	 * @throws Exception
	 */
	@When("^I swipe down contact list$")
	public void ISwipeDownContactList() throws Exception {
		PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.contactListPage
				.swipeDown(1000);
	}

	/**
	 * Swipes right on a contact to archive the conversation with that contact
	 * (Perhaps this step should only half swipe to reveal the archive button,
	 * since the "I swipe archive conversation" step exists)
	 * 
	 * @step. ^I swipe right on a (.*)$
	 * @param contact
	 *            the contact or conversation name on which to swipe
	 * @throws Exception
	 */
	@When("^I swipe right on a (.*)$")
	public void ISwipeRightOnContact(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently - seems bad...
		}
		AndroidPage page = PagesCollection.contactListPage.swipeRightOnContact(
				1000, contact);
		if (page instanceof DialogPage) {
			PagesCollection.dialogPage = (DialogPage) page;
		}
	}

	/**
	 * Presses on the open start UI button (the "+" symbol in the conversation
	 * list) Sets the people picker page
	 * 
	 * @step. ^I press Open StartUI Button$
	 * @throws Exception
	 */
	@When("^I press Open StartUI Button")
	public void WhenIPressOpenStartUIButton() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.contactListPage
				.pressOpenStartUIButton();
	}

	/**
	 * Swipes up on the contact list to reveal archived conversations
	 * 
	 * @step. ^I swipe up contact list$
	 * @throws Exception
	 */
	@When("^I swipe up contact list$")
	public void ISwipeUpContactList() throws Exception {
		PagesCollection.contactListPage.contactListSwipeUp(1000);
	}

	/**
	 * Asserts that two given contact names exist in the conversation list
	 * (should this maybe be set to a list?)
	 * 
	 * @step. ^I see (.*) and (.*) chat in contact list$
	 * @param contact1
	 *            The first contact to check in the conversation list
	 * @param contact2
	 *            The second contact to check in the conversation list
	 * @throws Exception
	 */
	@Then("^I see (.*) and (.*) chat in contact list$")
	public void ISeeGroupChatInContactList(String contact1, String contact2)
			throws Exception {
		contact1 = usrMgr.findUserByNameOrNameAlias(contact1).getName();
		contact2 = usrMgr.findUserByNameOrNameAlias(contact2).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactExists(contact1 + ", " + contact2)
				|| PagesCollection.contactListPage.isContactExists(contact2
						+ ", " + contact1));
	}

	/**
	 * Check to see that the current user's name appears at the top of the
	 * contact list
	 * 
	 * @step. ^Contact list appears$
	 *
	 * @throws Exception
	 */
	@Then("Contact list appears$")
	public void ThenContactListAppears() throws Exception {
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished());
	}

	/**
	 * Check to see that a given username appears in the contact list
	 * 
	 * @step. ^I( do not)? see contact list with name (.*)$
	 * @param userName
	 *            the username to check for in the contact list
	 * @param shouldNotSee
	 *            equals to null if "do not" part does not exist
	 * @throws Exception
	 */
	@Then("^I( do not)? see contact list with name (.*)$")
	public void ISeeUserNameInContactList(String shouldNotSee, String userName)
			throws Exception {
		try {
			userName = usrMgr.findUserByNameOrNameAlias(userName).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.contactListPage.waitForConversationListLoad();
		if (shouldNotSee == null) {
			Assert.assertTrue(PagesCollection.contactListPage.isContactExists(
					userName, 1));
		} else {
			Assert.assertTrue(PagesCollection.contactListPage
					.waitUntilContactDisappears(userName));
		}
	}

	/**
	 * Check that Conversation List contains Random user from PYMK
	 * 
	 * @step. ^I see contact list loaded with PeoplePicker Random Connect$
	 * 
	 */
	@Then("^I see contact list loaded with PeoplePicker Random Connect$")
	public void ThenISeeContactListLoadedWithPeoplePickerRandomConnect()
			throws Exception {
		Assert.assertTrue(PagesCollection.contactListPage.isContactExists(
				PeoplePickerPageSteps.randomConnectName, 5));
	}

	/**
	 * Checks to see that the muted symbol appears. Note, it just checks to see
	 * that a mute symbol exists, not that one exists next to a given user
	 * 
	 * @step. ^Contact (.*) is muted$
	 * @param contact
	 * @throws Exception
	 */
	@Then("^Contact (.*) is muted$")
	public void ContactIsMuted(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactMuted(contact));
	}

	/**
	 * Check to see that the muted symbol does not appear Note, this step just
	 * checks to see that there is no muted symbol, not that there isn't one
	 * next to a given user
	 * 
	 * @step. ^Contact (.*) is not muted$
	 * @param contact
	 * @throws Exception
	 */
	@Then("^Contact (.*) is not muted$")
	public void ThenContactIsNotMuted(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.contactListPage
				.waitUntilContactNotMuted(contact));
	}

	/**
	 * Verify that Play/Pause media content button is visible in Conversation
	 * List
	 * 
	 * @step. ^I see PlayPause media content button in Conversations List$
	 * 
	 */
	@Then("^I see PlayPause media content button in Conversations List$")
	public void ThenISeePlayPauseMediaContentButtonInConvLst() throws Exception {
		Assert.assertTrue(PagesCollection.contactListPage
				.isPlayPauseMediaButtonVisible());
	}

	/**
	 * Open People Picker by clicking the Search button in the right top corner
	 * of convo list
	 * 
	 * @step. ^I open People Picker$
	 * 
	 * @throws Exception
	 */
	@When("^I open People Picker$")
	public void IOpenPeoplePicker() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.contactListPage
				.openPeoplePicker();
	}
}