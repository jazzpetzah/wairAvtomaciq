package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class CallOngoingVideoPage extends CallingOverlayPage {
    private static final By idVideoSelfPreview = By.id("ll__self_view_layout");

    private static final By idVideoSelfPreviewOff =
            By.xpath("//*[@id='tv__self_preview_place_holder' and @value='VIDEO OFF']");

    private static final By xpathOngoingCallContainer = By.xpath("//*[@id='video_calling_view']");

    public CallOngoingVideoPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    private static final int ELEMENT_VISIBILITY_TIMEOUT_SECONDS = 1;

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
        try {
            return super.getSpecialButtonScreenshot();
        } catch (IllegalStateException e) {
            tapOngoingVideo();
            return super.getSpecialButtonScreenshot();
        }
    }

    @Override
    public BufferedImage getMuteButtonScreenshot() throws Exception {
        try {
            return super.getMuteButtonScreenshot();
        } catch (IllegalStateException e) {
            tapOngoingVideo();
            return super.getMuteButtonScreenshot();
        }
    }


    @Override
    protected void tapSpecialAction() throws Exception {
        try {
            super.tapSpecialAction();
        } catch (IllegalStateException e) {
            tapOngoingVideo();
            super.tapSpecialAction();
        }
    }

    @Override
    public void hangup() throws Exception {
        try {
            super.hangup();
        } catch (IllegalStateException e) {
            tapOngoingVideo();
            super.hangup();
        }
    }

    @Override
    public void toggleMute() throws Exception {
        try {
            super.toggleMute();
        } catch (IllegalStateException e) {
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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idVideoSelfPreview);
    }

    public boolean isVideoSelfPreviewInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idVideoSelfPreviewOff);
    }
}
