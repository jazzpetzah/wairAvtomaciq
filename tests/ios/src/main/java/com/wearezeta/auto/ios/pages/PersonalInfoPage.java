package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PersonalInfoPage extends IOSPage {
    private static final String xpathEmailField =
            "//UIAElement[@name='ProfileSelfNameField']/following-sibling::UIAStaticText[1]";
    @FindBy(xpath = xpathEmailField)
    private WebElement emailField;

    private static final String xpathUserProfileName =
            "//UIAApplication[1]/UIAWindow[1]/UIAElement[1]/UIATextView[1]";
    @FindBy(xpath = xpathUserProfileName)
    private WebElement profileNameField;

    private static final String classNameUIAButton = "UIAButton";
    @FindBy(className = classNameUIAButton)
    private List<WebElement> optionsButtons;

    private static final String nameProfileSettingsButton = "SettingsButton";
    @FindBy(name = nameProfileSettingsButton)
    private WebElement settingsButton;

    private static final String xpathSettingsAboutButton = "//UIAButton[@name='ABOUT' or @name='About']";
    @FindBy(xpath = xpathSettingsAboutButton)
    private WebElement aboutButton;

    private static final String nameTermsOfUseButton = "Terms of Use";
    @FindBy(name = nameTermsOfUseButton)
    private WebElement termsOfUseButton;

    private static final String nameSignOutButton = "SignOutButton";
    @FindBy(name = nameSignOutButton)
    private WebElement signoutButton;

    @FindBy(xpath = xpathStrMainWindow)
    private WebElement personalPage;

    private static final String namePictureButton = "CameraLibraryButton";
    @FindBy(name = namePictureButton)
    private WebElement pictureButton;

    private static final String xpathProfileNameEditField = "//UIAElement[@name='ProfileSelfNameField']/UIATextView";
    @FindBy(xpath = xpathProfileNameEditField)
    private WebElement profileNameEditField;

    private static final String nameSelfNameTooShortError = "AT LEAST 2 CHARACTERS ";
    @FindBy(name = nameSelfNameTooShortError)
    private WebElement nameTooShortError;

    public static final String xpathSettingsPage = "//UIANavigationBar[@name='Settings']";
    @FindBy(xpath = xpathSettingsPage)
    private WebElement settingsPage;

    private static final String xpathOptionsSettingsButton = "//UIAButton[@name='SETTINGS' or @name='Settings']";
    @FindBy(xpath = xpathOptionsSettingsButton)
    private WebElement optionsSettingsButton;

    private static final String nameSoundAlertsButton = "Alerts";
    @FindBy(name = nameSoundAlertsButton)
    private WebElement soundAlertsButton;

    private static final String xpathSoundAlertsPage = "//UIANavigationBar[@name='Alerts']";
    @FindBy(xpath = xpathSoundAlertsPage)
    private WebElement soundAlertsPage;

    private static final String xpathAllSoundAlertsButton = "//UIATableCell[@name='All']";
    @FindBy(xpath = xpathAllSoundAlertsButton)
    private WebElement allSoundAlertsButton;

    private static final String nameSettingsChangePasswordButton = "Change Password";
    @FindBy(name = nameSettingsChangePasswordButton)
    private WebElement settingsChangePasswordButton;

    private static final String nameSettingsResetPasswordButton = "Reset Password";
    @FindBy(name = nameSettingsResetPasswordButton)
    private WebElement settingsResetPasswordButton;

    private static final String nameSettingsAccountInfoButton = "Account";
    @FindBy(name = nameSettingsAccountInfoButton)
    private WebElement settingsAccountInfoButton;

    private static final String nameOptionsHelpButton = "HELP";
    @FindBy(name = nameOptionsHelpButton)
    private WebElement settingsHelpButton;

    private static final String xpathSettingsHelpHeader = "//UIAWebView/UIAStaticText[@name='Support']";
    @FindBy(xpath = xpathSettingsHelpHeader)
    private WebElement supportWebPageHeader;

    private static final String nameAccentColorPicker = "AccentColorPickerView";
    @FindBy(name = nameAccentColorPicker)
    private WebElement accentColorPicker;

    private static final String xpathSettingsChatheadSwitch = "//UIASwitch[@name='Message previews']";
    @FindBy(xpath = xpathSettingsChatheadSwitch)
    private WebElement settingsChatheadSwitch;

    private static final String nameSettingsBackButton = "Back";
    @FindBy(name = nameSettingsBackButton)
    private WebElement settingsBackButton;

    private static final String nameSettingsDoneButton = "Done";
    @FindBy(name = nameSettingsDoneButton)
    private WebElement settingsDoneButton;

    private static final String nameCloseButton = "CloseButton";
    @FindBy(name = PersonalInfoPage.nameCloseButton)
    private WebElement closeButton;

    private static final String nameWireWebsiteButton = "wire.com";
    @FindBy(name = nameWireWebsiteButton)
    private WebElement wireWebsiteButton;

    public static final String namePrivacyPolicyButton = "Privacy Policy";
    @FindBy(name = namePrivacyPolicyButton)
    private WebElement privacyPolicyButton;

    private static final String xpathBuildNumberText =
            "//UIAApplication/UIAWindow/UIAStaticText[contains(@name, 'Wire Swiss GmbH • version')]";
    @FindBy(xpath = xpathBuildNumberText)
    private WebElement buildNumberText;

    private static final String nameCloseLegalPageButton = "WebViewCloseButton";
    @FindBy(name = nameCloseLegalPageButton)
    private WebElement closeLegalPageButton;

    private static final String xpathTermsOfUsePageText =
            "//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAWebView[1]/UIAStaticText[2]";
    @FindBy(xpath = xpathTermsOfUsePageText)
    private WebElement termsOfUsePageText;

    private static final String xpathPrivacyPolicyPageText =
            "//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAWebView[1]/UIALink[1]/UIAStaticText[1]";
    @FindBy(xpath = xpathPrivacyPolicyPageText)
    private WebElement privacyPolicyPageText;

    private static final String xpathWireWebsiteUrl = "//UIAElement[@name ='URL']";
    @FindBy(xpath = xpathWireWebsiteUrl)
    private WebElement wireWebsitePageUrlLabel;

    private static final String xpathAboutPageWireLogo =
            "//UIAApplication/UIAWindow/UIAButton[@name='wire.com']/preceding-sibling::UIAImage[1]";
    @FindBy(xpath = xpathAboutPageWireLogo)
    private WebElement aboutPageWireLogo;

    private static final String nameAboutCloseButton = "aboutCloseButton";
    @FindBy(name = nameAboutCloseButton)
    private WebElement aboutCloseButton;

    private static final String nameAddPhoneNumberButton = "ADD PHONE NUMBER";
    @FindBy(name = nameAddPhoneNumberButton)
    private WebElement addPhoneNumberButton;

    private static final String nameThemeSwitcherButton = "ThemeButton";
    @FindBy(name = nameThemeSwitcherButton)
    private WebElement themeSwitcherButton;

    private static final Function<String, String> xpathPhoneEmailFieldByValue =
            value -> String.format("//UIAStaticText[contains(@name, '%s')]", value);

    private static final String nameProfileName = "ProfileSelfNameField";

    private static final String xpathChangePasswordPageChangePasswordButton = "//UIAButton[@name='RESET PASSWORD']";

    public PersonalInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    final String TERMS_OF_USE_PAGE_VALUE = "PLEASE READ THIS AGREEMENT CAREFULLY; THIS IS A BINDING CONTRACT.";
    final String PRIVACY_POLICY_PAGE_VALUE = "Our Privacy Commitment";
    final String ABOUT_LOGO_IMAGE = "about_page_logo.png";
    final int COLORS_COUNT = 7;

    final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.95;

    public void closePersonalInfo() {
        closeButton.click();
    }

    public String getUserNameValue() {
        return profileNameEditField.getText();
    }

    public boolean isUserNameContainingSpaces() {
        return profileNameEditField.getAttribute("value").contains(" ");
    }

    public String getUserEmailVaue() {
        return emailField.getText();
    }

    public void clickOnSettingsButton() {
        settingsButton.click();
    }

    public void clickOnAboutButton() {
        aboutButton.click();
    }

    public boolean isAboutPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameTermsOfUseButton));
    }

    public void clickAboutCloseButton() {
        aboutCloseButton.click();
    }

    public boolean isAboutPageCertainColor(String color) throws Exception {
        if (!color.equals("Violet")) {
            return false;
        }
        String aboutLogo = ABOUT_LOGO_IMAGE;
        String deviceType = CommonUtils.getDeviceName(this.getClass());
        BufferedImage coloredLogoImage = getElementScreenshot(aboutPageWireLogo)
                .orElseThrow(IllegalStateException::new);
        if (deviceType.equals("iPhone 6") || deviceType.equals("iPhone 6 Plus")) {
            aboutLogo = ABOUT_LOGO_IMAGE.replace(".png", "_iPhone.png");
        } else if (deviceType.equals("iPad Air")) {
            aboutLogo = ABOUT_LOGO_IMAGE.replace(".png", "_iPad.png");
        }
        BufferedImage realLogoImage = ImageUtil.readImageFromFile(IOSPage
                .getImagesPath() + aboutLogo);
        double score = ImageUtil.getOverlapScore(realLogoImage,
                coloredLogoImage,
                ImageUtil.RESIZE_REFERENCE_TO_TEMPLATE_RESOLUTION);
        return (score >= MIN_ACCEPTABLE_IMAGE_VALUE);
    }

    public boolean isWireWebsiteButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameWireWebsiteButton));
    }

    public boolean isTermsButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameTermsOfUseButton));
    }

    public boolean isPrivacyPolicyButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(namePrivacyPolicyButton));
    }

    public boolean isBuildNumberTextVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathBuildNumberText));
    }

    public void openTermsOfUsePage() {
        termsOfUseButton.click();
    }

    public void openPrivacyPolicyPage() {
        privacyPolicyButton.click();
    }

    public void openWireWebsite() {
        wireWebsiteButton.click();
    }

    public boolean isWireWebsitePageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathWireWebsiteUrl));
    }

    public void closeLegalPage() {
        closeLegalPageButton.click();
    }

    public boolean isTermsOfUsePageVisible() {
        return termsOfUsePageText.getAttribute("name").equals(
                TERMS_OF_USE_PAGE_VALUE);
    }

    public boolean isPrivacyPolicyPageVisible() {
        return privacyPolicyPageText.getAttribute("name").equals(
                PRIVACY_POLICY_PAGE_VALUE);
    }

    public boolean isResetPasswordPageVisible() throws Exception {
        return DriverUtils
                .waitUntilLocatorIsDisplayed(
                        getDriver(), By.xpath(xpathChangePasswordPageChangePasswordButton));
    }

    public void clickChangePasswordButton() {
        settingsResetPasswordButton.click();
    }

    public void tapOnEditNameField() throws Exception {
        getElement(By.xpath(xpathProfileNameEditField), "Edit name field is not visible").click();
    }

    public void changeNameUsingOnlySpaces() throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(), profileNameEditField);
        profileNameEditField.clear();
        profileNameEditField.sendKeys("  \n");
    }

    public void attemptTooLongName() {
        String name = CommonUtils.generateRandomString(80).toLowerCase();
        profileNameEditField.sendKeys(name + "\n");
    }

    public int getSelfNameLength() {
        return getUserNameValue().length();
    }

    public void changeName(String newName) throws Exception {
        final WebElement profileNameEditField = getElement(By.xpath(xpathProfileNameEditField),
                "Edit name field is not visible");
        profileNameEditField.clear();
        profileNameEditField.sendKeys(newName + "\n");
    }

    public boolean isTooShortNameErrorMessage() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameSelfNameTooShortError));
    }

    public void clearNameField() {
        profileNameEditField.clear();
    }

    public void enterNameInNameField(String username) throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(), profileNameEditField);
        profileNameEditField.sendKeys(username);
    }

    public void pressEnterInNameField() throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(), profileNameEditField);
        profileNameEditField.sendKeys("\n");
    }

    public void tapOnPersonalPage() {
        personalPage.click();
    }

    public void pressCameraButton() throws Exception {
        pictureButton.click();
    }

    public void tapOnSettingsButton() {
        optionsSettingsButton.click();
    }

    public boolean isSettingsPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathSettingsPage));
    }

    public void enterSoundAlertSettings() {
        soundAlertsButton.click();
    }

    public boolean isSoundAlertsPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathSoundAlertsPage));
    }

    public boolean isDefaultSoundValOne() {
        return allSoundAlertsButton.getAttribute("value").equals("1");
    }

    public void clickOnHelpButton() throws Exception {
        getElement(By.name(nameOptionsHelpButton), "Help button is not visible in Options").click();
    }

    public boolean isSupportWebPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathSettingsHelpHeader));
    }

    private void swipeColorPickerFromColorToColor(int startColor, int endColor) throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), accentColorPicker,
                1000, startColor * 2 * COLORS_COUNT - COLORS_COUNT, 50,
                endColor * 2 * COLORS_COUNT - COLORS_COUNT, 50);
    }

    public void switchChatheadsOnOff() {
        settingsChatheadSwitch.click();
    }

    public void pressSettingsBackButton() {
        settingsBackButton.click();
    }

    public void pressSettingsDoneButton() throws Exception {
        getElement(By.name(nameSettingsDoneButton), "Done button is not present in Settings").click();
    }

    public boolean waitSelfProfileVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                By.name(nameProfileName));
    }

    public void swipeAccentColor(String currentColor, String destColor)
            throws Exception {
        swipeColorPickerFromColorToColor(AccentColor.getByName(currentColor)
                .getId(), AccentColor.getByName(destColor).getId());
    }

    public void clickAddPhoneNumberButton() {
        addPhoneNumberButton.click();
    }

    public boolean isPhoneNumberAttachedToProfile(String number)
            throws Exception {
        final By locator = By.xpath(xpathPhoneEmailFieldByValue.apply(number));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isThemeSwitcherButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.name(nameThemeSwitcherButton), 5);
    }

    public void clickAccountInfoButton() throws Exception {
        getElement(By.name(nameSettingsAccountInfoButton), "Account button is not present in settings").
                click();
    }
}
