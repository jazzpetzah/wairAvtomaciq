package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationInfoPageSteps {
	@When("I choose user (.*) in Conversation info")
	public void WhenIChooseUserInConversationInfo(String user) throws MalformedURLException, IOException {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		CommonSteps.senderPages.setConversationInfoPage(new ConversationInfoPage(
				 CommonUtils.getUrlFromConfig(ConversationInfoPage.class),
				 CommonUtils.getAppPathFromConfig(ConversationInfoPage.class)
				 ));
		CommonSteps.senderPages.getConversationInfoPage().selectUser(user);
		CommonSteps.senderPages.getConversationInfoPage().selectUserIfNotSelected(user);
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
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
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
}
