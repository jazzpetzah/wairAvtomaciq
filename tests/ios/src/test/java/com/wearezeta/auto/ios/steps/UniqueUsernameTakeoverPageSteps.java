package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.UniqueUsernameTakeoverPage;
import cucumber.api.java.en.When;


public class UniqueUsernameTakeoverPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private UniqueUsernameTakeoverPage getPage() throws Exception {
        return pagesCollection.getPage(UniqueUsernameTakeoverPage.class);
    }

    /**
     * Tap the corresponding button щn Unique Username Takeover page$
     *
     * @param buttonName one of possible button names
     * @throws Exception
     * @step. ^I tap (Choose Yours|Keep This One) button on Unique Username Takeover page$
     */
    @When("^I tap (Choose Yours|Keep This One) button on Unique Username Takeover page$")
    public void ITapButton(String buttonName) throws Exception {
        getPage().tapButton(buttonName);
    }
}
