package com.wearezeta.auto.common.driver.facebook_ios_driver;


import com.wearezeta.auto.common.misc.Timedelta;

public class FBDragArguments {
    private double fromX;
    private double fromY;
    private double toX;
    private double toY;
    private Timedelta duration;

    public FBDragArguments(double fromX, double fromY, double toX, double toY, Timedelta duration) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        DurationValidator.validate(duration.asFloatSeconds());
        this.duration = duration;
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

    public Timedelta getDuration() {
        return duration;
    }
}
