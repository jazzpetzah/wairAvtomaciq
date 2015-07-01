package com.wearezeta.auto.android.steps;

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
	 * @step. ^I click on the GIF button$
	 * 
	 * @throws Exception
	 */
	@When("^I click on the GIF button$")
	public void IClickOnTheGifButton() throws Exception {
		getGiphyPreviewPage().clickOnGIFButton();
	}
	
	/**
	 * Check if giphy preview page with all required UI elements appears
	 * 
	 * @step. ^I see giphy preview page$
	 * 
	 * @throws Exception
	 */
	@When("^I see giphy preview page$")
	public void ISeeGiphyPreviewPage() throws Exception {
	Assert.assertTrue("Gipgy preview page is not shown", getGiphyPreviewPage().isGiphyPreviewShown());
	}
	
	/**
	 * Click on SEND button
	 * 
	 * @step. ^I click on the giphy send button$
	 * 
	 * @throws Exception
	 */
	@When("^I click on the giphy send button$")
	public void IClickOnTheGiphySendButton() throws Exception {
		getGiphyPreviewPage().clickeSendButton();
	}
}
