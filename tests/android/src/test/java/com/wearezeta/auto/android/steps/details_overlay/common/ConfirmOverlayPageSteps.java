package com.wearezeta.auto.android.steps.details_overlay.common;

import com.wearezeta.auto.android.pages.details_overlay.common.ConfirmOverlayPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import cucumber.api.java.en.When;

public class ConfirmOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private ConfirmOverlayPage getConfirmOverlayPage() throws Exception {
        return pagesCollection.getPage(ConfirmOverlayPage.class);
    }

    /**
     * Tap the corresponding button
     *
     * @param itemName button name
     * @throws Exception
     * @step. ^I tap (DELETE|REMOVE|LEAVE|BLOCK|CANCEL) button on Confirm overlay page$
     */
    @When("^I tap (DELETE|REMOVE|LEAVE|BLOCK|CANCEL) button on Confirm overlay page$")
    public void ITapOptionsMenuItem(String itemName) throws Exception {
        getConfirmOverlayPage().tapOnButton(itemName);
    }

}
