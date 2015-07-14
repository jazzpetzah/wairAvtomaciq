package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.popovers.ConnectToPopoverContainer;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;

public class ConnectToPopoverPageSteps {
	/**
	 * Clicks Connect button on connect popup
	 * 
	 * @step. ^I click Connect button on Connect To popover$
	 * @throws Exception
	 * 
	 */
	@When("^I click Connect button on Connect To popover$")
	public void IAcceptConnectionRequestFromUser() throws Exception {
		((ConnectToPopoverContainer) PagesCollection.popoverPage)
				.clickConnectButton();
	}

	/**
	 * Verified whether Connect To popover is displayed within some timeout
	 * 
	 * @throws Exception
	 */
	@And("^I see Connect To popover$")
	public void ISeeConnectToPopover() throws Exception {
		((ConnectToPopoverContainer) PagesCollection.popoverPage)
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
			((ConnectToPopoverContainer) PagesCollection.popoverPage)
					.waitUntilVisibleOrThrowException();
		} else {
			((ConnectToPopoverContainer) PagesCollection.popoverPage)
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
		((ConnectToPopoverContainer) PagesCollection.popoverPage)
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
		((ConnectToPopoverContainer) PagesCollection.popoverPage)
				.waitUntilVisibleOrThrowException();
	}

	/**
	 * Clicks No button on Cancel request confirmation popover
	 * 
	 * @step. ^I click No button on Cancel request confirmation popover$
	 */
	@When("^I click No button on Cancel request confirmation popover$")
	public void IClickNoButtonOnCancelRequestConfirmationPopover() {
		((ConnectToPopoverContainer) PagesCollection.popoverPage)
				.clickNoButton();
	}

	/**
	 * Clicks Yes button on Cancel request confirmation popover
	 * 
	 * @step. ^I click Yes request on Pending Outgoing Connection popover$
	 */
	@When("^I click Yes button on Cancel request confirmation popover$")
	public void IClickYesButtonOnCancelRequestConfirmationPopover() {
		((ConnectToPopoverContainer) PagesCollection.popoverPage)
				.clickYesButton();
	}
}
