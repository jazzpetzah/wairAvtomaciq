package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.pages.HistoryInfoPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.hamcrest.MatcherAssert.assertThat;

public class HistoryInfoPageSteps {

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	@Then("^I see the history info page$")
	public void ISeeConfirmButton() throws Exception {
		assertThat("Confirm button not visible", webappPagesCollection.getPage(HistoryInfoPage.class)
				.isConfirmButtonVisible());
	}

	@Then("^I click confirm on history info page$")
	public void IClickConfirmButton() throws Exception {
		webappPagesCollection.getPage(HistoryInfoPage.class)
				.clickConfirmButton();
	}
}
