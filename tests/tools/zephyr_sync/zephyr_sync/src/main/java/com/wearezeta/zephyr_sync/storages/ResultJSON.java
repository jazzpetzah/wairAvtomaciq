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
import com.wearezeta.zephyr_sync.ExecutedCucumberTestcase;
import com.wearezeta.zephyr_sync.Testcase;

public class ResultJSON extends TestcasesStorage {
	private String path;

	public ResultJSON(String path) {
		this.path = path;
	}

	private static String getStepStatus(final JSONObject step) {
		return step.getJSONObject("result").getString("status");
	}

	private static boolean isGivenStepType(final JSONObject step) {
		return step.has("keyword")
				&& step.getString("keyword").trim().toLowerCase()
						.equals("given");
	}

	private static boolean parseIsPassed(JSONArray steps) {
		boolean isPending = false;
		for (int stepIdx = 0; stepIdx < steps.length(); stepIdx++) {
			final JSONObject step = steps.getJSONObject(stepIdx);
			if (getStepStatus(step).equals("pending")) {
				// Parse test cases with pending steps as passed by default
				isPending = true;
			} else if (!getStepStatus(step).equals("passed") && !isPending) {
				return false;
			} else if (getStepStatus(step).equals("failed")) {
				if (!isGivenStepType(step)) {
					return false;
				}
			}
		}
		return true;
	}

	private static boolean parseIsFailed(JSONArray steps) {
		for (int stepIdx = 0; stepIdx < steps.length(); stepIdx++) {
			final JSONObject step = steps.getJSONObject(stepIdx);
			if (getStepStatus(step).equals("failed")) {
				// Don't fail the test case if test setup failed
				if (!isGivenStepType(step)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean parseIsSkipped(JSONArray steps) {
		for (int stepIdx = 0; stepIdx < steps.length(); stepIdx++) {
			final JSONObject step = steps.getJSONObject(stepIdx);
			if (!getStepStatus(step).equals("skipped")) {
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

	private static List<ExecutedCucumberTestcase> extractTestcasesFromFeature(
			JSONObject feature) {
		List<ExecutedCucumberTestcase> parsedTestcases = new ArrayList<ExecutedCucumberTestcase>();
		if (!feature.has("elements")) {
			return parsedTestcases;
		}

		JSONArray elements = feature.getJSONArray("elements");
		for (int elementIdx = 0; elementIdx < elements.length(); elementIdx++) {
			JSONObject element = elements.getJSONObject(elementIdx);
			final String cucumberId = element.getString("id");
			final String name = element.getString("name");
			CucumberExecutionStatus status = CucumberExecutionStatus.Undefined;
			JSONArray steps = element.getJSONArray("steps");
			if (parseIsPassed(steps)) {
				status = CucumberExecutionStatus.Passed;
			} else if (parseIsFailed(steps)) {
				status = CucumberExecutionStatus.Failed;
			} else if (parseIsSkipped(steps)) {
				status = CucumberExecutionStatus.Skipped;
			}
			Set<String> tags = new LinkedHashSet<String>();
			if (element.has("tags")) {
				tags = parseTagsList(element.getJSONArray("tags"));
			}
			final String id = Testcase.extractIdsFromTags(tags);
			ExecutedCucumberTestcase tc = new ExecutedCucumberTestcase(id,
					name, tags, cucumberId, status);
			parsedTestcases.add(tc);
		}
		return parsedTestcases;
	}

	@Override
	public List<ExecutedCucumberTestcase> getTestcases() throws Exception {
		final String json = FileUtils.readFileToString(new File(path), "UTF-8");
		List<ExecutedCucumberTestcase> resultList = new ArrayList<ExecutedCucumberTestcase>();

		JSONArray allFeatures = new JSONArray(json);
		for (int featureIdx = 0; featureIdx < allFeatures.length(); featureIdx++) {
			resultList.addAll(extractTestcasesFromFeature(allFeatures
					.getJSONObject(featureIdx)));
		}
		return resultList;
	}

	@Override
	public void syncTestcases(List<? extends Testcase> testcases)
			throws Exception {
		throw new NotImplemented();
	}

}
