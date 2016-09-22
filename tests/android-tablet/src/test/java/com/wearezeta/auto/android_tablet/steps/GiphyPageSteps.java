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
     * @step. ^I tap (Send|Cancel) button on the Giphy [Pp]review page$
     */
    @When("^I tap (Send|Cancel) button on the Giphy [Pp]review page$")
    public void ITapButton(String buttonName) throws Exception {
        getGiphyPage().tapOnConfirmationButton(buttonName);
    }

    /**
     * Selects one of the gifs from the grid preview
     *
     * @throws Exception
     * @step. ^I select (a random|\d+(?:st|nd|th)) gif from the grid preview$
     */
    @When("^I select (a random|\\d+(?:st|nd|th)) gif from the grid preview$")
    public void ISelectARandomGifFromTheGridPreview(String index) throws Exception {
        getGiphyPage().tapGifPreviewByIndex(index);
    }
}
