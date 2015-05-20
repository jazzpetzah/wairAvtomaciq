package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import android.view.KeyEvent;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

public class PersonalInfoPage extends AndroidPage {

	private static final Logger log = ZetaLogger.getLog(PeoplePickerPage.class
			.getSimpleName());

	@FindBy(id = AndroidLocators.PersonalInfoPage.idBackgroundOverlay)
	private WebElement backgroundOverlay;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idSettingsBox)
	private WebElement settingBox;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idEmailField)
	private WebElement emailField;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idNameField)
	private WebElement nameField;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idSettingsBtn)
	private WebElement settingsButton;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idNameEdit)
	private WebElement nameEdit;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idChangePhotoBtn)
	private WebElement changePhotoBtn;

	@FindBy(id = AndroidLocators.CommonLocators.idGalleryBtn)
	private WebElement galleryBtn;

	@FindBy(id = AndroidLocators.DialogPage.idConfirmButton)
	private WebElement confirmBtn;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idProfileOptionsButton)
	private WebElement optionsButton;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idAboutButton)
	private WebElement aboutButton;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idSelfProfileClose)
	private WebElement selfProfileClose;

	@FindBy(id = AndroidLocators.CommonLocators.idPager)
	private WebElement page;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idSignOutBtn)
	private WebElement signOutBtn;

	@FindBy(how = How.ID, using = AndroidLocators.PersonalInfoPage.idOpenFrom)
	private List<WebElement> openFrom;

	@FindBy(id = AndroidLocators.PersonalInfoPage.idProfileOptionsButton)
	private List<WebElement> settingsButtonList;

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
		changePhotoBtn.click();
	}

	public void tapGalleryButton() throws Exception {
		galleryBtn.click();
	}

	public void tapConfirmButton() throws Exception {
		this.hideKeyboard();
		confirmBtn.click();
	}

	public void tapSignOutBtn() throws Exception {
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
		optionsButton.click();
	}

	public SettingsPage tapSettingsButton() throws Exception {
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
				By.id(AndroidLocators.PersonalInfoPage.idNameEdit))) {
			DriverUtils.mobileTapByCoordinates(getDriver(), nameField);
		}
	}

	public boolean isNameEditVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.id(AndroidLocators.PersonalInfoPage.idNameEdit));
	}

	public boolean isNameEditCanBeCleaned() throws Exception {
		nameEdit.clear();
		if (!nameEdit.getText().equals(EMPTY_NAME)) {
			log.debug("Text in name field is not as expected, trying to clean by KEYCODE commands");
			int stringLength = nameEdit.getText().length();
			for (int i = 0; i < stringLength; i++) {
				this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
				this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_DEL);
			}
		} else {
			return true;
		}
		if (!nameEdit.getText().equals(EMPTY_NAME)) {
			return false;
		} else {
			return true;
		}
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
		aboutButton.click();
		return new AboutPage(this.getLazyDriver());
	}

	public boolean isSettingsVisible() {
		return DriverUtils.isElementPresentAndDisplayed(settingBox);
	}

	public boolean waitForOptionsMenuToDisappear() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.PersonalInfoPage.idAboutButton), 10);
	}

	public boolean waitForSettingsDissapear() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.PersonalInfoPage.idProfileOptionsButton));
	}

	public ContactListPage pressCloseButton() throws Exception {
		selfProfileClose.click();
		return new ContactListPage(getLazyDriver());
	}

}
