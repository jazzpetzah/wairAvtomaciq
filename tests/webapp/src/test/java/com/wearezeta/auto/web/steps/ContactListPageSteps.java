package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.SelfProfilePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {

	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Checks that we can see signed in user on top of Contact List
	 * 
	 * @step. ^I see my name on top of Contact list$
	 * 
	 * @throws AssertionError
	 *             if self user name does not appear at the top of Contact List
	 */
	@Given("^I see my name on top of Contact list$")
	public void ISeeMyNameOnTopOfContactList() throws Exception {
		Assert.assertTrue("No contact list loaded.",
				PagesCollection.contactListPage.waitForContactListVisible());
		Assert.assertTrue(PagesCollection.contactListPage
				.isSelfNameEntryExist());
	}

	/**
	 * Verify whether self name entry is selected in the convo list
	 * 
	 * @step. ^I see my name is selected on top of Contact list$
	 * 
	 * @throws Exception
	 */
	@Then("^I see my name is selected on top of Contact list$")
	public void ISeeMyNameIsSelectedOnTopOfContactList() throws Exception {
		Assert.assertTrue("No contact list loaded.",
				PagesCollection.contactListPage.waitForContactListVisible());
		Assert.assertTrue(PagesCollection.contactListPage
				.isSelfNameEntrySelected());
		if (PagesCollection.selfProfilePage == null) {
			PagesCollection.selfProfilePage = (SelfProfilePage) PagesCollection.contactListPage
					.instantiatePage(SelfProfilePage.class);
		}
	}

	/**
	 * Checks that we can see conversation with specified name in Contact List
	 * 
	 * @step. I see Contact list with name (.*)
	 * 
	 * @param name
	 *            conversation name string
	 * 
	 * @throws AssertionError
	 *             if conversation name does not appear in Contact List
	 */
	@Given("I see Contact list with name (.*)")
	public void GivenISeeContactListWithName(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		log.debug("Looking for contact with name " + name);
		Assert.assertTrue("No contact list loaded.",
				PagesCollection.contactListPage.waitForContactListVisible());
		for (int i = 0; i < 5; i++) {
			if (PagesCollection.contactListPage
					.isConvoListEntryWithNameExist(name)) {
				return;
			}
			Thread.sleep(1000);
		}
		throw new AssertionError("Conversation list entry '" + name
				+ "' is not visible after timeout expired");
	}

	/**
	 * Opens conversation by choosing it from Contact List
	 * 
	 * @step. ^I open conversation with (.*)
	 * 
	 * @param contact
	 *            conversation name string
	 * 
	 * @throws Exception
	 */
	@Given("^I open conversation with (.*)")
	public void GivenIOpenConversationWith(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.conversationPage = PagesCollection.contactListPage
				.openConversation(contact);
	}

	/**
	 * Verifies whether the particular conversation is selected in the list
	 * 
	 * @step. ^I see conversation with (.*) is selected in conversations list$
	 * 
	 * @param convoName
	 *            conversation name
	 * @throws Exception
	 */
	@Then("^I see conversation with (.*) is selected in conversations list$")
	public void ISeeConversationIsSelected(String convoName) throws Exception {
		convoName = usrMgr.replaceAliasesOccurences(convoName,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(String.format("Conversation '%s' should be selected",
				convoName), PagesCollection.contactListPage
				.isConversationSelected(convoName));
	}

	/**
	 * Unarchives conversation 'name'
	 * 
	 * @step. I unarchive conversation with (.*)
	 * 
	 * @param name
	 *            conversation name string
	 * 
	 * @throws Exception
	 */
	@Given("^I unarchive conversation (.*)")
	public void GivenIUnarchiveConversation(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		PagesCollection.conversationPage = PagesCollection.contactListPage
				.unarchiveConversation(name);
	}

	/**
	 * Clicks the self name item in the convo list to open self profile page
	 * 
	 * @step. ^I open self profile$
	 * 
	 * @throws Exception
	 */
	@When("^I open self profile$")
	public void IOpenSelfProfile() throws Exception {
		PagesCollection.selfProfilePage = PagesCollection.contactListPage
				.openSelfProfile();
	}

	/**
	 * Archive conversation by choosing it from Contact List
	 * 
	 * @step. ^I archive conversation (.*)$
	 * 
	 * @param contact
	 *            conversation name string
	 * @throws Exception
	 * 
	 */
	@When("^I archive conversation (.*)$")
	public void IClickArchiveButton(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.contactListPage.clickOptionsButtonForContact(contact);
		PagesCollection.contactListPage
				.clickArchiveConversationForContact(contact);
	}

	/**
	 * Open archived conversations
	 * 
	 * @step. ^I open archive$
	 * @throws Exception
	 * 
	 */
	@When("^I open archive$")
	public void IOpenArchive() throws Exception {
		PagesCollection.contactListPage.openArchive();
	}

	/**
	 * Checks that we cannot see conversation with specified name in Contact
	 * List
	 * 
	 * @step. ^I do not see Contact list with name (.*)$
	 * 
	 * @param name
	 *            conversation name string
	 * 
	 * @throws AssertionError
	 *             if conversation name appear in Contact List
	 */
	@Given("^I do not see Contact list with name (.*)$")
	public void IDoNotSeeContactListWithName(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.contactListPage
				.isConvoListEntryNotVisible(name));
	}

	/**
	 * Checks that connection request is displayed in Conversation List
	 * 
	 * @step. ^I see connection request from one user$
	 * 
	 * @throws Exception
	 */
	@When("^I see connection request from one user$")
	public void ISeeIncomingConnectionFromOneUser() throws Exception {
		Assert.assertTrue(PagesCollection.contactListPage
				.getIncomingPendingItemText().equals(
						WebAppLocators.Common.CONTACT_LIST_ONE_PERSON_WAITING));
	}

	/**
	 * Checks that connection request is displayed in Conversation List or not
	 *
	 * @param doNot
	 *            is set to null if "do not" part does not exist
	 * @step. ^I(do not)? see connection request from one user$
	 *
	 * @throws Exception
	 */
	@When("^I( do not)? see connection request from one user$")
	public void IDoNotSeeIncomingConnection(String doNot) throws Exception {
		if (doNot == null) {
			Assert.assertTrue(PagesCollection.contactListPage
					.isIncomingPendingItemTextPresent());
		} else {
			Assert.assertFalse(PagesCollection.contactListPage
					.isIncomingPendingItemTextPresent());
		}
	}

	/**
	 * Opens list of connection requests from Contact list
	 * 
	 * @step. ^I open the list of incoming connection requests$
	 * 
	 * @throws Exception
	 */
	@Given("^I open the list of incoming connection requests$")
	public void IOpenIncomingConnectionRequestsList() throws Exception {
		PagesCollection.pendingConnectionsPage = PagesCollection.contactListPage
				.openConnectionRequestsList();
	}

	/**
	 * Opens People Picker in Contact List
	 * 
	 * @step. ^I open People Picker from Contact List$
	 * 
	 * @throws Exception
	 */
	@When("^I open People Picker from Contact List$")
	public void IOpenPeoplePicker() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.contactListPage
				.openPeoplePicker();
	}

	/**
	 * Silence the particular conversation from the list
	 * 
	 * @step. ^I set muted state for conversation (.*)
	 * 
	 * @param contact
	 *            conversation name string
	 * @throws Exception
	 */
	@When("^I set muted state for conversation (.*)")
	public void ISetMutedStateFor(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.contactListPage.clickOptionsButtonForContact(contact);
		PagesCollection.contactListPage
				.clickMuteConversationForContact(contact);
	}

	/**
	 * Set unmuted state for the particular conversation from the list if it is
	 * already muted
	 * 
	 * @step. ^I set unmuted state for conversation (.*)
	 * 
	 * @param contact
	 *            conversation name string
	 * @throws Exception
	 */
	@When("^I set unmuted state for conversation (.*)")
	public void ISetUnmutedStateFor(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.contactListPage.clickOptionsButtonForContact(contact);
		PagesCollection.contactListPage
				.clickUnmuteConversationForContact(contact);
	}

	/**
	 * Verify that conversation is muted by checking mute icon
	 * 
	 * @step. ^I see that conversation (.*) is muted$
	 * @param contact
	 *            conversation name string
	 * @throws Exception
	 */
	@When("^I see that conversation (.*) is muted$")
	public void ISeeConversationIsMuted(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);

		Assert.assertTrue(PagesCollection.contactListPage
				.isConversationMuted(contact));
	}

	/**
	 * Verify that conversation is muted by checking mute icon is invisible
	 * 
	 * @step. ^I see that conversation (.*) is not muted$
	 * @param contact
	 *            conversation name string
	 * @throws Exception
	 */
	@When("^I see that conversation (.*) is not muted$")
	public void ISeeConversationIsNotMuted(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);

		Assert.assertFalse(PagesCollection.contactListPage
				.isConversationMuted(contact));
	}

	/**
	 * Verify that my name color is the same as in color picker
	 * 
	 * @step. ^I verify my name color is the same as in color picker$
	 * @throws Exception
	 * 
	 */
	@Then("^I verify my name color is the same as in color picker$")
	public void IVerifyMyNameColor() throws Exception {
		final String selfNameColor = PagesCollection.contactListPage
				.getSelfNameColor();
		final String colorInColorPicker = PagesCollection.selfProfilePage
				.getCurrentAccentColor();
		Assert.assertTrue("Colors are not the same",
				colorInColorPicker.equalsIgnoreCase(selfNameColor));

	}

	/**
	 * Verify whether the particular conversations list item has expected index
	 * 
	 * @step. ^I verify that (.*) index in Contact list is (\\d+)$
	 * 
	 * @param convoNameAlias
	 *            conversation name/alias
	 * @param expectedIndex
	 *            the expected index (starts from 1)
	 * @throws Exception
	 */
	@Then("^I verify that (.*) index in Contact list is (\\d+)$")
	public void IVerifyContactIndex(String convoNameAlias, int expectedIndex)
			throws Exception {
		convoNameAlias = usrMgr.replaceAliasesOccurences(convoNameAlias,
				FindBy.NAME_ALIAS);
		final int actualIndex = PagesCollection.contactListPage
				.getItemIndex(convoNameAlias);
		Assert.assertTrue(
				String.format(
						"The index of '%s' item in Conevrsations list does not equal to %s (current value is %s)",
						convoNameAlias, expectedIndex, actualIndex),
				actualIndex == expectedIndex);
	}

	private static final int ARCHIVE_BTN_VISILITY_TIMEOUT = 5; // seconds

	/**
	 * Verify whether Archive button at the bottom of the convo list is visible
	 * or not
	 * 
	 * @step. ^I( do not)? see Archive button at the bottom of my Contact list$
	 * 
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part does not exist in the step
	 * @throws Exception
	 */
	@Then("^I( do not)? see Archive button at the bottom of my Contact list$")
	public void IVerifyArchiveButtonVisibility(String shouldNotBeVisible)
			throws Exception {
		if (shouldNotBeVisible == null) {
			PagesCollection.contactListPage
					.waitUntilArhiveButtonIsVisible(ARCHIVE_BTN_VISILITY_TIMEOUT);
		} else {
			PagesCollection.contactListPage
					.waitUntilArhiveButtonIsNotVisible(ARCHIVE_BTN_VISILITY_TIMEOUT);
		}
	}
}
