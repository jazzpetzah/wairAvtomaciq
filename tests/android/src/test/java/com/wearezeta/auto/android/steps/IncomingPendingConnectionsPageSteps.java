package com.wearezeta.auto.android.steps;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.IncomingPendingConnectionsPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class IncomingPendingConnectionsPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private IncomingPendingConnectionsPage getIncomingPendingConnectionsPage() throws Exception {
        return pagesCollection.getPage(IncomingPendingConnectionsPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    /**
     * Verifies that the current screen shows the connect to dialog with a user you have not yet connected with
     *
     * @param contact The name of the user with whom you are not yet connected.
     * @throws Throwable
     * @step. ^I see connect to (.*) dialog$
     */
    @Then("^I see connect to (.*) dialog$")
    public void ISeeConnectToUserDialog(String contact) throws Throwable {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(String.format("Connect To header with text '%s' is not visible, but should be", contact),
                getIncomingPendingConnectionsPage().isConnectToHeaderVisible(contact));
    }

    private final static int MAX_USERS = 5;

    /**
     * Scroll to gived user in the inbox
     *
     * @param contact The name of the user to search.
     * @throws Throwable
     * @step. ^I scroll to inbox contact (.*)$
     */
    @When("^I scroll to inbox contact (.*)$")
    public void IScrollToInboxContact(String contact) throws Throwable {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        getIncomingPendingConnectionsPage().scrollToInboxContact(contact, MAX_USERS);
    }

    /**
     * Verifies that the connect and ignore buttons are visible when viewing the dialog of a user who has sent you a
     * connection request
     *
     * @throws Exception
     * @step. ^I see Accept and Ignore buttons$
     */
    @Then("^I see Accept and Ignore buttons$")
    public void ISeeConnectAndIgnoreButtons() throws Exception {
        Assert.assertTrue(getIncomingPendingConnectionsPage().isIgnoreConnectButtonVisible());
    }

    /**
     * Swipe Up on Connect Page
     *
     * @throws Exception
     * @step. ^I swipe up on connect page$
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
     * Click the accept connection request button from within the dialog of a user who has sent you a connection request
     *
     * @throws Exception
     * @step. ^I Connect with contact by taping button$
     */
    @When("^I (?:Connect with|Unblock) contact by pressing button$")
    public void IConnectWithContactBytapionButton() throws Exception {
        getIncomingPendingConnectionsPage().tapAcceptConnectButton();
    }

    /**
     * Click the ignore connection request button from within the dialog of a user who has sent you a connection request
     *
     * @throws Exception
     * @step. ^I tap Ignore connect button$
     */
    @When("^I tap Ignore connect button$")
    public void ITapIgnoreConnectButton() throws Exception {
        getIncomingPendingConnectionsPage().tapIgnoreButton();
    }

    /**
     * Navigates back from the connect page to the contactList page
     *
     * @throws Exception
     * @step. ^I navigate back from connect page$
     */
    @When("^I navigate back from connect page$")
    public void INavigateBackFromDialogPage() throws Exception {
        getIncomingPendingConnectionsPage().navigateBack();
    }

    /**
     * Checks to see that a connection request is still pending
     *
     * @throws Exception
     * @step. ^I see that connection is pending$
     */
    @Then("^I see that connection is pending$")
    public void ConnectionIsPending() throws Exception {
        Assert.assertTrue("Pending connection screen is not visible", getIncomingPendingConnectionsPage().isPending());
    }

    /**
     * Checks to see if the connect button is either enabled or disabled (true or false)
     *
     * @param state
     * @throws Throwable
     * @step. ^I see connect button enabled state is (.*)$
     */
    @Then("^I see connect button enabled state is (.*)$")
    public void ISeeConnectButtonIsDisabled(boolean state) throws Throwable {
        Assert.assertEquals(state, getIncomingPendingConnectionsPage().getConnectButtonState());
    }

    /**
     * Checks to see that the counter value has a given number of remaining characters
     *
     * @param value
     * @throws Throwable
     * @step. ^I see counter value (.*)$
     */
    @Then("^I see counter value (.*)$")
    public void ISeeCounterValue(int value) throws Throwable {
        Assert.assertEquals(value, getIncomingPendingConnectionsPage().getCharCounterValue());
    }

    /**
     * Taps the left button by label
     *
     * @param buttonLabel Connect or Blocked
     * @throws Exception
     * @step. ^I tap left (.*) button$
     */
    @When("^I tap left (.*) button$")
    public void ITapLeftButtonByLabel(String buttonLabel) throws Exception {
        getIncomingPendingConnectionsPage().tapLeftButtonByLabel(buttonLabel);
    }

    /**
     * Taps the connect button to send a connection request
     *
     * @throws Exception
     * @step. ^I tap [Cc]onnect button on connect to page$
     */
    @When("^I tap [Cc]onnect button on connect to page$")
    public void ITapConnectButton() throws Exception {
        getIncomingPendingConnectionsPage().tapConnectButton();
    }

    /**
     * Click ellipsis button to open additional menu items
     *
     * @throws Exception
     * @step. ^I tap ellipsis button$"
     */
    @When("^I tap ellipsis button$")
    public void ITapEllipsisButton() throws Exception {
        getIncomingPendingConnectionsPage().clickEllipsisButton();
    }

    /**
     * Blocks an incoming connection request
     *
     * @throws Exception
     * @step. ^I tap Block button on connect to page$
     */
    @When("^I tap Block button$")
    public void ITapBlockButton() throws Exception {
        getIncomingPendingConnectionsPage().tapBlockBtn();
    }

    /**
     * Unblocks an incoming connection request
     *
     * @throws Exception
     * @step. ^I tap Unblock button on connect to page$
     */
    @When("^I tap Unblock button$")
    public void ITapUnblockButton() throws Exception {
        getIncomingPendingConnectionsPage().clickUnblockBtn();
    }

    /**
     * Click the "Confirm Block" button that appears after taping the block button (Should the two steps be merged?)
     *
     * @throws Exception
     * @step. ^I confirm block on connect to page$
     */
    @When("^I confirm block on connect to page$")
    public void IConfirmBlock() throws Exception {
        getIncomingPendingConnectionsPage().tapConfirmBlockButton();
    }

    /**
     * Closes the connect to dialog by taping the cross in the connect to dialog
     *
     * @throws Exception
     * @step. ^I close Connect To dialog$
     */
    @Then("I close Connect To dialog")
    public void CloseConnectToDialog() throws Exception {
        getIncomingPendingConnectionsPage().tapCloseButton();
    }
}
