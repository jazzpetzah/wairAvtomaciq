package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.pages.external.StartPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.containsString;

public class StartPageSteps {

	private static final Logger log = ZetaLogger.getLog(StartPageSteps.class.getSimpleName());

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
                        case "unsupported":
				context.getPagesCollection().getPage(StartPage.class).setUrl(
						WebAppConstants.STAGING_SITE_ROOT + "/unsupported/%3Fagent=" + agent);
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
        for (WebElement element : context.getPagesCollection().getPage(StartPage.class).getAllLinkElements()) {
            String href = element.getAttribute("href");
			log.info("Check URL " + href);
            int statusCode = context.getPagesCollection().getPage(StartPage.class).getStatusCode(href);
			log.info("Status Code " + statusCode);
            assertThat("Tested URL: " + href,statusCode, lessThan(400));
        }
		for (WebElement element : context.getPagesCollection().getPage(StartPage.class).getAllImageElements()) {
			String src = element.getAttribute("src");
			log.info("Check Image " + src);
			int statusCode = context.getPagesCollection().getPage(StartPage.class).getStatusCode(src);
			log.info("Status Code " + statusCode);
			assertThat("Tested Image: " + src,statusCode, lessThan(400));
		}
	}

	/**
	 * Verifies that the language button is visible
	 *
	 * @step ^I can see language switch button for (.*) on (.*) for (.*)
	 *
	 * @param language the sites current language
	 * @param page the current utility page
	 * @param agent the used agent (iphone/android/osx/windows)
	 *
	 * @throws Exception
     */
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

	/**
	 * Verifies that an utility page is in the given language
	 *
	 * @step ^(.*) page for (.*) is (.*)
	 *
	 * @param page the current utility page
	 * @param agent the used agent (iphone/android/osx/windows)
	 * @param language the expected language
	 *
	 * @throws Exception
     */
	@Then("^(.*) page for (.*) is (.*)$")
	public void StartPageIs(String page, String agent, String language) throws Exception {
		StartPage startPage = context.getPagesCollection().getPage(StartPage.class);
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

	@Then("I switch to support page tab$")
	public void ISwitchToSupportPageTab() throws Exception {
		context.getPagesCollection().getPage(StartPage.class).switchToSupportPageTab();
	}

	@Then("^I verify the page title is in language (.*)$")
	public void IVerifyCorrectPageTitle(String language) throws Exception {
		StartPage startPage = context.getPagesCollection().getPage(StartPage.class);
		switch (language) {
			case "en":
				System.out.println("TITLE: " + startPage.getEnglishTitle());
				assertTrue("Title for " + language + " support page is not correct",startPage.getEnglishTitle().equals("Wire - Support"));
				break;
			case "de":
				System.out.println("TITLE: " + startPage.getGermanTitle());
				assertThat("Title for " + language + " support page is not correct",startPage.getGermanTitle(), containsString("Wire Hilfe"));
				break;
			default: break;
		}
	}

	@Then("^I see localized (.*) support page$")
	public void ISeeLocalizedSupportPage(String language) throws Exception {
		if (language.equals("en")) { language = "en-us"; }
		assertTrue("Support page is not in the correct language: " + language,
				context.getPagesCollection().getPage(WebPage.class).getCurrentUrl().equals("https://support.wire.com/hc/" + language));
	}
}
