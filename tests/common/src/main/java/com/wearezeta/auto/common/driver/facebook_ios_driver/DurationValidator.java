package com.wearezeta.auto.common.driver.facebook_ios_driver;

class DurationValidator {
    private static final double MAX_DURATION_VALUE = 60;
    private static final double MIN_DURATION_VALUE = 0.5;

    public static void validate(double actualValue) {
        if (actualValue < MIN_DURATION_VALUE || actualValue > MAX_DURATION_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "Duration value is expected to be in range [%.2f seconds, %.2f seconds]. " +
                            "The actual duration value %.2f does not fit into this range.",
                    MIN_DURATION_VALUE, MAX_DURATION_VALUE, actualValue));
        }
    }
}
