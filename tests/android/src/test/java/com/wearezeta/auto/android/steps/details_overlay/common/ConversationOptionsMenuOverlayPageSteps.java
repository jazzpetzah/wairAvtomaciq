package com.wearezeta.auto.android.steps.details_overlay.common;

import com.wearezeta.auto.android.pages.details_overlay.common.ConversationOptionsMenuOverlayPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class ConversationOptionsMenuOverlayPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private ConversationOptionsMenuOverlayPage getConversationOptionsMenuOverlayPage() throws Exception {
        return pagesCollection.getPage(ConversationOptionsMenuOverlayPage.class);
    }

    /**
     * Tap the corresponding item in conversation options menu
     *
     * @param itemName menu item name
     * @throws Exception
     * @step. ^I tap (.*) button on Conversation options menu overlay page$
     */
    @When("^I tap (.*) button on Conversation options menu overlay page$")
    public void ITapOptionsMenuItem(String itemName) throws Exception {
        getConversationOptionsMenuOverlayPage().tapOnOptionsMenuItem(itemName);
    }

    /**
     * Verifys the user profile menu item is visible
     *
     * @param itemName menu item name
     * @throws Exception
     * @step. ^I see (.*) button Conversation options menu overlay page$
     */
    @Then("^I see (.*) button on Conversation options menu overlay page$")
    public void ISeeButtonInUserProfileMenuAtPosition(String itemName) throws Exception {
        Assert.assertTrue(String.format("The %s menu item is not visible", itemName),
                getConversationOptionsMenuOverlayPage().waitUntilOptionMenuItemVisible(itemName));
    }

    /**
     * Verify I see the options menu
     *
     * @param shouldNotSee equals null means the options menu should be visible
     * @throws Exception
     * @step. ^I( do not)? see Conversation options menu$
     */
    @Then("^I( do not)? see Conversation options menu$")
    public void ISeeConversationOptionsMenu(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The conversation options menu is not visible",
                    getConversationOptionsMenuOverlayPage().waitUntilOptionMenuVisible());
        } else {
            Assert.assertTrue("The conversation options menu is still visible",
                    getConversationOptionsMenuOverlayPage().waitUntilOptionMenuInvisible());
        }
    }

}
