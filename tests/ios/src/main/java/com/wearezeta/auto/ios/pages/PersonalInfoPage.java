package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class PersonalInfoPage extends IOSPage {

	@FindBy(how = How.XPATH, using = IOSLocators.xpathEmailField)
	private WebElement emailField;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathUserProfileName)
	private WebElement profileNameField;

	@FindBy(how = How.CLASS_NAME, using = IOSLocators.classNameUIAButton)
	private List<WebElement> optionsButtons;

	@FindBy(how = How.NAME, using = IOSLocators.nameProfileSettingsButton)
	private WebElement settingsButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSettingsAboutButton)
	private WebElement aboutButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameTermsOfUseButton)
	private WebElement termsOfUseButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameSignOutButton)
	private WebElement signoutButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathPersonalInfoPage)
	private WebElement personalPage;

	@FindBy(how = How.NAME, using = IOSLocators.namePictureButton)
	private WebElement pictureButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathProfileNameEditField)
	private WebElement profileNameEditField;

	@FindBy(how = How.NAME, using = IOSLocators.nameSelfNameTooShortError)
	private WebElement nameTooShortError;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSettingsPage)
	private WebElement settingsPage;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathOptionsSettingsButton)
	private WebElement optionsSettingsButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameSoundAlertsButton)
	private WebElement soundAlertsButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSoundAlertsPage)
	private WebElement soundAlertsPage;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathAllSoundAlertsButton)
	private WebElement allSoundAlertsButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameSettingsChangePasswordButton)
	private WebElement settingsChangePasswordButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathChangePasswordPageChangePasswordButton)
	private WebElement changePasswordPageChangePasswordButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameOptionsHelpButton)
	private WebElement settingsHelpButton;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSettingsHelpHeader)
	private WebElement supportWebPageHeader;

	@FindBy(how = How.NAME, using = IOSLocators.nameAccentColorPicker)
	private WebElement accentColorPicker;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathSettingsChatheadSwitch)
	private WebElement settingsChatheadSwitch;

	@FindBy(how = How.NAME, using = IOSLocators.nameSettingsBackButton)
	private WebElement settingsBackButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameSettingsDoneButton)
	private WebElement settingsDoneButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameWireWebsiteButton)
	private WebElement wireWebsiteButton;
	
	@FindBy(how = How.NAME, using = IOSLocators.namePrivacyPolicyButton)
	private WebElement privacyPolicyButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathBuildNumberText)
	private WebElement buildNumberText;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameCloseLegalPageButton)
	private WebElement closeLegalPageButton;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathTermsOfUsePageText)
	private WebElement termsOfUsePageText;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathPrivacyPolicyPageText)
	private WebElement privacyPolicyPageText;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathWireWebsitePageText)
	private WebElement wireWebsitePageText;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameWireWebsitePageUrl)
	private WebElement wireWebsitePageUrl;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathWireWebsitePageUrlValue)
	private WebElement wireWebsitePageUrlValue;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathAboutPageWireLogo)
	private WebElement aboutPageWireLogo;
	
	final String WIRE_WEBSITE_PAGE_VALUE = "Great conversations";
	final String TERMS_OF_USE_PAGE_VALUE = "PLEASE READ THIS AGREEMENT CAREFULLY; THIS IS A BINDING CONTRACT.";
	final String PRIVACY_POLICY_PAGE_VALUE = "Our Privacy Commitment";
	final String ABOUT_LOGO_IMAGE = "about_page_logo.png";
	
	final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.95;
	
	public PersonalInfoPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public String getUserNameValue() {
		String name = profileNameEditField.getText();
		return name;
	}

	public String getUserEmailVaue() {
		String email = emailField.getText();
		return email;
	}

	public boolean isSettingsButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameProfileSettingsButton));
	}

	public PersonalInfoPage clickOnSettingsButton() {
		settingsButton.click();
		return this;
	}

	public PersonalInfoPage clickOnAboutButton() {
		aboutButton.click();
		return this;
	}

	
	
	
	
	public boolean isAboutPageVisible() {
		return termsOfUseButton.isDisplayed();
	}

	public boolean isAboutPageCertainColor(String color) throws Exception {
		if(!color.equals("Violet")){
			return false;
		}
		BufferedImage coloredLogoImage = getElementScreenshot(aboutPageWireLogo);
		System.out.println(CommonUtils
				.getImagesPath(CommonUtils.class) + ABOUT_LOGO_IMAGE);
		BufferedImage realLogoImage = ImageUtil.readImageFromFile(CommonUtils
				.getImagesPath(CommonUtils.class) + ABOUT_LOGO_IMAGE);
		double score = ImageUtil.getOverlapScore(realLogoImage, coloredLogoImage,
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
	
	public boolean isWireWebsitePageVisible() {
		return wireWebsitePageText.getAttribute("name").equals(WIRE_WEBSITE_PAGE_VALUE);
	}
	
	public void closeLegalPage() {
		closeLegalPageButton.click();
	}
	
	public boolean isTermsOfUsePageVisible() {
		return termsOfUsePageText.getAttribute("name").equals(TERMS_OF_USE_PAGE_VALUE);
	}
	
	public boolean isPrivacyPolicyPageVisible() {
		return privacyPolicyPageText.getAttribute("name").equals(PRIVACY_POLICY_PAGE_VALUE);
	}
	
	
	
	
	
	
	
	public boolean isResetPasswordPageVisible() {
		return changePasswordPageChangePasswordButton.isDisplayed();
	}

	public void clickChangePasswordButton() {
		settingsChangePasswordButton.click();
	}

	public LoginPage clickSignoutButton() throws Exception {
		LoginPage page;
		signoutButton.click();
		page = new LoginPage(this.getLazyDriver());
		return page;
	}

	public void tapOnEditNameField() throws Exception {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(profileNameEditField));
		profileNameEditField.click();
	}

	public void changeName(String newName) throws Exception {
		profileNameEditField.clear();
		profileNameEditField.sendKeys(newName);
	}

	public boolean isTooShortNameErrorMessage() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.name(IOSLocators.nameSelfNameTooShortError));
	}

	public void clearNameField() {
		profileNameEditField.clear();
	}

	public void enterNameInNamefield(String username) throws Exception {
		DriverUtils.mobileTapByCoordinates(this.getDriver(),
				profileNameEditField);
		profileNameEditField.sendKeys(username);
	}

	public void pressEnterInNameField() throws Exception {
		DriverUtils.mobileTapByCoordinates(this.getDriver(),
				profileNameEditField);
		profileNameEditField.sendKeys("\n");
	}

	public void waitForSettingsButtonAppears() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameProfileSettingsButton));
	}

	public void waitForEmailFieldVisible() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathEmailField));
	}

	public void tapOptionsButtonByText(String buttonText) {

		for (WebElement button : optionsButtons) {
			if (button.getText().equals(buttonText)) {
				button.click();
				break;
			}
		}
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

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {

		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			break;
		}
		case UP: {
			break;
		}
		case LEFT: {
			break;
		}
		case RIGHT: {
			page = new ContactListPage(this.getLazyDriver());
			break;
		}
		}
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

	public void isDefaultSoundValOne() {
		allSoundAlertsButton.getAttribute("value").equals("1");
	}

	public void clickOnHelpButton() {
		settingsHelpButton.click();
	}

	public boolean isSupportWebPageVisible() {
		return supportWebPageHeader.isDisplayed();
	}

	public void changeAccentColor() {
		accentColorPicker.click();
	}

	public void switchChatheadsOnOff() {
		settingsChatheadSwitch.click();
	}

	public void pressSettingsBackButton() {
		settingsBackButton.click();
	}

	public void pressSettingsDoneButton() {
		settingsDoneButton.click();
	}

}
