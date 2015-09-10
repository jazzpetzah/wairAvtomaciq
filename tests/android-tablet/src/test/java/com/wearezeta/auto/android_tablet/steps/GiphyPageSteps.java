package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletGiphyPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GiphyPageSteps {
	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletGiphyPage getGiphyPage() throws Exception {
		return (TabletGiphyPage) pagesCollection.getPage(TabletGiphyPage.class);
	}

	/**
	 * Verify whether Giphy page is visible
	 * 
	 * @step. ^I see Giphy [Pp]review page$"
	 * @throws Exception
	 */
	@Then("^I see Giphy [Pp]review page$")
	public void ISeeGiphyPage() throws Exception {
		Assert.assertTrue("Giphy preview page is not shown", getGiphyPage()
				.waitUntilIsVisible());
	}
	
	/**
	 * Tap the corresponding button on Giphy preview page
	 * 
	 * @step. ^I tap Send button on the Giphy [Pp]review page$
	 * 
	 * @throws Exception
	 */
	@When("^I tap Send button on the Giphy [Pp]review page$")
	public void ITapButton() throws Exception {
		getGiphyPage().tapSendButton();
	}
}
