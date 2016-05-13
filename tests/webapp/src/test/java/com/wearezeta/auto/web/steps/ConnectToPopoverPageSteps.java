package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.popovers.ConnectToPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class ConnectToPopoverPageSteps {

    private final TestContext context;
    
    public ConnectToPopoverPageSteps() {
        this.context = new TestContext();
    }

    public ConnectToPopoverPageSteps(TestContext context) {
        this.context = context;
    }

	/**
	 * Clicks Connect button on connect popup
	 * 
	 * @step. ^I click Connect button on Connect To popover$
	 * @throws Exception
	 * 
	 */
	@When("^I click Connect button on Connect To popover$")
	public void IAcceptConnectionRequestFromUser() throws Exception {
		context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
				.clickConnectButton();
	}

	/**
	 * Verified whether Connect To popover is displayed within some timeout
	 * 
	 * @throws Exception
	 */
	@And("^I see Connect To popover$")
	public void ISeeConnectToPopover() throws Exception {
		context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
				.waitUntilVisibleOrThrowException();
	}

	/**
	 * Verified whether Pending Outgoing Connection popover is displayed within
	 * some timeout
	 *
	 * @param doNot
	 *            is set to null if "do not" part does not exist
	 * @throws Exception
	 */
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

	/**
	 * Clicks Cancel request button on Pending Outgoing Connection popover
	 * 
	 * @step. ^I click Cancel request on Pending Outgoing Connection popover$
	 * @throws Exception
	 * 
	 */
	@When("^I click Cancel request on Pending Outgoing Connection popover$")
	public void IClickCancelRequestButtonOnPendingOutgoingConnectionPopover()
			throws Exception {
		context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
				.clickCancelRequestButton();
	}

	/**
	 * Verified whether Cancel request confirmation popover is displayed within
	 * some timeout
	 * 
	 * @throws Exception
	 */
	@And("^I see Cancel request confirmation popover$")
	public void ISeeCancelRequestConfirmationPopover() throws Exception {
		context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
				.waitUntilVisibleOrThrowException();
	}

	/**
	 * Clicks No button on Cancel request confirmation popover
	 * 
	 * @step. ^I click No button on Cancel request confirmation popover$
	 */
	@When("^I click No button on Cancel request confirmation popover$")
	public void IClickNoButtonOnCancelRequestConfirmationPopover()
			throws Exception {
		context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
				.clickNoButton();
	}

	/**
	 * Clicks Yes button on Cancel request confirmation popover
	 * 
	 * @step. ^I click Yes request on Pending Outgoing Connection popover$
	 */
	@When("^I click Yes button on Cancel request confirmation popover$")
	public void IClickYesButtonOnCancelRequestConfirmationPopover()
			throws Exception {
		context.getPagesCollection().getPage(ConnectToPopoverContainer.class)
				.clickYesButton();
	}
}
