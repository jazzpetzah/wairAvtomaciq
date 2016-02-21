package com.wearezeta.auto.android.common;

import com.wearezeta.auto.common.ImageUtil;
import java.awt.image.BufferedImage;
import java.util.Optional;


public class ElementState {
    private static final long MS_INTERVAL = 500;

    private Optional<BufferedImage> previousScreenshot = Optional.empty();
    private StateGetter stateGetter;

    public ElementState(StateGetter stateGetter) throws Exception {
        this.stateGetter = stateGetter;
    }

    public void remember() throws Exception {
        this.previousScreenshot = Optional.of(stateGetter.getState());
    }

    public boolean isChanged(int timeoutSeconds, double minScore) throws Exception {
        final long msStarted = System.currentTimeMillis();
        do {
            final BufferedImage currentState = stateGetter.getState();
            final double score = ImageUtil.getOverlapScore(
                    this.previousScreenshot.orElseThrow(
                            () -> new IllegalStateException("Please remember the previous element state first")),
                    currentState, ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
            if (score < minScore) {
                return true;
            }
            Thread.sleep(MS_INTERVAL);
        } while (System.currentTimeMillis() - msStarted <= timeoutSeconds);
        return false;
    }

    public boolean isNotChanged(int timeoutSeconds, double minScore) throws Exception {
        final long msStarted = System.currentTimeMillis();
        do {
            final BufferedImage currentState = stateGetter.getState();
            final double score = ImageUtil.getOverlapScore(
                    this.previousScreenshot.orElseThrow(
                            () -> new IllegalStateException("Please remember the previous element state first")),
                    currentState, ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
            if (score > minScore) {
                return true;
            }
            Thread.sleep(MS_INTERVAL);
        } while (System.currentTimeMillis() - msStarted <= timeoutSeconds);
        return false;
    }
}
