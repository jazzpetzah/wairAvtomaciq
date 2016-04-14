package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.pages.GroupConvoCreationErrorOverlay;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class GroupConvoCreationErrorOverlaySteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private GroupConvoCreationErrorOverlay getConvoCreationErrorOverlay() throws Exception {
        return pagesCollection.getPage(GroupConvoCreationErrorOverlay.class);
    }

    /**
     * Confirms the Conversation Creation Error overlay is visible or not
     *
     * @param shouldNotBeVisible is set to null if "do not" part is not present
     * @throws Exception
     * @step. ^I (do not )?see Unable to Create Group Conversation overlay$"
     */
    @Then("^I (do not )?see Unable to Create Group Conversation overlay$")
    public void ThenISeeOverlay(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Conversation Creation Error overlay is not visible after timeout",
                    getConvoCreationErrorOverlay().isVisible());
        } else {
            Assert.assertTrue("Conversation Creation Error overlay is still visible after timeout",
                    getConvoCreationErrorOverlay().isInvisible());
        }
    }

    /**
     * Tap Manage Devices button
     * <p/>
     * #step. ^I tap Manage Devices button on Manage Devices overlay$
     *
     * @throws Exception
     */
    @When("^I accept Unable to Create Group Conversation overlay$")
    public void IAcceptOverlay() throws Exception {
        getConvoCreationErrorOverlay().tapContinueButton();
    }
}
