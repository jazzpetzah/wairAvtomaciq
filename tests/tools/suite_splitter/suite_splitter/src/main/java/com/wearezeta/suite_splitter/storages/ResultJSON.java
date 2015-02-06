package com.wearezeta.suite_splitter.storages;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.suite_splitter.Testcase;

public class ResultJSON extends TestcasesStorage {
	public ResultJSON(String path) {
		super(path);
	}

	@Override
	public List<Testcase> getTestcases() throws Throwable {
		throw new RuntimeException("Not implemented");
	}

	private static Map<String, Integer> statusPriorityMap = new HashMap<String, Integer>();
	static {
		statusPriorityMap.put("failed", 2);
		statusPriorityMap.put("skipped", 1);
		statusPriorityMap.put("passed", 0);
	}

	private static void setWorstResult(JSONObject srcElement,
			JSONObject dstElement) {
		if (srcElement.has("steps") && dstElement.has("steps")) {
			final JSONArray srcSteps = srcElement.getJSONArray("steps");
			final JSONArray dstSteps = dstElement.getJSONArray("steps");
			for (int dstStepIdx = 0; dstStepIdx < dstSteps.length(); dstStepIdx++) {
				if (dstStepIdx < srcSteps.length()) {
					final String srcStepResult = srcSteps
							.getJSONObject(dstStepIdx).getJSONObject("result")
							.getString("status");
					final int srcStepPriority = statusPriorityMap
							.containsKey(srcStepResult) ? statusPriorityMap
							.get(srcStepResult) : 3;
					final String dstStepResult = dstSteps
							.getJSONObject(dstStepIdx).getJSONObject("result")
							.getString("status");
					final int dstStepPriority = statusPriorityMap
							.containsKey(dstStepResult) ? statusPriorityMap
							.get(dstStepResult) : 3;
					if (srcStepPriority > dstStepPriority) {
						dstSteps.getJSONObject(dstStepIdx)
								.getJSONObject("result")
								.put("result", srcStepResult);
					}
				}
			}
		}
	}

	private static void importElements(JSONObject srcItem, JSONObject dstItem) {
		if (srcItem.has("elements")) {
			JSONArray srcElements = srcItem.getJSONArray("elements");
			if (dstItem.has("elements")) {
				JSONArray dstElements = dstItem.getJSONArray("elements");
				for (int srcElIdx = 0; srcElIdx < srcElements.length(); srcElIdx++) {
					boolean isDestElReplaced = false;
					for (int dstElIdx = 0; dstElIdx < dstElements.length(); dstElIdx++) {
						final String dstElId = dstElements.getJSONObject(
								dstElIdx).getString("id");
						final String srcElId = srcElements.getJSONObject(
								srcElIdx).getString("id");
						if (dstElId.equals(srcElId)) {
							setWorstResult(srcElements.getJSONObject(srcElIdx),
									dstElements.getJSONObject(dstElIdx));
							isDestElReplaced = true;
							break;
						}
					}
					if (!isDestElReplaced) {
						dstElements.put(srcElements.getJSONObject(srcElIdx));
					}
				}
			} else {
				dstItem.put("elements", srcElements);
			}
		}
	}

	private static void updateMainReport(JSONArray mainArray, JSONArray nextPart) {
		Map<String, JSONObject> existingItems = new HashMap<String, JSONObject>();
		for (int mainIdx = 0; mainIdx < mainArray.length(); mainIdx++) {
			existingItems.put(mainArray.getJSONObject(mainIdx).getString("id"),
					mainArray.getJSONObject(mainIdx));
		}

		for (int partIdx = 0; partIdx < nextPart.length(); partIdx++) {
			JSONObject partItem = nextPart.getJSONObject(partIdx);
			final String partId = partItem.getString("id");
			if (existingItems.containsKey(partId)) {
				importElements(partItem, existingItems.get(partId));
			} else {
				mainArray.put(partItem);
			}
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
