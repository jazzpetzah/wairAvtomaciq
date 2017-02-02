package com.wearezeta.auto.common.driver.facebook_ios_driver;

public class FBPinchArguments {
    private double scale;
    private double velocity;

    public FBPinchArguments(double scale, double velocity) {
        this.scale = scale;
        this.velocity = velocity;
    }

    public double getScale() {
        return scale;
    }

    public double getVelocity() {
        return velocity;
    }
}
