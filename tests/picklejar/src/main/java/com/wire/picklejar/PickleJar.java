package com.wire.picklejar;

import com.wire.picklejar.execution.PickleExecutor;
import com.wire.picklejar.scan.PickleJarScanner;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface PickleJar {

    /**
     * Info:<br>
     * - Generates testclasses based on a template and maven-ant-plugin - Test classes are generated on a
     * per-scenario/per-example basis (enables live results)<br>
     *
     * Features:<br>
     * - Can run steps of other jars<br>
     * - Execution of single example of testcase<br>
     * - Inherited parallelism of junit<br>
     * - Inherited rerun feature of junit<br>
     * - Inherited live results feature of junit<br>
     *
     * Con:<br>
     * - Currently the full load is on one machine
     *
     * Restrictions:<br>
     *
     * Requirements:<br>
     * - Surefire plugin v2.19.1 needed for parameterized test (re-)execution<br>
     * - Surefire plugin v2.18 supports "rerunFailingTestsCount" for deflake
     *
     * TODO:<br>
     * - [low prio] deal with multiple package paths for step classes<br>
     * - deal with multiple tags for tests (maybe AND, OR relations)<br>
     * - (maybe) run tests by category<br>
     * - (maybe) run tests by id<br>
     * - (maybe) Remove Cucumber dependency by introducing own annotations - Handle PendingException properly (atm it's failing
     * instead of skipping)
     */
    public static Collection<Object[]> getTestcases() {
        return PickleJarScanner.getTestcases();
    }

    public static Collection<Object[]> getTestcases(String[] filterTags) {
        return PickleJarScanner.getTestcases(filterTags);
    }
    
    public static Collection<Object[]> getRandom(int randomCount) {
        return getRandom((List<Object[]>) PickleJarScanner.getTestcases(), randomCount);
    }

    public static Collection<Object[]> getRandom(String[] filterTags, int randomCount) {
        return getRandom((List<Object[]>) PickleJarScanner.getTestcases(filterTags), randomCount);
    }
    
    public static Collection<Object[]> getRandom(List<Object[]> testcases, int randomCount) {
        Collections.shuffle(testcases);
        return testcases.subList(0, testcases.size() > randomCount ? randomCount : testcases.size());
    }

    PickleExecutor getExecutor();

    void reset();

}
