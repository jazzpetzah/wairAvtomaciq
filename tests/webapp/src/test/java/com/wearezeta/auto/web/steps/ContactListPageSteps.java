package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {

	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Checks that we can see signed in user in Contact List
	 * 
	 * @step. ^I see my name (.*) in Contact list$
	 * 
	 * @param name
	 *            user name string
	 * 
	 * @throws AssertionError
	 *             if user name does not appear in Contact List
	 */
	@Given("^I see my name (.*) in Contact list$")
	public void ISeeMyNameInContactList(String name) throws Exception {
		GivenISeeContactListWithName(name);
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
		PagesCollection.peoplePickerPage = PagesCollection.contactListPage
				.isHiddenByPeoplePicker();
		if (PagesCollection.peoplePickerPage != null) {
			PagesCollection.peoplePickerPage.closeSearch();
		}
		log.debug("Looking for contact with name " + name);
		Assert.assertTrue("No contact list loaded.",
				PagesCollection.contactListPage.waitForContactListVisible());
		if (usrMgr.isSelfUserSet()
				&& usrMgr.getSelfUser().getName().equals(name)) {
			Assert.assertTrue(PagesCollection.contactListPage
					.isSelfNameEntryExist(name));
		} else {
			for (int i = 0; i < 5; i++) {
				if (PagesCollection.contactListPage
						.isConvoListEntryWithNameExist(name)) {
					return;
				}
				Thread.sleep(1000);
			}
			assert false : "Conversation list entry '" + name
					+ "' is not visible after timeout expired";
		}
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
	 * 
	 */
	@When("^I open archive$")
	public void IOpenArchive() {
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
	 * @step. ^I see connection request$
	 * 
	 * @throws Exception
	 */
	@When("^I see connection request$")
	public void ISeeConnectInvitation() throws Exception {
		Assert.assertTrue(PagesCollection.contactListPage
				.getIncomingPendingItemText().equals(
						WebAppLocators.Common.CONTACT_LIST_ONE_PERSON_WAITING));
	}

	/**
	 * Opens list of connection requests from Contact list
	 * 
	 * @step. ^I open connection requests list$
	 * 
	 * @throws Exception
	 */
	@Given("^I open connection requests list$")
	public void IOpenConnectionRequestsList() throws Exception {
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
	 * 
	 */
	@Then("^I verify my name color is the same as in color picker$")
	public void IVerifyMyNameColor() {
		final String selfNameColor = PagesCollection.contactListPage
				.getSelfNameColor();
		final String colorInColorPicker = PagesCollection.selfProfilePage
				.getCurrentAccentColor();
		Assert.assertTrue("Colors are not the same",
				colorInColorPicker.equalsIgnoreCase(selfNameColor));

	}
}
