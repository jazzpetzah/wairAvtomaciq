package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.popovers.ConnectToPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class ConnectToPopoverPageSteps {

    private final TestContext context;

    public ConnectToPopoverPageSteps(TestContext context) {
        this.context = context;
    }

    @When("^I click Connect button on Connect To popover$")
    public void IAcceptConnectionRequestFromUser() throws Exception {
        context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
                .clickConnectButton();
    }

    @And("^I see Connect To popover$")
    public void ISeeConnectToPopover() throws Exception {
        context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
                .waitUntilVisibleOrThrowException();
    }

    @And("^I( do not)? see Pending Outgoing Connection popover$")
    public void ISeePendingOutgoingConnectionPopover(String doNot)
            throws Exception {
        if (doNot == null) {
            context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
                    .waitUntilVisibleOrThrowException();
        } else {
            context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
                    .waitUntilNotVisibleOrThrowException();
        }
    }

    @When("^I click Cancel request on Pending Outgoing Connection popover$")
    public void IClickCancelRequestButtonOnPendingOutgoingConnectionPopover()
            throws Exception {
        context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
                .clickCancelRequestButton();
    }

    @And("^I see Cancel request confirmation popover$")
    public void ISeeCancelRequestConfirmationPopover() throws Exception {
        context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
                .waitUntilVisibleOrThrowException();
    }

    @When("^I click No button on Cancel request confirmation popover$")
    public void IClickNoButtonOnCancelRequestConfirmationPopover()
            throws Exception {
        context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
                .clickNoButton();
    }

    @When("^I click Yes button on Cancel request confirmation popover$")
    public void IClickYesButtonOnCancelRequestConfirmationPopover()
            throws Exception {
        context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
                .clickYesButton();
    }
}
