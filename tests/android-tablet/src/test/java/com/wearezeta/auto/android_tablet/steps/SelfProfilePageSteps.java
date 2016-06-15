package com.wearezeta.auto.android_tablet.steps;

import java.awt.image.BufferedImage;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletSelfProfilePage;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@Deprecated
public class SelfProfilePageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
            .getInstance();

    private TabletSelfProfilePage getSelfProfilePage() throws Exception {
        return pagesCollection.getPage(TabletSelfProfilePage.class);
    }

    /**
     * Verify that self name is visible on Self Profile page
     *
     * @throws Exception
     * @step. ^I see my name on (?:the |\\s*)[Ss]elf [Pp]rofile page$"
     */
    @Then("^I see my name on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
    public void ISeeMyName() throws Exception {
        final String name = usrMgr.getSelfUserOrThrowError().getName();
        Assert.assertTrue(String.format(
                "Self name '%s' is not visible on Self Profile page", name),
                getSelfProfilePage().isNameVisible(name));
    }

    /**
     * Tap Options button on Self Profile page
     *
     * @throws Exception
     * @step. ^I tap Options button on (?:the |\\s*)[Ss]elf [Pp]rofile page$"
     */
    @When("^I tap Options button on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
    public void ITapOptionsButton() throws Exception {
        getSelfProfilePage().tapOptionsButton();
    }

    /**
     * Select the corresponding item from Options menu
     *
     * @param itemName the name of existing Options menu item
     * @throws Exception
     * @step. ^I select \"(.*)\" menu item on (?:the |\\s*)[Ss]elf [Pp]rofile
     * page$
     */
    @When("^I select \"(.*)\" menu item on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
    public void ISelectOptionsMenuItem(String itemName) throws Exception {
        getSelfProfilePage().selectOptionsMenuItem(itemName);
    }

    /**
     * Verify whether the corresponding item from Options menu is visible
     *
     * @param itemName the name of an menu item
     * @throws Exception
     * @step. ^I see \"(.*)\" menu item on (?:the |\\s*)[Ss]elf [Pp]rofile page$
     */
    @When("^I see \"(.*)\" menu item on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
    public void ISeeOptionsMenuItem(String itemName) throws Exception {
        Assert.assertTrue(
                String.format("'%s' menu item is not visible", itemName),
                getSelfProfilePage().waitUntilOptionsMenuItemVisible(itemName));
    }

    /**
     * Tap the self name field on Self Profile page
     *
     * @throws Exception
     * @step. ^I tap my name field on (?:the |\\s*)[Ss]elf [Pp]rofile page$
     */
    @When("^I tap my name field on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
    public void ITapMyName() throws Exception {
        getSelfProfilePage().tapSelfNameField();
    }

    /**
     * Change self name to the passed one
     *
     * @param newName a new name
     * @throws Exception
     * @step. ^I change my name to (.*) on (?:the |\\s*)[Ss]elf [Pp]rofile page$
     */
    @And("^I change my name to (.*) on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
    public void IChangeMyNameTo(String newName) throws Exception {
        getSelfProfilePage().changeSelfNameTo(newName);
        usrMgr.getSelfUserOrThrowError().setName(newName);
    }

    /**
     * Tap in the center of the screen on Self Profile page to switch to full
     * color view
     *
     * @throws Exception
     * @step. ^I tap in the center of [Ss]elf [Pp]rofile page$
     */
    @And("^I tap in the center of [Ss]elf [Pp]rofile page$")
    public void ITapInTheCenter() throws Exception {
        getSelfProfilePage().tapInTheCenter();
    }

    private BufferedImage savedProfileScreenshot;

    /**
     * Stores the screenshot of current Self Profile screen for the further
     * comparison
     *
     * @throws Exception
     * @step. ^I remember my current profile picture on [Ss]elf [Pp]rofile page$
     */
    @When("^I remember my current profile picture on [Ss]elf [Pp]rofile page$")
    public void IRememberMyCurrentProfilePictureTablet() throws Exception {
        savedProfileScreenshot = getSelfProfilePage().getScreenshot();
    }

    private static final double MAX_SCREENSHOTS_OVERLAP_SCORE = 0.55;
    private static final long PROFILE_PICTURE_UPDATE_TIMEOUT_MILLISECONDS = 10000;

    /**
     * Compare the previous Self Profile page screenshot with the current one to
     * calculate the overlap score
     *
     * @throws Exception
     * @step. ^I verify that my current profile picture is different from the
     * previous one$
     */
    @Then("^I verify that my current profile picture is different from the previous one$")
    public void IVerifyMyPictureDiffersFromThePreviousOne() throws Exception {
        if (savedProfileScreenshot == null) {
            throw new IllegalStateException("Please take a screenshot of the previous page state first");
        }
        final long millisecondsStarted = System.currentTimeMillis();
        double overlapScore = 1;
        do {
            final BufferedImage currentProfileScreenshot = getSelfProfilePage().getScreenshot();
            overlapScore = ImageUtil.getOverlapScore(currentProfileScreenshot,
                    savedProfileScreenshot, ImageUtil.RESIZE_NORESIZE);
            if (overlapScore <= MAX_SCREENSHOTS_OVERLAP_SCORE) {
                break;
            }
        } while (System.currentTimeMillis() - millisecondsStarted <= PROFILE_PICTURE_UPDATE_TIMEOUT_MILLISECONDS);
        Assert.assertTrue(String.format(
                "The overlap value between the previous and the current profile picture is greater than expected (%.2f > %.02f)",
                overlapScore, MAX_SCREENSHOTS_OVERLAP_SCORE), overlapScore <= MAX_SCREENSHOTS_OVERLAP_SCORE);
    }

    /**
     * Verify whether chathead notification is visible
     *
     * @param shouldNotSee equals to null if the notification should be visible
     * @throws Exception
     * @step. ^I (do not )?see chathead notification$
     */
    @Then("^I (do not )?see chathead notification$")
    public void ISeeChatheadNotification(String shouldNotSee) throws Exception {
        if (shouldNotSee == null) {
            Assert.assertTrue("Chathead notification is not visible",
                    getSelfProfilePage().waitForChatheadNotification().isPresent());
        } else {
            Assert.assertTrue("Chathead notification is still visible",
                    getSelfProfilePage().waitUntilChatheadNotificationInvisible());
        }
    }

    /**
     * Tap chathead notification as soon as it appears on the screen
     *
     * @throws Exception
     * @step. ^I tap the chathead$
     */
    @And("^I tap the chathead notification$")
    public void ITapChathead() throws Exception {
        getSelfProfilePage().tapChatheadNotification();
    }
}
