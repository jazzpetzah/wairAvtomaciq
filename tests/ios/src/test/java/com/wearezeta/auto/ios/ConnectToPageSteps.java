package com.wearezeta.auto.ios;

import org.junit.Assert;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class ConnectToPageSteps {
	
	@When("^I see connect to (.*) dialog$")
	public void WhenISeeConnectToUserDialog(String contact) throws Throwable {
		
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
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
	
	@Given("^I have connection request from (.*)$")
	public void IHaveConnectionRequest(String contact) {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.yourUsers) {
			if(user.getName().equals(contact)) {
				BackEndREST.sendConnectRequest(user, CommonUtils.yourUsers.get(0), "CONNECT TO " + contact, "Hello");
			}
		}
	}
	
	@When("^I see connection request from (.*)$")
	public void IReceiveInvitationMessage(String contact) throws Throwable {
		
		Assert.assertTrue(PagesCollection.contactListPage.waitForConnectionAllert());
	}
	
	@When("^I confirm connection request$")
	public void IAcceptInvitationMessage() {
		
		PagesCollection.contactListPage.acceptConnectionRequest();
	}

}
