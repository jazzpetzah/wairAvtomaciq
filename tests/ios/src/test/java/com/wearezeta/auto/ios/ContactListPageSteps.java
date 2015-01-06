package com.wearezeta.auto.ios;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

import cucumber.api.java.en.*;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.*;

public class ContactListPageSteps {
	private static final Logger log = ZetaLogger
			.getLog(ContactListPageSteps.class.getSimpleName());
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name) throws Throwable {
		name = usrMgr.findUserByNameAlias(name).getName();
		if (PagesCollection.loginPage.isSelfProfileVisible()) {
			PagesCollection.loginPage.swipeRight(1000);
		}

		Assert.assertTrue("Username : " + name
				+ " dind't appear in contact list",
				PagesCollection.loginPage.isLoginFinished(name));
		ISwipeDownContactList();
		PeoplePickerPageSteps steps = new PeoplePickerPageSteps();
		steps.WhenISeePeoplePickerPage();
		steps.IClickCloseButtonDismissPeopleView();
	}

	@When("I dismiss tutorial layout")
	public void IDismissTutorial() {
		boolean tutorialIsVisible = PagesCollection.contactListPage
				.isTutorialShown();
		if (tutorialIsVisible) {
			PagesCollection.contactListPage.dismissTutorial();
		} else {
			log.debug("No tutorial is shown");
		}
	}

	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws IOException {
		name = usrMgr.findUserByNameAlias(name).getName();
		IOSPage page = PagesCollection.contactListPage.tapOnName(name);

		if (page instanceof PersonalInfoPage) {
			PagesCollection.personalInfoPage = (PersonalInfoPage) page;
			PagesCollection.personalInfoPage.waitForEmailFieldVisible();
		} else {
			PagesCollection.dialogPage = (DialogPage) page;
		}
	}

	@When("^I tap on contact name (.*)$")
	public void WhenITapOnContactName(String name) throws IOException {
		try {
			name = usrMgr.findUserByNameAlias(name).getName();
		} catch (NoSuchElementException e) {
			// Ignore silently
		}
		IOSPage page = PagesCollection.contactListPage.tapOnName(name);

		if (page instanceof DialogPage) {
			PagesCollection.dialogPage = (DialogPage) page;
		}

		PagesCollection.iOSPage = page;
	}

	@When("^I tap on group chat with name (.*)$")
	public void WhenITapOnGroupChatName(String chatName) throws IOException {

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
		value = usrMgr.findUserByNameAlias(value).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isChatInContactList(value));
	}

	@When("^I create group chat with (.*) and (.*)$")
	public void ICreateGroupChat(String contact1, String contact2)
			throws Throwable {
		contact1 = usrMgr.findUserByNameAlias(contact1).getName();
		contact2 = usrMgr.findUserByNameAlias(contact2).getName();
		WhenITapOnContactName(contact1);
		DialogPageSteps dialogSteps = new DialogPageSteps();
		dialogSteps.WhenISeeDialogPage();
		dialogSteps.WhenISwipeUpOnDialogPage();

		OtherUserPersonalInfoPageSteps infoPageSteps = new OtherUserPersonalInfoPageSteps();
		infoPageSteps.WhenISeeOtherUserProfilePage(contact1);
		infoPageSteps.WhenIPressAddButton();

		PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
		pickerSteps.WhenISeePeoplePickerPage();
		pickerSteps.WhenITapOnSearchInputOnPeoplePickerPage();
		pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact2);
		pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenITapOnUserNameFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenIClickOnAddToConversationButton();

		GroupChatPageSteps groupChatSteps = new GroupChatPageSteps();
		final String[] names = new String[] {contact1, contact2};
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
			throws InterruptedException {
		contact1 = usrMgr.findUserByNameAlias(contact1).getName();
		contact2 = usrMgr.findUserByNameAlias(contact2).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isGroupChatAvailableInContactList());
	}

	@Then("^I tap on a group chat with (.*) and (.*)$")
	public void ITapOnGroupChat(String contact1, String contact2)
			throws IOException {
		contact1 = usrMgr.findUserByNameAlias(contact1).getName();
		contact2 = usrMgr.findUserByNameAlias(contact2).getName();
		PagesCollection.contactListPage.tapOnUnnamedGroupChat(contact1,
				contact2);
	}

	@When("^I swipe right on a (.*)$")
	public void ISwipeRightOnContact(String contact) throws IOException {
		contact = usrMgr.findUserByNameAlias(contact).getName();
		PagesCollection.contactListPage.swipeRightOnContact(500, contact);
	}

	@When("^I click mute conversation$")
	public void IClickMuteConversation() throws IOException,
			InterruptedException {

		PagesCollection.contactListPage.muteConversation();
	}

	@Then("^Contact (.*) is muted$")
	public void ContactIsMuted(String contact) throws IOException {
		contact = usrMgr.findUserByNameAlias(contact).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactMuted(contact));
	}

	@Then("^Contact (.*) is not muted$")
	public void ContactIsNotMuted(String contact) throws IOException {
		contact = usrMgr.findUserByNameAlias(contact).getName();
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
	public void ISeePlayPauseButtonNextToUserName(String contact) {
		String name = usrMgr.findUserByNameAlias(contact).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isPlayPauseButtonVisible(name));
	}

	@When("I dont see play/pause button next to username (.*) in contact list")
	public void IDontSeePlayPauseButtonNextToUserName(String contact) {
		String name = usrMgr.findUserByNameAlias(contact).getName();
		Assert.assertFalse(PagesCollection.contactListPage
				.isPlayPauseButtonVisible(name));
	}

	@When("I tap on play/pause button in contact list")
	public void ITapOnPlayPauseButtonInContactList() {
		PagesCollection.contactListPage.tapPlayPauseButton();
	}

	@When("I tap play/pause button in contact list next to username (.*)")
	public void ITapPlayPauseButtonInContactListNextTo(String contact)
			throws InterruptedException {
		String name = usrMgr.findUserByNameAlias(contact).getName();
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
	public void ISeePendingRequestLinkInContacts() {
		Assert.assertTrue("Pending request link is not in Contact list",
				PagesCollection.contactListPage.isPendingRequestInContactList());
	}

	@When("I dont see Pending request link in contact list")
	public void IDontSeePendingRequestLinkInContacts() {
		Assert.assertFalse(PagesCollection.contactListPage
				.isPendingRequestInContactList());
	}

	@When("I see conversation with not connected user (.*)")
	public void ISeeConversationWithUser(String name) {
		name = usrMgr.findUserByNameAlias(name).getName();
		Assert.assertTrue(PagesCollection.contactListPage
				.isDisplayedInContactList(name));
	}

	@When("I don't see conversation with not connected user (.*)")
	public void IDontSeeConversationWithUser(String name) {
		name = usrMgr.findUserByNameAlias(name).getName();
		Assert.assertFalse(PagesCollection.contactListPage
				.isDisplayedInContactList(name));
	}

	@When("I see in contact list group chat with (.*) (.*) (.*)")
	public void ISeeInContactsGroupChatWith(String name1, String name2,
			String name3) {
		name1 = usrMgr.findUserByNameAlias(name1).getName();
		name2 = usrMgr.findUserByNameAlias(name2).getName();
		name3 = usrMgr.findUserByNameAlias(name3).getName();
		boolean chatExists = PagesCollection.contactListPage
				.conversationWithUsersPresented(name1, name2, name3);
		Assert.assertTrue("Convesation with : " + name1 + ", " + name2 + ", "
				+ name3 + ", " + " is not in chat list", chatExists);
	}

	@When("I don't see in contact list group chat with (.*) (.*) (.*)")
	public void IDontSeeInContactsGroupChatWith(String name1, String name2,
			String name3) {
		name1 = usrMgr.findUserByNameAlias(name1).getName();
		name2 = usrMgr.findUserByNameAlias(name2).getName();
		name3 = usrMgr.findUserByNameAlias(name3).getName();
		boolean chatExists = PagesCollection.contactListPage
				.conversationWithUsersPresented(name1, name2, name3);
		Assert.assertFalse("Convesation with : " + name1 + ", " + name2 + ", "
				+ name3 + ", " + " is in chat list", chatExists);
	}

}
