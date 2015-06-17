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
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private ContactListPage getContactListPage() throws Exception {
		return (ContactListPage) pagesCollection.getPage(ContactListPage.class);
	}

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verify whether conversations list is visible and no contacts are there
	 * 
	 * @step. ^I see Contact list with (no )?contacts$
	 * 
	 * @throws Exception
	 */
	@Given("^I see Contact list with (no )?contacts$")
	public void GivenISeeContactListWithNoContacts(String shouldNotBeVisible)
			throws Exception {
		getContactListPage().verifyContactListIsFullyLoaded();
		if (shouldNotBeVisible == null) {
			Assert.assertTrue(
					"No conversations are visible in the conversations list, but some are expected",
					getContactListPage().isAnyConversationVisible());
		} else {
			Assert.assertFalse(
					"Some conversations are visible in the conversations list, but zero is expected",
					getContactListPage().isAnyConversationVisible());

		}
	}

	/**
	 * Verify whether conversations list is visible
	 * 
	 * @step. ^I see Contact list$
	 * 
	 * @throws Exception
	 */
	@Given("^I see Contact list$")
	public void GivenISeeContactList() throws Exception {
		getContactListPage().verifyContactListIsFullyLoaded();
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
		getContactListPage().tapOnName(contactName);
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
		getContactListPage().tapOnMyAvatar();
	}

	/**
	 * Swipes down on the contact list to return the search list page
	 * 
	 * @step. ^I swipe down contact list$
	 * @throws Exception
	 */
	@When("^I swipe down contact list$")
	public void ISwipeDownContactList() throws Exception {
		getContactListPage().swipeDown(1000);
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
		getContactListPage().swipeRightOnConversation(1000, contact);
	}

	/**
	 * Presses on search bar in the conversation List to open start UI Sets the
	 * people picker page
	 * 
	 * @step. ^I press Open StartUI$
	 * @throws Exception
	 */
	@When("^I press Open StartUI")
	public void WhenIPressOpenStartUI() throws Exception {
		getContactListPage().pressOpenStartUI();
	}

	/**
	 * Swipes up on the contact list to reveal archived conversations
	 * 
	 * @step. ^I swipe up contact list$
	 * @throws Exception
	 */
	@When("^I swipe up contact list$")
	public void ISwipeUpContactList() throws Exception {
		getContactListPage().contactListSwipeUp(1000);
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
		// FIXME: the step should be more universal
		Assert.assertTrue(getContactListPage().isContactExists(
				contact1 + ", " + contact2)
				|| getContactListPage().isContactExists(
						contact2 + ", " + contact1));
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
		getContactListPage().waitForConversationListLoad();
		if (shouldNotSee == null) {
			Assert.assertTrue(getContactListPage().isContactExists(userName, 1));
		} else {
			Assert.assertTrue(getContactListPage().waitUntilContactDisappears(
					userName));
		}
	}

	/**
	 * Checks to see that the muted symbol appears or not for the given contact.
	 * 
	 * @step. "^Contact (.*) is (not )?muted$
	 * @param contact
	 * @param shouldNotBeMuted
	 *            is set to null if 'not' part does not exist
	 * @throws Exception
	 */
	@Then("^Contact (.*) is (not )?muted$")
	public void ContactIsMutedOrNot(String contact, String shouldNotBeMuted)
			throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		if (shouldNotBeMuted == null) {
			Assert.assertTrue(
					String.format(
							"The conversation '%s' is supposed to be muted, but it is not",
							contact),
					getContactListPage().isContactMuted(contact));
		} else {
			Assert.assertTrue(
					String.format(
							"The conversation '%s' is supposed to be not muted, but it is",
							contact), getContactListPage()
							.waitUntilContactNotMuted(contact));
		}
	}

	/**
	 * Verify that Play/Pause media content button is visible in Conversation
	 * List
	 * 
	 * @step. ^I see PlayPause media content button for conversation (.*)
	 * @param convoName
	 *            conversation name for which button presence is checked
	 * 
	 */
	@Then("^I see PlayPause media content button for conversation (.*)")
	public void ThenISeePlayPauseButtonForConvo(String convoName)
			throws Exception {
		convoName = usrMgr.replaceAliasesOccurences(convoName,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(getContactListPage().isPlayPauseMediaButtonVisible(
				convoName));
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
		getContactListPage().openPeoplePicker();
	}
}