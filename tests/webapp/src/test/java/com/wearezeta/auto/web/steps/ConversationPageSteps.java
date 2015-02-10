package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationPageSteps {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(ConversationPageSteps.class.getSimpleName());

	private String randomMessage;

	/**
	 * Sends random message (generated GUID) into opened conversation
	 * 
	 * @step. ^I write random message$
	 */
	@When("^I write random message$")
	public void WhenIWriteRandomMessage() {
		randomMessage = CommonUtils.generateGUID();
		IWriteMessage(randomMessage);
	}

	/**
	 * Sends text message to opened conversation
	 * 
	 * @step. ^I write message (.*)$
	 * 
	 * @param message
	 *            text message
	 */
	@When("^I write message (.*)$")
	public void IWriteMessage(String message) {
		PagesCollection.conversationPage.writeNewMessage(message);
	}

	/**
	 * Submits entered message for sending
	 * 
	 * @step. ^I send message$
	 */
	@When("^I send message$")
	public void WhenISendMessage() {
		PagesCollection.conversationPage.sendNewMessage();
	}

	/**
	 * Checks that last sent random message appear in conversation
	 * 
	 * @step. ^I see random message in conversation$
	 * 
	 * @throws AssertionError
	 *             if message did not appear in conversation
	 */
	@Then("^I see random message in conversation$")
	public void ThenISeeRandomMessageInConversation() {
		Assert.assertTrue(PagesCollection.conversationPage
				.isMessageSent(randomMessage));
	}

	/**
	 * Click on the button from conversation that popups user profile
	 * 
	 * @step. I click show user profile button
	 * 
	 * @throws Exception
	 */
	@When("I click show user profile button")
	public void WhenIClickShowUserProfileButton() throws Exception {
		PagesCollection.userProfilePopup = PagesCollection.conversationPage
				.clickShowUserProfileButton();
	}

	/**
	 * Send a picture into current conversation
	 * 
	 * @param pictureName
	 *            the name of a picture file. This file should already exist in
	 *            the ~/Documents folder
	 * @throws Exception
	 */
	@When("^I send picture (.*)")
	public void WhenISendPicture(String pictureName) throws Exception {
		PagesCollection.conversationPage.sendPicture(pictureName);
	}

	/**
	 * Verifies whether previously sent picture exists in the conversation view
	 * 
	 * @param pictureName
	 *            the name of a picture file. This file should already exist in
	 *            the ~/Documents folder
	 * @throws Exception
	 */
	@Then("^I see sent picture (.*) in the conversation view$")
	public void ThenISeeSentPicture(String pictureName) throws Exception {
		Assert.assertTrue(PagesCollection.conversationPage
				.isPictureSent(pictureName));
	}

	/**
	 * Checks action message (e.g. you left, etc.) appear in conversation
	 * 
	 * @step. ^I see (.*) action in conversation$
	 * 
	 * @throws AssertionError
	 *             if action message did not appear in conversation
	 */
	@Then("^I see (.*) action in conversation$")
	public void ThenISeeActionInConversation(String message) {
		Assert.assertTrue(PagesCollection.conversationPage
				.isActionMessageSent(message));
	}
}
