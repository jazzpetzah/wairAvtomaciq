package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

public class VideoCallingOverlayPage extends CallingOverlayPage {

    private static final int ELEMENT_VISIBILITY_TIMEOUT = 5;

    public VideoCallingOverlayPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private void tapOnScreenToRevealButton() throws Exception {
        this.getDriver().tap(1, 10, 10, 1000);
    }

    @Override
    public void tapButtonByName(String buttonName) throws Exception {
        final Optional<WebElement> button = getElementIfDisplayed(getButtonLocatorByName(buttonName),
                ELEMENT_VISIBILITY_TIMEOUT);
        if (button.isPresent()) {
            button.get().click();
        } else {
            tapOnScreenToRevealButton();
            super.tapButtonByName(buttonName);
        }
    }

    @Override
    public boolean isButtonVisible(String buttonName) throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getButtonLocatorByName(buttonName),
                ELEMENT_VISIBILITY_TIMEOUT)) {
            return true;
        } else {
            tapOnScreenToRevealButton();
            return super.isButtonVisible(buttonName);
        }
    }

    @Override
    public BufferedImage getMuteButtonScreenshot() throws Exception {
        final Optional<WebElement> muteButton = getElementIfDisplayed(nameMuteCallButton,
                ELEMENT_VISIBILITY_TIMEOUT);
        if (muteButton.isPresent()) {
            return this.getElementScreenshot(muteButton.get()).orElseThrow(
                    () -> new IllegalStateException("Cannot take a screenshot of Mute button"));
        } else {
            tapOnScreenToRevealButton();
            return super.getMuteButtonScreenshot();
        }
    }

    public BufferedImage getVideoButtonScreenshot() throws Exception {
        final Optional<WebElement> videoButton = getElementIfDisplayed(nameCallVideoButton,
                ELEMENT_VISIBILITY_TIMEOUT);
        if (videoButton.isPresent()) {
            return this.getElementScreenshot(videoButton.get()).orElseThrow(
                    () -> new IllegalStateException("Cannot take a screenshot of Video button"));
        } else {
            tapOnScreenToRevealButton();
            return this.getElementScreenshot(getElement(nameCallVideoButton)).orElseThrow(
                    () -> new IllegalStateException("Cannot take a screenshot of Video button"));
        }
    }
}
