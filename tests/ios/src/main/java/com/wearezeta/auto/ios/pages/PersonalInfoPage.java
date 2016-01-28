package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PersonalInfoPage extends IOSPage {
    private static final Function<String, String> xpathStrEmailFieldByValue = value ->
            String.format("//*UIAStaticText[contains(@name, '%s')]", value);

    private static final By nameProfileSettingsButton = By.name("SettingsButton");

    private static final By xpathSettingsAboutButton = By.xpath("//UIAButton[@name='ABOUT' or @name='About']");

    private static final By nameTermsOfUseButton = By.name("Terms of Use");

    private static final By namePictureButton = By.name("CameraLibraryButton");

    private static final By xpathProfileNameEditField =
            By.xpath("//UIAElement[@name='ProfileSelfNameField']/UIATextView");

    private static final By nameSelfNameTooShortError = By.name("AT LEAST 2 CHARACTERS ");

    public static final By xpathSettingsPage = By.xpath("//UIANavigationBar[@name='Settings']");

    private static final By xpathOptionsSettingsButton = By.xpath("//UIAButton[@name='SETTINGS' or @name='Settings']");

    private static final By nameSoundAlertsButton = By.name("Alerts");

    private static final By xpathSoundAlertsPage = By.xpath("//UIANavigationBar[@name='Alerts']");

    private static final By xpathAllSoundAlertsButton = By.xpath("//UIATableCell[@name='All']");

    private static final By nameSettingsResetPasswordButton = By.name("Reset Password");

    private static final By nameSettingsAccountInfoButton = By.name("Account");

    private static final By nameOptionsHelpButton = By.name("HELP");

    private static final By xpathSettingsHelpHeader = By.xpath("//UIAWebView/UIAStaticText[@name='Support']");

    private static final By nameAccentColorPicker = By.name("AccentColorPickerView");

    private static final By xpathSettingsChatheadSwitch = By.xpath("//UIASwitch[@name='Message previews']");

    private static final By nameSettingsBackButton = By.name("Back");

    private static final By nameSettingsDoneButton = By.name("Done");

    private static final By nameCloseButton = By.name("CloseButton");

    private static final By nameWireWebsiteButton = By.name("wire.com");

    public static final By namePrivacyPolicyButton = By.name("Privacy Policy");

    private static final By xpathBuildNumberText = By.xpath(
            "//UIAApplication/UIAWindow/UIAStaticText[contains(@name, 'Wire Swiss GmbH • version')]");

    private static final By nameCloseLegalPageButton = By.name("WebViewCloseButton");

    private static final Function<String, String> xpathStrTermsOfUseByText = text ->
            String.format("//UIAStaticText[@name='%s']", text);

    private static final Function<String, String> xpathStrPrivacyPolicyByText = text ->
            String.format("//UIAStaticText[@name='%s']", text);

    private static final By xpathWireWebsiteUrl = By.xpath("//UIAElement[@name ='URL']");

    private static final By xpathAboutPageWireLogo = By.xpath(
            "//UIAApplication/UIAWindow/UIAButton[@name='wire.com']/preceding-sibling::UIAImage[1]");

    private static final By nameAboutCloseButton = By.name("aboutCloseButton");

    private static final By nameAddPhoneNumberButton = By.name("ADD PHONE NUMBER");

    private static final By nameThemeSwitcherButton = By.name("ThemeButton");

    private static final Function<String, String> xpathStrPhoneEmailFieldByValue =
            value -> String.format("//UIAStaticText[contains(@name, '%s')]", value);

    private static final By nameProfileName = By.name("ProfileSelfNameField");

    private static final By xpathChangePasswordPageChangePasswordButton =
            By.xpath("//UIAButton[@name='RESET PASSWORD']");

    private static final String TERMS_OF_USE_PAGE_VALUE =
            "PLEASE READ THIS AGREEMENT CAREFULLY; THIS IS A BINDING CONTRACT.";
    private static final String PRIVACY_POLICY_PAGE_VALUE = "Our Privacy Commitment";
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

    public boolean isUserNameContainingSpaces() throws Exception {
        return getElement(xpathProfileNameEditField).getAttribute("value").contains(" ");
    }

    public boolean isEmailVisible(String expectedEmail) throws Exception {
        final By locator = By.xpath(xpathStrEmailFieldByValue.apply(expectedEmail));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void clickOnSettingsButton() throws Exception {
        getElement(nameProfileSettingsButton).click();
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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathWireWebsiteUrl);
    }

    public void closeLegalPage() throws Exception {
        getElement(nameCloseLegalPageButton).click();
    }

    public boolean isTermsOfUsePageVisible() throws Exception {
        final By locator = By.xpath(xpathStrTermsOfUseByText.apply(TERMS_OF_USE_PAGE_VALUE));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isPrivacyPolicyPageVisible() throws Exception {
        final By locator = By.xpath(xpathStrPrivacyPolicyByText.apply(PRIVACY_POLICY_PAGE_VALUE));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isResetPasswordPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathChangePasswordPageChangePasswordButton);
    }

    public void clickChangePasswordButton() throws Exception {
        getElement(nameSettingsResetPasswordButton).click();
    }

    public void tapOnEditNameField() throws Exception {
        getElement(xpathProfileNameEditField, "Edit name field is not visible").click();
    }

    public void changeNameUsingOnlySpaces() throws Exception {
        final WebElement profileNameEditField = getElement(xpathProfileNameEditField);
        DriverUtils.tapByCoordinates(this.getDriver(), profileNameEditField);
        profileNameEditField.clear();
        profileNameEditField.sendKeys("  \n");
    }

    public void attemptTooLongName() throws Exception {
        String name = CommonUtils.generateRandomString(80).toLowerCase();
        getElement(xpathProfileNameEditField).sendKeys(name + "\n");
    }

    public int getSelfNameLength() throws Exception {
        return getUserNameValue().length();
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

    public void tapOnPersonalPage() throws Exception {
        getElement(nameMainWindow).click();
    }

    public void pressCameraButton() throws Exception {
        getElement(namePictureButton).click();
    }

    public void tapOnSettingsButton() throws Exception {
        getElement(xpathOptionsSettingsButton).click();
    }

    public boolean isSettingsPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSettingsPage);
    }

    public void enterSoundAlertSettings() throws Exception {
        getElement(nameSoundAlertsButton).click();
    }

    public boolean isSoundAlertsPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSoundAlertsPage);
    }

    public boolean isDefaultSoundValOne() throws Exception {
        return getElement(xpathAllSoundAlertsButton).getAttribute("value").equals("1");
    }

    public void clickOnHelpButton() throws Exception {
        getElement(nameOptionsHelpButton, "Help button is not visible in Options").click();
    }

    public boolean isSupportWebPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSettingsHelpHeader);
    }

    private void swipeColorPickerFromColorToColor(int startColor, int endColor) throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), getElement(nameAccentColorPicker),
                1000, startColor * 2 * COLORS_COUNT - COLORS_COUNT, 50,
                endColor * 2 * COLORS_COUNT - COLORS_COUNT, 50);
    }

    public void switchChatheadsOnOff() throws Exception {
        getElement(xpathSettingsChatheadSwitch).click();
    }

    public void pressSettingsBackButton() throws Exception {
        getElement(nameSettingsBackButton).click();
    }

    public void pressSettingsDoneButton() throws Exception {
        getElement(nameSettingsDoneButton, "Done button is not present in Settings").click();
    }

    public boolean waitSelfProfileVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), nameProfileName);
    }

    public void swipeAccentColor(String currentColor, String destColor)
            throws Exception {
        swipeColorPickerFromColorToColor(AccentColor.getByName(currentColor)
                .getId(), AccentColor.getByName(destColor).getId());
    }

    public void clickAddPhoneNumberButton() throws Exception {
        getElement(nameAddPhoneNumberButton).click();
    }

    public boolean isPhoneNumberAttachedToProfile(String number)
            throws Exception {
        final By locator = By.xpath(xpathStrPhoneEmailFieldByValue.apply(number));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isThemeSwitcherButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameThemeSwitcherButton, 5);
    }

    public void clickAccountInfoButton() throws Exception {
        getElement(nameSettingsAccountInfoButton, "Account button is not present in settings").click();
    }
}
