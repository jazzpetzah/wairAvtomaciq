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
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.ios.IOSConstants;
import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.IOSPage;
import com.wearezeta.auto.ios.pages.ImageFullScreenPage;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.CameraRollPage;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.VideoPlayerPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DialogPageSteps {
	private static final Logger log = ZetaLogger.getLog(DialogPageSteps.class
			.getSimpleName());
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	public static String message;
	private String longMessage = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.";

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
		PagesCollection.dialogPage = (DialogPage) PagesCollection.iOSPage;
		Assert.assertTrue(PagesCollection.dialogPage
				.waitForCursorInputVisible());
	}

	@When("I tap on dialog page")
	public void ITapOnDialogPage() throws Exception {
		PagesCollection.dialogPage.tapDialogWindow();
	}

	@When("^I tap on text input$")
	public void WhenITapOnTextInput() throws Exception {
		for (int i = 0; i < 5; i++) {
			PagesCollection.dialogPage.tapOnCursorInput();
			if (PagesCollection.dialogPage.isKeyboardVisible()) {
				break;
			}
		}
	}

	@When("^I type the message$")
	public void WhenITypeTheMessage() throws Throwable {
		// message = CommonUtils.generateGUID().replace('-', 'x');
		message = automationMessage;
		PagesCollection.dialogPage.sendStringToInput(message);
	}

	@When("I input message from keyboard (.*)")
	public void IInputMessageFromKeyboard(String message) throws Throwable {
		PagesCollection.dialogPage.inputStringFromKeyboard(message);
	}

	@When("I paste long text to input")
	public void IPasteLongTextToInput() throws Throwable {
		PagesCollection.dialogPage.pasteTextToInput(longMessage);
	}

	@When("^I multi tap on text input$")
	public void WhenIMultiTapOnTextInput() throws Throwable {
		PagesCollection.dialogPage.multiTapOnCursorInput();
	}

	@Then("^I see You Pinged message in the dialog$")
	public void ISeeHelloMessageFromMeInTheDialog() throws Throwable {
		String pingmessage = IOSLocators.nameYouPingedMessage;

		Assert.assertTrue(PagesCollection.dialogPage
				.isMessageVisible(pingmessage));
	}

	@Then("^I see You Pinged Again message in the dialog$")
	public void ISeeHeyMessageFromMeInTheDialog() throws Throwable {
		String pingagainmessage = IOSLocators.nameYouPingedAgainMessage;

		Assert.assertTrue(PagesCollection.dialogPage
				.isMessageVisible(pingagainmessage));
	}

	@Then("^I see User (.*) Pinged message in the conversation$")
	public void ISeeUserPingedMessageTheDialog(String user) throws Throwable {
		String username = usrMgr.findUserByNameOrNameAlias(user).getName();
		String expectedPingMessage = username.toUpperCase() + " PINGED";
		if (PagesCollection.dialogPage != null) {
			Assert.assertTrue(PagesCollection.dialogPage
					.isMessageVisible(expectedPingMessage));
		} else {
			Assert.assertTrue(PagesCollection.groupChatPage
					.isMessageVisible(expectedPingMessage));
		}
	}

	@Then("^I see User (.*) Pinged Again message in the conversation$")
	public void ISeeUserHotPingedMessageTheDialog(String user) throws Throwable {
		String username = usrMgr.findUserByNameOrNameAlias(user).getName();
		String expectedPingMessage = username.toUpperCase() + " PINGED AGAIN";
		if (PagesCollection.dialogPage != null) {
			Assert.assertTrue(PagesCollection.dialogPage
					.isMessageVisible(expectedPingMessage));
		} else {
			Assert.assertTrue(PagesCollection.groupChatPage
					.isMessageVisible(expectedPingMessage));
		}
	}

	@When("^I type the message and send it$")
	public void ITypeTheMessageAndSendIt() throws Throwable {
		// message = CommonUtils.generateGUID().replace('-', 'x');
		message = automationMessage;
		PagesCollection.dialogPage.sendStringToInput(message + "\n");
	}

	/**
	 * Verifies that the title bar is present with a certain conversation name
	 * 
	 * @step. ^I see title bar in conversation name (.*)$
	 * 
	 * @param convName
	 *            name of the conversation
	 * 
	 * @throws InterruptedException
	 * 
	 * @throws AssertionError
	 *             if title bar is not visible
	 * 
	 * @throws AssertionError
	 *             if title bar has incorrect name
	 */
	@Then("^I see title bar in conversation name (.*)$")
	public void ThenITitleBar(String convName) throws NoSuchUserException,
			InterruptedException {
		String chatName = usrMgr.findUserByNameOrNameAlias(convName).getName();
		Assert.assertTrue("Title bar is not on the page",
				PagesCollection.dialogPage.isTitleBarDisplayed());
		Assert.assertTrue("Title bar has incorrect name",
				PagesCollection.dialogPage.isTitleBarNamed(chatName));
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
		PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.dialogPage
				.openConversationDetailsClick();
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
		PagesCollection.otherUserOnPendingProfilePage = PagesCollection.dialogPage
				.clickConversationDeatailForPendingUser();
	}

	@When("^I send the message$")
	public void WhenISendTheMessage() throws Throwable {
		PagesCollection.dialogPage.inputStringFromKeyboard("\n");
	}

	@When("^I swipe up on dialog page to open other user personal page$")
	public void WhenISwipeUpOnDialogPage() throws Exception {
		PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.dialogPage
				.swipeUp(1000);
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
		PagesCollection.otherUserOnPendingProfilePage = PagesCollection.dialogPage
				.swipePendingDialogPageUp(500);
	}

	@Then("^I see message in the dialog$")
	public void ThenISeeMessageInTheDialog() throws Throwable {
		String dialogLastMessage = PagesCollection.dialogPage
				.getLastMessageFromDialog();
		Assert.assertTrue("Message is different, actual: " + dialogLastMessage
				+ " expected: " + message,
				dialogLastMessage.equals((message).trim()));
	}

	@Then("I see last message in dialog is expected message (.*)")
	public void ThenISeeLasMessageInTheDialogIsExpected(String msg)
			throws Throwable {
		String dialogLastMessage = PagesCollection.dialogPage
				.getLastMessageFromDialog();

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
		String dialogLastMessage = PagesCollection.dialogPage
				.getLastMessageFromDialog();
		Assert.assertTrue("Message is different, actual: " + dialogLastMessage
				+ " expected: " + lastLine,
				dialogLastMessage.equals((longMessage).trim()));
	}

	@When("^I swipe the text input cursor$")
	public void ISwipeTheTextInputCursor() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.iOSPage;
		for (int i = 0; i < 3; i++) {
			PagesCollection.dialogPage.swipeInputCursor();
			if (PagesCollection.dialogPage.isPingButtonVisible()) {
				break;
			}
		}
	}

	@When("^I press Add Picture button$")
	public void IPressAddPictureButton() throws Throwable {
		CameraRollPage page = PagesCollection.dialogPage
				.pressAddPictureButton();
		PagesCollection.cameraRollPage = (CameraRollPage) page;
	}

	/**
	 * Click call button to start a call
	 * 
	 * @step. ^I press call button$
	 * @throws Throwable
	 */
	@When("^I press call button$")
	public void IPressCallButton() throws Throwable {

		PagesCollection.callPage = PagesCollection.dialogPage.pressCallButton();
	}

	@When("^I click Ping button$")
	public void IPressPingButton() throws Throwable {
		PagesCollection.dialogPage.pressPingButton();
	}

	@Then("^I see Pending Connect to (.*) message on Dialog page from user (.*)$")
	public void ISeePendingConnectMessage(String contact, String user)
			throws Throwable {
		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		String expectedConnectingLabel = PagesCollection.dialogPage
				.getExpectedConnectingLabel(contact);
		String actualConnectingLabel = PagesCollection.dialogPage
				.getConnectMessageLabel();
		String lastMessage = PagesCollection.dialogPage.getConnectionMessage();
		String expectedConnectMessage = PagesCollection.dialogPage
				.getExpectedConnectMessage(contact, user);
		Assert.assertEquals("Expected: " + expectedConnectingLabel
				+ " | Actual: " + actualConnectingLabel,
				expectedConnectingLabel, actualConnectingLabel);
		Assert.assertEquals("Expected: " + expectedConnectMessage
				+ " | Actual: " + lastMessage,
				expectedConnectMessage.toLowerCase(), lastMessage.toLowerCase());
	}

	@Then("^I see new photo in the dialog$")
	public void ISeeNewPhotoInTheDialog() throws Throwable {
		int afterNumberOfImages = -1;

		boolean isNumberIncreased = false;
		for (int i = 0; i < 3; i++) {
			afterNumberOfImages = PagesCollection.dialogPage
					.getNumberOfImages();
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
		PagesCollection.dialogPage.sendMessageUsingScript(longMessage);
		Thread.sleep(1000);
		PagesCollection.dialogPage.sendMessageUsingScript(link);
	}

	@When("^I memorize message send time$")
	public void IMemorizeMessageSendTime() {
		sendDate = PagesCollection.dialogPage.getSendTime();
	}

	@Then("I see media link (.*) and media in dialog")
	public void ISeeMediaLinkAndMediaInDialog(String link) throws Exception {
		Assert.assertTrue("Media is missing in dialog",
				PagesCollection.dialogPage.isMediaContainerVisible());

		for (int i = 0; i < 10; i++) {
			if (!link.equalsIgnoreCase(PagesCollection.dialogPage
					.getLastMessageFromDialog())) {
				Thread.sleep(1000);
			}
		}
		Assert.assertEquals(link.toLowerCase(), PagesCollection.dialogPage
				.getLastMessageFromDialog().toLowerCase());
	}

	@When("I click video container for the first time")
	public void IPlayVideoFirstTime() throws Exception {
		PagesCollection.videoPlayerPage = (VideoPlayerPage) PagesCollection.dialogPage
				.clickOnVideoContainerFirstTime();
	}

	@When("I tap on dialog window")
	public void ITapOnDialogWindow() throws Exception {
		PagesCollection.dialogPage.tapDialogWindow();
	}

	@When("I swipe right on Dialog page")
	public void ISwipeRightOnDialogPage() throws Exception {
		
		for (int i = 0; i < 3; i++) {
			PagesCollection.contactListPage = (ContactListPage) PagesCollection.dialogPage
					.swipeRight(1000,
							DriverUtils.SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL, 28);
			if (PagesCollection.dialogPage.waitForCursorInputNotVisible()) {
				break;
			}
		}
	}

	@When("I send long message")
	public void ISendLongMessage() throws Exception {
		PagesCollection.dialogPage.sendMessageUsingScript(longMessage);
	}

	@When("^I post media link (.*)$")
	public void IPostMediaLink(String link) throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.iOSPage;
		PagesCollection.dialogPage.sendMessageUsingScript(link);
	}

	@When("^I tap media link$")
	public void ITapMediaLink() throws Throwable {
		PagesCollection.dialogPage.startMediaContent();
		memTime = System.currentTimeMillis();
	}

	@When("^I scroll media out of sight until media bar appears$")
	public void IScrollMediaOutOfSightUntilMediaBarAppears() throws Exception {
		PagesCollection.dialogPage = PagesCollection.dialogPage
				.scrollDownTilMediaBarAppears();
		Assert.assertTrue("Media bar is not displayed",
				PagesCollection.dialogPage.isMediaBarDisplayed());
	}

	@When("^I pause playing the media in media bar$")
	public void IPausePlayingTheMediaInMediaBar() throws Exception {
		PagesCollection.dialogPage.pauseMediaContent();
	}

	@When("^I press play in media bar$")
	public void IPressPlayInMediaBar() throws Exception {
		PagesCollection.dialogPage.playMediaContent();
	}

	@When("^I stop media in media bar$")
	public void IStopMediaInMediaBar() throws Exception {
		PagesCollection.dialogPage.stopMediaContent();
	}

	@Then("I see playing media is paused")
	public void ThePlayingMediaIsPaused() {
		String pausedState = IOSLocators.MEDIA_STATE_PAUSED;
		mediaState = PagesCollection.dialogPage.getMediaState();
		Assert.assertEquals(pausedState, mediaState);
	}

	@Then("I see media is playing")
	public void TheMediaIsPlaying() {
		String playingState = IOSLocators.MEDIA_STATE_PLAYING;
		mediaState = PagesCollection.dialogPage.getMediaState();
		Assert.assertEquals(playingState, mediaState);
	}

	@Then("The media stopps playing")
	public void TheMediaStoppsPlaying() {
		String endedState = IOSLocators.MEDIA_STATE_STOPPED;
		mediaState = PagesCollection.dialogPage.getMediaState();
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
	public void ISeeMediaBarInDialog() {
		Assert.assertTrue(PagesCollection.dialogPage.isMediaBarDisplayed());
	}

	@Then("I dont see media bar on dialog page")
	public void ISeeMediaBarDisappear() {
		Assert.assertFalse(PagesCollection.dialogPage.isMediaBarDisplayed());
	}

	@When("^I tap on the media bar$")
	public void ITapOnMediaBar() throws Exception {
		PagesCollection.dialogPage.tapOnMediaBar();
	}

	@When("I scroll back to media container")
	public void IScrollUpOnDialogPage() throws Throwable {
		PagesCollection.dialogPage.scrollUpToMediaContainer();
	}

	@Then("I see conversation view is scrolled back to the playing media link (.*)")
	public void ISeeConversationViewIsScrolledBackToThePlayingMedia(String link)
			throws Exception {
		Assert.assertEquals(link.toLowerCase(), PagesCollection.dialogPage
				.getLastMessageFromDialog().toLowerCase());
		Assert.assertTrue("View did not scroll back",
				PagesCollection.dialogPage.isMediaContainerVisible());
		String playingState = IOSLocators.MEDIA_STATE_PLAYING;
		mediaState = PagesCollection.dialogPage.getMediaState();
		Assert.assertEquals(playingState, mediaState);
	}

	@When("I tap and hold image to open full screen")
	public void ITapImageToOpenFullScreen() throws Throwable {
		PagesCollection.imageFullScreenPage = (ImageFullScreenPage) PagesCollection.dialogPage
				.tapImageToOpen();
	}

	@Then("^I scroll away the keyboard$")
	public void IScrollKeyboardAway() throws Throwable {
		PagesCollection.dialogPage.swipeDialogPageDown(500);
		Thread.sleep(2000);
	}

	@Then("^I navigate back to conversations view$")
	public void INavigateToConversationsView() throws Exception {
		for (int i = 0; i < 3; i++) {
			PagesCollection.dialogPage.swipeRight(SWIPE_DURATION);
			if (PagesCollection.contactListPage
					.isMyUserNameDisplayedFirstInContactList(usrMgr
							.getSelfUser().getName())) {
				break;
			}
		}
	}

	@When("I try to send message with only spaces")
	public void ISendMessageWithOnlySpaces() throws Throwable {
		PagesCollection.dialogPage.sendStringToInput(onlySpacesMessage + "\n");
	}

	@Then("I see message with only spaces is not send")
	public void ISeeMessageWithOnlySpacesIsNotSend() {
		Assert.assertFalse(onlySpacesMessage.equals(PagesCollection.dialogPage
				.getLastMessageFromDialog()));
	}

	@When("I input message with leading empty spaces")
	public void IInpuMessageWithLeadingEmptySpace() throws Throwable {
		message = onlySpacesMessage + automationMessage;
		PagesCollection.dialogPage.sendStringToInput(message);
		message = automationMessage;
	}

	@When("I input message with trailing emtpy spaces")
	public void IInputMessageWithTrailingEmptySpace() throws Throwable {
		message = automationMessage + "." + onlySpacesMessage;
		PagesCollection.dialogPage.sendStringToInput(message);
	}

	@When("I input message with lower case and upper case")
	public void IInputMessageWithLowerAndUpperCase() throws Throwable {
		message = CommonUtils.generateRandomString(7).toLowerCase()
				+ CommonUtils.generateRandomString(7).toUpperCase();
		PagesCollection.dialogPage.sendStringToInput(message);
	}

	@When("I input more than 200 chars message and send it")
	public void ISend200CharsMessage() throws Exception {
		message = CommonUtils.generateRandomString(210).toLowerCase()
				.replace("x", " ");
		PagesCollection.dialogPage.sendMessageUsingScript(message);
	}

	@When("I tap and hold on message input")
	public void ITapHoldTextInput() throws Exception {
		PagesCollection.dialogPage.tapHoldTextInput();
	}

	@When("^I scroll to the beginning of the conversation$")
	public void IScrollToTheBeginningOfTheConversation() throws Throwable {
		PagesCollection.dialogPage.scrollToBeginningOfConversation();
	}

	@When("^I send predefined message (.*)$")
	public void ISendPredefinedMessage(String message) throws Throwable {
		PagesCollection.dialogPage.sendStringToInput(message + "\n");
	}

	@When("^I send using script predefined message (.*)$")
	public void ISendUsingScriptPredefinedMessage(String message)
			throws Exception {
		PagesCollection.dialogPage.sendMessageUsingScript(message);
	}

	@When("I verify image in dialog is same as template (.*)")
	public void IVerifyImageInDialogSameAsTemplate(String filename)
			throws Throwable {
		BufferedImage templateImage = PagesCollection.dialogPage
				.takeImageScreenshot();
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
		PagesCollection.dialogPage = PagesCollection.dialogPage.scrollToImage();
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
		double score;
		if (PagesCollection.dialogPage != null) {
			PagesCollection.dialogPage.waitPingAnimation();
			score = PagesCollection.dialogPage.checkPingIcon(iconLabel);
		} else {
			PagesCollection.groupChatPage.waitPingAnimation();
			score = PagesCollection.groupChatPage.checkPingIcon(iconLabel);
		}
		Assert.assertTrue(
				"Overlap between two images has not enough score. Expected >= 0.75, current = "
						+ score, score >= 0.75d);
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
		PagesCollection.dialogPage.scrollToEndOfConversation();
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
		mail = usrMgr.findUserByEmailOrEmailAlias(mail).getEmail();
		final String finalString = String.format(sendInviteMailContent, mail);
		String lastMessage = PagesCollection.dialogPage
				.getLastMessageFromDialog();
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
		if (PagesCollection.dialogPage != null) {
			Assert.assertTrue(username + " called message is missing in dialog", PagesCollection.dialogPage
					.isMessageVisible(expectedCallMessage));
		}
		else {
			Assert.fail("This method is for dialog page and current page is not or is no initiated");
		}
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
		PagesCollection.callPage = PagesCollection.dialogPage
				.clickOnCallButtonForContact(contact.toUpperCase());
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
		boolean result = PagesCollection.dialogPage
				.isTypeOrSlideExists(TAP_OR_SLIDE);
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
		boolean chatheadIsVisible = PagesCollection.dialogPage
				.chatheadIsVisible(contact);
		Assert.assertTrue("No Chathead visible.", chatheadIsVisible);
		boolean chMessageIsVisible = PagesCollection.dialogPage
				.chatheadMessageIsVisible(message);
		Assert.assertTrue("No Chathead message visible.", chMessageIsVisible);
		boolean chAvatarImageIsVisible = PagesCollection.dialogPage
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
		boolean chatheadIsVisible = PagesCollection.dialogPage
				.chatheadIsVisible(contact);
		Assert.assertFalse("Chathead visible.", chatheadIsVisible);
	}
}
