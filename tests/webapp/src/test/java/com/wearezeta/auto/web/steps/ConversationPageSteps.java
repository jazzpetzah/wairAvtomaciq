package com.wearezeta.auto.web.steps;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.ParticipantsPopupPage;
import com.wearezeta.auto.web.pages.UserProfilePopupPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

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
		PagesCollection.userProfilePopupPage = (UserProfilePopupPage) PagesCollection.conversationPage
				.clickShowUserProfileButton(false);
	}

	/**
	 * Click on the button from conversation that popups participant profile
	 * 
	 * @step. I click show participants profile button
	 * 
	 * @throws Exception
	 */
	@When("I click show participant profile button")
	public void WhenIClickShowParticipantsProfileButton() throws Exception {
		PagesCollection.participantsPopupPage = (ParticipantsPopupPage) PagesCollection.conversationPage
				.clickShowUserProfileButton(true);
	}

	/**
	 * Send a picture into current conversation
	 * 
	 * @step. ^I send picture (.*) to ([a-z]*) conversation$
	 * 
	 * @param pictureName
	 *            the name of a picture file. This file should already exist in
	 *            the ~/Documents folder
	 * @param chatType
	 *            specifies is it group chat or conversation with single user
	 * 
	 * @throws Exception
	 */
	@When("^I send picture (.*) to ([a-z]*) conversation$")
	public void WhenISendPicture(String pictureName, String chatType)
			throws Exception {
		boolean isGroup = (chatType.equals("group") ? true : false);
		PagesCollection.conversationPage.sendPicture(pictureName, isGroup);
	}

	/**
	 * Verifies whether previously sent picture exists in the conversation view
	 * 
	 * @step. ^I send picture (.*)
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

	/**
	 * Checks action message (e.g. you left, etc.) appear in conversation
	 * 
	 * @step. ^I see (.*) action for (.*) in conversation$
	 * 
	 * @throws AssertionError
	 *             if action message did not appear in conversation
	 * 
	 * @param message
	 * 
	 * @param contact
	 * 
	 */
	@Then("^I see (.*) action for (.*) in conversation$")
	public void ThenISeeActionForContactInConversation(String message,
			String contact) {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		Assert.assertTrue(PagesCollection.conversationPage
				.isActionMessageSent(message + " " + contact));
	}

	/**
	 * Add a user to group chat
	 * 
	 * @step. ^I add (.*) to group chat$
	 * 
	 * @param contact
	 * @throws Exception
	 */
	@When("^I add (.*) to group chat$")
	public void IAddContactToGroupChat(String contact) throws Exception {
		WhenIClickShowParticipantsProfileButton();
		ParticipantsProfilePopupSteps steps = new ParticipantsProfilePopupSteps();
		steps.IClickAddPeopleButtonOnUserProfilePopupPage();
		steps.IClickConfirmAddGroupChat();
		steps.ISearchForUser(contact);
		steps.ISelectUserFromPeoplePickerResults(contact);
		steps.IChooseToCreateConversationFromPopupPage();
	}
	
	/**
	 * Click ping button to send ping and hot ping
	 * @step. ^I click ping button$
	 * @throws Exception
	 */
	@When("^I click ping button$")
	public void IClickPingButton() throws Exception {
		PagesCollection.conversationPage.clickPingButton();
	}
	
	/**
	 * Verify ping (or ping again) message is visible in conversation
	 * @step. ^I see ping message (.*)$
	 * @param message
	 * 			pinged/pinged again
	 * @throws Exception
	 */
	@When("^I see ping message (.*)$")
	public void ISeePingMessage(String message) throws Exception {
		Assert.assertTrue(PagesCollection.conversationPage.isPingMessageVisible(message));
	}
	
	/**
	 * Verify that there is only one ping message visible in conversation
	 * @step. ^I see only one ping message$
	 * @throws Exception
	 */
	@When("^I see only one ping message$")
	public void ISeeOnlyOnePingMessage() throws Exception {
		Assert.assertEquals(PagesCollection.conversationPage.numberOfPingMessagesVisible(), 1);
	}
}
