package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBDriverAPI;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class UniqueUsernamePage extends IOSPage {

    private static final By namePageHeader = MobileBy.AccessibilityId("Username");
    private static final By nameSaveButton = MobileBy.AccessibilityId("Save");
    private static final By nameUniqueUsernameInput = MobileBy.AccessibilityId("handleTextField");
    private static final Function<String, String> xpathStrErrorLabelByText = text ->
            String.format("//XCUIElementTypeStaticText[@value='%s']", text);

    public UniqueUsernamePage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

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

    public void inputStringInNameInput(String name) throws Exception {
        final WebElement el = getElement(nameUniqueUsernameInput);
        el.clear();
        el.sendKeys(name);
    }

    public boolean isSaveButtonEnabled() throws Exception {
        return getElement(nameSaveButton).isEnabled();
    }

    public String inputXCharsRandomString(int count) throws Exception {
        String newUniqueName = CommonUtils.generateRandomAlphanumericPlusUnderscoreString(count).toLowerCase();
        inputStringInNameInput(newUniqueName);
        return newUniqueName;
    }

    public String getNameInputValue() throws Exception {
        return getElement(nameUniqueUsernameInput).getText();
    }

    public int getNameInputTextLength() throws Exception {
        return getNameInputValue().length();
    }

    public boolean isNameInputEmpty() throws Exception {
        return getNameInputValue().equals(FBDriverAPI.NULL_VALUE);
    }

    public boolean isPageHeaderVisible() throws Exception {
        return isLocatorDisplayed(namePageHeader);
    }

    public boolean isErrorLabelVisible(String expectedLabel) throws Exception {
        final By locator = By.xpath(xpathStrErrorLabelByText.apply(expectedLabel));
        return isLocatorDisplayed(locator);
    }
}
