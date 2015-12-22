package com.wearezeta.auto.common.rc;

import com.wearezeta.auto.common.zephyr.ZephyrExecutionStatus;
import gherkin.formatter.model.Step;

import java.util.Map;


public class TestcaseResultToZephyrTransformer extends TestcaseResultTransformer {

    public TestcaseResultToZephyrTransformer(Map<Step, String> testcase) {
        super(testcase);
    }

    public ZephyrExecutionStatus transform() {
        if (isPassed()) {
            return ZephyrExecutionStatus.Pass;
        } else if (isFailed()) {
            return ZephyrExecutionStatus.Fail;
        } else if (isSkipped()) {
            return ZephyrExecutionStatus.Blocked;
        }
        return ZephyrExecutionStatus.Fail;
    }
}
