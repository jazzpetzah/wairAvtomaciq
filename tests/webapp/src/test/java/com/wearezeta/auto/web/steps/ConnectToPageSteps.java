package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.When;

public class ConnectToPageSteps {

	@SuppressWarnings("unused")
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	/**
	 * Clicks Connect button on connect popup
	 * 
	 * @step. I click Connect button on Connect to popup
	 * 
	 */
	@When("^I click Connect button on Connect to popup$")
	public void IAcceptConnectionRequestFromUser() {
		PagesCollection.connectToPopup.clickConnectButton();
	}
}
