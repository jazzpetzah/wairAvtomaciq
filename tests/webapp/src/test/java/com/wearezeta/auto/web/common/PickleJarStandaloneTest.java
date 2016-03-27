package com.wearezeta.auto.web.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wire.picklejar.PickleJar;
import com.wire.picklejar.PickleJarJUnitProvider;
import com.wire.picklejar.execution.ParallelParameterized;
import cucumber.runtime.ScenarioImpl;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Scenario;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@RunWith(ParallelParameterized.class)
public class PickleJarStandaloneTest {

    /**
     * Info:<br>
     * - Surefire plugin v2.19.1 needed for parameterized test execution<br>
     * - Convention: step classes must have a no-args constructor<br>
     *
     * Features:<br>
     * - Can run steps of other jars<br>
     * - Execution of single example of testcase<br>
     * - Inherited parallelism of junit<br>
     * - Inherited rerun feature of junit<br>
     *
     * Con:<br>
     *
     * TODO:<br>
     * - [low prio] deal with multiple package paths for step classes<br>
     * - [low prio] using proper logger<br>
     * - run tests by category<br>
     * - run tests by id<br>
     */
    private static final AtomicInteger TEST_COUNTER = new AtomicInteger(0);
    private final PickleJar pickle = new PickleJarJUnitProvider();

    private Reporter reporter;
    private Lifecycle lifecycle;

    private final ScenarioImpl scenario;

    private final String feature;
    private final String testcase;
    private final List<String> steps;
    private final Map<String, String> examples;

    @Parameters(name = "{0}: {1} {2}")
    public static Collection<Object[]> getTestcases() throws IOException {
        return PickleJar.getTestcases();
    }

    /**
     *
     * @param feature
     * @param testcase
     * @param exampleNum
     * @param steps
     * @param examples
     */
    public PickleJarStandaloneTest(String feature, String testcase, Integer exampleNum, List<String> steps,
            Map<String, String> examples) {
        this.feature = feature;
        this.testcase = testcase;
        this.steps = steps;
        this.examples = examples;
        reporter = new ZetaFormatter();
        this.scenario = new ScenarioImpl(reporter, Collections.emptySet(), new Scenario(Collections.emptyList(),
                Collections.emptyList(), "keyword", testcase, "desc", 1, exampleNum.toString()));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("### Before full testrun");
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("### Before testcase Count: " + TEST_COUNTER.incrementAndGet());
        lifecycle = new Lifecycle();
        lifecycle.setUp(scenario);
    }

    @Test
    public void test() throws Exception {
        System.out.println(feature);
        System.out.println(testcase);

        for (String step : steps) {
            pickle.getExecutor().invokeMethodForStep(step, examples, lifecycle.getContext());
        }
    }

    @After
    public void tearDown() throws Exception {
        lifecycle.tearDown(scenario);
        System.out.println("### After testcase Count: " + TEST_COUNTER.decrementAndGet());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("### After full testrun");
    }

}
