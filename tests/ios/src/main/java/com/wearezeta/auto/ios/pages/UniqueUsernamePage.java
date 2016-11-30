package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;


public class UniqueUsernamePage extends IOSPage {

    private static final By namePageHeader = MobileBy.AccessibilityId("@Name");
    private static final By nameSaveButton = MobileBy.AccessibilityId("Save");

    public UniqueUsernamePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private By getLocatorByName(String locatorName) {
        switch (locatorName.toLowerCase()) {
            case "save" :
                return nameSaveButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown locator name %s", locatorName));
        }
    }

    public void tapButtonByName(String buttonName) throws Exception {
        getElement(getLocatorByName(buttonName)).click();
    }

    public boolean isUsernamePageVisible() throws Exception {
        return isLocatorDisplayed(nameSaveButton);
    }

}
