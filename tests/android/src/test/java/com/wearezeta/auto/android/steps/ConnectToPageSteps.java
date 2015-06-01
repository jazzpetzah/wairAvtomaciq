package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ConnectToPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ConnectToPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Verifies that the current screen shows the connect to dialog with a user
	 * you have not yet connected with
	 * 
	 * @step. ^I see connect to (.*) dialog$
	 * 
	 * @param contact
	 *            The name of the user with whom you are not yet connected.
	 * @throws Throwable
	 */
	@When("^I see connect to (.*) dialog$")
	public void WhenISeeConnectToUserDialog(String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		if (PagesCollection.connectToPage == null) {
			PagesCollection.connectToPage = (ConnectToPage) PagesCollection.currentPage;
		}
		Assert.assertTrue(
				String.format(
						"Connect To header with text '%s' is not visible, but should be",
						contact), PagesCollection.connectToPage
						.isConnectToHeaderVisible(contact));
	}

	/**
	 * Verifies that the connect and ignore buttons are visible when viewing the
	 * dialog of a user who has sent you a connection request.
	 * 
	 * @step. ^I see Accept and Ignore buttons$
	 * 
	 * @throws Exception
	 */
	@Then("^I see Accept and Ignore buttons$")
	public void ISeeConnectAndIgnoreButtons() throws Exception {
		Assert.assertTrue(PagesCollection.connectToPage
				.isIgnoreConnectButtonVisible());
	}

	/**
	 * Swipe Up on Connect Page
	 * 
	 * @step. ^I swipe up on connect page$
	 * 
	 * @throws Exception
	 */
	@Then("^I swipe up on connect page$")
	public void ISwipeUpOnConnectPage() throws Exception {
		if (PagesCollection.connectToPage == null) {
			PagesCollection.connectToPage = (ConnectToPage) PagesCollection.currentPage;
		}
		PagesCollection.connectToPage.waitUntilIgnoreButtonIsVisible();
		PagesCollection.connectToPage.swipeUpCoordinates(1000, 90);
		// It is very hard to detect whehn swipe animation is finished that is
		// why this hardcoded sleep is needed here
		Thread.sleep(5000);
	}

	/**
	 * Presses the accept connection request button from within the dialog of a
	 * user who has sent you a connection request.
	 * 
	 * @step. ^I Connect with contact by pressing button$
	 * 
	 * @throws Exception
	 */
	@When("^I Connect with contact by pressing button$")
	public void WhenIConnectWithContactByPressionButton() throws Exception {
		PagesCollection.dialogPage = PagesCollection.connectToPage
				.pressAcceptConnectButton();
	}

	/**
	 * Presses the ignore connection request button from within the dialog of a
	 * user who has sent you a connection request.
	 * 
	 * @step. ^I press Ignore connect button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Ignore connect button$")
	public void WhenIPressIgnoreConnectButton() throws Exception {
		if (PagesCollection.connectToPage == null) {
			PagesCollection.connectToPage = (ConnectToPage) PagesCollection.currentPage;
		}
		PagesCollection.contactListPage = PagesCollection.connectToPage
				.pressIgnoreButton();
	}

	/**
	 * Navigates back from the connect page to the contactList page
	 * 
	 * @step. ^I navigate back from connect page$
	 * 
	 * @throws Exception
	 */
	@When("^I navigate back from connect page$")
	public void WhenINavigateBackFromDialogPage() throws Exception {
		PagesCollection.contactListPage = PagesCollection.connectToPage
				.navigateBack();
	}

	/**
	 * Checks to see that a connection request is still pending
	 * 
	 * @step. ^I see that connection is pending$
	 * 
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@Then("^I see that connection is pending$")
	public void ThenConnectionIsPending() throws NumberFormatException,
			Exception {
		if (PagesCollection.connectToPage == null) {
			PagesCollection.connectToPage = (ConnectToPage) PagesCollection.currentPage;
		}
		Assert.assertTrue(PagesCollection.connectToPage.isPending());
	}

	/**
	 * Taps on the connection request message to put it in focus. Note: The
	 * message is also cleared (method name does not suggest this).
	 * 
	 * @step. ^I tap on edit connect request field$
	 * @throws Exception
	 * 
	 */
	@When("^I tap on edit connect request field$")
	public void WhenITapOnEditConnectRequestField() throws Exception {
		PagesCollection.connectToPage.tapEditConnectionRequest();
	}

	/**
	 * Types a message into the connect message input area
	 * 
	 * @step. ^I type Connect request \"(.*)\"$
	 * 
	 * @param message
	 *            The message to be sent
	 * @throws Exception
	 */
	@When("^I type Connect request \"(.*)\"$")
	public void WhenITypeConnectRequest(String message) throws Exception {
		PagesCollection.connectToPage.typeConnectionRequies(message);
	}

	/**
	 * Checks to see if the connect button is either enabled or disabled (true
	 * or false)
	 * 
	 * @step. ^I see connect button enabled state is (.*)$
	 * 
	 * @param state
	 * @throws Throwable
	 */
	@Then("^I see connect button enabled state is (.*)$")
	public void ThenISeeConnectButtonIsDisabled(boolean state) throws Throwable {
		Assert.assertEquals(state,
				PagesCollection.connectToPage.getConnectButtonState());
	}

	/**
	 * Checks to see that the counter value has a given number of remaining
	 * characters
	 * 
	 * @step. ^I see counter value (.*)$
	 * 
	 * @param value
	 * @throws Throwable
	 */
	@Then("^I see counter value (.*)$")
	public void ThenISeeCounterValue(int value) throws Throwable {
		Assert.assertEquals(value,
				PagesCollection.connectToPage.getCharCounterValue());
	}

	/**
	 * Taps the connect button to send a connection request
	 * 
	 * @step. ^I click [Cc]onnect button on connect to page$
	 * 
	 * @throws Exception
	 */
	@When("^I click [Cc]onnect button on connect to page$")
	public void WhenIClickConnectButton() throws Exception {
		PagesCollection.contactListPage = PagesCollection.connectToPage
				.pressConnectButton();
	}

	/**
	 * Blocks an incoming connection request
	 * 
	 * @step. ^I click Block button on connect to page$
	 * 
	 */
	@When("^I click Block button on connect to page$")
	public void IClickBlockButton() {
		PagesCollection.connectToPage.clickBlockBtn();

	}

	/**
	 * Presses the "Confirm Block" button that appears after pressing the block
	 * button (Should the two steps be merged?)
	 * 
	 * @step. ^I confirm block on connect to page$
	 * 
	 * @throws Exception
	 */
	@When("^I confirm block on connect to page$")
	public void WhenIConfirmBlock() throws Exception {
		PagesCollection.connectToPage.pressConfirmBtn();
	}

	/**
	 * Closes the connect to dialog by pressing the cross in the connect to
	 * dialog
	 * 
	 * @step. ^I close Connect To dialog$
	 * 
	 * @throws Exception
	 */
	@Then("I close Connect To dialog")
	public void ThenCloseConnectToDialog() throws Exception {
		PagesCollection.peoplePickerPage = PagesCollection.connectToPage
				.clickCloseButton();
	}

	/**
	 * Tap connection notification on Home Screen
	 * 
	 * @step. ^I tap connection notification on Home Screen$
	 * 
	 * @throws Exception
	 */
	@Then("^I tap connection notification on Home Screen$")
	public void TapConnectionNotification() throws Exception {
		if (PagesCollection.connectToPage == null) {
			PagesCollection.connectToPage = (ConnectToPage) PagesCollection.loginPage
					.instantiatePage(ConnectToPage.class);
		}
		PagesCollection.contactListPage = PagesCollection.connectToPage
				.tapConnectNotification();
	}
}
