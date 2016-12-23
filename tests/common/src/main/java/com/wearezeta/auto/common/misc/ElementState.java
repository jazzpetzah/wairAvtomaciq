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

    private static final Timedelta INTERVAL = Timedelta.fromMilliSeconds(500);

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
                Thread.sleep(INTERVAL.asMilliSeconds());
            }
        } while (nTry < maxRetries);
        throw savedException;
    }

    public ElementState remember(BufferedImage customInitialState) throws Exception {
        this.previousScreenshot = Optional.of(customInitialState);
        return this;
    }

    private boolean checkState(Function<Double, Boolean> checkerFunc, Timedelta timeout) throws Exception {
        return checkState(checkerFunc, timeout, ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
    }

    private boolean checkState(Function<Double, Boolean> checkerFunc, Timedelta timeout, int resizeMode)
            throws Exception {
        final Timedelta started = Timedelta.now();
        do {
            try {
                final BufferedImage currentState = stateGetter.getState();
                final double score = ImageUtil.getOverlapScore(
                        this.previousScreenshot.orElseThrow(
                                () -> new IllegalStateException("Please remember the previous element state first")),
                        currentState, resizeMode);
                log.debug(String.format("Actual score: %.4f; Time left: %s", score,
                        timeout.sum(started).diff(Timedelta.now()).toString()));
                if (checkerFunc.apply(score)) {
                    return true;
                }
            } catch (StaleElementReferenceException e) {
                log.debug(String.format("Actual score: <calculation error>; Time left: %s",
                        timeout.sum(started).diff(Timedelta.now()).toString()));
            }
            Thread.sleep(INTERVAL.asMilliSeconds());
        } while (Timedelta.now().isDiffLessOrEqual(started, timeout));
        return false;
    }

    public boolean isChanged(Timedelta timeout, double minScore) throws Exception {
        log.debug(String.format(
                "Checking if element state has been changed in %s (Min score: %.4f)...",
                timeout.toString(), minScore));
        return checkState((x) -> x < minScore, timeout);
    }

    public boolean isChanged(Timedelta timeout, double minScore, int resizeMode) throws Exception {
        log.debug(String.format(
                "Checking if element state has been changed in %s (Min score: %.4f)...",
                timeout, minScore));
        return checkState((x) -> x < minScore, timeout, resizeMode);
    }

    public boolean isNotChanged(Timedelta timeout, double minScore) throws Exception {
        log.debug(String.format(
                "Checking if element state has NOT been changed in %s (Min score: %.4f)...",
                timeout, minScore));
        return checkState((x) -> x >= minScore, timeout);
    }

    public boolean isNotChanged(Timedelta timeout, double minScore, int resizeMode) throws Exception {
        log.debug(String.format(
                "Checking if element state has NOT been changed in %s (Min score: %.4f)...",
                timeout, minScore));
        return checkState((x) -> x >= minScore, timeout, resizeMode);
    }
}
