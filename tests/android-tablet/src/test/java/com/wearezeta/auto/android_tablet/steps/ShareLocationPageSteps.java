package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.pages.TabletShareLocationPage;
import cucumber.api.java.en.When;

public class ShareLocationPageSteps {
    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection.getInstance();

    private TabletShareLocationPage getShareLocationPage() throws Exception {
        return pagesCollection.getPage(TabletShareLocationPage.class);
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
