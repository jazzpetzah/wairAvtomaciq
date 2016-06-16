package com.wearezeta.auto.common.misc;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Function;


public class ElementState {
    private static final Logger log = ZetaLogger.getLog(ElementState.class.getSimpleName());

    private static final long MS_INTERVAL = 500;

    private Optional<BufferedImage> previousScreenshot = Optional.empty();
    private FunctionalInterfaces.StateGetter stateGetter;

    public ElementState(FunctionalInterfaces.StateGetter stateGetter) {
        this.stateGetter = stateGetter;
    }

    public ElementState remember() throws Exception {
        final int maxRetries = 3;
        int nTry = 0;
        Exception savedException;
        do {
            try {
                this.previousScreenshot = Optional.of(stateGetter.getState());
                return this;
            } catch (StaleElementReferenceException e) {
                savedException = e;
                nTry++;
                Thread.sleep(MS_INTERVAL);
            }
        } while (nTry < maxRetries);
        throw savedException;
    }

    public ElementState remember(BufferedImage customInitialState) throws Exception {
        this.previousScreenshot = Optional.of(customInitialState);
        return this;
    }

    private boolean checkState(Function<Double, Boolean> checkerFunc, int timeoutSeconds) throws Exception {
        final long msTimeout = timeoutSeconds * 1000;
        final long msStarted = System.currentTimeMillis();
        do {
            try {
                final BufferedImage currentState = stateGetter.getState();
                final double score = ImageUtil.getOverlapScore(
                        this.previousScreenshot.orElseThrow(
                                () -> new IllegalStateException("Please remember the previous element state first")),
                        currentState, ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
                log.debug(String.format("Actual score: %.4f; Time left: %s ms", score,
                        msTimeout + msStarted - System.currentTimeMillis()));
                if (checkerFunc.apply(score)) {
                    return true;
                }
            } catch (StaleElementReferenceException e) {
                log.debug(String.format("Actual score: <calculation error>; Time left: %s ms",
                        msTimeout + msStarted - System.currentTimeMillis()));
            }
            Thread.sleep(MS_INTERVAL);
        } while (System.currentTimeMillis() - msStarted <= msTimeout);
        return false;
    }

    public boolean isChanged(int timeoutSeconds, double minScore) throws Exception {
        log.debug(String.format(
                "Checking if element state has been changed in %s seconds (Min score: %.4f)...",
                timeoutSeconds, minScore));
        return checkState((x) -> x < minScore, timeoutSeconds);
    }

    public boolean isNotChanged(int timeoutSeconds, double minScore) throws Exception {
        log.debug(String.format(
                "Checking if element state has NOT been changed in %s seconds (Min score: %.4f)...",
                timeoutSeconds, minScore));
        return checkState((x) -> x >= minScore, timeoutSeconds);
    }
}
