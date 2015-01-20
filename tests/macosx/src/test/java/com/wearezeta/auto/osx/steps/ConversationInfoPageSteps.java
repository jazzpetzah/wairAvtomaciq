package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;
import com.wearezeta.auto.osx.pages.OSXPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private static final Logger log = ZetaLogger.getLog(ConversationInfoPageSteps.class.getSimpleName());
	
	@When("I choose user (.*) in Conversation info")
	public void WhenIChooseUserInConversationInfo(String user) throws Exception {
		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		CommonOSXSteps.senderPages.setConversationInfoPage(new ConversationInfoPage(
				 CommonUtils.getOsxAppiumUrlFromConfig(ConversationInfoPage.class),
				 CommonUtils.getOsxApplicationPathFromConfig(ConversationInfoPage.class)
				 ));
		CommonOSXSteps.senderPages.getConversationInfoPage().selectUser(user);
		CommonOSXSteps.senderPages.getConversationInfoPage().selectUserIfNotSelected(user);
	}
	
	@Then("I do not see user (.*) in Conversation info")
	public void IDontSeeUserInConversationInfo(String user) throws Exception {
		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		CommonOSXSteps.senderPages.setConversationInfoPage(new ConversationInfoPage(
				 CommonUtils.getOsxAppiumUrlFromConfig(ConversationInfoPage.class),
				 CommonUtils.getOsxApplicationPathFromConfig(ConversationInfoPage.class)
				 ));
		Assert.assertTrue(CommonOSXSteps.senderPages.getConversationInfoPage().userIsNotExistInConversation(user));
	}
	
	@When("I remove selected user from conversation")
	public void WhenIRemoveSelectedUserFromConversation() {
		CommonOSXSteps.senderPages.getConversationInfoPage().removeUser();
	}
	
	@When("I leave conversation")
	public void WhenILeaveConversation() {
		CommonOSXSteps.senderPages.getConversationInfoPage().leaveConversation();
	}
	
	@Then("I see conversation name (.*) in conversation info")
	public void ISeeConversationNameInConversationInfo(String contact) {
		if (contact.equals(OSXLocators.RANDOM_KEYWORD)) {
			contact = CommonOSXSteps.senderPages.getConversationInfoPage().getCurrentConversationName();
		} else {
			contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		}
		Assert.assertTrue(CommonOSXSteps.senderPages.getConversationInfoPage().isConversationNameEquals(contact));
	}
	
	@Then("I see that conversation has (.*) people")
	public void ISeeThatConversationHasPeople(int expectedNumberOfPeople) {
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages.getConversationInfoPage();
		int actualNumberOfPeople = conversationInfo.numberOfPeopleInConversation();
		Assert.assertTrue("Actual number of people in chat (" + actualNumberOfPeople
				+ ") is not the same as expected (" + expectedNumberOfPeople +")",
				actualNumberOfPeople == expectedNumberOfPeople);
	}
	
	@Then("I see (.*) participants avatars")
	public void ISeeParticipantsAvatars(int number) {
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages.getConversationInfoPage();
		int actual = conversationInfo.numberOfParticipantsAvatars();
		Assert.assertTrue("Actual number of avatars (" + actual
				+ ") is not the same as expected (" + number +")",
				actual == number);
	}
	
	@When("I select to remove user from group chat")
	public void ISelectToRemoveUserFromGroupChat() {
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages.getConversationInfoPage();
		conversationInfo.tryRemoveUser();
	}
	
	@Then("I see confirmation request about removing user")
	public void ISeeConfirmationRequestAboutRemovingUser() {
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages.getConversationInfoPage();
		Assert.assertTrue(
				"There is no confirmation request on removing user from group chat",
				conversationInfo.isRemoveUserConfirmationAppear());
	}
	
	@Then("I see user (.*) personal info")
	public void ISeeUserPersonalInfo(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages.getConversationInfoPage();
		conversationInfo.isContactPersonalInfoAppear(contact);
	}
	
	@When("I return to participant view from personal info")
	public void IReturnToParticipantViewFromPersonalInfo() {
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages.getConversationInfoPage();
		conversationInfo.goBackFromUserProfileView();
	}
	
	@Then("^I see (.*) name in Conversation info$")
	public void ISeeContactNameInConversationInfo(String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages.getConversationInfoPage();
		Assert.assertTrue(conversationInfo.isUserNameDisplayed(contact));
	}

	@Then("^I see (.*) email in Conversation info$")
	public void ISeeContactEmailInConversationInfo(String contact) throws Throwable {
		ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(contact);
		contact = dstUser.getName();
		String email = dstUser.getEmail();
		Assert.assertNotNull("Can't find an e-mail for contact user " + contact, email);
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages.getConversationInfoPage();
		log.debug("Looking for email " + email + " in single chat user info.");
		Assert.assertTrue(conversationInfo.isEmailButtonExists(email.toLowerCase()));
	}

	@Then("^I dont see (.*) email in Conversation info$")
	public void IDontSeeContactEmailInConversationInfo(String contact) throws Throwable {
		ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(contact);
		contact = dstUser.getName();
		String email = dstUser.getEmail();
		Assert.assertNotNull("Can't find an e-mail for contact user " + contact, email);
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages.getConversationInfoPage();
		log.debug("Looking for email " + email + " in single chat user info.");
		Assert.assertFalse(conversationInfo.isEmailButtonExists(email.toLowerCase()));
	}
	
	@Then("^I see (.*) photo in Conversation info$")
	public void ISeeContactPhotoInConversationInfo(String photo) throws Throwable {
		ConversationInfoPage conversationInfo = CommonOSXSteps.senderPages.getConversationInfoPage();
		conversationInfo.openImageInPopup();
		BufferedImage screen = conversationInfo.takeScreenshot();
		BufferedImage picture = ImageUtil.readImageFromFile(OSXPage.imagesPath + photo);
		
		final double minOverlapScore = 0.8d;
		final double score = ImageUtil.getOverlapScore(screen, picture, ImageUtil.RESIZE_FROM1920x1080OPTIMIZED);
		log.debug("Score for comparison of 2 pictures = " + score);
		Assert.assertTrue(
				String.format(
						"Overlap between two images has no enough score. Expected >= %f, current = %f",
						minOverlapScore, score), score >= minOverlapScore);
		conversationInfo.closeImagePopup();
	}

	@Then("^I see add new people button$")
	public void ISeeAddNewPeopleButton() throws Throwable {
	    Assert.assertTrue(CommonOSXSteps.senderPages.getConversationInfoPage().isAddPeopleButtonExists());
	}

	@Then("^I see block a person button$")
	public void ISeeBlockAPersonButton() throws Throwable {
	    Assert.assertTrue(CommonOSXSteps.senderPages.getConversationInfoPage().isBlockUserButtonExists());
	}

	@Then("^I see open conversation button$")
	public void ISeeOpenConversationButton() {
		Assert.assertTrue(CommonOSXSteps.senderPages.getConversationInfoPage().isOpenConversationButtonExists());
	}
	
	@Then("^I see pending button$")
	public void ISeePendingButton() {
		Assert.assertTrue(CommonOSXSteps.senderPages.getConversationInfoPage().isPendingButtonExists());
	}
	
	@Then("^I see connect button$")
	public void ISeeConnectButton() {
		Assert.assertTrue(CommonOSXSteps.senderPages.getConversationInfoPage().isConnectButtonExists());
	}
	
	@Then("^I see remove person from conversation button$")
	public void ISeeRemovePersonFromConversationButton() {
		Assert.assertTrue(CommonOSXSteps.senderPages.getConversationInfoPage().isRemoveUserFromConversationButtonExists());
	}
	
	@Then("^I see connection request message (.*)$")
	public void ISeeConnectionRequestMessage(String message) {
		Assert.assertTrue(CommonOSXSteps.senderPages.getConversationInfoPage().isSentConnectionRequestMessageExists(message));
	}
}
