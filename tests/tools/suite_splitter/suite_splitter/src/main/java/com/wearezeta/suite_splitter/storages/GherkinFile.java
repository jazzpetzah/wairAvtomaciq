package com.wearezeta.suite_splitter.storages;

import gherkin.JSONParser;
import gherkin.formatter.JSONFormatter;
import gherkin.formatter.PrettyFormatter;
import gherkin.parser.Parser;
import gherkin.util.FixJava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.suite_splitter.CucumberTestcase;
import com.wearezeta.suite_splitter.Testcase;

public class GherkinFile extends TestcasesStorage {
	public GherkinFile(String path) {
		super(path);
	}

	private String gherkinToJSON() throws UnsupportedEncodingException,
			FileNotFoundException {
		String gherkin = FixJava.readReader(new InputStreamReader(
				new FileInputStream(getPath()), "UTF-8"));

		StringBuilder json = new StringBuilder();
		JSONFormatter formatter = new JSONFormatter(json);

		Parser parser = new Parser(formatter);
		parser.parse(gherkin, getPath(), 0);
		formatter.done();
		formatter.close();
		return json.toString();
	}

	private String JSONtoGherkin(String json) {
		StringBuilder gherkin = new StringBuilder();
		PrettyFormatter formatter = new PrettyFormatter(gherkin, true, false);

		JSONParser parser = new JSONParser(formatter, formatter);
		parser.parse(json);
		formatter.done();
		formatter.close();
		return gherkin.toString();
	}

	@Override
	public List<CucumberTestcase> getTestcases() throws Throwable {
		final String json = gherkinToJSON();
		List<CucumberTestcase> resultList = new ArrayList<CucumberTestcase>();

		JSONArray allGroups = new JSONArray(json);
		for (int groupIdx = 0; groupIdx < allGroups.length(); groupIdx++) {
			JSONObject group = allGroups.getJSONObject(groupIdx);
			if (!group.has("elements")) {
				continue;
			}
			JSONArray allTestCases = group.getJSONArray("elements");
			for (int tcIdx = 0; tcIdx < allTestCases.length(); tcIdx++) {
				JSONObject testCase = allTestCases.getJSONObject(tcIdx);
				final String testCaseName = testCase.getString("name");

				Set<String> tags = new LinkedHashSet<String>();
				if (testCase.has("tags")) {
					JSONArray tagsList = testCase.getJSONArray("tags");
					for (int tagIdx = 0; tagIdx < tagsList.length(); tagIdx++) {
						tags.add(tagsList.getJSONObject(tagIdx).getString(
								"name"));
					}
				}
				final String cucumberId = testCase.getString("id");

				CucumberTestcase tc = new CucumberTestcase(
						Testcase.extractIdsFromTags(tags), testCaseName, tags,
						cucumberId);
				resultList.add(tc);
			}
		}
		return resultList;
	}

	private void syncJSONTestcases(List<? extends Testcase> testcasesToKeep,
			JSONArray allGroups) {
		List<String> tcIDsToKeep = new ArrayList<String>();
		for (Testcase tc : testcasesToKeep) {
			tcIDsToKeep.add(((CucumberTestcase) tc).getCucumberId());
		}

		int groupIdx = 0;
		for (groupIdx = 0; groupIdx < allGroups.length(); groupIdx++) {
			JSONObject group = allGroups.getJSONObject(groupIdx);
			if (!group.has("elements")) {
				continue;
			}
			JSONArray allTestCases = group.getJSONArray("elements");
			int tcIdx = 0;
			while (tcIdx < allTestCases.length()) {
				JSONObject testCase = allTestCases.getJSONObject(tcIdx);
				final String cucumberId = testCase.getString("id");
				if (tcIDsToKeep.contains(cucumberId)) {
					tcIdx++;
				} else {
					allTestCases.remove(tcIdx);
				}
			}
		}

		// Cleanup of empty groups
		groupIdx = 0;
		while (groupIdx < allGroups.length()) {
			JSONObject group = allGroups.getJSONObject(groupIdx);
			if (group.has("elements")
					&& group.getJSONArray("elements").length() == 0) {
				allGroups.remove(groupIdx);
			} else {
				groupIdx++;
			}
		}
	}

	public void saveTestcasesAs(List<? extends Testcase> testcases,
			String dstPath) throws Throwable {
		final String json = gherkinToJSON();

		JSONArray resultJSON = new JSONArray(json);
		syncJSONTestcases(testcases, resultJSON);

		final String gherkin = JSONtoGherkin(resultJSON.toString());
		FileUtils.writeStringToFile(new File(dstPath), gherkin, "UTF-8");
	}
}
