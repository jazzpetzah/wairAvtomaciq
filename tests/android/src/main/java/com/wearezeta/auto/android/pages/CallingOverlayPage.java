package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

public abstract class CallingOverlayPage extends AndroidPage {
    protected static final By idMute = By.id("ccbv__calling_controls__mute");
    protected static final By idHangup = By.id("ccbv__calling_controls__hangup");
    //Could be VideoOnOff or SpeakOnOff
    protected static final By idRight = By.id("ccbv__calling_controls__right_button");
    protected static final By idVideoSelfPreview = By.id("spv__self_preview");

    public BufferedImage getSpecialButtonScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idRight)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of special button state")
        );
    }

    public BufferedImage getMuteButtonScreenshot() throws Exception {
        return this.getElementScreenshot(getElement(idMute)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of mute button state")
        );
    }

    public CallingOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean hangupIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idHangup);
    }

    public boolean toggleMuteIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMute);
    }

    protected boolean specialActionIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idRight);
    }
    private boolean specialActionIsNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idRight);
    }

    public void toggleMute() throws Exception {
        getElement(idMute).click();
    }

    public void hangup() throws Exception {
        getElement(idHangup).click();
    }

    public void tapSpecialAction() throws Exception {
        getElement(idRight).click();
    }

    public boolean toggleSpeakerIsVisible() throws Exception {
        return specialActionIsVisible();
    }

    public boolean toggleVideoIsVisible() throws Exception {
        return specialActionIsVisible();
    }

    public void toggleSpeaker() throws Exception {
        tapSpecialAction();
    }

    public void toggleVideo() throws Exception {
        tapSpecialAction();
    }

    public boolean toggleMuteIsNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idMute);
    }

    public boolean toggleSpeakerIsNotVisible() throws Exception {
        return specialActionIsNotVisible();
    }

    public boolean isVideoSelfPreviewVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(),idVideoSelfPreview);
    }

    public boolean isVideoSelfPreviewInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),idVideoSelfPreview);
    }
}
