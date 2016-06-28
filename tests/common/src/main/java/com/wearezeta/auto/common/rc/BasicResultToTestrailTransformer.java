package com.wearezeta.auto.common.rc;


import com.wearezeta.auto.common.testrail.TestrailExecutionStatus;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;

public abstract class BasicResultToTestrailTransformer {

    private static final Logger LOG = Logger.getLogger(BasicResultToTestrailTransformer.class.getName());
    
    public static final String SKIPPED = "skipped";
    public static final String UNDEFINED = "undefined";
    public static final String PASSED = "passed";
    public static final String FAILED = "failed";
    
    private final Map<String, String> scenario;

    public BasicResultToTestrailTransformer(Map<String, String> scenario) {
        Objects.requireNonNull(scenario);
        this.scenario = scenario;
    }
    
    public abstract TestrailExecutionStatus transform();

    protected boolean isPassed() {
        boolean isPending = false;
        for (Map.Entry<String, String> entry : scenario.entrySet()) {
            final String stepResult = entry.getValue();
            final String stepName = entry.getKey();
            if (stepResult == null || stepName == null) {
                continue;
            }
            if (stepResult.equals(UNDEFINED)) {
                isPending = true;
            } else if (!stepResult.equals(PASSED) && !isPending) {
                return false;
            } else if (stepResult.equals(FAILED)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isFailed() {
        for (Map.Entry<String, String> entry : scenario.entrySet()) {
            final String stepResult = entry.getValue();
            final String stepName = entry.getKey();
            if (stepResult == null || stepName == null) {
                continue;
            }
            if (stepResult.equals(FAILED)) {
                return true;
            }
        }
        return false;
    }

    protected boolean isSkipped() {
        for (Map.Entry<String, String> entry : scenario.entrySet()) {
            final String stepResult = entry.getValue();
            if (stepResult == null) {
                continue;
            }
            if (stepResult.equals(SKIPPED)) {
                return true;
            }
        }
        return false;
    }
    
    protected void printStepResults(){
        for (Map.Entry<String, String> entry : scenario.entrySet()) {
            LOG.debug(String.format("%s -\t %s", entry.getValue(), entry.getKey()));
        }
    }
    
}
