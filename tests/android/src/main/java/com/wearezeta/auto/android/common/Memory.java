package com.wearezeta.auto.android.common;

import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Memory {

    protected static final Logger log = ZetaLogger.getLog(Memory.class.getSimpleName());
    private static final long DEFAULT_STATE_CHANGE_TIMEOUT_MILLISECONDS = 15000;
    private static final double DEFAULT_STATE_OVERLAP_MAX_SCORE = 0.4d;
    private static final boolean DEBUG = true;

    private final long STATE_CHANGE_TIMEOUT_MILLISECONDS;
    private final double STATE_OVERLAP_MAX_SCORE;

    private BufferedImage rembered;
    private By by;
    private RemoteWebDriver driver;

    public Memory(RemoteWebDriver driver, By by) throws Exception {
        this(driver, by, DEFAULT_STATE_CHANGE_TIMEOUT_MILLISECONDS, DEFAULT_STATE_OVERLAP_MAX_SCORE);
    }

    public Memory(RemoteWebDriver driver, By by, long timeout, double overlapScore) throws Exception {
        this.STATE_CHANGE_TIMEOUT_MILLISECONDS = timeout;
        this.STATE_OVERLAP_MAX_SCORE = overlapScore;
        this.by = by;
        this.driver = driver;
        this.rembered = getElementScreenshot().orElse(null);
    }

    public boolean hasChanged() throws Exception {
        final long millisecondsStarted = System.currentTimeMillis();
        double overlapScore;
        if (DEBUG && rembered != null) {
            ImageIO.write(rembered, "png", new File("/tmp/remembered.png"));
        }
        int counter = 0;
        do {
            counter++;
            final BufferedImage currentStateScreenshot = getElementScreenshot().orElse(null);
            if ((rembered == null && currentStateScreenshot != null) || (rembered != null && currentStateScreenshot == null)) {
                return true;
            }
            if (rembered == null && currentStateScreenshot == null) {
                return false;
            }
            if (DEBUG) {
                ImageIO.write(currentStateScreenshot, "png", new File("/tmp/actual_" + counter + ".png"));
            }
            overlapScore = ImageUtil.getOverlapScore(rembered, currentStateScreenshot,
                    ImageUtil.RESIZE_TEMPLATE_TO_REFERENCE_RESOLUTION);
            if (DEBUG) {
                log.debug("overlapscore: " + overlapScore);
            }
            if (overlapScore <= STATE_OVERLAP_MAX_SCORE) {
                return true;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - millisecondsStarted <= STATE_CHANGE_TIMEOUT_MILLISECONDS);
        log.warn(String.format(
                "Element state has not been changed within %s seconds timeout. Current overlap score: %.2f, expected overlap score: <= %.2f",
                STATE_CHANGE_TIMEOUT_MILLISECONDS / 1000, overlapScore, STATE_OVERLAP_MAX_SCORE));
        return false;
    }

    public boolean hasNotChanged() throws Exception {
        return !hasChanged();
    }

    private Optional<BufferedImage> getElementScreenshot() throws Exception {
        WebElement wel = DriverUtils.getElementIfPresentInDOM(this.driver, this.by).orElse(null);
        if (wel != null) {
            final Optional<BufferedImage> screenshot = DriverUtils.takeFullScreenShot((ZetaDriver) this.driver);
            if (screenshot.isPresent()) {
                final Point elementLocation = wel.getLocation();
                final Dimension elementSize = wel.getSize();
                return Optional.of(screenshot.get().getSubimage(
                        elementLocation.x, elementLocation.y, elementSize.width, elementSize.height));
            } else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
