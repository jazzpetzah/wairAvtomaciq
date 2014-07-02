package com.wearezeta.auto.android;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.*;

public class DialogPageSteps{

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
	public void WhenITypeTheMessageAndSendIt() throws Throwable {
		message = CommonUtils.generateGUID();
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

	@When("^I press Add Picture button$")
	public void WhenIPressAddPictureButton() throws Throwable {
		PagesCollection.dialogPage.tapAddPictureBtn(0);
	}

	@When("^I press \"(.*)\" button$")
	public void WhenIPressButton(String buttonName) throws Throwable {
		 switch(buttonName.toLowerCase())
		  {
		  case "take photo":
			  PagesCollection.dialogPage.takePhoto();
			  break;
		  case "confirm":
			  PagesCollection.dialogPage.confirm();
			  break;
		  }
	}
	
	@Then("^I see Hello message in the dialog$")
	public void ThenISeeHelloMessageInTheDialog() throws Throwable {
		Assert.assertTrue(PagesCollection.dialogPage.isknockAnimationExist());
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
	public void ISeeAddedMessageOnDialogPage(String user, String contact) throws Throwable {
		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		String chatMessage = user + " ADDED " + contact;
		Assert.assertTrue(PagesCollection.dialogPage.isConnectMessageVisible());
		Assert.assertTrue(PagesCollection.dialogPage.isConnectMessageValid(chatMessage));
	}
	
	@When("^I swipe left on dialog page$")
	public void WhenISwipeLeftOnDialogPage() throws IOException{
		PagesCollection.dialogPage.swipeLeft(1000);
	}
	
	@When("^I swipe up on dialog page$")
	public void WhenISwipeUpOnDialogPage() throws IOException{
		PagesCollection.otherUserPersonalInfoPage = (OtherUserPersonalInfoPage) PagesCollection.dialogPage.swipeUp(1000);
	}
}
