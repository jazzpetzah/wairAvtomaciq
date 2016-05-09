package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CallOngoingVideoPage extends CallingOverlayPage {
    private static final By idVideoSelfPreview = By.id("spv__self_preview");

    private static final By xpathOngoingCallContainer =
            By.xpath("//*[@id='fl__calling_controls__self_video_preview']");

    public CallOngoingVideoPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    private static final int ELEMENT_VISIBILITY_TIMEOUT_SECONDS = 3;

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOngoingCallContainer,
                VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOngoingCallContainer);
    }

    private void tapOngoingVideo() throws Exception {
        getElement(xpathOngoingCallContainer).click();
        // Wait for elements to stay visible
        Thread.sleep(300);
    }

    @Override
    public BufferedImage getSpecialButtonScreenshot() throws Exception {
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idRight, ELEMENT_VISIBILITY_TIMEOUT_SECONDS)) {
            tapOngoingVideo();
        }
        return super.getSpecialButtonScreenshot();
    }

    @Override
    public BufferedImage getMuteButtonScreenshot() throws Exception {
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMute, ELEMENT_VISIBILITY_TIMEOUT_SECONDS)) {
            tapOngoingVideo();
        }
        return super.getMuteButtonScreenshot();
    }


    @Override
    protected void tapSpecialAction() throws Exception {
        final Optional<WebElement> specialActionBtn = getElementIfDisplayed(idRight,
                ELEMENT_VISIBILITY_TIMEOUT_SECONDS);
        if (specialActionBtn.isPresent()) {
            specialActionBtn.get().click();
        } else {
            tapOngoingVideo();
            super.tapSpecialAction();
        }
    }

    @Override
    public void hangup() throws Exception {
        final Optional<WebElement> hangUpBtn = getElementIfDisplayed(idHangup, ELEMENT_VISIBILITY_TIMEOUT_SECONDS);
        if (hangUpBtn.isPresent()) {
            hangUpBtn.get().click();
        } else {
            tapOngoingVideo();
            super.hangup();
        }
    }

    @Override
    public void toggleMute() throws Exception {
        final Optional<WebElement> muteBtn = getElementIfDisplayed(idMute, ELEMENT_VISIBILITY_TIMEOUT_SECONDS);
        if (muteBtn.isPresent()) {
            muteBtn.get().click();
        } else {
            tapOngoingVideo();
            super.toggleMute();
        }
    }

    @Override
    public boolean hangupIsVisible() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idHangup, ELEMENT_VISIBILITY_TIMEOUT_SECONDS)) {
            return true;
        } else {
            tapOngoingVideo();
            return super.hangupIsVisible();
        }
    }

    @Override
    public boolean toggleMuteIsVisible() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMute, ELEMENT_VISIBILITY_TIMEOUT_SECONDS)) {
            return true;
        } else {
            tapOngoingVideo();
            return super.toggleMuteIsVisible();
        }
    }

    @Override
    protected boolean specialActionIsVisible() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idRight, ELEMENT_VISIBILITY_TIMEOUT_SECONDS)) {
            return true;
        } else {
            tapOngoingVideo();
            return super.specialActionIsVisible();
        }
    }

    public boolean isVideoSelfPreviewVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(),idVideoSelfPreview);
    }

    public boolean isVideoSelfPreviewInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),idVideoSelfPreview);
    }
}
