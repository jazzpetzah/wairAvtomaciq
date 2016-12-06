package com.wearezeta.auto.android.steps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.calling2.v1.model.Call;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import com.wearezeta.auto.common.log.ZetaLogger;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CallingSteps {

    private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2.getInstance();

    AndroidCommonCallingSteps androidCallingSteps = new AndroidCommonCallingSteps();

    /**
     * Make audio or video call(s) to one specific conversation.
     *
     * @param callerNames      caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) start(?:s|ing) a video call to (.*)$
     */
    @When("^(.*) start(?:s|ing) a video call to (.*)$")
    public void UserXCallsWithVideoToConversationY(String callerNames, String conversationName) throws Exception {
        commonCallingSteps.startVideoCallToConversation(commonCallingSteps.getUsersManager().splitAliases(callerNames),
                conversationName);
    }

    /**
     * Make voice calls to one specific conversation.
     *
     * @param callerNames      caller names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) calls (.*)$
     */
    @When("^(.*) calls (.*)$")
    public void UserXCallsToConversationY(String callerNames, String conversationName) throws Exception {
        androidCallingSteps.callToConversation(commonCallingSteps.getUsersManager().splitAliases(callerNames),
                conversationName);
    }

    /**
     * Stop call on the other side
     *
     * @param instanceUsers    comma separated list of user names/aliases
     * @param conversationName destination conversation name
     * @throws Exception
     * @step. ^(.*) stops? calls( to (.*))$
     */
    @When("^(.*) stops? calling( (.*))?$")
    public void UserXStopsCallsToUserY(String instanceUsers, String outgoingCall, String conversationName)
            throws Exception {
        if (outgoingCall == null) {
            commonCallingSteps.stopIncomingCall(commonCallingSteps.getUsersManager().splitAliases(instanceUsers));
        } else {
            commonCallingSteps.stopOutgoingCall(commonCallingSteps.getUsersManager().splitAliases(instanceUsers),
                    conversationName);
        }
    }

    /**
     * Verify whether call status is changed to one of the expected values after N seconds timeout
     *
     * @param conversationName destination conversation
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$
     */
    @Then("(.*) verifies that call status to (.*) is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callers,
                                              String conversationName, String expectedStatuses, int timeoutSeconds)
            throws Exception {
        commonCallingSteps.verifyCallingStatus(commonCallingSteps.getUsersManager().splitAliases(callers),
                conversationName, expectedStatuses, timeoutSeconds);
    }

    /**
     * Verify whether waiting instance status is changed to one of the expected values after N seconds timeout
     *
     * @param callees          comma separated callee names/aliases
     * @param expectedStatuses comma-separated list of expected call statuses. See
     *                         com.wearezeta.auto.common.calling2.v1.model.CallStatus for more details
     * @param timeoutSeconds   number of seconds to wait until call status is changed
     * @throws Exception
     * @step. (.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$
     */
    @Then("(.*) verif(?:y|ies) that waiting instance status is changed to (.*) in (\\d+) seconds?$")
    public void UserXVerifesCallStatusToUserY(String callees,
                                              String expectedStatuses, int timeoutSeconds) throws Exception {
        commonCallingSteps.verifyAcceptingCallStatus(commonCallingSteps.getUsersManager().splitAliases(callees),
                expectedStatuses, timeoutSeconds);
    }

    /**
     * Execute instance as 'userAsNameAlias' user on calling server using 'callingServiceBackend' tool
     *
     * @param callees               callee name/alias
     * @param callingServiceBackend available values: 'autocall', 'chrome', 'firefox', 'zcall'
     * @throws Exception
     * @step. (.*) starts? instances? using (.*)$
     */
    @When("(.*) starts? instances? using (.*)$")
    public void UserXStartsInstance(String callees,
                                    String callingServiceBackend) throws Exception {
        commonCallingSteps.startInstances(commonCallingSteps.getUsersManager().splitAliases(callees),
                callingServiceBackend, "Android", ZetaFormatter.getScenario());
    }

    /**
     * Automatically accept the next incoming audio call or for the particular user as soon as it appears in UI. Waiting
     * instance should be already created for this particular user
     *
     * @param callees callee names/aliases
     * @throws Exception
     * @step. (.*) accepts? next incoming call automatically$
     */
    @When("(.*) accepts? next incoming( video)? call automatically$")
    public void UserXAcceptsNextIncomingCallAutomatically(String callees, String video)
            throws Exception {
        if (video == null) {
            commonCallingSteps.acceptNextCall(commonCallingSteps.getUsersManager().splitAliases(callees));
        } else {
            commonCallingSteps.acceptNextVideoCall(commonCallingSteps.getUsersManager().splitAliases(callees));
        }

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
            assertThat(commonCallingSteps.getFlows(callee),
                    hasSize(numberOfFlows));
        }
    }

    /**
     * Verify that each flow of the instance had incoming and outgoing bytes running over the line
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that all flows have greater than 0 bytes$
     */
    @Then("(.*) verif(?:ies|y) that all flows have greater than 0 bytes$")
    public void UserXVerifesHavingXFlows(String callees) throws Exception {
        for (String callee : commonCallingSteps.getUsersManager().splitAliases(callees)) {
            for (Flow flow : commonCallingSteps.getFlows(callee)) {
                assertThat("incoming bytes", flow.getTelemetry().getStats().getAudio().getBytesReceived(), greaterThan(0L));
                assertThat("outgoing bytes", flow.getTelemetry().getStats().getAudio().getBytesSent(),
                        greaterThan(0L));
            }
        }
    }

    /**
     * Verify that each call of the instances was successful
     *
     * @param callees comma separated list of callee names/aliases
     * @throws Exception
     * @step. (.*) verif(?:ies|y) that call to conversation (.*) was successful$
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
     * @param callDurationMinutes
     * @param times               number of consecutive calls
     * @param callees             participants which will wait for a call
     * @throws java.lang.Throwable
     * @step. ^I call (\\d+) times for (\\d+) minutes with (.*)$
     */
    @Then("^I call (\\d+) times for (\\d+) minutes with (.*)$")
    public void ICallXTimes(int times, int callDurationMinutes, String callees)
            throws Throwable {
        final int timeBetweenCall = 10;
        final List<String> calleeList = commonCallingSteps.getUsersManager().splitAliases(callees);
        final ConversationViewPageSteps convSteps = new ConversationViewPageSteps();
        final CallOngoingAudioPageSteps callOngoingPageSteps = new CallOngoingAudioPageSteps();
        final CallOutgoingPageSteps callOutgoingPageSteps = new CallOutgoingPageSteps();
        final CommonAndroidSteps commonAndroidSteps = new CommonAndroidSteps();
        final Map<Integer, Throwable> failures = new HashMap<>();
        for (int i = 0; i < times; i++) {
            LOG.info("\n\nSTARTING CALL " + i);
            try {
                convSteps.ITapTopToolbarButton("Audio Call");
                callOutgoingPageSteps.ISeeOutgoingCall(null, null);

                for (String callee : calleeList) {
                    UserXAcceptsNextIncomingCallAutomatically(callee, null);
                    UserXVerifesCallStatusToUserY(callee, "active", 20);
                }
                LOG.info("All instances are active");

                callOngoingPageSteps.ISeeOngoingCall(null);
                LOG.info("Calling overlay is visible");

                commonAndroidSteps.WaitForTime(callDurationMinutes * 60);

                callOngoingPageSteps.IHangUp();

                for (String callee : calleeList) {
                    UserXVerifesCallStatusToUserY(callee, "destroyed", 20);
                }
                LOG.info("All instances are destroyed");

                callOngoingPageSteps.ISeeOngoingCall("do not");
                LOG.info("Calling overlay is NOT visible");
                LOG.info("CALL " + i + " SUCCESSFUL");
                commonAndroidSteps.WaitForTime(timeBetweenCall);

            } catch (Throwable t) {
                LOG.info("CALL " + i + " FAILED");
                LOG.error("Can not stop waiting call " + i + " " + t);
                try {
                    callOngoingPageSteps.IHangUp();
                    convSteps.ISeeVideoCallButtonInUpperToolbar(null, "audio");
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
