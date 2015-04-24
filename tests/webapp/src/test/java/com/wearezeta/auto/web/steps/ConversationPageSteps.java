package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.PagesCollection;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;

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
	 * @throws Exception
	 *
	 * @throws AssertionError
	 *             if message did not appear in conversation
	 */
	@Then("^I see random message in conversation$")
	public void ThenISeeRandomMessageInConversation() throws Exception {
		Assert.assertTrue(PagesCollection.conversationPage
				.isMessageSent(randomMessage));
	}

	/**
	 * Click People button in 1:1 conversation
	 *
	 * @step. I click People button in one to one conversation$
	 *
	 * @throws Exception
	 */
	@When("^I click People button in one to one conversation$")
	public void WhenIClickPeopleButtonIn1to1() throws Exception {
		PagesCollection.popoverPage = PagesCollection.conversationPage
				.clickPeopleButton(false);
	}

	/**
	 * Click People button in a group conversation to close People Popover
	 *
	 * @step. ^I close Group Participants popover$
	 *
	 * @throws Exception
	 *             if the popover is not visible
	 */
	@When("^I close Group Participants popover$")
	public void WhenICloseGroupParticipantsPopover() throws Exception {
		if (PagesCollection.popoverPage != null) {

			PagesCollection.popoverPage.waitUntilVisibleOrThrowException();
			PagesCollection.conversationPage.clickPeopleButton(true);
		}
	}

	/**
	 * Click People button in 1:1 conversation to close People Popover
	 *
	 * @step. ^I close Single User Profile popover$
	 *
	 * @throws Exception
	 *             if the popover is not visible
	 */
	@When("^I close Single User Profile popover$")
	public void WhenICloseSingleUserPopover() throws Exception {
		if (PagesCollection.popoverPage != null) {
			PagesCollection.popoverPage.waitUntilVisibleOrThrowException();
			PagesCollection.conversationPage.clickPeopleButton(false);
		}
	}

	/**
	 * Click People button in a group conversation
	 *
	 * @step. I click People button in group conversation$
	 *
	 * @throws Exception
	 */
	@When("^I click People button in group conversation$")
	public void WhenIClickPeopleButtonInGroup() throws Exception {
		PagesCollection.popoverPage = PagesCollection.conversationPage
				.clickPeopleButton(true);
	}

	/**
	 * Send a picture into current conversation
	 *
	 * @step. ^I send picture (.*) to the current conversation$
	 *
	 * @param pictureName
	 *            the name of a picture file. This file should already exist in
	 *            the ~/Documents folder
	 *
	 * @throws Exception
	 */
	@When("^I send picture (.*) to the current conversation$")
	public void WhenISendPicture(String pictureName) throws Exception {
		PagesCollection.conversationPage.sendPicture(pictureName);
	}

	/**
	 * Verifies whether previously sent picture exists in the conversation view
	 *
	 * @step. ^I see sent picture (.*) in the conversation view$
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
	 * @throws Exception
	 *
	 * @throws AssertionError
	 *             if action message did not appear in conversation
	 */
	@Then("^I see (.*) action in conversation$")
	public void ThenISeeActionInConversation(String message) throws Exception {
		Assert.assertTrue(PagesCollection.conversationPage
				.isActionMessageSent(message));
	}

	/**
	 * Verifies whether people button tool tip is correct or not.
	 *
	 * @step. ^I see correct people button tool tip$
	 *
	 */
	@Then("^I see correct people button tool tip$")
	public void ThenISeeCorrectPeopleButtonToolTip() {
		Assert.assertTrue(PagesCollection.conversationPage
				.isPeopleButtonToolTipCorrect());
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
	 * @throws Exception
	 *
	 */
	@Then("^I see (.*) action for (.*) in conversation$")
	public void ThenISeeActionForContactInConversation(String message,
			String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		if (contact.contains(",")) {
			contact = contact.replaceAll(",", ", ");
		}
		Assert.assertTrue(PagesCollection.conversationPage
				.isActionMessageSent(message + " " + contact));
	}

	/**
	 * Checks action message (e.g. you left, etc.) appear in conversation
	 *
	 * @step. ^I see (.*) user (.*) action for (.*) in conversation
	 *
	 * @throws AssertionError
	 *             if action message did not appear in conversation
	 *
	 * @param message
	 *            message string
	 *
	 * @param user1
	 *            user who did action string
	 *
	 * @param contacts
	 *            user(s) who was actioned string
	 *
	 * @throws Exception
	 *
	 */
	@Then("^I see user (.*) action (.*) for (.*) in conversation$")
	public void ThenISeeUserActionForContactInConversation(String user1,
			String message, String contacts) throws Exception {
		user1 = usrMgr.replaceAliasesOccurences(user1, FindBy.NAME_ALIAS);
		contacts = usrMgr.replaceAliasesOccurences(contacts, FindBy.NAME_ALIAS);
		if (contacts.contains(",")) {
			contacts = contacts.replaceAll(",", ", ");
		}
		if (contacts.contains(usrMgr.getSelfUser().getName())) {
			contacts = contacts.replace(usrMgr.getSelfUser().getName(), "you");
		}
		String actionMessage = user1 + " " + message + " " + contacts;
		Assert.assertTrue(PagesCollection.conversationPage
				.isActionMessageSent(actionMessage));
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
		WhenIClickPeopleButtonInGroup();
		GroupPopoverPageSteps cpSteps = new GroupPopoverPageSteps();
		cpSteps.IClickAddPeopleButton();
		cpSteps.IClickConfirmAddToChat();
		cpSteps.ISearchForUser(contact);
		cpSteps.ISelectUserFromSearchResults(contact);
		cpSteps.IChooseToCreateConversation();
	}

	/**
	 * Click ping button to send ping and hot ping
	 *
	 * @step. ^I click ping button$
	 * @throws Exception
	 */
	@When("^I click ping button$")
	public void IClickPingButton() throws Exception {
		PagesCollection.conversationPage.clickPingButton();
	}

	/**
	 * Verify ping (or ping again) message is visible in conversation
	 *
	 * @step. ^I see ping message (.*)$
	 * @param message
	 *            pinged/pinged again
	 * @throws Exception
	 */
	@When("^I see ping message (.*)$")
	public void ISeePingMessage(String message) throws Exception {
		Assert.assertTrue(PagesCollection.conversationPage
				.isPingMessageVisible(message));
	}

	/**
	 * Verify a text message is visible in conversation
	 *
	 * @step. ^I see text message (.*)
	 * @param message
	 * @throws Exception
	 */
	@Then("^I see text message (.*)")
	public void ISeeTextMessage(String message) throws Exception {
		Assert.assertTrue(PagesCollection.conversationPage
				.isTextMessageVisible(message));
	}

	/**
	 * Verify a text message is not visible in conversation
	 *
	 * @step. ^I do not see text message (.*)
	 * @param message
	 * @throws Exception
	 */
	@Then("^I do not see text message (.*)")
	public void IDontSeeTextMessage(String message) throws Exception {
		Assert.assertFalse(PagesCollection.conversationPage
				.isTextMessageVisible(message));
	}

	/**
	 * Verify that there is only one ping message visible in conversation
	 *
	 * @step. ^I see only one ping message$
	 * @throws Exception
	 */
	@When("^I see only one ping message$")
	public void ISeeOnlyOnePingMessage() throws Exception {
		Assert.assertEquals(
				PagesCollection.conversationPage.numberOfPingMessagesVisible(),
				1);
	}

	/**
	 * Start call in opened conversation
	 *
	 * @step. ^I call$
	 */
	@When("^I call$")
	public void ICallUser() throws Throwable {
		PagesCollection.conversationPage.clickCallButton();
	}

	/**
	 * @step. ^I see the calling bar$
	 *
	 * @throws Exception
	 */
	@Then("^I see the calling bar$")
	public void IWaitForCallingBar() throws Exception {
		if (PagesCollection.conversationPage == null) {
			PagesCollection.conversationPage = new ConversationPage(
					PagesCollection.loginPage.getDriver(),
					PagesCollection.loginPage.getWait());
		}
		PagesCollection.conversationPage.waitForCallingBarToBeDisplayed();
	}

	/**
	 * Verifies whether calling bar is not visible anymore
	 *
	 * @step. ^I do not see the calling bar$
	 *
	 * @throws Exception
	 */
	@Then("^I do not see the calling bar$")
	public void IDoNotCallingBar() throws Exception {
		if (PagesCollection.conversationPage == null) {
			PagesCollection.conversationPage = new ConversationPage(
					PagesCollection.loginPage.getDriver(),
					PagesCollection.loginPage.getWait());
		}
		PagesCollection.conversationPage.verifyCallingBarIsNotVisible();
	}

	/**
	 * Accepts incoming call by clicking the check button on the calling bar
	 *
	 * @step. ^I accept the incoming call$
	 *
	 * @throws Exception
	 */
	@When("^I accept the incoming call$")
	public void IAcceptIncomingCall() throws Exception {
		PagesCollection.conversationPage.clickAcceptCallButton();
	}

	/**
	 * Silences the incoming call by clicking the corresponding button on the
	 * calling bar
	 *
	 * @step. ^I silence the incoming call$
	 *
	 * @throws Exception
	 */
	@When("^I silence the incoming call$")
	public void ISilenceIncomingCall() throws Exception {
		PagesCollection.conversationPage.clickSilenceCallButton();
	}

	/**
	 * End the current call by clicking the X button on calling bar
	 *
	 * @step. ^I end the call$
	 *
	 * @throws Exception
	 */
	@When("^I end the call$")
	public void IEndTheCall() throws Exception {
		PagesCollection.conversationPage.clickEndCallButton();
	}

	/**
	 * Verify that conversation contains missed call from contact
	 *
	 * @step. ^I see conversation with missed call from (.*)$
	 *
	 * @param contact
	 *            contact name string
	 *
	 * @throws Exception
	 */
	@Then("^I see conversation with missed call from (.*)$")
	public void ThenISeeConversationWithMissedCallFrom(String contact)
			throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName()
				.toUpperCase();
		Assert.assertEquals(contact + " CALLED",
				PagesCollection.conversationPage.getMissedCallMessage());
	}
}
