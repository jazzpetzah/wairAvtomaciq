package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.ios.pages.GiphyPreviewPage;

import cucumber.api.java.en.When;

public class GiphyPreviewPageSteps {

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private GiphyPreviewPage getGiphyPreviewPage() throws Exception {
		return (GiphyPreviewPage) pagesCollecton
				.getPage(GiphyPreviewPage.class);
	}
	
	/**
	 * Sends the gif from preview page 
	 * 
	 * @step. ^I send gif from giphy preview page$
	 * 
	 * @throws Exception
	 */
	@When("^I send gif from giphy preview page$")
	public void ISendGifFromGiphyPreview() throws Exception {
		getGiphyPreviewPage().tapSendGiphyButton();
	}
	
	/**
	 * Verify that all elements on giphy preview page are visible
	 * 
	 * @step. ^I see giphy preview page$
	 * 
	 * @throws Exception
	 */
	@When("^I see giphy preview page$")
	public void ISeeGiphyPreviewPage() throws Exception {
		Assert.assertTrue("Giphy Refresh Button is not visible",
				getGiphyPreviewPage().isGiphyRefreshButtonVisible());
		Assert.assertTrue("Giphy Link Button is not visible",
				getGiphyPreviewPage().isGiphyLinkButtonVisible());
		Assert.assertTrue("Giphy Title Button is not visible",
				getGiphyPreviewPage().isGiphyTitleButtonVisible());
		Assert.assertTrue("Giphy Image is not visible", getGiphyPreviewPage()
				.isGiphyImageVisible());
		Assert.assertTrue("Giphy Reject Button is not visible",
				getGiphyPreviewPage().isGiphyRejectButtonVisible());
		Assert.assertTrue("Giphy Send Button is not visible",
				getGiphyPreviewPage().isGiphySendButtonVisible());
	}
	
	/**
	 * Click on more gifs button
	 * 
	 * @step. ^I click more giphy button$
	 * 
	 * @throws Exception
	 */
	@When("^I click more giphy button$")
	public void IClickMoreGiphyButton() throws Exception {
		getGiphyPreviewPage().clickGiphyMoreButton();
	}

	/**
	 * Verify that giphy grid is shown
	 * 
	 * @step. ^I see giphy grid preview$
	 * 
	 * @throws Exception
	 */
	@When("^I see giphy grid preview$")
	public void ISeeGiphyGridPreview() throws Exception {
		Assert.assertTrue("Giphy grid is not shown", getGiphyPreviewPage()
				.isGiphyGridShown());
	}

}