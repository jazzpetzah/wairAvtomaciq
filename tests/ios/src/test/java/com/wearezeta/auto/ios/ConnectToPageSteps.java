package com.wearezeta.auto.ios;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.ConnectToPage;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.PeoplePickerPage;

import cucumber.api.java.en.When;

public class ConnectToPageSteps {
	
	@When("^I see connect to (.*) dialog$")
	public void WhenISeeConnectToUserDialog(String contact) throws Throwable {
	    Assert.assertEquals("Connect to "+ contact, PagesCollection.connectToPage.getConnectToUserLabelValue());
		Assert.assertTrue("Connect dialog is not visible", PagesCollection.connectToPage.waitForConnectDialog());
		
	}
	
	@When("^I input message in connect to dialog$")
	public void WhenIInputMessageInConnectToDialog() throws Throwable{
		PagesCollection.connectToPage.fillTextInConnectDialog();
	}
	
	@When("^I input message in connect to dialog and click Send button$")
	public void WhenIInputMessageInConnectDialogAndClickSendButton(String name) throws Throwable {
		PagesCollection.iOSPage = PagesCollection.connectToPage.sendInvitation(name);
	}
	
	@When("^I tap connect dialog Send button$")
	public void WhenITapOnSendButtonBelowConnectDialog() throws Throwable {
		PagesCollection.connectToPage.clickSendButton();
	}

}
