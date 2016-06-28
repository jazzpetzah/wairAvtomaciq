package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

public class VideoCallingOverlayPage extends CallingOverlayPage {

    public VideoCallingOverlayPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private Optional<Rectangle> makeOverlayButtonVisible(String name) throws Exception {
        final String invisibleXpath = String.format("//*[@name='%s' and @visible='false']",
                getButtonAccessibilityIdByName(name));
        final Optional<Rectangle> bounds = getElementBounds(invisibleXpath);
        if (bounds.isPresent()) {
            getDriver().tap(1,
                    (int) bounds.get().getX() + (int) bounds.get().getWidth() / 2,
                    (int) bounds.get().getY() + (int) bounds.get().getHeight() / 2,
                    DriverUtils.SINGLE_TAP_DURATION);
            Thread.sleep(300);
        }
        final String xpath = String.format("//*[@name='%s' and @visible='true']",
                getButtonAccessibilityIdByName(name));
        return getElementBounds(xpath);
    }

    private void tapOverlayButton(String name) throws Exception {
        final Rectangle bounds = makeOverlayButtonVisible(name).orElseThrow(
                () -> new IllegalStateException(String.format("'%s' button is not visible", name))
        );
        getDriver().tap(1,
                (int) bounds.getX() + (int) bounds.getWidth() / 2,
                (int) bounds.getY() + (int) bounds.getHeight() / 2,
                DriverUtils.SINGLE_TAP_DURATION);
    }

    @Override
    public void tapButtonByName(String buttonName) throws Exception {
        tapOverlayButton(buttonName);
    }

    @Override
    public boolean isButtonVisible(String buttonName) throws Exception {
        return makeOverlayButtonVisible(buttonName).isPresent();
    }

    @Override
    public BufferedImage getMuteButtonScreenshot() throws Exception {
        final Rectangle bounds = makeOverlayButtonVisible("Mute").orElseThrow(
                () -> new IllegalStateException("Mute button is not visible")
        );
        final BufferedImage screenshot = takeScreenshot().orElseThrow(
                () -> new IllegalStateException("Cannot make a screenshot")
        );
        return screenshot.getSubimage(
                (int) bounds.getX(), (int) bounds.getY(),
                (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    public BufferedImage getVideoButtonScreenshot() throws Exception {
        final Rectangle bounds = makeOverlayButtonVisible("Call Video").orElseThrow(
                () -> new IllegalStateException("Video button is not visible")
        );
        final BufferedImage screenshot = takeScreenshot().orElseThrow(
                () -> new IllegalStateException("Cannot make a screenshot")
        );
        return screenshot.getSubimage(
                (int) bounds.getX(), (int) bounds.getY(),
                (int) bounds.getWidth(), (int) bounds.getHeight());
    }
}
