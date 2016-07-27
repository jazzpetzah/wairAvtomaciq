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

    private static final By nameProfileSettingsButton = MobileBy.AccessibilityId("SettingsButton");

    private static final By xpathSettingsAboutButton = By.xpath("//UIAButton[@name='ABOUT' or @name='About']");

    private static final By nameTermsOfUseButton = MobileBy.AccessibilityId("Terms of Use");

//    private static final By nameOpenCameraRollButton = MobileBy.AccessibilityId("CameraLibraryButton");

    private static final By nameOpenCameraButton = MobileBy.AccessibilityId("cameraButton");

    private static final By xpathProfileNameEditField =
            By.xpath("//UIAElement[@name='ProfileSelfNameField']/UIATextView");

    private static final By nameSelfNameTooShortError = MobileBy.AccessibilityId("AT LEAST 2 CHARACTERS ");

    private static final By xpathOptionsSettingsButton = By.xpath("//UIAButton[@name='SETTINGS' or @name='Settings']");

    private static final By nameOptionsHelpButton = MobileBy.AccessibilityId("HELP");

    private static final By xpathAskSupport = By.xpath("//*[@name='Ask Support']");

    private static final By nameAccentColorPicker = MobileBy.AccessibilityId("AccentColorPickerView");

    private static final By nameCloseButton = MobileBy.AccessibilityId("CloseButton");

    private static final By nameWireWebsiteButton = MobileBy.AccessibilityId("wire.com");

    public static final By namePrivacyPolicyButton = MobileBy.AccessibilityId("Privacy Policy");

    private static final By xpathBuildNumberText = By.xpath(
            "//UIAApplication/UIAWindow/UIAStaticText[contains(@name, 'Wire Swiss GmbH • version')]");

    private static final By nameCloseLegalPageButton = MobileBy.AccessibilityId("WebViewCloseButton");

    private static final By xpathWireWebsiteUrl = MobileBy.AccessibilityId("URL");

    private static final By nameAboutCloseButton = MobileBy.AccessibilityId("aboutCloseButton");

    private static final By nameAddPhoneNumberButton = MobileBy.AccessibilityId("ADD PHONE NUMBER");

    private static final By nameThemeSwitcherButton = MobileBy.AccessibilityId("ThemeButton");

    private static final Function<String, String> xpathStrPhoneEmailFieldByValue =
            value -> String.format("//UIAStaticText[contains(@name, '%s')]", value);

    private static final By nameProfileName = MobileBy.AccessibilityId("ProfileSelfNameField");

    private static final By xpathChangePasswordPageChangePasswordButton =
            By.xpath("//UIAButton[@name='RESET PASSWORD']");

    private static final By xpathTermsAndConditions = By.xpath("//*[@name='Basic Information']");
    private static final By xpathPrivacyPolicy= By.xpath("//*[@name='Our Privacy Commitment']");

    private static final int COLORS_COUNT = 7;

    public PersonalInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void closePersonalInfo() throws Exception {
        getElement(nameCloseButton).click();
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameProfileSettingsButton);
    }

    public String getUserNameValue() throws Exception {
        return getElement(xpathProfileNameEditField).getText();
    }

    public boolean isEmailVisible(String expectedEmail) throws Exception {
        final By locator = By.xpath(xpathStrEmailFieldByValue.apply(expectedEmail));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void clickOnSettingsButton() throws Exception {
        getElement(nameProfileSettingsButton).click();
        // Wait for animation
        Thread.sleep(2000);
    }

    public void clickOnAboutButton() throws Exception {
        getElement(xpathSettingsAboutButton).click();
    }

    public boolean isAboutPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameTermsOfUseButton);
    }

    public void clickAboutCloseButton() throws Exception {
        getElement(nameAboutCloseButton).click();
    }

    public boolean isWireWebsiteButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameWireWebsiteButton);
    }

    public boolean isTermsButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameTermsOfUseButton);
    }

    public boolean isPrivacyPolicyButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), namePrivacyPolicyButton);
    }

    public boolean isBuildNumberTextVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathBuildNumberText);
    }

    public void openTermsOfUsePage() throws Exception {
        getElement(nameTermsOfUseButton).click();
    }

    public void openPrivacyPolicyPage() throws Exception {
        getElement(namePrivacyPolicyButton).click();
    }

    public void openWireWebsite() throws Exception {
        getElement(nameWireWebsiteButton).click();
    }

    public boolean isWireWebsitePageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), xpathWireWebsiteUrl, 30);
    }

    public void closeLegalPage() throws Exception {
        getElement(nameCloseLegalPageButton).click();
    }

    public boolean isTermsOfUsePageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), xpathTermsAndConditions, 15);
    }

    public boolean isPrivacyPolicyPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), xpathPrivacyPolicy, 15);
    }

    public boolean isResetPasswordPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), xpathChangePasswordPageChangePasswordButton);
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

    public void tapCameraButton() throws Exception {
        getElement(nameOpenCameraButton).click();
    }

    public void tapOnSettingsButton() throws Exception {
        getElement(xpathOptionsSettingsButton).click();
    }

    public void clickOnHelpButton() throws Exception {
        getElement(nameOptionsHelpButton, "Help button is not visible in Options").click();
    }

    public boolean isSupportWebPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), xpathAskSupport, 15);
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
}
