package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.Random;
import java.util.concurrent.Future;

public class VoiceFiltersOverlay extends IOSPage {
    private static final By nameRecordButton = MobileBy.AccessibilityId("record");
    private static final By nameStopRecordButton = MobileBy.AccessibilityId("stopRecording");
    private static final By nameConfirmRecordButton = MobileBy.AccessibilityId("confirmRecording");
    private static final By nameRedoRecordButton = MobileBy.AccessibilityId("redoRecording");
    private static final By nameCancelRecordButton = MobileBy.AccessibilityId("cancelRecording");

    private static final By nameEffectNoneButton = MobileBy.AccessibilityId("None");
    private static final By nameEffectHeliumButton = MobileBy.AccessibilityId("Helium");
    private static final By nameEffectJellyfishButton = MobileBy.AccessibilityId("Jellyfish");
    private static final By nameEffectHareButton = MobileBy.AccessibilityId("Hare");
    private static final By nameEffectCathedralButton = MobileBy.AccessibilityId("Cathedral");
    private static final By nameEffectAlienButton = MobileBy.AccessibilityId("Alien");
    private static final By nameEffectVocoderMedButton = MobileBy.AccessibilityId("VocoderMed");
    private static final By nameEffectUpsideDownButton = MobileBy.AccessibilityId("UpsideDown");

    public VoiceFiltersOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "start recording":
                return nameRecordButton;
            case "stop recording":
                return nameStopRecordButton;
            case "confirm":
                return nameConfirmRecordButton;
            case "redo":
                return nameRedoRecordButton;
            case "cancel":
                return nameCancelRecordButton;
            // effects
            case "no effect":
                return nameEffectNoneButton;
            case "helium effect":
                return nameEffectHeliumButton;
            case "jellyfish effect":
                return nameEffectJellyfishButton;
            case "hare effect":
                return nameEffectHareButton;
            case "cathedral effect":
                return nameEffectCathedralButton;
            case "alien effect":
                return nameEffectAlienButton;
            case "vocoder mix effect":
                return nameEffectVocoderMedButton;
            case "upside down effect":
                return nameEffectUpsideDownButton;
            default:
                throw new IllegalArgumentException(
                        String.format("The '%s' button is unknown to Voice Filters overlay", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        if (locator.equals(nameConfirmRecordButton)) {
            this.clickElementWithRetryIfStillDisplayed(locator, 3);
        } else {
            getElement(locator).click();
        }
    }

    public boolean isButtonVisible(String name) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getButtonLocatorByName(name));
    }

    public boolean isButtonInvisible(String name) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), getButtonLocatorByName(name));
    }

    private static final Random rand = new Random();

    public void tapRandomEffectButtons(int count) throws Exception {
        final By[] availableButtons = new By[]{
                nameEffectHeliumButton, nameEffectJellyfishButton, nameEffectHareButton, nameEffectCathedralButton,
                nameEffectAlienButton, nameEffectVocoderMedButton, nameEffectUpsideDownButton
        };
        for (int i = 0; i < count; i++) {
            getElement(availableButtons[rand.nextInt(availableButtons.length)]).click();
        }
    }
}