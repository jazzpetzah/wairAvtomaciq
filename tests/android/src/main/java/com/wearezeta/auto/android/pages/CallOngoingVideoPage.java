package com.wearezeta.auto.android.pages;

import java.util.Optional;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CallOngoingVideoPage extends CallingOverlayPage {

    private static final By xpathOngoingCallContainer =
            By.xpath("//*[@id='tcfl__calling__container' and //*[@id='spv__self_preview']]");

    public CallOngoingVideoPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOngoingCallContainer,
                VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOngoingCallContainer);
    }

    private void tapOngoingVideo() throws Exception {
        getElement(xpathOngoingCallContainer).click();
    }

    @Override
    protected void tapSpecialAction() throws Exception {
        final Optional<WebElement> specialActionBtn = getElementIfDisplayed(idRight);
        if (specialActionBtn.isPresent()) {
            specialActionBtn.get().click();
        } else {
            tapOngoingVideo();
            super.tapSpecialAction();
        }
    }

    @Override
    public void hangup() throws Exception {
        final Optional<WebElement> hangUpBtn = getElementIfDisplayed(idHangup);
        if (hangUpBtn.isPresent()) {
            hangUpBtn.get().click();
        } else {
            tapOngoingVideo();
            super.hangup();
        }
    }

    @Override
    public void toggleMute() throws Exception {
        final Optional<WebElement> muteBtn = getElementIfDisplayed(idMute);
        if (muteBtn.isPresent()) {
            muteBtn.get().click();
        } else {
            tapOngoingVideo();
            super.toggleMute();
        }
    }

    @Override
    public boolean hangupIsVisible() throws Exception {
        final Optional<WebElement> hangUpBtn = getElementIfDisplayed(idHangup);
        if (hangUpBtn.isPresent()) {
            return true;
        } else {
            tapOngoingVideo();
            return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idHangup);
        }
    }

    @Override
    public boolean toggleMuteIsVisible() throws Exception {
        final Optional<WebElement> muteBtn = getElementIfDisplayed(idMute);
        if (muteBtn.isPresent()) {
            return true;
        } else {
            tapOngoingVideo();
            return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMute);
        }
    }

    @Override
    protected boolean specialActionIsVisible() throws Exception {
        final Optional<WebElement> toggleVideoBtn = getElementIfDisplayed(idRight);
        if (toggleVideoBtn.isPresent()) {
            return true;
        } else {
            tapOngoingVideo();
            return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idRight);
        }
    }
}
