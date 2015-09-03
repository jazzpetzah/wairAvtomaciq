package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletConversationViewPage;
import com.wearezeta.auto.android_tablet.pages.camera.ConversationViewCameraPage;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationViewPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletConversationViewPage getConversationViewPage()
			throws Exception {
		return (TabletConversationViewPage) pagesCollection
				.getPage(TabletConversationViewPage.class);
	}

	private ConversationViewCameraPage getConversationViewCameraPage()
			throws Exception {
		return (ConversationViewCameraPage) pagesCollection
				.getPage(ConversationViewCameraPage.class);
	}

	/**
	 * Verifies whether conversation view is currently visible or not
	 * 
	 * @step. ^I (do not )?see (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @param shouldNotSee
	 *            equals to null is 'do not ' does not exist in step signature
	 * 
	 * @throws Exception
	 */
	@When("^I (do not )?see (?:the |\\s*)[Cc]onversation view$")
	public void ISeeConversationView(String shouldNotSee) throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue("The conversation view is not currently visible",
					getConversationViewPage().waitUntilVisible());
		} else {
			Assert.assertTrue(
					"The conversation view is visible, but should be hidden",
					getConversationViewPage().waitUntilInvisible());
		}
	}

	/**
	 * Tap the Show Tools button on conversation view
	 * 
	 * @step. ^I tap Show Tools button on [Cc]onversation view page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Show Tools button on [Cc]onversation view page$")
	public void ITapShowToolsButton() throws Exception {
		getConversationViewPage().tapShowInstrumentsButton();
	}

	/**
	 * Tap the Close Tools button on conversation view
	 * 
	 * @step. ^I tap Close Tools button on [Cc]onversation view page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Close Tools button on [Cc]onversation view page$")
	public void ITapCloseToolsButton() throws Exception {
		getConversationViewPage().tapCloseInstrumentsButton();
	}

	/**
	 * Tap the Show Details button on conversation view
	 * 
	 * @step. ^I tap Show Details button on [Cc]onversation view page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Show Details button on [Cc]onversation view page$")
	public void ITapShowDetailsButton() throws Exception {
		getConversationViewPage().tapShowDetailsButton();
	}

	/**
	 * Verify the last conversation message contains expected text
	 * 
	 * @step. ^I see the system message contains \"(.*)\" text on
	 *        [Cc]onversation view page$
	 * 
	 * @param expectedMessage
	 *            the message to verify
	 * @throws Exception
	 */
	@Then("^I see the system message contains \"(.*)\" text on [Cc]onversation view page$")
	public void ISeeTheLastSystemMessage(String expectedMessage)
			throws Exception {
		expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(
				String.format(
						"The system message containing text '%s' is not visible in the conversation view",
						expectedMessage), getConversationViewPage()
						.waitForSystemMessageContains(expectedMessage));
	}

	/**
	 * Verify the last conversation name message text
	 * 
	 * @step. ^I see the conversation name system message \"(.*)\" on
	 *        [Cc]onversation view page$
	 * 
	 * @param expectedMessage
	 *            the expected conversation name
	 * @throws Exception
	 */
	@Then("^I see the conversation name system message \"(.*)\" on [Cc]onversation view page$")
	public void ISeeTheConversationNameSystemMessage(String expectedMessage)
			throws Exception {
		expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(String.format(
				"The conversation name system message does not equal to '%s'",
				expectedMessage), getConversationViewPage()
				.waitForConversationNameSystemMessage(expectedMessage));
	}

	/**
	 * Verify the connection system conversation message contains expected text
	 * 
	 * @step. ^I see the system connection message contains \"(.*)\" text on
	 *        [Cc]onversation view page$
	 * 
	 * @param expectedMessage
	 *            the message to verify
	 * @throws Exception
	 */
	@Then("^I see the system connection message contains \"(.*)\" text on [Cc]onversation view page$")
	public void ISeeTheSystemConnectionMessage(String expectedMessage)
			throws Exception {
		expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(
				String.format(
						"The system connection message containing text '%s' is not visible in the conversation view",
						expectedMessage),
				getConversationViewPage()
						.waitForSystemConnectionMessageContains(expectedMessage));
	}

	/**
	 * Verify the chat header system message contains expected text
	 * 
	 * @step. ^I see the chat header message contains \"(.*)\" text on
	 *        conversation view page$
	 * 
	 * @param expectedMessage
	 *            the message to verify
	 * @throws Exception
	 */
	@Then("^I see the chat header message contains \"(.*)\" text on conversation view page$")
	public void ISeeChatHeaderSystemMessage(String expectedMessage)
			throws Exception {
		expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(
				String.format(
						"The chat header message containing text '%s' is not visible in the conversation view",
						expectedMessage), getConversationViewPage()
						.waitForChatHeaderMessageContains(expectedMessage));
	}

	/**
	 * Verify whether the particular outgoing invitation message is visible in
	 * conversation view
	 * 
	 * @step. ^I see the outgoing invitation message \"(.*)\" on [Cc]onversation
	 *        view page$
	 * 
	 * @param expectedMessage
	 *            the expected message text
	 * @throws Exception
	 */
	@Then("^I see the outgoing invitation message \"(.*)\" on [Cc]onversation view page$")
	public void ISeeOutgoungInvitationMessage(String expectedMessage)
			throws Exception {
		expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
				FindBy.NAME_ALIAS);
		Assert.assertTrue(
				String.format(
						"The outgoing invitation message containing text '%s' is not visible in the conversation view",
						expectedMessage), getConversationViewPage()
						.waitForOutgoingInvitationMessage(expectedMessage));
	}

	/**
	 * Tap the text input field in the conversation view to start typing
	 * 
	 * @step. ^I tap (?:the |\\s*)text input in (?:the |\\s*)[Cc]onversation
	 *        view$
	 * 
	 * @throws Exception
	 */
	@And("^I tap (?:the |\\s*)text input in (?:the |\\s*)[Cc]onversation view$")
	public void ITapTheTextInput() throws Exception {
		getConversationViewPage().tapTextInput();
	}

	/**
	 * Type a message into the active conversation. The text input should be
	 * already active. The message is NOT sent automatically
	 * 
	 * @step. ^I type (?:the |\\s*)message \"(.*)\" in (?:the
	 *        |\\s*)[Cc]onversation view$
	 * 
	 * @param message
	 *            the text to type
	 * @throws Exception
	 */
	@And("^I type (?:the |\\s*)message \"(.*)\" in (?:the |\\s*)[Cc]onversation view$")
	public void ITypeMessage(String message) throws Exception {
		getConversationViewPage().typeMessage(message);
	}

	/**
	 * Tap Enter to send the typed message into the conversation
	 * 
	 * @step. ^I send the typed message in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@And("^I send the typed message in (?:the |\\s*)[Cc]onversation view$")
	public void ISendMessage() throws Exception {
		getConversationViewPage().sendMessage();
	}

	/**
	 * Verify whether the message is visible in the conversation view
	 * 
	 * @step. ^I (do not )?see the message \"(.*)\" in (?:the
	 *        |\\s*)[Cc]onversation view$
	 * 
	 * @param expectedMessage
	 *            the message to check
	 * @param shouldNotSee
	 *            equals to null if the message should be visible in the convo
	 *            view
	 * @throws Exception
	 */
	@Then("^I (do not )?see the message \"(.*)\" in (?:the |\\s*)[Cc]onversation view$")
	public void ISeeMessage(String shouldNotSee, String expectedMessage)
			throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue(
					String.format(
							"The expected message '%s' is not visible in the conversation view",
							expectedMessage), getConversationViewPage()
							.waitUntilMessageIsVisible(expectedMessage));
		} else {
			Assert.assertTrue(
					String.format(
							"The expected message '%s' is visible in the conversation view, but it should not",
							expectedMessage), getConversationViewPage()
							.waitUntilMessageIsNotVisible(expectedMessage));
		}
	}

	/**
	 * Swipe on the text input field to show the available instruments
	 * 
	 * @step. ^I swipe left on text input in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe left on text input in (?:the |\\s*)[Cc]onversation view$")
	public void ISwipeLeftOnTextInput() throws Exception {
		getConversationViewPage().swipeLeftOnTextInput();
	}

	/**
	 * Tap the Ping button to send Ping event from the currently opened
	 * conversation
	 * 
	 * @step. ^I tap Ping button in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Ping button in (?:the |\\s*)[Cc]onversation view$")
	public void ITapPingButton() throws Exception {
		getConversationViewPage().tapPingButton();
	}

	/**
	 * Tap the Add Picture button. The input field slider should be already
	 * opened
	 * 
	 * @step. ^I tap Add Picture button in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Add Picture button in (?:the |\\s*)[Cc]onversation view$")
	public void ITapAddPicture() throws Exception {
		getConversationViewCameraPage().tapLensButton();
	}

	/**
	 * Tap the Take Photo button. The Add Picture button should be already
	 * clicked
	 * 
	 * @step. ^I tap Take Photo button in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Take Photo button in (?:the |\\s*)[Cc]onversation view$")
	public void ITapTakePhotoButton() throws Exception {
		getConversationViewCameraPage().tapTakePhotoButton();
	}

	/**
	 * Tap the Select From Gallery button. The Add Picture button should be
	 * already clicked
	 * 
	 * @step. ^I tap Gallery button in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Gallery button in (?:the |\\s*)[Cc]onversation view$")
	public void ITapGalleryButton() throws Exception {
		getConversationViewCameraPage().tapGalleryButton();
	}

	/**
	 * Confirm the taken photo or selected picture
	 * 
	 * @step. ^I confirm the picture for (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@When("^I confirm the picture for (?:the |\\s*)[Cc]onversation view$")
	public void IConfirmPicture() throws Exception {
		getConversationViewCameraPage().confirmPictureSelection();
	}

	/**
	 * Verify whether there is at least one picture in the conversation view
	 * 
	 * @step. ^I (do not )?see (?:a|any) new pictures? in (?:the
	 *        |\\s*)[Cc]onversation view$
	 * 
	 * @param shouldNotSee
	 *            equals to null if 'do not' exists in step signature
	 * 
	 * @throws Exception
	 */
	@Then("^I (do not )?see (?:a|any) new pictures? in (?:the |\\s*)[Cc]onversation view$")
	public void ISeeNewPicture(String shouldNotSee) throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue(
					"No new pictures are visible in the conversation view",
					getConversationViewPage().waitUntilAPictureAppears());
		} else {
			Assert.assertTrue(
					"Some pictures are still visible in the conversation view",
					getConversationViewPage().waitUntilPicturesNotVisible());
		}
	}

	/**
	 * Tap the recent picture in the conversation view to open a preview
	 * 
	 * @step. ^I tap the new picture in (?:the |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@When("^I tap the new picture in (?:the |\\s*)[Cc]onversation view$")
	public void ITapNewPicture() throws Exception {
		getConversationViewPage().tapRecentPicture();
	}

	/**
	 * Verify whether ping message is visible in the current conversation view
	 * 
	 * @step. ^I (do not )?see the [Pp]ing message \"<(.*)>\" in (?:the
	 *        |\\s*)[Cc]onversation view$
	 * 
	 * @param shouldNotBeVisible
	 *            equals to null if "do not" part does not exist in the step
	 *            signature
	 * @param expectedMessage
	 *            the text of expected ping message
	 * @throws Exception
	 */
	@Then("^I (do not )?see the [Pp]ing message \"(.*)\" in (?:the |\\s*)[Cc]onversation view$")
	public void ISeePingMessage(String shouldNotBeVisible,
			String expectedMessage) throws Exception {
		expectedMessage = usrMgr.replaceAliasesOccurences(expectedMessage,
				FindBy.NAME_ALIAS);
		if (shouldNotBeVisible == null) {
			Assert.assertTrue(
					String.format(
							"The expected ping message '%s' is not visible in the conversation view",
							expectedMessage), getConversationViewPage()
							.waitUntilPingMessageIsVisible(expectedMessage));
		} else {
			Assert.assertTrue(
					String.format(
							"The ping message '%s' is still visible in the conversation view",
							expectedMessage), getConversationViewPage()
							.waitUntilPingMessageIsInvisible(expectedMessage));
		}
	}

	/**
	 * Verify whether missed call notification is visible in conversation view
	 * 
	 * @step. ^I see missed (?:group |\\s*)call notification in (?:the
	 *        |\\s*)[Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@Then("^I see missed (?:group |\\s*)call notification in (?:the |\\s*)[Cc]onversation view$")
	public void ISeeMissedCallNotification() throws Exception {
		// Notifications for both group and 1:1 calls have the same locators so
		// we don't really care
		Assert.assertTrue(
				"The expected missed call notification is not visible in the conversation view",
				getConversationViewPage().waitUntilGCNIsVisible());
	}

	/**
	 * Swipe right to show the convo list
	 * 
	 * @step. ^I swipe right to show (?:the |\\s*)conversations list$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe right to show (?:the |\\s*)conversations list$")
	public void ISwipeRight() throws Exception {
		getConversationViewPage().doSwipeRight();
	}

	/**
	 * Scroll to the bottom side of conversation view
	 * 
	 * @step. ^I scroll to the bottom of the [Cc]onversation view$
	 * 
	 * @throws Exception
	 */
	@When("^I scroll to the bottom of the [Cc]onversation view$")
	public void IScrollToTheBottom() throws Exception {
		getConversationViewPage().scrollToTheBottom();
	}

	private static final double MAX_SIMILARITY_THRESHOLD = 0.95;

	private static enum PictureDestination {
		CONVERSATION_VIEW, PREVIEW;
	}

	/**
	 * Verify whether the recent picture in convo view is animated
	 * 
	 * @step. ^I see the picture in (?:the |\\s*)(preview|[Cc]onversation view)
	 *        is animated$
	 * 
	 * @param destination
	 *            either 'preview' or 'conversation view'
	 * 
	 * @throws Exception
	 */
	@Then("^I see the picture in (?:the |\\s*)(preview|[Cc]onversation view) is animated$")
	public void ISeePictureIsAnimated(String destination) throws Exception {
		final PictureDestination dst = PictureDestination.valueOf(destination
				.toUpperCase().replace(" ", "_"));
		double avgThreshold;
		// no need to wait, since screenshoting procedure itself is quite long
		final long screenshotingDelay = 0;
		final int maxFrames = 4;
		switch (dst) {
		case CONVERSATION_VIEW:
			avgThreshold = ImageUtil.getAnimationThreshold(
					getConversationViewPage()::getRecentPictureScreenshot,
					maxFrames, screenshotingDelay);
			Assert.assertTrue(
					String.format(
							"The picture in the conversation view seems to be static (%.2f >= %.2f)",
							avgThreshold, MAX_SIMILARITY_THRESHOLD),
					avgThreshold < MAX_SIMILARITY_THRESHOLD);
			break;
		case PREVIEW:
			avgThreshold = ImageUtil.getAnimationThreshold(
					getConversationViewPage()::getPreviewPictureScreenshot,
					maxFrames, screenshotingDelay);
			Assert.assertTrue(
					String.format(
							"The picture in the image preview view seems to be static (%.2f >= %.2f)",
							avgThreshold, MAX_SIMILARITY_THRESHOLD),
					avgThreshold < MAX_SIMILARITY_THRESHOLD);
			break;
		}
	}

	/**
	 * Verify whether unsent indicator is visible next to the particular message
	 * 
	 * @step. ^I see unsent indicator next to the message \"(.*)\" in the
	 *        [Cc]onversation view$
	 * 
	 * @param msg
	 *            the expected message text
	 * @throws Exception
	 */
	@Then("^I see unsent indicator next to the message \"(.*)\" in the [Cc]onversation view$")
	public void ISeeUnsentIndicatorNextTo(String msg) throws Exception {
		Assert.assertTrue(
				String.format(
						"Unsent indicator is not visible next to the '%s' message",
						msg), getConversationViewPage()
						.waitUntilUnsentIndicatorIsVisible(msg));
	}

	/**
	 * Verify whether unsent indicator is visible next to a picture
	 * 
	 * @step. ^I see unsent indicator next to new picture in the [Cc]onversation
	 *        view$
	 * 
	 * @throws Exception
	 */
	@Then("^I see unsent indicator next to new picture in the [Cc]onversation view$")
	public void ISeeUnsentIndicatorNextToAPicture() throws Exception {
		Assert.assertTrue("Unsent indicator is not visible next to a picture",
				getConversationViewPage()
						.waitUntilUnsentIndicatorIsVisibleForAPicture());
	}
}
