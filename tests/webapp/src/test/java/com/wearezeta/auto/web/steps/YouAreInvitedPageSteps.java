package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.YouAreInvitedPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class YouAreInvitedPageSteps {

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

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
			webappPagesCollection.getPage(YouAreInvitedPage.class)
					.waitUntilConnectButtonVisible();
		} else {
			webappPagesCollection.getPage(YouAreInvitedPage.class)
					.waitUntilDownloadWireButtonVisible();
		}
	}

	/**
	 * Open generic invitation link directly and overwrite User-Agent
	 *
	 * @step. ^I use generic invitation link for invitation for (.*)$
	 *
	 * @param agent
	 *            ?agent= parameter to determine if iphone, android, osx or
	 *            windows is used
	 * @throws Exception
	 */
	@When("^I use generic invitation link for invitation for (.*)$")
	public void IUseGenericInvitationLinkForInvitation(String agent)
			throws Exception {
		String code = "hello";
		webappPagesCollection.getPage(YouAreInvitedPage.class).setUrl(
				WebAppConstants.STAGING_SITE_ROOT + "/c/" + code + "/?agent="
						+ agent);
	}

	@Then("^I see button that sends me to App Store$")
	public void ISeeButtonThatSendsMeToAppStore() {

	}

	@Then("^I see button that sends me to Play Store$")
	public void ISeeButtonThatSendsMeToPlayStore() {

	}

	@Then("^I see button to connect for iphone including invitation code$")
	public void ISeeButtonToConnectForIPhone() {

	}

	@Then("^I see button to connect for android including invitation code$")
	public void ISeeButtonToConnectForAndroid() {

	}
}
