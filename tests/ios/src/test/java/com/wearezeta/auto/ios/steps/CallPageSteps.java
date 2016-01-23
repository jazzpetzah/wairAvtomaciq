package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.IncomingCallPage;
import com.wearezeta.auto.ios.pages.StartedCallPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallPageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollecton = IOSPagesCollection
            .getInstance();

    private StartedCallPage getStartedCallPage() throws Exception {
        return pagesCollecton.getPage(StartedCallPage.class);
    }

    private IncomingCallPage getIncomingCallPage() throws Exception {
        return pagesCollecton.getPage(IncomingCallPage.class);
    }

    /**
     * Verify that calling UI is visible
     *
     * @param contact User name whom we call
     * @throws Exception
     * @step. ^I see calling to contact (.*) message$
     */
    @When("^I see calling to contact (.*) message$")
    public void ISeeCallingMesage(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(getStartedCallPage().isCallingMessageVisible());
    }

    /**
     * Verify that calling UI buttons are visible
     *
     * @throws Exception
     * @step. ^I see mute call, end call and speakers buttons$
     */
    @When("^I see mute call, end call and speakers buttons$")
    public void ISeeCallingPageButtons() throws Exception {
        try {
            Assert.assertTrue("End call button is not visible",
                    getStartedCallPage().isEndCallVisible());
            Assert.assertTrue("Mute call button is not visible",
                    getStartedCallPage().isMuteCallVisible());
            Assert.assertTrue("Speakers button is not visible",
                    getStartedCallPage().isSpeakersVisible());
        } catch (AssertionError e) {
            // For debug purposes
            getStartedCallPage().printPageSource();
            throw e;
        }
    }

    /**
     * Verify that calling UI buttons are visible (using it for iPad
     * verification step as far speakers button is not shown there)
     *
     * @throws Exception
     * @step. ^I see mute call, end call buttons$
     */
    @When("^I see mute call, end call buttons$")
    public void ISeeCallingPageButtonsOnIpad() throws Exception {
        Assert.assertTrue("End call button is not visible",
                (getStartedCallPage().isEndCallVisible()));
        Assert.assertTrue("Mute call button is not visible",
                (getStartedCallPage().isMuteCallVisible()));
    }

    /**
     * Click on end call button
     *
     * @throws Exception
     * @step. ^I end started call$
     */
    @When("^I end started call$")
    public void IEndStartedCall() throws Exception {
        getStartedCallPage().clickEndCallButton();
    }

    /**
     * Verify that calling page is not visible
     *
     * @throws Exception
     * @step. ^I dont see calling page$
     */
    @When("^I dont see calling page$")
    public void IDontSeeCallPage() throws Exception {
        Assert.assertTrue("Calling bar is visible", getStartedCallPage()
                .waitCallingMessageDisappear());
    }

    /**
     * Verify that incoming call page is not visible
     *
     * @throws Exception
     * @step. ^I dont see incoming call page$
     */
    @When("^I dont see incoming call page$")
    public void IDontSeeIncomingCallPage() throws Exception {
        Assert.assertTrue("Calling bar is still visible",
                getIncomingCallPage().isCallingMessageInvisible());
    }

    /**
     * Verify that incoming calling UI is visible
     *
     * @param contact User name who calls
     * @throws Exception
     * @step. ^I see incoming calling message for contact (.*)$
     */
    @When("^I see incoming calling message for contact (.*)$")
    public void ISeeIncomingCallingMessage(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(String.format("Calling message for '%s' is not visible", contact),
                getIncomingCallPage().isCallingMessageVisible(contact));
    }

    /**
     * Click on ignore call button
     *
     * @throws Exception
     * @step. ^I ignore incoming call$
     */
    @When("^I ignore incoming call$")
    public void IignoreIncomingCall() throws Exception {
        getIncomingCallPage().ignoreIncomingCallClick();
    }

    /**
     * Accept incoming call by clicking accept button
     *
     * @throws Exception
     * @step. ^I accept incoming call$
     */
    @When("^I accept incoming call$")
    public void IAcceptIncomingCall() throws Exception {
        getIncomingCallPage().acceptIncomingCallClick();
    }

    /**
     * Verify that started call message is visible
     *
     * @param contact contact name with whom you have a call
     * @throws Exception
     * @step. ^I see started call message for contact (.*)$
     */
    @When("^I see started call message for contact (.*)$")
    public void ISeeStartedCallMesage(String contact) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(getStartedCallPage().isStartedCallMessageVisible(
                contact.toUpperCase()));
    }

    /**
     * Verify is mute call button icon selected or not
     *
     * @throws Exception
     * @step. ^I see mute call button on calling bar is selected$
     */
    @When("^I see mute call button on calling bar is selected$")
    public void ISeeMuteCallButtonOnCallingBarIsSelected() throws Exception {
        Assert.assertTrue("Mute call button is not selected",
                getStartedCallPage().isMuteCallButtonSelected());
    }

    /**
     * Verify that incoming group calling UI is visible
     *
     * @throws Exception
     * @step. ^I see incoming group calling message$
     */
    @When("^I see incoming group calling message$")
    public void ISeeIncomingGroupCallingMessage() throws Exception {
        Assert.assertTrue(getIncomingCallPage().isGroupCallingMessageVisible());
    }

    /**
     * Verifies the visibility of the Join Call bar
     *
     * @throws Exception
     * @step. ^I see Join Call bar$
     */
    @Then("^I see Join Call bar$")
    public void ISeeJoinCallBar() throws Exception {
        boolean joinCallBarIsVisible = getIncomingCallPage()
                .isJoinCallBarVisible();
        Assert.assertTrue("Join Call bar is not visible", joinCallBarIsVisible);
    }

    /**
     * Verifies that a second call is coming in alert is shown
     *
     * @throws Exception
     * @step. ^I see Accept second call alert$
     */
    @When("^I see Accept second call alert$")
    public void ISeeAcceptSecondCallAlert() throws Exception {
        boolean secondCallAlertIsVisible = getIncomingCallPage()
                .isSecondCallAlertVisible();
        Assert.assertTrue("Second call Alert is not shown",
                secondCallAlertIsVisible);
    }

    /**
     * Presses the accept button on the second incoming call alert
     *
     * @throws Exception
     * @step. ^I press Accept button on alert$
     */
    @When("^I press Accept button on alert$")
    public void IPressAnswerCallButtonOnAlert() throws Exception {
        getIncomingCallPage().pressAnswerCallAlertButton();
    }

    private static final int CALL_AVATARS_VISIBILITY_TIMEOUT = 20; //seconds

    /**
     * Verifies a number of avatars in the group call bar
     *
     * @param expectedNumberOfAvatars the expected number of avatars in group call bar
     * @throws Exception
     * @step. ^I see (\\d+) avatars in the group call bar$
     */
    @Then("^I see (\\d+) avatars in the group call bar$")
    public void ISeeAvatarsInTheGroupCallBar(int expectedNumberOfAvatars) throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        int actualNumberOfAvatars = 0;
        while (System.currentTimeMillis() - millisecondsStarted <= CALL_AVATARS_VISIBILITY_TIMEOUT * 1000) {
            actualNumberOfAvatars = getIncomingCallPage().getNumberOfGroupCallAvatar();
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
     * Rejoin group call by clicking the join button
     *
     * @throws Exception
     * @step. ^I rejoin call by clicking Join button$
     */
    @When("^I rejoin call by clicking Join button$")
    public void IRejoinCallByClickingJoinButton() throws Exception {
        getIncomingCallPage().clickJoinCallButton();
    }

    /**
     * Verifies the calling to a group call message
     *
     * @throws Exception
     * @step. ^I see calling to a group message$
     */
    @Then("^I see calling to a group message$")
    public void ISeeCallingToMessage() throws Exception {
        Assert.assertTrue(getStartedCallPage().isCallingMessageVisible());
    }

    /**
     * Verifies that the group call is full message is shown
     *
     * @throws Exception
     * @step. ^I see group call is Full message$
     */
    @Then("^I see group call is Full message$")
    public void ISeeGroupCallIsFullMessage() throws Exception {
        Assert.assertTrue(getIncomingCallPage().isGroupCallFullMessageShown());
    }

}
