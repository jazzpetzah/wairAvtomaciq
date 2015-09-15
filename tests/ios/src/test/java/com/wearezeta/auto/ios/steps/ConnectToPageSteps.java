package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.pages.ConnectToPage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class ConnectToPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private ConnectToPage getConnectToPage() throws Exception {
		return (ConnectToPage) pagesCollecton.getPage(ConnectToPage.class);
	}

	@When("^I see connect to (.*) dialog$")
	public void WhenISeeConnectToUserDialog(String name) throws Throwable {
		Assert.assertTrue("Connection input is not visible", getConnectToPage()
				.isConnectToUserDialogVisible());
	}

	@When("^I input message in connect to dialog$")
	public void WhenIInputMessageInConnectToDialog() throws Exception {
		getConnectToPage().fillHelloTextInConnectDialog();
	}

	/**
	 * Erases all connection message content
	 * 
	 * @step. I delete all connect message content
	 * @throws Exception
	 */
	@When("^I delete all connect message content$")
	public void IDeleteAllMessageContent() throws Exception {
		getConnectToPage().deleteTextInConnectDialog();
	}

	/**
	 * Verifies connection button is disabled
	 * 
	 * @step. I see that connect button is disabled
	 * @throws Exception
	 * 
	 * @throws AssertionError
	 *             if connection button is visible
	 */
	@When("^I see that connect button is disabled$")
	public void ISeeConnectButtonDisabled() throws Exception {
		Assert.assertFalse("Connect button is not disabled", getConnectToPage()
				.isConnectButtonVisibleAndDisabled());
	}

	@When("I click Connect button on connect to dialog")
	public void IClickConnectButtonConnectDialog() throws Throwable {
		// if (getConnectToPage().isKeyboardVisible()) {
		// getConnectToPage().clickKeyboardGoButton();
		// }
		getConnectToPage().sendInvitation();
	}

	/**
	 * Verify if connect dialog closed
	 * 
	 * @step. ^I see Connect dialog is closed$
	 * 
	 * @throws Exception
	 */
	@When("^I see Connect dialog is closed$")
	public void ISeeConnectDialogClosed() throws Exception {
		Assert.assertFalse("Connect dialog still shown", getConnectToPage()
				.isConnectButtonVisible());
	}

	@When("^I input message in connect to dialog and click Send button$")
	public void WhenIInputMessageInConnectDialogAndClickSendButton(String name)
			throws Throwable {
		getConnectToPage().sendInvitation(name);
	}

	@Given("^I have connection request from (.*)$")
	public void IHaveConnectionRequest(String contact) throws Throwable {
		BackendAPIWrappers.sendConnectRequest(
				usrMgr.findUserByNameOrNameAlias(contact),
				usrMgr.getSelfUserOrThrowError(), "CONNECT TO " + contact,
				"Hello");
	}

	/**
	 * Inputs a message with a certain number of random characters by script on
	 * iPAD
	 * 
	 * @step. I input message in connect dialog with (.*) characters
	 * 
	 * @param characters
	 *            number of characters to input into dialog
	 * @throws Exception
	 */

	@When("^I input message in connect dialog with (.*) characters$")
	public void IInputMessageWithLength(int characters) throws Exception {
		getConnectToPage().inputCharactersIntoConnectDialogByScript(characters,
				false);
	}

	/**
	 * Inputs a message with a certain number of random characters by script on
	 * iPhone
	 * 
	 * @step. ^I type message in connect dialog with (.*) characters$
	 * 
	 * @param characters
	 *            number of characters to input into dialog
	 * @throws Exception
	 */

	@When("^I type message in connect dialog with (.*) characters$")
	public void IInputMessageWithLengthPhone(int characters) throws Exception {
		getConnectToPage().inputCharactersIntoConnectDialogByScript(characters,
				true);
	}

	/**
	 * Fill in a message with a certain number of random characters from
	 * keyboard
	 * 
	 * @step. I fill in (.*) characters in connect dialog
	 * 
	 * @param characters
	 *            number of characters to input into dialog
	 * @throws Exception
	 */

	@When("^I fill in (.*) characters in connect dialog$")
	public void IFillInCharsInConnectDialog(int characters) throws Exception {
		getConnectToPage().fillTextInConnectDialogWithLengh(characters);
	}

	/**
	 * Checks that the max amount of characters are present
	 * 
	 * @step. I see message with max number of characters
	 * @throws Exception
	 * 
	 */

	@When("^I see message with max number of characters$")
	public void VerifyMaxCharacterCount() throws Exception {
		Assert.assertTrue(getConnectToPage().isMaxCharactersInMessage());
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
		getConnectToPage().acceptInvitation();
		// Not needed since we auto accept all alerts
	}

}
