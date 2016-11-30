package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;


public class UniqueUsernamePage extends IOSPage {

    private static final By namePageHeader = MobileBy.AccessibilityId("@Name");
    private static final By nameSaveButton = MobileBy.AccessibilityId("Save");
    private static final By fbUniqueUsernameInput = FBBy.AccessibilityId("handleTextField");

    public UniqueUsernamePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static By getLocatorByName(String locatorName) {
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

    public void inputStringInNameInput(String name) throws Exception {
        final FBElement el = ((FBElement) getElement(fbUniqueUsernameInput));
        el.clear();
        el.sendKeys(name);
    }

    public void clearNameInput() throws Exception {
        inputStringInNameInput("");
    }

}
