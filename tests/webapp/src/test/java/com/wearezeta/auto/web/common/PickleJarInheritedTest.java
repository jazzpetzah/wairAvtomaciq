package com.wearezeta.auto.web.common;

import java.util.List;
import java.util.Map;
import org.junit.runner.RunWith;
import com.wire.picklejar.PickleJar;
import com.wire.picklejar.execution.PickleJarTest;
import java.io.IOException;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class PickleJarInheritedTest extends PickleJarTest {

    @Parameters(name = "{0}: {1} {2}")
    public static Collection<Object[]> getTestcases() throws IOException {
        return PickleJar.getTestcases();
    }

    public PickleJarInheritedTest(String feature, String testcase, Integer exampleNum, List<String> steps,
            Map<String, String> examples, List<String> tags) throws Exception {
        super(new PickleJarHook(), feature, testcase, exampleNum, steps, examples, tags);
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        PickleJarTest.setUpClass();
    }

    @Before
    @Override
    public void setUp() throws Throwable {
        super.setUp();
    }

    @Test
    @Override
    public void test() throws Throwable {
        super.test();
    }

    @After
    @Override
    public void tearDown() throws Throwable {
        super.tearDown();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        PickleJarTest.tearDownClass();
    }
}