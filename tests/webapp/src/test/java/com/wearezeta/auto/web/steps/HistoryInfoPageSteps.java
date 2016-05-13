package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.HistoryInfoPage;
import cucumber.api.java.en.Then;

import static org.hamcrest.MatcherAssert.assertThat;

public class HistoryInfoPageSteps {

        private final TestContext context;
        
    public HistoryInfoPageSteps() {
        this.context = new TestContext();
    }

    public HistoryInfoPageSteps(TestContext context) {
        this.context = context;
    }

	@Then("^I see the history info page$")
	public void ISeeConfirmButton() throws Exception {
		assertThat("Confirm button not visible",
				context.getPagesCollection().getPage(HistoryInfoPage.class)
						.isConfirmButtonVisible());
	}

	@Then("^I click confirm on history info page$")
	public void IClickConfirmButton() throws Exception {
		context.getPagesCollection().getPage(HistoryInfoPage.class)
				.clickConfirmButton();
	}
}
