package com.wire.picklejar.construct;

import com.wire.picklejar.PickleJar;
import com.wire.picklejar.execution.PickleExecutor;
import static com.wire.picklejar.execution.PickleExecutor.replaceExampleOccurences;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnusedStepChecker {

    private static final Logger LOG = LoggerFactory.getLogger(UnusedStepChecker.class.getSimpleName());

    public static void main(String[] args) {
        LOG.info("Searching for unused steps...");
        findUnusedSteps(getAllGherkinSteps()).forEach((exp, method) -> {
            LOG.warn(String.format("'%s' \n\texpression with method '%s#%s(...)' seems to be unused", exp, method.
                    getDeclaringClass(), method.getName()));
        });
    }

    private static Set<String> getAllGherkinSteps() {
        return PickleJar.getAllTestcases().parallelStream()
                .map((testcase) -> new TestCase(testcase))
                .map((generateTestCase) -> {
                    return generateTestCase.getSteps().parallelStream()
                            .map((rawStep) -> {
                                return replaceExampleOccurences(rawStep, generateTestCase.getExamples());
                            })
                            .collect(Collectors.toSet());
                })
                .flatMap(testCaseStepSet -> testCaseStepSet.parallelStream())
                .collect(Collectors.toSet());
    }

    private static Map<String, Method> findUnusedSteps(Set<String> gherkinSteps) {
        Map<String, Method> unusedSteps = new HashMap<>(new PickleExecutor().getMethodCache());
        Set<String> usedSteps = unusedSteps.entrySet().parallelStream()
                .map((entry) -> {
                    return gherkinSteps.parallelStream()
                            .filter((step) -> step.matches(entry.getKey()))
                            .map((step) -> entry.getKey())
                            .findAny();
                })
                .filter((any) -> any.isPresent())
                .map((any) -> any.get())
                .collect(Collectors.toSet());

        usedSteps.forEach((exp) -> {
            unusedSteps.remove(exp);
        });
        LOG.debug(String.format("Found %d used steps", usedSteps.size()));
        LOG.info(String.format("Found %d unused steps", unusedSteps.size()));
        return unusedSteps;
    }
}
