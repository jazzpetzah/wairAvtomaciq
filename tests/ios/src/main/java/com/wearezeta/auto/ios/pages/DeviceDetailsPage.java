package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class DeviceDetailsPage extends IOSPage {
    private final static By nameVerifySwitcher = By.xpath("//UIASwitch");

    private static final By xpathBackButton = By.xpath(xpathStrMainWindow + "/UIAButton[4]");
    
    private static final By xpathKeyFingerprintValue = By.xpath("//UIATableCell[@name='Key Fingerprint']/UIAStaticText[2]");

    private static final By nameRemoveDevice = MobileBy.AccessibilityId("Remove Device");

    public DeviceDetailsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapVerifySwitcher() throws Exception {
        getElement(nameVerifySwitcher).click();
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
