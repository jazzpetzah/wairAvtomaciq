package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CallKitOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class CallKitPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private CallKitOverlayPage getPage() throws Exception {
        return pagesCollection.getPage(CallKitOverlayPage.class);
    }

    /**
     * Verify whether Call Kit overlay is visible or not
     *
     * @param shouldNotBeVisible equals to null if the overlay should be visible
     * @param overlayType        either Audio or Video
     * @throws Exception
     * @step. ^I (do not )?see (Video|Audio) Call Kit overlay$
     */
    @Then("^I (do not )?see (Video|Audio) Call Kit overlay$")
    public void ISeeOverlay(String shouldNotBeVisible, String overlayType) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(String.format("No %s Call Kit overlay is visible", overlayType),
                    getPage().isVisible(overlayType));
        } else {
            Assert.assertTrue(String.format("%s Call Kit overlay is visible, but should be hidden", overlayType),
                    getPage().isInvisible(overlayType));
        }
    }

    /**
     * Tap the corresponding button on Call Kit overlay
     *
     * @param btnName one of available button names
     * @throws Exception
     * @step. ^I tap (Accept|Decline|Message|Remind Me) button on Call Kit overlay$
     */
    @When("^I tap (Accept|Decline|Message|Remind Me) button on Call Kit overlay$")
    public void ITapButton(String btnName) throws Exception {
        getPage().tapButton(btnName);
    }
}