package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.popovers.ConnectToPopoverContainer;

import cucumber.api.java.en.When;

public class ConnectToPopoverPageSteps {
	/**
	 * Clicks Connect button on connect popup
	 * 
	 * @step. I click Connect button on Connect to popup
	 * @throws Exception
	 * 
	 */
	@When("^I click Connect button on Connect to popup$")
	public void IAcceptConnectionRequestFromUser() throws Exception {
		((ConnectToPopoverContainer) PagesCollection.popoverPage)
				.clickConnectButton();
	}
}
