package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.Lifecycle;
import com.wearezeta.auto.web.pages.HistoryInfoPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import cucumber.api.java.en.Then;

import static org.hamcrest.MatcherAssert.assertThat;

public class HistoryInfoPageSteps {

	private final WebappPagesCollection webappPagesCollection;
        
        private final Lifecycle.TestContext context;

    public HistoryInfoPageSteps(Lifecycle.TestContext context) {
        this.context = context;
        this.webappPagesCollection = context.getPagesCollection();
    }

	@Then("^I see the history info page$")
	public void ISeeConfirmButton() throws Exception {
		assertThat("Confirm button not visible",
				webappPagesCollection.getPage(HistoryInfoPage.class)
						.isConfirmButtonVisible());
	}

	@Then("^I click confirm on history info page$")
	public void IClickConfirmButton() throws Exception {
		webappPagesCollection.getPage(HistoryInfoPage.class)
				.clickConfirmButton();
	}
}
