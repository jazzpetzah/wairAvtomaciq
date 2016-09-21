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
	 * Type text in Giphy Toolbar Input field
	 *
	 * @param text
	 * @param hideKeyboard equals null means do not hide keyboard
	 * @throws Exception
	 * @step. ^I type "(.*)" in Giphy toolbar input field$
     */
	@When("^I type \"(.*)\" in Giphy toolbar input field( and hide keyboard)?$")
	public void ISearchGiphy(String text, String hideKeyboard) throws Exception {
		getGiphyPreviewPage().typeTextOnSearchField(text,hideKeyboard != null);
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
}