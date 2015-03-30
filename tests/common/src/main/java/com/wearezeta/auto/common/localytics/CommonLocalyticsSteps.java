package com.wearezeta.auto.common.localytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.log.ZetaLogger;

public final class CommonLocalyticsSteps {

	public static final Logger log = ZetaLogger
			.getLog(CommonLocalyticsSteps.class.getSimpleName());

	private Map<String, Date> eventsSnapshotDates = new HashMap<String, Date>();
	private Map<String, Integer> eventsCountSnapshot = new HashMap<String, Integer>();
	private Map<String, Date> attributeSnapshotDates = new HashMap<String, Date>();
	private Map<String, Integer> attributesCountSnapshot = new HashMap<String, Integer>();
	private LocalyticsAPIWrappers localyticsApiWrappers;
	private String appId;

	public CommonLocalyticsSteps(String appId,
			LocalyticsAPIWrappers localyticsApiWrappers) {
		this.localyticsApiWrappers = localyticsApiWrappers;
		this.appId = appId;
	}

	private static final String EVENTS_SEPARATOR = "\\|";

	private static List<String> splitEvents(String events) {
		return new ArrayList<String>(Arrays.asList(events
				.split(EVENTS_SEPARATOR))).stream().map(x -> x.trim())
				.collect(Collectors.toList());
	}

	private static final int MILLISECONDS_IN_TWO_HOURS = 2 * 3600 * 1000;

	public void ITakeSnapshotOfXEventCount(String events) throws Exception {
		List<String> eventsList = splitEvents(events);
		final Date twoHoursBefore = new Date(new Date().getTime()
				- MILLISECONDS_IN_TWO_HOURS);
		for (String eventName : eventsList) {
			final Date currentTimestamp = new Date();
			eventsSnapshotDates.put(eventName, twoHoursBefore);
			eventsCountSnapshot.put(eventName, localyticsApiWrappers
					.getNumberOfEventOccurencesPerPeriod(appId, eventName,
							twoHoursBefore, currentTimestamp));
			log.debug(String.format("Taken snapshot for '%s' event at %s",
					events, currentTimestamp.toString()));
		}
	}

	private static final int SECONDS_INTERVAL = 20;

	private static void pingDrivers() {
		Collection<RemoteWebDriver> registeredDrivers = PlatformDrivers
				.getInstance().getRegisteredDrivers();
		for (RemoteWebDriver driver : registeredDrivers) {
			driver.getCurrentUrl();
		}
	}

	public void IVerifyTheCountOfXEventHasBeenIncreasedWithinYSeconds(
			String events, long secondsTimeout) throws Exception {
		List<String> eventsList = splitEvents(events);
		for (String eventName : eventsList) {
			log.debug(String.format("Verifying stats for event '%s'...",
					eventName));
			if (!eventsCountSnapshot.containsKey(eventName)) {
				throw new RuntimeException(String.format(
						"'%s' event has to be snapshotted before", eventName));
			}
			final int snapshottedValue = eventsCountSnapshot.get(eventName);
			log.debug(String
					.format("\tSnapshotted value: %s", snapshottedValue));
			int currentValue = -1;
			long milliSecondsStarted = new Date().getTime();
			do {
				currentValue = localyticsApiWrappers
						.getNumberOfEventOccurencesPerPeriod(appId, eventName,
								eventsSnapshotDates.get(eventName), new Date());
				if (currentValue > snapshottedValue) {
					break;
				}
				Thread.sleep(SECONDS_INTERVAL * 1000);
				// This is to keep selenium alive
				pingDrivers();
			} while (new Date().getTime() - milliSecondsStarted < secondsTimeout * 1000);
			log.debug(String.format("\tCurrent value: %s", currentValue));
			if (currentValue <= snapshottedValue) {
				throw new AssertionError(
						String.format(
								"'%s' events count has not been changed within %s second(s) timeout",
								eventName, secondsTimeout));
			} else {
				log.debug(String.format("%s > %s -> OK", currentValue,
						snapshottedValue));
			}
		}
	}

	private static final String EVENT_ATTRIBUTE_SEPARATOR = ":";

	private static String getSingleKey(String eventName, String attributeName) {
		return String.format("%s%s%s", eventName, EVENT_ATTRIBUTE_SEPARATOR,
				attributeName);
	}

	private static Map<String, String> splitAttributes(String attributes) {
		Map<String, String> result = new HashMap<String, String>();
		for (String eventAttrPair : splitEvents(attributes)) {
			final int separatorPos = eventAttrPair
					.indexOf(EVENT_ATTRIBUTE_SEPARATOR);
			assert separatorPos > 0 : String
					.format("Event/Attribute separator '%s' is expected, but '%s' is found",
							EVENT_ATTRIBUTE_SEPARATOR, eventAttrPair);
			final String eventName = eventAttrPair.substring(0, separatorPos)
					.trim();
			final String attribute = eventAttrPair.substring(separatorPos + 1,
					eventAttrPair.length()).trim();
			result.put(eventName, attribute);
		}
		return result;
	}

	private static final String ATTRIBUTE_NAME_VALUE_SEPARATOR = "=";

	private static String extractAttributeName(String attributeNameValue) {
		final int separatorPos = attributeNameValue
				.indexOf(ATTRIBUTE_NAME_VALUE_SEPARATOR);
		if (separatorPos >= 0) {
			assert separatorPos > 0;
			return attributeNameValue.substring(0, separatorPos).trim();
		} else {
			return attributeNameValue.trim();
		}
	}

	private static String extractAttributeValue(String attributeNameValue) {
		final int separatorPos = attributeNameValue
				.indexOf(ATTRIBUTE_NAME_VALUE_SEPARATOR);
		if (separatorPos >= 0) {
			assert separatorPos > 0 : String.format(
					"Attribute name is expected in '%s', but found '%s'",
					attributeNameValue, ATTRIBUTE_NAME_VALUE_SEPARATOR);
			return attributeNameValue.substring(separatorPos + 1,
					attributeNameValue.length()).trim();
		} else {
			return null;
		}
	}

	public void ITakeSnapshotOfXAttributesCount(String attributes)
			throws Exception {
		final Map<String, String> attributesMap = splitAttributes(attributes);
		final Date twoHoursBefore = new Date(new Date().getTime()
				- MILLISECONDS_IN_TWO_HOURS);
		for (Entry<String, String> attributeForEvent : attributesMap.entrySet()) {
			final Date currentTimestamp = new Date();
			final String eventName = attributeForEvent.getKey();
			final String attributeNameValue = attributeForEvent.getValue();
			final String attributeName = extractAttributeName(attributeNameValue);
			final String attributeValue = extractAttributeValue(attributeNameValue);
			final String singleKey = getSingleKey(eventName, attributeNameValue);
			attributeSnapshotDates.put(singleKey, twoHoursBefore);
			attributesCountSnapshot.put(singleKey, localyticsApiWrappers
					.getNumberOfAttributeOccurencesPerPeriod(appId, eventName,
							attributeName, attributeValue, twoHoursBefore,
							currentTimestamp));
			log.debug(String.format("Taken snapshot for '%s' attribute at %s",
					singleKey, currentTimestamp.toString()));
		}
	}

	public void IVerifyTheCountOfXAttributesHasBeenIncreasedWithinYSeconds(
			String attributes, long secondsTimeout) throws Exception {
		final Map<String, String> attributesMap = splitAttributes(attributes);
		for (Entry<String, String> attributeForEvent : attributesMap.entrySet()) {
			final String eventName = attributeForEvent.getKey();
			final String attributeNameValue = attributeForEvent.getValue();
			final String attributeName = extractAttributeName(attributeNameValue);
			final String attributeValue = extractAttributeValue(attributeNameValue);
			final String singleKey = getSingleKey(eventName, attributeNameValue);
			log.debug(String.format("Verifying stats for attribute '%s'...",
					getSingleKey(eventName, attributeNameValue)));
			if (!attributesCountSnapshot.containsKey(singleKey)) {
				throw new RuntimeException(String.format(
						"'%s' attribute has to be snapshotted before",
						singleKey));
			}
			final int snapshottedValue = attributesCountSnapshot.get(singleKey);
			log.debug(String
					.format("\tSnapshotted value: %s", snapshottedValue));
			int currentValue = -1;
			long milliSecondsStarted = new Date().getTime();
			do {
				currentValue = localyticsApiWrappers
						.getNumberOfAttributeOccurencesPerPeriod(appId,
								eventName, attributeName, attributeValue,
								attributeSnapshotDates.get(singleKey),
								new Date());
				if (currentValue > snapshottedValue) {
					break;
				}
				Thread.sleep(SECONDS_INTERVAL * 1000);
				// This is to keep selenium alive
				pingDrivers();
			} while (new Date().getTime() - milliSecondsStarted < secondsTimeout * 1000);
			log.debug(String.format("\tCurrent value: %s", currentValue));
			if (currentValue <= snapshottedValue) {
				throw new AssertionError(
						String.format(
								"'%s' attribute count has not been changed within %s second(s) timeout",
								singleKey, secondsTimeout));
			} else {
				log.debug(String.format("%s > %s -> OK", currentValue,
						snapshottedValue));
			}
		}
	}
}
