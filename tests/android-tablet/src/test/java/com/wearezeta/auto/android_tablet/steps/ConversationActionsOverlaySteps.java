package com.wearezeta.auto.android_tablet.steps;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.popovers.ConversationActionsOverlay;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;

public class ConversationActionsOverlaySteps {
    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();

    private ConversationActionsOverlay getConversationActionsOverlay()
            throws Exception {
        return pagesCollection
                .getPage(ConversationActionsOverlay.class);
    }

    /**
     * Verify whether Conversation Actions overlay is visible or not
     *
     * @param shouldNotBeVisible equals to null is "no not" part does not exist in the step
     * @throws Exception
     * @step. ^I( do not)? see Conversation Actions overlay$
     */
    @Then("^I( do not)? see Conversation Actions overlay$")
    public void ISeeOverlay(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Conversation Actions overlay is not visible",
                    getConversationActionsOverlay().waitUntilVisible());
        } else {
            Assert.assertTrue("Conversation Actions overlay is still visible",
                    getConversationActionsOverlay().waitUntilInvisible());
        }
    }

    /**
     * Select the appropriate menu item on the overlay
     *
     * @param itemName menu item name
     * @throws Exception
     * @step. ^I select (.*) menu item on Conversation Actions overlay$
     */
    @When("^I select (.*) menu item on Conversation Actions overlay$")
    public void ISelectMenuItem(String itemName) throws Exception {
        getConversationActionsOverlay().selectMenuItem(itemName);
    }

    /**
     * Confirm the alert about conversation deletion
     *
     * @throws Exception
     * @step.^I confirm conversation deletion on Conversation Actions overlay$
     */
    @And("^I confirm conversation deletion on Conversation Actions overlay$")
    public void IConfirmConversationDeletion() throws Exception {
        getConversationActionsOverlay().confirmDeleteConversationAlert();
    }
}
