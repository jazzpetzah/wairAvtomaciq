package com.wearezeta.auto.osx.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationPageSteps {
	 private String randomMessage;
	 private int beforeNumberOfKnocks = -1;
	 
	 @When("I write random message")
	 public void WhenIWriteRandomMessage() {	
		 randomMessage = CommonUtils.generateGUID();
		 CommonSteps.senderPages.getConversationPage().writeNewMessage(randomMessage);
	 }
	 
	 @When("I send message")
	 public void WhenISendMessage() {
		 CommonSteps.senderPages.getConversationPage().sendNewMessage();
	 }
	 
	 @Then("I see random message in conversation")
	 public void ThenISeeRandomMessageInConversation() {
		 Assert.assertTrue(CommonSteps.senderPages.getConversationPage().isMessageSent(randomMessage));
	 }
	 
	 @When("I am knocking to user")
	 public void WhenIAmKnockingToUser() {
		 if (beforeNumberOfKnocks < 0) {
			 beforeNumberOfKnocks =
				 CommonSteps.senderPages.getConversationPage()
				 		.calcMessageEntries(OSXLocators.YOU_KNOCKED_MESSAGE);
		 }
		 CommonSteps.senderPages.getConversationPage().knock();
	 }
	 
	 @Then("I see message (.*) in conversation")
	 public void ThenISeeMessageInConversation(String message) {
		 if (message.equals(OSXLocators.YOU_KNOCKED_MESSAGE)) {
			 int afterNumberOfKnocks = CommonSteps.senderPages.getConversationPage().calcMessageEntries(message);
			 Assert.assertTrue(afterNumberOfKnocks == beforeNumberOfKnocks+1);
		 } else {
			 Assert.assertTrue(CommonSteps.senderPages.getConversationPage().isMessageExist(message));
		 }
	 }
}
