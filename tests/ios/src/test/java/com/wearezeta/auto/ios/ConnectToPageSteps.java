package com.wearezeta.auto.ios;

import java.io.IOException;

import javax.ws.rs.core.UriBuilderException;

import org.junit.Assert;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class ConnectToPageSteps {

	@When("^I see connect to (.*) dialog$")
	public void WhenISeeConnectToUserDialog(String contact) throws Throwable {

		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		Assert.assertTrue("Connect dialog is not visible",
				PagesCollection.connectToPage.waitForConnectDialog());
		Assert.assertTrue(PagesCollection.connectToPage
				.getConnectToUserLabelValue().contains(
						IOSLocators.CONNECT_TO_MESSAGE));
	}

	@When("^I input message in connect to dialog$")
	public void WhenIInputMessageInConnectToDialog() throws Throwable {
		PagesCollection.contactListPage = PagesCollection.connectToPage
				.fillTextInConnectDialog();

		// TODO: workaround, should return to Contact list page automatically
		PeoplePickerPageSteps pick = new PeoplePickerPageSteps();
		pick.WhenIClickClearButton();
	}

	@When("^I input message in connect to dialog and click Send button$")
	public void WhenIInputMessageInConnectDialogAndClickSendButton(String name)
			throws Throwable {
		PagesCollection.iOSPage = PagesCollection.connectToPage
				.sendInvitation(name);
	}

	@Given("^I have connection request from (.*)$")
	public void IHaveConnectionRequest(String contact)
			throws IllegalArgumentException, UriBuilderException, IOException,
			BackendRequestException {
		contact = CommonUtils.retrieveRealUserContactPasswordValue(contact);
		for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().equals(contact)) {
				BackEndREST.sendConnectRequest(user,
						CommonUtils.yourUsers.get(0), "CONNECT TO " + contact,
						"Hello");
			}
		}
	}

	@When("^I see connection request from (.*)$")
	public void IReceiveInvitationMessage(String contact) throws Throwable {

		// Not needed since we auto accept all alerts
	}

	@When("^I confirm connection request$")
	public void IAcceptInvitationMessage() {

		// Not needed since we auto accept all alerts
	}

}
