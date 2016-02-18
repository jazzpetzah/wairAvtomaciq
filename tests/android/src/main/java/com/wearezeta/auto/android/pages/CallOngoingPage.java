package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.android.common.Memory;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import java.util.function.Function;
import org.openqa.selenium.By;

public class CallOngoingPage extends AndroidPage {

    private static final By xpathOngoingCallContainer = By.xpath("//*[@id='ttv__calling__header__duration' and contains(@value, ':') and //*[@id='ccbv__calling_controls__hangup']]");

    private static final String idStrMute = "ccbv__calling_controls__mute";
    private static final By idMute = By.id(idStrMute);
    private static final String idStrHangup = "ccbv__calling_controls__hangup";
    private static final By idHangup = By.id(idStrHangup);
    //Could be VideoOnOff or SpeakOnOff
    private static final String idStrRight = "ccbv__calling_controls__right_button";
    private static final By idRight = By.id(idStrRight);

    private static final By idParticipants = By.id("chv__calling__participants_grid__chathead");

    private static final Function<String, String> xpathCallingHeaderByName = name -> String
            .format("//*[@id='ttv__calling__header__name' and contains(@value, '%s')]", name);

    private Memory spacialButtonState;
    private Memory muteButtonState;

    public CallOngoingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOngoingCallContainer, VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOngoingCallContainer);
    }

    public boolean waitUntilNameAppearsOnCallingBarCaption(String name) throws Exception {
        final By locator = By.xpath(xpathCallingHeaderByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void rememberSpecialActionButtonState() throws Exception {
        spacialButtonState = new Memory(getDriver(), idRight, 15000, 0.4d);
    }
    
    public void rememberMuteButtonState() throws Exception {
        muteButtonState = new Memory(getDriver(), idMute, 15000, 0.4d);
    }

    public boolean specialActionButtonStateHasChanged() throws Exception {
        if (spacialButtonState == null) {
            throw new IllegalStateException("Please call the corresponding step to take the screenshot of special action button state first");
        }
        return spacialButtonState.hasChanged();
    }
    
    public boolean muteButtonStateHasChanged() throws Exception {
        if (muteButtonState == null) {
            throw new IllegalStateException("Please call the corresponding step to take the screenshot of mute button state first");
        }
        return muteButtonState.hasChanged();
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

    public boolean toggleSpeakerIsVisible() throws Exception {
        return specialActionIsVisible();
    }

    public boolean toggleVideoIsVisible() throws Exception {
        return specialActionIsVisible();
    }

    public void toggleMute() throws Exception {
        getElement(idMute).click();
    }

    public void hangup() throws Exception {
        getElement(idHangup).click();
    }

    private void specialAction() throws Exception {
        getElement(idRight).click();
    }

    public void toggleSpeaker() throws Exception {
        specialAction();
    }

    public void toggleVideo() throws Exception {
        specialAction();
    }

    public int getNumberOfParticipants() throws Exception {
        return getElements(idParticipants).size();
    }

}
