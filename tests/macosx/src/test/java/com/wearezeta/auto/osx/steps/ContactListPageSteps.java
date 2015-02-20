package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.StringParser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ConversationPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.pages.PeoplePickerPage;
import com.wearezeta.auto.osx.pages.UserProfilePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ContactListPageSteps {

	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@Given("^I see my name (.*) in Contact list$")
	public void ISeeMyNameInContactList(String name) throws Exception {
		PagesCollection.loginPage.sendProblemReportIfFound();
		PagesCollection.contactListPage.pressLaterButton();
		PagesCollection.peoplePickerPage = new PeoplePickerPage(
				OSXExecutionContext.appiumUrl, OSXExecutionContext.wirePath);
		if (PagesCollection.peoplePickerPage.isPeoplePickerPageVisible()) {
			log.debug("People picker appears. Closing it.");
			PagesCollection.peoplePickerPage.closePeoplePicker();
		} else {
			log.debug("No people picker found.\nPage source: "
					+ PagesCollection.peoplePickerPage.getPageSource());
		}
		GivenISeeContactListWithName(name);
	}

	@Given("I see Contact list with name (.*)")
	public void GivenISeeContactListWithName(String name) throws Exception {
		if (name.equals(OSXLocators.RANDOM_KEYWORD)) {
			name = PagesCollection.conversationInfoPage
					.getCurrentConversationName();
		} else {
			name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		}
		log.debug("Looking for contact with name " + name);
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactWithNameExists(name));
	}

	@Given("I do not see conversation {1}(.*) {1}in contact list")
	public void IDoNotSeeConversationInContactList(String conversation)
			throws Exception {
		try {
			conversation = usrMgr.findUserByNameOrNameAlias(conversation)
					.getName();
		} catch (NoSuchUserException e) {
			// do nothing
		}
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactWithNameDoesNotExist(conversation));
	}

	@Then("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue("Login finished",
				PagesCollection.loginPage.waitForLogin());
		Assert.assertTrue(name + " were not found in contact list",
				PagesCollection.loginPage.isLoginFinished(name));
	}

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

	private void clickOnContactListEntry(String contact, boolean isUserProfile)
			throws MalformedURLException, IOException, NoSuchUserException {
		if (contact.equals(OSXLocators.RANDOM_KEYWORD)) {
			contact = PagesCollection.conversationInfoPage
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
	}

	@Given("I open conversation with (.*)")
	public void GivenIOpenConversationWith(String contact) throws Exception {
		clickOnContactListEntry(contact, false);
		PagesCollection.conversationPage = new ConversationPage(
				OSXExecutionContext.appiumUrl, OSXExecutionContext.wirePath);
	}

	@Given("I go to user (.*) profile")
	public void GivenIGoToUserProfile(String user) throws Exception {
		clickOnContactListEntry(user, true);
		PagesCollection.userProfilePage = new UserProfilePage(
				OSXExecutionContext.appiumUrl, OSXExecutionContext.wirePath);
	}

	@When("I open People Picker from contact list")
	public void WhenIOpenPeoplePickerFromContactList() throws Exception {
		PagesCollection.contactListPage.openPeoplePicker();
		PagesCollection.peoplePickerPage = new PeoplePickerPage(
				OSXExecutionContext.appiumUrl, OSXExecutionContext.wirePath);
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

	@When("I see connect invitation")
	public void ISeeConnectInvitation() throws Exception {
		GivenISeeContactListWithName(OSXLocators.CONTACT_LIST_ONE_CONNECT_REQUEST);
	}

	@When("I accept invitation")
	public void IAcceptInvitation() {
		PagesCollection.contactListPage.acceptAllInvitations();
	}

	@When("I ignore invitation")
	public void IIgnoreInvitation() {
		PagesCollection.contactListPage.ignoreAllInvitations();
	}

	@When("I archive conversation with (.*)")
	public void IArchiveConversation(String conversation) throws Exception {
		conversation = usrMgr.findUserByNameOrNameAlias(conversation).getName();
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
			PagesCollection.conversationInfoPage
					.setCurrentConversationName(CommonUtils.generateGUID());
		} else {
			PagesCollection.conversationInfoPage
					.setCurrentConversationName(name);
		}
		PagesCollection.conversationInfoPage
				.setNewConversationName(PagesCollection.conversationInfoPage
						.getCurrentConversationName());
	}
}
