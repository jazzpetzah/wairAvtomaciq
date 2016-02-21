package com.wearezeta.auto.common.misc;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Function;


public class ElementState {
    private static final Logger log = ZetaLogger.getLog(ElementState.class.getSimpleName());

    private static final long MS_INTERVAL = 500;

    private Optional<BufferedImage> previousScreenshot = Optional.empty();
    private FunctionalInterfaces.StateGetter stateGetter;

    public ElementState(FunctionalInterfaces.StateGetter stateGetter) throws Exception {
        this.stateGetter = stateGetter;
    }

    public void remember() throws Exception {
        this.previousScreenshot = Optional.of(stateGetter.getState());
    }

    private boolean checkState(Function<Double, Boolean> checkerFunc, int timeoutSeconds) throws Exception {
        final long msStarted = System.currentTimeMillis();
        do {
            final BufferedImage currentState = stateGetter.getState();
            final double score = ImageUtil.getOverlapScore(
                    this.previousScreenshot.orElseThrow(
                            () -> new IllegalStateException("Please remember the previous element state first")),
                    currentState, ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
            log.debug(String.format("Actual score: %.2f; Time left: %s ms", score,
                    System.currentTimeMillis() - msStarted));
            if (checkerFunc.apply(score)) {
                return true;
            }
            Thread.sleep(MS_INTERVAL);
        } while (System.currentTimeMillis() - msStarted <= timeoutSeconds);
        return false;
    }

    public boolean isChanged(int timeoutSeconds, double minScore) throws Exception {
        log.debug(String.format(
                "Checking if element state has been changed in %s seconds (Min Score: %.2f)...",
                timeoutSeconds, minScore));
        return checkState((x) -> x < minScore, timeoutSeconds);
    }

    public boolean isNotChanged(int timeoutSeconds, double minScore) throws Exception {
        log.debug(String.format(
                "Checking if element state has NOT been changed in %s seconds (Min Score: %.2f)...",
                timeoutSeconds, minScore));
        return checkState((x) -> x >= minScore, timeoutSeconds);
    }
}
