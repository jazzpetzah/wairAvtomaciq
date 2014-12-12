package com.wearezeta.split_suite.storages;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.split_suite.ExecutedTestcase;
import com.wearezeta.split_suite.Testcase;

public class ResultJSON extends TestcasesStorage {
	private static final String NEW_ID_FORMAT = "%s-part%s";
	private static final String NEW_NAME_FORMAT = "%s Part %s";

	public ResultJSON(String path) {
		super(path);
	}

	private static boolean parseIsPassed(JSONArray steps) {
		for (int stepIdx = 0; stepIdx < steps.length(); stepIdx++) {
			if (!steps.getJSONObject(stepIdx).getJSONObject("result")
					.getString("status").equals("passed")) {
				return false;
			}
		}
		return true;
	}

	private static boolean parseIsFailed(JSONArray steps) {
		for (int stepIdx = 0; stepIdx < steps.length(); stepIdx++) {
			if (steps.getJSONObject(stepIdx).getJSONObject("result")
					.getString("status").equals("failed")) {
				return true;
			}
		}
		return false;
	}

	private static boolean parseIsSkipped(JSONArray steps) {
		for (int stepIdx = 0; stepIdx < steps.length(); stepIdx++) {
			if (!steps.getJSONObject(stepIdx).getJSONObject("result")
					.getString("status").equals("skipped")) {
				return false;
			}
		}
		return true;
	}

	private static Set<String> parseTagsList(JSONArray tags) {
		Set<String> resultList = new LinkedHashSet<String>();

		for (int tagIdx = 0; tagIdx < tags.length(); tagIdx++) {
			resultList.add(tags.getJSONObject(tagIdx).getString("name"));
		}
		return resultList;
	}

	private static List<ExecutedTestcase> extractTestcasesFromFeature(
			JSONObject feature) {
		List<ExecutedTestcase> parsedTestcases = new ArrayList<ExecutedTestcase>();
		if (!feature.has("elements")) {
			return parsedTestcases;
		}
		JSONArray elements = feature.getJSONArray("elements");
		for (int elementIdx = 0; elementIdx < elements.length(); elementIdx++) {
			JSONObject element = elements.getJSONObject(elementIdx);
			final String cucumberId = element.getString("id");
			final String name = element.getString("name");
			final boolean isPassed = parseIsPassed(element
					.getJSONArray("steps"));
			final boolean isFailed = parseIsFailed(element
					.getJSONArray("steps"));
			final boolean isSkipped = parseIsSkipped(element
					.getJSONArray("steps"));
			Set<String> tags = new LinkedHashSet<String>();
			if (element.has("tags")) {
				tags = parseTagsList(element.getJSONArray("tags"));
			}
			final String id = Testcase.extractIdsFromTags(tags);
			ExecutedTestcase tc = new ExecutedTestcase(id, name, tags,
					cucumberId, isPassed, isFailed, isSkipped);
			parsedTestcases.add(tc);
		}

		return parsedTestcases;
	}

	@Override
	public List<ExecutedTestcase> getTestcases() throws Throwable {
		final String json = FileUtils.readFileToString(new File(getPath()),
				"UTF-8");
		List<ExecutedTestcase> resultList = new ArrayList<ExecutedTestcase>();

		JSONArray allFeatures = new JSONArray(json);
		for (int featureIdx = 0; featureIdx < allFeatures.length(); featureIdx++) {
			resultList.addAll(extractTestcasesFromFeature(allFeatures
					.getJSONObject(featureIdx)));
		}
		return resultList;
	}

	private static int computeNewPartIndex(String currentId,
			Set<String> existingIds) {
		int suffix = 2;
		while (existingIds.contains(String.format(NEW_ID_FORMAT, currentId,
				suffix))) {
			suffix++;
		}
		return suffix;
	}

	private static void updateMainReport(JSONArray mainArray, JSONArray nextPart) {
		Set<String> existingIds = new HashSet<String>();
		for (int mainIdx = 0; mainIdx < mainArray.length(); mainIdx++) {
			existingIds.add(mainArray.getJSONObject(mainIdx).getString("id"));
		}

		for (int partIdx = 0; partIdx < nextPart.length(); partIdx++) {
			JSONObject item = nextPart.getJSONObject(partIdx);
			final String partId = item.getString("id");
			if (existingIds.contains(partId)) {
				final int newPartIndex = computeNewPartIndex(partId,
						existingIds);
				item.put("id",
						String.format(NEW_ID_FORMAT, partId, newPartIndex));
				item.put("name", String.format(NEW_NAME_FORMAT,
						item.getString("name"), newPartIndex));
			}
			mainArray.put(item);
		}
	}

	public static ResultJSON createFromSetOfResults(String resultPath,
			Set<String> srcPaths) throws Throwable {
		JSONArray resultJSON = new JSONArray();
		List<String> sortedPaths = new ArrayList<String>(srcPaths);
		Collections.sort(sortedPaths);

		for (String path : sortedPaths) {
			final String json = FileUtils.readFileToString(new File(path),
					"UTF-8");
			updateMainReport(resultJSON, new JSONArray(json));
		}

		FileUtils.writeStringToFile(new File(resultPath),
				resultJSON.toString(2), "UTF-8");
		return new ResultJSON(resultPath);
	}
}
