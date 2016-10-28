package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.GiphyPreviewPage;

import cucumber.api.java.en.When;

public class GiphyPreviewPageSteps {

    private final IOSPagesCollection pagesCollecton = IOSPagesCollection.getInstance();

    private GiphyPreviewPage getGiphyPreviewPage() throws Exception {
        return pagesCollecton.getPage(GiphyPreviewPage.class);
    }

    /**
     * Tap the first item from Giphy search grid
     *
     * @throws Exception
     * @step. ^I select the first item from Giphy grid$
     */
    @When("^I select the first item from Giphy grid$")
    public void ISelectFirstItem() throws Exception {
        getGiphyPreviewPage().selectFirstItem();
    }

    /**
     * Tap the corresponding button on Giphy preview page
     *
     * @throws Exception
     * @step. ^I tap (Send|Cancel) button on Giphy preview page$
     */
    @When("^I tap (Send|Cancel) button on Giphy preview page$")
    public void ITapButtonOnGiphyPreview(String btnName) throws Exception {
        getGiphyPreviewPage().tapButton(btnName);
    }

    /**
     * Verify that all elements on giphy preview page are visible
     *
     * @throws Exception
     * @step. ^I see Giphy preview page$
     */
    @When("^I see Giphy preview page$")
    public void ISeeGiphyPreviewPage() throws Exception {
        Assert.assertTrue("Giphy Image is not visible", getGiphyPreviewPage().isPreviewVisible());
        Assert.assertTrue("Giphy Cancel Button is not visible", getGiphyPreviewPage().isButtonVisible("Cancel"));
        Assert.assertTrue("Giphy Send Button is not visible", getGiphyPreviewPage().isButtonVisible("Send"));
    }

    /**
     * Verify that giphy grid is shown
     *
     * @throws Exception
     * @step. ^I see giphy grid preview$
     */
    @When("^I see Giphy grid preview$")
    public void ISeeGiphyGridPreview() throws Exception {
        Assert.assertTrue("Giphy grid is not shown", getGiphyPreviewPage().isGridVisible());
    }

}