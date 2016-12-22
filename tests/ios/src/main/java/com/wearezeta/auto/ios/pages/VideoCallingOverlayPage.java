package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.misc.Timedelta;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

public class VideoCallingOverlayPage extends CallingOverlayPage {
    public VideoCallingOverlayPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private Optional<WebElement> makeOverlayButtonVisible(String name) throws Exception {
        final By locator = MobileBy.AccessibilityId(getButtonAccessibilityIdByName(name));
        final Optional<WebElement> dstBtn = getElementIfDisplayed(locator, Timedelta.fromSeconds(1));
        if (!dstBtn.isPresent()) {
            final Dimension screenSize = getDriver().manage().window().getSize();
            this.tapScreenAt(screenSize.getWidth() / 2, screenSize.getHeight() / 2);
            return getElementIfExists(locator, Timedelta.fromSeconds(5));
        }
        return dstBtn;
    }

    @Override
    public void tapButtonByName(String buttonName) throws Exception {
        final By locator = MobileBy.AccessibilityId(getButtonAccessibilityIdByName(buttonName));
        final Optional<WebElement> dstBtn = getElementIfDisplayed(locator, Timedelta.fromSeconds(1));
        if (dstBtn.isPresent()) {
            dstBtn.get().click();
            return;
        }
        // FIXME: These buttons disappear so fast (or the framework is so slow), that we need to tap by coordinates
        final Dimension screenSize = getDriver().manage().window().getSize();
        final int y = screenSize.getHeight() / 6 * 5;
        final int screenWidth = screenSize.getWidth();
        switch (buttonName) {
            case "Leave":
                this.tapScreenAt(screenWidth / 2, y);
                Thread.sleep(1000);
                this.tapScreenAt(screenWidth / 2, y);
                break;
            case "Mute":
                this.tapScreenAt(screenWidth / 3, y);
                Thread.sleep(1000);
                this.tapScreenAt(screenWidth / 3, y);
                break;
            case "Video":
                this.tapScreenAt(screenWidth / 3 * 2, y);
                Thread.sleep(1000);
                this.tapScreenAt(screenWidth / 3 * 2, y);
                break;
            default:
                throw new IllegalArgumentException(String.format("Button '%s' is not supported ", buttonName));
        }
    }

    @Override
    public boolean isButtonVisible(String buttonName) throws Exception {
        return makeOverlayButtonVisible(buttonName).isPresent();
    }

    @Override
    public BufferedImage getMuteButtonScreenshot() throws Exception {
        final WebElement btn = makeOverlayButtonVisible("Mute").orElseThrow(
                () -> new IllegalStateException("Mute button is not visible")
        );
        return getElementScreenshot(btn).orElseThrow(
                () -> new IllegalStateException("Cannot make a screenshot")
        );
    }

    public BufferedImage getVideoButtonScreenshot() throws Exception {
        final WebElement btn = makeOverlayButtonVisible("Call Video").orElseThrow(
                () -> new IllegalStateException("Video button is not visible")
        );
        return getElementScreenshot(btn).orElseThrow(
                () -> new IllegalStateException("Cannot make a screenshot")
        );
    }
}
