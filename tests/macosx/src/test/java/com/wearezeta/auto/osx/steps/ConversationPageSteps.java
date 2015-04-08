package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.osx.common.OSXExecutionContext;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.pages.common.ChoosePicturePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationPageSteps {
	private static final Logger log = ZetaLogger
			.getLog(ConversationPageSteps.class.getSimpleName());
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private String randomMessage;
	private int beforeNumberOfKnocks = 0;
	private int beforeNumberOfHotKnocks = 0;
	private int beforeNumberOfImages = 0;
	public String pingID;

	@When("I write random message")
	public void WhenIWriteRandomMessage() {
		randomMessage = CommonUtils.generateGUID();
		IWriteMessage(randomMessage);
	}

	@When("^Contact (.*) sends random message to user (.*)$")
	public void UserSendsRandomMessageToConversation(
			String msgFromUserNameAlias, String dstUserNameAlias)
			throws Exception {
		randomMessage = CommonUtils.generateGUID();
		CommonSteps.getInstance().UserSentMessageToUser(msgFromUserNameAlias,
				dstUserNameAlias, randomMessage);
	}

	@When("I write message (.*)")
	public void IWriteMessage(String message) {
		PagesCollection.conversationPage.writeNewMessage(message);
	}

	/**
	 * Verifies that the message text area is not visible when your contact is
	 * e.g. in pending state.
	 * 
	 * @step. I can not write a random message
	 * 
	 */
	@When("I can not write a random message")
	public void ICanNotWriteAMessage() {
		boolean messageTextArea = PagesCollection.conversationPage
				.isMessageTextAreaVisible();
		Assert.assertFalse(
				"This is a pending user, message area should be hidden and not possible to send any message",
				messageTextArea);
	}

	@When("I send message")
	public void WhenISendMessage() {
		PagesCollection.conversationPage.sendNewMessage();
	}

	@When("I send picture (.*)")
	public void WhenISendPicture(String imageFilename) throws Exception {
		if (beforeNumberOfImages < 0) {
			beforeNumberOfImages = PagesCollection.conversationPage
					.getNumberOfImageEntries();
		}
		PagesCollection.conversationPage.writeNewMessage("");
		PagesCollection.conversationPage.openChooseImageDialog();
		PagesCollection.choosePicturePage = new ChoosePicturePage(
				PagesCollection.conversationPage.getDriver(),
				PagesCollection.conversationPage.getWait());

		Assert.assertTrue("Choose picture page were not opened.",
				PagesCollection.choosePicturePage.isVisible());

		PagesCollection.choosePicturePage.openImage(imageFilename);
	}

	@Then("^I see HD picture (.*) in conversation with (.*)$")
	public void ThenISeeHDPictureInConversation(String filename,
			String contactNameAlias) throws Throwable {

		// fist check, if there is a picture sent
		int afterNumberOfImages = -1;

		boolean isNumberIncreased = false;
		for (int i = 0; i < 60; i++) {
			afterNumberOfImages = PagesCollection.conversationPage
					.getNumberOfImageEntries();
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

		// second check, if that picture is the expected HD picture and not just
		// any picture
		boolean retry = false;
		int retriesCount = 0;
		Exception lastException = null;
		BufferedImage pictureAssetFromConv = null;
		ClientUser selfUser = usrMgr.getSelfUserOrThrowError();
		ClientUser contactUser = usrMgr
				.findUserByNameOrNameAlias(contactNameAlias);
		do {
			retry = false;
			try {
				pictureAssetFromConv = BackendAPIWrappers
						.getLastPictureAssetFromConversation(selfUser,
								contactUser);
			} catch (BackendRequestException e) {
				retry = true;
				retriesCount++;
				lastException = e;
				try {
					Thread.sleep(500);
				} catch (InterruptedException ie) {
				}
			}
		} while (retry == true && retriesCount < 5);

		BufferedImage origSentPicture = ImageUtil
				.readImageFromFile(OSXExecutionContext.userDocuments + "/"
						+ filename);

		Assert.assertNotNull(
				"Can't get picture asset from conversation via backend.\n"
						+ (lastException == null ? "" : lastException
								.getMessage()), pictureAssetFromConv);

		double score = ImageUtil.getOverlapScore(pictureAssetFromConv,
				origSentPicture,
				ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= 0.98, current = "
						+ score, score >= 0.98d);
	}

	@Then("I see random message in conversation$")
	public void ThenISeeRandomMessageInConversation() throws Exception {
		Assert.assertTrue(PagesCollection.conversationPage
				.isMessageSent(randomMessage));
	}

	@Then("I see picture in conversation$")
	public void ThenISeePictureInConversation() throws Exception {
		int afterNumberOfImages = -1;

		boolean isNumberIncreased = false;
		for (int i = 0; i < 10; i++) {
			afterNumberOfImages = PagesCollection.conversationPage
					.getNumberOfImageEntries();
			if (afterNumberOfImages == beforeNumberOfImages + 1) {
				isNumberIncreased = true;
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		if (!isNumberIncreased) {
			log.debug("New picture was not found. Check source: "
					+ PagesCollection.conversationPage.getPageSource());
		}

		Assert.assertTrue("Incorrect images count: before - "
				+ beforeNumberOfImages + ", after - " + afterNumberOfImages,
				isNumberIncreased);
	}

	@When("I scroll down to conversation")
	public void IScrollDownToConversation() throws Exception {
		PagesCollection.conversationPage.scrollDownToLastMessage();
	}

	private void calcNumberOfPings(String user) {
		if (beforeNumberOfKnocks < 0) {
			beforeNumberOfKnocks = PagesCollection.conversationPage
					.getNumberOfYouPingedMessages(String.format(
							OSXLocators.xpathOtherPingedMessage, user));
		}
		if (beforeNumberOfHotKnocks < 0) {
			beforeNumberOfHotKnocks = PagesCollection.conversationPage
					.getNumberOfYouPingedMessages(String.format(
							OSXLocators.xpathOtherPingedAgainMessage, user));
		}
	}

	@When("I ping user")
	public void WhenIPingUser() {
		calcNumberOfPings("YOU");
		PagesCollection.conversationPage.ping();
	}

	@When("I ping again user")
	public void IPingAgainUser() {
		calcNumberOfPings("YOU");
		PagesCollection.conversationPage.pingAgain();
	}

	@When("^User (.*) pings in chat (.*)$")
	public void WhenUserPingsInChat(String contactNameAlias, String conversation)
			throws Throwable {
		ClientUser yourСontact = usrMgr
				.findUserByNameOrNameAlias(contactNameAlias);
		pingID = BackendAPIWrappers.sendPingToConversation(yourСontact,
				conversation);
		Thread.sleep(1000);
	}

	@Then("^I see User (.*) Pinged action in the conversation$")
	public void ThenISeeUserPingedActionInTheConversation(String user)
			throws Throwable {
		String username = usrMgr.findUserByNameOrNameAlias(user).getName();
		String expectedPingMessage = username.toUpperCase()
				+ OSXLocators.USER_PINGED_MESSAGE;
		String dialogLastMessage = username.toUpperCase() + " PINGED";

		boolean isNumberIncreased = false;
		int afterNumberOfKnocks = -1;
		int afterNumberOfHotKnocks = -1;

		if (dialogLastMessage.equals(expectedPingMessage)) {
			for (int i = 0; i < 3; i++) {
				afterNumberOfKnocks = PagesCollection.conversationPage
						.getNumberOfYouPingedMessages(OSXLocators.xpathOtherPingedMessage);
				if (afterNumberOfKnocks == beforeNumberOfKnocks + 1) {
					isNumberIncreased = true;
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}

			Assert.assertTrue(
					"Incorrect messages count: before - "
							+ beforeNumberOfKnocks + ", after - "
							+ afterNumberOfKnocks, isNumberIncreased);
		} else {
			for (int i = 0; i < 3; i++) {
				afterNumberOfHotKnocks = PagesCollection.conversationPage
						.getNumberOfYouPingedMessages(OSXLocators.xpathOtherPingedAgainMessage);
				if (afterNumberOfHotKnocks == beforeNumberOfHotKnocks + 1) {
					isNumberIncreased = true;
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}

			Assert.assertTrue("Incorrect messages count: before - "
					+ beforeNumberOfHotKnocks + ", after - "
					+ afterNumberOfHotKnocks, isNumberIncreased);
		}
	}

	@When("^User (.*) pings again in chat (.*)$")
	public void WhenUserPingsAgainInChat(String contactNameAlias,
			String conversation) throws Throwable {
		ClientUser yourСontact = usrMgr
				.findUserByNameOrNameAlias(contactNameAlias);
		BackendAPIWrappers.sendHotPingToConversation(yourСontact, conversation,
				pingID);
		Thread.sleep(1000);
	}

	private void verifyPingedMessage(String user) throws InterruptedException {
		boolean isNumberIncreased = false;
		int afterNumberOfKnocks = -1;
		for (int i = 0; i < 3; i++) {
			afterNumberOfKnocks = PagesCollection.conversationPage
					.getNumberOfYouPingedMessages(String.format(
							OSXLocators.xpathOtherPingedMessage, user));
			if (afterNumberOfKnocks == beforeNumberOfKnocks + 1) {
				isNumberIncreased = true;
				break;
			}
			Thread.sleep(1000);
		}

		Assert.assertTrue("Incorrect messages count: before - "
				+ beforeNumberOfKnocks + ", after - " + afterNumberOfKnocks,
				isNumberIncreased);
	}

	private void verifyPingedAgainMessage(String user)
			throws InterruptedException {
		boolean isNumberIncreased = false;
		int afterNumberOfHotKnocks = -1;
		for (int i = 0; i < 3; i++) {
			afterNumberOfHotKnocks = PagesCollection.conversationPage
					.getNumberOfYouPingedMessages(String.format(
							OSXLocators.xpathOtherPingedAgainMessage, user));
			if (afterNumberOfHotKnocks == beforeNumberOfHotKnocks + 1) {
				isNumberIncreased = true;
				break;
			}
			Thread.sleep(1000);
		}

		Assert.assertTrue("Incorrect messages count: before - "
				+ beforeNumberOfHotKnocks + ", after - "
				+ afterNumberOfHotKnocks, isNumberIncreased);
	}

	private void verifyMsgExistsInConversationView(String msg)
			throws InterruptedException {
		msg = usrMgr.replaceAliasesOccurences(msg, FindBy.NAME_ALIAS);
		msg = msg.toUpperCase();
		Assert.assertTrue(String.format("Message '%s' not found.", msg),
				PagesCollection.conversationPage.isMessageExist(msg));
	}

	@Then("I see message (.*) in conversation$")
	public void ThenISeeMessageInConversation(String message)
			throws InterruptedException {
		if (message.equals(OSXLocators.YOU_PINGED_MESSAGE)) {
			verifyPingedMessage("YOU");
		} else if (message.equals(OSXLocators.YOU_PINGED_AGAIN_MESSAGE)) {
			verifyPingedAgainMessage("YOU");
		} else if (message.contains(OSXLocators.USER_PINGED_MESSAGE)) {
			message = usrMgr.replaceAliasesOccurences(message,
					FindBy.NAME_ALIAS);
			verifyPingedMessage(message.split(" ")[0].toUpperCase());
		} else {
			verifyMsgExistsInConversationView(message);
		}
	}

	@When("I open People Picker from conversation")
	public void WhenIOpenPeoplePickerFromConversation() throws Exception {
		IScrollDownToConversation();
		PagesCollection.conversationPage.writeNewMessage("");
		PagesCollection.conversationPage.openConversationPeoplePicker();
		PagesCollection.conversationInfoPage = new ConversationInfoPage(
				PagesCollection.conversationPage.getDriver(),
				PagesCollection.conversationPage.getWait());
		if (!PagesCollection.conversationInfoPage.isPeoplePopoverDisplayed()) {
			PagesCollection.conversationPage.openConversationPeoplePicker();
		}
		PagesCollection.peoplePickerPage = PagesCollection.conversationInfoPage
				.openPeoplePicker();
	}

	@When("I open Conversation info")
	public void WhenIOpenConversationInfo() throws Exception {
		PagesCollection.conversationPage.writeNewMessage("");
		PagesCollection.conversationPage.openConversationPeoplePicker();
		if (PagesCollection.conversationInfoPage == null) {
			PagesCollection.conversationInfoPage = new ConversationInfoPage(
					PagesCollection.conversationPage.getDriver(),
					PagesCollection.conversationPage.getWait());
		}
	}

	@Given("^I create group chat with (.*) and (.*)$")
	public void WhenICreateGroupChatWithUser1AndUser2(String user1, String user2)
			throws Exception {
		try {
			user1 = usrMgr.findUserByNameOrNameAlias(user1).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		try {
			user2 = usrMgr.findUserByNameOrNameAlias(user2).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		ContactListPageSteps clSteps = new ContactListPageSteps();
		PeoplePickerPageSteps ppSteps = new PeoplePickerPageSteps();
		clSteps.GivenIOpenConversationWith(user1);
		this.WhenIOpenPeoplePickerFromConversation();
		ppSteps.ISearchForUser(user2);
		ppSteps.ISeeUserFromSearchResults(user2);
		ppSteps.IAddUserFromSearchResults(user2);
	}

	@Given("^I post media link (.*)$")
	public void WhenIPostMediaLink(String link) throws Throwable {
		PagesCollection.conversationPage.writeNewMessage(link);
	}

	@Then("^I see media link (.*) and media in dialog$")
	public void ThenISeeMediaLinkAndMediaInDialog(String link) throws Throwable {
		boolean isLinkAppears = PagesCollection.conversationPage
				.isMediaLinkAppearsInDialog(link);
		boolean mediaVisible = PagesCollection.conversationPage
				.isSoundCloudContainerVisible();
		Assert.assertTrue("SoundCloud Container is missing in dialog",
				isLinkAppears);
		Assert.assertTrue("SoundCloud Container is missing in dialog",
				mediaVisible);
	}

	@When("^I tap SoundCloud link$")
	public void WhenITapSoundCloudLink() throws Throwable {
		PagesCollection.conversationPage.tapOnSoundCloudMessage();
	}

	@Then("^I see the embedded media is playing$")
	public void ThenISeeTheEmbeddedMediaIsPlaying() throws Throwable {
		verifySoundCloudButtonState(OSXLocators.SOUNDCLOUD_BUTTON_STATE_PAUSE);
	}

	@Given("^I post messages and media link (.*)$")
	public void WhenIPostMessagesAndMediaLink(String link) throws Throwable {
		final int RANDOM_MESSAGE_COUNT = 20;
		for (int i = 0; i <= RANDOM_MESSAGE_COUNT; i++) {
			WhenIWriteRandomMessage();
			WhenISendMessage();
			Thread.sleep(500);
		}
		PagesCollection.conversationPage.writeNewMessage(link);
		WhenISendMessage();
	}

	@When("^I scroll media out of sight till media bar appears$")
	public void WhenIScrollMediaOutOfSightUntilMediaBarAppears()
			throws Throwable {
		Thread.sleep(2000);
		PagesCollection.conversationPage.scrollDownTillMediaBarAppears();
		Assert.assertTrue("Media bar doesn't appear",
				PagesCollection.conversationPage.isMediaBarVisible());
	}

	@When("^I pause playing media in media bar$")
	public void WhenIPausePlayingTheMediaInMediaBar() throws Throwable {
		PagesCollection.conversationPage.pressPlayPauseButton();
	}

	@Then("^The playing media is paused$")
	public void ThenThePlayingMediaIsPaused() throws Throwable {
		verifySoundCloudButtonState(OSXLocators.SOUNDCLOUD_BUTTON_STATE_PLAY);
	}

	@When("^I press play in media bar$")
	public void WhenIPressPlayInMediaBar() throws Throwable {
		PagesCollection.conversationPage.pressPlayPauseButton();
	}

	@Then("^The media is playing$")
	public void ThenTheMediaIsPlaying() throws Throwable {
		verifySoundCloudButtonState(OSXLocators.SOUNDCLOUD_BUTTON_STATE_PAUSE);
	}

	@When("^I stop media in media bar$")
	public void WhenIStopMediaInMediaBar() throws Throwable {
		PagesCollection.conversationPage.pressStopButton();
	}

	@Then("^The media stops playing$")
	public void ThenTheMediaStoppsPlaying() throws Throwable {
		verifySoundCloudButtonState(OSXLocators.SOUNDCLOUD_BUTTON_STATE_PLAY);
	}

	private void verifySoundCloudButtonState(String expectedState) {
		String actualState = "";
		long startDate = new Date().getTime();
		long endDate;
		for (int i = 0; i < 3; i++) {
			actualState = PagesCollection.conversationPage
					.getSoundCloudButtonState();
			if (actualState.equals(expectedState))
				break;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			endDate = new Date().getTime();
			log.debug("Try #" + (i + 1)
					+ " finished with incorrect result after "
					+ (endDate - startDate) + "ms from start");
		}
		Assert.assertEquals("SoundCloud button state " + actualState
				+ " differs from expected " + expectedState, expectedState,
				actualState);
	}

	@When("^I press the media bar title$")
	public void WhenIPressTheMediaBarTitle() {
		PagesCollection.conversationPage.pressMediaTitle();
	}

	@Then("^I see conversation name (.*) in conversation$")
	public void ISeeConversationNameInConversation(String name) {
		if (name.equals(OSXLocators.RANDOM_KEYWORD)) {
			name = PagesCollection.conversationInfoPage
					.getCurrentConversationName();
		}
		String result = PagesCollection.conversationPage
				.getLastConversationNameChangeMessage();
		// workaround for bug with irrelevant characters in conversation name
		// after renaming it
		Assert.assertTrue("New conversation name '" + result
				+ "' does not equal to expected '" + name + "'",
				result.startsWith(name));
	}

	@When("^I wait (.*) seconds till playback finishes$")
	public void WhenIWaitTillPlaybackFinishes(int time)
			throws InterruptedException {
		Thread.sleep(time * 1000);
		String currentState = PagesCollection.conversationPage
				.getSoundCloudButtonState();
		if (currentState.equals("Pause")) {
			// if song still playing due to some lags, wait once more
			log.debug("Seems like audio track still not finished to play. Waiting for finish once more. "
					+ "Current playback time: "
					+ PagesCollection.conversationPage.getCurrentPlaybackTime());
			Thread.sleep(time * 1000);
		}
		Assert.assertEquals("Current state \"" + currentState
				+ "\" is not equal to expected \"Play\"", "Play", currentState);
	}

	@Then("^I see media bar disappears$")
	public void ThenISeeMediaBarDisappears() {
		boolean isVisible = PagesCollection.conversationPage
				.isMediaBarVisible();
		Assert.assertFalse("Media bar is still visible", isVisible);
	}

	@When("I count number of images in conversation")
	public void ICountNumberOfImagesInConversation() throws Exception {
		beforeNumberOfImages = PagesCollection.conversationPage
				.getNumberOfImageEntries();
	}

	@When("I open Documents folder in Finder")
	public void IOpenDocumentsFolderInFinder() {
		PagesCollection.conversationPage.openFinder();
	}

	@When("I drag picture (.*) to conversation")
	public void IDragPictureToConversation(String picture) throws Exception {
		PagesCollection.conversationPage.dragPictureToConversation(picture);
	}
}
