package com.wearezeta.auto.web.common;

import java.util.List;
import java.util.Map;
import org.junit.runner.RunWith;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wire.picklejar.PickleJar;
import com.wire.picklejar.execution.ParallelParameterized;
import com.wire.picklejar.execution.PickleJarTest;
import java.io.IOException;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

@RunWith(ParallelParameterized.class)
public class PickleJarInheritedTest extends PickleJarTest{
    
    private Lifecycle lifecycle;

    @Parameters(name = "{0}: {1} {2}")
    public static Collection<Object[]> getTestcases() throws IOException {
        return PickleJar.getTestcases();
    }

    public PickleJarInheritedTest(String feature, String testcase, Integer exampleNum, List<String> steps, Map<String, String> examples) throws Exception {
        super(feature, testcase, exampleNum, steps, examples, new ZetaFormatter());
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        PickleJarTest.setUpClass();
        System.out.println("### Before inherited full testrun");
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        lifecycle = new Lifecycle();
        lifecycle.setUp(getScenario());
    }

    @Test
    public void test() throws Exception {
        super.test();
        for (String step : getSteps()) {
            getPickle().getExecutor().invokeMethodForStep(step, getExamples(), lifecycle.getContext());
        }
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        lifecycle.tearDown(getScenario());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        PickleJarTest.tearDownClass();
        System.out.println("### After inherited full testrun");
    }

}
