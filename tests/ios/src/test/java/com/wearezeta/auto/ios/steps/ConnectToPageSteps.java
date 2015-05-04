package com.wearezeta.auto.ios.steps;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.ConnectToPage;
import com.wearezeta.auto.ios.pages.PagesCollection;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class ConnectToPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@When("^I see connect to (.*) dialog$")
	public void WhenISeeConnectToUserDialog(String name) throws Throwable {
		Assert.assertTrue("Connection input is not visible",
				PagesCollection.connectToPage.isConnectToUserDialogVisible());
	}

	@When("^I input message in connect to dialog$")
	public void WhenIInputMessageInConnectToDialog() {
		PagesCollection.connectToPage.fillTextInConnectDialog();
	}

	/**
	 * Erases all connection message content
	 * 
	 * @step. I delete all connect message content
	 * 
	 * @throws IOException
	 */

	@When("^I delete all connect message content$")
	public void IDeleteAllMessageContent() throws IOException {
		PagesCollection.connectToPage.deleteTextInConnectDialog();
	}

	/**
	 * Verifies connection button is disabled
	 * 
	 * @step. I see that connect button is disabled
	 * 
	 * @throws AssertionError
	 *             if connection button is visible
	 */

	@When("^I see that connect button is disabled$")
	public void IClickSendButtonConnectDialog() throws IOException {
		Assert.assertFalse("Connect button is not disabled",
				PagesCollection.connectToPage.isConnectButtonVisible());
	}

	@When("I click Connect button on connect to dialog")
	public void IClickConnectButtonConnectDialog() throws Throwable {
		PagesCollection.peoplePickerPage = PagesCollection.connectToPage
				.sendInvitation();
	}

	@When("^I input message in connect to dialog and click Send button$")
	public void WhenIInputMessageInConnectDialogAndClickSendButton(String name)
			throws Throwable {
		PagesCollection.iOSPage = PagesCollection.connectToPage
				.sendInvitation(name);
	}

	@Given("^I have connection request from (.*)$")
	public void IHaveConnectionRequest(String contact) throws Throwable {
		BackendAPIWrappers.sendConnectRequest(
				usrMgr.findUserByNameOrNameAlias(contact),
				usrMgr.getSelfUserOrThrowError(), "CONNECT TO " + contact,
				"Hello");
	}

	/**
	 * Inputs a message with a certain number of random characters
	 * 
	 * @step. I input message in connect dialog with (.*) characters
	 * 
	 * @param characters
	 *            number of characters to input into dialog
	 * 
	 * @throws IOException
	 */

	@When("^I input message in connect dialog with (.*) characters$")
	public void IInputMessageWithLength(int characters) throws IOException {
		PagesCollection.connectToPage.enterCharactersIntoDialog(characters);
		;
	}

	/**
	 * Checks that the max amount of characters are present
	 * 
	 * @step. I input message in connect dialog with (.*) characters
	 * 
	 * @throws AssertionError
	 *             if too many characters are present
	 */

	@When("^I see message with max number of characters$")
	public void VerifyMaxCharacterCount() throws IOException {
		Assert.assertTrue(PagesCollection.connectToPage
				.isMaxCharactersInMessage());
	}

	@When("^I see connection request from (.*)$")
	public void IReceiveInvitationMessage(String contact) throws Throwable {
		// Not needed since we auto accept all alerts
		ContactListPageSteps clSteps = new ContactListPageSteps();
		clSteps.ISeeUserNameFirstInContactList(IOSLocators.xpathPendingRequest);
	}

	@When("^I confirm connection request$")
	public void IAcceptInvitationMessage() throws Exception {
		ContactListPageSteps clSteps = new ContactListPageSteps();
		clSteps.WhenITapOnContactName(IOSLocators.xpathPendingRequest);
		PagesCollection.connectToPage = (ConnectToPage) PagesCollection.loginPage
				.instantiatePage(ConnectToPage.class);
		PagesCollection.connectToPage.acceptInvitation();
		// Not needed since we auto accept all alerts
	}

}
