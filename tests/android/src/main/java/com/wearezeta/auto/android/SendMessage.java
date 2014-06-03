package com.wearezeta.auto.android;

import java.io.File;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;

public class SendMessage{

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

	@Then("^I see my message in the dialog$")
	public void ThenISeeMyMessageInTheDialog() throws Throwable {
		String dialogMessage = PagesCollection.dialogPage.getLastMessageFromDialog();
		System.out.println("Last message from the dialog\"" + dialogMessage + "\"");
		System.out.println("Generated message\"" + message.trim() + "\"");
		Assert.assertTrue(dialogMessage.equals(message.trim()));
	}

}
