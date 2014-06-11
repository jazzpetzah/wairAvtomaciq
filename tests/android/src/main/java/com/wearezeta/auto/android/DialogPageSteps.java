package com.wearezeta.auto.android;

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

	@When("^I type the message$")
	public void WhenITypeTheMessage() throws Throwable {
		message = CommonUtils.generateGUID() + " ";
		PagesCollection.dialogPage.typeMessage(message);
	}

	@When("^I press send$")
	public void WhenIPressSend() throws Throwable {
		PagesCollection.dialogPage.typeMessage("\\n");
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
		Assert.assertTrue(PagesCollection.dialogPage.getLastMessageFromDialog().equals(message.trim()));
	}
	
	@Then("^I see new photo in the dialog$")
	public void ThenISeeNewPhotoInTheDialog() throws Throwable {
	    Assert.assertTrue(PagesCollection.dialogPage.isImageExists());
	}

}
