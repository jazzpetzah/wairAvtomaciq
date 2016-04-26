package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.FirstTimeOverlay;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.junit.Assert;

public class FirstTimeOverlaySteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private FirstTimeOverlay getFirstTimeOverlay() throws Exception {
        return pagesCollection.getPage(FirstTimeOverlay.class);
    }

    /**
     * Verify whether First Time overlay is visible
     *
     * @param shouldNotSee equals to null if the overlay should be visible
     * @throws Exception
     * @step. I (do not )?see First Time overlay$
     */
    @Then("^I (do not )?see First Time overlay$")
    public void ISeeFirstTimeOverlay(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("First time overlay is not visible", getFirstTimeOverlay().waitUntilVisible());
        } else {
            Assert.assertTrue("First time overlay is not visible", getFirstTimeOverlay().waitUntilInvisible());
        }
    }

    /**
     * Wait for a while and accept First Time Usage overlay if it is visisble
     *
     * @throws Exception
     * @step. ^I accept First Time overlay if it is visible
     */
    @And("^I accept First Time overlay if it is visible$")
    public void IAcceptFirstTimeOverlayIfVisible() throws Exception {
        getFirstTimeOverlay().acceptIfVisible(7);
    }

    /**
     * Accept First Time Usage overlay when it is visisble
     *
     * @throws Exception
     * @step. ^I accept First Time overlay$
     */
    @And("^I accept First Time overlay$")
    public void IAcceptFirstTimeOverlay() throws Exception {
        getFirstTimeOverlay().accept();
    }
}