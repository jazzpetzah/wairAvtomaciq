package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.GiphyPreviewPage;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.When;

public class GiphySteps {
	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
		.getInstance();

	private GiphyPreviewPage getGiphyPreviewPage() throws Exception {
		return (GiphyPreviewPage) pagesCollection.getPage(GiphyPreviewPage.class);
	}
	
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
	/**
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
	 * 
	 * @step. ^I click on the giphy send button$
	 * 
	 * @throws Exception
	 */
	@When("^I click on the giphy send button$")
	public void IClickOnTheGiphySendButton() throws Exception {
		getGiphyPreviewPage().clickeSendButton();
	}
	
	/**
	 * 
	 * @step. ^I see a gif in the conversation view$
	 * 
	 * @throws Exception
	 */
	@When("^I see the via giphy dotcom message$")
	public void ISeeAGifInTheConversationView() throws Exception {
		//TODO
	}
}
