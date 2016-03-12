package com.wire.picklejar.execution;

import com.wire.picklejar.PickleJar;
import java.util.List;
import java.util.Map;
import com.wire.picklejar.PickleJarJUnitProvider;
import cucumber.runtime.ScenarioImpl;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Scenario;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PickleJarTest {
    
    private static final Logger LOG = LogManager.getLogger();

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

    private final ScenarioImpl scenario;
    private final String feature;
    private final String testcase;
    private final List<String> steps;
    private final Map<String, String> examples;

    /**
     *
     * @param feature
     * @param testcase
     * @param exampleNum
     * @param steps
     * @param examples
     */
    public PickleJarTest(String feature, String testcase, Integer exampleNum, List<String> steps, Map<String, String> examples, Reporter reporter) throws Exception {
        this.feature = feature;
        this.testcase = testcase;
        this.steps = steps;
        this.examples = examples;
        this.reporter = reporter;
        this.scenario = new ScenarioImpl(reporter, Collections.emptySet(), new Scenario(Collections.emptyList(),
                Collections.emptyList(), "keyword", testcase, "desc", 1, exampleNum.toString()));
    }

    @BeforeClass
    protected static void setUpClass() throws Exception {
        LOG.log(Level.INFO, "### Before full testrun");
    }

    @Before
    protected void setUp() throws Exception {
        LOG.log(Level.INFO, "### Before testcase Count: " + TEST_COUNTER.incrementAndGet());
        pickle.reset();
    }
    
    @Test
    public void test() throws Exception {
        LOG.log(Level.INFO, "Executing "+feature+": "+testcase);
    }

    @After
    protected void tearDown() throws Exception {
        LOG.log(Level.INFO, "### After testcase Count: " + TEST_COUNTER.decrementAndGet());
    }

    @AfterClass
    protected static void tearDownClass() throws Exception {
        LOG.log(Level.INFO, "### After full testrun");
    }

    public PickleJar getPickle() {
        return pickle;
    }

    public Reporter getReporter() {
        return reporter;
    }

    public ScenarioImpl getScenario() {
        return scenario;
    }

    public String getFeature() {
        return feature;
    }

    public String getTestcase() {
        return testcase;
    }

    public List<String> getSteps() {
        return steps;
    }

    public Map<String, String> getExamples() {
        return examples;
    }
    
    

}
