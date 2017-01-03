package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.device_helpers.IOSRealDeviceHelpers;
import com.wearezeta.auto.common.driver.device_helpers.IOSSimulatorHelpers;
import com.wearezeta.auto.ios.tools.LocalyticsHelpers;
import cucumber.api.java.en.Then;
import org.json.JSONObject;
import org.junit.Assert;

public class LocalyticsSteps {
    private static String getDeviceLogs() throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(LocalyticsSteps.class)) {
            return IOSSimulatorHelpers.getLogs();
        }
        return IOSRealDeviceHelpers.getLogs();
    }

    /**
     * Verify the count of event occurrences in the log
     *
     * @param expectedEvent      the name of the expected event
     * @param comparator         comparator string
     * @param expectedCount      the expected event occurrences count
     * @param expectedAttributes the expected event attributes represented as valid JSON string (optional)
     * @throws Exception
     * @step. ^I see "(.*)" event (?:with (.*) attributes? )?is sent to Localytics (at least|exactly) (\d+) times?$
     */
    @Then("^I see \"(.*)\" event (?:with (.*) attributes? )?is sent to Localytics (at least|exactly) (\\d+) times?$")
    public void IVerifyEventsCount(String expectedEvent, String expectedAttributes,
                                   String comparator, int expectedCount) throws Exception {
        final String deviceLog = getDeviceLogs();
        final long actualCount;
        if (expectedAttributes == null) {
            actualCount = LocalyticsHelpers.getEventOccurrencesCount(deviceLog, expectedEvent);
        } else {
            final JSONObject expectedJSONAttributes = new JSONObject(expectedAttributes);
            actualCount = LocalyticsHelpers.getEventOccurrencesCount(deviceLog, expectedEvent,
                    expectedJSONAttributes);
        }
        switch (comparator.toLowerCase()) {
            case "at least":
                Assert.assertTrue(String.format("The actual count of '%s' event occurrences in the log (%d) " +
                                "is less than the expected count %d", expectedEvent, actualCount, expectedCount),
                        actualCount >= expectedCount);
                break;
            case "exactly":
                Assert.assertEquals(String.format("The actual count of '%s' event occurrences in the log (%d) " +
                                "is not equal to the expected count %d", expectedEvent, actualCount, expectedCount),
                        expectedCount, actualCount);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown comparator value '%s'", comparator));
        }
    }
}
