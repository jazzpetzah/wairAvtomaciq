package com.wearezeta.suite_splitter.storages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.wearezeta.suite_splitter.Testcase;

public abstract class TestcasesStorage {
	private String path;

	public TestcasesStorage(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public abstract List<? extends Testcase> getTestcases() throws Throwable;

	public List<? extends Testcase> getTestcases(Set<String> includeTags,
			Set<String> excludeTags) throws Throwable {
		List<Testcase> resultList = new ArrayList<Testcase>();

		for (Testcase tc : this.getTestcases()) {
			if (includeTags == null
					|| (includeTags != null && !Collections.disjoint(
							tc.getTags(), includeTags))) {
				if (excludeTags == null
						|| (excludeTags != null && Collections.disjoint(
								tc.getTags(), excludeTags))) {
					resultList.add(tc);
				}
			}
		}

		return resultList;
	}
}
