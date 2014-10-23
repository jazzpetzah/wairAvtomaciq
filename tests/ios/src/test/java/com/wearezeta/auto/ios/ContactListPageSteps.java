package com.wearezeta.auto.ios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilderException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.junit.Assert;

import cucumber.api.java.en.*;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.ios.pages.*;

public class ContactListPageSteps {
	private static final Logger log = ZetaLogger.getLog(ContactListPageSteps.class.getSimpleName());
	
	@Given("^I see Contact list with my name (.*)$")
	public void GivenISeeContactListWithMyName(String name) throws Throwable {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		boolean tutorialIsVisible = PagesCollection.contactListPage.isTutorialShown();
		if(tutorialIsVisible) {
			PagesCollection.contactListPage.dismissTutorial();
		} else {
			log.debug("No tutorial is shown");
		}
		
		Assert.assertTrue(PagesCollection.loginPage.isLoginFinished(name));
		ISwipeDownContactList();
		PeoplePickerPageSteps steps = new PeoplePickerPageSteps();
		steps.WhenISeePeoplePickerPage();
		steps.IClickCloseButtonDismissPeopleView();
	}

	@Given("^I have group chat named (.*) with an unconnected user, made by (.*)$")
	public void GivenGroupChatWithName(String chatName, String groupCreator)
			throws Throwable {
		BackEndREST.createGroupChatWithUnconnecteduser(
				chatName, groupCreator);
	}

	@When("^I tap on my name (.*)$")
	public void WhenITapOnMyName(String name) throws IOException {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
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

		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
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

		value = CommonUtils.retrieveRealUserContactPasswordValue(value);
		Assert.assertTrue(PagesCollection.contactListPage.isChatInContactList(value));
	}

	@When("^I create group chat with (.*) and (.*)$")
	public void ICreateGroupChat(String contact1, String contact2)
			throws Throwable {

		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		WhenITapOnContactName(contact1);
		DialogPageSteps dialogSteps = new DialogPageSteps();
		dialogSteps.WhenISeeDialogPage();
		dialogSteps.WhenISwipeUpOnDialogPage();

		OtherUserPersonalInfoPageSteps infoPageSteps = new OtherUserPersonalInfoPageSteps();
		infoPageSteps.WhenISeeOtherUserProfilePage(contact1);
		infoPageSteps.WhenIPressAddButton();

		PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
		pickerSteps.WhenISeePeoplePickerPage();
		pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact2);
		pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenITapOnUserNameFoundOnPeoplePickerPage(contact2);
		pickerSteps.WhenIClickOnAddToConversationButton();

		GroupChatPageSteps groupChatSteps = new GroupChatPageSteps();
		groupChatSteps.ThenISeeGroupChatPage(contact1, contact2);
	}

	@When("^I see the group conversation name changed in the chat list$")
	public void ISeeGroupNameChangeInChatList() {
		Assert.assertTrue(PagesCollection.contactListPage
				.verifyChangedGroupNameInChatList());
	}

	@Then("^I see (.*) and (.*) chat in contact list$")
	public void ISeeGroupChatInContactList(String contact1, String contact2)
			throws InterruptedException {

		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		Assert.assertTrue(PagesCollection.contactListPage
				.isGroupChatAvailableInContactList());
	}

	@Then("^I tap on a group chat with (.*) and (.*)$")
	public void ITapOnGroupChat(String contact1, String contact2)
			throws IOException {

		contact1 = CommonUtils.retrieveRealUserContactPasswordValue(contact1);
		contact2 = CommonUtils.retrieveRealUserContactPasswordValue(contact2);
		PagesCollection.contactListPage.tapOnUnnamedGroupChat(contact1,
				contact2);
	}

	@When("^I swipe right on a (.*)$")
	public void ISwipeRightOnContact(String contact) throws IOException {

		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		PagesCollection.contactListPage.swipeRightOnContact(500, contact);
	}

	@When("^I click mute conversation$")
	public void IClickMuteConversation() throws IOException,
			InterruptedException {

		PagesCollection.contactListPage.muteConversation();
	}

	@Then("^Contact (.*) is muted$")
	public void ContactIsMuted(String contact) throws IOException {

		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		Assert.assertTrue(PagesCollection.contactListPage
				.isContactMuted(contact));
	}

	@Then("^Contact (.*) is not muted$")
	public void ContactIsNotMuted(String contact) throws IOException {

		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
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
	public void ISeePlayPauseButtonNextToUserName(String contact){
		String name = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		Assert.assertTrue(PagesCollection.contactListPage.isPlayPauseButtonVisible(name));
	}
	
	@When("I dont see play/pause button next to username (.*) in contact list")
	public void IDontSeePlayPauseButtonNextToUserName(String contact){
		String name = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		Assert.assertFalse(PagesCollection.contactListPage.isPlayPauseButtonVisible(name));
	}
	
	@When("I tap on play/pause button in contact list")
	public void ITapOnPlayPauseButtonInContactList(){
		PagesCollection.contactListPage.tapPlayPauseButton();
	}
	
	@When("I see in contact list group chat named (.*)")
	public void ISeeInContactListGroupChatWithName(String name){
		Assert.assertTrue(PagesCollection.contactListPage.isChatInContactList(name));
	}
	
	@When("I click on Pending request link in contact list")
	public void ICcickPendingRequestLinkContactList() throws Throwable{
		PagesCollection.pendingRequestsPage = PagesCollection.contactListPage.clickPendingRequest();
	}
	
	@When("I see Pending request link in contact list")
	
	public void ISeePendingRequestLinkInContacts(){
		Assert.assertTrue(PagesCollection.contactListPage.isPendingRequestInContactList());
	}
	
	@When("I dont see Pending request link in contact list")
	public void IDontSeePendingRequestLinkInContacts(){
		Assert.assertFalse(PagesCollection.contactListPage.isPendingRequestInContactList());
	}
	
	@When("I see conversation with not connected user (.*)")
	public void ISeeConversationWithUser(String name){
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		Assert.assertTrue(PagesCollection.contactListPage.isDisplayedInContactList(name));
	}
	
	@When("I don't see conversation with not connected user (.*)")
	public void IDontSeeConversationWithUser(String name){
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		Assert.assertFalse(PagesCollection.contactListPage.isDisplayedInContactList(name));
	}
	
	@When("I see in contact list group chat with (.*) (.*) (.*)")
	public void ISeeInContactsGroupChatWith(String name1, String name2, String name3){
		name1=CommonUtils.retrieveRealUserContactPasswordValue(name1);
		name2=CommonUtils.retrieveRealUserContactPasswordValue(name2);
		name3=CommonUtils.retrieveRealUserContactPasswordValue(name3);
		String chatname = name1 + ", " + name2 + ", " + name3;
		Assert.assertTrue(PagesCollection.contactListPage.isDisplayedInContactList(chatname));
	}
	
	@When("I don't see in contact list group chat with (.*) (.*) (.*)")
	public void IDontSeeInContactsGroupChatWith(String name1, String name2, String name3){
		name1=CommonUtils.retrieveRealUserContactPasswordValue(name1);
		name2=CommonUtils.retrieveRealUserContactPasswordValue(name2);
		name3=CommonUtils.retrieveRealUserContactPasswordValue(name3);
		String chatname = name1 + ", " + name2 + ", " + name3;
		Assert.assertFalse(PagesCollection.contactListPage.isDisplayedInContactList(chatname));
	}

}
