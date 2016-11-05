package com.wearezeta.auto.ios.steps;

import cucumber.api.java.en.Then;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.details_overlay.single.SinglePendingUserIncomingConnectionPage;

import cucumber.api.java.en.When;

public class SingleUserIncomingPendingConnectionPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private SinglePendingUserIncomingConnectionPage getPendingRequestsPage() throws Exception {
        return pagesCollection.getPage(SinglePendingUserIncomingConnectionPage.class);
    }

    /**
     * Tap the corresponding button on Pending Request page
     *
     * @param btnName button name to tap
     * @throws Exception
     * @step. ^I tap (Ignore|Connect) button on Incoming Pending Requests page$
     */
    @When("^I tap (Ignore|Connect) button on Incoming Pending Requests page$")
    public void ITapIgnoreButtonPendingRequests(String btnName) throws Exception {
        getPendingRequestsPage().tapButton(btnName);
    }

    /**
     * Tap the corresponding button on Pending Request page multiple times
     *
     * @param btnName button name to tap
     * @param count how many times the button should be clicked. Should be greater than zero
     * @throws Exception
     * @step. ^I tap (Ignore|Connect) button on Incoming Pending Requests page (\d+) times?$
     */
    @When("^I tap (Ignore|Connect) button on Incoming Pending Requests page (\\d+) times?$")
    public void ITapIgnoreButtonPendingRequests(String btnName, int count) throws Exception {
        getPendingRequestsPage().tapButton(btnName, count);
    }

    @Then("^I see Incoming Pending Requests page$")
    public void ISeePendingRequestPage() throws Exception {
        Assert.assertTrue("Incoming Pending Requests page is not shown",
                getPendingRequestsPage().isConnectButtonDisplayed());
    }

    @Then("^I see Hello connect message from user (.*) on Incoming Pending Requests page$")
    public void ISeeHelloConnectMessageFrom(String user) throws Exception {
        user = usrMgr.findUserByNameOrNameAlias(user).getName();
        Assert.assertTrue(String.format("Connect To message is not visible for user '%s'", user),
                getPendingRequestsPage().isConnectToNameExist(user));
    }
}
