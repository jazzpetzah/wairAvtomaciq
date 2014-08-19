package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.ImageFullScreenPage;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.CameraRollPage;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.VideoPlayerPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DialogPageSteps {
	
	private String message;
	private String longMessage = "Lorem ipsum dolor sit amet, \n consectetur adipisicing elit,\n"
								+ "sed do eiusmod tempor \n incididunt ut labore \n et dolore magna aliqua.\n"
								+ " Ut enim ad minim veniam,\n quis nostrud \n"
								+ "exercitation ullamco laboris \n"
								+ "nisi ut aliquip ex \n ea commodo consequat.\n";

	private String lastLine = "ea commodo consequat.";
	private String mediaState;
	public static String sendDate;
	private static final int SWIPE_DURATION = 1000;
	private static String onlySpacesMessage="     ";

	
	@When("^I see dialog page$")
	public void WhenISeeDialogPage() throws Throwable {
	    PagesCollection.dialogPage = (DialogPage) PagesCollection.iOSPage;
	    PagesCollection.dialogPage.waitForCursorInputVisible();
	}
	
	@When("I tap on dialog page")
	public void ITapOnDialogPage(){
		PagesCollection.dialogPage.tapDialogWindow();
	}

	@When("^I tap on text input$")
	public void WhenITapOnTextInput() throws Throwable {
	    PagesCollection.dialogPage.tapOnCursorInput();
	}
	
	@When("^I type the message$")
	public void WhenITypeTheMessage() throws Throwable {
		message = CommonUtils.generateGUID();
		PagesCollection.dialogPage.sendStringToInput(message);
	}

	@When("^I multi tap on text input$")
	public void WhenIMultiTapOnTextInput() throws Throwable {
		PagesCollection.dialogPage.multiTapOnCursorInput();
	}
	
	@Then("^I see Hello message in the dialog$")
	public void ISeeHelloMessageFromMeInTheDialog() throws Throwable {
		String hellomessage = "HELLO FROM";
		String dialogHelloMessage = PagesCollection.dialogPage.getHelloCellFromDialog();
		Assert.assertTrue("Message \"" + dialogHelloMessage + "\" is not correct HELLO FROM message.", dialogHelloMessage.contains(hellomessage));
	}
	
	@Then("^I see Hey message in the dialog$")
	public void ISeeHeyMessageFromMeInTheDialog() throws Throwable {
		String heymessage = "HEY FROM";
		String dialogHeyMessage = PagesCollection.dialogPage.getHeyCellFromDialog();
		Assert.assertTrue("Message \"" + dialogHeyMessage + "\" is not correct HEY FROM message.", dialogHeyMessage.contains(heymessage));
	}

	@When("^I type the message and send it$")
	public void ITypeTheMessageAndSendIt() throws Throwable {
	    message = CommonUtils.generateGUID();
	    PagesCollection.dialogPage.sendStringToInput(message + "\n");
	}
	
	@When("^I send the message$")
	public void WhenISendTheMessage() throws Throwable {
		PagesCollection.dialogPage.inputStringFromKeyboard("\n");
	}
	
	@When("^I swipe up on dialog page to open other user personal page$")
	public void WhenISwipeUpOnDialogPage() throws IOException{
		PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage)PagesCollection.dialogPage.swipeUp(1000);
	}

	@Then("^I see my message in the dialog$")
	public void ThenISeeMyMessageInTheDialog() throws Throwable {
	    String dialogLastMessage = PagesCollection.dialogPage.getLastMessageFromDialog();
	    Assert.assertTrue("Message is different, actual :" + dialogLastMessage + " expected: " + message, dialogLastMessage.equals((message).trim()));
	}
	
	@Then("I see last message in dialog is expected message (.*)")
	public void ThenISeeLasMessageInTheDialogIsExpected(String msg) throws Throwable {
	    String dialogLastMessage = PagesCollection.dialogPage.getLastMessageFromDialog();
	    Assert.assertTrue("Message is different, actual :" + dialogLastMessage + " expected: " + msg, dialogLastMessage.equals((msg).trim()));
	}
	
	@Then("^I see last message in the dialog$")
	public void ThenISeeLastMessageInTheDialog() throws Throwable {
	    String dialogLastMessage = PagesCollection.dialogPage.getLastMessageFromDialog();
	    Assert.assertTrue("Message is different, actual :" + dialogLastMessage + " expected: " + lastLine, dialogLastMessage.equals((lastLine).trim()));
	}
	
	@When("^I swipe the text input cursor$")
	public void ISwipeTheTextInputCursor() throws Throwable {
		PagesCollection.dialogPage.swipeInputCursor();
	}
	
	@When("^I press Add Picture button$")
	public void IPressAddPictureButton() throws Throwable {
		CameraRollPage page = PagesCollection.dialogPage.pressAddPictureButton();
		PagesCollection.cameraRollPage = (CameraRollPage) page;
	}
	
	@Then("^I see Pending Connect to (.*) message on Dialog page$")
	public void ISeePendingConnectMessage(String user) throws Throwable {
		
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		Assert.assertTrue(PagesCollection.dialogPage.isConnectMessageValid(user));
		Assert.assertTrue(PagesCollection.dialogPage.isPendingButtonVisible());
	}
	
	@Then("^I see new photo in the dialog$")
	public void ISeeNewPhotoInTheDialog() throws Throwable {
		String dialogLastMessage = PagesCollection.dialogPage.getImageCellFromDialog();
		String imageCell = "ImageCell";
		Assert.assertEquals(imageCell, dialogLastMessage);
	}
	
	@When("I type and send long message and media link (.*)")
	public void ITypeAndSendLongTextAndMediaLink(String link) throws InterruptedException{
		PagesCollection.dialogPage.sendStringToInput(longMessage + link + "\n");
	}
	
	@When("^I memorize message send time$")
	public void IMemorizeMessageSendTime(){
		sendDate = PagesCollection.dialogPage.getSendTime();
	}
	
	@Then("I see media link (.*) and media in dialog")
	public void ISeeMediaLinkAndMediaInDialog(String link){
		Assert.assertEquals(link, PagesCollection.dialogPage.getLastMessageFromDialog());
		Assert.assertTrue("Media is missing in dialog", PagesCollection.dialogPage.isMediaContainerVisible());
	}
	
	@When("I click video container for the first time")
	public void IPlayVideoFirstTime() throws IOException, InterruptedException{
		PagesCollection.videoPlayerPage = (VideoPlayerPage)PagesCollection.dialogPage.clickOnVideoContainerFirstTime();
	}
	
	@When("I tap on dialog window")
	public void ITapOnDialogWindow(){
		PagesCollection.dialogPage.tapDialogWindow();
	}
	
	@When("I swipe right on Dialog page")
	public void ISwipeRightOnDialogPage() throws IOException{
		PagesCollection.dialogPage.swipeRight(500);
	}
	
	@When("I send long message")
	public void ISendLongMessage() throws InterruptedException{
		PagesCollection.dialogPage.sendStringToInput(longMessage);
	}
	
	@When("^I post media link (.*)$")
	public void IPostMediaLink(String link) throws Throwable {
	    PagesCollection.dialogPage.sendStringToInput(link + "\n");
	}
	
	@When("^I tap media link$")
	public void ITapMediaLink() throws Throwable {
		PagesCollection.dialogPage.startMediaContent();
	}
	
	@When("^I scroll media out of sight until media bar appears$")
	public void IScrollMediaOutOfSightUntilMediaBarAppears() throws Exception{
		PagesCollection.dialogPage = PagesCollection.dialogPage.scrollDownTilMediaBarAppears();
	 
	}
	
	@When("^I pause playing the media in media bar$")
	public void IPausePlayingTheMediaInMediaBar() throws Exception{
		PagesCollection.dialogPage.pauseMediaContent();
	}
	
	@When("^I press play in media bar$")
	public void IPressPlayInMediaBar() throws Exception{
		PagesCollection.dialogPage.playMediaContent();
	}
	
	@When("^I stop media in media bar$")
	public void IStopMediaInMediaBar() throws Exception{
		PagesCollection.dialogPage.stopMediaContent();
	}
	
	@Then("I see playing media is paused")
	public void ThePlayingMediaIsPaused(){
		String pausedState = IOSLocators.MEDIA_STATE_PAUSED;
		mediaState = PagesCollection.dialogPage.getMediaState();
		Assert.assertEquals(pausedState, mediaState);
	}
	
	@Then("I see media is playing")
	public void TheMediaIsPlaying(){
		String playingState = IOSLocators.MEDIA_STATE_PLAYING;
		mediaState = PagesCollection.dialogPage.getMediaState();
		Assert.assertEquals(playingState, mediaState);
	}
	
	@Then("The media stopps playing")
	public void TheMediaStoppsPlaying(){
		String endedState = IOSLocators.MEDIA_STATE_STOPPED;
		mediaState = PagesCollection.dialogPage.getMediaState();
		Assert.assertEquals(endedState, mediaState);
	}
	
	@When("I wait (.*) seconds for media to stop playing")
	public void IWaitForMediaStopPlaying(int time) throws Throwable{
		Thread.sleep(time*1000);
	}
	
	@Then("I see media bar on dialog page")
	public void ISeeMediaBarInDialog(){
		Assert.assertTrue(PagesCollection.dialogPage.isMediaBarDisplayed());
	}
		
	@Then("I dont see media bar on dialog page")
	public void ISeeMediaBarDisappear(){
		Assert.assertFalse(PagesCollection.dialogPage.isMediaBarDisplayed());
	}
	
	@When("^I tap on the media bar$")
	public void ITapOnMediaBar() throws Exception{
		PagesCollection.dialogPage.tapOnMediaBar();
	}
	
	@When("I scroll back to media container")
	public void IScrollUpOnDialogPage() throws Throwable{
		PagesCollection.dialogPage.scrollUpToMediaContainer();
	}
	
	@Then("I see conversation view is scrolled back to the playing media link (.*)")
	public void ISeeConversationViewIsScrolledBackToThePlayingMedia(String link){
		Assert.assertEquals(link, PagesCollection.dialogPage.getLastMessageFromDialog());
		Assert.assertTrue("View did not scroll back", PagesCollection.dialogPage.isMediaContainerVisible());
		String playingState = IOSLocators.MEDIA_STATE_PLAYING;
		mediaState = PagesCollection.dialogPage.getMediaState();
		Assert.assertEquals(playingState, mediaState);
	}
	
	@When("I tap and hold image to open full screen")
		public void ITapImageToOpenFullScreen() throws Throwable{
		PagesCollection.imageFullScreenPage = (ImageFullScreenPage)PagesCollection.dialogPage.tapImageToOpen();
	}
	
	@Then("^I scroll away the keyboard$")
	public void IScrollKeyboardAway() throws Throwable {
		PagesCollection.dialogPage.swipeDialogPageDown(500);
	}
	
	@Then("^I navigate back to conversations view$")
	public void INavigateToConversationsView() throws IOException {
		PagesCollection.dialogPage.swipeRight(SWIPE_DURATION);
	}
	
	@When("I try to send message with only spaces")
	public void ISendMessageWithOnlySpaces() throws Throwable{
		PagesCollection.dialogPage.sendStringToInput(onlySpacesMessage+ "\n");
	}
	
	@Then("I see message with only spaces is not send")
	public void ISeeMessageWithOnlySpacesIsNotSend(){
		Assert.assertFalse(onlySpacesMessage.equals(PagesCollection.dialogPage.getLastMessageFromDialog()));
	}
	
	@When("I input message with leading empty spaces")
	public void IInpuMessageWithLeadingEmptySpace() throws Throwable{
		message = onlySpacesMessage + CommonUtils.generateRandomString(10).toLowerCase();
		PagesCollection.dialogPage.inputStringFromKeyboard(message);
	}
	
	@When("I input message with trailing emtpy spaces")
	public void IInputMessageWithTrailingEmptySpace() throws Throwable{
		message = CommonUtils.generateRandomString(10).toLowerCase() + "." + onlySpacesMessage;
		PagesCollection.dialogPage.inputStringFromKeyboard(message);
	}
	
	@When("I input message with lower case and upper case")
	public void IInputMessageWithLowerAndUpperCase() throws Throwable{
		message = CommonUtils.generateRandomString(7).toLowerCase() + CommonUtils.generateRandomString(7).toUpperCase();
		PagesCollection.dialogPage.sendStringToInput(message);
	}
	
	@When("I input more than 200 chars message and send it")
	public void ISend200CharsMessage() throws Exception{
		message = CommonUtils.generateRandomString(210).toLowerCase().replace("x", " ");
		PagesCollection.dialogPage.sendStringToInput(message + "\n");
	}
	
	@When("I see keyboard")
	public void ISeeKeyboard(){
		Assert.assertTrue(PagesCollection.dialogPage.isKeyboardVisible());
	}
	
	@When("I dont see keyboard")
	public void IDontSeeKeyboard(){
		Assert.assertFalse(PagesCollection.dialogPage.isKeyboardVisible());
	}
	
	@When("I tap and hold on message input")
	public void ITapHoldTextInput(){
		PagesCollection.dialogPage.tapHoldTextInput();
	}
	
	@When("^I scroll to the beginning of the conversation$")
	public void IScrollToTheBeginningOfTheConversation() throws Throwable {
		PagesCollection.dialogPage.scrollToBeginningOfConversation();
	}

}
