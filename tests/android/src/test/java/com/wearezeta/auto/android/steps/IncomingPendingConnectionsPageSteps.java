package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.IncomingPendingConnectionsPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class IncomingPendingConnectionsPageSteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private IncomingPendingConnectionsPage getIncomingPendingConnectionsPage() throws Exception {
		return pagesCollection.getPage(IncomingPendingConnectionsPage.class);
	}

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
	@Then("^I see connect to (.*) dialog$")
	public void ISeeConnectToUserDialog(String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(
				String.format(
						"Connect To header with text '%s' is not visible, but should be",
						contact),
				getIncomingPendingConnectionsPage().isConnectToHeaderVisible(contact));
	}

	private final static int MAX_USERS = 5;

	/**
	 * Scroll to gived user in the inbox
	 * 
	 * @step. ^I scroll to inbox contact (.*)$
	 * 
	 * @param contact
	 *            The name of the user to search.
	 * @throws Throwable
	 */
	@When("^I scroll to inbox contact (.*)$")
	public void IScrollToInboxContact(String contact) throws Throwable {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		getIncomingPendingConnectionsPage().scrollToInboxContact(contact, MAX_USERS);
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
		Assert.assertTrue(getIncomingPendingConnectionsPage().isIgnoreConnectButtonVisible());
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
		getIncomingPendingConnectionsPage().waitUntilIgnoreButtonIsClickable();
		getIncomingPendingConnectionsPage().swipeUpCoordinates(1000, 50);
		// It is very hard to detect when swipe animation is finished that is
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
	public void IConnectWithContactByPressionButton() throws Exception {
		getIncomingPendingConnectionsPage().pressAcceptConnectButton();
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
	public void IPressIgnoreConnectButton() throws Exception {
		getIncomingPendingConnectionsPage().pressIgnoreButton();
	}

	/**
	 * Navigates back from the connect page to the contactList page
	 * 
	 * @step. ^I navigate back from connect page$
	 * 
	 * @throws Exception
	 */
	@When("^I navigate back from connect page$")
	public void INavigateBackFromDialogPage() throws Exception {
		getIncomingPendingConnectionsPage().navigateBack();
	}

	/**
	 * Checks to see that a connection request is still pending
	 * 
	 * @step. ^I see that connection is pending$
	 * 
	 * @throws Exception
	 */
	@Then("^I see that connection is pending$")
	public void ConnectionIsPending() throws Exception {
		Assert.assertTrue("Pending connection screen is not visible",
				getIncomingPendingConnectionsPage().isPending());
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
	public void ISeeConnectButtonIsDisabled(boolean state) throws Throwable {
		Assert.assertEquals(state, getIncomingPendingConnectionsPage().getConnectButtonState());
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
	public void ISeeCounterValue(int value) throws Throwable {
		Assert.assertEquals(value, getIncomingPendingConnectionsPage().getCharCounterValue());
	}

	/**
	 * Taps the connect button to send a connection request
	 * 
	 * @step. ^I click left Connect button$
	 * 
	 * @throws Exception
	 */
	@When("^I click left Connect button$")
	public void IClickLeftConnectButton() throws Exception {
		getIncomingPendingConnectionsPage().pressLeftConnectButton();
	}

	/**
	 * Taps the connect button to send a connection request
	 * 
	 * @step. ^I click [Cc]onnect button on connect to page$
	 * 
	 * @throws Exception
	 */
	@When("^I click [Cc]onnect button on connect to page$")
	public void IClickConnectButton() throws Exception {
		getIncomingPendingConnectionsPage().pressConnectButton();
	}

	/**
	 * Click ellipsis button to open additional menu items
	 * 
	 * @step. ^I click ellipsis button$"
	 * 
	 * @throws Exception
	 */
	@When("^I click ellipsis button$")
	public void IClickEllipsisButton() throws Exception {
		getIncomingPendingConnectionsPage().clickEllipsisButton();
	}

	/**
	 * Blocks an incoming connection request
	 * 
	 * @step. ^I click Block button on connect to page$
	 * @throws Exception
	 * 
	 */
	@When("^I click Block button$")
	public void IClickBlockButton() throws Exception {
		getIncomingPendingConnectionsPage().clickBlockBtn();
	}

	/**
	 * Unblocks an incoming connection request
	 * 
	 * @step. ^I click Unblock button on connect to page$
	 * @throws Exception
	 * 
	 */
	@When("^I click Unblock button$")
	public void IClickUnblockButton() throws Exception {
		getIncomingPendingConnectionsPage().clickUnblockBtn();
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
	public void IConfirmBlock() throws Exception {
		getIncomingPendingConnectionsPage().tapConfirmBlockButton();
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
	public void CloseConnectToDialog() throws Exception {
		getIncomingPendingConnectionsPage().clickCloseButton();
	}
}
