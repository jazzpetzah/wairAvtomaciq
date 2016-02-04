package com.wearezeta.auto.web.steps;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.StartPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StartPageSteps {
	
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	
	/**
	 * Open start page on website
	 *
	 * @step. ^I navigate to start page for (.*)$
	 *
	 * @throws Exception
	 */
	@When("^I navigate to start page for (.*)$")
	public void INavigateToStartPage(String agent) throws Exception {
		webappPagesCollection.getPage(StartPage.class).setUrl(
				WebAppConstants.STAGING_SITE_ROOT + "/%3Fagent=" + agent);
		webappPagesCollection.getPage(StartPage.class).navigateTo();
	}
	
	@When("^I navigate to german start page for (.*)$")
	public void INavigateToGermanStartPage(String agent) throws Exception {
		webappPagesCollection.getPage(StartPage.class).setUrl(
				WebAppConstants.STAGING_SITE_ROOT + "/l/de/");
		webappPagesCollection.getPage(StartPage.class).navigateTo();
		webappPagesCollection.getPage(StartPage.class).setUrl(
				WebAppConstants.STAGING_SITE_ROOT + "/%3Fagent=" + agent);
		webappPagesCollection.getPage(StartPage.class).navigateTo();
	}
	
	/**
	 * No dead links
	 * Gathers all links in a list and checks the status code for each link
	 * 
	 * @step. ^I can see no dead links$
	 * 
	 * @throws Exception
	 */
	@Then("^I can see no dead links$")
	public void ICanSeeNoDeadLinks() throws Exception {
        for (WebElement element : webappPagesCollection.getPage(StartPage.class).getAllElements()) {
            String href = element.getAttribute("href");
            //System.out.println("URL: " + href);
            int statusCode = webappPagesCollection.getPage(StartPage.class).getStatusCode(href);
            //System.out.println("Status Code: " + statusCode);
            //System.out.println(" ");
            assertThat("Tested URL: " + href,statusCode, lessThan(400));
        }
	}
	
}
