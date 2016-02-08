package com.wearezeta.auto.android;

import com.wearezeta.picklejar.PickleExecutor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import com.wearezeta.auto.android.steps.*;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.picklejar.PickleJar;
import cucumber.runtime.ScenarioImpl;
import gherkin.ast.ScenarioOutline;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Scenario;
import java.io.IOException;
import java.util.Collections;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

@RunWith(Parameterized.class)
public class PickleJarTest {

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
     * - parse feature files<br>
     * - run tests by category<br>
     * - run tests by id<br>
     */
    private static final String STEP_PACKAGE = "com.wearezeta.auto.android.steps";
    private static PickleExecutor stepExecutor;
    private static Reporter reporter;
    private static CommonAndroidSteps commonSteps;

    private final ScenarioImpl scenario;

    private final String feature;
    private final String testcase;
    private final List<String> steps;
    private final Map<String, String> examples;

    @Parameters(name = "{0}: {1} ({2})")
    public static Collection<Object[]> getTestcases() throws IOException {
        PickleJar pickle = new PickleJar(new String[]{"@torun"});

        // might fail when getting examples because it might be a scenario (without outline)
        // should be 'getFeatures'. we need features for generating reports anyway
        List<ScenarioOutline> scenarios = pickle.getScenarios();
        for (ScenarioOutline scenario : scenarios) {
            System.out.println(scenario.getName());
        }

        Collection<Object[]> params = new ArrayList<>();
        //TODO parse cucumber feature files
        //TODO create params for testcase, steps and examples
        //TODO add argument to execute tests by annotation

        Object[] set = new Object[5];

        //Feature
        set[0] = "E2EE";

        //Scenario
        set[1] = "Verify the appropriate device is signed out if you remove it from settings";

        //Example#
        set[2] = 1;

        List<String> steps = new ArrayList<>();
        steps.add("There are 1 users where <Name> is me");
        steps.add("I sign in using my email or phone number");
        steps.add("I accept First Time overlay as soon as it is visible");
        steps.add("User Myself removes all his registered OTR clients");
        steps.add("I see welcome screen");
        steps.add("I sign in using my email or phone number");
        steps.add("I accept First Time overlay as soon as it is visible");
        //Steplist
        set[3] = steps;

        Map<String, String> examples = new HashMap<>();
        examples.put("Name", "user1Name");
        //Example (one row)
        set[4] = examples;

        params.add(set);

        System.out.println("###############################################################################################");

        set = new Object[5];

        set[0] = "E2EE";

        set[1] = "Verify the appropriate device is signed out if you remove it from settings";

        set[2] = 2;

        steps = new ArrayList<>();
        steps.add("There are 1 users where <Name> is me");
        steps.add("I sign in using my email or phone number");
        steps.add("I accept First Time overlay as soon as it is visible");
        steps.add("User Myself removes all his registered OTR clients");
        steps.add("I see welcome screen");
        steps.add("I sign in using my email or phone number");
        steps.add("I accept First Time overlay as soon as it is visible");
        set[3] = steps;

        examples = new HashMap<>();
        examples.put("Name", "user2Name");
        set[4] = examples;

//        params.add(set);
        return params;
    }

    /**
     * @C3231 @rc @regression</br>
     * Scenario Outline: Verify the appropriate device is signed out if you remove it from settings</br>
     * Given There are 1 users where <Name> is me</br>
     * Given I sign in using my email or phone number</br>
     * Given I accept First Time overlay as soon as it is visible</br>
     * When User Myself removes all his registered OTR clients</br>
     * Then I see welcome screen</br>
     * And I sign in using my email or phone number</br>
     * Given I accept First Time overlay as soon as it is visible</br>
     * </br>
     * Examples:</br>
     * | Name |</br>
     * | user1Name |</br>
     *
     */
    /**
     *
     * @param feature
     * @param testcase
     * @param exampleNum
     * @param steps
     * @param examples
     */
    public PickleJarTest(String feature, String testcase, Integer exampleNum, List<String> steps, Map<String, String> examples) {
        this.feature = feature;
        this.testcase = testcase;
        this.steps = steps;
        this.examples = examples;
        this.scenario = new ScenarioImpl(reporter, Collections.emptySet(), new Scenario(Collections.emptyList(),
                Collections.emptyList(), "keyword", testcase, "desc", 1, exampleNum.toString()));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("### Before full testrun");
        stepExecutor = new PickleExecutor(STEP_PACKAGE);
        reporter = new ZetaFormatter();
        commonSteps = new CommonAndroidSteps();
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("### Before testcase");
        commonSteps.setUp(scenario);
    }

    @Test
    public void test() throws Exception {
        System.out.println(feature);
        System.out.println(testcase);
        for (String step : steps) {
            stepExecutor.invokeMethodForStep(step, examples);
        }
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("### After testcase");
        commonSteps.tearDown(scenario);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("### After full testrun");
    }

}
