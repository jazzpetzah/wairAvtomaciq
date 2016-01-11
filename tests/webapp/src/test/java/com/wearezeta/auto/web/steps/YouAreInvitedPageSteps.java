package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.YouAreInvitedPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class YouAreInvitedPageSteps {
	
	String code = "hello";
	
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
	
	@Then("^I see You are invited page with agent$")
	public void ISeeYouAreInvitedPageWithAgent() throws Exception {
		if (WebAppExecutionContext.isCurrentPlatformWindows()) {
			webappPagesCollection.getPage(YouAreInvitedPage.class)
					.waitUntilConnectButtonVisible();
		} else {
			webappPagesCollection.getPage(YouAreInvitedPage.class)
					.waitUntilDownloadButtonVisible();
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
	public void IUseGenericInvitationLinkForInvitation(String agent) throws Exception {
		//String code = "hello";
		webappPagesCollection.getPage(YouAreInvitedPage.class).setUrl(
				WebAppConstants.STAGING_SITE_ROOT + "/c/" + code + "/%3Fagent=" + agent);
		webappPagesCollection.getPage(YouAreInvitedPage.class).navigateTo();
	}

	@Then("^I see button that sends me to App Store$")
	public void ISeeButtonThatSendsMeToAppStore() throws Exception {
		String storeUrl = "https://itunes.apple.com/app/wire/id930944768?mt=8";
		assertThat(webappPagesCollection.getPage(YouAreInvitedPage.class).getDownloadHref(), equalTo(storeUrl));
	}

	@Then("^I see button that sends me to Play Store$")
	public void ISeeButtonThatSendsMeToPlayStore() throws Exception {
		//String code = "hello";
		String storeUrl = "https://play.google.com/store/apps/details?id=com.wire&referrer=" + code;
		assertThat(webappPagesCollection.getPage(YouAreInvitedPage.class).getDownloadHref(), equalTo(storeUrl));		
	}

	@Then("^I see button to connect for iphone including invitation code$")
	public void ISeeButtonToConnectForIPhone() throws Exception {
		//String code = "hello";
		assertThat(webappPagesCollection.getPage(YouAreInvitedPage.class).getConnectHref(), containsString(code));
	}

	@Then("^I see button to connect for android including invitation code$")
	public void ISeeButtonToConnectForAndroid() throws Exception {
		//String code = "hello";
		assertThat(webappPagesCollection.getPage(YouAreInvitedPage.class).getConnectHref(), containsString(code));
	}
	
	@Then("^I see 'Open Wire' button$")
	public void ISeeOpenWireButton() throws Exception {
		//String code = "hello";
		String url = "https://wire-webapp-staging.zinfra.io/auth/?connect=" + code;
		assertThat(webappPagesCollection.getPage(YouAreInvitedPage.class).getOpenHref(), equalTo(url));
	}
	
}
