package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class YouAreInvitedPageSteps {

	/**
	 * Verifies whether You are invited page is visible. Verification differs
	 * for Windows and Mac platforms
	 * 
	 * @step. ^I see You are invited page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see You are invited page$")
	public void ISeeYouAreInvitedPage() throws Exception {
		if (WebAppExecutionContext.isCurrentPlatfromWindows()) {
			PagesCollection.youAreInvitedPage.waitUntilConnectButtonVisible();
		} else {
			PagesCollection.youAreInvitedPage
					.waitUntilDownloadWireButtonVisible();
		}
	}

	/**
	 * Click connect button on You are invited page or throws PendingException
	 * on non-Windows platform
	 * 
	 * @step. ^I click Connect button on You are invited page$
	 * 
	 * @throws Exception
	 */
	@And("^I click Connect button on You are invited page$")
	public void ISeeConnectButton() throws Exception {
		if (WebAppExecutionContext.isCurrentPlatfromWindows()) {
			PagesCollection.youAreInvitedPage.clickConnectButton();
		} else {
			throw new PendingException(
					"The feature is not available under Mac OS");
		}
	}
}
