package com.wearezeta.auto.android_tablet.common;

import java.util.Optional;

import org.openqa.selenium.ScreenOrientation;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * This class is a dirty fix for App/Selendroid(?) issue The problem is that
 * device originalOrientation is sometimes overriden without any reason and we have to
 * fix it in such a tricky way
 *
 */
public final class ScreenOrientationHelper {

    public static final long ROTATION_DELAY_MS = 1500;

    ScreenOrientationHelper() {
    }

    private Optional<ScreenOrientation> originalOrientation = Optional.empty();

    public void setOriginalOrientation(ScreenOrientation originalOrientation) {
        this.originalOrientation = Optional.of(originalOrientation);
    }

    public Optional<ScreenOrientation> getOriginalOrientation() {
        return originalOrientation;
    }

    public void resetOrientation() {
        this.originalOrientation = Optional.empty();
    }

    public ScreenOrientation fixOrientation(final ZetaAndroidDriver driver) throws Exception {
        final ScreenOrientation original = this.originalOrientation.orElseThrow(
                () -> new IllegalStateException("Original orientation value has not been set before")
        );
        final ScreenOrientation currentOrientation = driver.getOrientation();
        if (original != currentOrientation) {
            driver.rotate(original);
            Thread.sleep(ROTATION_DELAY_MS);
            return original;
        }
        return currentOrientation;
    }
}
