package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.pages.popovers.ConnectToPopover;
import com.wearezeta.auto.osx.util.NSPoint;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private static final Logger log = ZetaLogger
			.getLog(ConversationInfoPageSteps.class.getSimpleName());

	@When("I choose user (.*) in Conversation info")
	public void WhenIChooseUserInConversationInfo(String user) throws Exception {
		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		PagesCollection.conversationInfoPage = (ConversationInfoPage) PagesCollection.mainMenuPage
				.instantiatePage(ConversationInfoPage.class);
		PagesCollection.conversationInfoPage
				.setParent(PagesCollection.conversationPage);
		PagesCollection.conversationInfoPage.selectUser(user);
		PagesCollection.conversationInfoPage.selectUserIfNotSelected(user);
	}

	@Then("I do not see user (.*) in Conversation info")
	public void IDontSeeUserInConversationInfo(String user) throws Exception {
		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		PagesCollection.conversationInfoPage = (ConversationInfoPage) PagesCollection.mainMenuPage
				.instantiatePage(ConversationInfoPage.class);
		PagesCollection.conversationInfoPage
				.setParent(PagesCollection.conversationPage);
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.userIsNotExistInConversation(user));
	}

	@When("I remove selected user from conversation")
	public void WhenIRemoveSelectedUserFromConversation() throws Exception {
		PagesCollection.conversationInfoPage.removeUser();
	}

	@When("I leave conversation")
	public void WhenILeaveConversation() throws Exception {
		PagesCollection.conversationInfoPage.leaveConversation();
	}

	@Then("I see conversation name (.*) in conversation info")
	public void ISeeConversationNameInConversationInfo(String contact) {
		if (contact.equals(OSXLocators.RANDOM_KEYWORD)) {
			contact = PagesCollection.conversationPage
					.getCurrentConversationName();
		} else {
			contact = usrMgr.replaceAliasesOccurences(contact,
					FindBy.NAME_ALIAS);
		}
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.isConversationNameEquals(contact));
	}

	@Then("I see that conversation has (.*) people")
	public void ISeeThatConversationHasPeople(int expectedNumberOfPeople)
			throws Exception {
		int actualNumberOfPeople = PagesCollection.conversationInfoPage
				.numberOfPeopleInConversation();
		Assert.assertTrue("Actual number of people in chat ("
				+ actualNumberOfPeople + ") is not the same as expected ("
				+ expectedNumberOfPeople + ")",
				actualNumberOfPeople == expectedNumberOfPeople);
	}

	@Then("I see (.*) participants avatars")
	public void ISeeParticipantsAvatars(int number) throws Exception {
		int actual = PagesCollection.conversationInfoPage
				.numberOfParticipantsAvatars();
		Assert.assertTrue("Actual number of avatars (" + actual
				+ ") is not the same as expected (" + number + ")",
				actual == number);
	}

	@When("I select to remove user from group chat")
	public void ISelectToRemoveUserFromGroupChat() {
		PagesCollection.conversationInfoPage.tryRemoveUser();
	}

	@Then("I see confirmation request about removing user")
	public void ISeeConfirmationRequestAboutRemovingUser() {
		Assert.assertTrue(
				"There is no confirmation request on removing user from group chat",
				PagesCollection.conversationInfoPage
						.isRemoveUserConfirmationAppear());
	}

	@Then("I see user (.*) personal info")
	public void ISeeUserPersonalInfo(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.conversationInfoPage
				.isContactPersonalInfoAppear(contact);
	}

	@When("I return to participant view from personal info")
	public void IReturnToParticipantViewFromPersonalInfo() {
		PagesCollection.conversationInfoPage.goBackFromUserProfileView();
	}

	@Then("^I see (.*) name in Conversation info$")
	public void ISeeContactNameInConversationInfo(String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.isUserNameDisplayed(contact));
	}

	@Then("^I see (.*) email in Conversation info$")
	public void ISeeContactEmailInConversationInfo(String contact)
			throws Throwable {
		ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(contact);
		contact = dstUser.getName();
		String email = dstUser.getEmail();
		Assert.assertNotNull(
				"Can't find an e-mail for contact user " + contact, email);
		log.debug("Looking for email " + email + " in single chat user info.");
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.isEmailButtonExists(email.toLowerCase()));
	}

	@Then("^I dont see (.*) email in Conversation info$")
	public void IDontSeeContactEmailInConversationInfo(String contact)
			throws Throwable {
		ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(contact);
		contact = dstUser.getName();
		String email = dstUser.getEmail();
		Assert.assertNotNull(
				"Can't find an e-mail for contact user " + contact, email);
		log.debug("Looking for email " + email + " in single chat user info.");
		Assert.assertFalse(PagesCollection.conversationInfoPage
				.isEmailButtonExists(email.toLowerCase()));
	}

	@Then("^I see (.*) photo in Conversation info$")
	public void ISeeContactPhotoInConversationInfo(String photo)
			throws Throwable {
		PagesCollection.conversationInfoPage.openImageInPopup();
		BufferedImage screen = PagesCollection.conversationInfoPage
				.takeScreenshot().orElseThrow(AssertionError::new);
		BufferedImage picture = ImageUtil
				.readImageFromFile(OSXExecutionContext.userDocuments + "/"
						+ photo);

		NSPoint avatarWinSize = PagesCollection.conversationInfoPage
				.retrieveAvatarFullScreenWindowSize();

		if (OSXCommonUtils.isRetinaDisplay(screen.getWidth(),
				screen.getHeight())) {
			avatarWinSize = new NSPoint(avatarWinSize.x() * 2,
					avatarWinSize.y() * 2);
		}

		final double minOverlapScore = 0.8d;
		final double score = ImageUtil.getOverlapScore(screen, picture,
				ImageUtil.RESIZE_TEMPLATE_TO_RESOLUTION, avatarWinSize.x(),
				avatarWinSize.y());
		log.debug("Score for comparison of 2 pictures = " + score);
		Assert.assertTrue(
				String.format(
						"Overlap between two images has no enough score. Expected >= %f, current = %f",
						minOverlapScore, score), score >= minOverlapScore);
		PagesCollection.conversationInfoPage.closeImagePopup();
	}

	@Then("^I see add new people button$")
	public void ISeeAddNewPeopleButton() throws Throwable {
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.isAddPeopleButtonExists());
	}

	@Then("^I see block a person button$")
	public void ISeeBlockAPersonButton() throws Throwable {
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.isBlockUserButtonExists());
	}

	@Then("^I see open conversation button$")
	public void ISeeOpenConversationButton() throws Exception {
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.isOpenConversationButtonExists());
	}

	@Then("^I do not see open conversation button$")
	public void IDoNotSeeOpenConversationButton() throws Exception {
		Assert.assertFalse(PagesCollection.conversationInfoPage
				.isOpenConversationButtonExists());
	}

	@Then("^I click on connect button on people popover$")
	public void IClickOnConnectButtonOnPeoplePopover() throws Exception {
		PagesCollection.popover = PagesCollection.conversationInfoPage
				.connectToUser();
	}

	@Then("^I see pending button$")
	public void ISeePendingButton() {
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.isPendingButtonExists());
	}

	@Then("^I see connect button$")
	public void ISeeConnectButton() throws Exception {
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.isConnectButtonExists());
	}

	@Then("^I see remove person from conversation button$")
	public void ISeeRemovePersonFromConversationButton() throws Exception {
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.isRemoveUserFromConversationButtonExists());
	}

	@Then("^I see connection request message (.*)$")
	public void ISeeConnectionRequestMessage(String message) throws Exception {
		Assert.assertTrue(PagesCollection.conversationInfoPage
				.isSentConnectionRequestMessageExists(message));
	}

	/**
	 * Checks that user is suggested to send connection request to selected user
	 * 
	 * @step. ^I see connect popover$
	 * 
	 * @throws AssertionError
	 *             if there is no send request dialog
	 */
	@Then("^I see connect popover$")
	public void ISeeConnectPopover() throws Exception {
		Assert.assertTrue("There is no connect to user popover opened.",
				((ConnectToPopover) PagesCollection.popover).isVisible());
	}
}
