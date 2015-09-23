package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

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
		if (WebAppExecutionContext.isCurrentPlatformWindows()) {
			WebappPagesCollection.youAreInvitedPage.waitUntilConnectButtonVisible();
		} else {
			WebappPagesCollection.youAreInvitedPage
					.waitUntilDownloadWireButtonVisible();
		}
	}
}
