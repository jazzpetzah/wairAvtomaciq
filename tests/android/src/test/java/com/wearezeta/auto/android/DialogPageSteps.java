package com.wearezeta.auto.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

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
	public void WhenISeeDialogPage() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		PagesCollection.dialogPage.waitForCursorInputVisible();
	}

	@When("^I tap on text input$")
	public void WhenITapOnTextInput() throws Throwable {
		PagesCollection.dialogPage.tapOnCursorInput();
	}

	@When("^I type the message and send it$")
	public void WhenITypeRandomMessageAndSendIt() throws Throwable {
		message = CommonUtils.generateGUID();
		PagesCollection.dialogPage.typeMessage(message);
	}

	@When("^I input (.*) message and send it$")
	public void ITypeTheMessageAndSendIt(String myMessage) throws Throwable {
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
	public void WhenISwipeOnTextInput() throws Throwable {
		PagesCollection.dialogPage.SwipeOnCursorInput();
	}

	@When("^I swipe left on text input$")
	public void WhenISwipeLeftOnTextInput() throws Throwable {
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

	@When("^I press \"(.*)\" button$")
	public void WhenIPressButton(String buttonName) throws Throwable {
		switch (buttonName.toLowerCase()) {
		case "take photo":
			PagesCollection.dialogPage.changeCamera();
			Thread.sleep(1000);
			PagesCollection.dialogPage.takePhoto();
			break;
		case "confirm":
			PagesCollection.dialogPage.confirm();
			break;
		case "gallery":
			PagesCollection.dialogPage.openGallery();
		}
	}

	@When("^I select picture for dialog$")
	public void WhenISelectPicture() throws Throwable {
		PagesCollection.dialogPage.selectPhoto();
	}

	@Then("^I see Hello-Hey message (.*) with (.*) in the dialog$")
	public void ThenISeeHelloHeyMessageInTheDialog(String message, String action)
			throws Throwable {
		message = usrMgr.findUserByNameAlias(message).getName();
		Assert.assertEquals("Ping message compare",
				message + " " + action.trim(),
				PagesCollection.dialogPage.getKnockText());
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

	@Then("^I see (.*) added (.*) message on Dialog page$")
	public void ISeeAddedMessageOnDialogPage(String user, String contact)
			throws Throwable {
		user = usrMgr.findUserByNameAlias(user).getName();
		contact = usrMgr.findUserByNameAlias(contact).getName();
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

	@When("^I navigate back from dialog page$")
	public void WhenINavigateBackFromDialogPage() throws Exception {
		PagesCollection.contactListPage = PagesCollection.dialogPage
				.navigateBack();
	}

	@Then("^I see Connect to (.*) Dialog page$")
	public void ThenIseeConnectToDialogPage(String contact) {
		if (PagesCollection.dialogPage == null) {
			PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		}
		contact = usrMgr.findUserByNameAlias(contact).getName();
		Assert.assertEquals("connected to",
				PagesCollection.dialogPage.getConnectRequestChatLabel());
		Assert.assertEquals(contact.toLowerCase(),
				PagesCollection.dialogPage.getConnectRequestChatUserName());
	}

	@Then("I see uploaded picture")
	public void ThenISeeChangedUserPicture() throws IOException {
		Assert.assertTrue(PagesCollection.dialogPage.dialogImageCompare());
	}

	@Then("^I see (.*) icon$")
	public void ThenIseeIcon(String iconLabel) throws IOException {
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
			throws Throwable {
		PagesCollection.dialogPage.isDialogVisible();
		List<String> participantNames = new ArrayList<String>();
		for (String nameAlias : CommonSteps
				.splitAliases(participantNameAliases)) {
			participantNames.add(usrMgr.findUserByNameAlias(nameAlias)
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
		contact = usrMgr.findUserByNameAlias(contact).getName().toUpperCase();
		Assert.assertTrue(PagesCollection.dialogPage.isMessageExists(message
				+ " " + contact));
	}
}
