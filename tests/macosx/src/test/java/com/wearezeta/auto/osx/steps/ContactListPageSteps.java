package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.StringParser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {

	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Checks that self profile entry exists in contact list
	 * 
	 * @step. ^I see my name in [Cc]ontact [Ll]ist$
	 * 
	 * @throws AssertionError
	 *             if there is no self profile entry in contact list
	 */
	@Given("^I see my name in [Cc]ontact [Ll]ist$")
	public void ISeeMyNameInContactList() throws Exception {
		String name = usrMgr.getSelfUser().getName();
		String selfProfileUser = PagesCollection.contactListPage
				.readSelfProfileName();
		Assert.assertTrue(String.format(
				"Another user is logged in. Logged in user %s, expected - %s",
				selfProfileUser, name), selfProfileUser.equals(name));
	}

	// left for backward compatibility (currently there is no need to specify
	// signed in user for this step)
	@Deprecated
	@Given("^I see my name (.*) in [Cc]ontact [Ll]ist$")
	public void ISeeMyNameXInContactList(String user) throws Exception {
		ISeeMyNameInContactList();
	}

	/**
	 * Checks that color used for text for self profile name in contact list is
	 * the same as expected
	 * 
	 * @step. ^I see my name in [Cc]ontact [Ll]ist highlighted with color (.*)$
	 * 
	 * @param colorName
	 *            one of possible accent colors:
	 *            StrongBlue|StrongLimeGreen|BrightYellow
	 *            |VividRed|BrightOrange|SoftPink|Violet
	 * 
	 * @throws AssertionError
	 *             if accent color is not equal to expected
	 */
	@Then("^I see my name in [Cc]ontact [Ll]ist highlighted with color (.*)$")
	public void ISeeMyNameHighlightedWithColor(String colorName)
			throws IOException {
		AccentColor expectedColor = AccentColor.getByName(colorName);
		AccentColor selfNameTextColor = PagesCollection.contactListPage
				.selfNameEntryTextAccentColor();
		Assert.assertNotNull("Can't determine text color for self name.",
				selfNameTextColor);
		Assert.assertTrue(String.format(
				"Self name text color (%s) is not as expected (%s)",
				selfNameTextColor, expectedColor),
				selfNameTextColor == expectedColor);
	}

	/**
	 * Checks that contact exists in contact list
	 * 
	 * @step. ^I see contact (.*) in [Cc]ontact [Ll]ist$
	 * 
	 * @param name
	 *            contact name
	 * 
	 * @throws AssertionError
	 *             if there is no contact in contact list
	 */
	@Given("^I see contact (.*) in [Cc]ontact [Ll]ist$")
	public void ISeeContactListWithName(String name) throws Exception {
		if (name.equals(OSXLocators.RANDOM_KEYWORD)) {
			name = PagesCollection.conversationPage
					.getCurrentConversationName();
		} else {
			name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		}
		log.debug("Looking for contact with name " + name);
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactWithNameExists(name));
	}

	/**
	 * Opens self profile
	 * 
	 * @step. ^I open self profile$
	 * 
	 * @throws Exception
	 */
	@Given("^I open self profile$")
	public void GivenIGoToUserProfile() throws Exception {
		PagesCollection.selfProfilePage = PagesCollection.contactListPage
				.openSelfProfile();
	}

	/**
	 * Opens connection requests
	 * 
	 * @step. ^I open connection requests$
	 * 
	 * @throws Exception
	 */
	@When("^I open connection requests$")
	public void IOpenConnectionRequests() throws Exception {
		PagesCollection.connectionRequestsPage = PagesCollection.contactListPage
				.openConnectionRequests();
	}

	/**
	 * Checks that there is connection request from 1 user in contact list
	 * 
	 * @step. ^I see connect request$
	 * 
	 * @throws AssertionError
	 *             if there is no connection requests or more than 1
	 */
	@When("^I see connect request$")
	public void ISeeConnectRequest() throws Exception {
		ISeeXConnectRequests(1);
	}

	/**
	 * Checks that there is specified number of connection requests in contact
	 * list
	 * 
	 * @step. ^I see (\\d+) connect requests?$
	 * 
	 * @param number
	 *            number of expected connection requests
	 * 
	 * @throws AssertionError
	 *             if number of connection requests is not as expected
	 */
	@When("^I see (\\d+) connect requests?$")
	public void ISeeXConnectRequests(int number) throws Exception {
		if (number == 1) {
			ISeeContactListWithName(OSXLocators.ContactListPage.titleSingleConnectRequestCLEntry);
		} else {
			ISeeContactListWithName(String
					.format(OSXLocators.ContactListPage.titleFormatMultipleConnectRequestsCLEntry,
							number));
		}
	}

	/**
	 * Checks that there is no connection requests in contact list
	 * 
	 * @step. ^I do not see connection requests$
	 * 
	 * @throws AssertionError
	 *             if any connection request exists
	 */
	@When("^I do not see connection requests$")
	public void IDoNotSeeConnectionRequests() throws Exception {
		ArrayList<String> list = PagesCollection.contactListPage.listContacts();
		boolean isExist = false;
		for (String contact : list) {
			if (contact
					.matches(OSXLocators.ContactListPage.regexPatternConnectRequests)) {
				isExist = true;
				break;
			}
		}
		Assert.assertFalse(String.format(
				"Found connect requests in contact list, "
						+ "but not expect. Contacts: %s", list.toString()),
				isExist);
	}

	/**
	 * Minimizes Wire window
	 * 
	 * @step. ^I minimize Wire$
	 */
	@When("^I minimize Wire$")
	public void IMinimizeWire() {
		PagesCollection.contactListPage.minimizeWindowUsingScript();
	}

	/**
	 * Checks that there is no conversation in contact list
	 * 
	 * @step. ^I do not see conversation (.*) in [Cc]ontact [Ll]ist$
	 * 
	 * @param name
	 *            conversation name
	 * 
	 * @throws AssertionError
	 *             if conversation found in contact list
	 */
	@Given("^I do not see conversation (.*) in [Cc]ontact [Ll]ist$")
	public void IDoNotSeeNameInContactList(String name) throws Exception {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// do nothing
		}
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactWithNameDoesNotExist(name));
	}

	// to review
	private String findNoNameGroupChatContacts(String groupChat)
			throws NoSuchUserException {
		String result = "";
		String contacts[] = groupChat.split(",");
		for (int i = 0; i < contacts.length; i++) {
			result += usrMgr.findUserByNameOrNameAlias(contacts[i].trim())
					.getName();
			if (i < contacts.length - 1) {
				result += ",";
			}
		}
		return result;
	}

	private String clickOnContactListEntry(String contact, boolean isUserProfile)
			throws MalformedURLException, IOException, NoSuchUserException {
		if (contact.equals(OSXLocators.RANDOM_KEYWORD)) {
			contact = PagesCollection.conversationPage
					.getCurrentConversationName();
		} else {
			if (!contact.contains(",")) {
				try {
					contact = usrMgr.findUserByNameOrNameAlias(contact)
							.getName();
				} catch (NoSuchUserException e) {
					// do nothing
				}
			} else {
				contact = findNoNameGroupChatContacts(contact);
			}
		}

		boolean isConversationExist = false;
		for (int i = 0; i < 10; i++) {
			isConversationExist = PagesCollection.contactListPage
					.openConversation(contact, isUserProfile);
			if (isConversationExist)
				break;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		Assert.assertTrue("Contact list entry with name " + contact
				+ " was not found.", isConversationExist);
		return contact;
	}

	@Given("I open conversation with (.*)")
	public void GivenIOpenConversationWith(String contact) throws Exception {
		String realName = clickOnContactListEntry(contact, false);
		PagesCollection.conversationPage = new ConversationPage(
				PagesCollection.mainMenuPage.getDriver(),
				PagesCollection.mainMenuPage.getWait(), realName);
	}

	@When("I open People Picker from contact list")
	public void WhenIOpenPeoplePickerFromContactList() throws Exception {
		String currentUserEmail = usrMgr.getSelfUser().getEmail();
		PagesCollection.peoplePickerPage = PagesCollection.contactListPage
				.openPeoplePicker();
		Boolean isUsedAlready = ContactListPage.shareContactsProcessedUsers
				.get(currentUserEmail);
		if (isUsedAlready == null) {
			PagesCollection.contactListPage.pressLaterButton();
			ContactListPage.shareContactsProcessedUsers.put(currentUserEmail,
					false);
		}
	}

	@When("I change mute state of conversation with (.*)")
	public void IChangeConversationMuteState(String conversation)
			throws Exception {
		conversation = usrMgr.findUserByNameOrNameAlias(conversation).getName();
		PagesCollection.contactListPage
				.changeMuteStateForConversation(conversation);
	}

	@Then("I see conversation (.*) is muted")
	public void ISeeConversationIsMuted(String conversation) throws Exception {
		conversation = usrMgr.findUserByNameOrNameAlias(conversation).getName();
		Assert.assertTrue("Conversation with name " + conversation
				+ " were not muted.", PagesCollection.contactListPage
				.isConversationMutedButtonVisible(conversation));
	}

	@Then("I see conversation (.*) is unmuted")
	public void ISeeConversationIsUnmuted(String conversation) throws Exception {
		conversation = usrMgr.findUserByNameOrNameAlias(conversation).getName();
		Assert.assertTrue("Conversation with name " + conversation
				+ " is still muted.", PagesCollection.contactListPage
				.isConversationMutedButtonNotVisible(conversation));
	}

	@When("I archive conversation with (.*)")
	public void IArchiveConversation(String conversation) throws Exception {
		try {
			conversation = usrMgr.findUserByNameOrNameAlias(conversation)
					.getName();
		} catch (NoSuchUserException e) {
			// do nothing
		}
		PagesCollection.contactListPage.moveConversationToArchive(conversation);
	}

	@When("I go to archive")
	public void IGoToArchive() {
		PagesCollection.contactListPage.showArchivedConversations();
	}

	@When("I set name {1}(.*) {1}for conversation$")
	public void ISetRandomNameForConversation(String name)
			throws MalformedURLException, IOException {
		name = StringParser.unescapeString(name);
		if (name.equals(OSXLocators.RANDOM_KEYWORD)) {
			PagesCollection.conversationPage
					.setCurrentConversationName(CommonUtils.generateGUID());
		} else {
			PagesCollection.conversationPage.setCurrentConversationName(name);
		}
		PagesCollection.conversationInfoPage
				.setNewConversationName(PagesCollection.conversationPage
						.getCurrentConversationName());
	}

}
