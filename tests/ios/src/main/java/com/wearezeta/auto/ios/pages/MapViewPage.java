package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class MapViewPage extends  IOSPage{

    private static final By nameSendLocationButton = MobileBy.AccessibilityId("sendLocation");

    public MapViewPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickSendLocationButton() throws Exception {
        getElement(nameSendLocationButton).click();
    }
}
