package com.wearezeta.auto.ios.steps;

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

}