package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OtherUserPersonalInfoPageSteps {
	private final String BG_IMAGE_NAME = "aqaPictureContactBG.png";
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@When("^I see (.*) user profile page$")
	public void WhenISeeOherUserProfilePage(String name) {
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

	@When("^I swipe down other user profile page$")
	public void WhenISwipeDownOtherUserProfilePage() throws Exception {
		PagesCollection.peoplePickerPage = (PeoplePickerPage) PagesCollection.otherUserPersonalInfoPage
				.swipeDown(1000);
	}

	@When("^I swipe up on other user profile page$")
	public void WhenISwipeUpOnOtherUserProfilePage() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.swipeUp(500);
	}

	@When("^I click Remove$")
	public void WhenIClickRemove() throws Throwable {
		//TODO: check for native button click
		PagesCollection.otherUserPersonalInfoPage
				.pressOptionsMenuButton();
	}

	@When("^I see warning message$")
	public void WhenISeeWarningMessage() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isRemoveFromConversationAlertVisible());
	}

	@When("^I confirm remove$")
	public void WhenIConfirmRemove() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressRemoveConfirmBtn();
	}

	@When("^I confirm block$")
	public void WhenIConfirmBlock() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressRemoveConfirmBtn();
	}

	@When("^I press add contact button$")
	public void WhenIPressAddContactButton() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.otherUserPersonalInfoPage
				.tapAddContactBtn();
	}

	@When("^I Press Block button$")
	public void IPressBlockButton() {
		PagesCollection.otherUserPersonalInfoPage.clickBlockBtn();

	}

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

	@Then("^User info should be shown with Block button$")
	public void UserShouldBeShownWithUnBlockButton() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isUnblockBtnVisible());
	}

	@Then("^I click Unblock button$")
	public void IClickUnblockButton() throws Throwable {
		PagesCollection.androidPage = PagesCollection.otherUserPersonalInfoPage
				.clickUnblockBtn();
	}

	@Then("^I see correct background image$")
	public void ThenISeeCorrectBackgroundImage() throws Throwable {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isBackGroundImageCorrect(BG_IMAGE_NAME));
	}

	// ------ Group

	public static final String GROUP_CHAT_NAME = "TempTestChat";

	@When("^I tap on group chat contact (.*)$")
	public void WhenITapOnGroupChatContact(String contact) throws Throwable {
		try {
			contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.androidPage = PagesCollection.otherUserPersonalInfoPage
				.tapOnContact(contact);
		if (PagesCollection.androidPage instanceof OtherUserPersonalInfoPage) {
			PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.androidPage;
		}
	}

	@When("^I swipe right on other user profile page$")
	public void WhenISwipeRightOnGroupChatInfoPage() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.otherUserPersonalInfoPage
				.swipeRight(500);
	}

	@When("^I press options menu button$")
	public void WhenIPressOptionsMenuButton() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage
				.pressOptionsMenuButton();
	}

	@When("^I press Leave conversartion button$")
	public void WhenIPressLeaveConversationButton() throws Throwable {
		PagesCollection.contactListPage = PagesCollection.otherUserPersonalInfoPage
				.pressLeaveButton();
	}

	@When("^I press Silence conversartion button$")
	public void WhenIPressSilenceConversationButton() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressSilenceButton();
	}

	@When("^I confirm leaving$")
	public void WhenIConfirmLeaving() throws Throwable {
		PagesCollection.otherUserPersonalInfoPage.pressRemoveConfirmBtn();
	}

	@When("^I select contact (.*)$")
	public void WhenISelectContact(String name) throws Throwable {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		PagesCollection.androidPage = PagesCollection.otherUserPersonalInfoPage
				.selectContactByName(name);
	}

	@Then("^I see that the conversation name is (.*)$")
	public void IVerifyCorrectConversationName(String name) throws IOException {
		Assert.assertEquals(
				PagesCollection.otherUserPersonalInfoPage.getConversationName(),
				name);
	}

	@Then("^I see the correct participant (.*) and (.*) avatars$")
	public void ISeeCorrectParticipantAvatars(String contact1, String contact2)
			throws Exception {
		try {
			contact1 = usrMgr.findUserByNameOrNameAlias(contact1).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		try {
			contact2 = usrMgr.findUserByNameOrNameAlias(contact2).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isParticipantAvatars(contact1, contact2));
	}

	@Then("^I see the correct number of participants in the title (.*)$")
	public void IVerifyParticipantNumber(String realNumberOfParticipants)
			throws IOException {
		Assert.assertEquals(
				PagesCollection.otherUserPersonalInfoPage.getSubHeader(),
				realNumberOfParticipants + " people");
	}

	@Then("^I do not see (.*) on group chat info page$")
	public void ThenIDoNotSeeOnGroupChatInfoPage(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertFalse(PagesCollection.otherUserPersonalInfoPage
				.isContactExists(contact));
	}

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
				.isOneToOneOptionsMenuFullyVisible());
	}

	/**
	 * Check that any UI content of other user profile is not showed
	 * 
	 * @step. ^I do not see (.*) profile page$
	 * 
	 * @param Contact
	 *            other user name
	 * 
	 * @throws Throwable
	 */
	@Then("^I do not see profile page$")
	public void IDoNotSeeContactProfile() throws Exception {
		Assert.assertFalse(PagesCollection.otherUserPersonalInfoPage
				.isOneToOneUserProfileUIContentVisible());
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
		Assert.assertFalse(PagesCollection.otherUserPersonalInfoPage
				.isOneToOneOptionsMenuUIContentVisible());
	}
	
	/**
	 * Check that any UI content of other user profile is not showed
	 * 
	 * @step. ^I do not see (.*) profile page$
	 * 
	 * @param Contact
	 *            other user name
	 * 
	 * @throws Throwable
	 */
	@Then("^I see profile page$")
	public void ISeeCorrectContactProfile() throws Exception {
		Assert.assertTrue(PagesCollection.otherUserPersonalInfoPage
				.isOneToOneUserProfileFullyVisible());
	}
	
	@When("^I do small swipe down")
	public void IDoSmallSwipeDown() throws Exception {
		PagesCollection.androidPage.swipeByCoordinates(300, 50, 50, 50, 55);
	}
}