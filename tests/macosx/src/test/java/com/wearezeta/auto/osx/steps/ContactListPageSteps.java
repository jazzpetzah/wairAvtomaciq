package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.StringParser;
import com.wearezeta.auto.common.user_management.ClientUsersManager;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;
import com.wearezeta.auto.osx.pages.ConversationPage;
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
		CommonOSXSteps.senderPages.getLoginPage().sendProblemReportIfFound();
		CommonOSXSteps.senderPages.getContactListPage().pressLaterButton();
		Thread.sleep(1000);
		CommonOSXSteps.senderPages
				.setPeoplePickerPage(new PeoplePickerPage(
						CommonUtils
								.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
						CommonUtils
								.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
		PeoplePickerPage peoplePickerPage = CommonOSXSteps.senderPages
				.getPeoplePickerPage();
		if (peoplePickerPage.isPeoplePickerPageVisible()) {
			log.debug("People picker appears. Closing it.");
			peoplePickerPage.closePeoplePicker();
		}
		GivenISeeContactListWithName(name);
	}

	@Given("I see Contact list with name (.*)")
	public void GivenISeeContactListWithName(String name) throws Exception {
		if (name.equals(OSXLocators.RANDOM_KEYWORD)) {
			name = CommonOSXSteps.senderPages.getConversationInfoPage()
					.getCurrentConversationName();
		} else {
			name = usrMgr.findUserByNameAlias(name).getName();
		}
		log.debug("Looking for contact with name " + name);
		Assert.assertTrue(CommonOSXSteps.senderPages.getContactListPage()
				.isContactWithNameExists(name));
	}

	@Given("I do not see conversation {1}(.*) {1}in contact list")
	public void IDoNotSeeConversationInContactList(String conversation)
			throws IOException {
		conversation = usrMgr.findUserByNameAlias(conversation).getName();
		Assert.assertTrue(CommonOSXSteps.senderPages.getContactListPage()
				.isContactWithNameDoesNotExist(conversation));
	}

	@Then("Contact list appears with my name (.*)")
	public void ThenContactListAppears(String name) {
		name = usrMgr.findUserByNameAlias(name).getName();
		Assert.assertTrue("Login finished", CommonOSXSteps.senderPages
				.getLoginPage().waitForLogin());
		Assert.assertTrue(name + " were not found in contact list",
				CommonOSXSteps.senderPages.getLoginPage().isLoginFinished(name));
	}

	private void clickOnContactListEntry(String contact, boolean isUserProfile)
			throws MalformedURLException, IOException {
		if (contact.equals(OSXLocators.RANDOM_KEYWORD)) {
			contact = CommonOSXSteps.senderPages.getConversationInfoPage()
					.getCurrentConversationName();
		} else {
			contact = usrMgr.findUserByNameAlias(contact).getName();
		}

		boolean isConversationExist = false;
		for (int i = 0; i < 10; i++) {
			isConversationExist = CommonOSXSteps.senderPages
					.getContactListPage().openConversation(contact,
							isUserProfile);
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
	public void GivenIOpenConversationWith(String contact)
			throws MalformedURLException, IOException {
		clickOnContactListEntry(contact, false);
		CommonOSXSteps.senderPages
				.setConversationPage(new ConversationPage(
						CommonUtils
								.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
						CommonUtils
								.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
	}

	@Given("I go to user (.*) profile")
	public void GivenIGoToUserProfile(String user)
			throws MalformedURLException, IOException {
		clickOnContactListEntry(user, true);
		CommonOSXSteps.senderPages
				.setUserProfilePage(new UserProfilePage(
						CommonUtils
								.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
						CommonUtils
								.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
	}

	@When("I open People Picker from contact list")
	public void WhenIOpenPeoplePickerFromContactList()
			throws MalformedURLException, IOException {
		CommonOSXSteps.senderPages.getContactListPage().openPeoplePicker();
		CommonOSXSteps.senderPages
				.setPeoplePickerPage(new PeoplePickerPage(
						CommonUtils
								.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
						CommonUtils
								.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
	}

	@When("I change mute state of conversation with (.*)")
	public void IChangeConversationMuteState(String conversation) {
		conversation = usrMgr.findUserByNameAlias(conversation).getName();
		ContactListPage contactList = CommonOSXSteps.senderPages
				.getContactListPage();
		contactList.changeMuteStateForConversation(conversation);
	}

	@Then("I see conversation (.*) is muted")
	public void ISeeConversationIsMuted(String conversation) {
		conversation = usrMgr.findUserByNameAlias(conversation).getName();
		ContactListPage contactList = CommonOSXSteps.senderPages
				.getContactListPage();
		Assert.assertTrue("Conversation with name " + conversation
				+ " were not muted.",
				contactList.isConversationMutedButtonVisible(conversation));
	}

	@Then("I see conversation (.*) is unmuted")
	public void ISeeConversationIsUnmuted(String conversation) {
		conversation = usrMgr.findUserByNameAlias(conversation).getName();
		ContactListPage contactList = CommonOSXSteps.senderPages
				.getContactListPage();
		Assert.assertTrue("Conversation with name " + conversation
				+ " is still muted.",
				contactList.isConversationMutedButtonNotVisible(conversation));
	}

	@When("I see connect invitation")
	public void ISeeConnectInvitation() throws Exception {
		GivenISeeContactListWithName(OSXLocators.CONTACT_LIST_ONE_CONNECT_REQUEST);
	}

	@When("I accept invitation")
	public void IAcceptInvitation() {
		ContactListPage contactList = CommonOSXSteps.senderPages
				.getContactListPage();
		contactList.acceptAllInvitations();
	}

	@When("I archive conversation with (.*)")
	public void IArchiveConversation(String conversation) {
		conversation = usrMgr.findUserByNameAlias(conversation).getName();
		ContactListPage contactList = CommonOSXSteps.senderPages
				.getContactListPage();
		contactList.moveConversationToArchive(conversation);
	}

	@When("I go to archive")
	public void IGoToArchive() {
		ContactListPage contactList = CommonOSXSteps.senderPages
				.getContactListPage();
		contactList.showArchivedConversations();
	}

	@When("I set name {1}(.*) {1}for conversation$")
	public void ISetRandomNameForConversation(String name)
			throws MalformedURLException, IOException {
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages
				.getConversationInfoPage();
		name = StringParser.unescapeString(name);
		if (name.equals(OSXLocators.RANDOM_KEYWORD)) {
			conversationInfo.setCurrentConversationName(CommonUtils
					.generateGUID());
		} else {
			conversationInfo.setCurrentConversationName(name);
		}
		conversationInfo.setNewConversationName(conversationInfo
				.getCurrentConversationName());
	}
}
