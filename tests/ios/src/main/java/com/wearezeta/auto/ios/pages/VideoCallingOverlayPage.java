package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
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

    private WebElement makeOverlayButtonVisible(String name) throws Exception {
        final By locator = MobileBy.AccessibilityId(getButtonAccessibilityIdByName(name));
        final Optional<WebElement> dstBtn = getElementIfDisplayed(locator, 1);
        if (!dstBtn.isPresent()) {
            final Dimension screenSize = getDriver().manage().window().getSize();
            this.tapScreenAt(screenSize.getWidth() / 2, screenSize.getHeight() / 2);
            Thread.sleep(1000);
            return getElementIfDisplayed(locator, 1).orElseThrow(
                    () -> new IllegalStateException(
                            String.format("The button identified by '%s' is expected to be present", name))
            );
        }
        return dstBtn.get();
    }

    private void tapOverlayButton(String name) throws Exception {
        makeOverlayButtonVisible(name).click();
    }

    @Override
    public void tapButtonByName(String buttonName) throws Exception {
        tapOverlayButton(buttonName);
    }

    @Override
    public boolean isButtonVisible(String buttonName) throws Exception {
        return makeOverlayButtonVisible(buttonName).isDisplayed();
    }

    @Override
    public BufferedImage getMuteButtonScreenshot() throws Exception {
        final WebElement btn = makeOverlayButtonVisible("Mute");
        return getElementScreenshot(btn).orElseThrow(
                () -> new IllegalStateException("Cannot make a screenshot")
        );
    }

    public BufferedImage getVideoButtonScreenshot() throws Exception {
        final WebElement btn = makeOverlayButtonVisible("Call Video");
        return getElementScreenshot(btn).orElseThrow(
                () -> new IllegalStateException("Cannot make a screenshot")
        );
    }
}
