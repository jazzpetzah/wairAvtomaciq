package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement page;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idSignOutBtn")
	private WebElement signOutBtn;

	@FindBy(how = How.ID, using = AndroidLocators.PersonalInfoPage.idOpenFrom)
	private List<WebElement> openFrom;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idProfileOptionsButton")
	private List<WebElement> settingsButtonList;

	public PersonalInfoPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public boolean isPersonalInfoVisible() throws Exception {
		refreshUITree();
		return isVisible(emailField);
	}

	public void waitForEmailFieldVisible() {
		this.getWait().until(ExpectedConditions.visibilityOf(emailField));
	}

	public void clickOnPage() throws InterruptedException {
		DriverUtils.androidMultiTap(this.getDriver(), page, 1, 0.2);
	}

	public void tapChangePhotoButton() throws Throwable {
		changePhotoBtn.click();
		Thread.sleep(1000); //fix for animation
	}

	public void tapGalleryButton() throws Throwable {
		galleryBtn.click();
		Thread.sleep(1000); //fix for animation
	}

	public void tapConfirmButton() throws IOException, Throwable {
		confirmBtn.click();
		Thread.sleep(1000); //fix for animation
	}

	public void tapSignOutBtn() {

		refreshUITree();
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
			page = new ContactListPage(this.getDriver(), this.getWait());
			break;
		}
		}
		return page;
	}

	public void tapOptionsButton() throws InterruptedException {
		refreshUITree();
		optionsButton.click();
		Thread.sleep(1000); //fix for animation
	}

	public SettingsPage tapSettingsButton() throws Exception {
		refreshUITree();
		settingsButton.click();
		Thread.sleep(1000); //fix for animation
		return new SettingsPage(this.getDriver(), this.getWait());
	}

	public void waitForConfirmBtn() {
		this.getWait().until(ExpectedConditions.visibilityOf(confirmBtn));
	}

	public void tapOnMyName() throws Exception {
		refreshUITree();
		this.getWait().until(ExpectedConditions.visibilityOf(nameField));
		nameField.click();
		Thread.sleep(2000); //fix for animation
		refreshUITree();
		DriverUtils.waitUntilElementAppears(driver,
				AndroidLocators.PersonalInfoPage.getByForNameEditField());
		if(!isVisible(nameEdit)) {
			DriverUtils.mobileTapByCoordinates(getDriver(), nameField);
		}
	}

	public void changeName(String name, String newName) throws Exception {
		refreshUITree();
		DriverUtils.waitUntilElementDissapear(driver,
				By.id(AndroidLocators.PersonalInfoPage.idNameField));
		refreshUITree();
		this.getWait().until(ExpectedConditions.visibilityOf(nameEdit));

		try {
			nameEdit.clear();
		} catch (Exception ex) {
			//ignore silently
		}
		
		//FIX if nameEdit.clear() failed to clear text
		int stringLength = nameEdit.getText().length();
		if (stringLength > 0) {
			for (int i = 0; i < stringLength; i++) {
				this.getDriver().sendKeyEvent(22); // "KEYCODE_DPAD_RIGHT"
			}

			for (int i = 0; i < stringLength; i++) {
				this.getDriver().sendKeyEvent(67); // "KEYCODE_DEL"
			}
		}
		
		nameEdit.sendKeys(newName);
		driver.navigate().back();
		Thread.sleep(1000);
	}

	@Override
	public ContactListPage navigateBack() throws Exception {
		driver.navigate().back();
		return new ContactListPage(this.getDriver(), this.getWait());
	}

	public String getUserName() {
		refreshUITree();
		return nameField.getText();
	}

	public AboutPage tapAboutButton() throws Exception {
		refreshUITree();
		aboutButton.click();
		return new AboutPage(this.getDriver(), this.getWait());
	}

	public boolean isSettingsVisible() {

		return settingBox.isDisplayed();
	}

	public boolean isSettingsButtonNotVisible() throws Exception {
		boolean flag = false;
		refreshUITree();
		DriverUtils
				.waitUntilElementDissapear(driver,
						AndroidLocators.PersonalInfoPage
								.getByForProfileOptionsButton());
		if (!isVisible(settingsButton)) {
			flag = true;
		}
		return flag;
	}

	public boolean waitForSettingsDissapear() throws Exception {
		return DriverUtils.waitUntilElementDissapear(driver,
				By.id(AndroidLocators.PersonalInfoPage.idProfileOptionsButton));
	}

}
