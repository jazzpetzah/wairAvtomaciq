package com.wearezeta.auto.common.driver.facebook_ios_driver;

public enum FBDeviceOrientation {
    PORTRAIT(0), PORTRAIT_UPSIDE_DOWN(180), LANDSCAPE_LEFT(270), LANDSCAPE_RIGHT(90);

    private final int angle;

    FBDeviceOrientation(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return this.angle;
    }

    public static FBDeviceOrientation fromAngle(int angle) {
        switch (angle) {
            case 0:
                return PORTRAIT;
            case 90:
                return LANDSCAPE_RIGHT;
            case 180:
                return PORTRAIT_UPSIDE_DOWN;
            case 270:
                return LANDSCAPE_LEFT;
            default:
                throw new IllegalArgumentException(
                        String.format("Orientation value for %s angle is unknown", angle));
        }
    }
}
