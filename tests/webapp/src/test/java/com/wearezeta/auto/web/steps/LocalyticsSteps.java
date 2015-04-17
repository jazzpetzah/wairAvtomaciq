package com.wearezeta.auto.web.steps;

import com.wearezeta.auto.common.localytics.CommonLocalyticsSteps;
import com.wearezeta.auto.common.localytics.LocalyticsAPIWrappers;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class LocalyticsSteps {
	// ID for webapp staging
	private final static String APP_ID = "905792736c9f17c3464fd4e-60d90c82-d14a-11e4-af66-009c5fda0a25";
	// TODO: probably, these ids are the same for other platforms
	// if yes, then it's better to move this stuff to the common config file
	private final static String API_KEY = "5ecfc1339b7d3ece604da2c-449cfea8-b37b-11e4-acba-009c5fda0a25";
	private final static String API_SECRET = "720582f28b9c47b8f24e0a0-449d03c0-b37b-11e4-acba-009c5fda0a25";

	private static CommonLocalyticsSteps commonSteps = new CommonLocalyticsSteps(
			APP_ID, new LocalyticsAPIWrappers(API_KEY, API_SECRET));

	/**
	 * Takes snapshot of current event occurrence values on Localytics
	 * 
	 * @step. ^I take snapshot of (.*) events? count$
	 * 
	 * @param events
	 *            the list of events to take snapshot from. Every event name is
	 *            separated from each other by pipe "|" character. Use
	 *            https://api.localytics.com/docs#show-attributes API call to
	 *            get the list of available event names
	 * 
	 * @throws Exception
	 */
	@Given("^I take snapshot of (.*) events? count$")
	public void ITakeSnapshotOfXEventCount(String events) throws Exception {
		commonSteps.ITakeSnapshotOfXEventCount(events);
	}

	/**
	 * Takes snapshot of current event occurrence values on Localytics
	 * 
	 * @step. ^I take snapshot of (.*) attributes? count$
	 * 
	 * @param attributes
	 *            the list of attributes to take snapshot from. Every item is
	 *            separated from each other by pipe "|" character. Item format
	 *            is the following: EventName1:AttributeName1=AttributeValue1 If
	 *            AttributeValue is not provided for the particular
	 *            AttributeName then all occurrences of this particular
	 *            attribute will be counted. Use
	 *            https://api.localytics.com/docs#show-attributes API call to
	 *            get the list of available event and attribute names. Check the
	 *            documentation on the particular app and/or its source code
	 *            about the possible attribute values.
	 * 
	 * @throws Exception
	 */
	@Given("^I take snapshot of (.*) attributes? count$")
	public void ITakeSnapshotOfXAttributesCount(String attributes)
			throws Exception {
		commonSteps.ITakeSnapshotOfXAttributesCount(attributes);
	}

	/**
	 * Verifies whether the particular events occurrences count is increased
	 * within the given timeout on Localytics. It is mandatory to snapshot all
	 * the previous event occurrence values by calling the 'I take snapshot of
	 * ... events count' step before.
	 * 
	 * @step. ^I verify the count of (.*) events? (?:has|have) been increased
	 *        within (\\d+) seconds?$
	 * 
	 * @param events
	 *            the list of events, whose occurrences count we have to verify.
	 *            Check the documentation for 'I take snapshot of ... events
	 *            count' step about how to format the list of events.
	 * @param secondsTimeout
	 *            the number of seconds to wait until occurrences count is
	 *            changed (usually it takes up to 5-15 minutes for Localytics to
	 *            catch up)
	 * @throws Exception
	 */
	@Then("^I verify the count of (.*) events? (?:has|have) been increased within (\\d+) seconds?$")
	public void IVerifyTheCountOfXEventHasBeenIncreasedWithinYSeconds(
			String events, long secondsTimeout) throws Exception {
		commonSteps.IVerifyTheCountOfXEventHasBeenIncreasedWithinYSeconds(
				events, secondsTimeout);
	}

	/**
	 * Verifies whether the particular event attribute occurrences count is
	 * increased within the given timeout on Localytics. It is mandatory to
	 * snapshot all the previous attribute occurrence values by calling the 'I
	 * take snapshot of ... attributes count' step before.
	 * 
	 * @step. ^I verify the count of (.*) attributes? (?:has|have) been
	 *        increased within (\\d+) seconds?$
	 * 
	 * @param attributes
	 *            the list of attributes, whose occurrences count we have to
	 *            verify. Check the documentation for 'I take snapshot of ...
	 *            attributes count' step about how to format the list of
	 *            attributes.
	 * @param secondsTimeout
	 *            the number of seconds to wait until occurrences count is
	 *            changed (usually it takes up to 5-15 minutes for Localytics to
	 *            catch up)
	 * 
	 * @throws Exception
	 */
	@Then("^I verify the count of (.*) attributes? (?:has|have) been increased within (\\d+) seconds?$")
	public void IVerifyTheCountOfXAttributesHasBeenIncreasedWithinYSeconds(
			String attributes, long secondsTimeout) throws Exception {
		commonSteps.IVerifyTheCountOfXAttributesHasBeenIncreasedWithinYSeconds(
				attributes, secondsTimeout);
	}
}
