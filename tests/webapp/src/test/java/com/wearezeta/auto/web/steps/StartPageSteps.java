package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.LoginPage;
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.pages.external.StartPage;
import com.wearezeta.auto.web.pages.external.SupportPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
		final String website = CommonUtils.getWebsitePathFromConfig(StartPageSteps.class);
		switch (page) {
			case "start":
				context.getPagesCollection().getPage(StartPage.class).setUrl(website + "/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "privacy":
				context.getPagesCollection().getPage(StartPage.class).setUrl(website + "/privacy/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "legal":
				context.getPagesCollection().getPage(StartPage.class).setUrl(website + "/legal/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "job":
				context.getPagesCollection().getPage(StartPage.class).setUrl(website + "/jobs/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "download":
				context.getPagesCollection().getPage(StartPage.class).setUrl(website + "/download/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "forgot":
				context.getPagesCollection().getPage(StartPage.class).setUrl(website + "/forgot/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "unsupported":
				context.getPagesCollection().getPage(StartPage.class).setUrl(website + "/unsupported/%3Fagent=" + agent);
				context.getPagesCollection().getPage(StartPage.class).navigateTo();
				break;
			case "login":
				System.out.println(CommonUtils.getWebAppApplicationPathFromConfig(LoginPage.class));
				context.getPagesCollection().getPage(WebPage.class).setUrl(
						CommonUtils.getWebAppApplicationPathFromConfig(LoginPage.class) + "/?agent=" + agent);
				context.getPagesCollection().getPage(WebPage.class).navigateTo();
				break;
			default: break;
		}
	}
	
	@When("^I open german start page for (.*)$")
	public void IOpenGermanStartPage(String agent) throws Exception {
		final String website = CommonUtils.getWebsitePathFromConfig(StartPageSteps.class);
		context.getPagesCollection().getPage(StartPage.class).setUrl(website + "/%3Fagent=" + agent);
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
		List<String> links = new ArrayList<>();
		List<String> pictures = new ArrayList<>();
		for (WebElement element : context.getPagesCollection().getPage(StartPage.class).getAllLinkElements()) {
			String href = element.getAttribute("href");
			log.info("Check URL " + href);
			if (href != null) {
				int statusCode = context.getPagesCollection().getPage(StartPage.class).getStatusCode(href);
				log.info("Status Code " + statusCode);
				if (statusCode >= 400) {
					links.add(href);
				}
			} else {
				log.info("skip check because attribute is null");
			}
		}
		for (WebElement element : context.getPagesCollection().getPage(StartPage.class).getAllImageElements()) {
			String src = element.getAttribute("src");
			log.info("Check Image " + src);
			int statusCode = context.getPagesCollection().getPage(StartPage.class).getStatusCode(src);
			log.info("Status Code " + statusCode);
			if (statusCode >= 400) {
				pictures.add(src);
			}
		}
		if (!links.isEmpty() || !pictures.isEmpty()) {
			System.out.println("");
			for (String href : links) {
				int statusCode = context.getPagesCollection().getPage(StartPage.class).getStatusCode(href);
				System.out.println("Failed connection to " + href + " with statuscode " + statusCode);
			}
			for (String src : pictures) {
				int statusCode = context.getPagesCollection().getPage(StartPage.class).getStatusCode(src);
				System.out.println("Failed connection to " + src + " with statuscode " + statusCode);
			}
			Assert.fail("Statuscode is greater than 400 for one or more links/pictures: " + links + " | " + pictures);
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

	@Then("I see ask support link")
	public void ISeeAskSupportLink() throws Exception {
		assertTrue("No 'Ask Support' link", context.getPagesCollection().getPage(SupportPage.class).isAskSupportVisible());
	}

	@Then("^I see localized (.*) support page$")
	public void ISeeLocalizedSupportPage(String language) throws Exception {
		if (language.equals("en")) {
			language = "en-us";
		}
		assertThat("Support page is not in the correct language: " + language,
				context.getPagesCollection().getPage(WebPage.class).getCurrentUrl(), equalTo("https://support.wire.com/hc/" + language));
	}

	@Then("^I see unsupported browser page$")
	public void ISeeUnsupportedBrowserPage() throws Exception {
		assertThat(
				context.getPagesCollection().getPage(
						StartPage.class)
						.isUnsupportedPageVisible(), is(true));
	}
}
