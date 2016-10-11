package com.wearezeta.auto.common.driver.facebook_ios_driver;

public enum FBDeviceRotation {
    PORTRAIT(0), PORTRAIT_UPSIDE_DOWN(180), LANDSCAPE_LEFT(270), LANDSCAPE_RIGHT(90);

    private final int angle;

    FBDeviceRotation(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return this.angle;
    }

    public static FBDeviceRotation fromAngle(int angle) {
        if (angle > -90 && angle < 90) {
            return PORTRAIT;
        } else if (angle >= 90 && angle < 180) {
            return LANDSCAPE_RIGHT;
        } else if (angle >= 180 && angle < 270) {
            return PORTRAIT_UPSIDE_DOWN;
        } else if (angle >= 270 && angle < 360) {
            return LANDSCAPE_LEFT;
        }
        throw new IllegalArgumentException(
                String.format("Orientation value for %s angle is unknown", angle));
    }
}
