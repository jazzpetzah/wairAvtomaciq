package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.ShareContactsOverlay;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class ShareContactsOverlaySteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private ShareContactsOverlay getShareContactsOverlay() throws Exception {
        return pagesCollection.getPage(ShareContactsOverlay.class);
    }

    /**
     * Verify whether Share contacts overlay is visible
     *
     * @param shouldNotSee equals to null if the overlay should be visible
     * @throws Exception
     * @step. I (do not )?see Share Contacts overlay$
     */
    @Then("^I (do not )?see Share Contact overlay$")
    public void ISeeShareContactsOverlay(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Share Contacts overlay is not visible", getShareContactsOverlay().waitUntiVisible());
        } else {
            Assert.assertTrue("Share Contacts overlay is not visible", getShareContactsOverlay().waitUntiInvisible());
        }
    }

    /**
     * Tap the corresponding button on the overlay
     *
     * @param btnName one of possible button names
     * @throws Exception
     * @step. ^I tap (Share Contacts|Not Now) button on Share Contacts overlay$
     */
    @When("^I tap (Share Contacts|Not Now) button on Share Contacts overlay$")
    public void TTapShareContactsButton(String btnName) throws Exception {
        switch (btnName) {
            case "Share Contacts":
                getShareContactsOverlay().tapShareContactsButton();
                break;
            case "Not Now":
                getShareContactsOverlay().tapNotNowButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", btnName));
        }
    }
}