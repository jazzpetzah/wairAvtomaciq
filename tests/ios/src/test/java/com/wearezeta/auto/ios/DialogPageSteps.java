package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.CameraRollPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DialogPageSteps {
	
	private String message;
	
	@When("^I see dialog page$")
	public void WhenISeeDialogPage() throws Throwable {
	    PagesCollection.dialogPage = (DialogPage) PagesCollection.iOSPage;
	    PagesCollection.dialogPage.waitForCursorInputVisible();
	}

	@When("^I tap on text input$")
	public void WhenITapOnTextInput() throws Throwable {
	    PagesCollection.dialogPage.tapOnCursorInput();
	}
	
	@When("^I type the message$")
	public void WhenITypeTheMessage() throws Throwable {
		PagesCollection.dialogPage.waitForTextMessageInputVisible();
		message = CommonUtils.generateGUID();
		PagesCollection.dialogPage.typeMessage(message + "\n");
	}
	
	@When("^I press send$")
	public void WhenIPressSend() throws Throwable {
		//PagesCollection.dialogPage.typeMessage("\\n");
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
		PagesCollection.dialogPage.waitForTextMessageInputVisible();
	    message = CommonUtils.generateGUID();
	    PagesCollection.dialogPage.typeMessage(message + "\n");
	}
	
	@When("^I swipe up on dialog page$")
	public void WhenISwipeUpOnDialogPage() throws IOException{
		PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage)PagesCollection.dialogPage.swipeUp(1000);
	}

	@Then("^I see my message in the dialog$")
	public void ThenISeeMyMessageInTheDialog() throws Throwable {
	    String dialogLastMessage = PagesCollection.dialogPage.getLastMessageFromDialog();
	    Assert.assertTrue("Message is different, actual :" + dialogLastMessage +
	    		" expected: " + message, dialogLastMessage.equals((message).trim()));
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
	
	@When("^I post media link$")
	public void IPostMediaLink() throws Throwable {
		PagesCollection.dialogPage.waitForTextMessageInputVisible();
	    message = "https://soundcloud.com/klinke-auf-cinch/04-whats-happening-boytalk-remix";
	    PagesCollection.dialogPage.typeMessage(message + "\n");
	    PagesCollection.dialogPage.typeMessage(message + "\n");
	}
	
	@When("^I tap media link$")
	public void ITapMediaLink() throws Throwable {
		PagesCollection.dialogPage.startMediaContent();
		Thread.sleep(5000);
	}
	
	@When("^I scroll media out of sight until media bar appears$")
	public void IScrollMediaOutOfSightUntilMediaBarAppears() throws Exception{
		PagesCollection.dialogPage.scrollDownTilMediaBarAppears();
	 
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
	

}
