package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class DeviceDetailsPage extends IOSPage {
    private final static By xpathVerifySwitcher = By.xpath("//XCUIElementTypeSwitch");

    private static final By xpathBackButton =
            By.xpath("//XCUIElementTypeButton[@name='SHOW MY DEVICE FINGERPRINT']" +
                    "/preceding-sibling::XCUIElementTypeButton[1]");

    private static final By xpathKeyFingerprintValue =
            By.xpath("//XCUIElementTypeStaticText[@name='Key Fingerprint']" +
                    "/following-sibling::XCUIElementTypeStaticText[1]");

    private static final By nameRemoveDevice = MobileBy.AccessibilityId("Remove Device");

    public DeviceDetailsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapVerifySwitcher() throws Exception {
        getElement(xpathVerifySwitcher).click();
    }

    public void tapBackButton() throws Exception {
        getElement(xpathBackButton).click();
    }

    public String getFingerprintValue() throws Exception {
        return getElement(xpathKeyFingerprintValue).getAttribute("value");
    }

    public boolean verifyFingerPrintNotEmpty() throws Exception {
        return !getFingerprintValue().isEmpty();
    }

    public void tapRemoveDevice() throws Exception {
        getElement(nameRemoveDevice).click();
    }
}
