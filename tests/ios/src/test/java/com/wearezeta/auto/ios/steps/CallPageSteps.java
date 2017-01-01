package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.ios.common.IOSPagesCollection;
import com.wearezeta.auto.ios.common.IOSTestContextHolder;
import com.wearezeta.auto.ios.pages.CallingOverlayPage;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallPageSteps {
    private CallingOverlayPage getCallingOverlayPage() throws Exception {
        return IOSTestContextHolder.getInstance().getTestContext().getPagesCollection()
                .getPage(CallingOverlayPage.class);
    }

    /**
     * Verify whether calling overlay is visible or not
     *
     * @param shouldNotBeVisible equals to null if the overlay should be visible
     * @throws Exception
     * @step. ^I (do not )?see Calling overlay$
     */
    @Then("^I (do not )?see Calling overlay$")
    public void ISeeCallingOverlay(String shouldNotBeVisible) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue("Calling overlay is not visible", getCallingOverlayPage().isCallStatusLabelVisible());
        } else {
            Assert.assertTrue("Calling overlay is visible, but should be hidden",
                    getCallingOverlayPage().isCallStatusLabelInvisible());
        }
    }

    /**
     * Tap the corresponding button on calling overlay
     *
     * @param name one of possible button names
     * @throws Exception
     * @step. ^I tap (Ignore|Mute|Leave|Accept|Accept Video|Call Video|Call Speaker|Switch Camera) button on (?:the |\s*)Calling overlay$
     */
    @When("^I tap (Ignore|Mute|Leave|Accept|Accept Video|Call Video|Call Speaker|Switch Camera) button on (?:the |\\s*)Calling overlay$")
    public void ITapButton(String name) throws Exception {
        getCallingOverlayPage().tapButtonByName(name);
    }

    /**
     * Check whether the corresponding button on calling overlay is visible
     *
     * @param shouldNotBeVisible equals to null is the button should not be visible
     * @param name               one of possible button names
     * @throws Exception
     * @step. ^I (do not )?see (Ignore|Mute|Leave|Accept|Accept Video|Call Video|Call Speaker|Switch Camera) button on (?:the |\s*)Calling overlay$
     */
    @Then("^I (do not )?see (Ignore|Mute|Leave|Accept|Accept Video|Call Video|Call Speaker|Switch Camera) button on (?:the |\\s*)Calling overlay$")
    public void ISeeButton(String shouldNotBeVisible, String name) throws Exception {
        if (shouldNotBeVisible == null) {
            Assert.assertTrue(String.format("The '%s' button is not visible on the calling overlay", name),
                    getCallingOverlayPage().isButtonVisible(name));
        } else {
            Assert.assertTrue(String.format(
                    "The '%s' button is visible on the calling overlay, but should be hidden", name),
                    getCallingOverlayPage().isButtonInvisible(name));
        }
    }

    /**
     * Verify that call status message contains the particular text
     *
     * @param text the message to verify. This can contain user names
     * @throws Exception
     * @step. ^I see call status message contains "(.*)"$
     */
    @When("^I see call status message contains \"(.*)\"$")
    public void ISeeCallStatusMessage(String text) throws Exception {
        text = IOSTestContextHolder.getInstance().getTestContext().getUserManager()
                .replaceAliasesOccurences(text, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Call status message containing '%s' is not visible", text),
                getCallingOverlayPage().isCallingMessageContainingVisible(text));
    }

    private static final Timedelta CALL_AVATARS_VISIBILITY_TIMEOUT = Timedelta.fromSeconds(20);

    /**
     * Verifies a number of avatars in the calling overlay
     *
     * @param expectedNumberOfAvatars the expected number of avatars
     * @throws Exception
     * @step. ^I see (\\d+) avatars? in on the Calling overlay$
     */
    @Then("^I see (\\d+) avatars? on the Calling overlay$")
    public void ISeeXAvatars(int expectedNumberOfAvatars) throws Exception {
        Assert.assertTrue(
                String.format("The actual number of calling avatars is not equal to the expected number %s",
                        expectedNumberOfAvatars),
                getCallingOverlayPage().isCountOfAvatarsEqualTo(expectedNumberOfAvatars,
                        CALL_AVATARS_VISIBILITY_TIMEOUT)
        );
    }
}