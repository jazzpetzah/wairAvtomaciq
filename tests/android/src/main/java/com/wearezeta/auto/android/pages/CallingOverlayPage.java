package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class CallingOverlayPage extends AndroidPage {
    protected static final By idMute = By.id("ccbv__calling_controls__mute");
    protected static final By idHangup = By.id("ccbv__calling_controls__hangup");
    //Could be VideoOnOff or SpeakOnOff
    protected static final By idRight = By.id("ccbv__calling_controls__right_button");

    private FunctionalInterfaces.StateGetter specialButtonStateFunction = ()  -> this.getElementScreenshot(getElement(idRight)).orElseThrow(
                    () -> new IllegalStateException("Cannot get a screenshot of special button state")
            );
    private FunctionalInterfaces.StateGetter muteButtonStateFunction = ()  -> this.getElementScreenshot(getElement(idMute)).orElseThrow(
                    () -> new IllegalStateException("Cannot get a screenshot of mute button state")
            );
    

    public FunctionalInterfaces.StateGetter getSpecialButtonStateFunction() {
        return specialButtonStateFunction;
    }

    public FunctionalInterfaces.StateGetter getMuteButtonStateFunction() {
        return muteButtonStateFunction;
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

    private boolean specialActionIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idRight);
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
