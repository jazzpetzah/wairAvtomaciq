package com.wearezeta.auto.android;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.*;

public class DialogPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private static final String ANDROID_LONG_MESSAGE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
			+ "Maecenas sed lorem dignissim lacus tincidunt scelerisque nec sed sem. Nunc lacinia non tortor a fringilla. "
			+ "Fusce cursus neque at posuere viverra. Duis ultricies ipsum ac leo mattis, a aliquet neque consequat. "
			+ "Vestibulum ut eros eu risus mattis iaculis quis ac eros. Nam sit amet venenatis felis. "
			+ "Vestibulum blandit nisi felis, id hendrerit quam viverra at. Curabitur nec facilisis felis.";
	private String message;

	@When("^I see dialog page$")
	public void WhenISeeDialogPage() throws Exception {
		if (PagesCollection.dialogPage == null) {
			PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		}
		PagesCollection.dialogPage.waitForCursorInputVisible();
	}

	@When("^I tap on text input$")
	public void WhenITapOnTextInput() throws Exception {
		PagesCollection.dialogPage.tapOnCursorInput();
	}

	@When("^I type the message and send it$")
	public void WhenITypeRandomMessageAndSendIt() throws Exception {
		message = CommonUtils.generateGUID();
		PagesCollection.dialogPage.typeMessage(message);
	}

	@When("^I input (.*) message and send it$")
	public void ITypeTheMessageAndSendIt(String myMessage) throws Exception {
		message = myMessage;

		PagesCollection.dialogPage.typeMessage(myMessage);
	}

	@When("^I type long message and send it$")
	public void WhenITypeLongMessageAndSendIt() throws Throwable {
		message = ANDROID_LONG_MESSAGE;
		PagesCollection.dialogPage.typeMessage(message);
	}

	@When("^I type Upper/Lower case message and send it$")
	public void WhenITypeUpperLowerCaseAndSendIt() throws Throwable {
		message = CommonUtils.generateRandomString(5).toLowerCase() + " "
				+ CommonUtils.generateRandomString(5).toUpperCase();
		PagesCollection.dialogPage.typeMessage(message);
	}

	@When("^I multi tap on text input$")
	public void WhenIMultiTapOnTextInput() throws Throwable {
		PagesCollection.dialogPage.multiTapOnCursorInput();
	}

	@When("^I swipe on text input$")
	public void WhenISwipeOnTextInput() throws Exception {
		PagesCollection.dialogPage.SwipeOnCursorInput();
	}

	@When("^I swipe left on text input$")
	public void WhenISwipeLeftOnTextInput() throws Exception {
		PagesCollection.dialogPage.SwipeLeftOnCursorInput();
	}

	@When("^I press Add Picture button$")
	public void WhenIPressAddPictureButton() throws Throwable {
		PagesCollection.dialogPage.tapAddPictureBtn();
	}

	@When("^I press Ping button$")
	public void WhenIPressPButton() throws Throwable {
		PagesCollection.dialogPage.tapPingBtn();
	}

	/**
	 * Tap on Dialog page bottom for scrolling page to the end
	 * 
	 * @step. ^I tap Dialog page bottom$
	 * 
	 * @throws Throwable
	 * 
	 */
	@When("^I tap Dialog page bottom$")
	public void WhenITapOnDialogPageBottom() throws Throwable {
		PagesCollection.dialogPage.tapDialogPageBottomLinearLayout();
	}

	/**
	 * Tap on Play/Pause media item button
	 * 
	 * @step. ^I press PlayPause media item button$
	 * 
	 * @throws Throwable
	 * 
	 */
	@When("^I press PlayPause media item button$")
	public void WhenIPressPlayPauseButton() throws Throwable {
		PagesCollection.dialogPage.tapPlayPauseBtn();
	}
	
	/**
	 * Tap on Play/Pause button on Media Bar
	 * 
	 * @step. ^I press PlayPause on Mediabar button$
	 * 
	 * @throws Throwable
	 * 
	 */
	@When("^I press PlayPause on Mediabar button$")
	public void WhenIPressPlayPauseOnMediaBarButton() throws Throwable {
		PagesCollection.dialogPage.tapPlayPauseMediaBarBtn();
	}
	
	@When("^I press \"(.*)\" button$")
	public void WhenIPressButton(String buttonName) throws Throwable {
		switch (buttonName.toLowerCase()) {
		case "take photo":
			// Temp fix for Moto
			// PagesCollection.dialogPage.changeCamera();
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

	@When("^I select picture for dialog$")
	public void WhenISelectPicture() throws Throwable {
		PagesCollection.dialogPage.selectPhoto();
	}

	@Then("^I see Hello-Hey message (.*) with (.*) in the dialog$")
	public void ThenISeeHelloHeyMessageInTheDialog(String message, String action)
			throws Exception {
		try {
			message = usrMgr.findUserByNameOrNameAlias(message).getName();
		} catch (NoSuchUserException ex) {
			// Ignore silently
		}
		Assert.assertTrue(PagesCollection.dialogPage.isKnockText(message,
				action));
		/*
		 * Assert.assertEquals("Ping message compare", message + " " +
		 * action.trim(), PagesCollection.dialogPage.getKnockText());
		 */
	}

	@Then("^I see my message in the dialog$")
	public void ThenISeeMyMessageInTheDialog() throws Throwable {
		PagesCollection.dialogPage.waitForMessage();
		String lastMess = PagesCollection.dialogPage.getLastMessageFromDialog();
		Assert.assertTrue(lastMess.equals(message.trim()));
	}

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

	@Then("^I see (.*) added (.*) message on Dialog page$")
	public void ISeeAddedMessageOnDialogPage(String user, String contact)
			throws Throwable {
		user = usrMgr.findUserByNameOrNameAlias(user).getName();
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		String chatMessage = user + " ADDED " + contact;
		Assert.assertTrue(PagesCollection.dialogPage.isConnectMessageVisible());
		Assert.assertTrue(PagesCollection.dialogPage
				.isConnectMessageValid(chatMessage));
	}

	@Then("^I multi tap on text input again$")
	public void ThenIMultiTapOnTextInputAgain() throws Throwable {
		PagesCollection.dialogPage.multiTapOnCursorInput();
	}

	@When("^I swipe left on dialog page$")
	public void WhenISwipeLeftOnDialogPage() throws Exception {
		PagesCollection.dialogPage.swipeLeft(1000);
	}

	@When("^I swipe up on dialog page$")
	public void WhenISwipeUpOnDialogPage() throws Exception {
		if (PagesCollection.dialogPage == null) {
			PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		}
		PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.dialogPage
				.swipeUp(1000);
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
	public void WhenISwipedownOnDialogPage() throws Exception{
		if (PagesCollection.dialogPage == null) {
			PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		}
		PagesCollection.dialogPage.swipeDown(1000);
	}
	
	@When("^I navigate back from dialog page$")
	public void WhenINavigateBackFromDialogPage() throws Exception {
		PagesCollection.contactListPage = PagesCollection.dialogPage
				.navigateBack();
	}

	@Then("^I see Connect to (.*) Dialog page$")
	public void ThenIseeConnectToDialogPage(String contact) throws Exception {
		if (PagesCollection.dialogPage == null) {
			PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		}
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertEquals("connected to "+contact.toLowerCase(),
				PagesCollection.dialogPage.getConnectRequestChatLabel());
	}

	@Then("I see uploaded picture")
	public void ThenISeeChangedUserPicture() throws Exception {
		Assert.assertTrue(PagesCollection.dialogPage.dialogImageCompare());
	}

	@Then("^I see (.*) icon$")
	public void ThenIseeIcon(String iconLabel) throws Exception {
		double score = PagesCollection.dialogPage.checkPingIcon(iconLabel);
		Assert.assertTrue(
				"Overlap between two images has not enough score. Expected >= 0.75, current = "
						+ score, score >= 0.75d);
	}

	// ------- From Group Chat Page
	public static final String userRemovedMessage = "YOU REMOVED ";

	@When("^I swipe right on dialog page$")
	public void WhenISwipeRightOnGroupDialogPage() throws Throwable {
		PagesCollection.contactListPage = (ContactListPage) PagesCollection.dialogPage
				.swipeRight(500);
	}

	@Then("^I see group chat page with users (.*)$")
	public void ThenISeeGroupChatPage(String participantNameAliases)
			throws Exception {
		PagesCollection.dialogPage.isDialogVisible();
		List<String> participantNames = new ArrayList<String>();
		for (String nameAlias : CommonSteps
				.splitAliases(participantNameAliases)) {
			participantNames.add(usrMgr.findUserByNameOrNameAlias(nameAlias)
					.getName());
		}
		Assert.assertTrue(PagesCollection.dialogPage
				.isGroupChatDialogContainsNames(participantNames));
	}

	@Then("^I see message that I left chat$")
	public void ThenISeeMessageThatILeftChat() throws Throwable {
		Assert.assertTrue(PagesCollection.dialogPage
				.isMessageExists(DialogPage.I_LEFT_CHAT_MESSAGE));
	}

	@Then("^I see  message (.*) contact (.*) on group page$")
	public void ThenISeeMessageContactOnGroupPage(String message, String contact)
			throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName()
				.toUpperCase();
		Assert.assertTrue(PagesCollection.dialogPage.isMessageExists(message
				+ " " + contact));
	}

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
		double score = PagesCollection.dialogPage.checkMediaBarControlIcon(iconLabel);
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
		double score = PagesCollection.dialogPage.checkMediaControlIcon(iconLabel);
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
	 * 
	 * @throws NoSuchUserException
	 */
	@Then("^I see dialog with missed call from (.*)$")
	public void ThenISeeDialogWithMissedCallFrom(String contact) throws NoSuchUserException {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertEquals(contact + " CALLED", PagesCollection.dialogPage.getMissedCallMessage());
	}
}
