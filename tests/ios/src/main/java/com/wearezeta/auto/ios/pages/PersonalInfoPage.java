package com.wearezeta.auto.ios.pages;

import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

	@FindBy(how = How.NAME, using = IOSLocators.nameSettingsAboutButton)
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

	@FindBy(how = How.NAME, using = IOSLocators.nameOptionsSettingsButton)
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

	public PersonalInfoPage(ZetaIOSDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public String getUserNameValue() {
		String name = profileNameField.getText();
		return name;
	}

	public String getUserEmailVaue() {
		String email = emailField.getText();
		return email;
	}

	public boolean isSettingsButtonVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
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

	public boolean isResetPasswordPageVisible() {
		return changePasswordPageChangePasswordButton.isDisplayed();
	}

	public void clickChangePasswordButton() {
		settingsChangePasswordButton.click();
	}

	public LoginPage clickSignoutButton() throws Exception {
		LoginPage page;
		signoutButton.click();
		page = new LoginPage(this.getDriver(), this.getWait());
		return page;
	}

	public void tapOnEditNameField() {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(profileNameEditField));
		profileNameEditField.click();
	}

	public boolean isTooShortNameErrorMessage() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(),
				By.name(IOSLocators.nameSelfNameTooShortError));
	}

	public void clearNameField() {
		profileNameEditField.clear();
	}

	public void enterNameInNamefield(String username) {
		DriverUtils.mobileTapByCoordinates(this.getDriver(),
				profileNameEditField);
		profileNameEditField.sendKeys(username);
	}

	public void pressEnterInNameField() {
		DriverUtils.mobileTapByCoordinates(this.getDriver(),
				profileNameEditField);
		profileNameEditField.sendKeys("\n");
	}

	public void waitForSettingsButtonAppears() throws Exception {
		DriverUtils.waitUntilElementAppears(driver,
				By.name(IOSLocators.nameProfileSettingsButton));
	}

	public void waitForEmailFieldVisible() throws Exception {
		DriverUtils.waitUntilElementAppears(driver,
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
		page = new CameraRollPage(this.getDriver(), this.getWait());
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
			page = new ContactListPage(this.getDriver(), this.getWait());
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

}
