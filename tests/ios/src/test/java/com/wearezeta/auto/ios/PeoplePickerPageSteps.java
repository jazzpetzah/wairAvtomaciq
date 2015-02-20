package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.pages.GroupChatPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PeoplePickerPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@When("^I see People picker page$")
	public void WhenISeePeoplePickerPage() throws Exception {
		Assert.assertTrue(PagesCollection.peoplePickerPage
				.isPeoplePickerPageVisible());
	}

	@When("I see Upload contacts dialog")
	public void WhenISeeUploadContactsDialog() throws Exception {
		if (PagesCollection.peoplePickerPage == null) {
			PagesCollection.peoplePickerPage = new PeoplePickerPage(
					PagesCollection.loginPage.getDriver(),
					PagesCollection.loginPage.getWait());
		}
		Assert.assertTrue("Upload dialog is not shown",
				PagesCollection.peoplePickerPage.isUploadDialogShown());
	}

	@When("I dont see Upload contacts dialog")
	public void WhenIDontSeeUploadContactsDialog() {
		Assert.assertFalse("Upload dialog is shown",
				PagesCollection.peoplePickerPage.isUploadDialogShown());
	}

	@When("I click Later button on Upload dialog")
	public void IClickLaterButtonOnUploadDialog() {
		PagesCollection.peoplePickerPage.clickLaterButton();
	}

	@When("I click Continue button on Upload dialog")
	public void IClickContinueButtonOnUploadDialog() {
		PagesCollection.peoplePickerPage.clickContinueButton();
	}

	@When("I see PEOPLE YOU MAY KNOW label")
	public void ISeePepopleYouMayKnowLabel() {
		Assert.assertTrue("PEOPLE YOU MAY KNOW lable is not visible",
				PagesCollection.peoplePickerPage
						.isPeopleYouMayKnowLabelVisible());
	}

	@When("I dont see PEOPLE YOU MAY KNOW label")
	public void IDontSeePepopleYouMayKnowLabel() {
		Assert.assertFalse("PEOPLE YOU MAY KNOW lable is visible",
				PagesCollection.peoplePickerPage
						.isPeopleYouMayKnowLabelVisible());
	}

	@When("I re-enter the people picker if top people list is not there")
	public void IRetryPeoplePickerIfNotLoaded() throws IOException, Exception {
		if (!PagesCollection.peoplePickerPage.isTopPeopleLabelVisible()) {
			IClickCloseButtonDismissPeopleView();
			if (CommonUtils.getIsSimulatorFromConfig(IOSPage.class) != true) {
				PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.contactListPage
						.swipeDown(1000);
			} else {
				PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.contactListPage
						.swipeDownSimulator();
			}
		}
	}

	@When("^I tap on Search input on People picker page$")
	public void WhenITapOnSearchInputOnPeoplePickerPage() throws Exception {
		PagesCollection.peoplePickerPage.tapOnPeoplePickerSearch();
	}

	@When("^I input in People picker search field user name (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserName(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.peoplePickerPage.fillTextInPeoplePickerSearch(contact);
	}

	@When("^I input in People picker search field user email (.*)$")
	public void WhenIInputInPeoplePickerSearchFieldUserEmail(String email)
			throws Exception {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.peoplePickerPage.fillTextInPeoplePickerSearch(email);
	}

	@When("I tap go to enter conversation")
	public void IEnterConversation() {
		PagesCollection.peoplePickerPage.goIntoConversation();
	}

	@When("^I see user (.*) found on People picker page$")
	public void WhenISeeUserFoundOnPeoplePickerPage(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertTrue("User :" + contact
				+ " is not presented on Pepople picker page",
				PagesCollection.peoplePickerPage
						.waitUserPickerFindUser(contact));
	}

	@When("^I tap on NOT connected user name on People picker page (.*)$")
	public void WhenITapOnUserNameFoundOnPeoplePickerPage(String contact)
			throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.connectToPage = PagesCollection.peoplePickerPage
				.clickOnNotConnectedUser(contact);
	}

	@When("^I tap on user on pending name on People picker page (.*)$")
	public void WhenITapOnUserOnPendingFoundOnPeoplePickerPage(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.otherUserOnPendingProfilePage = PagesCollection.peoplePickerPage
				.clickOnUserOnPending(contact);
	}

	@When("^I search for user name (.*) and tap on it on People picker page$")
	public void WhenISearchForUserNameAndTapOnItOnPeoplePickerPage(
			String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.connectToPage = PagesCollection.peoplePickerPage
				.pickUserAndTap(contact);
	}

	/**
	 * Verifies that a clock is present on a pending user's searched avatar
	 * 
	 * @step. ^I see the user avatar with a clock$
	 * @throws Exception
	 */

	@Then("^I see the user (.*) avatar with a clock$")
	public void ISeeUserWithAvatarClock(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(
				"Avatar does not have a clock icon",
				PagesCollection.peoplePickerPage.checkAvatarClockIcon(contact) > 0.95);
	}

	@When("^I search for ignored user name (.*) and tap on it$")
	public void WhenISearchForIgnoredUserNameAndTapOnItOnPeoplePickerPage(
			String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.peoplePickerPage.pickIgnoredUserAndTap(contact);
	}

	@When("^I see Add to conversation button$")
	public void WhenISeeAddToConversationButton() {
		Assert.assertTrue("Add to conversation button is not visible",
				PagesCollection.peoplePickerPage
						.isAddToConversationBtnVisible());
	}

	@When("^I don't see Add to conversation button$")
	public void WhenIDontSeeAddToConversationButton() {
		Assert.assertTrue("Add to conversation button is visible",
				PagesCollection.peoplePickerPage.addToConversationNotVisible());
	}

	@When("^I click Go button to create 1:1 conversation$")
	public void WhenIClickOnGoButtonForSingle() throws Exception {
		PagesCollection.iOSPage = PagesCollection.peoplePickerPage
				.clickOnGoButton(false);
	}

	@When("^I click on Go button$")
	public void WhenIClickOnGoButton() throws Exception {
		PagesCollection.groupChatPage = (GroupChatPage) PagesCollection.peoplePickerPage
				.clickOnGoButton(true);
	}

	@When("^I click clear button$")
	public void WhenIClickClearButton() throws Exception {
		PagesCollection.contactListPage = PagesCollection.peoplePickerPage
				.dismissPeoplePicker();
	}

	@Then("I tap on (.*) top connections")
	public void WhenITapOnTopConnections(int numberOfTopConnections) {
		PagesCollection.peoplePickerPage
				.tapNumberOfTopConnections(numberOfTopConnections);
	}

	@When("I click on connected user (.*) avatar on People picker page")
	public void IClickOnUserIconToAddItToExistingGroupChat(String contact)
			throws Throwable {
		String name = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.peoplePickerPage.clickConnectedUserAvatar(name);
	}

	@When("I see contact list on People picker page")
	public void ISeeContactListOnPeoplePickerPage() {
		Assert.assertTrue("Contacts label is not shown",
				PagesCollection.peoplePickerPage.isContactsLabelVisible());
	}

	@When("I see top people list on People picker page")
	public void ISeeTopPeopleListOnPeoplePickerPage() {
		Assert.assertTrue("Top People label is not shown",
				PagesCollection.peoplePickerPage.isTopPeopleLabelVisible());
	}

	@When("I tap on connected user (.*) on People picker page")
	public void ISelectUserOnPeoplePickerPage(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		PagesCollection.peoplePickerPage.selectUser(name);
	}

	@When("I see Create Conversation button on People picker page")
	public void ISeeCreateConversationButton() {
		Assert.assertTrue("Create Conversation button is not visible.",
				PagesCollection.peoplePickerPage
						.isCreateConversationButtonVisible());
	}

	@When("I click Create Conversation button on People picker page")
	public void IClickCreateConversationButton() throws Throwable {
		PagesCollection.iOSPage = PagesCollection.peoplePickerPage
				.clickCreateConversationButton();
		if (PagesCollection.iOSPage instanceof GroupChatPage) {
			PagesCollection.groupChatPage = (GroupChatPage) PagesCollection.iOSPage;
		}
	}

	@When("I see user (.*) on People picker page is selected")
	public void ISeeUserIsSelectedOnPeoplePickerPage(String name)
			throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertTrue(PagesCollection.peoplePickerPage.isUserSelected(name));
	}

	@When("I see user (.*) on People picker page is NOT selected")
	public void ISeeUserIsNotSelectedOnPeoplePickerPage(String name)
			throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		Assert.assertFalse(PagesCollection.peoplePickerPage
				.isUserSelected(name));
	}

	@When("I press backspace button")
	public void IPressBackspaceBtn() {
		PagesCollection.peoplePickerPage.hitDeleteButton();
	}

	@When("I swipe up on People picker page")
	public void ISwipeUpPeoplePickerPage() throws Throwable {
		PagesCollection.peoplePickerPage.swipeUp(500);
	}

	@When("^I click on Add to conversation button$")
	public void WhenIClickOnAddToConversationButton() throws Exception {
		if (PagesCollection.peoplePickerPage.isKeyboardVisible()) {
			PagesCollection.groupChatPage = (GroupChatPage) PagesCollection.peoplePickerPage
					.clickOnGoButton(true);
		} else {
			PagesCollection.groupChatPage = (GroupChatPage) PagesCollection.peoplePickerPage
					.clickAddToCoversationButton();
		}
	}

	@When("I click close button to dismiss people view")
	public void IClickCloseButtonDismissPeopleView() throws Exception {
		PagesCollection.peoplePickerPage.tapOnPeoplePickerClearBtn();
	}

	/**
	 * Unblocks a blocked user by clicking the unblock button
	 * 
	 * @step. I unblock user
	 * 
	 */
	@When("^I unblock user$")
	public void IUnblockUser() throws Exception {
		PagesCollection.dialogPage = PagesCollection.peoplePickerPage
				.unblockUser();

	}

	/**
	 * This step checks if the number of the selected contacts is correct.
	 * 
	 * @step. ^I see that (\\d+) contacts are selected$
	 * 
	 * @param number
	 *            expected number of contacts
	 * 
	 */
	@Then("^I see that (\\d+) contacts are selected$")
	public void ISeeThatContactsAreSelected(int number) {
		int numberOfSelectedTopPeople = PagesCollection.peoplePickerPage
				.getNumberOfSelectedTopPeople();
		Assert.assertEquals(number, numberOfSelectedTopPeople);
	}
	
	/**
	 * Presses the Send An Invite button in the people picker. To invite 
	 * people via mail.
	 * 
	 * @step. ^I press the send an invite button$
	 * 
	 */
	@When("^I press the send an invite button$")
	public void IPressTheSendAnInviteButton(){
		PagesCollection.peoplePickerPage.tapSendInviteButton();
	}

	/**
	 * Presses the Copy button on the Send Invitation pop up
	 * 
	 * @step. ^I press the copy button$
	 * 
	 */
	@When("^I press the copy button$")
	public void IPressTheCopyButton(){
		PagesCollection.peoplePickerPage.tapSendInviteCopyButton();
	}
}
