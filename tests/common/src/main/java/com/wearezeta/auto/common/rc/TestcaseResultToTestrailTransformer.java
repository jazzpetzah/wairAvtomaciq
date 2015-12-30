package com.wearezeta.auto.common.rc;

import com.wearezeta.auto.common.testrail.TestrailExecutionStatus;
import gherkin.formatter.model.Step;

import java.util.Map;


public class TestcaseResultToTestrailTransformer extends TestcaseResultTransformer {

    public TestcaseResultToTestrailTransformer(Map<Step, String> testcase) {
        super(testcase);
    }

    public TestrailExecutionStatus transform() {
        if (isPassed()) {
            return TestrailExecutionStatus.Passed;
        } else if (isFailed()) {
            return TestrailExecutionStatus.Failed;
        }
        return TestrailExecutionStatus.Retest;
    }
}
