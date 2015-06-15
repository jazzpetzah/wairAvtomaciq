package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private OtherUserPersonalInfoPage getOtherUserPersonalInfoPage()
			throws Exception {
		return (OtherUserPersonalInfoPage) pagesCollection
				.getPage(OtherUserPersonalInfoPage.class);
	}

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
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getOtherUserPersonalInfoPage().isOtherUserNameVisible(name);
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
		getOtherUserPersonalInfoPage().pressOptionsMenuButton();
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
		Assert.assertTrue("Warning message is not shown",
				getOtherUserPersonalInfoPage().isConversationAlertVisible());
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
		getOtherUserPersonalInfoPage().pressConfirmBtn();
	}

	/**
	 * Confirms the block of another user when they send a connection request
	 * 
	 * @step. ^I confirm block$
	 * 
	 * @throws Exception
	 */
	@When("^I confirm block$")
	public void WhenIConfirmBlock() throws Exception {
		getOtherUserPersonalInfoPage().pressConfirmBtn();
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
		getOtherUserPersonalInfoPage().tapAddContactBtn();
	}

	/**
	 * Blocks a user from the user profile page
	 * 
	 * @step. ^I Press Block button$
	 * @throws Exception
	 */
	@When("^I Press Block button$")
	public void IPressBlockButton() throws Exception {
		getOtherUserPersonalInfoPage().clickBlockBtn();
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
		Assert.assertTrue("User name is not visible",
				getOtherUserPersonalInfoPage().isOtherUserNameVisible(contact));
		Assert.assertTrue("User email is not visible",
				getOtherUserPersonalInfoPage().isOtherUserMailVisible(email));
	}

	/**
	 * Checks to see that a user has been blocked by looking at the "is blocked"
	 * button on their profile page
	 * 
	 * @step. ^User info should be shown with Block button$
	 * 
	 * @throws Exception
	 */
	@Then("^User info should be shown with Block button$")
	public void UserShouldBeShownWithUnBlockButton() throws Exception {
		Assert.assertTrue("Unblock button is not visible",
				getOtherUserPersonalInfoPage().isUnblockBtnVisible());
	}

	/**
	 * Clicks the unblock button in the other user's profile page
	 * 
	 * @step. ^I click Unblock button$
	 * 
	 * @throws Exception
	 */
	@Then("^I click Unblock button$")
	public void IClickUnblockButton() throws Exception {
		getOtherUserPersonalInfoPage().clickUnblockBtn();
	}

	/**
	 * -unused
	 * 
	 * @step. ^I see correct background image$
	 * 
	 * @throws Exception
	 */
	@Then("^I see correct background image$")
	public void ThenISeeCorrectBackgroundImage() throws Exception {
		Assert.assertTrue(
				"The background image seems to be incorrect",
				getOtherUserPersonalInfoPage().isBackGroundImageCorrect(
						BG_IMAGE_NAME));
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
	 * @throws Exception
	 */
	@When("^I tap on group chat contact (.*)$")
	public void WhenITapOnGroupChatContact(String contact) throws Exception {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		getOtherUserPersonalInfoPage().tapOnParticipant(contact);
	}

	/**
	 * Presses the ellipses ("...") on the right hand side of the other user's
	 * profile page -unclear
	 * 
	 * @step. ^I press Right conversation button$
	 * 
	 * @throws Exception
	 */
	@When("^I press options menu button$")
	public void WhenIPressOptionsMenuButton() throws Exception {
		getOtherUserPersonalInfoPage().pressOptionsMenuButton();
	}

	/**
	 * Presses the leave conversation button in the conversation settings page
	 * -outofplace
	 * 
	 * @step. ^I press Leave conversation button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Leave conversation button$")
	public void WhenIPressLeaveConversationButton() throws Exception {
		getOtherUserPersonalInfoPage().pressLeaveButton();
	}

	/**
	 * Presses the "silence conversation" button in the conversation settings
	 * page
	 * 
	 * @step. ^I press Silence conversation button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Silence conversation button$")
	public void WhenIPressSilenceConversationButton() throws Exception {
		getOtherUserPersonalInfoPage().pressSilenceButton();
	}

	/**
	 * Presses the "notify conversation" button in the conversation settings
	 * page (Note, this performs the same action as the press silence button)
	 * 
	 * @step. ^I press Notify conversation button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Notify conversation button$")
	public void WhenIPressNotifyConversationButton() throws Exception {
		// FIXME: Notify and Silence menu items should have different locators
		WhenIPressSilenceConversationButton();
	}

	/**
	 * Confirms the current user's decision to leave a conversation -outofplace
	 * 
	 * @step. ^I confirm leaving$
	 * 
	 * @throws Exception
	 */
	@When("^I confirm leaving$")
	public void WhenIConfirmLeaving() throws Exception {
		getOtherUserPersonalInfoPage().pressConfirmBtn();
	}

	/**
	 * -duplicate of WhenITapOnGroupChatContact(String)
	 * 
	 * @step. ^I select contact (.*)$
	 * 
	 * @param name
	 * @throws Exception
	 */
	@When("^I select contact (.*)$")
	public void WhenISelectContact(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		getOtherUserPersonalInfoPage().tapOnParticipant(name);
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
		Assert.assertEquals(getOtherUserPersonalInfoPage()
				.getConversationName(), name);
	}

	/**
	 * Checks to see that correct avatars for given users appear
	 * 
	 * @step. ^I see the correct participant avatars for (.*)
	 * 
	 * @param contacts
	 *            one or more contacs separated by comma
	 * @throws Exception
	 */
	@Then("^I see the correct participant avatars for (.*)")
	public void ISeeCorrectParticipantAvatars(String contacts) throws Exception {
		for (String contactName : CommonSteps.splitAliases(contacts)) {
			contactName = usrMgr.findUserByNameOrNameAlias(contactName)
					.getName();
			Assert.assertTrue(
					String.format("The avatar for '%s' is not visible",
							contactName),
					getOtherUserPersonalInfoPage().isParticipantAvatarVisible(
							contactName));
		}
	}

	/**
	 * Checks to see that the correct number of users appears in the group name
	 * title -outofplace
	 * 
	 * @step. ^I see the correct number of participants in the title (.*)$
	 * 
	 * @param realNumberOfParticipants
	 * @throws Exception
	 */
	@Then("^I see the correct number of participants in the title (.*)$")
	public void IVerifyParticipantNumber(String realNumberOfParticipants)
			throws Exception {
		Assert.assertEquals(getOtherUserPersonalInfoPage().getSubHeader()
				.toLowerCase(), realNumberOfParticipants + " people");
	}

	/**
	 * Checks to see a given user does not exist in the group chat info page
	 * -outofplace
	 * 
	 * @step. ^I do not see (.*) on group chat info page$
	 * 
	 * @param contact
	 * @throws Exception
	 */
	@Then("^I do not see (.*) on group chat info page$")
	public void ThenIDoNotSeeOnGroupChatInfoPage(String contact)
			throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertFalse(getOtherUserPersonalInfoPage().isParticipantExists(
				contact));
	}

	/**
	 * Returns to the group chat dialog page
	 * 
	 * @step. ^I return to group chat page$
	 * 
	 * @throws Exception
	 */
	@Then("^I return to group chat page$")
	public void ThenIReturnToGroupChatPage() throws Exception {
		getOtherUserPersonalInfoPage().tabBackButton();
	}

	/**
	 * Renames a group conversation by first tapping on the participants header
	 * and then sending the message to the text input field
	 * 
	 * @step. ^I rename group conversation to (.*)$
	 * 
	 * @param newConversationName
	 *            the new conversation name
	 * @throws Exception
	 */
	@Then("^I rename group conversation to (.*)$")
	public void ThenIRenameGroupConversationTo(String newConversationName)
			throws Exception {
		getOtherUserPersonalInfoPage().tapOnParticipantsHeader();
		getOtherUserPersonalInfoPage().renameGroupChat(newConversationName);
	}

	/**
	 * Check that all required 1:1 option menu elements are showed correctly
	 * 
	 * @step. ^I see correct 1:1 options menu$
	 * 
	 * @throws Exception
	 */
	@Then("^I see correct 1:1 options menu$")
	public void ThenISeeOneToOneOptionsMenu() throws Exception {
		Assert.assertTrue(getOtherUserPersonalInfoPage()
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
				getOtherUserPersonalInfoPage()
						.isOneToOneUserProfileUIContentNotVisible());
	}

	/**
	 * Check that any UI content of 1:1 options menu is not showed
	 * 
	 * @step. ^I do not see 1:1 options menu$
	 * 
	 * @throws Exception
	 */
	@Then("^I do not see 1:1 options menu$")
	public void ThenIDoNotSeeOptionsMenu() throws Exception {
		Assert.assertTrue(
				"1on1 options menu is visible, but expected not to be.",
				getOtherUserPersonalInfoPage()
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
		Assert.assertTrue(getOtherUserPersonalInfoPage()
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
		getOtherUserPersonalInfoPage().swipeByCoordinates(2500, 50, 50, 50, 53);
	}

	private UnknownUserDetailsPage getUnknownUserDetailsPage() throws Exception {
		return (UnknownUserDetailsPage) pagesCollection
				.getPage(UnknownUserDetailsPage.class);
	}

	/**
	 * Checks to see that you are on the page to connect with a user who you
	 * have not yet connected with, but are in a group conversation with
	 * 
	 * @step. ^I see user name (.*) on non connected user page$
	 * 
	 * @param username
	 *            the user who your are not connected to
	 * 
	 * @throws Exception
	 */
	@Then("^I see user name (.*) on non connected user page$")
	public void ISeeConnectToUnconnectedUserPageWithUser(String username)
			throws Exception {
		username = usrMgr.findUserByNameOrNameAlias(username).getName();
		Assert.assertTrue(String.format(
				"User name '%s' does not exist in non connected page header",
				username),
				getUnknownUserDetailsPage().isNameExistInHeader(username));
	}

	/**
	 * Clicks on the connect button when viewing the user details of an
	 * unconnected user from a group chat
	 * 
	 * @step. ^I click Connect button on non connected user page$
	 * 
	 * @throws Exception
	 */
	@Then("^I click Connect button on non connected user page$")
	public void IClickOnUnconnectedUserConnectButton() throws Exception {
		getUnknownUserDetailsPage().tapConnectButton();
	}

	/**
	 * Clicks on the pending button when viewing the user details of a pending
	 * user from a group chat
	 * 
	 * @step. ^I click Pending button on pending user page$
	 * 
	 * @throws Exception
	 */
	@When("^I click Pending button on pending user page$")
	public void IClickPendingButton() throws Exception {
		getUnknownUserDetailsPage().tapPendingButton();
	}

	/**
	 * Verify whether Pending button is visible on pending user page in a group
	 * chat
	 * 
	 * @step. ^I see Pending button on pending user page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see Pending button on pending user page$")
	public void ISeePendingButton() throws Exception {
		Assert.assertTrue("Pending button is not visible, but it should be",
				getUnknownUserDetailsPage().isPendingButtonVisible());
	}
}
