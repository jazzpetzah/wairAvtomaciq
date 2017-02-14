package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.web.common.WebAppTestContext;
import com.wearezeta.auto.web.pages.ContentSearchPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class ContentSearchPageSteps {

    private final WebAppTestContext context;

    public ContentSearchPageSteps(WebAppTestContext context) {
        this.context = context;
    }

    @Then("^I see (?:(\\d+) )?search results?$")
    public void ISeeResults(Integer number) throws Exception {
        if (number != null) {
            assertThat("Found results",
                    context.getPagesCollection().getPage(ContentSearchPage.class).waitForPresentResults(),
                    is(equalTo(number)));
        } else {
            assertThat("Found results",
                    context.getPagesCollection().getPage(ContentSearchPage.class).waitForPresentResults(),
                    is(greaterThan(0)));
        }

    }

    @Then("^I (do not )?see search result with text (.*)$")
    public void ISeeResultWithText(String doNot, String resultText) throws Exception {
        if (doNot == null) {
            assertThat("Found result",
                    context.getPagesCollection().getPage(ContentSearchPage.class)
                            .waitForVisibilityOfResultContainingText(resultText),
                    is(true));
        } else {
            assertThat("Found result",
                    context.getPagesCollection().getPage(ContentSearchPage.class)
                            .waitForInvisibilityOfResultContainingText(resultText),
                    is(true));
        }

    }

    @When("^I click search result with text (.*)$")
    public void IClickResultWithText(String resultText) throws Exception {
        context.getPagesCollection().getPage(ContentSearchPage.class).clickResultWithText(resultText);
    }

}
