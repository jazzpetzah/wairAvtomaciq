package com.wearezeta.auto.web.steps;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class ContactListPageSteps {

	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private static final String TOOLTIP_SILENCE = "Silence";
	private static final String SHORTCUT_SILENCE_WIN = "(Ctrl + Alt + L)";
	private static final String SHORTCUT_SILENCE_MAC = "(⌘⌥L)";

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
		PagesCollection.contactListPage.clickArchiveConversation();
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
		PagesCollection.contactListPage.clickMuteConversation();
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
		PagesCollection.contactListPage.clickUnmuteConversation();
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
		conversationName = usrMgr.replaceAliasesOccurences(conversationName,
				FindBy.NAME_ALIAS);
		if (shouldNotBeVisible == null) {
			assertTrue(
					String.format(
							"The call notification in conversation '%s' should be visible",
							conversationName),
					PagesCollection.contactListPage
							.isMissedCallVisibleForContact(conversationName));
		} else {
			assertTrue(
					String.format(
							"The call notification in conversation '%s' should NOT be visible",
							conversationName),
					PagesCollection.contactListPage
							.isMissedCallInvisibleForContact(conversationName));
		}
	}

	/**
	 * Verify whether joined call notification is present for the given
	 * conversation.
	 *
	 *
	 * @step. ^I( do not)? see joined group call notification for conversation
	 *        (.*)$
	 * @param conversationName
	 *            name of the conversation
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part does not exist in the step
	 * @throws Exception
	 */
	@Then("^I( do not)? see joined group call notification for conversation (.*)$")
	public void isJoinedGroupCallNotificationVisibleForConversation(
			String shouldNotBeVisible, String conversationName)
			throws Exception {
		conversationName = usrMgr.replaceAliasesOccurences(conversationName,
				FindBy.NAME_ALIAS);
		if (shouldNotBeVisible == null) {
			assertTrue(
					String.format(
							"The joined group call notification in conversation '%s' should be visible",
							conversationName),
					PagesCollection.contactListPage
							.isJoinedGroupCallNotificationVisibleForConversation(conversationName));
		} else {
			assertTrue(
					String.format(
							"The joined group call notification in conversation '%s' should NOT be visible",
							conversationName),
					PagesCollection.contactListPage
							.isJoinedGroupCallNotificationInvisibleForConversation(conversationName));
		}
	}

	/**
	 * Verify whether unjoined group call notification is present for the given
	 * conversation.
	 *
	 *
	 * @step. ^I( do not)? see unjoined group call notification for conversation
	 *        (.*)$
	 * @param conversationName
	 *            name of the conversation
	 * @param shouldNotBeVisible
	 *            is set to null if "do not" part does not exist in the step
	 * @throws Exception
	 */
	@Then("^I( do not)? see unjoined group call notification for conversation (.*)$")
	public void isUnjoinedGroupCallNotificationVisibleForConversation(
			String shouldNotBeVisible, String conversationName)
			throws Exception {
		conversationName = usrMgr.replaceAliasesOccurences(conversationName,
				FindBy.NAME_ALIAS);
		if (shouldNotBeVisible == null) {
			assertTrue(
					String.format(
							"The unjoined group call notification in conversation '%s' should be visible",
							conversationName),
					PagesCollection.contactListPage
							.isUnjoinedGroupCallNotificationVisibleForConversation(conversationName));
		} else {
			assertTrue(
					String.format(
							"The unjoined group call notification in conversation '%s' should NOT be visible",
							conversationName),
					PagesCollection.contactListPage
							.isUnjoinedGroupCallNotificationInvisibleForConversation(conversationName));
		}
	}

	/*
	 * Verify if ping icon in contact list in conversation with user is colored
	 * to expected accent color
	 * 
	 * @step.
	 * "^I verify ping icon in conversation with (\\w+) has (\\w+) color$"
	 * 
	 * @param colorName one of these colors: StrongBlue, StrongLimeGreen,
	 * BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * 
	 * @throws Exception
	 */
	@Given("^I verify ping icon in conversation with (\\w+) has (\\w+) color$")
	public void IVerifyPingIconColor(String conversationName, String colorName)
			throws Exception {
		conversationName = usrMgr.replaceAliasesOccurences(conversationName,
				FindBy.NAME_ALIAS);
		final AccentColor expectedColor = AccentColor.getByName(colorName);
		final AccentColor pingIconColor = PagesCollection.contactListPage
				.getCurrentPingIconAccentColor(conversationName);
		Assert.assertEquals(expectedColor, pingIconColor);
	}

	/*
	 * Verify if unread dot in contact list in conversation with user is colored
	 * to expected accent color
	 * 
	 * @step.
	 * "^I verify unread dot in conversation with (\\w+) has (\\w+) color$"
	 * 
	 * @param colorName one of these colors: StrongBlue, StrongLimeGreen,
	 * BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * 
	 * @throws Exception
	 */
	@Given("^I verify unread dot in conversation with (\\w+) has (\\w+) color$")
	public void IVerifyUnreadDotColor(String conversationName, String colorName)
			throws Exception {
		conversationName = usrMgr.replaceAliasesOccurences(conversationName,
				FindBy.NAME_ALIAS);
		final AccentColor expectedColor = AccentColor.getByName(colorName);
		final AccentColor unreadDotColor = PagesCollection.contactListPage
				.getCurrentUnreadDotAccentColor(conversationName);
		Assert.assertEquals(expectedColor, unreadDotColor);
	}

	/*
	 * Verify if there is a ping icon in contact list in conversation with user
	 * 
	 * @step. "^I see ping icon in conversation with (\\w+)"
	 * 
	 * @throws Exception
	 */
	@Given("^I see ping icon in conversation with (\\w+)")
	public void ISeePingIcon(String conversationName) throws Exception {
		conversationName = usrMgr.replaceAliasesOccurences(conversationName,
				FindBy.NAME_ALIAS);
		Assert.assertTrue("No ping visible.", PagesCollection.contactListPage
				.isPingIconVisibleForConversation(conversationName));
	}

	/**
	 * Verifies whether the conversation with previously remembered users is
	 * selected in the conversation list
	 * 
	 * @step. ^I see previously remembered user selected in the conversations
	 *        list$
	 * 
	 * @throws Exception
	 */
	@Then("^I see previously remembered user selected in the conversations list$")
	public void ISeePreviouslyRememberedUserSelectedInConversationList()
			throws Exception {
		final List<String> selectedTopPeople = PeoplePickerPageSteps
				.getSelectedTopPeople();
		if (selectedTopPeople != null) {
			assert selectedTopPeople.size() == 1 : "Count of selected Top People is expected to be 1";
			String oneSelectedTopPeople = selectedTopPeople.get(0);
			oneSelectedTopPeople = usrMgr.replaceAliasesOccurences(
					oneSelectedTopPeople, FindBy.NAME_ALIAS);
			log.debug("Looking for contact with name " + selectedTopPeople);
			Assert.assertTrue("No contact list loaded.",
					PagesCollection.contactListPage.waitForContactListVisible());
			for (int i = 0; i < 5; i++) {
				if (PagesCollection.contactListPage
						.isConvoListEntryWithNameExist(oneSelectedTopPeople)) {
					return;
				}
				Thread.sleep(1000);
			}
			throw new AssertionError("Conversation list entry '"
					+ selectedTopPeople
					+ "' is not visible after timeout expired");
		} else
			throw new Error("Top People are not selected");
	}

	/**
	 * Click on options button for conversation
	 * 
	 * @step. ^I click on options button for conversation (.*)$
	 * 
	 * @param contact
	 *            conversation name string
	 * @throws Exception
	 */

	@When("^I click on options button for conversation (.*)$")
	public void IClickOnOptionsButton(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.contactListPage.clickOptionsButtonForContact(contact);
	}

	/**
	 * Verifies whether silence button tool tip is correct or not.
	 *
	 * @step. ^I see correct tooltip for silence button in options popover$
	 * @throws Exception
	 *
	 */
	@Then("^I see correct tooltip for silence button in options popover$")
	public void ISeeCorrectTooltipForSilenceButton() throws Exception {
		String tooltip = TOOLTIP_SILENCE + " ";
		if (WebAppExecutionContext.isCurrentPlatformWindows()) {
			tooltip = tooltip + SHORTCUT_SILENCE_WIN;
		} else {
			tooltip = tooltip + SHORTCUT_SILENCE_MAC;
		}
		assertThat("Silence button tooltip",
				PagesCollection.contactListPage.getMuteButtonToolTip(),
				equalTo(tooltip));
	}

	/**
	 * Types shortcut combination to mute or unmute the conversation
	 * 
	 * @param contact
	 * @step. ^I type shortcut combination to mute the conversation (.*)$
	 * @throws Exception
	 */
	@When("^I type shortcut combination to mute or unmute the conversation (.*)$")
	public void ITypeShortcutCombinationToMuteOrUnmute(String contact)
			throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.contactListPage.pressShortCutToMuteOrUnmute(contact);

	}

	/**
	 * Click the leave option
	 * 
	 * @step. ^I click the option to leave in the options popover$
	 * @throws Exception
	 */
	@When("^I click the option to leave in the options popover$")
	public void IClickLeaveButton() throws Exception {
		PagesCollection.contactListPage.clickLeaveConversation();
	}

	/**
	 * Click the block option
	 * 
	 * @step. ^I click the option to block in the options popover$
	 * @throws Exception
	 */
	@When("^I click the option to block in the options popover$")
	public void IClickBlockButton() throws Exception {
		PagesCollection.contactListPage.clickBlockConversation();
	}

	/**
	 * Verifies the modal is visible
	 * 
	 * @step. ^I see a leave warning modal$
	 * @throws Exception
	 */
	@Then("^I see a leave warning modal$")
	public void ISeeALeaveWarning() throws Exception {
		Assert.assertTrue(PagesCollection.contactListPage
				.isLeaveWarningModalVisible());
	}

	/**
	 * Click the cancel button
	 * 
	 * @step. ^I click cancel button in the leave warning$
	 * @throws Throwable
	 */
	@Then("^I click cancel button in the leave warning$")
	public void IClickCancelButtonOnLeaveWarning() throws Throwable {
		PagesCollection.contactListPage.clickCancelOnLeaveWarning();
	}

	/**
	 * Verifies the modal is visible
	 * 
	 * @step. ^I see a block warning modal$
	 * @throws Exception
	 */
	@Then("^I see a block warning modal$")
	public void ISeeABlockWarning() throws Exception {
		Assert.assertTrue(PagesCollection.contactListPage
				.isBlockWarningModalVisible());
	}

	/**
	 * Click the cancel button
	 * 
	 * @step. ^I click cancel button in the block warning$
	 * @throws Throwable
	 */
	@Then("^I click cancel button in the block warning$")
	public void IClickCancelButtonOnBlockWarning() throws Throwable {
		PagesCollection.contactListPage.clickCancelOnBlockWarning();
	}

	/**
	 * Click the block button
	 * 
	 * @step. ^I click block button in the block warning$
	 * @throws Throwable
	 */
	@Then("^I click block button in the block warning$")
	public void IClickBlockButtonOnBlockWarning() throws Throwable {
		PagesCollection.contactListPage.clickBlockOnBlockWarning();
	}

	/**
	 * Verifies a conversation is on top of conversation list
	 *
	 * @param conv
	 * @step. ^I see conversation (.*) is on the top$
	 * @throws Exception
	 */
	@When("^I see conversation (.*) is on the top$")
	public void ISeeConversationWithNameOnTop(String conv) throws Exception {
		conv = usrMgr.replaceAliasesOccurences(conv, FindBy.NAME_ALIAS);
		int itemIndex = PagesCollection.contactListPage.getItemIndex(conv);
		assertThat("Conversation is not on the top", itemIndex, equalTo(1));

	}

	/**
	 * Click the delete option
	 * 
	 * @step. ^I click delete in the options popover$
	 * @throws Exception
	 */
	@When("^I click delete in the options popover$")
	public void IClickDeleteButton() throws Exception {
		PagesCollection.contactListPage.clickDeleteConversation();
	}

	/**
	 * Verifies the delete modal is visible
	 * 
	 * @step. ^I see a delete warning modal for group conversations$
	 * @throws Exception
	 */
	@Then("^I see a delete warning modal for group conversations$")
	public void ISeeDeleteWarningForGroup() throws Exception {
		Assert.assertTrue(PagesCollection.contactListPage
				.isDeleteWarningModalForGroupVisible());
	}

	/**
	 * Click the delete button in the delete warning
	 * 
	 * @step. ^I click delete button in the delete warning$
	 * @throws Throwable
	 */
	@Then("^I click delete button in the delete warning for group conversations$")
	public void IClickDeleteButtonOnDeleteWarning() throws Throwable {
		PagesCollection.contactListPage.clickDeleteOnDeleteWarning();
	}

	/**
	 * Click the leave button in the leave warning
	 * 
	 * @step. ^I click leave button in the leave warning$
	 * @throws Throwable
	 */
	@Then("^I click leave button in the leave warning$")
	public void IClickLeaveButtonOnLeaveWarning() throws Throwable {
		PagesCollection.contactListPage.clickLeaveOnLeaveWarning();
	}

	/**
	 * Click Leave checkbox on a delete warning modal for group conversations
	 * 
	 * @step. ^I click Leave checkbox on a delete warning modal for group
	 *        conversations$
	 * @throws Throwable
	 */
	@Then("^I click Leave checkbox on a delete warning modal for group conversations$")
	public void IClickLeaveCheckboxOnDeleteWarning() throws Throwable {
		PagesCollection.contactListPage.clickLeaveCheckboxOnDeleteWarning();
	}

	/**
	 * Click the cancel button in the delete warning
	 * 
	 * @step. ^I click cancel button in the delete warning$
	 * @throws Throwable
	 */
	@Then("^I click cancel button in the delete warning for group conversations$")
	public void IClickCancelButtonOnDeleteWarning() throws Throwable {
		PagesCollection.contactListPage.clickCancelOnDeleteWarning();
	}

	/**
	 * Verifies the delete modal is visible
	 * 
	 * @step. ^I see a delete warning modal for 1:1 conversations$
	 * @throws Exception
	 */
	@Then("^I see a delete warning modal for 1:1 conversations$")
	public void ISeeDeleteWarningForSingle() throws Exception {
		Assert.assertTrue(PagesCollection.contactListPage
				.isDeleteWarningModalSingleVisible());
	}

	/**
	 * Click the delete button in the delete warning
	 * 
	 * @step. ^I click delete button in the delete warning for 1:1 conversation$
	 * @throws Throwable
	 */
	@Then("^I click delete button in the delete warning for 1:1 conversations$")
	public void IClickDeleteButtonOnDeleteWarningForSingle() throws Throwable {
		PagesCollection.contactListPage.clickDeleteOnDeleteWarningSingle();
	}

	/**
	 * Click the cancel button in the delete warning
	 * 
	 * @step. ^I click cancel button in the delete warning for 1:1 conversation$
	 * @throws Throwable
	 */
	@Then("^I click cancel button in the delete warning for 1:1 conversations$")
	public void IClickCancelButtonOnDeleteWarningForSingle() throws Throwable {
		PagesCollection.contactListPage.clickCancelOnDeleteWarningSingle();
	}
}
