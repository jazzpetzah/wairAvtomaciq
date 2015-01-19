package com.wearezeta.zephyr_sync.storages;

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
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wearezeta.zephyr_sync.CucumberTestcase;
import com.wearezeta.zephyr_sync.Testcase;

public class GherkinFile extends TestcasesStorage {
	private String path;

	public GherkinFile(String path) {
		this.path = path;
	}

	private String gherkinToJSON() throws UnsupportedEncodingException,
			FileNotFoundException {
		String gherkin = FixJava.readReader(new InputStreamReader(
				new FileInputStream(path), "UTF-8"));

		StringBuilder json = new StringBuilder();
		JSONFormatter formatter = new JSONFormatter(json);

		Parser parser = new Parser(formatter);
		parser.parse(gherkin, path, 0);
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
	public List<CucumberTestcase> getTestcases() throws Exception {
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

	private void updateJSONTestcase(CucumberTestcase updatedTCInfo,
			JSONArray allGroups) {
		for (int groupIdx = 0; groupIdx < allGroups.length(); groupIdx++) {
			JSONObject group = allGroups.getJSONObject(groupIdx);
			JSONArray allTestCases = group.getJSONArray("elements");
			for (int tcIdx = 0; tcIdx < allTestCases.length(); tcIdx++) {
				JSONObject testCase = allTestCases.getJSONObject(tcIdx);

				final String cucumberId = testCase.getString("id");
				if (!cucumberId.equals(updatedTCInfo.getCucumberId())) {
					continue;
				}

				if (updatedTCInfo.getTags().size() > 0) {
					Collection<JSONObject> newTags = new ArrayList<JSONObject>();
					for (String tag : updatedTCInfo.getTags()) {
						JSONObject tagInfo = new JSONObject();
						tagInfo.put("name", tag);
						// actually, we don't care about the number here
						// the parser is smart enough to renumerate all lines
						tagInfo.put("line", 1);
						newTags.add(tagInfo);
					}
					testCase.put("tags", newTags);
				} else if (testCase.has("tags")) {
					testCase.remove("tags");
				}
			}
		}
	}

	@Override
	public void syncTestcases(List<? extends Testcase> testcases)
			throws Exception {
		final String json = gherkinToJSON();

		JSONArray resultJSON = new JSONArray(json);
		for (Testcase tc : testcases) {
			if (!tc.getIsChanged()) {
				continue;
			}
			updateJSONTestcase((CucumberTestcase) tc, resultJSON);
		}

		final String gherkin = JSONtoGherkin(resultJSON.toString());
		FileUtils.writeStringToFile(new File(path), gherkin, "UTF-8");
	}
}
