package com.wearezeta.auto.osx.steps.webapp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wearezeta.auto.common.CommonSteps;

import static com.wearezeta.auto.common.CommonSteps.splitAliases;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.ConversationPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Keys;

public class ConversationPageSteps {

	private static final double MIN_ACCEPTABLE_IMAGE_SCORE = 0.75;

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	private static final String TOOLTIP_PING = "Ping";
	private static final String SHORTCUT_PING_WIN = "(Ctrl + Alt + K)";
	private static final String SHORTCUT_PING_MAC = "(⌘⌥K)";
	private static final String TOOLTIP_CALL = "Call";
	private static final String SHORTCUT_CALL_WIN = "(Ctrl + Alt + R)";
	private static final String SHORTCUT_CALL_MAC = "(⌘⌥R)";

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(ConversationPageSteps.class.getSimpleName());

	private String randomMessage;

	/**
	 * Sends random message (generated GUID) into opened conversation
	 *
	 * @step. ^I write random message$
	 * @throws Exception
	 */
	@When("^I write random message$")
	public void WhenIWriteRandomMessage() throws Exception {
		randomMessage = CommonUtils.generateGUID();
		IWriteMessage(randomMessage);
	}

	/**
	 * Verify that the input text field contains random message
	 *
	 */
	@Then("^I verify that random message was typed$")
	public void IVerifyThatRandomMessageWasTyped() throws Exception {
		assertThat("Random message in input field", webappPagesCollection
				.getPage(ConversationPage.class).getMessageFromInputField(),
				equalTo(randomMessage));
	}

	/**
	 * Verify that the input text field contains message X
	 *
	 * @param message
	 *            the message it should contain
	 */
	@Then("^I verify that message \"(.*)\" was typed$")
	public void IVerifyThatMessageWasTyped(String message) throws Exception {
		assertThat("Message in input field",
				webappPagesCollection.getPage(ConversationPage.class)
						.getMessageFromInputField(), equalTo(message));
	}

	/**
	 * Types text message to opened conversation, but does not send it
	 *
	 * @step. ^I write message (.*)$
	 *
	 * @param message
	 *            text message
	 * @throws Exception
	 */
	@When("^I write message (.*)$")
	public void IWriteMessage(String message) throws Exception {
		webappPagesCollection.getPage(ConversationPage.class).writeNewMessage(
				message);
	}

	/**
	 * Types x number of new lines to opened conversation, but does not send
	 * them
	 *
	 * @step. ^I write (.*) new lines$
	 *
	 * @param amount
	 *            number of lines to write
	 * @throws Exception
	 */
	@When("^I write (\\d+) new lines$")
	public void IWriteXNewLines(int amount) throws Exception {
		String message = "";
		for (int i = 0; i < amount; i++) {
			message = message + Keys.chord(Keys.SHIFT, Keys.ENTER);
		}
		webappPagesCollection.getPage(ConversationPage.class).writeNewMessage(
				message);
	}

	/**
	 * Submits entered message for sending
	 *
	 * @step. ^I send message$
	 */
	@When("^I send message$")
	public void WhenISendMessage() throws Exception {
		webappPagesCollection.getPage(ConversationPage.class).sendNewMessage();
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
		Assert.assertTrue(webappPagesCollection.getPage(ConversationPage.class)
				.isMessageSent(randomMessage));
	}

	/**
	 * Verifies whether YouTube video is visible
	 *
	 * @step. ^I see embedded youtube video of (.*)
	 *
	 * @throws Exception
	 *
	 */
	@Then("^I see embedded youtube video of (.*)")
	public void ThenISeeEmbeddedYoutubeVideoOf(String url) throws Exception {
		Assert.assertTrue(webappPagesCollection.getPage(ConversationPage.class)
				.isYoutubeVideoEmbedded(url));
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
		webappPagesCollection.getPage(ConversationPage.class)
				.clickPeopleButton();
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
		GroupPopoverContainer peoplePopoverPage = webappPagesCollection
				.getPage(GroupPopoverContainer.class);
		if (peoplePopoverPage.isVisible()) {

			peoplePopoverPage.waitUntilVisibleOrThrowException();
			webappPagesCollection.getPage(ConversationPage.class)
					.clickPeopleButton();
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
		SingleUserPopoverContainer peoplePopoverPage = webappPagesCollection
				.getPage(SingleUserPopoverContainer.class);
		if (peoplePopoverPage.isVisible()) {

			peoplePopoverPage.waitUntilVisibleOrThrowException();
			webappPagesCollection.getPage(ConversationPage.class)
					.clickPeopleButton();
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
		webappPagesCollection.getPage(ConversationPage.class)
				.clickPeopleButton();
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
		webappPagesCollection.getPage(ConversationPage.class).sendPicture(
				pictureName);
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
	public void ISeeSentPicture(String pictureName) throws Exception {
		assertThat("Message with image not found", webappPagesCollection
				.getPage(ConversationPage.class).isImageMessageFound());
		assertThat("Overlap score of image comparsion",
				webappPagesCollection.getPage(ConversationPage.class)
						.getOverlapScoreOfLastImage(pictureName),
				greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
	}

	/**
	 * Verifies that only x images are in the conversation. Helps with checking
	 * for doubles.
	 *
	 * @step. ^I see only (\\d+) picture[s]? in the conversation$
	 *
	 * @param x
	 *            the amount of images
	 */
	@Then("^I see only (\\d+) picture[s]? in the conversation$")
	public void ISeeOnlyXPicturesInConversation(int x) throws Exception {
		assertThat("Number of images in the conversation",
				webappPagesCollection.getPage(ConversationPage.class)
						.getNumberOfImagesInCurrentConversation(), equalTo(x));
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
	@Then("^I( do not)? see (.*) action in conversation$")
	public void ThenISeeActionInConversation(String doNot, String message)
			throws Exception {
		if (doNot == null) {
			webappPagesCollection.getPage(
					ConversationPage.class).waitForMessageHeaderContains(message);
		} else {
			Assert.assertTrue(webappPagesCollection.getPage(
					ConversationPage.class).isActionMessageNotSent(message));

		}
	}

	/**
	 * Verifies whether people button tool tip is correct or not.
	 *
	 * @step. ^I see correct people button tool tip$
	 *
	 */
	@Then("^I see correct people button tool tip$")
	public void ThenISeeCorrectPeopleButtonToolTip() throws Exception {
		Assert.assertTrue(webappPagesCollection.getPage(ConversationPage.class)
				.isPeopleButtonToolTipCorrect());
	}

	@Then("^I see connecting message for (.*) in conversation$")
	public void ISeeConnectingMessage(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		assertThat("User name",
				webappPagesCollection.getPage(ConversationPage.class)
						.getConnectedMessageUser(), equalTo(contact));
		assertThat("Label",
				webappPagesCollection.getPage(ConversationPage.class)
						.getConnectedMessageLabel(), equalTo("CONNECTING"));
	}

	@Then("^I see connected message for (.*) in conversation$")
	public void ISeeConnectedMessage(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		assertThat("User name",
				webappPagesCollection.getPage(ConversationPage.class)
						.getConnectedMessageUser(), equalTo(contact));
		assertThat("Label",
				webappPagesCollection.getPage(ConversationPage.class)
						.getConnectedMessageLabel(), equalTo("CONNECTED"));
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
	 *            constant part of the system message
	 * @param contacts
	 *            list of comma separated contact names/aliases
	 * @throws Exception
	 *
	 */
	@Then("^I( do not)? see (.*) action for (.*) in conversation$")
	public void ThenISeeActionForContactInConversation(String doNot,
			String message, String contacts) throws Exception {
		contacts = usrMgr.replaceAliasesOccurences(contacts, FindBy.NAME_ALIAS);
		Set<String> parts = new HashSet<String>();
		parts.add(message);
		parts.addAll(CommonSteps.splitAliases(contacts));
		if (doNot == null) {
			webappPagesCollection.getPage(ConversationPage.class)
					.waitForMessageHeaderContains(parts);
		} else {
			// TODO: replace this check with a nicer one
			assertThat("Check action",
					webappPagesCollection.getPage(ConversationPage.class)
							.getLastActionMessage(),
					not(containsString(message)));
		}
	}

	/**
	 * Click ping button to send ping and hot ping
	 *
	 * @step. ^I click ping button$
	 * @throws Exception
	 */
	@When("^I click ping button$")
	public void IClickPingButton() throws Exception {
		webappPagesCollection.getPage(ConversationPage.class).clickPingButton();
	}

	/**
	 * Verify a text message is visible in conversation.
	 *
	 * @step. ^I see text message (.*)
	 * @param message
	 * @throws Exception
	 */
	@Then("^I see text message (.*)")
	public void ISeeTextMessage(String message) throws Exception {
		Assert.assertTrue(webappPagesCollection.getPage(ConversationPage.class)
				.isTextMessageVisible(message));
	}

	private static String expandPattern(final String originalStr) {
		final String lineBreak = "LF";
		final Pattern p = Pattern
				.compile("\\(\\s*'(\\w+)'\\s*\\*\\s*([0-9]+)\\s*\\)");
		final Matcher m = p.matcher(originalStr);
		final StringBuilder result = new StringBuilder();
		int lastPosInOriginalString = 0;
		while (m.find()) {
			if (m.start() > lastPosInOriginalString) {
				result.append(originalStr.substring(lastPosInOriginalString,
						m.start()));
			}
			final String toAdd = m.group(1).replace(lineBreak, "\n");
			final int times = Integer.parseInt(m.group(2));
			for (int i = 0; i < times; i++) {
				result.append(toAdd);
			}
			lastPosInOriginalString = m.end();
		}
		if (lastPosInOriginalString < originalStr.length()) {
			result.append(originalStr.substring(lastPosInOriginalString,
					originalStr.length()));
		}
		return result.toString();
	}

	/**
	 * Verify the text of the last text message in conversation. Patterns are
	 * allowed, for example ('a' * 100) will print the a character 100 times.
	 * Line break is equal to LF char sequence.
	 *
	 * @step. ^I verify the last text message equals to (.*)
	 * @param expectedMessage
	 *            the expected message
	 * @throws Exception
	 */
	@Then("^I verify the last text message equals to (.*)")
	public void IVerifyLastTextMessage(String expectedMessage) throws Exception {
		Assert.assertEquals(expandPattern(expectedMessage),
				webappPagesCollection.getPage(ConversationPage.class)
						.getLastTextMessage());
	}

	/**
	 * Verify the text of the second last text message in conversation. This
	 * step should only be used after verifying the last message of the
	 * conversation, because otherwise you might run into a race condition.
	 *
	 * @step. ^I verify the second last text message equals to (.*)
	 * @param expectedMessage
	 *            the expected message
	 * @throws Exception
	 */
	@Then("^I verify the second last text message equals to (.*)")
	public void IVerifySecondLastTextMessage(String expectedMessage)
			throws Exception {
		assertThat(webappPagesCollection.getPage(ConversationPage.class)
				.getSecondLastTextMessage(), equalTo(expectedMessage));
	}

	/**
	 * Verify a text message is not visible in conversation
	 *
	 * @step. ^I do not see text message (.*)
	 * @param message
	 * @throws Exception
	 */
	@Then("^I do not see text message ?(.*)$")
	public void IDontSeeTextMessage(String message) throws Exception {
		Assert.assertFalse("Saw text message " + message,
				webappPagesCollection.getPage(ConversationPage.class)
						.isTextMessageVisible(message == null ? "" : message));
	}

	/**
	 * Verify that there is only one ping message visible in conversation
	 *
	 * @step. ^I see only one ping message$
	 * @throws Exception
	 */
	@When("^I see only one ping message$")
	public void ISeeOnlyOnePingMessage() throws Exception {
		assertThat("PING action", webappPagesCollection.getPage(ConversationPage.class)
				.waitForNumberOfMessageHeadersContain(Collections.singleton("PING")), equalTo(1));
	}

	/**
	 * Start call in opened conversation
	 *
	 * @step. ^I call$
	 */
	@When("^I call$")
	public void ICallUser() throws Throwable {
		webappPagesCollection.getPage(ConversationPage.class).clickCallButton();
	}

	/**
	 * Verifies whether calling button is visible or not.
	 *
	 * @param doNot
	 *            is set to null if "do not" part does not exist
	 *
	 * @step. ^I can see calling button$
	 * @throws java.lang.Exception
	 */
	@Then("^I( do not)? see calling button$")
	public void ISeeCallButton(String doNot) throws Exception {
		if (doNot == null) {
			Assert.assertTrue(webappPagesCollection.getPage(
					ConversationPage.class).isCallButtonVisible());
		} else {
			Assert.assertFalse(webappPagesCollection.getPage(
					ConversationPage.class).isCallButtonVisible());
		}
	}

	/**
	 * @step. ^I see the calling bar$
	 *
	 * @throws Exception
	 */
	@Then("^I see the calling bar$")
	public void IWaitForCallingBar() throws Exception {
		webappPagesCollection.getPage(ConversationPage.class)
				.waitForCallingBarToBeDisplayed();
	}

	/**
	 *
	 * @step. ^I see the calling bar from users? (.*)$
	 * @param aliases
	 *            comma separated list of usernames currently calling
	 * @throws Exception
	 */
	@Then("^I see the calling bar from users? (.*)$")
	public void IWaitForCallingBar(String aliases) throws Exception {
		final List<String> aliasList = splitAliases(aliases);
		ConversationPage conversationPage = webappPagesCollection
				.getPage(ConversationPage.class);
		for (String alias : aliasList) {
			final String participantName = usrMgr.findUserByNameOrNameAlias(
					alias).getName();
			conversationPage
					.waitForCallingBarToBeDisplayedWithName(participantName);
		}
	}

	/**
	 *
	 * @step. ^I see outgoing call for users? (.*)$
	 * @param aliases
	 *            comma separated list of usernames currently calling
	 * @throws Exception
	 */
	@Then("^I see outgoing call for users? (.*)$")
	public void ISeeOutgoingCallForUsers(String aliases) throws Exception {
		final List<String> participants = new ArrayList<String>();
		final List<String> aliasList = splitAliases(aliases);
		for (String alias : aliasList) {
			final String participantName = usrMgr.findUserByNameOrNameAlias(
					alias).getName();
			participants.add(participantName.toUpperCase());
		}
		assertThat(webappPagesCollection.getPage(ConversationPage.class)
				.getNamesFromOutgoingCallingBar(), is(participants));
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
		webappPagesCollection.getPage(ConversationPage.class)
				.verifyCallingBarIsNotVisible();
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
		webappPagesCollection.getPage(ConversationPage.class)
				.clickAcceptCallButton();
	}

	/**
	 * Joins ongoing call by clicking the join call bar
	 *
	 * @step. ^I join call$
	 *
	 * @throws Exception
	 */
	@When("^I join call$")
	public void IJoinCall() throws Exception {
		webappPagesCollection.getPage(ConversationPage.class)
				.clickJoinCallBar();
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
		webappPagesCollection.getPage(ConversationPage.class)
				.clickSilenceCallButton();
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
		webappPagesCollection.getPage(ConversationPage.class)
				.clickEndCallButton();
	}

	/**
	 * Verify that conversation contains my missed call
	 *
	 * @step. ^I see conversation with my missed call$
	 *
	 * @throws Exception
	 */
	@Then("^I see conversation with my missed call$")
	public void ThenISeeConversationWithMyMissedCall() throws Exception {
		Assert.assertEquals("YOU CALLED",
				webappPagesCollection.getPage(ConversationPage.class)
						.getMissedCallMessage());
	}

	/**
	 * Click on picture to open it in full screen mode
	 *
	 * @step. ^I click on picture$
	 *
	 * @throws Exception
	 */
	@When("^I click on picture$")
	public void WhenIClickOnPicture() throws Exception {
		webappPagesCollection.getPage(ConversationPage.class).clickOnPicture();
	}

	/**
	 * Verifies whether picture is in fullscreen or not.
	 *
	 * @param doNot
	 *            is set to null if "do not" part does not exist
	 *
	 * @step. ^I( do not)? see picture in fullscreen$
	 * @throws java.lang.Exception
	 */
	@Then("^I( do not)? see picture (.*) in fullscreen$")
	public void ISeePictureInFullscreen(String doNot, String pictureName)
			throws Exception {
		ConversationPage conversationPage = webappPagesCollection
				.getPage(ConversationPage.class);
		if (doNot == null) {
			Assert.assertTrue(conversationPage.isPictureInModalDialog());
			Assert.assertTrue(conversationPage.isPictureInFullscreen());
			assertThat("Overlap score of image comparsion",
					conversationPage
							.getOverlapScoreOfFullscreenImage(pictureName),
					greaterThan(MIN_ACCEPTABLE_IMAGE_SCORE));
		} else {
			Assert.assertTrue(conversationPage.isPictureNotInModalDialog());
		}
	}

	/**
	 * Click x button to close picture fullscreen mode
	 *
	 * @step. ^I click x button to close fullscreen mode$
	 * @throws Exception
	 */
	@When("^I click x button to close fullscreen mode$")
	public void IClickXButtonToCloseFullscreen() throws Exception {
		webappPagesCollection.getPage(ConversationPage.class).clickXButton();
	}

	/**
	 * I click on black border to close fullscreen mode
	 *
	 * @step. ^I click on black border to close fullscreen mode$
	 * @throws Exception
	 */
	@When("^I click on black border to close fullscreen mode$")
	public void IClickOnBlackBorderToCloseFullscreen() throws Exception {
		webappPagesCollection.getPage(ConversationPage.class)
				.clickOnBlackBorder();
	}

	@When("^I click GIF button$")
	public void IClickGIFButton() throws Throwable {
		webappPagesCollection.getPage(ConversationPage.class).clickGIFButton();
	}

	@Then("^I see sent gif in the conversation view$")
	public void ISeeSentGifInTheConversationView() throws Throwable {
		webappPagesCollection.getPage(ConversationPage.class).isGifVisible();
	}

	/**
	 * Verify that the input text field contains message X
	 *
	 * @param message
	 *            the message it should contain
	 */
	@Then("^I verify that message (.*) was cached$")
	public void IVerifyThatMessageWasCached(String message) throws Exception {
		assertThat("Cached message in input field", webappPagesCollection
				.getPage(ConversationPage.class).getMessageFromInputField(),
				equalTo(message));
	}

	/**
	 * Hovers ping button
	 *
	 * @step. ^I hover ping button$
	 * @throws Exception
	 */
	@Then("^I hover ping button$")
	public void IHoverPingButton() throws Exception {
		webappPagesCollection.getPage(ConversationPage.class).hoverPingButton();
	}

	/**
	 * Verifies whether ping button tool tip is correct or not.
	 *
	 * @step. ^I see correct ping button tool tip$
	 *
	 */
	@Then("^I see correct ping button tooltip$")
	public void ISeeCorrectPingButtonTooltip() throws Exception {

		String tooltip = TOOLTIP_PING + " ";
		if (WebAppExecutionContext.isCurrentPlatformWindows()) {
			tooltip = tooltip + SHORTCUT_PING_WIN;
		} else {
			tooltip = tooltip + SHORTCUT_PING_MAC;
		}
		assertThat("Ping button tooltip",
				webappPagesCollection.getPage(ConversationPage.class)
						.getPingButtonToolTip(), equalTo(tooltip));
	}

	/**
	 * Hovers call button
	 *
	 * @step. ^I hover call button$
	 */
	@When("^I hover call button$")
	public void IHoverCallButton() throws Throwable {
		webappPagesCollection.getPage(ConversationPage.class).hoverCallButton();
	}

	/**
	 * Verifies whether call button tool tip is correct or not.
	 *
	 * @step. ^I see correct call button tool tip$
	 *
	 */
	@Then("^I see correct call button tooltip$")
	public void ISeeCorrectCallButtonTooltip() throws Exception {

		String tooltip = TOOLTIP_CALL + " ";
		if (WebAppExecutionContext.isCurrentPlatformWindows()) {
			tooltip = tooltip + SHORTCUT_CALL_WIN;
		} else {
			tooltip = tooltip + SHORTCUT_CALL_MAC;
		}
		assertThat("Call button tooltip",
				webappPagesCollection.getPage(ConversationPage.class)
						.getCallButtonToolTip(), equalTo(tooltip));
	}

	@And("^I click on pending user avatar$")
	public void IClickOnPendingUserAvatar() throws Exception {
		webappPagesCollection.getPage(ConversationPage.class).clickUserAvatar();
	}

	/**
	 * Click on an avatar bubble inside the conversation view
	 * 
	 * @step. ^I click on avatar of user (.*) in conversation view$
	 * @param userAlias
	 *            name of the user
	 * @throws Exception
	 */
	@And("^I click on avatar of user (.*) in conversation view$")
	public void IClickOnUserAvatar(String userAlias) throws Exception {
		ClientUser user = usrMgr.findUserBy(userAlias, FindBy.NAME_ALIAS);
		webappPagesCollection.getPage(ConversationPage.class).clickUserAvatar(
				user.getId());
	}
}
