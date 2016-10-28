package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class TabletDeviceDetailsPage extends DeviceDetailsPage {
    private static final By nameOtherUserProfilePageCloseButton =
            By.xpath("//XCUIElementTypeButton[@name='SHOW MY DEVICE FINGERPRINT']" +
                    "/preceding-sibling::XCUIElementTypeButton[1]");

    public TabletDeviceDetailsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    public void tapBackButton() throws Exception {
        getElement(nameOtherUserProfilePageCloseButton).click();
    }
}
