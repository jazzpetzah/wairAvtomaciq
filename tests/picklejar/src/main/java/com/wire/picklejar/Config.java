package com.wire.picklejar;

public class Config {

    private static final String EXECUTION_TAG_KEY = "picklejar.tag";
    private static final String FEATURE_PACKAGE_KEY = "picklejar.feature.package";
    private static final String STEP_PACKAGE_KEY = "picklejar.steps.package";
    private static final String GENERATED_TEST_PACKAGE_KEY = "picklejar.tests.package";
    private static final String PICKLE_MAX_PARALLEL_KEY = "picklejar.parallel.max";
    public static final String FEATURE_EXTENSION = "feature";

    public static final String STEP_PACKAGE = System.getProperty(STEP_PACKAGE_KEY, "com");
    public static final String GENERATED_TEST_PACKAGE = System.getProperty(GENERATED_TEST_PACKAGE_KEY, "c.pj.g");
    public static final String FEATURE_PACKAGE = System.getProperty(FEATURE_PACKAGE_KEY, "com");
    public static final String[] EXECUTION_TAG = System.getProperty(EXECUTION_TAG_KEY, "@torun").split(",");
    public static final int PICKLE_MAX_PARALLEL = Integer.parseInt(System.getProperty(
            PICKLE_MAX_PARALLEL_KEY,
            Integer.toString(Runtime.getRuntime().availableProcessors())));

}
