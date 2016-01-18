package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ConnectToPage;

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
}
