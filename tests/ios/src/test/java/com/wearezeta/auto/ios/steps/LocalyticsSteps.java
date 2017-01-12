package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.device_helpers.IOSRealDeviceHelpers;
import com.wearezeta.auto.common.driver.device_helpers.IOSSimulatorHelpers;
import com.wearezeta.auto.common.misc.Timedelta;
import com.wearezeta.auto.ios.tools.LocalyticsHelpers;
import cucumber.api.java.en.Then;
import org.json.JSONObject;
import org.junit.Assert;

public class LocalyticsSteps {
    private static final Timedelta TIMEOUT = Timedelta.fromSeconds(15);
    private static final Timedelta INTERVAL = Timedelta.fromSeconds(3);

    private static String getDeviceLogs() throws Exception {
        if (CommonUtils.getIsSimulatorFromConfig(LocalyticsSteps.class)) {
            return IOSSimulatorHelpers.getLogs();
        }
        return IOSRealDeviceHelpers.getLogs();
    }

    private static long getOccurrencesCount(String deviceLog, String expectedEvent, String expectedAttributes) {
        if (expectedAttributes == null) {
            return LocalyticsHelpers.getEventOccurrencesCount(deviceLog, expectedEvent);
        }
        final JSONObject expectedJSONAttributes = new JSONObject(expectedAttributes);
        return LocalyticsHelpers.getEventOccurrencesCount(deviceLog, expectedEvent, expectedJSONAttributes);
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
        final boolean result;
        switch (comparator.toLowerCase()) {
            case "at least":
                result = CommonUtils.waitUntilTrue(TIMEOUT, INTERVAL,
                        () -> getOccurrencesCount(getDeviceLogs(), expectedEvent, expectedAttributes) >= expectedCount
                );
                Assert.assertTrue(String.format("The actual count of '%s' event occurrences in the log " +
                        "is less than the expected count %d", expectedEvent, expectedCount), result);
                break;
            case "exactly":
                result = CommonUtils.waitUntilTrue(TIMEOUT, INTERVAL,
                        () -> getOccurrencesCount(getDeviceLogs(), expectedEvent, expectedAttributes) == expectedCount
                );
                Assert.assertTrue(String.format("The actual count of '%s' event occurrences in the log " +
                        "is not equal to the expected count %d", expectedEvent, expectedCount), result);
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown comparator value '%s'", comparator));
        }
    }
}
