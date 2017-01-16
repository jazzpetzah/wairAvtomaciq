package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.misc.Timedelta;
import cucumber.api.java.en.Then;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.GiphyPreviewPage;

import cucumber.api.java.en.When;

public class GiphySteps {
    private GiphyPreviewPage getGiphyPreviewPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(GiphyPreviewPage.class);
    }

    private final ElementState giphyImageContainerState = new ElementState(
            () -> getGiphyPreviewPage().getGiphyImageContainerState());

    private final ElementState giphyImagePreviewState = new ElementState(
            () -> getGiphyPreviewPage().getGiphyImagePreviewState());


    /**
     * Type text in Giphy Toolbar Input field
     *
     * @param text
     * @param hideKeyboard equals null means do not hide keyboard
     * @throws Exception
     * @step. ^I type "(.*)" in Giphy toolbar input field$
     */
    @When("^I type \"(.*)\" in Giphy toolbar input field( and hide keyboard)?$")
    public void ISearchGiphy(String text, String hideKeyboard) throws Exception {
        getGiphyPreviewPage().typeTextOnSearchField(text, hideKeyboard != null);
    }

    /**
     * Clear text in Giphy Search Field
     *
     * @throws Exception
     * @step. ^I clear Giphy search input field$
     */
    @When("^I clear Giphy search input field$")
    public void IClearGiphySearchField() throws Exception {
        getGiphyPreviewPage().clearSearchField();
    }

    /**
     * Check if giphy preview page with all required UI elements appears
     *
     * @throws Exception
     * @step. ^I see giphy preview page$
     */
    @When("^I see giphy preview page$")
    public void ISeeGiphyPreviewPage() throws Exception {
        Assert.assertTrue("Giphy preview page is not shown", getGiphyPreviewPage().isGiphyPreviewShown());
    }

    /**
     * Checks to see that the grid preview is visible
     *
     * @throws Exception
     * @step. ^I see the giphy grid preview$
     */
    @Then("^I see the giphy grid preview$")
    public void ISeeTheGiphyGridPreview() throws Exception {
        Assert.assertTrue("Giphy Grid view is not shown", getGiphyPreviewPage().isGiphyGridViewShown());
    }

    /**
     * Verify Giphy error message is visible or invisible
     *
     * @param shouldNotSee equals null means the Giphy error message should be visible
     * @throws Exception
     * @step.^I see giphy error message$
     */
    @Then("^I( do not)? see giphy error message$")
    public void ISeeGiphyErrorMessage(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The Giphy error message should be visible",
                    getGiphyPreviewPage().waitUntilGiphyErrorMessageVisible());
        } else {
            Assert.assertTrue("The Giphy error message should be invisible",
                    getGiphyPreviewPage().waitUntilGiphyErrorMessageInvisible());
        }
    }

    /**
     * Selects one of the gifs from the grid preview
     *
     * @throws Exception
     * @step. ^I select (a random|\d+(?:st|nd|th)) gif from the grid preview$
     */
    @When("^I select (a random|\\d+(?:st|nd|th)) gif from the grid preview$")
    public void ISelectARandomGifFromTheGridPreview(String index) throws Exception {
        getGiphyPreviewPage().tapGifPreviewByIndex(index);
    }


    /**
     * Tap on Giphy Grid view close button
     *
     * @throws Exception
     * @step. ^I close Giphy Grid view$
     */
    @When("^I close Giphy Grid view$")
    public void ICloseGiphyGridView() throws Exception {
        getGiphyPreviewPage().tapOnCloseButton();
    }

    /**
     * Click on SEND button
     *
     * @throws Exception
     * @step. ^I tap on the giphy (Send|Cancel) button$
     */
    @When("^I tap on the giphy (Send|Cancel) button$")
    public void ITapOnTheGiphySendButton(String buttonName) throws Exception {
        getGiphyPreviewPage().tapOnConfirmationButton(buttonName);
    }

    /**
     * Verify the Giphy preview confirmation button is visible
     *
     * @param buttonName
     * @throws Exception
     * @step. ^I see (Send|Cancel) button on Giphy Preview page$
     */
    @Then("^I see (Send|Cancel) button on Giphy Preview page$")
    public void ISeeConfirmationButton(String buttonName) throws Exception {
        Assert.assertTrue(String.format("Expect Giphy %s button be visible", buttonName),
                getGiphyPreviewPage().waitUntilConfirmationButtonVisible(buttonName));
    }

    /**
     * Store the screenshot of Giphy Image Container
     *
     * @throws Exception
     * @step. ^I remember the state of Giphy Image Container$
     */
    @When("^I remember the state of Giphy Image Container$")
    public void IRememberGiphyImageContainerState() throws Exception {
        giphyImageContainerState.remember();
    }

    /**
     * Store the screenshot of Giphy Image Preview
     *
     * @throws Exception
     * @step. ^I remember the state of Giphy Image Preview$
     */
    @When("^I remember the state of Giphy Image Preview$")
    public void IRememberGiphyImagePreviewState() throws Exception {
        giphyImagePreviewState.remember();
    }

    private static final double MIN_GIPHY_IMPAGE_CONTAINER_SCORE = 0.4;
    private static final Timedelta MIN_GIPHY_IMPAGE_CONTAINER_CHANGE_TIMEOUT = Timedelta.fromSeconds(20);

    /**
     * Verify whether current Giphy Image Container differs from the previous one
     *
     * @throws Exception
     * @step. ^I verify the state of Giphy Image Container is changed$
     */
    @Then("^I verify the state of Giphy Image Container is changed$")
    public void ISeeGiphyImageContainerStateChanged() throws Exception {
        Assert.assertTrue("The current and previous state of Giphy Image Container seems to be same",
                giphyImageContainerState.isChanged(MIN_GIPHY_IMPAGE_CONTAINER_CHANGE_TIMEOUT,
                        MIN_GIPHY_IMPAGE_CONTAINER_SCORE));
    }

    private static final double MIN_GIPHY_IMPAGE_PREVIEW_SCORE = 0.55;
    private static final Timedelta MIN_GIPHY_IMPAGE_PREVIEW_CHANGE_TIMEOUT = Timedelta.fromSeconds(20);

    /**
     * Verify whether current Giphy Image Preview differs from the previous one
     *
     * @throws Exception
     * @step. ^I verify the state of Giphy Image Preview is changed$
     */
    @Then("^I verify the state of Giphy Image Preview is changed$")
    public void ISeeGiphyImagePreviewStateChanged() throws Exception {
        Assert.assertTrue("The current and previous state of Giphy Image Preview seems to be same",
                giphyImagePreviewState.isChanged(MIN_GIPHY_IMPAGE_PREVIEW_CHANGE_TIMEOUT,
                        MIN_GIPHY_IMPAGE_PREVIEW_SCORE));
    }

    /**
     * Verify the Giphy Search Field with expected text is visible
     *
     * @param text
     * @throws Exception
     * @step. ^I see Giphy search field with text "(.*)"$
     */
    @Then("^I see Giphy search field with text \"(.*)\"$")
    public void ISeeGiphySearchFieldWithText(String text) throws Exception {
        Assert.assertTrue(String.format("Giphy search field with text '%s' is expected be visible", text),
                getGiphyPreviewPage().waitUntilGiphySearchField(text));
    }
}
