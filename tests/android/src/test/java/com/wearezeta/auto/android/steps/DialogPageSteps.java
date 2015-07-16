package com.wearezeta.auto.android.steps;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.*;

public class DialogPageSteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private DialogPage getDialogPage() throws Exception {
		return (DialogPage) pagesCollection.getPage(DialogPage.class);
	}

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private static final String ANDROID_LONG_MESSAGE = CommonUtils
			.generateRandomString(300);
	private static final String LONG_MESSAGE_ALIAS = "LONG_MESSAGE";

	private static String expandMessage(String message) {
		final Map<String, String> specialStrings = new HashMap<String, String>();
		specialStrings.put(LONG_MESSAGE_ALIAS, ANDROID_LONG_MESSAGE);
		if (specialStrings.containsKey(message)) {
			return specialStrings.get(message);
		} else {
			return message;
		}
	}

	/**
	 * Waits for the dialog page to appear This step makes no assertions and
	 * doesn't fail if the dialog page doesn't appear.
	 * 
	 * @step. ^I see dialog page$
	 * 
	 * @throws Exception
	 */
	@When("^I see dialog page$")
	public void WhenISeeDialogPage() throws Exception {
		getDialogPage().waitForCursorInputVisible();
	}

	/**
	 * Taps on the text input
	 * 
	 * @step. ^I tap on text input$
	 * 
	 * @throws Exception
	 */
	@When("^I tap on text input$")
	public void WhenITapOnTextInput() throws Exception {
		getDialogPage().tapOnCursorInput();
	}

	/**
	 * Send message to the chat
	 * 
	 * @step. ^I type the message \"(.*)\" and send it$
	 * 
	 * @param msg
	 *            message to type. There are several special shortcuts:
	 *            LONG_MESSAGE - to type long message
	 * 
	 * @throws Exception
	 */
	@When("^I type the message \"(.*)\" and send it$")
	public void ITypeMessageAndSendIt(String msg) throws Exception {
		getDialogPage().typeAndSendMessage(expandMessage(msg));
	}

	/**
	 * Inputs a custom message and does NOT send it
	 * 
	 * @step. ^I type the message \"(.*)\"$
	 * 
	 * @param msg
	 *            message to type. There are several special shortcuts:
	 *            LONG_MESSAGE - to type long message
	 * 
	 * @throws Exception
	 */
	@When("^I type the message \"(.*)\"$")
	public void ITypeMessage(String msg) throws Exception {
		getDialogPage().typeMessage(expandMessage(msg));
	}

	/**
	 * Sends the message by pressing the keyboard send button
	 * 
	 * @step. ^I send the message$
	 * 
	 * @throws Exception
	 */
	@When("^I send the message$")
	public void ISendTheMessage() throws Exception {
		getDialogPage().pressKeyboardSendButton();
	}

	/**
	 * Swipes the text input area to reveal the different input options
	 * 
	 * @step. ^I swipe on text input$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe on text input$")
	public void WhenISwipeOnTextInput() throws Exception {
		getDialogPage().swipeOnCursorInput();
	}

	/**
	 * Swipes the text input area to hide the different input options
	 * 
	 * @step. ^I swipe left on text input$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe left on text input$")
	public void WhenISwipeLeftOnTextInput() throws Exception {
		getDialogPage().swipeLeftOnCursorInput();
	}

	/**
	 * Presses the image input button to open the camera or gallery
	 * 
	 * @step. ^I press Add Picture button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Add Picture button$")
	public void WhenIPressAddPictureButton() throws Exception {
		getDialogPage().tapAddPictureBtn();
	}

	/**
	 * Press on the ping button in the input controls
	 * 
	 * @step. ^I press Ping button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Ping button$")
	public void WhenIPressPButton() throws Exception {
		getDialogPage().tapPingBtn();
	}

	/**
	 * Press on the call button in the input controls
	 * 
	 * @step. ^I press Call button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Call button$")
	public void WhenIPressCallButton() throws Exception {
		getDialogPage().tapCallBtn();
	}

	/**
	 * Press on the sketch button in the input controls
	 * 
	 * @step. ^I press Sketch button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Sketch button$")
	public void WhenIPressOnSketchButton() throws Exception {
		getDialogPage().tapSketchBtn();
	}

	/**
	 * Press on the mute button in the calling controls
	 * 
	 * @step. ^I press Mute button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Mute button$")
	public void WhenIPressMuteButton() throws Exception {
		getDialogPage().tapMuteBtn();
	}

	/**
	 * Press on the Speaker button in the calling controls
	 * 
	 * @step. ^I press Speaker button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Speaker button$")
	public void WhenIPressSpeakerButton() throws Exception {
		getDialogPage().tapSpeakerBtn();
	}

	/**
	 * Press on the Cancel call button in the Calling controls
	 * 
	 * @step. ^I press Cancel call button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Cancel call button$")
	public void WhenIPressCancelCallButton() throws Exception {
		getDialogPage().tapCancelCallBtn();
	}

	private final Map<String, BufferedImage> savedButtonStates = new HashMap<String, BufferedImage>();

	/**
	 * Takes screenshot of current button state for the further comparison
	 * 
	 * @step. ^I remember the current state of (\\w+) button$
	 * 
	 * @param buttonName
	 *            the name of the button to take screenshot. Available values:
	 *            MUTE, SPEAKER
	 * @throws Exception
	 */
	@When("^I remember the current state of (\\w+) button$")
	public void IRememberButtonState(String buttonName) throws Exception {
		savedButtonStates.put(buttonName, getDialogPage()
				.getCurrentButtonStateScreenshot(buttonName));
	}

	private static final long BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS = 15000;
	private static final double BUTTON_STATE_OVERLAP_MAX_SCORE = 0.4d;

	/**
	 * Checks to see if a certain calling button state is changed. Make sure,
	 * that the screenshot of previous state is already taken for this
	 * particular button
	 * 
	 * @step. ^I see (\\w+) button state is changed$
	 * 
	 * @param buttonName
	 *            the name of the button to check. Available values: MUTE,
	 *            SPEAKER
	 * 
	 * @throws Exception
	 */
	@Then("^I see (\\w+) button state is changed$")
	public void ICheckButtonStateIsChanged(String buttonName) throws Exception {
		if (!savedButtonStates.containsKey(buttonName)) {
			throw new IllegalStateException(
					String.format(
							"Please call the corresponding step to take the screenshot of previous '%s' button state first",
							buttonName));
		}
		final BufferedImage previousStateScreenshot = savedButtonStates
				.get(buttonName);
		final long millisecondsStarted = System.currentTimeMillis();
		double overlapScore;
		do {
			final BufferedImage currentStateScreenshot = getDialogPage()
					.getCurrentButtonStateScreenshot(buttonName);
			overlapScore = ImageUtil.getOverlapScore(currentStateScreenshot,
					previousStateScreenshot,
					ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
			if (overlapScore <= BUTTON_STATE_OVERLAP_MAX_SCORE) {
				return;
			}
			Thread.sleep(500);
		} while (System.currentTimeMillis() - millisecondsStarted <= BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS);
		throw new AssertionError(
				String.format(
						"Button state has not been changed within %s seconds timeout. Current overlap score: %.2f, expected overlap score: <= %.2f",
						BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS / 1000,
						overlapScore, BUTTON_STATE_OVERLAP_MAX_SCORE));
	}

	/**
	 * Checks to see if call overlay is present
	 * 
	 * @step. ^I see call overlay$
	 * @param shouldNotSee
	 *            is set to null if " do not" part does not exist
	 * 
	 * @throws Exception
	 */
	@Then("^I( do not)? see call overlay$")
	public void WhenISeeCallOverlay(String shouldNotSee) throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue("Call overlay not visible", getDialogPage()
					.checkCallingOverlay());
		} else {
			Assert.assertTrue(
					"Call overlay is visible, it should have been dismissed",
					getDialogPage().checkNoCallingOverlay());
		}
	}

	/**
	 * Tap on Dialog page bottom for scrolling page to the end
	 * 
	 * @step. ^I scroll to the bottom of conversation view$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I scroll to the bottom of conversation view$")
	public void IScrollToTheBottom() throws Exception {
		getDialogPage().tapDialogPageBottom();
	}

	/**
	 * Tap in Dialog page on details button to open participants view
	 * 
	 * @step. ^I tap conversation details button$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I tap conversation details button$")
	public void WhenITapConversationDetailsBottom() throws Exception {
		getDialogPage().tapConversationDetailsButton();
	}

	/**
	 * Tap on Play/Pause media item button
	 * 
	 * @step. ^I press PlayPause media item button$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I press PlayPause media item button$")
	public void WhenIPressPlayPauseButton() throws Exception {
		getDialogPage().tapPlayPauseBtn();
	}

	/**
	 * Verify that Play button is visible on youtube container
	 * 
	 * @step. ^I see Play button on youtube container$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I see Play button on [Yy]outube container$")
	public void ISeePlayButtonOnYoutubeContainer() throws Exception {
		Assert.assertTrue(
				"Youtube Play button is not visible, but it should be",
				getDialogPage().waitUntilYoutubePlayButtonVisible());
	}

	/**
	 * Tap on Play/Pause button on Media Bar
	 * 
	 * @step. ^I press PlayPause on Mediabar button$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I press PlayPause on Mediabar button$")
	public void WhenIPressPlayPauseOnMediaBarButton() throws Exception {
		getDialogPage().tapPlayPauseMediaBarBtn();
	}

	/**
	 * Presses a given button name Not clear which page is returned from a given
	 * action
	 * 
	 * @step. ^I press \"(.*)\" button$
	 * 
	 * @param buttonName
	 *            the button to press
	 * @throws Throwable
	 */
	@When("^I press \"(.*)\" button$")
	public void WhenIPressButton(String buttonName) throws Throwable {
		switch (buttonName.toLowerCase()) {
		case "take photo":
			getDialogPage().takePhoto();
			break;
		case "confirm":
			getDialogPage().confirm();
			break;
		case "gallery":
			getDialogPage().openGallery();
			break;
		case "image close":
			getDialogPage().closeFullScreenImage();
			break;
		}
	}

	/**
	 * Selects the first picture from the gallery to send in the dialog
	 * 
	 * @step. ^I select picture for dialog$
	 * 
	 * @throws Exception
	 */
	@When("^I select picture for dialog$")
	public void WhenISelectPicture() throws Exception {
		getDialogPage().selectFirstGalleryPhoto();
	}

	/**
	 * Used to check that a ping has been sent Not very clear what this step
	 * does
	 * 
	 * @step. ^I see Ping message (.*) in the dialog$
	 * 
	 * @param message
	 * @throws Exception
	 */
	@Then("^I see Ping message (.*) in the dialog$")
	public void ThenISeePingMessageInTheDialog(String message) throws Exception {
		message = usrMgr.replaceAliasesOccurences(message, FindBy.NAME_ALIAS);
		Assert.assertTrue(String.format(
				"Ping message '%s' is not visible after the timeout", message),
				getDialogPage().waitForPingMessageWithText(message));
	}

	/**
	 * Checks to see that a message that has been sent appears in the chat
	 * history
	 * 
	 * @step. ^I see my message \"(.*)\" in the dialog$
	 * 
	 * @throws Exception
	 */
	@Then("^I see my message \"(.*)\" in the dialog$")
	public void ThenISeeMyMessageInTheDialog(String msg) throws Exception {
		getDialogPage().waitForMessage(expandMessage(msg));
	}

	/**
	 * Checks to see that a photo exists in the chat history. Does not check
	 * which photo though
	 * 
	 * @step. ^I see new photo in the dialog$
	 * 
	 * @throws Throwable
	 */
	@Then("^I see new photo in the dialog$")
	public void ThenISeeNewPhotoInTheDialog() throws Throwable {
		Assert.assertTrue("No new photo is present in the chat",
				getDialogPage().isImageExists());
	}

	/**
	 * Selects the last picture sent in a conversation view dialog
	 * 
	 * @step. ^I select last photo in dialog$
	 * 
	 * @throws Throwable
	 * 
	 */
	@When("^I select last photo in dialog$")
	public void WhenISelectLastPhotoInDialog() throws Throwable {
		getDialogPage().clickLastImageFromDialog();
	}

	private static final int SWIPE_DURATION_MILLISECONDS = 1000;

	/**
	 * 
	 * @step. ^I swipe up on dialog page
	 * 
	 * @throws Exception
	 */
	@When("^I swipe up on dialog page$")
	public void WhenISwipeUpOnDialogPage() throws Exception {
		getDialogPage().swipeUp(SWIPE_DURATION_MILLISECONDS);
	}

	/**
	 * Swipe down on dialog page
	 * 
	 * @step. ^I swipe down on dialog page$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I swipe down on dialog page$")
	public void WhenISwipedownOnDialogPage() throws Exception {
		getDialogPage().swipeDown(SWIPE_DURATION_MILLISECONDS);
	}

	private static final int MAX_SWIPES = 5;

	/**
	 * Swipe down on dialog page until Mediabar appears
	 * 
	 * @step. ^I swipe down on dialog page until Mediabar appears$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I swipe down on dialog page until Mediabar appears$")
	public void ISwipedownOnDialogPageUntilMediaBarAppears() throws Exception {
		int swipeNum = 1;
		while (swipeNum <= MAX_SWIPES) {
			getDialogPage().swipeDown(SWIPE_DURATION_MILLISECONDS);
			if (getDialogPage().waitUntilMediaBarVisible(2)) {
				return;
			}
			swipeNum++;
		}
		assert getDialogPage().waitUntilMediaBarVisible(1) : "The Media bar is still not visible after "
				+ MAX_SWIPES + " swipes";
	}

	/**
	 * Navigates back to the contact list page using a swipe right
	 * 
	 * @step. ^I navigate back from dialog page$
	 * 
	 * @throws Exception
	 */
	@When("^I navigate back from dialog page$")
	public void WhenINavigateBackFromDialogPage() throws Exception {
		getDialogPage().navigateBack(1000);
	}

	/**
	 * Checks to see that the "Connected To XYZ" appears at the start of a new
	 * dialog (Should changed step name to "Connected to")
	 * 
	 * @step. ^I see Connect to (.*) Dialog page$
	 * 
	 * @param contact
	 * 
	 * @throws Exception
	 */
	@Then("^I see Connect to (.*) Dialog page$")
	public void ThenIseeConnectToDialogPage(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		final String actualLabel = getDialogPage().getConnectRequestChatLabel();
		Assert.assertTrue(String.format(
				"The actual label '%s' does not contain '%s' part",
				actualLabel, contact),
				actualLabel.toLowerCase().contains(contact.toLowerCase()));
	}

	/**
	 * Seems to currently be blocked out in all tests
	 * 
	 * @step. ^I see (.*) icon$
	 * 
	 * @param iconLabel
	 * @throws Exception
	 */
	@Then("^I see (.*) icon$")
	public void ThenIseeIcon(String iconLabel) throws Exception {
		final double score = getDialogPage().checkPingIcon(iconLabel);
		Assert.assertTrue(
				"Overlap between two images has not enough score. Expected >= 0.75, current = "
						+ score, score >= 0.75d);
	}

	// ------- From Group Chat Page
	public static final String userRemovedMessage = "YOU REMOVED ";

	/**
	 * Checks to see that a group chat exists, where the name of the group chat
	 * is the list of users
	 * 
	 * @step. ^I see group chat page with users (.*)$
	 * 
	 * @param participantNameAliases
	 * @throws Exception
	 */
	@Then("^I see group chat page with users (.*)$")
	public void ThenISeeGroupChatPage(String participantNameAliases)
			throws Exception {
		assert getDialogPage().isDialogVisible() : "Group chat view is not visible";
		List<String> participantNames = new ArrayList<String>();
		for (String nameAlias : CommonSteps
				.splitAliases(participantNameAliases)) {
			participantNames.add(usrMgr.findUserByNameOrNameAlias(nameAlias)
					.getName());
		}
		Assert.assertTrue(getDialogPage().isGroupChatDialogContainsNames(
				participantNames));
	}

	/**
	 * Used to check that the message "YOU REMOVED XYZ" from group chat appears
	 * 
	 * 
	 * @step. ^I see message (.*) contact (.*) on group page$
	 * 
	 * @param message
	 * @param contact
	 * @throws Exception
	 */
	@Then("^I see message (.*) contact (.*) on group page$")
	public void ThenISeeMessageContactOnGroupPage(String message, String contact)
			throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName()
				.toUpperCase();
		getDialogPage().waitForMessage(message + " " + contact);
	}

	/**
	 * Checks to see that after the group was renamed, the user is informed of
	 * the change in the dialog page
	 * 
	 * @step. ^I see a message informing me that I renamed the conversation to
	 *        (.*)$
	 * 
	 * @param newConversationName
	 *            the new conversation name to check for
	 * @throws Exception
	 */
	@Then("^I see a message informing me that I renamed the conversation to (.*)$")
	public void ThenISeeMessageInformingGroupRename(String newConversationName)
			throws Exception {
		Assert.assertEquals(getDialogPage().getChangedGroupNameMessage(),
				newConversationName);
	}

	/**
	 * Used once to check that the last message sent is the same as what is
	 * expected
	 * 
	 * 
	 * @step. ^Last message is (.*)$
	 * 
	 * @param message
	 * @throws Exception
	 */
	@Then("^Last message is (.*)$")
	public void ThenLastMessageIs(String message) throws Exception {
		Assert.assertEquals(message.toLowerCase().trim(), getDialogPage()
				.getLastMessageFromDialog().toLowerCase().trim());
	}

	/**
	 * Verify that I see Play or Pause button on Mediabar
	 * 
	 * @step. ^I see (.*) on Mediabar$
	 * 
	 * @throws Exception
	 * 
	 */
	@Then("^I see (.*) on Mediabar$")
	public void ThenIseeOnMediaBar(String iconLabel) throws Exception {
		final double score = getDialogPage()
				.getMediaBarControlIconOverlapScore(iconLabel);
		Assert.assertTrue(
				"Overlap between two images has not enough score. Expected >= 0.75, current = "
						+ score, score >= 0.75d);
	}

	/**
	 * Verify that I see Play or Pause button in Media item
	 * 
	 * @step. ^I see (.*) button in Media$
	 * 
	 * @throws Exception
	 * 
	 */
	@Then("^I see (.*) button in Media$")
	public void ThenISeeButtonInMedia(String iconLabel) throws Exception {
		final double score = getDialogPage().getMediaControlIconOverlapScore(
				iconLabel);
		Assert.assertTrue(
				"Overlap between two images has not enough score. Expected >= 0.72, current = "
						+ score, score >= 0.72d);
	}

	/**
	 * Verify that dialog page contains missed call from contact
	 * 
	 * @step. ^I see dialog with missed call from (.*)$
	 * 
	 * @param contact
	 *            contact name string
	 * @throws Exception
	 */
	@Then("^I see dialog with missed call from (.*)$")
	public void ThenISeeDialogWithMissedCallFrom(String contact)
			throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertEquals(contact + " CALLED", getDialogPage()
				.getMissedCallMessage());
	}

	/**
	 * Swipes on calling bar to dismiss a call
	 * 
	 * @step. ^I dismiss calling bar by swipe$
	 * 
	 * @throws Exception
	 */
	@When("I dismiss calling bar by swipe$")
	public void IDismissCalling() throws Exception {
		Assert.assertTrue("Call overlay is not visible, nothing to swipe",
				getDialogPage().checkCallingOverlay());
		getDialogPage().swipeByCoordinates(1500, 30, 25, 30, 5);
	}
}
