package com.wearezeta.auto.ios.pages.details_overlay.common;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseDetailsOverlay;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class DeviceDetailsPage extends BaseDetailsOverlay {

    private final static By classVerifySwitcher = By.className("XCUIElementTypeSwitch");

    private static final By xpathBackButton =
            By.xpath("//XCUIElementTypeButton[@name='SHOW MY DEVICE FINGERPRINT']" +
                    "/preceding-sibling::XCUIElementTypeButton[1]");

    private static final By xpathKeyFingerprintValue =
            By.xpath("//XCUIElementTypeStaticText[@name='Key Fingerprint']" +
                    "/following-sibling::XCUIElementTypeStaticText[1]");

    private static final By nameRemoveDevice = MobileBy.AccessibilityId("Remove Device");

    public DeviceDetailsPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "verify":
                return classVerifySwitcher;
            case "back":
                return xpathBackButton;
            case "remove device":
                return nameRemoveDevice;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    @Override
    protected By getLeftActionButtonLocator() {
        throw new IllegalStateException("Left action button is not available for devices overlay");
    }

    @Override
    protected By getRightActionButtonLocator() {
        throw new IllegalStateException("Right action button is not available for devices overlay");
    }

    public String getFingerprintValue() throws Exception {
        return getElement(xpathKeyFingerprintValue).getAttribute("value");
    }

    public boolean verifyFingerPrintNotEmpty() throws Exception {
        return !getFingerprintValue().isEmpty();
    }
}
