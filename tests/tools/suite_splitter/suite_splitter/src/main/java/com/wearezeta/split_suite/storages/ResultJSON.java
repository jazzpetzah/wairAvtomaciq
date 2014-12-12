package com.wearezeta.split_suite.storages;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.split_suite.Testcase;

public class ResultJSON extends TestcasesStorage {
	private static final String NEW_ID_FORMAT = "%s-part%s";
	private static final String NEW_NAME_FORMAT = "%s Part %s";

	public ResultJSON(String path) {
		super(path);
	}

	@Override
	public List<Testcase> getTestcases() throws Throwable {
		throw new RuntimeException("Not implemented");
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
