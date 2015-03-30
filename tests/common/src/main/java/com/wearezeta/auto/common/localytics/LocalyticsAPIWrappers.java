package com.wearezeta.auto.common.localytics;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public final class LocalyticsAPIWrappers {
	private LocalyticsToken token;

	public LocalyticsAPIWrappers(String apiKey, String apiSecret) {
		this.token = new LocalyticsToken(apiKey, apiSecret);
	}

	private static JSONArray stringsArrayToJSONArray(String[] arrIn) {
		JSONArray result = new JSONArray();
		for (String item : arrIn) {
			result.put(item);
		}
		return result;
	}

	private static String dateToLocalyticsFullTimestamp(Date dt) {
		final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
		final SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm");
		return String.format("%sT%s", dateFmt.format(dt), timeFmt.format(dt));
	}

	public int getNumberOfEventOccurencesPerPeriod(String appId,
			String eventName, Date periodStart, Date periodEnd)
			throws Exception {
		JSONObject queryData = new JSONObject();
		queryData
				.put("app_id", stringsArrayToJSONArray(new String[] { appId }));
		queryData.put("metrics",
				stringsArrayToJSONArray(new String[] { "occurrences" }));
		// https://api.localytics.com/docs#dimensions
		queryData.put("dimensions",
				stringsArrayToJSONArray(new String[] { "event_name" }));
		JSONObject conditionsData = new JSONObject();
		JSONArray hourPeriodDefinition = stringsArrayToJSONArray(new String[] {
				"between", dateToLocalyticsFullTimestamp(periodStart),
				dateToLocalyticsFullTimestamp(periodEnd) });
		conditionsData.put("hour", hourPeriodDefinition);
		queryData.put("conditions", conditionsData);

		final JSONObject result = LocalyticsRestAPI.query(token, queryData);
		if (result.has("results")) {
			JSONArray results = result.getJSONArray("results");
			int numberOfOccurences = 0;
			for (int idx = 0; idx < results.length(); idx++) {
				if (results.getJSONObject(idx).getString("event_name")
						.equals(eventName)) {
					numberOfOccurences += results.getJSONObject(idx).getInt(
							"occurrences");
				}
			}
			return numberOfOccurences;
		} else {
			throw new RuntimeException("No results were returned!");
		}
	}

	public int getNumberOfAttributeOccurencesPerPeriod(String appId,
			String eventName, String attributeName, String attributeValue,
			Date periodStart, Date periodEnd) throws Exception {
		JSONObject queryData = new JSONObject();
		queryData
				.put("app_id", stringsArrayToJSONArray(new String[] { appId }));
		// https://api.localytics.com/docs#query-attribute-explorer
		final String caninicalAttributeName = String.format("a:%s",
				attributeName);
		queryData.put("metrics",
				stringsArrayToJSONArray(new String[] { "occurrences" }));
		queryData
				.put("dimensions",
						stringsArrayToJSONArray(new String[] { caninicalAttributeName }));
		JSONObject conditionsData = new JSONObject();
		conditionsData.put("event_name", eventName);
		JSONArray hourPeriodDefinition = stringsArrayToJSONArray(new String[] {
				"between", dateToLocalyticsFullTimestamp(periodStart),
				dateToLocalyticsFullTimestamp(periodEnd) });
		conditionsData.put("hour", hourPeriodDefinition);
		queryData.put("conditions", conditionsData);

		final JSONObject result = LocalyticsRestAPI.query(token, queryData);
		if (result.has("results")) {
			final JSONArray results = result.getJSONArray("results");
			int numberOfOccurences = 0;
			for (int idx = 0; idx < results.length(); idx++) {
				if (results.getJSONObject(idx).has(caninicalAttributeName)) {
					if (attributeValue == null) {
						numberOfOccurences += results.getJSONObject(idx)
								.getInt("occurrences");
					} else if (results.getJSONObject(idx)
							.getString(caninicalAttributeName)
							.equals(attributeValue)) {
						return results.getJSONObject(idx).getInt("occurrences");
					}
				}
			}
			return numberOfOccurences;
		} else {
			throw new RuntimeException("No results were returned!");
		}
	}
}
