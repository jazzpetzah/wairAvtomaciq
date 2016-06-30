package com.wearezeta.auto.common.rc;

import com.wearezeta.auto.common.testrail.TestrailExecutionStatus;

import java.util.Map;
import org.apache.log4j.Logger;


public class BasicScenarioResultToTestrailTransformer extends BasicResultToTestrailTransformer {
    
    private static final Logger LOG = Logger.getLogger(BasicResultToTestrailTransformer.class.getName());

    public BasicScenarioResultToTestrailTransformer(Map<String, String> scenario) {
        super(scenario);
    }

    @Override
    public TestrailExecutionStatus transform() {
        printStepResults();
        if (isPassed()) {
            return TestrailExecutionStatus.Passed;
        } else if (isFailed()) {
            return TestrailExecutionStatus.Failed;
        }
        return TestrailExecutionStatus.Retest;
    }
    
}
