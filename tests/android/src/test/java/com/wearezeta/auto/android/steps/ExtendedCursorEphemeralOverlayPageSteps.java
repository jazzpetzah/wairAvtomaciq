package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.cursor.EphemeralOverlayPage;
import cucumber.api.java.en.When;

public class ExtendedCursorEphemeralOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private EphemeralOverlayPage getExtendedCursorEphemeralOverlayPage() throws Exception {
        return pagesCollection.getPage(EphemeralOverlayPage.class);
    }

    /**
     * Set Ephemeral timeout
     *
     * @param timeoutStr which could be Off, 5 seconds, 15 seconds and 1 minute
     * @throws Exception
     * @step. ^I set timeout to (Off|5 seconds|15 seconds|1 minute|5 minutes) on Extended cursor ephemeral overlay$
     */
    @When("^I set timeout to (Off|5 seconds|15 seconds|1 minute|5 minutes) on Extended cursor ephemeral overlay$")
    public void ISetTimeout(String timeoutStr) throws Exception {
        getExtendedCursorEphemeralOverlayPage().setTimeout(timeoutStr);
    }
}
