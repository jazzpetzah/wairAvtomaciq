package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class DeviceDetailsPage extends IOSPage {
    private final static By nameVerifySwitcher = By.xpath("//UIASwitch");

    private static final By nameBackButton = By.name("Back");

    public DeviceDetailsPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapVerifySwitcher() throws Exception {
        getElement(nameVerifySwitcher).click();
    }

    public void tapBackButton() throws Exception {
        getElement(nameBackButton).click();
    }
}
