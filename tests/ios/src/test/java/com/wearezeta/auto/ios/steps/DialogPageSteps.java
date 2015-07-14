package com.wearezeta.auto.ios.steps;

import java.awt.image.BufferedImage;
import java.text.Normalizer;
import java.text.Normalizer.Form;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.ios.IOSConstants;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.GroupChatPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.locators.IOSLocators;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DialogPageSteps {
	private static final Logger log = ZetaLogger.getLog(DialogPageSteps.class
			.getSimpleName());
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private DialogPage getDialogPage() throws Exception {
		return (DialogPage) pagesCollecton.getPage(DialogPage.class);
	}

	private GroupChatPage getGroupChatPage() throws Exception {
		return (GroupChatPage) pagesCollecton.getPage(GroupChatPage.class);
	}

	private ContactListPage getContactListPage() throws Exception {
		return (ContactListPage) pagesCollecton.getPage(ContactListPage.class);
	}

	public static String message;

	private String lastLine = "ea commodo consequat.";
	private String mediaState;
	public static long sendDate;
	private static final int SWIPE_DURATION = 1000;
	private static String onlySpacesMessage = "     ";
	public static long memTime;
	public String pingId;
	private int beforeNumberOfImages = 0;
	final String sendInviteMailContent = "I’m on Wire. Search for %s";
	final String automationMessage = "iPhone has stupid spell checker";

	@When("^I see dialog page$")
	public void WhenISeeDialogPage() throws Exception {
		Assert.assertTrue(getDialogPage().waitForCursorInputVisible());
	}

	@When("I tap on dialog page")
	public void ITapOnDialogPage() throws Exception {
		getDialogPage().tapDialogWindow();
	}

	@When("^I tap on text input$")
	public void WhenITapOnTextInput() throws Exception {
		for (int i = 0; i < 5; i++) {
			getDialogPage().tapOnCursorInput();
			if (getDialogPage().isKeyboardVisible()) {
				break;
			}
		}
	}

	/**
	 * Verify that text input is not allowed
	 * 
	 * @step. I see text input in dialog is not allowed
	 * 
	 * @throws Exception
	 */
	@When("I see text input in dialog is not allowed")
	public void ISeeTextInputIsNotAllowed() throws Exception {
		Assert.assertFalse("Text input is allowed", getDialogPage()
				.isCursorInputVisible());
	}

	@When("^I type the message$")
	public void WhenITypeTheMessage() throws Exception {
		// message = CommonUtils.generateGUID().replace('-', 'x');
		message = automationMessage;
		getDialogPage().sendStringToInput(message);
	}

	@When("I input message from keyboard (.*)")
	public void IInputMessageFromKeyboard(String message) throws Throwable {
		getDialogPage().inputStringFromKeyboard(message);
	}

	@When("I paste long text to input")
	public void IPasteLongTextToInput() throws Throwable {
		getDialogPage().pasteTextToInput(IOSConstants.LONG_MESSAGE);
	}

	@When("^I multi tap on text input$")
	public void WhenIMultiTapOnTextInput() throws Throwable {
		getDialogPage().multiTapOnCursorInput();
	}

	@Then("^I see You Pinged message in the dialog$")
	public void ISeeHelloMessageFromMeInTheDialog() throws Throwable {
		String pingmessage = IOSLocators.nameYouPingedMessage;
		Assert.assertTrue(getDialogPage().isMessageVisible(pingmessage));
	}

	@Then("^I see You Pinged Again message in the dialog$")
	public void ISeeHeyMessageFromMeInTheDialog() throws Throwable {
		String pingagainmessage = IOSLocators.nameYouPingedAgainMessage;
		Assert.assertTrue(getDialogPage().isMessageVisible(pingagainmessage));
	}

	@Then("^I see User (.*) Pinged message in the conversation$")
	public void ISeeUserPingedMessageTheDialog(String user) throws Throwable {
		String username = usrMgr.findUserByNameOrNameAlias(user).getName();
		String expectedPingMessage = username.toUpperCase() + " PINGED";
		Assert.assertTrue(getDialogPage().isMessageVisible(expectedPingMessage)
				|| getGroupChatPage().isMessageVisible(expectedPingMessage));
	}

	@Then("^I see User (.*) Pinged Again message in the conversation$")
	public void ISeeUserHotPingedMessageTheDialog(String user) throws Throwable {
		String username = usrMgr.findUserByNameOrNameAlias(user).getName();
		String expectedPingMessage = username.toUpperCase() + " PINGED AGAIN";
		Assert.assertTrue(getDialogPage().isMessageVisible(expectedPingMessage)
				|| getGroupChatPage().isMessageVisible(expectedPingMessage));
	}

	@When("^I type the message and send it$")
	public void ITypeTheMessageAndSendIt() throws Throwable {
		// message = CommonUtils.generateGUID().replace('-', 'x');
		message = automationMessage;
		getDialogPage().sendStringToInput(message + "\n");
	}

	/**
	 * Verifies that the title bar is present with a certain conversation name
	 * 
	 * @step. ^I see title bar in conversation name (.*)$
	 * 
	 * @param convName
	 *            name of the conversation
	 * @throws Exception
	 * 
	 */
	@Then("^I see title bar in conversation name (.*)$")
	public void ThenITitleBar(String convName) throws Exception {
		String chatName = usrMgr.findUserByNameOrNameAlias(convName).getName();
		Assert.assertTrue("Title bar is not on the page", getDialogPage()
				.isTitleBarDisplayed());
		Assert.assertTrue("Title bar has incorrect name", getDialogPage()
				.isTitleBarNamed(chatName));
	}

	/**
	 * Click open conversation details button in 1:1 dialog
	 * 
	 * @step. ^I open conversation details$
	 * 
	 * @throws Exception
	 *             if other user personal profile page was not created
	 */
	@When("^I open conversation details$")
	public void IOpenConversationDetails() throws Exception {
		getDialogPage().openConversationDetailsClick();
	}

	/**
	 * Click open conversation details button in 1:1 dialog with Pending user
	 * 
	 * @step. ^I open pending user conversation details$
	 * 
	 * @throws Exception
	 *             if other user personal profile page was not created
	 */
	@When("^I open pending user conversation details$")
	public void IOpenPendingConversationDetails() throws Exception {
		getDialogPage().clickConversationDeatailForPendingUser();
	}

	@When("^I send the message$")
	public void WhenISendTheMessage() throws Throwable {
		getDialogPage().inputStringFromKeyboard("\n");
	}

	@When("^I swipe up on dialog page to open other user personal page$")
	public void WhenISwipeUpOnDialogPage() throws Exception {
		getDialogPage().swipeUp(1000);
	}

	/**
	 * Swipes up on the pending dialog page in order to access the pending
	 * personal info page
	 * 
	 * @step. ^I swipe up on pending dialog page to open other user pending
	 *        personal page$
	 * 
	 * @throws Throwable
	 */

	@When("^I swipe up on pending dialog page to open other user pending personal page$")
	public void WhenISwipeUpOnPendingDialogPage() throws Throwable {
		getDialogPage().swipePendingDialogPageUp(500);
	}

	@Then("^I see message in the dialog$")
	public void ThenISeeMessageInTheDialog() throws Throwable {
		String dialogLastMessage = getDialogPage().getLastMessageFromDialog();
		Assert.assertTrue("Message is different, actual: " + dialogLastMessage
				+ " expected: " + message,
				dialogLastMessage.equals((message).trim()));
	}

	@Then("I see last message in dialog is expected message (.*)")
	public void ThenISeeLasMessageInTheDialogIsExpected(String msg)
			throws Throwable {
		String dialogLastMessage = getDialogPage().getLastMessageFromDialog();
		if (!Normalizer.isNormalized(dialogLastMessage, Form.NFC)) {
			dialogLastMessage = Normalizer.normalize(dialogLastMessage,
					Form.NFC);
		}

		if (!Normalizer.isNormalized(msg, Form.NFC)) {
			dialogLastMessage = Normalizer.normalize(msg, Form.NFC);
		}

		Assert.assertTrue("Message is different, actual: " + dialogLastMessage
				+ " expected: " + msg, dialogLastMessage.equals(msg));
	}

	@Then("^I see last message in the dialog$")
	public void ThenISeeLastMessageInTheDialog() throws Throwable {
		String dialogLastMessage = getDialogPage().getLastMessageFromDialog();
		Assert.assertTrue("Message is different, actual: " + dialogLastMessage
				+ " expected: " + lastLine,
				dialogLastMessage.equals((IOSConstants.LONG_MESSAGE).trim()));
	}

	@When("^I swipe the text input cursor$")
	public void ISwipeTheTextInputCursor() throws Throwable {
		for (int i = 0; i < 3; i++) {
			getDialogPage().swipeInputCursor();
			if (getDialogPage().isPingButtonVisible()) {
				break;
			}
		}
	}

	@When("^I press Add Picture button$")
	public void IPressAddPictureButton() throws Throwable {
		getDialogPage().pressAddPictureButton();
	}

	/**
	 * Click call button to start a call
	 * 
	 * @step. ^I press call button$
	 * @throws Throwable
	 */
	@When("^I press call button$")
	public void IPressCallButton() throws Throwable {
		getDialogPage().pressCallButton();
	}

	@When("^I click Ping button$")
	public void IPressPingButton() throws Throwable {
		getDialogPage().pressPingButton();
	}

	@Then("^I see Pending Connect to (.*) message on Dialog page from user (.*)$")
	public void ISeePendingConnectMessage(String contact, String user)
			throws Throwable {
		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		String expectedConnectingLabel = getDialogPage()
				.getExpectedConnectingLabel(contact);
		String actualConnectingLabel = getDialogPage().getConnectMessageLabel();

		Assert.assertTrue(actualConnectingLabel
				.contains(expectedConnectingLabel));
	}

	@Then("^I see new photo in the dialog$")
	public void ISeeNewPhotoInTheDialog() throws Throwable {
		int afterNumberOfImages = -1;

		boolean isNumberIncreased = false;
		for (int i = 0; i < 3; i++) {
			afterNumberOfImages = getDialogPage().getNumberOfImages();
			if (afterNumberOfImages == beforeNumberOfImages + 1) {
				isNumberIncreased = true;
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		Assert.assertTrue("Incorrect images count: before - "
				+ beforeNumberOfImages + ", after - " + afterNumberOfImages,
				isNumberIncreased);
	}

	@When("I type and send long message and media link (.*)")
	public void ITypeAndSendLongTextAndMediaLink(String link) throws Exception {
		getDialogPage().sendMessageUsingScript(IOSConstants.LONG_MESSAGE);
		getDialogPage().waitLoremIpsumText();
		getDialogPage().sendMessageUsingScript(link);
		getDialogPage().waitSoundCloudLoad();
	}

	@When("^I memorize message send time$")
	public void IMemorizeMessageSendTime() throws Exception {
		sendDate = getDialogPage().getSendTime();
	}

	@Then("I see media link (.*) and media in dialog")
	public void ISeeMediaLinkAndMediaInDialog(String link) throws Exception {
		Assert.assertTrue("Media is missing in dialog", getDialogPage()
				.isMediaContainerVisible());
		Assert.assertEquals(link.toLowerCase(), getDialogPage()
				.getLastMessageFromDialog().toLowerCase());
	}

	@When("I click video container for the first time")
	public void IPlayVideoFirstTime() throws Exception {
		getDialogPage().clickOnVideoContainerFirstTime();
	}

	@When("I tap on dialog window")
	public void ITapOnDialogWindow() throws Exception {
		getDialogPage().tapDialogWindow();
	}

	@When("I swipe right on Dialog page")
	public void ISwipeRightOnDialogPage() throws Exception {
		for (int i = 0; i < 3; i++) {
			getDialogPage().swipeRight(1000,
					DriverUtils.SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL, 28);
			if (getDialogPage().waitForCursorInputNotVisible()) {
				break;
			}
		}
	}

	@When("I send long message")
	public void ISendLongMessage() throws Exception {
		getDialogPage().sendMessageUsingScript(IOSConstants.LONG_MESSAGE);
	}

	@When("^I post media link (.*)$")
	public void IPostMediaLink(String link) throws Throwable {
		getDialogPage().sendMessageUsingScript(link);
	}

	@When("^I tap media link$")
	public void ITapMediaLink() throws Throwable {
		getDialogPage().startMediaContent();
		memTime = System.currentTimeMillis();
	}

	@When("^I scroll media out of sight until media bar appears$")
	public void IScrollMediaOutOfSightUntilMediaBarAppears() throws Exception {
		getDialogPage().scrollDownTilMediaBarAppears();
		Assert.assertTrue("Media bar is not displayed", getDialogPage()
				.isMediaBarDisplayed());
	}

	@When("^I pause playing the media in media bar$")
	public void IPausePlayingTheMediaInMediaBar() throws Exception {
		getDialogPage().pauseMediaContent();
	}

	@When("^I press play in media bar$")
	public void IPressPlayInMediaBar() throws Exception {
		getDialogPage().playMediaContent();
	}

	@When("^I stop media in media bar$")
	public void IStopMediaInMediaBar() throws Exception {
		getDialogPage().stopMediaContent();
	}

	@Then("I see playing media is paused")
	public void ThePlayingMediaIsPaused() throws Exception {
		String pausedState = IOSLocators.MEDIA_STATE_PAUSED;
		mediaState = getDialogPage().getMediaState();
		Assert.assertEquals(pausedState, mediaState);
	}

	@Then("I see media is playing")
	public void TheMediaIsPlaying() throws Exception {
		String playingState = IOSLocators.MEDIA_STATE_PLAYING;
		mediaState = getDialogPage().getMediaState();
		Assert.assertEquals(playingState, mediaState);
	}

	@Then("The media stops playing")
	public void TheMediaStoppsPlaying() throws Exception {
		String endedState = IOSLocators.MEDIA_STATE_STOPPED;
		mediaState = getDialogPage().getMediaState();
		Assert.assertEquals(endedState, mediaState);
	}

	@When("I wait (.*) seconds for media to stop playing")
	public void IWaitForMediaStopPlaying(int time) throws Throwable {
		long deltaTime = 0;
		long currentTime = System.currentTimeMillis();
		if ((memTime + time * 1000) > currentTime) {
			deltaTime = time * 1000 - (currentTime - memTime);
			log.debug("Waiting " + deltaTime + " ms playback to finish");
			Thread.sleep(deltaTime + 5000);
			log.debug("Playback finished");
		} else {
			log.debug("Playback finished");
		}

	}

	@Then("I see media bar on dialog page")
	public void ISeeMediaBarInDialog() throws Exception {
		Assert.assertTrue(getDialogPage().isMediaBarDisplayed());
	}

	@Then("I dont see media bar on dialog page")
	public void ISeeMediaBarDisappear() throws Exception {
		Assert.assertFalse(getDialogPage().isMediaBarDisplayed());
	}

	@When("^I tap on the media bar$")
	public void ITapOnMediaBar() throws Exception {
		getDialogPage().tapOnMediaBar();
	}

	@When("I scroll back to media container")
	public void IScrollUpOnDialogPage() throws Throwable {
		getDialogPage().scrollUpToMediaContainer();
	}

	@Then("I see conversation view is scrolled back to the playing media link (.*)")
	public void ISeeConversationViewIsScrolledBackToThePlayingMedia(String link)
			throws Exception {
		Assert.assertEquals(link.toLowerCase(), getDialogPage()
				.getLastMessageFromDialog().toLowerCase());
		Assert.assertTrue("View did not scroll back", getDialogPage()
				.isMediaContainerVisible());
		String playingState = IOSLocators.MEDIA_STATE_PLAYING;
		mediaState = getDialogPage().getMediaState();
		Assert.assertEquals(playingState, mediaState);
	}

	@When("I tap and hold image to open full screen")
	public void ITapImageToOpenFullScreen() throws Throwable {
		getDialogPage().tapImageToOpen();
	}

	@Then("^I scroll away the keyboard$")
	public void IScrollKeyboardAway() throws Throwable {
		getDialogPage().swipeDialogPageDown(500);
		Thread.sleep(2000);
	}

	@Then("^I navigate back to conversations view$")
	public void INavigateToConversationsView() throws Exception {
		for (int i = 0; i < 3; i++) {
			getDialogPage().swipeRight(SWIPE_DURATION);
			if (getContactListPage().isMyUserNameDisplayedFirstInContactList(
					usrMgr.getSelfUser().getName())) {
				break;
			}
		}
	}

	@When("I try to send message with only spaces")
	public void ISendMessageWithOnlySpaces() throws Throwable {
		getDialogPage().sendStringToInput(onlySpacesMessage + "\n");
	}

	@Then("I see message with only spaces is not send")
	public void ISeeMessageWithOnlySpacesIsNotSend() throws Exception {
		Assert.assertFalse(onlySpacesMessage.equals(getDialogPage()
				.getLastMessageFromDialog()));
	}

	@When("I input message with leading empty spaces")
	public void IInpuMessageWithLeadingEmptySpace() throws Throwable {
		message = onlySpacesMessage + automationMessage;
		getDialogPage().sendStringToInput(message);
		message = automationMessage;
	}

	@When("I input message with trailing emtpy spaces")
	public void IInputMessageWithTrailingEmptySpace() throws Throwable {
		message = automationMessage + "." + onlySpacesMessage;
		getDialogPage().sendStringToInput(message);
	}

	@When("I input message with lower case and upper case")
	public void IInputMessageWithLowerAndUpperCase() throws Throwable {
		message = CommonUtils.generateRandomString(7).toLowerCase()
				+ CommonUtils.generateRandomString(7).toUpperCase();
		getDialogPage().sendStringToInput(message);
	}

	@When("I input more than 200 chars message and send it")
	public void ISend200CharsMessage() throws Exception {
		message = CommonUtils.generateRandomString(210).toLowerCase()
				.replace("x", " ");
		getDialogPage().sendMessageUsingScript(message);
	}

	@When("I tap and hold on message input")
	public void ITapHoldTextInput() throws Exception {
		getDialogPage().tapHoldTextInput();
	}

	@When("^I scroll to the beginning of the conversation$")
	public void IScrollToTheBeginningOfTheConversation() throws Throwable {
		getDialogPage().scrollToBeginningOfConversation();
	}

	@When("^I send predefined message (.*)$")
	public void ISendPredefinedMessage(String message) throws Throwable {
		getDialogPage().sendStringToInput(message + "\n");
	}

	@When("^I send using script predefined message (.*)$")
	public void ISendUsingScriptPredefinedMessage(String message)
			throws Exception {
		getDialogPage().sendMessageUsingScript(message);
	}

	@When("I verify image in dialog is same as template (.*)")
	public void IVerifyImageInDialogSameAsTemplate(String filename)
			throws Throwable {
		BufferedImage templateImage = getDialogPage().takeImageScreenshot();
		BufferedImage referenceImage = ImageUtil.readImageFromFile(IOSPage
				.getImagesPath() + filename);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage,
				ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
		System.out.println("SCORE: " + score);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= "
						+ IOSConstants.MIN_IMG_SCORE + ", current = " + score,
				score >= IOSConstants.MIN_IMG_SCORE);
	}

	@When("I scroll to image in dialog")
	public void IScrollToIMageInDIalog() throws Throwable {
		getDialogPage().scrollToImage();
	}

	@When("^User (.*) Ping in chat (.*) by BackEnd$")
	public void UserPingInChatByBE(String contact, String conversationName)
			throws Exception {
		ClientUser yourСontact = usrMgr.findUserByNameOrNameAlias(contact);
		conversationName = usrMgr.replaceAliasesOccurences(conversationName,
				FindBy.NAME_ALIAS);
		pingId = BackendAPIWrappers.sendPingToConversation(yourСontact,
				conversationName);
		Thread.sleep(1000);
	}

	@When("^User (.*) HotPing in chat (.*) by BackEnd$")
	public void UserHotPingInChatByBE(String contact, String conversationName)
			throws Exception {
		ClientUser yourСontact = usrMgr.findUserByNameOrNameAlias(contact);
		conversationName = usrMgr.replaceAliasesOccurences(conversationName,
				FindBy.NAME_ALIAS);
		BackendAPIWrappers.sendHotPingToConversation(yourСontact,
				conversationName, pingId);
		Thread.sleep(1000);
	}

	@Then("^I see (.*) icon in conversation$")
	public void ThenIseeIcon(String iconLabel) throws Exception,
			InterruptedException {
		getDialogPage().waitPingAnimation();
		// TODO: detect current page
		double score1 = getDialogPage().checkPingIcon(iconLabel);
		double score2 = getGroupChatPage().checkPingIcon(iconLabel);
		Assert.assertTrue(
				"Overlap between two images has not enough score. Expected >= 0.75",
				score1 >= 0.75d || score2 >= 0.75d);
	}

	@When("^Contact (.*) sends random message to user (.*)$")
	public void UserSendsRandomMessageToConversation(
			String msgFromUserNameAlias, String dstUserNameAlias)
			throws Exception {
		message = CommonUtils.generateRandomString(10);
		CommonSteps.getInstance().UserSentMessageToUser(msgFromUserNameAlias,
				dstUserNameAlias, message);
	}

	/**
	 * Scrolls to the end of the conversation
	 * 
	 * @step. ^I scroll to the end of the conversation$
	 * @throws Exception
	 * 
	 */
	@When("^I scroll to the end of the conversation$")
	public void IScrollToTheEndOfTheConversation() throws Exception {
		getDialogPage().scrollToEndOfConversation();
	}

	/**
	 * Checks if the copied content from send an invite via mail is correct
	 * 
	 * @step. ^I check copied content from (.*)$
	 * @param mail
	 *            Email thats the invite sent from
	 * @throws Exception
	 * 
	 */
	@Then("^I check copied content from (.*)$")
	public void ICheckCopiedContentFrom(String mail) throws Exception {
		mail = usrMgr.findUserByNameOrNameAlias(mail).getEmail();
		final String finalString = String.format(sendInviteMailContent, mail);
		String lastMessage = getDialogPage().getLastMessageFromDialog();
		boolean messageContainsContent = lastMessage.contains(finalString);
		Assert.assertTrue("Mail Invite content is not shown in lastMessage",
				messageContainsContent);
	}

	/**
	 * Check that missed call UI is visible in dialog
	 * 
	 * @step. ^I see missed call from contact (.*)$
	 * @param contact
	 *            User name who called
	 * @throws Exception
	 */
	@When("^I see missed call from contact (.*)$")
	public void ISeeMissedCall(String contact) throws Exception {
		String username = usrMgr.findUserByNameOrNameAlias(contact).getName();
		String expectedCallMessage = username.toUpperCase() + " CALLED";
		Assert.assertTrue(username + " called message is missing in dialog",
				getDialogPage().isMessageVisible(expectedCallMessage));
	}

	/**
	 * Start a call by clicking missed call button in dialog
	 * 
	 * @step. ^I click missed call button to call contact (.*)$
	 * @param contact
	 *            User name who called
	 * @throws Exception
	 */
	@When("^I click missed call button to call contact (.*)$")
	public void IClickMissedCallButton(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		getDialogPage().clickOnCallButtonForContact(contact.toUpperCase());
	}

	public static final String TAP_OR_SLIDE = "TAP OR SLIDE";

	/**
	 * Observing tutorial "swipe right" aka "tap or slide"
	 * 
	 * @step. ^I see TAPORSLIDE text$
	 * @throws Exception
	 */
	@Then("^I see TAPORSLIDE text$")
	public void ISeeTapOrSlideText() throws Exception {
		boolean result = getDialogPage().isTypeOrSlideExists(TAP_OR_SLIDE);
		Assert.assertTrue(result);
	}

	/**
	 * Checks if a chathaed is visible with message and avatar for 5sec
	 * 
	 * @step. ^I see chathead of contact (.*)
	 * @param contact
	 *            you see the chathead of
	 * @throws Exception
	 */
	@Then("^I see chathead of contact (.*)")
	public void ISeeChatheadOfContact(String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		boolean chatheadIsVisible = getDialogPage().chatheadIsVisible(contact);
		Assert.assertTrue("No Chathead visible.", chatheadIsVisible);
		boolean chMessageIsVisible = getDialogPage().chatheadMessageIsVisible(
				message);
		Assert.assertTrue("No Chathead message visible.", chMessageIsVisible);
		boolean chAvatarImageIsVisible = getDialogPage()
				.chatheadAvatarImageIsVisible();
		Assert.assertTrue("No Chathead avatar visible.", chAvatarImageIsVisible);
	}

	/**
	 * Verify that the chathaed is not seen after 5 seconds
	 * 
	 * @step. I do not see chathead of contact (.*)
	 * @param contact
	 *            you not see the chathead of
	 * @throws Exception
	 */
	@Then("^I do not see chathead of contact (.*)")
	public void IDoNotSeeChatheadOfContactForSecondsWithAvatarAndMessage(
			String contact) throws Exception {
		contact = usrMgr.replaceAliasesOccurences(contact, FindBy.NAME_ALIAS);
		boolean chatheadIsVisible = getDialogPage().chatheadIsVisible(contact);
		Assert.assertFalse("Chathead visible.", chatheadIsVisible);
	}

	/**
	 * Clicking on video play button in youtube player
	 * 
	 * @step. ^I click play video button$
	 * @throws Exception
	 */
	@When("I click play video button")
	public void IClickPlayButton() throws Exception {
		getDialogPage().clickOnPlayVideoButton();
	}

	/**
	 * Types in the tag for giphy and opens preview page
	 * 
	 * @step. ^I type tag for giphy preview (.*) and open preview overlay$
	 * 
	 * @param message
	 *            Tag to be fetched from giphy
	 * @throws Exception
	 */
	@When("^I type tag for giphy preview (.*) and open preview overlay$")
	public void ITypeGiphyTagAndOpenPreview(String message) throws Exception {
		getDialogPage().sendStringToInput(message);
		getDialogPage().openGifPreviewPage();
	}
}
