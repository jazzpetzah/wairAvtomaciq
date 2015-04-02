package com.wearezeta.auto.ios;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import cucumber.api.java.en.*;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.*;

public class ContactListPageSteps {
	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name) throws Throwable {
		name = usrMgr.replaceAliasesOccurences(name,FindBy.NAME_ALIAS);

		boolean loginFinished = PagesCollection.loginPage.isLoginFinished(name);
		if (!loginFinished) {
			log.debug(PagesCollection.loginPage.getPageSource());
		}
		Assert.assertTrue("Username : " + name
				+ " dind't appear in contact list", loginFinished);
		/*
		 * PagesCollection.loginPage.waitForLaterButton(5);
		 * PagesCollection.peoplePickerPage = PagesCollection.loginPage
		 * .clickLaterButton(); if (null != PagesCollection.peoplePickerPage) {
		 * PagesCollection.peoplePickerPage.setLaterClicked(true);
		 * PeoplePickerPageSteps steps = new PeoplePickerPageSteps();
		 * steps.WhenISeePeoplePickerPage();
		 * steps.IClickCloseButtonDismissPeopleView(); // workaround for black
		 * screen if
		 * (CommonUtils.getIsSimulatorFromConfig(ContactListPageSteps.class)) {
		 * PagesCollection.peoplePickerPage.minimizeApplication(2); } if
		 * (PagesCollection.peoplePickerPage.isPeoplePickerPageVisible()) {
		 * steps.IClickCloseButtonDismissPeopleView(); } }
		 */
	}

	@When("I dismiss tutorial layout")
	public void IDismissTutorial() throws Exception {
		boolean tutorialIsVisible = PagesCollection.contactListPage
				.isTutorialShown();
		if (tutorialIsVisible) {
			PagesCollection.contactListPage.dismissTutorial();
		} else {
			log.debug("No tutorial is shown");
		}
	}

	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		IOSPage page = PagesCollection.contactListPage.tapOnMyName(name);

		if (page instanceof PersonalInfoPage) {
			PagesCollection.personalInfoPage = (PersonalInfoPage) page;
			PagesCollection.personalInfoPage.waitForEmailFieldVisible();
		} 
	}

	@When("^I tap on contact name (.*)$")
	public void WhenITapOnContactName(String name) throws Exception {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		IOSPage page = null;

		page = PagesCollection.contactListPage.tapOnName(name);

		if (page instanceof DialogPage) {
			PagesCollection.dialogPage = (DialogPage) page;
		}

		PagesCollection.iOSPage = page;
	}

	@When("^I tap on group chat with name (.*)$")
	public void WhenITapOnGroupChatName(String chatName) throws Exception {

		IOSPage page = PagesCollection.contactListPage.tapOnGroupChat(chatName);

		if (page instanceof GroupChatPage) {
			PagesCollection.groupChatPage = (GroupChatPage) page;
		}
		PagesCollection.iOSPage = page;
	}

	@When("^I swipe down contact list$")
	public void ISwipeDownContactList() throws Throwable {
		if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
			PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.contactListPage
					.swipeDown(500);
		} else {
			PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.contactListPage
					.swipeDownSimulator();
		}
	}

	@Then("^I see first item in contact list named (.*)$")
	public void ISeeUserNameFirstInContactList(String value) throws Throwable {
		try {
			value = usrMgr.findUserByNameOrNameAlias(value).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}

		Assert.assertTrue(PagesCollection.contactListPage
				.isChatInContactList(value));
	}

	/**
	 * verifies the visibility of a specific user in the contact list
	 * 
	 * @step. ^I see user (.*) in contact list$
	 * 
	 * @param value
	 *            username value string
	 * @throws AssertionError
	 *             if the user does not exist
	 */
	@Then("^I see user (.*) in contact list$")
	public void ISeeUserInContactList(String value) throws Throwable {
		value = usrMgr.replaceAliasesOccurences(value, FindBy.NAME_ALIAS);

		Assert.assertTrue(PagesCollection.contactListPage
				.isChatInContactList(value));
	}

	@When("^I create group chat with (.*) and (.*)$")
	public void ICreateGroupChat(String contact1, String contact2)
			throws Exception {
		WhenITapOnContactName(contact1);
		DialogPageSteps dialogSteps = new DialogPageSteps();
		dialogSteps.WhenISeeDialogPage();
		dialogSteps.WhenISwipeUpOnDialogPage();

		OtherUserPersonalInfoPageSteps infoPageSteps = new OtherUserPersonalInfoPageSteps();
		infoPageSteps.WhenISeeOtherUserProfilePage(contact1);
		infoPageSteps.WhenIPressAddButton();

		PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
		pickerSteps.WhenISeePeoplePickerPage();
		CommonIOSSteps cSteps = new CommonIOSSteps();
		cSteps.UserWaitsUntilContactExistsInHisSearchResults("I", 15, contact2);
		pickerSteps.WhenITapOnSearchInputOnPeoplePickerPage();
		pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact2);
		pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenITapOnUserNameFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenIClickOnAddToConversationButton();

		Thread.sleep(2000); // wait for group chat to appear
		GroupChatPageSteps groupChatSteps = new GroupChatPageSteps();
		final String[] names = new String[] { contact1, contact2 };
		groupChatSteps.ThenISeeGroupChatPage(StringUtils.join(names,
				CommonSteps.ALIASES_SEPARATOR));
	}

	@When("^I see the group conversation name changed in the chat list$")
	public void ISeeGroupNameChangeInChatList() {
		Assert.assertTrue(PagesCollection.contactListPage
				.verifyChangedGroupNameInChatList());
	}

	@Then("^I see (.*) and (.*) chat in contact list$")
	public void ISeeGroupChatInContactList(String contact1, String contact2)
			throws Exception {
		contact1 = usrMgr.findUserByNameOrNameAlias(contact1).getName();
		contact2 = usrMgr.findUserByNameOrNameAlias(contact2).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isGroupChatAvailableInContactList());
	}

	@Then("^I tap on a group chat with (.*) and (.*)$")
	public void ITapOnGroupChat(String contact1, String contact2)
			throws Exception {
		contact1 = usrMgr.findUserByNameOrNameAlias(contact1).getName();
		contact2 = usrMgr.findUserByNameOrNameAlias(contact2).getName();
		PagesCollection.contactListPage.tapOnUnnamedGroupChat(contact1,
				contact2);
	}

	@When("^I swipe right on a (.*)$")
	public void ISwipeRightOnContact(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		PagesCollection.contactListPage.swipeRightOnContact(1500, contact);
	}

	@When("^I click mute conversation$")
	public void IClickMuteConversation() throws IOException,
			InterruptedException {

		PagesCollection.contactListPage.muteConversation();
	}

	@Then("^Contact (.*) is muted$")
	public void ContactIsMuted(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactMuted(contact));
	}

	@Then("^Contact (.*) is not muted$")
	public void ContactIsNotMuted(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertFalse(PagesCollection.contactListPage
				.isContactMuted(contact));
	}

	@Then("^I open archived conversations$")
	public void IOpenArchivedConversations() throws Exception {
		Thread.sleep(3000);
		if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
			PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.contactListPage
					.swipeUp(1000);
		} else {
			PagesCollection.contactListPage.swipeUpSimulator();
		}
	}

	@When("I see play/pause button next to username (.*) in contact list")
	public void ISeePlayPauseButtonNextToUserName(String contact)
			throws Exception {
		String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isPlayPauseButtonVisible(name));
	}

	@When("I dont see play/pause button next to username (.*) in contact list")
	public void IDontSeePlayPauseButtonNextToUserName(String contact)
			throws Exception {
		String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertFalse(PagesCollection.contactListPage
				.isPlayPauseButtonVisible(name));
	}

	@When("I tap on play/pause button in contact list")
	public void ITapOnPlayPauseButtonInContactList() {
		PagesCollection.contactListPage.tapPlayPauseButton();
	}

	@When("I tap play/pause button in contact list next to username (.*)")
	public void ITapPlayPauseButtonInContactListNextTo(String contact)
			throws Exception {
		String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.contactListPage.tapPlayPauseButtonNextTo(name);
	}

	@When("I see in contact list group chat named (.*)")
	public void ISeeInContactListGroupChatWithName(String name) {
		Assert.assertTrue(PagesCollection.contactListPage
				.isChatInContactList(name));
	}

	@When("I click on Pending request link in contact list")
	public void ICcickPendingRequestLinkContactList() throws Throwable {
		PagesCollection.pendingRequestsPage = PagesCollection.contactListPage
				.clickPendingRequest();
	}

	@When("I see Pending request link in contact list")
	public void ISeePendingRequestLinkInContacts() throws Exception {
		Assert.assertTrue("Pending request link is not in Contact list",
				PagesCollection.contactListPage.isPendingRequestInContactList());
	}

	@When("I dont see Pending request link in contact list")
	public void IDontSeePendingRequestLinkInContacts() throws Exception {
		Assert.assertFalse(PagesCollection.contactListPage
				.isPendingRequestInContactList());
	}

	@When("I see conversation with not connected user (.*)")
	public void ISeeConversationWithUser(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isDisplayedInContactList(name));
	}

	@When("I don't see conversation with not connected user (.*)")
	public void IDontSeeConversationWithUser(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertFalse(PagesCollection.contactListPage
				.isDisplayedInContactList(name));
	}

	@When("I see in contact list group chat with (.*) (.*) (.*)")
	public void ISeeInContactsGroupChatWith(String name1, String name2,
			String name3) throws Exception {
		name1 = usrMgr.findUserByNameOrNameAlias(name1).getName();
		name2 = usrMgr.findUserByNameOrNameAlias(name2).getName();
		name3 = usrMgr.findUserByNameOrNameAlias(name3).getName();
		boolean chatExists = PagesCollection.contactListPage
				.conversationWithUsersPresented(name1, name2, name3);
		Assert.assertTrue("Convesation with : " + name1 + ", " + name2 + ", "
				+ name3 + ", " + " is not in chat list", chatExists);
	}

	@When("I don't see in contact list group chat with (.*) (.*) (.*)")
	public void IDontSeeInContactsGroupChatWith(String name1, String name2,
			String name3) throws Exception {
		name1 = usrMgr.findUserByNameOrNameAlias(name1).getName();
		name2 = usrMgr.findUserByNameOrNameAlias(name2).getName();
		name3 = usrMgr.findUserByNameOrNameAlias(name3).getName();
		boolean chatExists = PagesCollection.contactListPage
				.conversationWithUsersPresented(name1, name2, name3);
		Assert.assertFalse("Convesation with : " + name1 + ", " + name2 + ", "
				+ name3 + ", " + " is in chat list", chatExists);
	}

	@When("I dont see conversation (.*) in contact list")
	public void IDoNotSeeConversationInContactList(String name)
			throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		Assert.assertFalse(PagesCollection.contactListPage
				.isDisplayedInContactList(name));
	}

	/**
	 * Conversation gets silenced by pressing the silence button
	 * 
	 * @step. ^I silence conversation (.*)$
	 * 
	 * @param conversation
	 *            conversation name to silence
	 * @throws Exception
	 *             if conversation is not found
	 * 
	 */
	@When("^I silence conversation (.*)$")
	public void ISilenceConversation(String conversation) throws Exception {
		conversation = usrMgr.findUserByNameOrNameAlias(conversation).getName();
		PagesCollection.contactListPage.silenceConversation(conversation);
	}

	/**
	 * Conversation gets unsilenced by pressing the silence button
	 * 
	 * @step. ^I unsilence conversation (.*)$
	 * 
	 * @param conversation
	 *            conversation name to unsilence
	 * @throws Exception
	 *             if conversation is not found
	 * 
	 */
	@When("^I unsilence conversation (.*)$")
	public void IUnSilenceConversation(String conversation) throws Exception {
		conversation = usrMgr.findUserByNameOrNameAlias(conversation).getName();
		PagesCollection.contactListPage.unsilenceConversation(conversation);
	}

	/**
	 * Verifies, that the conversation is really silenced
	 * 
	 * @step. ^I see conversation (.*) is silenced$
	 * 
	 * @param conversation
	 *            conversation name to silence
	 * @throws Exception
	 * 
	 */
	@Then("^I see conversation (.*) is silenced$")
	public void ISeeConversationIsSilenced(String conversation)
			throws Exception {
		conversation = usrMgr.findUserByNameOrNameAlias(conversation).getName();
		boolean isSilenced = PagesCollection.contactListPage
				.isConversationSilenced(conversation);
		Assert.assertTrue("Conversation is not silenced", isSilenced);

	}

	/**
	 * Verifies, that the conversation is unsilenced
	 * 
	 * @step. ^I see conversation (.*) is unsilenced$
	 * 
	 * @param conversation
	 *            conversation name to unsilence
	 * @throws Exception
	 * 
	 */
	@Then("^I see conversation (.*) is unsilenced$")
	public void ISeeConversationIsUnSilenced(String conversation)
			throws Exception {
		conversation = usrMgr.findUserByNameOrNameAlias(conversation).getName();
		boolean isSilenced = PagesCollection.contactListPage
				.isConversationSilenced(conversation);
		Assert.assertFalse("Conversation is silenced", isSilenced);

	}

	/**
	 * Conversation gets archived by pressing the archive button
	 * 
	 * @step. ^I archive conversation (.*)$
	 * 
	 * @param conversation
	 *            conversation name to archive
	 * @throws Exception
	 *             if conversation is not found
	 * 
	 */
	@When("^I archive conversation (.*)$")
	public void IArchiveConversation(String conversation) throws Exception {
		conversation = usrMgr.replaceAliasesOccurences(conversation,
				FindBy.NAME_ALIAS);
		PagesCollection.contactListPage.archiveConversation(conversation);
	}

	/**
	 * Verifies that an unread message dot is in the conversation list
	 * 
	 * @step. ^I see unread messages dot$
	 * 
	 * @param conversation
	 *            conversation name to check for unread dot
	 * @param dotSize
	 *            tells if dot is big or small
	 * 
	 * @throws IOException
	 * @throws Exception
	 * 
	 */
	@When("^I see unread (.*) messages dot for (.*)$")
	public void ISeeUnreadMessagesDot(String dotSize, String conversation)
			throws IOException, Exception {
		conversation = usrMgr.findUserByNameOrNameAlias(conversation).getName();
		boolean unreadDotSeen = false;
		if (dotSize.equals("big")) {
			unreadDotSeen = PagesCollection.contactListPage.unreadDotIsVisible(
					true, true, conversation);
		} else {
			unreadDotSeen = PagesCollection.contactListPage.unreadDotIsVisible(
					true, false, conversation);
		}
		Assert.assertTrue("No unread dot visible.", unreadDotSeen);
	}

	/**
	 * Verifies that an unread message dot is NOT seen in the conversation list
	 * 
	 * @step. ^I dont see an unread messages dot$
	 * 
	 * @param conversation
	 *            conversation name to check for unread dot
	 * 
	 * @throws IOException
	 * @throws Exception
	 * 
	 */
	@Then("^I dont see an unread message dot for (.*)$")
	public void IDontSeeAnUnreadMessageDot(String conversation)
			throws IOException, Exception {
		conversation = usrMgr.findUserByNameOrNameAlias(conversation).getName();
		boolean noUnreadDotSeen = PagesCollection.contactListPage
				.unreadDotIsVisible(false, false, conversation);
		Assert.assertTrue("No unread dot visible.", noUnreadDotSeen);

	}

	/**
	 * Doing a long right swipe to archive the conversation immediately
	 * 
	 * @step. ^I long swipe right to archive conversation (.*)$
	 * @param conversation
	 *            to archive
	 * @throws Exception 
	 */
	@When("^I long swipe right to archive conversation (.*)$")
	public void ILongSwipeRightToArchiveConversation(String conversation) throws Exception {
		conversation = usrMgr.replaceAliasesOccurences(conversation, FindBy.NAME_ALIAS);
		PagesCollection.contactListPage.longSwipeRightOnContact(1000, conversation);
	}

}
