package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;
import com.wearezeta.auto.osx.pages.OSXPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationInfoPageSteps {
	private static final Logger log = ZetaLogger.getLog(ConversationInfoPageSteps.class.getSimpleName());
	
	@When("I choose user (.*) in Conversation info")
	public void WhenIChooseUserInConversationInfo(String user) throws MalformedURLException, IOException {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		CommonSteps.senderPages.setConversationInfoPage(new ConversationInfoPage(
				 CommonUtils.getOsxAppiumUrlFromConfig(ConversationInfoPage.class),
				 CommonUtils.getOsxApplicationPathFromConfig(ConversationInfoPage.class)
				 ));
		CommonSteps.senderPages.getConversationInfoPage().selectUser(user);
		CommonSteps.senderPages.getConversationInfoPage().selectUserIfNotSelected(user);
	}
	
	@Then("I do not see user (.*) in Conversation info")
	public void IDontSeeUserInConversationInfo(String user) throws MalformedURLException, IOException {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		CommonSteps.senderPages.setConversationInfoPage(new ConversationInfoPage(
				 CommonUtils.getOsxAppiumUrlFromConfig(ConversationInfoPage.class),
				 CommonUtils.getOsxApplicationPathFromConfig(ConversationInfoPage.class)
				 ));
		Assert.assertTrue(CommonSteps.senderPages.getConversationInfoPage().userIsNotExistInConversation(user));
	}
	
	@When("I remove selected user from conversation")
	public void WhenIRemoveSelectedUserFromConversation() {
		CommonSteps.senderPages.getConversationInfoPage().removeUser();
	}
	
	@When("I leave conversation")
	public void WhenILeaveConversation() {
		CommonSteps.senderPages.getConversationInfoPage().leaveConversation();
	}
	
	@Then("I see conversation name (.*) in conversation info")
	public void ISeeConversationNameInConversationInfo(String contact) {
		if (contact.equals(OSXLocators.RANDOM_KEYWORD)) {
			contact = CommonSteps.senderPages.getConversationInfoPage().getCurrentConversationName();
		} else {
			contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		}
		Assert.assertTrue(CommonSteps.senderPages.getConversationInfoPage().isConversationNameEquals(contact));
	}
	
	@Then("I see that conversation has (.*) people")
	public void ISeeThatConversationHasPeople(int expectedNumberOfPeople) {
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		int actualNumberOfPeople = conversationInfo.numberOfPeopleInConversation();
		Assert.assertTrue("Actual number of people in chat (" + actualNumberOfPeople
				+ ") is not the same as expected (" + expectedNumberOfPeople +")",
				actualNumberOfPeople == expectedNumberOfPeople);
	}
	
	@Then("I see (.*) participants avatars")
	public void ISeeParticipantsAvatars(int number) {
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		int actual = conversationInfo.numberOfParticipantsAvatars();
		Assert.assertTrue("Actual number of avatars (" + actual
				+ ") is not the same as expected (" + number +")",
				actual == number);
	}
	
	@When("I select to remove user from group chat")
	public void ISelectToRemoveUserFromGroupChat() {
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		conversationInfo.tryRemoveUser();
	}
	
	@Then("I see confirmation request about removing user")
	public void ISeeConfirmationRequestAboutRemovingUser() {
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		Assert.assertTrue(
				"There is no confirmation request on removing user from group chat",
				conversationInfo.isRemoveUserConfirmationAppear());
	}
	
	@Then("I see user (.*) personal info")
	public void ISeeUserPersonalInfo(String contact) {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		conversationInfo.isContactPersonalInfoAppear(contact);
	}
	
	@When("I return to participant view from personal info")
	public void IReturnToParticipantViewFromPersonalInfo() {
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		conversationInfo.goBackFromUserProfileView();
	}
	
	@Then("^I see (.*) name in Conversation info$")
	public void ISeeContactNameInConversationInfo(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		Assert.assertTrue(conversationInfo.isUserNameDisplayed(contact));
	}

	@Then("^I see (.*) email in Conversation info$")
	public void ISeeContactEmailInConversationInfo(String contact) throws Throwable {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		String email = null;
		for (ClientUser clUser: CommonUtils.contacts) {
			if (clUser.getName().equals(contact)) {
				email = clUser.getEmail();
			}
		}
		Assert.assertNotNull("Can't find an e-mail for contact user " + contact, email);
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		log.debug("Looking for email " + email + " in single chat user info.");
		Assert.assertTrue(conversationInfo.isEmailButtonExists(email.toLowerCase()));
	}

	@Then("^I see (.*) photo in Conversation info$")
	public void ISeeContactPhotoInConversationInfo(String photo) throws Throwable {
		ConversationInfoPage conversationInfo = CommonSteps.senderPages.getConversationInfoPage();
		conversationInfo.openImageInPopup();
		BufferedImage screen = conversationInfo.takeScreenshot();
		BufferedImage picture = ImageUtil.readImageFromFile(OSXPage.imagesPath + photo);
		double score = ImageUtil.getOverlapScore(screen, picture, ImageUtil.RESIZE_FROM1920x1080OPTIMIZED);
		log.debug("Score for comparison of 2 pictures = " + score);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= 0.85, current = " + score,
				score >= 0.85d);
		conversationInfo.closeImagePopup();
	}

	@Then("^I see add new people button$")
	public void ISeeAddNewPeopleButton() throws Throwable {
	    Assert.assertTrue(CommonSteps.senderPages.getConversationInfoPage().isAddPeopleButtonExists());
	}

	@Then("^I see block a person button$")
	public void ISeeBlockAPersonButton() throws Throwable {
	    Assert.assertTrue(CommonSteps.senderPages.getConversationInfoPage().isBlockUserButtonExists());
	}


}
