package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.Lifecycle;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.StartPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StartPageSteps {
	
	private final WebappPagesCollection webappPagesCollection;

        
        private final Lifecycle.TestContext context;

    public StartPageSteps(Lifecycle.TestContext context) {
        this.context = context;
        this.webappPagesCollection = context.getPagesCollection();
    }
	
	/**
	 * Open start page on website
	 *
	 * @step. ^I navigate to start page for (.*)$
	 *
	 * @throws Exception
	 */
	@When("^I navigate to (.*) page for (.*)$")
	public void INavigateToPage(String page, String agent) throws Exception {
		switch (page) {
			case "start":
				webappPagesCollection.getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/%3Fagent=" + agent);
				webappPagesCollection.getPage(StartPage.class).navigateTo();
				break;
			case "privacy":
				webappPagesCollection.getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/privacy/%3Fagent=" + agent);
				webappPagesCollection.getPage(StartPage.class).navigateTo();
				break;
			case "legal":
				webappPagesCollection.getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/legal/%3Fagent=" + agent);
				webappPagesCollection.getPage(StartPage.class).navigateTo();
				break;
			case "job":
				webappPagesCollection.getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/jobs/%3Fagent=" + agent);
				webappPagesCollection.getPage(StartPage.class).navigateTo();
				break;
			case "download":
				webappPagesCollection.getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/download/%3Fagent=" + agent);
				webappPagesCollection.getPage(StartPage.class).navigateTo();
				break;
			case "forgot":
				webappPagesCollection.getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/forgot/%3Fagent=" + agent);
				webappPagesCollection.getPage(StartPage.class).navigateTo();
				break;
			default: break;
		}
	}
	
	@When("^I open german start page for (.*)$")
	public void IOpenGermanStartPage(String agent) throws Exception {
		webappPagesCollection.getPage(StartPage.class).setUrl(
				WebAppConstants.STAGING_SITE_ROOT + "/%3Fagent=" + agent);
		webappPagesCollection.getPage(StartPage.class).navigateTo();
		webappPagesCollection.getPage(StartPage.class).changeLanguageTo("german");
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
	
	@Then("^I can see language switch button for (.*) on (.*) for (.*)$")
	public void ICanSeeLanguageSwitchButton(String language, String page, String agent) throws Exception {
		String url = "";
		switch (language) {
			case "german":
				url = "/l/de/";
				assertThat("German language button is not visible on " + page + " page for " + agent, 
						webappPagesCollection.getPage(StartPage.class).getGermanValue(), equalTo(url));
				break;
			case "english":
				url = "/l/en/";
				assertThat("English language button is not visible on " + page + " page for " + agent,
						webappPagesCollection.getPage(StartPage.class).getEnglishValue(), equalTo(url));
				break;
			default: break;
		}
	}
	
	@Then("^I change language to (.*)$")
	public void IChangeLanguageTo(String language) throws Exception {
		webappPagesCollection.getPage(StartPage.class).changeLanguageTo(language);
	}
	
	@Then("^(.*) page for (.*) is (.*)$")
	public void StartPageIs(String page, String agent, String language) throws Exception {
		StartPage startPage = WebappPagesCollection.getInstance()
				.getPage(StartPage.class);
		switch (language) {
			case "english":
				assertTrue(page + "Page for " + agent + " is not in " + language, startPage.isEnglish());
				break;
			case "german":
				assertTrue(page + "Page for " + agent + " is not in " + language, startPage.isGerman());
				break;
			default: break;
		}
	}
}
