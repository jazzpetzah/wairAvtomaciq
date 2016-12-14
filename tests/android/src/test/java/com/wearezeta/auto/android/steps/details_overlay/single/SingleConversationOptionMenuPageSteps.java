package com.wearezeta.auto.android.steps.details_overlay.single;

import com.wearezeta.auto.android.pages.ConversationOptionsMenuPage;
import com.wearezeta.auto.android.steps.AndroidPagesCollection;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
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
     *          single Conversation option menu$
     */
    @Then("^I see (ARCHIVE|UNARCHIVE|BLOCK|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL) button on " +
            "single Conversation option menu$")
    public void iSeeButtonOnSingleConversationSettingsMenuAtPosition(String name) throws Exception {
        Assert.assertTrue("The conversation settings menu item is not visible",
                getSingleConversationOptionMenuPage().isConversationSettingsMenuItemVisible(name));
    }

    /**
     * Tap the corresponding item in conversation settings menu
     *
     * @param itemName menu item name
     * @throws Exception
     * @step. ^I tap (ARCHIVE|UNARCHIVE|BLOCK|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL) button on
     *          single Conversation option menu$
     */
    @And("^I tap (ARCHIVE|UNARCHIVE|BLOCK|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL) button on " +
            "single Conversation option menu$")
    public void iTapButtonOnSingleConversationOptionMenu(String itemName) throws Exception {
        getSingleConversationOptionMenuPage().tapConversationSettingsMenuItem(itemName);
    }

    /**
     * Checks if information is visible on single conversation option menu page
     *
     * @param shouldNotSee non-null if information should be visible
     * @param value        value to watch
     * @param infoType     type of information to seek for
     * @throws Exception
     * @step. ^I (do not )?see (user name|user info|unique username) "(.*)" on single Conversation option menu$
     */
    @And("^I (do not )?see (user name|user info|unique username) \"(.*)\" on single Conversation option menu$")
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
     * @step. ^I (do not )?see (user name|user info|unique username) on single Conversation option menu
     */
    @And("^I (do not )?see (user name|user info|unique username) on single Conversation option menu")
    public void iSeeUserDataOnSingleConversationListOptionMenu(String shouldNotSee, String infoType) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", infoType),
                    getSingleConversationOptionMenuPage().waitUntilUserDataVisible(infoType));
            return;
        }
        Assert.assertTrue(String.format("%s should not be visible", infoType),
                getSingleConversationOptionMenuPage().waitUntilUserDataInvisible(infoType));
    }

}
