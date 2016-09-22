package com.wearezeta.auto.common.driver.facebook_ios_driver;


public class DragArguments {
    private double fromX;
    private double fromY;
    private double toX;
    private double toY;
    private double durationSeconds;

    public DragArguments(double fromX, double fromY, double toX, double toY, double durationSeconds) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        validateDurationValue(durationSeconds);
        this.durationSeconds = durationSeconds;
    }

    private static final double MAX_DURATION_VALUE = 60;
    private static final double MIN_DURATION_VALUE = 0.5;

    private static void validateDurationValue(double actualValue) {
        if (actualValue < MIN_DURATION_VALUE || actualValue > MAX_DURATION_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "Duration value is expected to be in range [%.2f seconds, %.2f seconds]. " +
                            "The actual duration value %.2f does not fit into this range.",
                    MIN_DURATION_VALUE, MAX_DURATION_VALUE, actualValue));
        }
    }

    public double getFromX() {
        return fromX;
    }

    public double getFromY() {
        return fromY;
    }

    public double getToX() {
        return toX;
    }

    public double getToY() {
        return toY;
    }

    public double getDurationSeconds() {
        return durationSeconds;
    }
}
