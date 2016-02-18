package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.ImageUtil;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import java.awt.image.BufferedImage;
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

    private BufferedImage spacialButtonState;
    private BufferedImage muteButtonState;

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

    private static final long BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS = 15000;
    private static final double BUTTON_STATE_OVERLAP_MAX_SCORE = 0.4d;

    public void rememberSpecialActionButtonState() throws Exception {
        spacialButtonState = getElementScreenshot(getElement(idRight)).orElseThrow(IllegalStateException::new);
    }
    
    public void rememberMuteButtonState() throws Exception {
        muteButtonState = getElementScreenshot(getElement(idMute)).orElseThrow(IllegalStateException::new);
    }

    public boolean specialActionButtonStateHasChanged() throws Exception {
        if (spacialButtonState == null) {
            throw new IllegalStateException("Please call the corresponding step to take the screenshot of special action button state first");
        }
        final long millisecondsStarted = System.currentTimeMillis();
        double overlapScore;
        do {
            final BufferedImage currentStateScreenshot = getElementScreenshot(getElement(idRight)).orElseThrow(IllegalStateException::new);
            overlapScore = ImageUtil.getOverlapScore(currentStateScreenshot, spacialButtonState,
                    ImageUtil.RESIZE_TO_MAX_SCORE);
            if (overlapScore <= BUTTON_STATE_OVERLAP_MAX_SCORE) {
                return true;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - millisecondsStarted <= BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS);
        log.warn(String.format(
                "Button state has not been changed within %s seconds timeout. Current overlap score: %.2f, expected overlap score: <= %.2f",
                BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS / 1000, overlapScore, BUTTON_STATE_OVERLAP_MAX_SCORE));
        return false;
    }
    
    public boolean muteButtonStateHasChanged() throws Exception {
        if (muteButtonState == null) {
            throw new IllegalStateException("Please call the corresponding step to take the screenshot of mute button state first");
        }
        final long millisecondsStarted = System.currentTimeMillis();
        double overlapScore;
        do {
            final BufferedImage currentStateScreenshot = getElementScreenshot(getElement(idMute)).orElseThrow(IllegalStateException::new);
            overlapScore = ImageUtil.getOverlapScore(currentStateScreenshot, muteButtonState,
                    ImageUtil.RESIZE_TO_MAX_SCORE);
            if (overlapScore <= BUTTON_STATE_OVERLAP_MAX_SCORE) {
                return true;
            }
            Thread.sleep(500);
        } while (System.currentTimeMillis() - millisecondsStarted <= BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS);
        log.warn(String.format(
                "Button state has not been changed within %s seconds timeout. Current overlap score: %.2f, expected overlap score: <= %.2f",
                BUTTON_STATE_CHANGE_TIMEOUT_MILLISECONDS / 1000, overlapScore, BUTTON_STATE_OVERLAP_MAX_SCORE));
        return false;
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
