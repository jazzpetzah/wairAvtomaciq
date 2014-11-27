package com.wearezeta.zephyr_sync.storages;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.jdbc.NotImplemented;
import com.wearezeta.zephyr_sync.ExecutedTestcase;
import com.wearezeta.zephyr_sync.Testcase;

public class ResultJSON extends TestcasesStorage {
	private String path;

	public ResultJSON(String path) {
		this.path = path;
	}

	private static Boolean parseIsPassed(JSONArray steps) {
		for (int stepIdx = 0; stepIdx < steps.length(); stepIdx++) {
			if (!steps.getJSONObject(stepIdx).getJSONObject("result")
					.getString("status").equals("passed")) {
				return false;
			}
		}
		return true;
	}

	private static Boolean parseIsSkipped(JSONArray steps) {
		for (int stepIdx = 0; stepIdx < steps.length(); stepIdx++) {
			if (steps.getJSONObject(stepIdx).getJSONObject("result")
					.getString("status").equals("skipped")) {
				return true;
			}
		}
		return false;
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

		JSONArray elements = feature.getJSONArray("elements");
		for (int elementIdx = 0; elementIdx < elements.length(); elementIdx++) {
			JSONObject element = elements.getJSONObject(elementIdx);
			final String cucumberId = element.getString("id");
			final String name = element.getString("name");
			final Boolean isPassed = parseIsPassed(element.getJSONArray("steps"));
			final Boolean isSkipped = parseIsSkipped(element.getJSONArray("steps"));
			Set<String> tags = new LinkedHashSet<String>();
			if(element.has("tags")) {
				tags = parseTagsList(element.getJSONArray("tags"));
			}
			final String id = Testcase.extractIdsFromTags(tags);
			ExecutedTestcase tc = new ExecutedTestcase(id, name, tags,
					cucumberId, isPassed, isSkipped);
			parsedTestcases.add(tc);
		}

		return parsedTestcases;
	}

	@Override
	public List<ExecutedTestcase> getTestcases() throws Throwable {
		final String json = FileUtils.readFileToString(new File(path), "UTF-8");
		List<ExecutedTestcase> resultList = new ArrayList<ExecutedTestcase>();

		JSONArray allFeatures = new JSONArray(json);
		for (int featureIdx = 0; featureIdx < allFeatures.length(); featureIdx++) {
			resultList.addAll(extractTestcasesFromFeature(allFeatures
					.getJSONObject(featureIdx)));
		}
		return resultList;
	}

	@Override
	public void syncTestcases(List<? extends Testcase> testcases)
			throws Throwable {
		throw new NotImplemented();
	}

}
