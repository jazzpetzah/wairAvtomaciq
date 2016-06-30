package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.ShareLocationPage;
import cucumber.api.java.en.When;

public class ShareLocationPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private ShareLocationPage getShareLocationPage() throws Exception {
        return pagesCollection.getPage(ShareLocationPage.class);
    }

    /**
     * Tap the corresponding button on Share Location page
     *
     * @param buttonName on of possible button names
     * @throws Exception
     * @step. ^I tap (Send) button on Share Location page$
     */
    @When("^I tap (Send) button on Share Location page$")
    public void ThenISeeFirstTimeOverlay(String buttonName) throws Exception {
        getShareLocationPage().tapButton(buttonName);
    }
}
