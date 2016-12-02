package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.calling2.v1.model.Flow;

import com.wearezeta.auto.common.ZetaFormatter;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.wearezeta.auto.common.log.ZetaLogger;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.log4j.Logger;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallingSteps {

    private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2.getInstance();

    /**
     * Make call to a specific user. You may instantiate more than one incoming
     * call from single caller by calling this step multiple times
     *
     * @param caller           caller name/alias
     * @param conversationName destination conversation name
     *
     * @throws Exception
     * @step. (.*) calls (.*)$
     */
    @When("(.*) calls (.*)$")
    public void UserXCallsToUserYUsingCallBackend(String caller, String conversationName) throws Exception {
        commonCallingSteps.callToConversation(commonCallingSteps.getUsersManager().splitAliases(caller), conversationName);
    }

    /**
     * Stop outgoing or incoming call (audio and video) to the other side
     *
     * @step. ^(.*) stops? (incoming call from|outgoing call to) (.*)
     *
     * @param instanceUsers comma separated list of user names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     */
    @When("^(.*) stops? (incoming call from|outgoing call to) (.*)")
    public void UserXStopsIncomingOutgoingCallsToUserY(String instanceUsers, String typeOfCall, String conversationName)
            throws Exception {
        if (typeOfCall.equals("incoming call from")) {
            commonCallingSteps.stopIncomingCall(commonCallingSteps.getUsersManager().splitAliases(instanceUsers));
        } else {
            commonCallingSteps.stopOutgoingCall(commonCallingSteps.getUsersManager().splitAliases(instanceUsers), conversationName);
        }
    }



    /**
     * Verify whether call status is changed to one of the expected values after
     * N seconds timeout
     *
     * @param callers          caller names/aliases
     * @param conversationName destination conversation
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for
     *                         more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that call status to (.*) is changed to (.*) in
     * (\\d+) seconds?$
     */
    @Then("(.*) verif(?:ies|y) that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callers,
                                              String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        commonCallingSteps.verifyCallingStatus(commonCallingSteps.getUsersManager().splitAliases(callers),
                conversationName, expectedStatuses, timeoutSeconds);
    }

    /**
     * Verify whether waiting instance status is changed to one of the expected
     * values after N seconds timeout
     *
     * @param callees          comma separated list of callee names/aliases
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for
     *                         more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that waiting instance status is changed to
     * (.*) in * (\\d+) seconds?$
     */
    @Then("(.*) verif(?:ies|y) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callees,
                                              String expectedStatuses, int timeoutSeconds) throws Exception {
        commonCallingSteps.verifyAcceptingCallStatus(commonCallingSteps.getUsersManager().splitAliases(callees),
                expectedStatuses, timeoutSeconds);
    }

    /**
     * Verify that the instance has X active flows
     *
     * @param callees       comma separated list of callee names/aliases
     * @param numberOfFlows expected number of flows
     * @throws Exception
     * @step. (.*) verif(?:ies|y) to have (\\d+) flows?$
     */
    @Then("(.*) verif(?:ies|y) to have (\\d+) flows?$")
    public void UserXVerifesHavingXFlows(String callees, int numberOfFlows)
            throws Exception {
        for (String callee : commonCallingSteps.getUsersManager().splitAliases(callees)) {
            int actualFlowNumber = commonCallingSteps.getFlows(callee).size();
            Assert.assertTrue(String.format("Expected flows number is : %s but actual flows number was : %s",
                    numberOfFlows, actualFlowNumber), actualFlowNumber == numberOfFlows);
        }
    }

    /**
     * Verify that each flow of the instance had incoming and outgoing bytes
     * running over the line
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that all flows have greater than 0 bytes$
     */
    @Then("(.*) verif(?:ies|y) that all flows have greater than 0 bytes$")
    public void UserXVerifesHavingXFlows(String callees) throws Exception {
        for (String callee : commonCallingSteps.getUsersManager().splitAliases(callees)) {
            for (Flow flow : commonCallingSteps.getFlows(callee)) {
                Assert.assertTrue("There is no incoming bytes", flow.getTelemetry().getStats().getAudio().getBytesReceived() > 0L);
                Assert.assertTrue("There is no outgoing bytes", flow.getTelemetry().getStats().getAudio().getBytesSent() > 0L);
            }
        }
    }

    /**
     * Execute instance as 'userAsNameAlias' user on calling server
     * using 'callingServiceBackend' tool
     *
     * @param callees               comma separated callee name/alias
     * @param callingServiceBackend available values: 'blender', 'chrome', * 'firefox'
     * @throws Exception
     * @step. (.*) starts? instance using (.*)$
     */
    @When("(.*) starts? instance using (.*)$")
    public void UserXStartsInstance(String callees, String callingServiceBackend) throws Exception {
        commonCallingSteps.startInstances(commonCallingSteps.getUsersManager().splitAliases(callees),
                callingServiceBackend, "iOS", ZetaFormatter.getScenario());
    }

    /**
     * Automatically accept the next incoming call for the particular user as
     * soon as it appears in UI. Waiting instance should be already created for
     * this particular user
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) accepts? next incoming call automatically$
     */
    @When("(.*) accepts? next incoming call automatically$")
    public void UserXAcceptsNextIncomingCallAutomatically(String callees) throws Exception {
        commonCallingSteps.acceptNextCall(commonCallingSteps.getUsersManager().splitAliases(callees));
    }

    /**
     * Automatically accept the next incoming video call for the particular user
     * as soon as it appears in UI. Waiting instance should be already created
     * for this particular user
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) accepts? next incoming video call automatically$
     */
    @When("(.*) accepts? next incoming video call automatically$")
    public void UserXAcceptsNextIncomingVideoCallAutomatically(String callees) throws Exception {
        commonCallingSteps.acceptNextVideoCall(commonCallingSteps.getUsersManager().splitAliases(callees));
    }

    /**
     * Make a video call to a specific user.
     *
     * @param callers          caller names/aliases
     * @param conversationName destination conversation name
     *
     * @throws Exception
     * @step. (.*) starts? a video call to (.*)$
     */
    @When("(.*) starts? a video call to (.*)$")
    public void UserXStartVideoCallsToUserYUsingCallBackend(String callers, String conversationName) throws Exception {
        commonCallingSteps.startVideoCallToConversation(commonCallingSteps.getUsersManager().splitAliases(callers),
                conversationName);
    }

    /**
     * Verify that each call of the instances was successful
     *
     * @step. (.*) verif(?:ies|y) that call to conversation (.*) was successful$
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     */
    @Then("(.*) verif(?:ies|y) that call to conversation (.*) was successful$")
    public void UserXVerifesCallWasSuccessful(String callees, String conversation) throws Exception {
        for (Call call : commonCallingSteps.getOutgoingCall(commonCallingSteps.getUsersManager().splitAliases(callees),
                conversation)) {
            assertNotNull("There are no metrics available for this call \n" + call, call.getMetrics());
            assertTrue("Call failed: \n" + call + "\n" + call.getMetrics(), call.getMetrics().isSuccess());
        }
    }

    private static final Logger LOG = ZetaLogger.getLog(CallingSteps.class
            .getName());

    /**
     * Executes consecutive calls without logging out etc.
     *
     * @step. ^I call (\\d+) times for (\\d+) minutes with (.*)$
     *
     * @param callDurationMinutes
     * @param times number of consecutive calls
     * @param callees participants which will wait for a call
     * @throws java.lang.Throwable
     */
    @Then("^I call (\\d+) times for (\\d+) minutes with (.*)$")
    public void ICallXTimes(int times, int callDurationMinutes, String callees)
            throws Throwable {
        final int timeBetweenCall = 10;
        final List<String> calleeList = commonCallingSteps.getUsersManager().splitAliases(callees);
        final ConversationViewPageSteps convSteps = new ConversationViewPageSteps();
        final CallPageSteps callPageSteps = new CallPageSteps();
        final CommonIOSSteps commonIOSSteps = new CommonIOSSteps();
        final Map<Integer, Throwable> failures = new HashMap<>();
        for (int i = 0; i < times; i++) {
            LOG.info("\n\nSTARTING CALL " + i);
            try {
                    convSteps.ITapCallButton("Audio");

                    for (String callee : calleeList) {
                        UserXAcceptsNextIncomingCallAutomatically(callee);
                        UserXVerifesCallStatusToUserY(callee, "active", 20);
                    }
                    LOG.info("All instances are active");

                    callPageSteps.ISeeCallingOverlay(null);
                    LOG.info("Calling overlay is visible");

                    commonIOSSteps.WaitForTime(callDurationMinutes * 60);

                    callPageSteps.ITapButton("Leave");

                    for (String callee : calleeList) {
                        UserXVerifesCallStatusToUserY(callee, "destroyed", 20);
                    }
                    LOG.info("All instances are destroyed");

                    callPageSteps.ISeeCallingOverlay("do not");
                    LOG.info("Calling overlay is NOT visible");
                    LOG.info("CALL " + i + " SUCCESSFUL");
                    commonIOSSteps.WaitForTime(timeBetweenCall);

            } catch (Throwable t){
                LOG.info("CALL " + i + " FAILED");
                LOG.error("Can not stop waiting call " + i + " " + t);
                try {
                    callPageSteps.ITapButton("Leave");
                    convSteps.IDonotSeeCallingButtonsOnUpperToolbar(null, "Audio Call");
                } catch (Throwable ex) {
                    LOG.error("Can not stop call " + i + " " + ex);
                }
                failures.put(i, t);
            }

        }

        LOG.info(failures.size() + " failures happened during " + times
                + " calls");
        failures.forEach((Integer i, Throwable t) -> {
            LOG.error(i + ": " + t.getMessage());
        });

        for (Map.Entry<Integer, Throwable> entrySet : failures.entrySet()) {
            // will just throw the first exception to indicate failed calls in
            // test results
            throw entrySet.getValue();
        }
    }
}
