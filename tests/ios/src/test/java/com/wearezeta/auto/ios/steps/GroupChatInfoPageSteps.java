package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.GroupChatInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GroupChatInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private GroupChatInfoPage getGroupChatInfoPage() throws Exception {
		return (GroupChatInfoPage) pagesCollecton
				.getPage(GroupChatInfoPage.class);
	}

	private ContactListPage getContactListPage() throws Exception {
		return (ContactListPage) pagesCollecton.getPage(ContactListPage.class);
	}

	@When("^I press leave converstation button$")
	public void IPressLeaveConverstationButton() throws Exception {
		getGroupChatInfoPage().leaveConversation();
	}

	@When("^I change the conversation name$")
	public void IChangeConversationName() throws Exception {
		getGroupChatInfoPage().changeConversationNameToRandom();
	}

	@When("I change group conversation name to (.*)")
	public void IChangeConversationNameTo(String name) throws Exception {
		getGroupChatInfoPage().changeConversationName(name);
	}

	@Then("^I see that the conversation name is correct with (.*) and (.*)$")
	public void IVerifyCorrectConversationName(String contact1, String contact2)
			throws Exception {
		Assert.assertTrue(getGroupChatInfoPage().isCorrectConversationName(
				contact1, contact2));
	}

	@When("I see correct conversation name (.*)")
	public void ISeeCorrectConversationName(String name) throws Exception {
		Assert.assertEquals(getGroupChatInfoPage().getGroupChatName(), name);
	}

	@When("^I see the correct number of participants in the title (.*)$")
	public void IVerifyParticipantNumber(String realNumberOfParticipants)
			throws Exception {
		Assert.assertTrue(getGroupChatInfoPage().isNumberOfParticipants(
				Integer.parseInt(realNumberOfParticipants)));
	}

	@When("^I tap on (.*) and check email (.*) and name$")
	public void ITapAllParticipantsAndCheckElements(String user,
			String checkEmail) throws Exception {
		if (checkEmail.equals("visible")) {
			getGroupChatInfoPage().tapAndCheckAllParticipants(user, true);
		} else {
			getGroupChatInfoPage().tapAndCheckAllParticipants(user, false);
		}
	}

	@When("^I see the correct participant (.*) avatar$")
	public void IVerifyCorrectParticipantAvatars(String contact)
			throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(getGroupChatInfoPage().areParticipantAvatarCorrect(
				contact));
	}

	@When("^I exit the group info page$")
	// may require reworking when the UI changes
	public void IExitGroupInfoPage() throws Exception {
		getGroupChatInfoPage().exitGroupInfoPage();
	}

	@When("^I see leave conversation alert$")
	public void ISeeLeaveConversationAlert() throws Throwable {
		Assert.assertTrue(getGroupChatInfoPage()
				.isLeaveConversationAlertVisible());
	}

	@Then("^I press leave$")
	public void IPressLeave() throws Throwable {
		getGroupChatInfoPage().confirmLeaveConversation();
		getContactListPage().waitForContactListToLoad();
	}

	@When("^I select contact (.*)$")
	public void ISelectContact(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getGroupChatInfoPage().selectContactByName(name);
	}

	@When("I tap on not connected contact (.*)")
	public void ITapOnNotConnectedContact(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getGroupChatInfoPage().selectNotConnectedUser(name);
	}

	@Then("^I see that conversation has (.*) people$")
	public void ISeeThatConversationHasNumberPeople(int number)
			throws Throwable {
		int actualNumberOfPeople = getGroupChatInfoPage()
				.numberOfPeopleInConversation();
		Assert.assertTrue("Actual number of people in chat ("
				+ actualNumberOfPeople + ") is not the same as expected ("
				+ number + ")", actualNumberOfPeople == number);
	}

	@When("^I see (.*) participants avatars$")
	public void ISeeNumberParticipantsAvatars(int number) throws Throwable {
		int actual = getGroupChatInfoPage().numberOfParticipantsAvatars();
		Assert.assertTrue("Actual number of avatars (" + actual
				+ ") is not the same as expected (" + number + ")",
				actual == number);
	}

	@When("I swipe down on group chat info page")
	public void ISwipeUpOnGroupChatInfoPage() throws Exception {
		getGroupChatInfoPage().swipeDown(500);
	}

	@When("I close group chat info page")
	public void ICloseGroupChatInfoPage() throws Exception {
		getGroupChatInfoPage().closeGroupChatInfoPage();
	}

	@When("I tap on add button on group chat info page")
	public void ITapAddButtonOnGroupChatInfoPage() throws Exception {
		getGroupChatInfoPage().clickOnAddButton();
	}

	/**
	 * Verify that share history warning dialog is shown
	 * 
	 * @step. I see share history warning
	 * 
	 * @throws Exception
	 */
	@When("I see share history warning")
	public void ISeeAddPeopleToGroupChatDialog() throws Exception {
		Assert.assertTrue(getGroupChatInfoPage().isAddDialogHeaderVisible());
	}

	/**
	 * Click on continue button on share history warning dialog
	 * 
	 * @step. I click on Continue button on share history warning
	 * 
	 * @throws Throwable
	 */
	@When("I click on Continue button on share history warning")
	public void IClickOnContinueButtonInAddPeopleToGroupChatDialog()
			throws Throwable {
		getGroupChatInfoPage().clickOnAddDialogContinueButton();
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
