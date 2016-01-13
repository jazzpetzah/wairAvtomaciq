package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class PersonalInfoPage extends IOSPage {
    public static final String xpathEmailField =
            "//UIAElement[@name='ProfileSelfNameField']/following-sibling::UIAStaticText[1]";
    @FindBy(xpath = xpathEmailField)
    private WebElement emailField;

    public static final String xpathUserProfileName =
            "//UIAApplication[1]/UIAWindow[1]/UIAElement[1]/UIATextView[1]";
    @FindBy(xpath = xpathUserProfileName)
    private WebElement profileNameField;

    public static final String classNameUIAButton = "UIAButton";
    @FindBy(className = classNameUIAButton)
    private List<WebElement> optionsButtons;

    public static final String nameProfileSettingsButton = "SettingsButton";
    @FindBy(name = nameProfileSettingsButton)
    private WebElement settingsButton;

    public static final String xpathSettingsAboutButton = "//UIAButton[@name='ABOUT' or @name='About']";
    @FindBy(xpath = xpathSettingsAboutButton)
    private WebElement aboutButton;

    public static final String nameTermsOfUseButton = "Terms of Use";
    @FindBy(name = nameTermsOfUseButton)
    private WebElement termsOfUseButton;

    public static final String nameSignOutButton = "SignOutButton";
    @FindBy(name = nameSignOutButton)
    private WebElement signoutButton;

    public static final String xpathPersonalInfoPage = "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']";
    @FindBy(xpath = xpathPersonalInfoPage)
    private WebElement personalPage;

    public static final String namePictureButton = "CameraLibraryButton";
    @FindBy(name = namePictureButton)
    private WebElement pictureButton;

    public static final String xpathProfileNameEditField = "//UIAElement[@name='ProfileSelfNameField']/UIATextView";
    @FindBy(xpath = xpathProfileNameEditField)
    private WebElement profileNameEditField;

    public static final String nameSelfNameTooShortError = "AT LEAST 2 CHARACTERS ";
    @FindBy(name = nameSelfNameTooShortError)
    private WebElement nameTooShortError;

    public static final String xpathSettingsPage = "//UIANavigationBar[@name='Settings']";
    @FindBy(xpath = xpathSettingsPage)
    private WebElement settingsPage;

    public static final String xpathOptionsSettingsButton = "//UIAButton[@name='SETTINGS' or @name='Settings']";
    @FindBy(xpath = xpathOptionsSettingsButton)
    private WebElement optionsSettingsButton;

    public static final String nameSoundAlertsButton = "Alerts";
    @FindBy(name = nameSoundAlertsButton)
    private WebElement soundAlertsButton;

    public static final String xpathSoundAlertsPage = "//UIANavigationBar[@name='Alerts']";
    @FindBy(xpath = xpathSoundAlertsPage)
    private WebElement soundAlertsPage;

    public static final String xpathAllSoundAlertsButton = "//UIATableCell[@name='All']";
    @FindBy(xpath = xpathAllSoundAlertsButton)
    private WebElement allSoundAlertsButton;

    public static final String nameSettingsChangePasswordButton = "Change Password";
    @FindBy(name = nameSettingsChangePasswordButton)
    private WebElement settingsChangePasswordButton;

    public static final String nameSettingsResetPasswordButton = "Reset Password";
    @FindBy(name = nameSettingsResetPasswordButton)
    private WebElement settingsResetPasswordButton;

    public static final String nameSettingsAccountInfoButton = "Account";
    @FindBy(name = nameSettingsAccountInfoButton)
    private WebElement settingsAccountInfoButton;

    public static final String nameOptionsHelpButton = "HELP";
    @FindBy(name = nameOptionsHelpButton)
    private WebElement settingsHelpButton;

    public static final String xpathSettingsHelpHeader = "//UIAWebView/UIAStaticText[@name='Support']";
    @FindBy(xpath = xpathSettingsHelpHeader)
    private WebElement supportWebPageHeader;

    public static final String nameAccentColorPicker = "AccentColorPickerView";
    @FindBy(name = nameAccentColorPicker)
    private WebElement accentColorPicker;

    public static final String xpathSettingsChatheadSwitch = "//UIASwitch[@name='Message previews']";
    @FindBy(xpath = xpathSettingsChatheadSwitch)
    private WebElement settingsChatheadSwitch;

    public static final String nameSettingsBackButton = "Back";
    @FindBy(name = nameSettingsBackButton)
    private WebElement settingsBackButton;

    public static final String nameSettingsDoneButton = "Done";
    @FindBy(name = nameSettingsDoneButton)
    private WebElement settingsDoneButton;

    public static final String nameCloseButton = "CloseButton";
    @FindBy(name = PersonalInfoPage.nameCloseButton)
    private WebElement closeButton;

    public static final String nameWireWebsiteButton = "wire.com";
    @FindBy(name = nameWireWebsiteButton)
    private WebElement wireWebsiteButton;

    public static final String namePrivacyPolicyButton = "Privacy Policy";
    @FindBy(name = namePrivacyPolicyButton)
    private WebElement privacyPolicyButton;

    public static final String xpathBuildNumberText =
            "//UIAApplication/UIAWindow/UIAStaticText[contains(@name, 'Wire Swiss GmbH • version')]";
    @FindBy(xpath = xpathBuildNumberText)
    private WebElement buildNumberText;

    public static final String nameCloseLegalPageButton = "WebViewCloseButton";
    @FindBy(name = nameCloseLegalPageButton)
    private WebElement closeLegalPageButton;

    public static final String xpathTermsOfUsePageText =
            "//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAWebView[1]/UIAStaticText[2]";
    @FindBy(xpath = xpathTermsOfUsePageText)
    private WebElement termsOfUsePageText;

    public static final String xpathPrivacyPolicyPageText =
            "//UIAApplication[1]/UIAWindow[2]/UIAScrollView[1]/UIAWebView[1]/UIALink[1]/UIAStaticText[1]";
    @FindBy(xpath = xpathPrivacyPolicyPageText)
    private WebElement privacyPolicyPageText;

    public static final String xpathWireWebsiteUrl = "//UIAElement[@name ='URL']";
    @FindBy(xpath = xpathWireWebsiteUrl)
    private WebElement wireWebsitePageUrlLabel;

    public static final String xpathAboutPageWireLogo =
            "//UIAApplication/UIAWindow/UIAButton[@name='wire.com']/preceding-sibling::UIAImage[1]";
    @FindBy(xpath = xpathAboutPageWireLogo)
    private WebElement aboutPageWireLogo;

    public static final String nameAboutCloseButton = "aboutCloseButton";
    @FindBy(name = nameAboutCloseButton)
    private WebElement aboutCloseButton;

    public static final String nameAddPhoneNumberButton = "ADD PHONE NUMBER";
    @FindBy(name = nameAddPhoneNumberButton)
    private WebElement addPhoneNumberButton;

    public static final String nameThemeSwitcherButton = "ThemeButton";
    @FindBy(name = nameThemeSwitcherButton)
    private WebElement themeSwitcherButton;

    public static final String xpathPhoneEmailField =  "//UIAStaticText[contains(@name, '%s')]";

    public static final String nameProfileName = "ProfileSelfNameField";

    public static final String xpathChangePasswordPageChangePasswordButton = "//UIAButton[@name='RESET PASSWORD']";

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
        return DriverUtils.waitUntilElementClickable(getDriver(),
                termsOfUseButton);
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

    public boolean isWireWebsiteButtonVisible() {
        return wireWebsiteButton.isDisplayed();
    }

    public boolean isTermsButtonVisible() {
        return termsOfUseButton.isDisplayed();
    }

    public boolean isPrivacyPolicyButtonVisible() {
        return privacyPolicyButton.isDisplayed();
    }

    public boolean isBuildNumberTextVisible() {
        return buildNumberText.isDisplayed();
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
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(xpathWireWebsiteUrl));
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
                .waitUntilLocatorAppears(
                        getDriver(), By.xpath(xpathChangePasswordPageChangePasswordButton));
    }

    public void clickChangePasswordButton() {
        settingsResetPasswordButton.click();
    }

    public void tapOnEditNameField() throws Exception {
        this.getWait().until(
                ExpectedConditions.elementToBeClickable(profileNameEditField));
        profileNameEditField.click();
    }

    public void changeNameUsingOnlySpaces() throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(),
                profileNameEditField);
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
        DriverUtils
                .waitUntilElementClickable(getDriver(), profileNameEditField);
        profileNameEditField.clear();
        profileNameEditField.sendKeys(newName + "\n");
    }

    public boolean isTooShortNameErrorMessage() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.name(nameSelfNameTooShortError));
    }

    public void clearNameField() {
        profileNameEditField.clear();
    }

    public void enterNameInNamefield(String username) throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(),
                profileNameEditField);
        profileNameEditField.sendKeys(username);
    }

    public void pressEnterInNameField() throws Exception {
        DriverUtils.tapByCoordinates(this.getDriver(),
                profileNameEditField);
        profileNameEditField.sendKeys("\n");
    }

    public void tapOnPersonalPage() {
        personalPage.click();
    }

    public CameraRollPage pressCameraButton() throws Exception {

        CameraRollPage page;
        page = new CameraRollPage(this.getLazyDriver());
        pictureButton.click();

        return page;
    }

    public void tapOnSettingsButton() {
        optionsSettingsButton.click();
    }

    public void isSettingsPageVisible() {
        settingsPage.isDisplayed();
    }

    public void enterSoundAlertSettings() {
        soundAlertsButton.click();
    }

    public void isSoundAlertsPageVisible() {
        soundAlertsPage.isDisplayed();
    }

    public boolean isDefaultSoundValOne() {
        return allSoundAlertsButton.getAttribute("value").equals("1");
    }

    public void clickOnHelpButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), settingsHelpButton);
        settingsHelpButton.click();
    }

    public boolean isSupportWebPageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(xpathSettingsHelpHeader));
    }

    private void swipeColorPickerFromColorToColor(int startColor, int endColor)
            throws Exception {
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
        DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(nameSettingsDoneButton), 5);
        settingsDoneButton.click();
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
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By
                .xpath(String.format(xpathPhoneEmailField, number)));
    }

    public boolean isThemeSwitcherButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.name(nameThemeSwitcherButton), 5);
    }

    public void clickAccountInfoButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), settingsAccountInfoButton);
        settingsAccountInfoButton.click();
    }
}
