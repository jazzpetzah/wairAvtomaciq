package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBDriverAPI;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;


public class UniqueUsernamePage extends IOSPage {

    private static final By namePageHeader = MobileBy.AccessibilityId("@Name");
    private static final By nameSaveButton = MobileBy.AccessibilityId("Save");
    private static final By fbNameUniqueUsernameInput = FBBy.AccessibilityId("handleTextField");

    private static final Function<String, String> xpathStrUniqueUsernameInSettings = name ->
            String.format("//XCUIElementTypeStaticText[@name='@%s']", name);

    public UniqueUsernamePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private String  newUniqueName;

    private static By getLocatorByName(String locatorName) {
        switch (locatorName.toLowerCase()) {
            case "save":
                return nameSaveButton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name %s", locatorName));
        }
    }

    public void tapButtonByName(String buttonName) throws Exception {
        getElement(getLocatorByName(buttonName)).click();
    }

    public boolean isUsernamePageVisible() throws Exception {
        return isLocatorDisplayed(nameSaveButton);
    }

    public void inputStringInNameInput(String name) throws Exception {
        final FBElement el = ((FBElement) getElement(fbNameUniqueUsernameInput));
        el.clear();
        el.sendKeys(name);
    }

    public boolean isSaveButtonEnabled() throws Exception {
        return getElement(nameSaveButton).isEnabled();
    }

    public void inputXrandomString(int count) throws Exception {
        this.newUniqueName = CommonUtils.generateRandomString(count).toLowerCase();
        inputStringInNameInput(newUniqueName);
    }

    private String getNameInputValue() throws Exception {
        return ((FBElement) getElement(fbNameUniqueUsernameInput)).getText();
    }

    public int getNameInputTextLength() throws Exception {
        return getNameInputValue().length();
    }

    public boolean isNameInputEmpty() throws Exception {
        return getNameInputValue().equals(FBDriverAPI.NULL_VALUE);
    }

    public boolean isUniqueUsernameInSettingsDisplayed() throws Exception {
        By locator = By.xpath(xpathStrUniqueUsernameInSettings.apply(this.newUniqueName));
        return isLocatorDisplayed(locator);
    }
}
