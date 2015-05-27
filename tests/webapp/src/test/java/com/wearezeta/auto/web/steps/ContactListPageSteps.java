package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContactListPageSteps {

	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Checks that contact list is loaded and waits for profile avatar to be
	 * shown
	 * 
	 * @step. ^I see my avatar on top of Contact list$
	 * 
	 * @throws AssertionError
	 *             if contact list is not loaded or avatar does not appear at
	 *             the top of Contact List
	 */
	@Given("^I see my avatar on top of Contact list$")
	public void ISeeMyNameOnTopOfContactList() throws Exception {
		Assert.assertTrue("No contact list loaded.",
				PagesCollection.contactListPage.waitForContactListVisible());
		PagesCollection.contactListPage.waitForSelfProfileAvatar();
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
	 * Checks that we can see conversation with specified name in archive List
	 *
	 * @step. I see archive list with name (.*)
	 *
	 * @param name
	 *            conversation name string
	 *
	 * @throws Exception
	 *             if conversation name does not appear in archive List
	 */
	@Given("I see archive list with name (.*)")
	public void GivenISeeArchiveListWithName(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		log.debug("Looking for contact with name " + name);
		Assert.assertTrue("No contact list loaded.",
				PagesCollection.contactListPage.waitForContactListVisible());
		for (int i = 0; i < 5; i++) {
			if (PagesCollection.contactListPage
					.isArchiveListEntryWithNameExist(name)) {
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
					.getIncomingPendingItemText()
					.equals(WebAppLocators.Common.CONTACT_LIST_ONE_PERSON_WAITING));
		} else {
			String itemText = "";
			try {
				itemText = PagesCollection.contactListPage
						.getIncomingPendingItemText();
			} catch (AssertionError e) {
				log.debug(e.getMessage());
			}
			Assert.assertFalse(itemText
					.equals(WebAppLocators.Common.CONTACT_LIST_ONE_PERSON_WAITING));
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

	/**
	 * Verify whether missed call notification is present for the given
	 * conversation.
	 *
	 * @param conversationName
	 *            name of the conversation
	 * @step. I( do not)? see missed call notification for conversation (.*)
	 *
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part does not exist in the step
	 * @throws Exception
	 */
	@Then("^I( do not)? see missed call notification for conversation (.*)$")
	public void isCallMissedVisibleForContact(String shouldNotBeVisible,
			String conversationName) throws Exception {
		try {
			conversationName = usrMgr.replaceAliasesOccurences(
					conversationName, FindBy.NAME_ALIAS);
		} catch (Exception e) {
		}
		if (shouldNotBeVisible == null) {
			assertTrue(PagesCollection.contactListPage
					.isMissedCallVisibleForContact(conversationName));
		} else {
			assertFalse(PagesCollection.contactListPage
					.isMissedCallVisibleForContact(conversationName));
		}
	}

	/*
	 * Verify if ping icon in contact list is colored to expected accent color
	 * 
	 * @step. "^I verify ping icon is (\\w+) color$"
	 * 
	 * @param colorName one of these colors: StrongBlue, StrongLimeGreen,
	 * BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * 
	 * @throws Exception
	 */
	@Given("^I verify ping icon is (\\w+) color$")
	public void IVerifyPingIconColor(String colorName) throws Exception {
		final AccentColor expectedColor = AccentColor.getByName(colorName);
		final AccentColor pingColor = PagesCollection.contactListPage
				.getCurrentPingIconAccentColor();
		Assert.assertTrue("my avatar background accent color is not set",
				pingColor == expectedColor);
	}
}
