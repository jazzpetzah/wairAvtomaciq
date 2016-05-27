package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

public class VideoCallingOverlayPage extends CallingOverlayPage {

    private static final int ELEMENT_VISIBILITY_TIMEOUT = 5;

    public VideoCallingOverlayPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private void tapButtonIgnoringVisibility(String name, int times) throws Exception {
        final String xpath = String.format("//*[@name='%s']", getButtonAccessibilityIdByName(name));
        final Rectangle buttonBounds = getElementBounds(xpath);
        for (int i = 0; i < times; i++) {
            getDriver().tap(1,
                    (int) buttonBounds.getX() + (int) buttonBounds.getWidth() / 2,
                    (int) buttonBounds.getY() + (int) buttonBounds.getHeight() / 2,
                    DriverUtils.SINGLE_TAP_DURATION);
            if (i < times) {
                Thread.sleep(300);
            }
        }
    }

    @Override
    public void tapButtonByName(String buttonName) throws Exception {
        final Optional<WebElement> button = getElementIfDisplayed(getButtonLocatorByName(buttonName),
                ELEMENT_VISIBILITY_TIMEOUT);
        if (button.isPresent()) {
            button.get().click();
        } else {
            tapButtonIgnoringVisibility(buttonName, 2);
        }
    }

    @Override
    public boolean isButtonVisible(String buttonName) throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getButtonLocatorByName(buttonName),
                ELEMENT_VISIBILITY_TIMEOUT)) {
            return true;
        } else {
            tapButtonIgnoringVisibility(buttonName, 1);
            return super.isButtonVisible(buttonName);
        }
    }

    @Override
    public BufferedImage getMuteButtonScreenshot() throws Exception {
        final Optional<WebElement> muteButton = getElementIfDisplayed(nameMuteCallButton,
                ELEMENT_VISIBILITY_TIMEOUT);
        if (!muteButton.isPresent()) {
            tapButtonIgnoringVisibility(nameStrMuteCallButton, 1);
        }
        return super.getMuteButtonScreenshot();
    }

    public BufferedImage getVideoButtonScreenshot() throws Exception {
        final Optional<WebElement> videoButton = getElementIfDisplayed(nameCallVideoButton,
                ELEMENT_VISIBILITY_TIMEOUT);
        if (!videoButton.isPresent()) {
            tapButtonIgnoringVisibility(nameStrCallVideoButton, 1);
        }
        return this.getElementScreenshot(getElement(nameCallVideoButton)).orElseThrow(
                () -> new IllegalStateException("Cannot take a screenshot of Video button"));
    }
}
