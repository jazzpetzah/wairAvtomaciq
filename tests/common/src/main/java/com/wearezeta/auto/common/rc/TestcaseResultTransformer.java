package com.wearezeta.auto.common.rc;

import gherkin.formatter.model.Result;
import gherkin.formatter.model.Step;

import java.util.Map;
import java.util.Map.Entry;


public abstract class TestcaseResultTransformer {
	private static final String GIVEN_KEYWORD = "given";

    private Map<Step, String> testcase;

    public TestcaseResultTransformer(Map<Step, String> testcase) {
        this.testcase = testcase;
    }

	protected boolean isPassed() {
		boolean isPending = false;
		for (Entry<Step, String> entry : testcase.entrySet()) {
			final String stepResult = entry.getValue();
			final Step stepObj = entry.getKey();
			if (stepResult.equals(Result.UNDEFINED.toString())) {
				isPending = true;
			} else if (!stepResult.equals(Result.PASSED.toString())
					&& !isPending) {
				return false;
			} else if (stepResult.equals(Result.FAILED.toString())) {
				if (!stepObj.getKeyword().toLowerCase().trim()
						.equals(GIVEN_KEYWORD)) {
					return false;
				}
			}
		}
		return true;
	}

	protected boolean isFailed() {
		for (Entry<Step, String> entry : testcase.entrySet()) {
			final String stepResult = entry.getValue();
			final Step stepObj = entry.getKey();
			if (stepResult.equals(Result.FAILED.toString())) {
				if (!stepObj.getKeyword().toLowerCase().trim()
						.equals(GIVEN_KEYWORD)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean isSkipped() {
		for (Entry<Step, String> entry : testcase.entrySet()) {
			final String stepResult = entry.getValue();
			if (stepResult.equals(Result.SKIPPED.toString())) {
				return false;
			}
		}
		return true;
	}
}
