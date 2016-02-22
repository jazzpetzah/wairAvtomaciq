package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.misc.ElementState;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class CallingOverlayPage extends AndroidPage {
    private static final By idMute = By.id("ccbv__calling_controls__mute");
    private static final By idHangup = By.id("ccbv__calling_controls__hangup");
    //Could be VideoOnOff or SpeakOnOff
    private static final By idRight = By.id("ccbv__calling_controls__right_button");

    private ElementState specialButtonState = new ElementState(
            () -> this.getElementScreenshot(getElement(idRight)).orElseThrow(
                    () -> new IllegalStateException("Cannot get a screenshot of special button state")
            )
    );
    private ElementState muteButtonState = new ElementState(
            () -> this.getElementScreenshot(getElement(idMute)).orElseThrow(
                    () -> new IllegalStateException("Cannot get a screenshot of Mute button state")
            )
    );

    public CallingOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean hangupIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idHangup);
    }

    public boolean toggleMuteIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idMute);
    }

    private boolean specialActionIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idRight);
    }

    public void rememberSpecialActionButtonState() throws Exception {
        specialButtonState.remember();
    }

    public void rememberMuteButtonState() throws Exception {
        muteButtonState.remember();
    }

    private static final int STATE_CHANGE_TIMEOUT = 15;
    private static final double MIN_BUTTON_SIMILARITY_SCORE = 0.4;

    public boolean specialActionButtonStateHasChanged() throws Exception {
        return specialButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE);
    }

    public boolean muteButtonStateHasChanged() throws Exception {
        return muteButtonState.isChanged(STATE_CHANGE_TIMEOUT, MIN_BUTTON_SIMILARITY_SCORE);
    }

    public void toggleMute() throws Exception {
        getElement(idMute).click();
    }

    public void hangup() throws Exception {
        getElement(idHangup).click();
    }

    private void specialAction() throws Exception{
        getElement(idRight).click();
    }

    public boolean toggleSpeakerIsVisible() throws Exception {
        return specialActionIsVisible();
    }

    public boolean toggleVideoIsVisible() throws Exception {
        return specialActionIsVisible();
    }

    public void toggleSpeaker() throws Exception {
        specialAction();
    }

    public void toggleVideo() throws Exception {
        specialAction();
    }
}
