package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.ExtendedCursorEphemeralOverlayPage;
import cucumber.api.java.en.When;

public class ExtendedCursorEphemeralOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private ExtendedCursorEphemeralOverlayPage getExtendedCursorEphemeralOverlayPage() throws Exception {
        return pagesCollection.getPage(ExtendedCursorEphemeralOverlayPage.class);
    }

    /**
     * Set Ephemeral timeout
     *
     * @param timeoutStr which could be Off, 5 seconds, 15 seconds and 1 minute
     * @throws Exception
     * @step. ^I set timeout to (Off|5 seconds|15 seconds|1 minute) on Extended cursor ephemeral overlay$
     */
    @When("^I set timeout to (Off|5 seconds|15 seconds|1 minute) on Extended cursor ephemeral overlay$")
    public void ISetTimeout(String timeoutStr) throws Exception {
        getExtendedCursorEphemeralOverlayPage().setTimeout(timeoutStr);
    }
}
