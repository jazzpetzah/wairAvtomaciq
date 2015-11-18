package com.wearezeta.auto.android.steps;

import cucumber.api.java.en.And;
import org.junit.Assert;

import com.wearezeta.auto.android.pages.GiphyPreviewPage;

import cucumber.api.java.en.When;

public class GiphySteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();

	private GiphyPreviewPage getGiphyPreviewPage() throws Exception {
		return (GiphyPreviewPage) pagesCollection.getPage(GiphyPreviewPage.class);
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
		Assert.assertTrue("Gipgy preview page is not shown", getGiphyPreviewPage().isGiphyPreviewShown());
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

	/**
	 * Select the first item from Giphy grid
	 *
	 * @step. ^I select the first item in giphy grid$
	 *
	 * @throws Exception
     */
	@And("^I select the first item in giphy grid$")
	public void ISelectTheFirstGridItem() throws Exception {
		getGiphyPreviewPage().selectFirstGridItem();
	}
}