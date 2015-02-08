package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ConnectToPage;
import com.wearezeta.auto.ios.pages.GroupChatPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@When("^I press leave converstation button$")
	public void IPressLeaveConverstationButton() throws Throwable {
		PagesCollection.groupChatInfoPage.leaveConversation();
	}

	@When("^I change the conversation name$")
	public void IChangeConversationName() throws IOException {
		PagesCollection.groupChatInfoPage.changeConversationNameToRandom();
	}

	@When("I change group conversation name to (.*)")
	public void IChangeConversationNameTo(String name) {
		PagesCollection.groupChatInfoPage.changeConversationName(name);
	}

	@Then("^I see that the conversation name is correct with (.*) and (.*)$")
	public void IVerifyCorrectConversationName(String contact1, String contact2)
			throws Exception {
		Assert.assertTrue(PagesCollection.groupChatInfoPage
				.isCorrectConversationName(contact1, contact2));
	}

	@When("I see correct conversation name (.*)")
	public void ISeeCorrectConversationName(String name) {
		Assert.assertEquals(
				PagesCollection.groupChatInfoPage.getGroupChatName(), name);
	}

	@When("^I see the correct number of participants in the title (.*)$")
	public void IVerifyParticipantNumber(String realNumberOfParticipants)
			throws IOException {
		Assert.assertTrue(PagesCollection.groupChatInfoPage
				.isNumberOfParticipants(Integer
						.parseInt(realNumberOfParticipants)));
	}

	@When("^I tap on (.*) and check email (.*) and name$")
	public void ITapAllParticipantsAndCheckElements(String user,
			String checkEmail) throws Exception {
		if (checkEmail.equals("visible")) {
			PagesCollection.groupChatInfoPage.tapAndCheckAllParticipants(user,
					true);
		} else {
			PagesCollection.groupChatInfoPage.tapAndCheckAllParticipants(user,
					false);
		}
	}

	@When("^I see the correct participant avatars$")
	public void IVerifyCorrectParticipantAvatars() throws IOException {
		Assert.assertTrue(PagesCollection.groupChatInfoPage
				.areParticipantAvatarsCorrect());
	}

	@When("^I exit the group info page$")
	// may require reworking when the UI changes
	public void IExitGroupInfoPage() {
		PagesCollection.groupChatInfoPage.exitGroupInfoPage();
	}

	@When("^I see leave conversation alert$")
	public void ISeeLeaveConversationAlert() throws Throwable {

		Assert.assertTrue(PagesCollection.groupChatInfoPage
				.isLeaveConversationAlertVisible());
	}

	@Then("^I press leave$")
	public void IPressLeave() throws Throwable {
		PagesCollection.groupChatInfoPage.confirmLeaveConversation();
		PagesCollection.contactListPage.waitForContactListToLoad();
	}

	@When("^I select contact (.*)$")
	public void ISelectContact(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		PagesCollection.otherUserPersonalInfoPage = PagesCollection.groupChatInfoPage
				.selectContactByName(name);
	}

	@When("I tap on not connected contact (.*)")
	public void ITapOnNotConnectedContact(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		PagesCollection.connectToPage = (ConnectToPage) PagesCollection.groupChatInfoPage
				.selectNotConnectedUser(name);
	}

	@Then("^I see that conversation has (.*) people$")
	public void ISeeThatConversationHasNumberPeople(int number)
			throws Throwable {

		int actualNumberOfPeople = PagesCollection.groupChatInfoPage
				.numberOfPeopleInConversation();
		Assert.assertTrue("Actual number of people in chat ("
				+ actualNumberOfPeople + ") is not the same as expected ("
				+ number + ")", actualNumberOfPeople == number);
	}

	@When("^I see (.*) participants avatars$")
	public void ISeeNumberParticipantsAvatars(int number) throws Throwable {

		int actual = PagesCollection.groupChatInfoPage
				.numberOfParticipantsAvatars();
		Assert.assertTrue("Actual number of avatars (" + actual
				+ ") is not the same as expected (" + number + ")",
				actual == number);
	}

	@When("I swipe down on group chat info page")
	public void ISwipeUpOnGroupChatInfoPage() throws Exception {
		PagesCollection.groupChatPage = (GroupChatPage) PagesCollection.groupChatInfoPage
				.swipeDown(500);
	}

	@When("I close group chat info page")
	public void ICloseGroupChatInfoPage() throws Exception {
		PagesCollection.groupChatPage = (GroupChatPage) PagesCollection.groupChatInfoPage
				.closeGroupChatInfoPage();
	}

	@When("I tap on add button on group chat info page")
	public void ITapAddButtonOnGroupChatInfoPage() {
		PagesCollection.groupChatInfoPage.clickOnAddButton();
	}

	@When("I see Add people to group chat dialog")
	public void ISeeAddPeopleToGroupChatDialog() {
		Assert.assertTrue(PagesCollection.groupChatInfoPage
				.isAddDialogHeaderVisible());
	}

	@When("I click on Continue button in Add people to group chat dialog")
	public void IClickOnContinueButtonInAddPeopleToGroupChatDialog()
			throws Throwable {
		PagesCollection.peoplePickerPage = PagesCollection.groupChatInfoPage
				.clickOnAddDialogContinueButton();
	}

	@When("I add to existing group chat contact (.*)")
	public void IAddToExistingChatContact(String contact) throws Throwable {
		ITapAddButtonOnGroupChatInfoPage();
		IClickOnContinueButtonInAddPeopleToGroupChatDialog();

		PeoplePickerPageSteps pickerSteps = new PeoplePickerPageSteps();
		pickerSteps.WhenISeePeoplePickerPage();
		pickerSteps.WhenIInputInPeoplePickerSearchFieldUserName(contact);
		pickerSteps.WhenISeeUserFoundOnPeoplePickerPage(contact);
		pickerSteps.WhenITapOnUserNameFoundOnPeoplePickerPage(contact);
		pickerSteps.WhenIClickOnAddToConversationButton();
	}
}
