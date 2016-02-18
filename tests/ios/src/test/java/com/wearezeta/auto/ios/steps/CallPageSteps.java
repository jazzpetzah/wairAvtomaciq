package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.CallingOverlayPage;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private CallingOverlayPage getCallingOverlayPage() throws Exception {
        return pagesCollection.getPage(CallingOverlayPage.class);
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
        text = usrMgr.replaceAliasesOccurences(text, ClientUsersManager.FindBy.NAME_ALIAS);
        Assert.assertTrue(String.format("Call status message containing '%s' is not visible", text),
                getCallingOverlayPage().isCallingMessageContainingVisible(text));
    }

    /**
     * Verifies that a second call is coming in alert is shown
     *
     * @throws Exception
     * @step. ^I see Accept second call alert$
     */
    @When("^I see Accept second call alert$")
    public void ISeeAcceptSecondCallAlert() throws Exception {
        Assert.assertTrue("Second call Alert is not shown", getCallingOverlayPage().isSecondCallAlertVisible());
    }

    /**
     * Presses the accept button on the second incoming call alert
     *
     * @throws Exception
     * @step. ^I press Accept button on alert$
     */
    @When("^I press Accept button on alert$")
    public void IPressAnswerCallButtonOnAlert() throws Exception {
        getCallingOverlayPage().pressAnswerCallAlertButton();
    }

    private static final int CALL_AVATARS_VISIBILITY_TIMEOUT = 20; //seconds

    /**
     * Verifies a number of avatars in the calling overlay
     *
     * @param expectedNumberOfAvatars the expected number of avatars
     * @throws Exception
     * @step. ^I see (\\d+) avatars? in on the Calling overlay$
     */
    @Then("^I see (\\d+) avatars? on the Calling overlay$")
    public void ISeeXAvatars(int expectedNumberOfAvatars) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        int actualNumberOfAvatars = 0;
        while (System.currentTimeMillis() - millisecondsStarted <= CALL_AVATARS_VISIBILITY_TIMEOUT * 1000) {
            actualNumberOfAvatars = getCallingOverlayPage().getNumberOfParticipantsAvatars();
            if (actualNumberOfAvatars == expectedNumberOfAvatars) {
                return;
            } else if (actualNumberOfAvatars > expectedNumberOfAvatars) {
                break;
            }
            Thread.sleep(1000);
        }
        assert false :
                String.format("The actual number of calling avatars %s is not equal to the expected number %s",
                        actualNumberOfAvatars, expectedNumberOfAvatars);
    }

    /**
     * Verifies that the group call is full message is shown
     *
     * @throws Exception
     * @step. ^I see group call is Full message$
     */
    @Then("^I see group call is Full message$")
    public void ISeeGroupCallIsFullMessage() throws Exception {
        Assert.assertTrue("GROUP CALL IS FULL message is not visible",
                getCallingOverlayPage().isGroupCallFullMessageShown());
    }

}
