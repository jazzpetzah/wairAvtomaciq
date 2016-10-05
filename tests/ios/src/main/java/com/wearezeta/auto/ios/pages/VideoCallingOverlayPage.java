package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

public class VideoCallingOverlayPage extends CallingOverlayPage {

    public VideoCallingOverlayPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private FBElement makeOverlayButtonVisible(String name) throws Exception {
        final Optional<WebElement> dstBtn = getElementIfExists(
                FBBy.AccessibilityId(getButtonAccessibilityIdByName(name)));
        if (dstBtn.isPresent()) {
            if (!dstBtn.get().isDisplayed()) {
                this.tapAtTheCenterOfElement((FBElement) dstBtn.get());
                Thread.sleep(300);
            }
        } else {
            throw new IllegalStateException(String.format("the button identified by '%s' is expected to be present",
                    name));
        }
        return (FBElement) dstBtn.get();
    }

    private void tapOverlayButton(String name) throws Exception {
        final FBElement btn = makeOverlayButtonVisible(name);
        this.tapAtTheCenterOfElement(btn);
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
