package com.wearezeta.auto.android.steps.details_overlay.group;

import com.wearezeta.auto.android.common.AndroidTestContextHolder;
import com.wearezeta.auto.android.pages.details_overlay.ConversationOptionsMenuPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class GroupConversationOptionMenuPageSteps {
    private ConversationOptionsMenuPage getGroupConversationOptionMenuPage() throws Exception {
        return AndroidTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(ConversationOptionsMenuPage.class);
    }

    /**
     * Verifies that specific items are visible in the conversation settings menu
     *
     * @param name name of item to check in the menu (ARCHIVE, BLOCK, CANCEL,...)
     * @throws Exception
     * @step. ^I( do not)?  see (ARCHIVE|UNARCHIVE|LEAVE|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL) button on
     * Group conversation options menu$
     */
    @Then("^I( do not)? see (ARCHIVE|UNARCHIVE|LEAVE|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL|RENAME) button on " +
            "Group conversation options menu$")
    public void iSeeButtonOnSingleConversationSettingsMenuAtPosition(String shouldNotSee, String name) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("The conversation settings menu '%s' is not visible", name),
                    getGroupConversationOptionMenuPage().waitUntilConversationOptionsMenuItemVisible(name));
        } else {
            Assert.assertTrue(String.format("The conversation settings menu '%s' is not visible", name),
                    getGroupConversationOptionMenuPage().waitUntilConversationOptionsMenuItemInvisible(name));
        }
    }

    /**
     * Tap the corresponding item in conversation settings menu
     *
     * @param itemName menu item name
     * @throws Exception
     * @step. ^I tap (ARCHIVE|UNARCHIVE|LEAVE|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL) button on
     * Group conversation options menu$
     */
    @When("^I tap (ARCHIVE|UNARCHIVE|LEAVE|DELETE|MUTE|UNMUTE|PICTURE|CALL|CANCEL|RENAME) button on " +
            "Group conversation options menu$")
    public void iTapButtonOnSingleConversationOptionMenu(String itemName) throws Exception {
        getGroupConversationOptionMenuPage().tapConversationOptionsMenuItem(itemName);
    }

    /**
     * Checks if information is visible on group conversation option menu page
     *
     * @param shouldNotSee non-null if information should be visible
     * @param value        value to watch
     * @param infoType     type of information to seek for
     * @throws Exception
     * @step. ^I (do not )?see (user name|user info|unique username) "(.*)" on Group conversation options menu$
     */
    @Then("^I (do not )?see (user name|user info|unique username) \"(.*)\" on Group conversation options menu$")
    public void iSeeInfoOnSingleConversationOptionMenu(String shouldNotSee, String infoType, String value) throws
            Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", infoType),
                    getGroupConversationOptionMenuPage().waitUntilUserDataVisible(infoType, value));
            return;
        }
        Assert.assertTrue(String.format("%s should not be visible", infoType),
                getGroupConversationOptionMenuPage().waitUntilUserDataInvisible(infoType, value));
    }

    /**
     * Checks if information is visible on group conversation option menu page
     *
     * @param shouldNotSee non-null if information should be visible
     * @param infoType     type of information to seek for
     * @step. ^I (do not )?see (user name|user info|unique username) on Group conversation options menu
     */
    @Then("^I (do not )?see (user name|user info|unique username) on Group conversation options menu")
    public void iSeeUserDataOnSingleConversationListOptionMenu(String shouldNotSee, String infoType) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue(String.format("%s should be visible", infoType),
                    getGroupConversationOptionMenuPage().waitUntilUserDataVisible(infoType));
            return;
        }
        Assert.assertTrue(String.format("%s should not be visible", infoType),
                getGroupConversationOptionMenuPage().waitUntilUserDataInvisible(infoType));
    }

    /**
     * Verify I see the options menu
     *
     * @param shouldNotSee equals null means the options menu should be visible
     * @throws Exception
     * @step. ^I( do not)? see Group conversation options menu$
     */
    @Then("^I( do not)? see Group conversation options menu$")
    public void ISeeConversationOptionsMenu(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("The conversation options menu is not visible",
                    getGroupConversationOptionMenuPage().waitUntilOptionMenuVisible());
        } else {
            Assert.assertTrue("The conversation options menu is still visible",
                    getGroupConversationOptionMenuPage().waitUntilOptionMenuInvisible());
        }
    }
}
