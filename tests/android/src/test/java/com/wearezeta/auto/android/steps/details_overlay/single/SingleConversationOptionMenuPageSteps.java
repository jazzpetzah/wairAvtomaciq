package com.wearezeta.auto.android.steps.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.ConversationOptionsMenuPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class SingleConversationOptionMenuPageSteps {

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private ConversationOptionsMenuPage getSingleConversationOptionMenuPage() throws Exception {
        return pagesCollection.getPage(ConversationOptionsMenuPage.class);
    }

    /**
     * Verifies that specific items are visible in the conversation settings menu
     *
     * @param name name of item to check in the menu (ARCHIVE, BLOCK, CANCEL,...)
     * @throws Exception
     * @step. ^I see (ARCHIVE|UNARCHIVE|BLOCK|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL) button on
     *          Single conversation options menu$
     */
    @Then("^I see (ARCHIVE|UNARCHIVE|BLOCK|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL) button on " +
            "Single conversation options menu$")
    public void iSeeButtonOnSingleConversationSettingsMenuAtPosition(String name) throws Exception {
        Assert.assertTrue("The conversation settings menu item is not visible",
                getSingleConversationOptionMenuPage().waitUntilConversationOptionsMenuItemVisible(name));
    }

    /**
     * Tap the corresponding item in conversation settings menu
     *
     * @param itemName menu item name
     * @throws Exception
     * @step. ^I tap (ARCHIVE|UNARCHIVE|BLOCK|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL) button on
     *          single Conversation options menu$
     */
    @When("^I tap (ARCHIVE|UNARCHIVE|BLOCK|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL) button on " +
            "Single conversation options menu$")
    public void iTapButtonOnSingleConversationOptionMenu(String itemName) throws Exception {
        getSingleConversationOptionMenuPage().tapConversationOptionsMenuItem(itemName);
    }

    /**
     * Checks if information is visible on single conversation option menu page
     *
     * @param shouldNotSee non-null if information should be visible
     * @param value        value to watch
     * @param infoType     type of information to seek for
     * @throws Exception
     * @step. ^I (do not )?see (user name|user info|unique username) "(.*)" on Single conversation options menu$
     */
    @Then("^I (do not )?see (user name|user info|unique username) \"(.*)\" on Single conversation options menu$")
    public void iSeeInfoOnSingleConversationOptionMenu(String shouldNotSee, String infoType, String value) throws
            Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", infoType),
                    getSingleConversationOptionMenuPage().waitUntilUserDataVisible(infoType, value));
            return;
        }
        Assert.assertTrue(String.format("%s should not be visible", infoType),
                getSingleConversationOptionMenuPage().waitUntilUserDataInvisible(infoType, value));
    }

    /**
     * Checks if information is visible on single conversation option menu page
     *
     * @param shouldNotSee non-null if information should be visible
     * @param infoType     type of information to seek for
     * @step. ^I (do not )?see (user name|user info|unique username) on Single conversation options menu
     */
    @Then("^I (do not )?see (user name|user info|unique username) on Single conversation options menu")
    public void iSeeUserDataOnSingleConversationListOptionMenu(String shouldNotSee, String infoType) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", infoType),
                    getSingleConversationOptionMenuPage().waitUntilUserDataVisible(infoType));
            return;
        }
        Assert.assertTrue(String.format("%s should not be visible", infoType),
                getSingleConversationOptionMenuPage().waitUntilUserDataInvisible(infoType));
    }

    /**
     * Verify I see the options menu
     *
     * @param shouldNotSee equals null means the options menu should be visible
     * @throws Exception
     * @step. ^I( do not)? see Single conversation options menu$
     */
    @Then("^I( do not)? see Single conversation options menu$")
    public void ISeeConversationOptionsMenu(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The conversation options menu is invisible",
                    getSingleConversationOptionMenuPage().waitUntilOptionMenuVisible());
        } else {
            Assert.assertTrue("The conversation options menu is still visible",
                    getSingleConversationOptionMenuPage().waitUntilOptionMenuInvisible());
        }
    }
}
