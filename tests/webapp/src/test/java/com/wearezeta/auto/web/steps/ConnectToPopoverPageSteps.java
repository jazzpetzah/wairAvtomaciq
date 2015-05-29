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
	 * Verified whether Pending Outgoing Connection popover is displayed within some timeout
	 * 
	 * @throws Exception
	 */
	@And("^I see Pending Outgoing Connection popover$")
	public void ISeePendingOutgoingConnectionPopover() throws Exception {
		((ConnectToPopoverContainer) PagesCollection.popoverPage)
				.waitUntilVisibleOrThrowException();
	}
	
	/**
	 * Clicks Pending button on Pending Outgoing Connection popover
	 * 
	 * @step. ^I click Pending button on Pending Outgoing Connection popover$
	 * @throws Exception
	 * 
	 */
	@When("^I click Pending button on Pending Outgoing Connection popover$")
	public void IClickPendingButtonOnPendingOutgoingConnectionPopover() throws Exception {
		((ConnectToPopoverContainer) PagesCollection.popoverPage)
				.clickPendingButton();
	}
	
}
