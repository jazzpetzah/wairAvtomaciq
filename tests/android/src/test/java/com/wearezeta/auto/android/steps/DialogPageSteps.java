package com.wearezeta.auto.android.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;

import cucumber.api.java.en.*;

public class DialogPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private static final String ANDROID_LONG_MESSAGE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
			+ "Maecenas sed lorem dignissim lacus tincidunt scelerisque nec sed sem. Nunc lacinia non tortor a fringilla. "
			+ "Fusce cursus neque at posuere viverra. Duis ultricies ipsum ac leo mattis, a aliquet neque consequat. "
			+ "Vestibulum ut eros eu risus mattis iaculis quis ac eros. Nam sit amet venenatis felis. "
			+ "Vestibulum blandit nisi felis, id hendrerit quam viverra at. Curabitur nec facilisis felis.";
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
		if (PagesCollection.dialogPage == null) {
			PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		}
		PagesCollection.dialogPage.waitForCursorInputVisible();
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
		PagesCollection.dialogPage.tapOnCursorInput();
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
		PagesCollection.dialogPage.typeAndSendMessage(expandMessage(msg));
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
		PagesCollection.dialogPage.typeMessage(expandMessage(msg));
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
		PagesCollection.dialogPage.pressKeyboardSendButton();
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
		PagesCollection.dialogPage.swipeOnCursorInput();
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
		PagesCollection.dialogPage.tapAddPictureBtn();
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
		PagesCollection.dialogPage.tapPingBtn();
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
		PagesCollection.dialogPage.tapCallBtn();
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
		PagesCollection.dialogPage.tapMuteBtn();
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
		PagesCollection.dialogPage.tapSpeakerBtn();
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
		PagesCollection.dialogPage.tapCancelCallBtn();
	}

	/**
	 * Checks to see if a certain calling button is pressed
	 * 
	 * @step. ^I see (.*) calling button is pressed$
	 * 
	 * @param buttonName
	 *            the name of the calling button to check
	 * 
	 * @throws Exception
	 */
	@Then("^I see (.*) calling button is pressed$")
	public void WhenIPressCancelCallButton(String buttonName) throws Exception {
		final double score = PagesCollection.dialogPage
				.getExpectedButtonStateOverlapScore(buttonName);
		Assert.assertTrue(
				"Calling button not present or not clicked. Expected >= 0.95, current = "
						+ score, score >= 0.95d);
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
			Assert.assertTrue("Call overlay not visible",
					PagesCollection.dialogPage.checkCallingOverlay());
		} else {
			Assert.assertTrue(
					"Call overlay is visible, it should have been dismissed",
					PagesCollection.dialogPage.checkNoCallingOverlay());
		}
	}

	/**
	 * Tap on Dialog page bottom for scrolling page to the end
	 * 
	 * @step. ^I tap Dialog page bottom$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I tap Dialog page bottom$")
	public void WhenITapOnDialogPageBottom() throws Exception {
		PagesCollection.dialogPage.tapDialogPageBottom();
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
		PagesCollection.otherUserPersonalInfoPage = ((DialogPage) PagesCollection.androidPage)
				.tapConversationDetailsButton();
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
		PagesCollection.dialogPage.tapPlayPauseBtn();
	}

	/**
	 * Tap on Play on youtube container
	 * 
	 * @step. ^I press play on youtube container$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I press play on youtube container$")
	public void WhenIPressPlayOnYoutubeContainer() throws Exception {
		PagesCollection.dialogPage.tapYouTubePlay();
	}

	/**
	 * Waits until URL bar is visible to confirm tapping on YouTube link takes
	 * you out of Wire to view video
	 * 
	 * @step. ^I am taken out of Wire and into the native browser app$
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I am taken out of Wire and into the native browser app$")
	public void IPlayYoutubeVideoInNativeBrowser() throws Exception {
		PagesCollection.dialogPage.isNativeBrowserURLVisible();
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
		PagesCollection.dialogPage.tapPlayPauseMediaBarBtn();
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
			Thread.sleep(1000);
			PagesCollection.dialogPage.takePhoto();
			break;
		case "confirm":
			PagesCollection.dialogPage.confirm();
			break;
		case "gallery":
			PagesCollection.dialogPage.openGallery();
			break;
		case "image close":
			PagesCollection.dialogPage.closeFullScreenImage();
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
		PagesCollection.dialogPage.selectPhoto();
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
		Assert.assertTrue(PagesCollection.dialogPage.getLastPingText().equals(
				message));
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
		PagesCollection.dialogPage.waitForMessage(expandMessage(msg));
	}

	/**
	 * Verifies the URL is in the chat
	 * 
	 * @step. ^I see URL in the dialog$
	 * 
	 * @throws Exception
	 * 
	 */
	@Then("^I see URL in the dialog$")
	public void ThenISeeURLInDialog() throws Exception {
		// FIXME: Magic string
		PagesCollection.dialogPage.waitForMessage("www.google.com");
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
		Assert.assertTrue(PagesCollection.dialogPage.isImageExists());
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
		PagesCollection.dialogPage.clickLastImageFromDialog();
	}

	private static final int SWIPE_DURATION_MILLISECONDS = 2000;
	
	/**
	 * 
	 * @step. ^I swipe up on dialog page
	 * 
	 * @throws Exception
	 */
	@When("^I swipe up on dialog page$")
	public void WhenISwipeUpOnDialogPage() throws Exception {
		if (PagesCollection.dialogPage == null) {
			PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		}
		PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.dialogPage
				.swipeUp(SWIPE_DURATION_MILLISECONDS);
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
		if (PagesCollection.dialogPage == null) {
			PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		}
		PagesCollection.dialogPage.swipeDown(SWIPE_DURATION_MILLISECONDS);
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
		PagesCollection.contactListPage = PagesCollection.dialogPage
				.navigateBack();
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
		if (PagesCollection.dialogPage == null) {
			PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		}
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(PagesCollection.dialogPage
				.getConnectRequestChatLabel().contains(contact.toLowerCase()));
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
		double score = PagesCollection.dialogPage.checkPingIcon(iconLabel);
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
		assert PagesCollection.dialogPage.isDialogVisible() : "Group chat view is not visible";
		List<String> participantNames = new ArrayList<String>();
		for (String nameAlias : CommonSteps
				.splitAliases(participantNameAliases)) {
			participantNames.add(usrMgr.findUserByNameOrNameAlias(nameAlias)
					.getName());
		}
		Assert.assertTrue(PagesCollection.dialogPage
				.isGroupChatDialogContainsNames(participantNames));
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
		PagesCollection.dialogPage.waitForMessage(message + " " + contact);
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
		Assert.assertEquals(
				PagesCollection.dialogPage.getChangedGroupNameMessage(),
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
	 */
	@Then("^Last message is (.*)$")
	public void ThenLastMessageIs(String message) {
		Assert.assertEquals(message.toLowerCase().trim(),
				PagesCollection.dialogPage.getLastMessageFromDialog()
						.toLowerCase().trim());
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
		double score = PagesCollection.dialogPage
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
		double score = PagesCollection.dialogPage
				.getMediaControlIconOverlapScore(iconLabel);
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
		Assert.assertEquals(contact + " CALLED",
				PagesCollection.dialogPage.getMissedCallMessage());
	}
}
