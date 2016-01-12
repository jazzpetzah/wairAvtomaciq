package com.wearezeta.auto.android.steps;

import cucumber.api.java.en.And;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.GiphyPreviewPage;

import cucumber.api.java.en.When;

public class GiphySteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private GiphyPreviewPage getGiphyPreviewPage() throws Exception {
		return pagesCollection.getPage(GiphyPreviewPage.class);
	}

	/**
	 * Click on GIF button
	 *
	 * @throws Exception
	 * @step. ^I click on the GIF button$
	 */
	@When("^I click on the GIF button$")
	public void IClickOnTheGifButton() throws Exception {
		getGiphyPreviewPage().clickOnGIFButton();
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
	 * Clicks the giphy link button to open the grid preview
	 *
	 * @step. ^I click on the giphy link button$
	 *
	 * @throws Exception
	 */
	@When("^I click on the giphy link button$")
	public void IClickOnTheGifPreview() throws Exception {
		getGiphyPreviewPage().clickGiphyLinkButton();
	}

	/**
	 * Checks to see that the grid preview is visible
	 *
	 * @step. ^I see the giphy grid preview$
	 *
	 * @throws Exception
	 */
	@When("^I see the giphy grid preview$")
	public void ISeeTheGiphyGridPreview() throws Exception {
		Assert.assertTrue("Giphy Grid view is not shown", getGiphyPreviewPage().isGiphyGridViewShown());
	}

	/**
	 * Selects one of the gifs from the grid preview
	 *
	 * @step. ^I select a random gif from the grid preview$
	 *
	 * @throws Exception
	 */
	@When("^I select a random gif from the grid preview$")
	public void ISelectARandomGifFromTheGridPreview() throws Exception {
		getGiphyPreviewPage().clickOnSomeGif();
	}

	/**
	 * Click on SEND button
	 *
	 * @throws Exception
	 * @step. ^I click on the giphy send button$
	 */
	@When("^I click on the giphy send button$")
	public void IClickOnTheGiphySendButton() throws Exception {
		getGiphyPreviewPage().clickSendButton();
	}

	/**
	 * Click Show Giphy Grid button
	 *
	 * @throws Exception
	 * @step. ^I click Show giphy grid button$
	 */
	@When("^I click Show giphy grid button$")
	public void IClickShowGiphyGridButton() throws Exception {
		getGiphyPreviewPage().clickShowGiphyGridButton();
	}
}