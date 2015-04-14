package com.wearezeta.auto.android;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@SuppressWarnings("unused")
	private void disableHint(String name) throws Exception {
		Thread.sleep(2000);
		if (PagesCollection.contactListPage.isHintVisible()) {
			PagesCollection.contactListPage.closeHint();
			Thread.sleep(1000);
			ISwipeDownContactList();
			if (PagesCollection.peoplePickerPage.isPeoplePickerPageVisible()) {
				PagesCollection.peoplePickerPage.tapClearButton();
			}

			WhenITapOnMyName(name);
			PagesCollection.contactListPage.navigateBack();

			String contactName = "aqaContact1";
			WhenITapOnContactName(contactName);

			if (PagesCollection.contactListPage.isHintVisible()) {
				PagesCollection.contactListPage.closeHint();
			}
			PagesCollection.contactListPage.navigateBack();

			WhenITapOnContactName(contactName);

			if (PagesCollection.contactListPage.isHintVisible()) {
				PagesCollection.contactListPage.closeHint();
			}
			DialogPageSteps steps = new DialogPageSteps();
			steps.WhenISeeDialogPage();
			steps.WhenISwipeOnTextInput();
			steps.WhenISwipeLeftOnTextInput();
			PagesCollection.contactListPage.navigateBack();
		}
	}

	/**
	 * Close People Picker and open contact list without contacts (Should these
	 * two pieces of behaviour be different steps?)
	 * 
	 * @step. ^I see Contact list with no contacts and my name (.*)$
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@Given("^I see Contact list with no contacts and my name (.*)$")
	public void GivenISeeContactListWithNoContactsAndMyNameAnd(String name)
			throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		if (PagesCollection.peoplePickerPage.isPeoplePickerPageVisible()) {
			PagesCollection.peoplePickerPage.tapClearButton();
		}

		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));

	}

	/**
	 * After login checks to see that a conversation list exists with the
	 * current user name visible at the top (Must log in first for this step to
	 * work - seems like it should be independent of the step before it)
	 * 
	 * @step. ^I see Contact list with my name (.*)$
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws Exception
	 */
	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name) throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();

		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
		PagesCollection.contactListPage.waitForContactListLoadFinished();

	}

	/**
	 * Checks to see that a user does not exist in the conversation list
	 * 
	 * @step. ^I do not see Contact list with name (.*)$
	 * 
	 * @param selfContactName
	 *            the name of the contact to check
	 * @throws Exception
	 */
	@Given("^I do not see Contact list with name (.*)$")
	public void GivenIDoNotSeeContactListWithName(String selfContactName)
			throws Exception {
		try {
			selfContactName = usrMgr.findUserByNameOrNameAlias(selfContactName)
					.getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertFalse(PagesCollection.contactListPage
				.isContactExists(selfContactName));
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
	 * Taps on the currently logged-in user's name
	 * 
	 * @step. ^I tap on my name (.*)$
	 * @param name
	 * @throws Exception
	 */
	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws Exception {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.personalInfoPage = (PersonalInfoPage) PagesCollection.contactListPage
				.tapOnName(name);
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
	 * Establishes a group chat between the current user and two other users
	 * (Consider making the number of other users variable)
	 * 
	 * @step. ^I create group chat with (.*) and (.*)$
	 * @param contact1
	 *            The first user
	 * @param contact2
	 *            The second user
	 * @throws Exception
	 */
	@When("^I create group chat with (.*) and (.*)$")
	public void ICreateGroupChat(String contact1, String contact2)
			throws Exception {
		try {
			contact1 = usrMgr.findUserByNameOrNameAlias(contact1).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		try {
			contact2 = usrMgr.findUserByNameOrNameAlias(contact2).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		WhenITapOnContactName(contact1);
		DialogPageSteps dialogSteps = new DialogPageSteps();
		dialogSteps.WhenISeeDialogPage();
		dialogSteps.WhenISwipeLeftOnDialogPage();

		OtherUserPersonalInfoPageSteps infoPageSteps = new OtherUserPersonalInfoPageSteps();
		infoPageSteps.WhenISeeOherUserProfilePage(contact1);
		infoPageSteps.WhenISwipeDownOtherUserProfilePage();

		PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
		pickerSteps.WhenISeePeoplePickerPage();
		pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact2);
		pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenITapOnUserNameFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenIClickOnAddToConversationButton();

		DialogPageSteps groupChatSteps = new DialogPageSteps();
		final String[] names = new String[] { contact1, contact2 };
		groupChatSteps.ThenISeeGroupChatPage(StringUtils.join(names,
				CommonSteps.ALIASES_SEPARATOR));
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
	 * Doesn't currently work? (See comments for "I swipe right on a contact")
	 * 
	 * @step. ^I swipe archive conversation (.*)$
	 * @param contact
	 *            the contact on which to swipe to archive
	 * @throws Exception
	 */
	@When("^I swipe archive conversation (.*)$")
	public void ISwipeArchiveConversation(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		AndroidPage page = PagesCollection.contactListPage
				.swipeOnArchiveUnarchive(contact);
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
	 * @step. ^Contact list appears with my name (.*)$
	 * @param name
	 *            the current user's name
	 * @throws Exception
	 */
	@Then("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();

		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
	}

	/**
	 * Check to see that a given username appears in the contact list
	 * 
	 * @step. ^I see contact list loaded with User name (.*)$
	 * @param userName
	 *            the username to check for in the contact list
	 * @throws Exception
	 */
	@Then("^I see contact list loaded with User name (.*)$")
	public void ISeeUserNameFirstInContactList(String userName)
			throws Exception {
		try {
			userName = usrMgr.findUserByNameOrNameAlias(userName).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.contactListPage.waitForConversationListLoad();
		Assert.assertTrue(PagesCollection.contactListPage.isContactExists(
				userName, 5));
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

		Assert.assertTrue(PagesCollection.contactListPage.isContactMuted());
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

		Assert.assertFalse(PagesCollection.contactListPage.isContactMuted());
	}

	/**
	 * Check to see whether a given user appears in the contact list.
	 * 
	 * @step. ^Contact name (.*) is not in list$
	 * @param userName
	 *            The user to ensure doesn't exist in the contact list.
	 * @throws Exception
	 */
	@Then("^Contact name (.*) is not in list$")
	public void ThenContactNameIsNotInList(String userName) throws Exception {
		try {
			userName = usrMgr.findUserByNameOrNameAlias(userName).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertFalse(PagesCollection.contactListPage
				.isContactExists(userName));
	}

	/**
	 * Verify that Play/Pause media content button is visible in Conversation
	 * List
	 * 
	 * @step. ^I see PlayPause media content button in Conversations List$
	 * 
	 */
	@Then("^I see PlayPause media content button in Conversations List$")
	public void ThenISeePlayPauseMediaContentButtonInConvLst()
			throws NumberFormatException, Exception {
		Assert.assertTrue(PagesCollection.contactListPage
				.isPlayPauseMediaButtonVisible());
	}

	/**
	 * Check that conversation list contains missed call icon
	 * 
	 * @step. ^Conversation List contains missed call icon$
	 * 
	 * @throws Exception
	 */
	@Then("^Conversation List contains missed call icon$")
	public void ThenISeeMissedCallIcon() throws Exception {
		Assert.assertTrue(PagesCollection.contactListPage
				.isVisibleMissedCallIcon());
	}
}
