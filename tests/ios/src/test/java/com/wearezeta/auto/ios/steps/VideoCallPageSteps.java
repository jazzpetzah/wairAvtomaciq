package com.wearezeta.auto.ios.steps;


import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.VideoCallPage;
import cucumber.api.java.en.*;
import org.junit.Assert;

public class VideoCallPageSteps {

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private VideoCallPage getVideoCallingPage() throws Exception {
        return pagesCollection.getPage(VideoCallPage.class);
    }

    /**
     * Verify that call status label with correct user name is shown
     *
     * @param contact   user name
     * @param callState Calling status (ringing/calling)
     * @throws Exception
     * @step. ^I see user (.*) (ringing|calling) label on Video Call page$
     */
    @Then("^I see user (.*) (ringing|calling) label on Video Call page$")
    public void ISeeRingingUserLabel(String contact, String callState) throws Exception {
        contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
        Assert.assertTrue(String.format("%s %s label is not visible", contact, callState), getVideoCallingPage().isCallStatusLabelShown(contact, callState));
    }

    /**
     * Verify that button with pointed name and visibility is shown
     *
     * @param buttonName Name of the button
     * @param visibility is button enabled or disabled
     * @throws Exception
     * @step. ^I see (CallMute|LeaveCall|Accept|CallVideo) button and it is (enabled|disabled) on Video Call page$
     */
    @Then("^I see (CallMute|LeaveCall|Accept|CallVideo) button and it is (enabled|disabled) on Video Call page$")
    public void ISeeButtonWithNameAndItsVisibility(String buttonName, String visibility) throws Exception {
        String isEnabled = "";
        if (visibility.equals("enabled")) {
            isEnabled = "true";
        } else {
            isEnabled = "false";
        }
        Assert.assertTrue(String.format("%s button is not presented or not %s", buttonName, visibility), getVideoCallingPage().isButtonWithPointedVisibilityShown(buttonName, isEnabled));
    }

    /**
     * Click on Hang Up button on Video Call page
     *
     * @throws Exception
     * @step. ^I click Hang Up button on Video Call page$
     */
    @When("^I click Hang Up button on Video Call page$")
    public void IClickHangUpButton() throws Exception {
        getVideoCallingPage().clickHangUpButton();
    }

    /**
     * Click on Accept video call button
     *
     * @throws Exception
     * @step. ^I click Accept video call button$
     */
    @When("^I click Accept video call button$")
    public void IClickAcceptVideoCallButton() throws Exception {
        getVideoCallingPage().clickAcceptCallButton();
    }

    /**
     * Click on Decline video call button
     *
     * @throws Exception
     * @step. ^I click Decline video call button
     */
    @When("^I click Decline video call button$")
    public void IClickDeclineVideoCallButton() throws Exception {
        getVideoCallingPage().clickDeclineCallButton();
    }
}
