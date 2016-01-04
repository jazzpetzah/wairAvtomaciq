package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.DownloadPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DownloadPageSteps {
	
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	/**
	 * Open download page on website
	 *
	 * @step. ^I navigate to download page$
	 *
	 * @throws Exception
	 */
	@When("^I navigate to download page$")
	public void INavigateToDownloadPage() throws Exception {
		webappPagesCollection.getPage(DownloadPage.class).setUrl(
				WebAppConstants.STAGING_SITE_ROOT + "/download/");
		webappPagesCollection.getPage(DownloadPage.class).navigateTo();
	}
	
	@Then("^I see button for iOS$")
	public void ISeeButtonForIOS() throws Exception {
		String storeUrl = "https://itunes.apple.com/app/wire/id930944768?mt=8";
		assertThat(webappPagesCollection.getPage(DownloadPage.class).getIOSDownloadHref(), equalTo(storeUrl));
	}
}
