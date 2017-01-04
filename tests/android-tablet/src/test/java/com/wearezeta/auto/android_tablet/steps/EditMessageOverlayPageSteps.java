package com.wearezeta.auto.android_tablet.steps;

import com.wearezeta.auto.android_tablet.common.AndroidTabletTestContextHolder;
import com.wearezeta.auto.android_tablet.pages.TabletEditMessageOverlayPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class EditMessageOverlayPageSteps {
    private TabletEditMessageOverlayPage getEditMessageOverlayPage() throws Exception {
        return AndroidTabletTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(TabletEditMessageOverlayPage.class);
    }

    /**
     * Tap button on Edit Message Toolbar
     *
     * @param btnName button name which could be Reset, Approve and Close
     * @throws Exception
     * @step. ^I tap (Reset|Approve|Close) button in edit message toolbar$
     */
    @When("^I tap (Reset|Approve|Close) button in edit message toolbar$")
    public void ITapButton(String btnName) throws Exception {
        getEditMessageOverlayPage().tapOnButton(btnName);
    }

    /**
     * Verify the edit message toolbar is visible
     *
     * @param shouldNotSee equals null means the edit message toolbar should be visible
     * @throws Exception
     * @step. ^I see edit message toolbar$
     */
    @Then("^I (do not )?see edit message toolbar$")
    public void ISeeEditMessageToolbar(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The edit message toolbar should be visible",
                    getEditMessageOverlayPage().waitUntilVisible());
        } else {
            Assert.assertTrue("The edit message toolbar should be invislble",
                    getEditMessageOverlayPage().waitUntilInvisible());
        }
    }
}
