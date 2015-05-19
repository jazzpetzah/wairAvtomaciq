package com.wearezeta.auto.android.steps;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
	private final String BG_IMAGE_NAME = "aqaPictureContactBG.png";
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Checks to see that the profile page of another user is visible after
	 * swiping up from dialog
	 * 
	 * @step. ^I see (.*) user profile page$
	 * 
	 * @param name
	 * @throws Exception
	 */
	@When("^I see (.*) user profile page$")
	public void WhenISeeOherUserProfilePage(String name) throws Exception {
		if (PagesCollection.otherUserPersonalInfoPage == null) {
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.androidPage;
		}
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.otherUserPersonalInfoPage.isOtherUserNameVisible(name);
	}

	/**
	 * Removes a contact from a group conversation
	 * 
	 * @step. ^I click Remove$
	 * 
	 * @throws Exception
	 */
	@When("^I click Remove$")
	public void WhenIClickRemove() throws Exception {
		// TODO: check for native button click
		PagesCollection.otherUserPersonalInfoPage.pressOptionsMenuButton();
	}

	/**
	 * Checks to see that the remove user warning message appears -unclear
	 * 
	 * @step. ^I see warning message$
	 * 
	 * @throws Exception
	 */
	@When("^I see warning message$")
	public void WhenISeeWarningMessage() throws Exception {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isConversationAlertVisible());
	}

	/**
	 * Confirms the remove button after seeing the warning message
	 * 
	 * @step. ^I confirm remove$
	 * 
	 * @throws Exception
	 */
	@When("^I confirm remove$")
	public void WhenIConfirmRemove() throws Exception {
		PagesCollection.otherUserPersonalInfoPage.pressConfirmBtn();
	}

	/**
	 * Confirms the block of another user when they send a connection request
	 * 
	 * @step. ^I confirm block$
	 * 
	 * @throws Throwable
	 */
	@When("^I confirm block$")
	public void WhenIConfirmBlock() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressConfirmBtn();
	}

	/**
	 * Presses the "add people to conversation" button from another user's
	 * profile
	 * 
	 * @step. ^I press add contact button$
	 * 
	 * @throws Exception
	 */
	@When("^I press add contact button$")
	public void WhenIPressAddContactButton() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.otherUserPersonalInfoPage
				.tapAddContactBtn();
	}

	/**
	 * Blocks a user from the user profile page
	 * 
	 * @step. ^I Press Block button$
	 * @throws Exception
	 */
	@When("^I Press Block button$")
	public void IPressBlockButton() throws Exception {
		PagesCollection.otherUserPersonalInfoPage.clickBlockBtn();
	}

	/**
	 * Checks to see that we can see a given user's profile -duplicate of
	 * WhenISeeOherUserProfilePage(String)
	 * 
	 * @step. ^I see (.*) user name and email$
	 * 
	 * @param contact
	 * @throws Exception
	 */
	@Then("^I see (.*) user name and email$")
	public void ISeeUserNameAndEmail(String contact) throws Exception {
		ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(contact);
		contact = dstUser.getName();
		String email = dstUser.getEmail();
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isOtherUserNameVisible(contact));
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isOtherUserMailVisible(email));
	}

	/**
	 * Checks to see that a user has been blocked by looking at the "is blocked"
	 * button on their profile page
	 * 
	 * @step. ^User info should be shown with Block button$
	 * 
	 * @throws Throwable
	 */
	@Then("^User info should be shown with Block button$")
	public void UserShouldBeShownWithUnBlockButton() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isUnblockBtnVisible());
	}

	/**
	 * Clicks the unblock button in the other user's profile page
	 * 
	 * @step. ^I click Unblock button$
	 * 
	 * @throws Throwable
	 */
	@Then("^I click Unblock button$")
	public void IClickUnblockButton() throws Throwable {
		PagesCollection.androidPage = PagesCollection.otherUserPersonalInfoPage
				.clickUnblockBtn();
	}

	/**
	 * -unused
	 * 
	 * @step. ^I see correct background image$
	 * 
	 * @throws Throwable
	 */
	@Then("^I see correct background image$")
	public void ThenISeeCorrectBackgroundImage() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isBackGroundImageCorrect(BG_IMAGE_NAME));
	}

	// ------ Group
	// Separate steps file?

	public static final String GROUP_CHAT_NAME = "TempTestChat";

	/**
	 * Taps on a contact from the group details page (seems out of place)
	 * 
	 * @step. ^I tap on group chat contact (.*)$
	 * 
	 * @param contact
	 * @throws Throwable
	 */
	@When("^I tap on group chat contact (.*)$")
	public void WhenITapOnGroupChatContact(String contact) throws Throwable {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.androidPage = PagesCollection.otherUserPersonalInfoPage
				.tapOnParticipant(contact);
		if (PagesCollection.androidPage instanceof OtherUserPersonalInfoPage) {
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.androidPage;
		}

		// This needs to be moved eventually
		PagesCollection.unknownUserDetailsPage = (UnknownUserDetailsPage) PagesCollection.androidPage
				.instantiatePage(UnknownUserDetailsPage.class);
	}

	/**
	 * -unused
	 * 
	 * @step. ^I swipe right on other user profile page$
	 * 
	 * @throws Throwable
	 */
	@When("^I swipe right on other user profile page$")
	public void WhenISwipeRightOnGroupChatInfoPage() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.otherUserPersonalInfoPage
				.swipeRight(500);
	}

	/**
	 * Presses the ellipses ("...") on the right hand side of the other user's
	 * profile page -unclear
	 * 
	 * @step. ^I press Right conversation button$
	 * 
	 * @throws Throwable
	 */
	@When("^I press options menu button$")
	public void WhenIPressOptionsMenuButton() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressOptionsMenuButton();
	}

	/**
	 * Presses the leave conversation button in the conversation settings page
	 * -outofplace
	 * 
	 * @step. ^I press Leave conversation button$
	 * 
	 * @throws Throwable
	 */
	@When("^I press Leave conversation button$")
	public void WhenIPressLeaveConversationButton() throws Throwable {
		PagesCollection.contactListPage = PagesCollection.otherUserPersonalInfoPage
				.pressLeaveButton();
	}

	/**
	 * Presses the "silence conversation" button in the conversation settings
	 * page
	 * 
	 * @step. ^I press Silence conversation button$
	 * 
	 * @throws Throwable
	 */
	@When("^I press Silence conversation button$")
	public void WhenIPressSilenceConversationButton() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressSilenceButton();
	}

	/**
	 * Presses the "notify conversation" button in the conversation settings
	 * page (Note, this performs the same action as the press silence button)
	 * 
	 * @step. ^I press Notify conversation button$
	 * 
	 * @throws Throwable
	 */
	@When("^I press Notify conversation button$")
	public void WhenIPressNotifyConversationButton() throws Throwable {
		WhenIPressSilenceConversationButton();
	}

	/**
	 * Confirms the current user's decision to leave a conversation -outofplace
	 * 
	 * @step. ^I confirm leaving$
	 * 
	 * @throws Throwable
	 */
	@When("^I confirm leaving$")
	public void WhenIConfirmLeaving() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressConfirmBtn();
	}

	/**
	 * -duplicate of WhenITapOnGroupChatContact(String)
	 * 
	 * @step. ^I select contact (.*)$
	 * 
	 * @param name
	 * @throws Throwable
	 */
	@When("^I select contact (.*)$")
	public void WhenISelectContact(String name) throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		PagesCollection.androidPage = PagesCollection.otherUserPersonalInfoPage
				.tapOnParticipant(name);
	}

	/**
	 * Checks to see that the conversation name is what is expected -outofplace
	 * 
	 * @step. ^I see that the conversation name is (.*)$
	 * 
	 * @param name
	 * @throws Exception
	 */
	@Then("^I see that the conversation name is (.*)$")
	public void IVerifyCorrectConversationName(String name) throws Exception {
		Assert.assertEquals(
				PagesCollection.otherUserPersonalInfoPage.getConversationName(),
				name);
	}

	/**
	 * Checks to see that correct avatars for 2 given users appear -outofplace
	 * 
	 * @step. ^I see the correct participant (.*) and (.*) avatars$
	 * 
	 * @param contact1
	 * @param contact2
	 * @throws Exception
	 */
	@Then("^I see the correct participant (.*) and (.*) avatars$")
	public void ISeeCorrectParticipantAvatars(String contact1, String contact2)
			throws Exception {
		for (String contactName : new String[] { contact1, contact2 }) {
			contactName = usrMgr.replaceAliasesOccurences(contact1,
					FindBy.NAME_ALIAS);
			Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
					.isParticipantAvatarVisible(contactName));
		}
	}

	/**
	 * Checks to see that the correct number of users appears in the group name
	 * title -outofplace
	 * 
	 * @step. ^I see the correct number of participants in the title (.*)$
	 * 
	 * @param realNumberOfParticipants
	 * @throws IOException
	 */
	@Then("^I see the correct number of participants in the title (.*)$")
	public void IVerifyParticipantNumber(String realNumberOfParticipants)
			throws IOException {
		Assert.assertEquals(PagesCollection.otherUserPersonalInfoPage
				.getSubHeader().toLowerCase(), realNumberOfParticipants
				+ " people");
	}

	/**
	 * Checks to see a given user does not exist in the group chat info page
	 * -outofplace
	 * 
	 * @step. ^I do not see (.*) on group chat info page$
	 * 
	 * @param contact
	 * @throws Throwable
	 */
	@Then("^I do not see (.*) on group chat info page$")
	public void ThenIDoNotSeeOnGroupChatInfoPage(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertFalse(PagesCollection.otherUserPersonalInfoPage
				.isParticipantExists(contact));
	}

	/**
	 * Returns to the group chat dialog page
	 * 
	 * @step. ^I return to group chat page$
	 * 
	 * @throws Throwable
	 */
	@Then("^I return to group chat page$")
	public void ThenIReturnToGroupChatPage() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.otherUserPersonalInfoPage
				.tabBackButton();
	}

	/**
	 * Renames a group conversation by first tapping on the participants header
	 * and then sending the message to the text input field
	 * 
	 * @step. ^I rename group conversation to (.*)$
	 * 
	 * @param newConversationName
	 *            the new conversation name
	 * @throws Throwable
	 */
	@Then("^I rename group conversation to (.*)$")
	public void ThenIRenameGroupConversationTo(String newConversationName)
			throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.tapOnParticipantsHeader();
		PagesCollection.otherUserPersonalInfoPage
				.renameGroupChat(newConversationName);
	}

	/**
	 * Check that all required 1:1 option menu elements are showed correctly
	 * 
	 * @step. ^I see correct 1:1 options menu$
	 * 
	 * @throws Throwable
	 */
	@Then("^I see correct 1:1 options menu$")
	public void ThenISeeOneToOneOptionsMenu() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.areOneToOneMenuOptionsVisible());
	}

	/**
	 * Check that any UI content of other user profile is not showed
	 * 
	 * @step. ^I do not see profile page$
	 * 
	 * @throws Exception
	 */
	@Then("^I do not see profile page$")
	public void IDoNotSeeContactProfile() throws Exception {
		Assert.assertTrue(
				"Contact profile page is visible, but expected not to be.",
				PagesCollection.otherUserPersonalInfoPage
						.isOneToOneUserProfileUIContentNotVisible());
	}

	/**
	 * Check that any UI content of 1:1 options menu is not showed
	 * 
	 * @step. ^I do not see 1:1 options menu$
	 * 
	 * @throws Throwable
	 */
	@Then("^I do not see 1:1 options menu$")
	public void ThenIDoNotSeeOptionsMenu() throws Throwable {
		Assert.assertTrue(
				"1on1 options menu is visible, but expected not to be.",
				PagesCollection.otherUserPersonalInfoPage
						.areOneToOneMenuOptionsNotVisible());
	}

	/**
	 * Check that any UI content of other user profile is not showed
	 * 
	 * @step. ^I see profile page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see profile page$")
	public void ISeeCorrectContactProfile() throws Exception {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isOneToOneUserProfileUIContentVisible());
	}

	/**
	 * Perform small swipe down on the page
	 * 
	 * @step. ^I do small swipe down$
	 * 
	 * @throws Exception
	 */
	@When("^I do small swipe down")
	public void IDoSmallSwipeDown() throws Exception {
		PagesCollection.androidPage.swipeByCoordinates(2000, 50, 50, 50, 53);
	}

	/**
	 * Checks to see that you are on the page to connect with a user who you
	 * have not yet connected with, but are in a group conversation with
	 * 
	 * @step. ^I see connect to unconnected user page with user (.*)$
	 * 
	 * @param username
	 *            the user who your are not connected to
	 * 
	 * @throws Exception
	 */
	@Then("^I see connect to unconnected user page with user (.*)$")
	public void ISeeConnectToUnconnectedUserPageWithUser(String username)
			throws Exception {
		username = usrMgr.findUserByNameOrNameAlias(username).getName();

		Assert.assertTrue(PagesCollection.unknownUserDetailsPage
				.isConnectButtonVisible());
		Assert.assertEquals(username,
				PagesCollection.unknownUserDetailsPage.getOtherUsersName());
	}

	/**
	 * Clicks on the connect button when viewing the user details of an
	 * unconnected user from a group chat
	 * 
	 * @step. ^I click on the unconnected user page connect or pending button$
	 * 
	 * @throws Exception
	 */
	@Then("^I click on the unconnected user page connect or pending button$")
	public void IClickOnUnconnectedUserConnectButton() throws Exception {
		PagesCollection.connectToPage = PagesCollection.unknownUserDetailsPage
				.tapConnectAndPendingButton();
	}

	/**
	 * Checks to see that the previous "connect" button is now a pending
	 * TextView
	 * 
	 * @step. ^I see connect to unconnected user page pending button$
	 * 
	 * @throws Exception
	 */
	@Then("^I see connect to unconnected user page pending button$")
	public void ISeeNoEditTexts() throws Exception {
		Assert.assertTrue(PagesCollection.unknownUserDetailsPage
				.isPendingButtonVisible());
	}
}
