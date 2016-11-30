package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;


public class UsernamePage extends IOSPage {

    private static final By namePageHeader = MobileBy.AccessibilityId("@Name");
    private static final By nameSaveButton = MobileBy.AccessibilityId("Save");

    public UsernamePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapSaveButton() throws Exception {
        getElement(nameSaveButton).click();
    }

    public boolean isUsernamePageVisible() throws Exception {
        return isLocatorDisplayed(nameSaveButton);
    }

}
