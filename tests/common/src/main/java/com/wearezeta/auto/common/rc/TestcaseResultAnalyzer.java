package com.wearezeta.auto.common.rc;

import gherkin.formatter.model.Result;
import gherkin.formatter.model.Step;

import java.util.Map;
import java.util.Map.Entry;

import com.wearezeta.auto.common.zephyr.ZephyrExecutionStatus;

public class TestcaseResultAnalyzer {
	private static final String GIVEN_KEYWORD = "given";

	private static boolean isPassed(Map<Step, String> testcase) {
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

	private static boolean isFailed(Map<Step, String> testcase) {
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

	private static boolean isSkipped(Map<Step, String> testcase) {
		for (Entry<Step, String> entry : testcase.entrySet()) {
			final String stepResult = entry.getValue();
			if (stepResult.equals(Result.SKIPPED.toString())) {
				return false;
			}
		}
		return true;
	}

	public static ZephyrExecutionStatus analyzeSteps(Map<Step, String> testcase) {
		if (isPassed(testcase)) {
			return ZephyrExecutionStatus.Pass;
		} else if (isFailed(testcase)) {
			return ZephyrExecutionStatus.Fail;
		} else if (isSkipped(testcase)) {
			return ZephyrExecutionStatus.Blocked;
		}
		return ZephyrExecutionStatus.Fail;
	}
}
