package com.wearezeta.auto.android;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.When;

public class ConnectToPageSteps {

	@When("^I see connect to (.*) dialog$")
	public void WhenISeeConnectToUserDialog(String contact) throws Throwable {
		Assert.assertTrue("Connect dialog is not visible",PagesCollection.connectToPage.isSendRequestVisible());
		
	}
	
	@When("^I input message (.*) in connect to dialog$")
	public void WhenIInputMessageInConnectToDialog(String message) throws Throwable{
		PagesCollection.connectToPage.fillTextInConnectDialog(message);
	}
	
	@When("^I tap connect dialog Send button$")
	public void WhenITapOnSendButtonBelowConnectDialog() throws Throwable {
		PagesCollection.contactListPage = PagesCollection.connectToPage.tapSend();
	}
}
