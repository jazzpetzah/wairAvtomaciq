package com.wearezeta.auto.web.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Assert;

import com.wearezeta.auto.web.pages.WebappPagesCollection;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GiphyPageSteps {

	@Then("^I see Giphy popup$")
	public void ISeeGiphyPopup() throws Throwable {
		Assert.assertTrue("Giphy link icon in Giphy popup not visible",
				WebappPagesCollection.giphyPage.isGiphyLinkVisible());
	}

	@Then("^I verify that the search of the Giphy popup contains (.*)$")
	public void IVerifyThatTheSearchOfTheGiphyPopupContains(String term) {
		assertThat(WebappPagesCollection.giphyPage.getSearchTerm(), equalTo(term.toUpperCase()));
	}

	@Then("^I see gif image in Giphy popup$")
	public void ISeeGifImageInGiphyPopup() throws Throwable {
		Assert.assertTrue("Gif in Giphy popup not visible",
				WebappPagesCollection.giphyPage.isGifImageVisible());
	}

	@Then("^I see more button in Giphy popup$")
	public void ISeeMoreButtonInGiphyPopup() throws Throwable {
		Assert.assertTrue("More button in Giphy popup not visible",
				WebappPagesCollection.giphyPage.isMoreButtonVisible());
	}

	@When("^I click send button in Giphy popup$")
	public void IClickSendButtonInGiphyPopup() throws Throwable {
	    WebappPagesCollection.giphyPage.clickSendButton();
	}
}
