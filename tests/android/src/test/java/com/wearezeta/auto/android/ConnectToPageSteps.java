package com.wearezeta.auto.android;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ConnectToPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConnectToPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verifies that the current screen shows the connect to dialog with a user you have not yet connected with
	 * @param contact
	 * 		The name of the user with whom you are not yet connected.
	 * @throws Throwable
	 */
	@When("^I see connect to (.*) dialog$")
	public void WhenISeeConnectToUserDialog(String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		PagesCollection.connectToPage = (ConnectToPage) PagesCollection.androidPage;

//		Thread.sleep(2000);
//		if (PagesCollection.contactListPage.isHintVisible()) {
//			PagesCollection.contactListPage.closeHint();
//		}

		Assert.assertEquals(contact.toLowerCase(),
				PagesCollection.connectToPage.getConnectToHeader());
	}

	/**
	 * Verifies that the connect and ignore buttons are visible when viewing the dialog of a user who has sent you a connection request.
	 * @throws Exception
	 */
	@Then("^I see Accept and Ignore buttons$")
	public void ISeeConnectAndIgnoreButtons() throws Exception {
		Assert.assertTrue(PagesCollection.connectToPage
				.isIgnoreConnectButtonVisible());
	}

	/**
	 * Presses the accept connection request button from within the dialog of a user who has sent you a connection request.
	 * @throws Exception
	 */
	@When("^I Connect with contact by pressing button$")
	public void WhenIConnectWithContactByPressionButton() throws Exception {
		PagesCollection.dialogPage = PagesCollection.connectToPage
				.pressAcceptConnectButton();
	}

	/**
	 * Presses the ignore connection request button from within the dialog of a user who has sent you a connection request.
	 * @throws Exception
	 */
	@When("^I press Ignore connect button$")
	public void WhenIPressIgnoreConnectButton() throws Exception {
		if (PagesCollection.connectToPage == null) {
			PagesCollection.connectToPage = (ConnectToPage) PagesCollection.androidPage;
		}
		PagesCollection.contactListPage = PagesCollection.connectToPage
				.pressIgnoreButton();
	}

	/**
	 * Navigates back from the 
	 * @throws Exception
	 */
	@When("^I navigate back from connect page$")
	public void WhenINavigateBackFromDialogPage() throws Exception {
		PagesCollection.contactListPage = PagesCollection.connectToPage
				.navigateBack();
	}

	@Then("^I see that connection is pending$")
	public void ThenConnectionIsPending() throws NumberFormatException,
			Exception {
		if (PagesCollection.connectToPage == null) {
			PagesCollection.connectToPage = (ConnectToPage) PagesCollection.androidPage;
		}
		Assert.assertTrue(PagesCollection.connectToPage.isPending());
	}

	@When("^I tap on edit connect request field$")
	public void WhenITapOnEditConnectRequestFieldn() {
		PagesCollection.connectToPage.tapEditConnectionRequies();
	}

	@When("^I type Connect request \"(.*)\"$")
	public void WhenITypeConnectRequest(String message) throws Exception {
		PagesCollection.connectToPage.typeConnectionRequies(message);
	}

	@When("^I press Connect button$")
	public void WhenIPressConnectButton() throws Exception {
		PagesCollection.contactListPage = PagesCollection.connectToPage
				.pressConnectButton();
	}

	@Then("^I see connect button enabled state is (.*)$")
	public void ThenISeeConnectButtonIsDisabled(boolean state) throws Throwable {
		Assert.assertEquals(state,
				PagesCollection.connectToPage.getConnectButtonState());
	}

	@Then("^I see counter value (.*)$")
	public void ThenISeeCounterValue(int value) throws Throwable {
		Assert.assertEquals(value,
				PagesCollection.connectToPage.getCharCounterValue());
	}

	@When("^I Press Block button on connect to page$")
	public void IPressBlockButton() {
		PagesCollection.connectToPage.clickBlockBtn();

	}

	@When("^I confirm block on connect to page$")
	public void WhenIConfirmBlock() throws Throwable {
		PagesCollection.connectToPage.pressConfirmBtn();
	}

	@Then("I close Connect To dialog")
	public void ThenCloseConnectToDialog() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.connectToPage
				.clickCloseButton();
	}
}
