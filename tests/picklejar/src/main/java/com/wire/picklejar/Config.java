package com.wire.picklejar;

public class Config {

    private static final String EXECUTION_TAG_KEY = "picklejar.tag";
    private static final String CUCUMBER_REPORT_PATH_KEY = "picklejar.cucumber.report.path";
    private static final String SCREENSHOT_PATH_KEY = "picklejar.screenshot.path";
    private static final String FEATURE_PACKAGE_KEY = "picklejar.feature.package";
    private static final String STEP_PACKAGES_KEY = "picklejar.steps.package";
    private static final String GENERATED_TEST_PACKAGE_KEY = "picklejar.tests.package";
    private static final String PICKLE_MAX_PARALLEL_KEY = "picklejar.parallel.max";
    private static final String RANDOM_KEY = "picklejar.random";
    
    public static final String FEATURE_EXTENSION = "feature";
    public static final String CUCUMBER_REPORT_PATH = System.getProperty(CUCUMBER_REPORT_PATH_KEY, "target/");
    public static final String SCREENSHOT_PATH = System.getProperty(SCREENSHOT_PATH_KEY, "target/Images/");
    public static final String[] STEP_PACKAGES = System.getProperty(STEP_PACKAGES_KEY, "com").split(",");
    public static final String GENERATED_TEST_PACKAGE = System.getProperty(GENERATED_TEST_PACKAGE_KEY, "c.pj.g");
    public static final String FEATURE_PACKAGE = System.getProperty(FEATURE_PACKAGE_KEY, "com");
    public static final String[] EXECUTION_TAG = System.getProperty(EXECUTION_TAG_KEY, "@torun").split(",");
    public static final int PICKLE_MAX_PARALLEL = Integer.parseInt(System.getProperty(
            PICKLE_MAX_PARALLEL_KEY,
            Integer.toString(Runtime.getRuntime().availableProcessors())));
    public static final int RANDOM = Integer.parseInt(System.getProperty(RANDOM_KEY, "0"));

}
