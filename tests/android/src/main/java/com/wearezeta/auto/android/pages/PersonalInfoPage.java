package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class PersonalInfoPage extends AndroidPage {
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idBackgroundOverlay")
	private WebElement backgroundOverlay;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idSettingsBox")
	private WebElement settingBox;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idEmailField")
	private WebElement emailField;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idNameField")
	private WebElement nameField;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idSettingsBtn")
	private WebElement settingsButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idNameEdit")
	private WebElement nameEdit;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idChangePhotoBtn")
	private WebElement changePhotoBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idGalleryBtn")
	private WebElement galleryBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConfirmButton")
	private WebElement confirmBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idProfileOptionsButton")
	private WebElement optionsButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idAboutButton")
	private WebElement aboutButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idSelfProfileClose")
	private WebElement selfProfileClose;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement page;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idSignOutBtn")
	private WebElement signOutBtn;

	@FindBy(how = How.ID, using = AndroidLocators.PersonalInfoPage.idOpenFrom)
	private List<WebElement> openFrom;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idProfileOptionsButton")
	private List<WebElement> settingsButtonList;

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

	public void tapChangePhotoButton() throws Throwable {
		changePhotoBtn.click();
		Thread.sleep(1000); // fix for animation
	}

	public void tapGalleryButton() throws Throwable {
		galleryBtn.click();
		Thread.sleep(3000); // fix for animation
	}

	public void tapConfirmButton() throws IOException, Throwable {
		Thread.sleep(1000); // fix for animation
		try {
			this.getDriver().hideKeyboard();
		} catch (Exception ex) {
			//ignore silently
		}
		confirmBtn.click();
		Thread.sleep(1000); // fix for animation
	}

	public void tapSignOutBtn() throws Exception {
		signOutBtn.click();
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {

		AndroidPage page = null;
		switch (direction) {
		case DOWN: {
			break;
		}
		case UP: {
			page = this;
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

	public void tapOptionsButton() throws Exception {
		optionsButton.click();
		Thread.sleep(1000); // fix for animation
	}

	public SettingsPage tapSettingsButton() throws Exception {
		settingsButton.click();
		Thread.sleep(1000); // fix for animation
		return new SettingsPage(this.getLazyDriver());
	}

	public void waitForConfirmBtn() throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(confirmBtn));
	}

	public void tapOnMyName() throws Exception {
		this.getWait().until(ExpectedConditions.visibilityOf(nameField));
		nameField.click();
		Thread.sleep(2000); // fix for animation
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				AndroidLocators.PersonalInfoPage.getByForNameEditField());
		if (!DriverUtils.isElementPresentAndDisplayed(nameEdit)) {
			DriverUtils.mobileTapByCoordinates(getDriver(), nameField);
		}
	}

	public void changeName(String name, String newName) throws Exception {
		DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.id(AndroidLocators.PersonalInfoPage.idNameField));
		this.getWait().until(ExpectedConditions.visibilityOf(nameEdit));

		try {
			nameEdit.clear();
		} catch (Exception ex) {
			// ignore silently
		}
		nameEdit.sendKeys(newName);
		this.getDriver().navigate().back();
		Thread.sleep(1000);
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

		return settingBox.isDisplayed();
	}

	public boolean isSettingsButtonNotVisible() throws Exception {
		DriverUtils
				.waitUntilLocatorDissapears(this.getDriver(),
						AndroidLocators.PersonalInfoPage
								.getByForProfileOptionsButton());
		return !DriverUtils.isElementPresentAndDisplayed(settingsButton);
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
