package com.wearezeta.auto.android;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.*;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.*;

public class Conversation{

	private String message; 

	@When("^I see dialog page$")
	public void WhenISeeDialogPage() throws Throwable {
		PagesCollection.dialogPage = (DialogPage) PagesCollection.androidPage;
		PagesCollection.dialogPage.waitForCursorInputVisible();
	}

	@When("^I tap on bottom part of the screen$")
	public void WhenITapOnBottomPartOfTheScreen() throws Throwable {
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
	
	@When("^I multi tap on bottom part of the screen$")
	public void WhenIMultiTapOnBottomPartOfTheScreen() throws Throwable {
		PagesCollection.dialogPage.multiTapOnCursorInput();
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

}
