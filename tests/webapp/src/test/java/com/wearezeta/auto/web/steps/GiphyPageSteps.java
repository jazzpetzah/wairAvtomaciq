package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.GiphyPage;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Assert;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GiphyPageSteps {

    private final TestContext context;

    public GiphyPageSteps(TestContext context) {
        this.context = context;
    }

    @Then("^I see Giphy popup$")
    public void ISeeGiphyPopup() throws Throwable {
        Assert.assertTrue("Giphy link icon in Giphy popup not visible",
                context.getPagesCollection().getPage(GiphyPage.class)
                        .isGiphyQueryVisible());
    }

    @Then("^I verify that the search of the Giphy popup contains (.*)$")
    public void IVerifyThatTheSearchOfTheGiphyPopupContains(String term)
            throws Exception {
        assertThat(context.getPagesCollection().getPage(GiphyPage.class)
                .getSearchTerm(), equalTo(term.toUpperCase()));
    }

    @Then("^I see gif image in Giphy popup$")
    public void ISeeGifImageInGiphyPopup() throws Throwable {
        Assert.assertTrue("Gif in Giphy popup not visible",
                context.getPagesCollection().getPage(GiphyPage.class)
                        .isGifImageVisible());
    }

    @Then("^I see more button in Giphy popup$")
    public void ISeeMoreButtonInGiphyPopup() throws Throwable {
        Assert.assertTrue("More button in Giphy popup not visible",
                context.getPagesCollection().getPage(GiphyPage.class)
                        .isMoreButtonVisible());
    }

    @When("^I click send button in Giphy popup$")
    public void IClickSendButtonInGiphyPopup() throws Throwable {
        context.getPagesCollection().getPage(GiphyPage.class).clickSendButton();
    }
}
