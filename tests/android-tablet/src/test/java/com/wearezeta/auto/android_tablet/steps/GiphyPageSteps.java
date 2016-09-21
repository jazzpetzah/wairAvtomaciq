package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletGiphyPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GiphyPageSteps {
    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();

    private TabletGiphyPage getGiphyPage() throws Exception {
        return pagesCollection.getPage(TabletGiphyPage.class);
    }

    /**
     * Verify whether Giphy page is visible
     *
     * @throws Exception
     * @step. ^I see Giphy [Pp]review page$"
     */
    @Then("^I see Giphy [Pp]review page$")
    public void ISeeGiphyPage() throws Exception {
        Assert.assertTrue("Giphy preview page is not shown", getGiphyPage()
                .waitUntilIsVisible());
    }

    /**
     * Tap the corresponding button on Giphy preview page
     *
     * @throws Exception
     * @step. ^I tap Send button on the Giphy [Pp]review page$
     */
    @When("^I tap Send button on the Giphy [Pp]review page$")
    public void ITapButton() throws Exception {
        getGiphyPage().tapSendButton();
    }

    /**
     * Selects one of the gifs from the grid preview
     *
     * @throws Exception
     * @step. ^I select a random gif from the grid preview$
     */
    @When("^I select a random gif from the grid preview$")
    public void ISelectARandomGifFromTheGridPreview() throws Exception {
        getGiphyPage().clickOnSomeGif();
    }
}
