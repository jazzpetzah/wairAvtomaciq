package com.wearezeta.zephyr_sync.reporting;

import java.util.List;

public class ReportModel {
	private List<TestcaseGroup> aDataSet;
	String title;

	public ReportModel(String title, List<TestcaseGroup> aDataSet) {
		this.title = title;
		this.aDataSet = aDataSet;
	}

	List<TestcaseGroup> dataSet() {
		return this.aDataSet;
	}

	static public class TestcaseGroup {
		String name;
		List<String> aDetails;
		int count;
		
		List<String> details() {
			return this.aDetails;
		}
		
		public TestcaseGroup(String name, List<String> details, int count) {
			this.name = name;
			this.aDetails = details;
			this.count = count;
		}
	}
}
