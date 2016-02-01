package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.FirstTimeOverlay;
import cucumber.api.java.en.And;

public class FirstTimeOverlaySteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private FirstTimeOverlay getFirstTimeOverlay() throws Exception {
        return pagesCollection.getPage(FirstTimeOverlay.class);
    }

    /**
     * Wait for a while and accept First Time Usage overlay if it is viisble
     *
     * @throws Exception
     * @step. ^I accept First Time overlay if it is visible
     */
    @And("^I accept First Time overlay if it is visible")
    public void IAcceptFirstTimeOverlayIfVisible() throws Exception {
        getFirstTimeOverlay().acceptIfVisible(5);
    }
}