package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.ContactListPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationPageSteps {
	 private String randomMessage;
	 private int beforeNumberOfKnocks = -1;
	 private int beforeNumberOfImages = -1;
	 
	 @When("I write random message")
	 public void WhenIWriteRandomMessage() {	
		 randomMessage = CommonUtils.generateGUID();
		 CommonSteps.senderPages.getConversationPage().writeNewMessage(randomMessage);
	 }
	 
	 @When("I send message")
	 public void WhenISendMessage() {
		 CommonSteps.senderPages.getConversationPage().sendNewMessage();
	 }
	 
	 @When("I send picture")
	 public void WhenISendPicture() throws MalformedURLException, IOException {
		 if (beforeNumberOfImages < 0) {
			 beforeNumberOfImages =
				 CommonSteps.senderPages.getConversationPage()
				 		.getNumberOfImageEntries();
		 }
		 CommonSteps.senderPages.getConversationPage().writeNewMessage("");
		 CommonSteps.senderPages.getConversationPage().openChooseImageDialog();
		 CommonSteps.senderPages.setChoosePicturePage(new ChoosePicturePage(
				 CommonUtils.getUrlFromConfig(ContactListPage.class),
				 CommonUtils.getAppPathFromConfig(ContactListPage.class)
				 ));
		 
		 ChoosePicturePage choosePicturePage = CommonSteps.senderPages.getChoosePicturePage();
		 Assert.assertTrue(choosePicturePage.isVisible());
		 
		 choosePicturePage.searchForImage("test.jpg");
         
		 Assert.assertTrue("test.jpg was not found.", choosePicturePage.isOpenButtonEnabled());
		 choosePicturePage.openImage();
	 }
	 
	 @Then("I see random message in conversation")
	 public void ThenISeeRandomMessageInConversation() {
		 Assert.assertTrue(CommonSteps.senderPages.getConversationPage().isMessageSent(randomMessage));
	 }
	 
	 @Then("I see picture in conversation")
	 public void ThenISeePictureInConversation() {
		 int afterNumberOfImages = CommonSteps.senderPages.getConversationPage().getNumberOfImageEntries();
		 
		 boolean isNumberIncreased = false;
		 for (int i = 0; i < 20; i++) {
			 if (afterNumberOfImages == beforeNumberOfImages + 2) {
				 isNumberIncreased = true;
				 break;
			 }
			 try { Thread.sleep(1000); } catch (InterruptedException e) { }
		 }
		 
		 Assert.assertTrue("Incorrect images count: before - "
				 + beforeNumberOfImages + ", after - " + afterNumberOfImages, isNumberIncreased);
	 }
	 
	 @When("I am knocking to user")
	 public void WhenIAmKnockingToUser() {
		 if (beforeNumberOfKnocks < 0) {
			 beforeNumberOfKnocks =
				 CommonSteps.senderPages.getConversationPage()
				 		.getNumberOfMessageEntries(OSXLocators.YOU_KNOCKED_MESSAGE);
		 }
		 CommonSteps.senderPages.getConversationPage().knock();
	 }
	 
	 @Then("I see message (.*) in conversation")
	 public void ThenISeeMessageInConversation(String message) {
		 if (message.equals(OSXLocators.YOU_KNOCKED_MESSAGE)) {
			 boolean isNumberIncreased = false;
			 int afterNumberOfKnocks = -1;
			 for (int i = 0; i < 20; i++) {
				 afterNumberOfKnocks = CommonSteps.senderPages.getConversationPage().getNumberOfMessageEntries(message);
				 if (afterNumberOfKnocks == beforeNumberOfKnocks + 1) {
					 isNumberIncreased = true;
					 break;
				 }
				 try { Thread.sleep(1000); } catch (InterruptedException e) { }
			 }
			 
			 Assert.assertTrue("Incorrect messages count: before - "
					 + beforeNumberOfKnocks + ", after - " + afterNumberOfKnocks, isNumberIncreased);
			 
		} else {
			 Assert.assertTrue(CommonSteps.senderPages.getConversationPage().isMessageExist(message));
		 }
	 }
}
