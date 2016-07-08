package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.ConnectToPage;

import cucumber.api.java.en.When;

public class ConnectToPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ConnectToPage getConnectToPage() throws Exception {
        return pagesCollection.getPage(ConnectToPage.class);
    }

    /**
     * Verify visibility of connect dialog
     *
     * @param isClosed equals to null if should be visible
     * @throws Exception
     * @step. ^I see [Cc]onnect dialog( is closed)?
     */
    @When("^I see [Cc]onnect (to .*)?dialog( is closed)?$")
    public void WhenISeeConnectToUserDialog(String isClosed) throws Exception {
        boolean condition = (isClosed == null) ? getConnectToPage().isConnectToUserDialogVisible() :
                getConnectToPage().isConnectButtonInvisible();
        Assert.assertTrue(String.format("Connect dialog is %s but shouldn't be.", (isClosed == null) ? "invisible" :
                "visible"), condition);
    }

    /**
     * Tap Connect button on connect dialog
     *
     * @throws Exception
     * @step. ^I tap Connect button on connect to dialog$
     */
    @When("^I tap Connect button on connect to dialog$")
    public void IClickConnectButtonConnectDialog() throws Exception {
        getConnectToPage().tapConnectButton();
    }
}
