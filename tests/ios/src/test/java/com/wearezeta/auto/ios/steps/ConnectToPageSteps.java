package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.PendingOutgoingConnectionPage;

import cucumber.api.java.en.When;

public class ConnectToPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private PendingOutgoingConnectionPage getPendingOutgoingConnectionPage() throws Exception {
        return pagesCollection.getPage(PendingOutgoingConnectionPage.class);
    }

    /**
     * Verify visibility of pending outgoing connection
     *
     * @param shouldNotBeVisible equals to null if the view should be visible
     * @throws Exception
     * @step. ^I (do not )?see Pending outgoing connection page$
     */
    @When("^I (do not )?see Pending outgoing connection page$")
    public void ISeeView(String shouldNotBeVisible) throws Exception {
        boolean condition = (shouldNotBeVisible == null) ?
                getPendingOutgoingConnectionPage().isConnectButtonVisible() :
                getPendingOutgoingConnectionPage().isConnectButtonInvisible();
        Assert.assertTrue(String.format("Connect dialog is %s but shouldn't be.",
                (shouldNotBeVisible == null) ? "invisible" : "visible"), condition);
    }

    /**
     * Tap Connect button on Pending outgoing connection page
     *
     * @throws Exception
     * @step. ^I tap Connect button on Pending outgoing connection page$
     */
    @When("^I tap Connect button on Pending outgoing connection page$")
    public void ITapButton() throws Exception {
        getPendingOutgoingConnectionPage().tapConnectButton();
    }
}
