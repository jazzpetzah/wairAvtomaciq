package com.wearezeta.split_suite.reporting;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.wearezeta.split_suite.Testcase;

public class ReportModel {
	private Map<String, Map<String, List<String>>> splittedFeatures = new LinkedHashMap<String, Map<String, List<String>>>();

	public ReportModel() {
		// TODO Auto-generated constructor stub
	}

	private static List<String> getTestcasesInfo(
			List<? extends Testcase> testcases) {
		List<String> result = new ArrayList<String>();
		for (Testcase tc : testcases) {
			result.add(String.format("[%s] -> %s",
					StringUtils.join(tc.getTags(), ", "), tc.getName()));
		}
		return result;
	}

	public void addFeaturesPart(String containerPath, Map<String, List<? extends Testcase>> content) {
			Map<String, List<String>> partMap = new LinkedHashMap<String, List<String>>();
			for(Entry<String, List<? extends Testcase>> entry : content.entrySet()) {
				partMap.put(new File(entry.getKey()).getName(), getTestcasesInfo(entry.getValue()));
			}
			splittedFeatures.put(containerPath, partMap);
	}

	public Map<String, Map<String, List<String>>> getSplittedFeatures() {
		return this.splittedFeatures;
	}

}
