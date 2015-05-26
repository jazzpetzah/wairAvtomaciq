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
	 * Verified whether Connect To popoup is displayed within some timeout
	 * 
	 * @throws Exception
	 */
	@And("^I see Connect To popover$")
	public void ISeeConnectToPopover() throws Exception {
		((ConnectToPopoverContainer) PagesCollection.popoverPage)
				.waitUntilVisibleOrThrowException();
	}
	
}
