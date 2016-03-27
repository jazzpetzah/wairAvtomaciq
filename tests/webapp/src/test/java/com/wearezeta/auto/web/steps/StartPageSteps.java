package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.pages.external.StartPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class StartPageSteps {
	
        private final TestContext context;
        
    public StartPageSteps() {
        this.context = new TestContext();
    }

    public StartPageSteps(TestContext context) {
        this.context = context;
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
				context.getPagesCollection().getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "privacy":
				context.getPagesCollection().getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/privacy/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "legal":
				context.getPagesCollection().getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/legal/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "job":
				context.getPagesCollection().getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/jobs/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "download":
				context.getPagesCollection().getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/download/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "forgot":
				context.getPagesCollection().getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/forgot/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			default: break;
		}
	}
	
	@When("^I open german start page for (.*)$")
	public void IOpenGermanStartPage(String agent) throws Exception {
		context.getPagesCollection().getPage(StartPage.class).setUrl(
				WebAppConstants.STAGING_SITE_ROOT + "/%3Fagent=" + agent);
		context.getPagesCollection().getPage(StartPage.class).navigateTo();
		context.getPagesCollection().getPage(StartPage.class).changeLanguageTo("german");
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
        for (WebElement element : context.getPagesCollection().getPage(StartPage.class).getAllElements()) {
            String href = element.getAttribute("href");
            //System.out.println("URL: " + href);
            int statusCode = context.getPagesCollection().getPage(StartPage.class).getStatusCode(href);
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
						context.getPagesCollection().getPage(StartPage.class).getGermanValue(), equalTo(url));
				break;
			case "english":
				url = "/l/en/";
				assertThat("English language button is not visible on " + page + " page for " + agent,
						context.getPagesCollection().getPage(StartPage.class).getEnglishValue(), equalTo(url));
				break;
			default: break;
		}
	}
	
	@Then("^I change language to (.*)$")
	public void IChangeLanguageTo(String language) throws Exception {
		context.getPagesCollection().getPage(StartPage.class).changeLanguageTo(language);
	}
	
	@Then("^(.*) page for (.*) is (.*)$")
	public void StartPageIs(String page, String agent, String language) throws Exception {
		StartPage startPage = context.getPagesCollection().getInstance()
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
