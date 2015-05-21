package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class PersonalInfoPage extends AndroidPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(id = AndroidLocators.PersonalInfoPage.idBackgroundOverlay)
	private WebElement backgroundOverlay;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathSettingsBox)
	private WebElement settingBox;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathEmailField)
	private WebElement emailField;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathNameField)
	private WebElement nameField;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathSettingsBtn)
	private WebElement settingsButton;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathNameEdit)
	private WebElement nameEdit;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathChangePhotoBtn)
	private WebElement changePhotoBtn;

	@FindBy(id = AndroidLocators.CommonLocators.idGalleryBtn)
	private WebElement galleryBtn;

	@FindBy(id = AndroidLocators.DialogPage.idConfirmButton)
	private WebElement confirmBtn;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathProfileOptionsButton)
	private WebElement optionsButton;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathAboutButton)
	private WebElement aboutButton;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathSelfProfileClose)
	private WebElement selfProfileClose;

	@FindBy(id = AndroidLocators.CommonLocators.idPager)
	private WebElement page;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathSignOutBtn)
	private WebElement signOutBtn;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idOpenFrom)
	private List<WebElement> openFrom;

	private static final String EMPTY_NAME = "Your name";

	public PersonalInfoPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public boolean isPersonalInfoVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(emailField);
	}

	public void waitForEmailFieldVisible() throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(emailField));
	}

	public void clickOnPage() throws Exception {
		DriverUtils.androidMultiTap(this.getDriver(), page, 1, 0.2);
	}

	public void tapChangePhotoButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				changePhotoBtn);
		changePhotoBtn.click();
	}

	public void tapGalleryButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), galleryBtn);
		galleryBtn.click();
	}

	public void tapConfirmButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), confirmBtn);
		this.hideKeyboard();
		confirmBtn.click();
	}

	public void tapSignOutBtn() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), signOutBtn);
		signOutBtn.click();
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		switch (direction) {
		case RIGHT: {
			return new ContactListPage(this.getLazyDriver());
		}
		default:
			return null;
		}
	}

	public void tapOptionsButton() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), optionsButton);
		try {
			optionsButton.click();
		} catch (ElementNotVisibleException e) {
			// pass silently, this throws exception due to some internal
			// Selendroid (or AUT %) ) issue
		}
	}

	public SettingsPage tapSettingsButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				settingsButton);
		settingsButton.click();
		return new SettingsPage(this.getLazyDriver());
	}

	public void waitForConfirmBtn() throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(confirmBtn));
	}

	public void tapOnMyName() throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(nameField));
		nameField.click();
		if (!DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(AndroidLocators.PersonalInfoPage.xpathNameEdit))) {
			DriverUtils.mobileTapByCoordinates(getDriver(), nameField);
		}
	}

	public boolean isNameEditVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.xpath(AndroidLocators.PersonalInfoPage.xpathNameEdit));
	}

	public void clearSelfName() {
		nameEdit.clear();
	}

	public void changeName(String name, String newName) throws Exception {
		nameEdit.sendKeys(newName);
		this.getDriver().navigate().back();
	}

	@Override
	public ContactListPage navigateBack() throws Exception {
		this.getDriver().navigate().back();
		return new ContactListPage(this.getLazyDriver());
	}

	public String getUserName() throws Exception {
		return nameField.getText();
	}

	public AboutPage tapAboutButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(), aboutButton);
		aboutButton.click();
		return new AboutPage(this.getLazyDriver());
	}

	public boolean isSettingsVisible() {
		return DriverUtils.isElementPresentAndDisplayed(settingBox);
	}

	public boolean waitForOptionsMenuToDisappear() throws Exception {
		return DriverUtils
				.waitUntilLocatorDissapears(
						this.getDriver(),
						By.xpath(AndroidLocators.PersonalInfoPage.xpathAboutButton),
						10);
	}

	public boolean waitForSettingsDissapear() throws Exception {
		return DriverUtils
				.waitUntilLocatorDissapears(
						this.getDriver(),
						By.xpath(AndroidLocators.PersonalInfoPage.xpathProfileOptionsButton));
	}

	public ContactListPage pressCloseButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				selfProfileClose);
		selfProfileClose.click();
		return new ContactListPage(getLazyDriver());
	}

}
