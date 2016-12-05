package com.wire.picklejar.execution;

import com.wire.picklejar.PickleJar;
import com.wire.picklejar.execution.exception.StepNotExecutableException;
import com.wire.picklejar.execution.exception.StepNotFoundException;
import com.wire.picklejar.gherkin.model.Scenario;
import com.wire.picklejar.gherkin.model.Step;
import java.util.Map;

public interface PickleJarTestHook {
    
    public long invoke(String rawStep, Map<String, String> exampleRow) throws StepNotExecutableException, StepNotFoundException;
    
    public void setPickle(PickleJar pickle);
    
    public PickleJar getPickle();

    public void onBeforeScenario(Scenario scenario) throws Throwable;

    public void onBeforeStep(Scenario scenario, Step step) throws Throwable;

    public void onStepPassed(Scenario scenario, Step step) throws Throwable;

    public void onStepFailed(Scenario scenario, Step step) throws Throwable;

    public void onStepSkipped(Scenario scenario, Step step) throws Throwable;

    public void onAfterStep(Scenario scenario, Step step) throws Throwable;

    public void onAfterScenario(Scenario scenario) throws Throwable;
}
