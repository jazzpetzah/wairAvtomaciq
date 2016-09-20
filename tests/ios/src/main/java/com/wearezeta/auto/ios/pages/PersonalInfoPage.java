package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PersonalInfoPage extends IOSPage {
    private static final Function<String, String> xpathStrEmailFieldByValue = value ->
            String.format("//UIAStaticText[contains(@name, '%s')]", value);

    private static final By xpathSettingsAboutButton = By.xpath("//UIAButton[@name='ABOUT' or @name='About']");

    private static final By xpathProfileNameEditField =
            By.xpath("//UIAElement[@name='ProfileSelfNameField']/UIATextView");

    private static final By nameSelfNameTooShortError = MobileBy.AccessibilityId("AT LEAST 2 CHARACTERS ");

    private static final By nameAccentColorPicker = MobileBy.AccessibilityId("AccentColorPickerView");

    private static final By nameCloseButton = MobileBy.AccessibilityId("CloseButton");

    private static final By nameAddPhoneNumberButton = MobileBy.AccessibilityId("ADD PHONE NUMBER");

    private static final By nameThemeSwitcherButton = MobileBy.AccessibilityId("ThemeButton");

    private static final Function<String, String> xpathStrPhoneEmailFieldByValue =
            value -> String.format("//UIAStaticText[contains(@name, '%s')]", value);

    private static final By nameProfileName = MobileBy.AccessibilityId("ProfileSelfNameField");


    private static final By nameAddEmailAddressButton = MobileBy.AccessibilityId("ADD EMAIL ADDRESS AND PASSWORD");

    private static final int COLORS_COUNT = 7;

    public PersonalInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void closePersonalInfo() throws Exception {
        getElement(nameCloseButton).click();
    }

    public String getUserNameValue() throws Exception {
        return getElement(xpathProfileNameEditField).getText();
    }

    public boolean isEmailVisible(String expectedEmail) throws Exception {
        final By locator = By.xpath(xpathStrEmailFieldByValue.apply(expectedEmail));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void clickOnAboutButton() throws Exception {
        getElement(xpathSettingsAboutButton).click();
    }

    public void tapOnEditNameField() throws Exception {
        getElement(xpathProfileNameEditField, "Edit name field is not visible").click();
    }

    public void changeName(String newName) throws Exception {
        final WebElement profileNameEditField = getElement(xpathProfileNameEditField, "Edit name field is not visible");
        profileNameEditField.clear();
        profileNameEditField.sendKeys(newName + "\n");
    }

    public boolean isTooShortNameErrorMessage() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameSelfNameTooShortError);
    }

    public void clearNameField() throws Exception {
        getElement(xpathProfileNameEditField).clear();
    }

    public void enterNameInNameField(String username) throws Exception {
        final WebElement profileNameEditField = getElement(xpathProfileNameEditField, "Edit name field is not visible");
        DriverUtils.tapByCoordinates(this.getDriver(), profileNameEditField);
        profileNameEditField.sendKeys(username);
    }

    public void pressEnterInNameField() throws Exception {
        final WebElement profileNameEditField = getElement(xpathProfileNameEditField, "Edit name field is not visible");
        DriverUtils.tapByCoordinates(this.getDriver(), profileNameEditField);
        profileNameEditField.sendKeys("\n");
    }

    public boolean waitSelfProfileVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameProfileName);
    }

    public void selectAccentColor(AccentColor destColor) throws Exception {
        DriverUtils.tapOnPercentOfElement(getDriver(), getElement(nameAccentColorPicker),
                (destColor.getId() - 1) * 100 / COLORS_COUNT + 50 / COLORS_COUNT, 50);
    }

    public void clickAddPhoneNumberButton() throws Exception {
        getElement(nameAddPhoneNumberButton).click();
        // Wait for animation
        Thread.sleep(4000);
    }

    public boolean isPhoneNumberAttachedToProfile(String number) throws Exception {
        final By locator = By.xpath(xpathStrPhoneEmailFieldByValue.apply(number));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isThemeSwitcherButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameThemeSwitcherButton, 5);
    }

    public BufferedImage getPeoplePickerScreenshot() throws Exception {
        return getElementScreenshot(getElement(nameAccentColorPicker)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of color picker")
        );
    }

    public void tapAddEmailAddressButton() throws Exception {
        getElement(nameAddEmailAddressButton).click();
    }
}
