package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationInfoPage;
import com.wearezeta.auto.osx.pages.ConversationPage;
import com.wearezeta.auto.osx.pages.OSXPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConversationPageSteps {
	private static final Logger log = ZetaLogger.getLog(ConversationPageSteps.class.getSimpleName());
	
	 private String randomMessage;
	 private int beforeNumberOfKnocks = -1;
	 private int beforeNumberOfHotKnocks = -1;
	 private int beforeNumberOfImages = -1;
	 public String pingID;
	 
	 @When("I write random message")
	 public void WhenIWriteRandomMessage() {	
		 randomMessage = CommonUtils.generateGUID();
		 IWriteMessage(randomMessage);
	 }
	 
	 @When("I write message (.*)")
	 public void IWriteMessage(String message) {
		 CommonSteps.senderPages.getConversationPage().writeNewMessage(message);
	 }
	 
	 @When("I send message")
	 public void WhenISendMessage() {
		 CommonSteps.senderPages.getConversationPage().sendNewMessage();
	 }
	 
	 @When("I send picture (.*)")
	 public void WhenISendPicture(String imageFilename) throws MalformedURLException, IOException {
		 if (beforeNumberOfImages < 0) {
			 beforeNumberOfImages =
				 CommonSteps.senderPages.getConversationPage()
				 		.getNumberOfImageEntries();
		 }
		 CommonSteps.senderPages.getConversationPage().writeNewMessage("");
		 CommonSteps.senderPages.getConversationPage().openChooseImageDialog();
		 CommonSteps.senderPages.setChoosePicturePage(new ChoosePicturePage(
				 CommonUtils.getOsxAppiumUrlFromConfig(ContactListPage.class),
				 CommonUtils.getOsxApplicationPathFromConfig(ContactListPage.class)
				 ));
		 
		 ChoosePicturePage choosePicturePage = CommonSteps.senderPages.getChoosePicturePage();
		 Assert.assertTrue("Choose picture page were not opened.", choosePicturePage.isVisible());
		 
		 choosePicturePage.openImage(imageFilename);
	 }

	 @Then("^I see HD picture (.*) in conversation$")
	 public void ThenISeeHDPictureInConversation(String filename) throws Throwable {
		 
		 //fist check, if there is a picture sent
		 int afterNumberOfImages = -1;
		 
		 boolean isNumberIncreased = false;
		 for (int i = 0; i < 60; i++) {
			 afterNumberOfImages = CommonSteps.senderPages.getConversationPage().getNumberOfImageEntries();
			 if (afterNumberOfImages == beforeNumberOfImages + 1) {
				 isNumberIncreased = true;
				 break;
			 }
			 try { Thread.sleep(1000); } catch (InterruptedException e) { }
		 }
		 
		 Assert.assertTrue("Incorrect images count: before - "
				 + beforeNumberOfImages + ", after - " + afterNumberOfImages, isNumberIncreased);
		 
		 //second check, if that picture is the expected HD picture and not just any picture
		 boolean retry = false;
		 int retriesCount = 0;
		 Exception lastException = null;
		 BufferedImage pictureAssetFromConv = null;
		 do {
			 retry = false;
			 try {
				 pictureAssetFromConv = BackEndREST.getLastPictureAssetFromConversation(
						 CommonUtils.yourUsers.get(0),
						 CommonUtils.contacts.get(0));
			 } catch (BackendRequestException e) {
				 retry = true;
				 retriesCount++;
				 lastException = e;
				 try { Thread.sleep(500); } catch (InterruptedException ie) {}
			 }
		 } while (retry == true && retriesCount < 5);
		 
		 BufferedImage origSentPicture = ImageUtil.readImageFromFile(OSXPage.imagesPath + filename);
		 
		 Assert.assertNotNull("Can't get picture asset from conversation via backend.\n" + (lastException==null?"":lastException.getMessage()), pictureAssetFromConv);
		 
		 double score = ImageUtil.getOverlapScore(pictureAssetFromConv, origSentPicture, ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
		 Assert.assertTrue(
					"Overlap between two images has no enough score. Expected >= 0.98, current = " + score,
					score >= 0.98d);
	 }
	 
	 @Then("I see random message in conversation$")
	 public void ThenISeeRandomMessageInConversation() {
		 Assert.assertTrue(CommonSteps.senderPages.getConversationPage().isMessageSent(randomMessage));
	 }
	 
	 @Then("I see picture in conversation$")
	 public void ThenISeePictureInConversation() {
		 int afterNumberOfImages = -1;
		 
		 boolean isNumberIncreased = false;
		 for (int i = 0; i < 3; i++) {
			 afterNumberOfImages = CommonSteps.senderPages.getConversationPage().getNumberOfImageEntries();
			 if (afterNumberOfImages == beforeNumberOfImages + 1) {
				 isNumberIncreased = true;
				 break;
			 }
			 try { Thread.sleep(1000); } catch (InterruptedException e) { }
		 }
		 
		 Assert.assertTrue("Incorrect images count: before - "
				 + beforeNumberOfImages + ", after - " + afterNumberOfImages, isNumberIncreased);
	 }
	 
	 @When("I scroll down to conversation")
	 public void IScrollDownToConversation() throws Exception {
			CommonSteps.senderPages.getConversationPage().scrollDownToLastMessage();
	 }
	 
	 private void calcNumberOfPings() {
		 if (beforeNumberOfKnocks < 0) {
			 beforeNumberOfKnocks =
				CommonSteps.senderPages.getConversationPage()
				 		.getNumberOfYouPingedMessages(OSXLocators.xpathYouPingedMessage);
		 }
		 if (beforeNumberOfHotKnocks < 0) {
			 beforeNumberOfHotKnocks =
				CommonSteps.senderPages.getConversationPage()
					 	.getNumberOfYouPingedMessages(OSXLocators.xpathYouPingedAgainMessage);
		 }
	 }
	 
	 @When("I ping user")
	 public void WhenIPingUser() {
		 calcNumberOfPings();
		 CommonSteps.senderPages.getConversationPage().ping();
	 }
	 
	 @When("I ping again user")
	 public void IPingAgainUser() {
		 calcNumberOfPings();
		 CommonSteps.senderPages.getConversationPage().pingAgain();
	 }
	 
	 @When("^User (.*) pings in chat (.*)$")
	 public void WhenUserPingsInChat(String contact, String conversation) throws Throwable {
		 ClientUser yourСontact = null;
			contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
			for (ClientUser user : CommonUtils.contacts) {
				if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
					yourСontact = user;
				}
			}
			yourСontact = BackEndREST.loginByUser(yourСontact);
			pingID = BackEndREST.sendPingToConversation(yourСontact, conversation);
			Thread.sleep(1000);
	 }

	 @Then("^I see User (.*) Pinged action in the conversation$")
	 public void ThenISeeUserPingedActionInTheConversation(String user) throws Throwable {
		 String username = CommonUtils.retrieveRealUserContactPasswordValue(user);
		 String expectedPingMessage = username.toUpperCase() + OSXLocators.USER_PINGED_MESSAGE;
		 String dialogLastMessage = username.toUpperCase() + " PINGED";
		 
		 boolean isNumberIncreased = false;
		 int afterNumberOfKnocks = -1;
		 int afterNumberOfHotKnocks = -1;

		 if (dialogLastMessage.equals(expectedPingMessage)) {
			 for (int i = 0; i < 3; i++) {
				 afterNumberOfKnocks = CommonSteps.senderPages.getConversationPage().getNumberOfYouPingedMessages(OSXLocators.xpathOtherPingedMessage);
				 if (afterNumberOfKnocks == beforeNumberOfKnocks + 1) {
					 isNumberIncreased = true;
					 break;
				 }
				 try { Thread.sleep(1000); } catch (InterruptedException e) { }
			 }
			 
			 Assert.assertTrue("Incorrect messages count: before - "
					 + beforeNumberOfKnocks + ", after - " + afterNumberOfKnocks, isNumberIncreased);
		 }
		 else {
			 for (int i = 0; i < 3; i++) {
				 afterNumberOfHotKnocks = CommonSteps.senderPages.getConversationPage().getNumberOfYouPingedMessages(OSXLocators.xpathOtherPingedAgainMessage);
				 if (afterNumberOfHotKnocks == beforeNumberOfHotKnocks + 1) {
					 isNumberIncreased = true;
					 break;
				 }
				 try { Thread.sleep(1000); } catch (InterruptedException e) { }
			 }
			 
			 Assert.assertTrue("Incorrect messages count: before - "
					 + beforeNumberOfHotKnocks + ", after - " + afterNumberOfHotKnocks, isNumberIncreased);
		 	}
		 }

	 @When("^User (.*) pings again in chat (.*)$")
	 public void WhenUserPingsAgainInChat(String contact, String conversation) throws Throwable {
		 ClientUser yourСontact = null;
			contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
			for (ClientUser user : CommonUtils.contacts) {
				if (user.getName().toLowerCase().equals(contact.toLowerCase())) {
					yourСontact = user;
				}
			}
			yourСontact = BackEndREST.loginByUser(yourСontact);
			BackEndREST.sendHotPingToConversation(yourСontact, conversation,pingID);
			Thread.sleep(1000);
	 }
	 
	 @Then("I see message (.*) in conversation$")
	 public void ThenISeeMessageInConversation(String message) throws InterruptedException {
		 if (message.equals(OSXLocators.YOU_PINGED_MESSAGE)) {
			 boolean isNumberIncreased = false;
			 int afterNumberOfKnocks = -1;
			 for (int i = 0; i < 3; i++) {
				 afterNumberOfKnocks = CommonSteps.senderPages.getConversationPage().getNumberOfYouPingedMessages(OSXLocators.xpathYouPingedMessage);
				 if (afterNumberOfKnocks == beforeNumberOfKnocks + 1) {
					 isNumberIncreased = true;
					 break;
				 }
				 try { Thread.sleep(1000); } catch (InterruptedException e) { }
			 }
			 
			 Assert.assertTrue("Incorrect messages count: before - "
					 + beforeNumberOfKnocks + ", after - " + afterNumberOfKnocks, isNumberIncreased);
			 
		} else if (message.equals(OSXLocators.YOU_PINGED_AGAIN_MESSAGE)) {
			 boolean isNumberIncreased = false;
			 int afterNumberOfHotKnocks = -1;
			 for (int i = 0; i < 3; i++) {
				 afterNumberOfHotKnocks = CommonSteps.senderPages.getConversationPage().getNumberOfYouPingedMessages(OSXLocators.xpathYouPingedAgainMessage);
				 if (afterNumberOfHotKnocks == beforeNumberOfHotKnocks + 1) {
					 isNumberIncreased = true;
					 break;
				 }
				 try { Thread.sleep(1000); } catch (InterruptedException e) { }
			 }
			 
			 Assert.assertTrue("Incorrect messages count: before - "
					 + beforeNumberOfHotKnocks + ", after - " + afterNumberOfHotKnocks, isNumberIncreased);
			 
		} else if (message.contains(OSXLocators.YOU_ADDED_MESSAGE)) {
			message = CommonUtils.retrieveRealUserContactPasswordValue(message);
			message = message.toUpperCase();
			Assert.assertTrue("User was not added to conversation. Message " + message + " not found.", 
					CommonSteps.senderPages.getConversationPage().isMessageExist(message));
	 	} else if (message.contains(OSXLocators.YOU_STARTED_CONVERSATION_MESSAGE)) {
			message = CommonUtils.retrieveRealUserContactPasswordValue(message);
			message = message.toUpperCase();
			Assert.assertTrue("User was not added to conversation. Message " + message + " not found.", 
					CommonSteps.senderPages.getConversationPage().isMessageExist(message));
	 	} else if (message.contains(OSXLocators.YOU_REMOVED_MESSAGE)) {
	 		message = CommonUtils.retrieveRealUserContactPasswordValue(message);
			message = message.toUpperCase();
			Assert.assertTrue("User was not removed from conversation. Message " + message + " not found.", 
					CommonSteps.senderPages.getConversationPage().isMessageExist(message));
	 	} else if (message.contains(OSXLocators.CONNECTED_TO_MESSAGE)) {
	 		message = CommonUtils.retrieveRealUserContactPasswordValue(message).toUpperCase();
			Assert.assertTrue("Connected to message does not appear in conversation.", 
					CommonSteps.senderPages.getConversationPage().isMessageExist(message));
	 	} else {
	 		String updatedMessage = CommonUtils.retrieveRealUserContactPasswordValue(message);
	 		if (!updatedMessage.equals(message)) message = updatedMessage.toUpperCase();
			Assert.assertTrue(CommonSteps.senderPages.getConversationPage().isMessageExist(message));
		}
	 }
	 
	 @When("I open People Picker from conversation")
	 public void WhenIOpenPeoplePickerFromConversation() throws Exception {
		 IScrollDownToConversation();
		 ConversationPage conversationPage = CommonSteps.senderPages.getConversationPage(); 
		 conversationPage.writeNewMessage("");
		 conversationPage.openConversationPeoplePicker();
		 CommonSteps.senderPages.setConversationInfoPage(new ConversationInfoPage(
				 CommonUtils.getOsxAppiumUrlFromConfig(ConversationInfoPage.class),
				 CommonUtils.getOsxApplicationPathFromConfig(ConversationInfoPage.class)
				 ));
		 ConversationInfoPage conversationPeople = CommonSteps.senderPages.getConversationInfoPage();
		 if (!conversationPeople.isPeoplePopoverDisplayed()) {
			 conversationPage.openConversationPeoplePicker();
		 }
		 CommonSteps.senderPages.setPeoplePickerPage(conversationPeople.openPeoplePicker());
	 }
	 
	 @When("I open Conversation info")
	 public void WhenIOpenConversationInfo() throws MalformedURLException, IOException {
		 ConversationPage conversationPage = CommonSteps.senderPages.getConversationPage();
		 conversationPage.writeNewMessage("");
		 conversationPage.openConversationPeoplePicker();
		 if (CommonSteps.senderPages.getConversationInfoPage() == null) {
			 CommonSteps.senderPages.setConversationInfoPage(new ConversationInfoPage(
					 CommonUtils.getOsxAppiumUrlFromConfig(ConversationInfoPage.class),
					 CommonUtils.getOsxApplicationPathFromConfig(ConversationInfoPage.class)
					 ));
		 }
	 }
	 
	 @Given("^I create group chat with (.*) and (.*)$")
	 public void WhenICreateGroupChatWithUser1AndUser2(String user1, String user2) throws Exception {
		 user1 = CommonUtils.retrieveRealUserContactPasswordValue(user1);
		 user2 = CommonUtils.retrieveRealUserContactPasswordValue(user2);
		 ContactListPageSteps clSteps = new ContactListPageSteps();
		 PeoplePickerPageSteps ppSteps = new PeoplePickerPageSteps();
		 clSteps.GivenIOpenConversationWith(user1);
		 this.WhenIOpenPeoplePickerFromConversation();
		 ppSteps.WhenISearchForUser(user2);
		 ppSteps.WhenISeeUserFromSearchResults(user2);
		 ppSteps.WhenIAddUserFromSearchResults(user2);
	 }
	 
	 @Given("^I post media link (.*)$")
	 public void WhenIPostMediaLink(String link) throws Throwable { 
		 CommonSteps.senderPages.getConversationPage().writeNewMessage(link);
	 }

	 @Then("^I see media link (.*) and media in dialog$")
	 public void ThenISeeMediaLinkAndMediaInDialog(String link) throws Throwable {
		 ConversationPage page = CommonSteps.senderPages.getConversationPage();
		boolean isLinkAppears = page.isMediaLinkAppearsInDialog(link);
		boolean mediaVisible = page.isSoundCloudContainerVisible();
		Assert.assertTrue("SoundCloud Container is missing in dialog", isLinkAppears);  
		Assert.assertTrue("SoundCloud Container is missing in dialog", mediaVisible);  
	 }

	 @When("^I tap SoundCloud link$")
	 public void WhenITapSoundCloudLink() throws Throwable {
		 CommonSteps.senderPages.getConversationPage().tapOnSoundCloudMessage(); 
	 }

	 @Then("^I see the embedded media is playing$")
	 public void ThenISeeTheEmbeddedMediaIsPlaying() throws Throwable {
		 verifySoundCloudButtonState(OSXLocators.SOUNDCLOUD_BUTTON_STATE_PAUSE);
	 }
	 
	 @Given("^I post messages and media link (.*)$")
	 public void WhenIPostMessagesAndMediaLink(String link) throws Throwable { 
		 final int RANDOM_MESSAGE_COUNT = 20;
		 for (int i = 0; i <= RANDOM_MESSAGE_COUNT; i++){
			 WhenIWriteRandomMessage();
			 WhenISendMessage();
			 Thread.sleep(500);
		 }
		 CommonSteps.senderPages.getConversationPage().writeNewMessage(link);
		 WhenISendMessage();
	 }
	 
	 @When("^I scroll media out of sight till media bar appears$")
	 public void WhenIScrollMediaOutOfSightUntilMediaBarAppears() throws Throwable {
		 ConversationPage conversationPage = 
				 CommonSteps.senderPages.getConversationPage();
		 conversationPage.scrollDownTillMediaBarAppears();
		 Assert.assertTrue("Media bar doesn't appear", conversationPage.isMediaBarVisible());
	 }

	 @When("^I pause playing media in media bar$")
	 public void WhenIPausePlayingTheMediaInMediaBar() throws Throwable {
	     CommonSteps.senderPages.getConversationPage().pressPlayPauseButton();   
	 }

	 @Then("^The playing media is paused$")
	 public void ThenThePlayingMediaIsPaused() throws Throwable {
		 verifySoundCloudButtonState(OSXLocators.SOUNDCLOUD_BUTTON_STATE_PLAY);    
	 }

	 @When("^I press play in media bar$")
	 public void WhenIPressPlayInMediaBar() throws Throwable {
		 CommonSteps.senderPages.getConversationPage().pressPlayPauseButton();
	 }

	 @Then("^The media is playing$")
	 public void ThenTheMediaIsPlaying() throws Throwable {
		 verifySoundCloudButtonState(OSXLocators.SOUNDCLOUD_BUTTON_STATE_PAUSE);
	 }

	 @When("^I stop media in media bar$")
	 public void WhenIStopMediaInMediaBar() throws Throwable {
		 CommonSteps.senderPages.getConversationPage().pressStopButton();
	 }

	 @Then("^The media stops playing$")
	 public void ThenTheMediaStoppsPlaying() throws Throwable {
		 verifySoundCloudButtonState(OSXLocators.SOUNDCLOUD_BUTTON_STATE_PLAY);
	 }
	 
	 private void verifySoundCloudButtonState(String expectedState) {
		 String actualState = "";
		 for (int i = 0; i < 3; i++) {
		 	actualState = CommonSteps.senderPages.getConversationPage().getSoundCloudButtonState();
		 	if (actualState.equals(expectedState)) break;
		 	try { Thread.sleep(1000); } catch (InterruptedException e) { }
		 }
		 Assert.assertEquals(
				 "SoundCloud button state "
						 + actualState
						 + " differs from expected " + expectedState,
				expectedState, actualState);
	 }
	 
	 @When("^I press the media bar title$")
	 public void WhenIPressTheMediaBarTitle(){
		 CommonSteps.senderPages.getConversationPage().pressMediaTitle();
	 }
	 
	 @Then("^I see conversation name (.*) in conversation$")
	 public void ISeeConversationNameInConversation(String name) {
		 if (name.equals(OSXLocators.RANDOM_KEYWORD)) {
			 name = CommonSteps.senderPages.getConversationInfoPage().getCurrentConversationName();
		 }
		 String result = CommonSteps.senderPages.getConversationPage().getLastConversationNameChangeMessage();
		//workaround for bug with irrelevant characters in conversation name after renaming it
		 Assert.assertTrue(
				 "New conversation name '" + result + "' does not equal to expected '" + name + "'",
				 result.startsWith(name));
	 }
	 
	 @When("^I wait (.*) seconds till playback finishes$")
	 public void WhenIWaitTillPlaybackFinishes(int time) throws InterruptedException{
		Thread.sleep(time*1000);
	    String currentState = CommonSteps.senderPages.getConversationPage().getSoundCloudButtonState();
	    if (currentState.equals("Pause")) {
	    	//if song still playing due to some lags, wait once more
	    	log.debug("Seems like audio track still not finished to play. Waiting for finish once more. "
	    			+ "Current playback time: "
	    			+ CommonSteps.senderPages.getConversationPage().getCurrentPlaybackTime());
	    	Thread.sleep(time*1000);
	    }
	    Assert.assertEquals(
	    		"Current state \"" + currentState + "\" is not equal to expected \"Play\"",
	    		"Play", currentState); 
	 }

	 @Then("^I see media bar disappears$")
	 public void ThenISeeMediaBarDisappears(){
		 boolean isVisible = CommonSteps.senderPages.getConversationPage().isMediaBarVisible();
		 Assert.assertFalse("Media bar is still visible", isVisible);
	 }
	 
	 @When("I count number of images in conversation")
	 public void ICountNumberOfImagesInConversation() {
		 beforeNumberOfImages =
				 CommonSteps.senderPages.getConversationPage()
				 		.getNumberOfImageEntries();
	 }
	 
	 @When("I open Documents folder in Finder")
	 public void IOpenDocumentsFolderInFinder() {
		 CommonSteps.senderPages.getConversationPage().openFinder();
	 }
	
	 @When("I drag picture (.*) to conversation")
	 public void IDragPictureToConversation(String picture) throws IOException {
		 CommonSteps.senderPages.getConversationPage().dragPictureToConversation(picture);
	 }
}
