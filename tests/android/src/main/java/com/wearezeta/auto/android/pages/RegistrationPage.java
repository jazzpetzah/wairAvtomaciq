package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class RegistrationPage extends AndroidPage {

	@FindBy(id = AndroidLocators.DialogPage.idDialogTakePhotoButton)
	private WebElement cameraButton;

	@FindBy(xpath = AndroidLocators.DialogPage.xpathConfirmOKButton)
	private WebElement confirmImageButton;

	@FindBy(id = AndroidLocators.RegistrationPage.idSignUpGalleryIcon)
	protected WebElement signUpGalleryIcon;

	@FindBy(xpath = AndroidLocators.RegistrationPage.xpathNameField)
	protected WebElement nameField;

	@FindBy(id = AndroidLocators.RegistrationPage.idCreateUserBtn)
	private WebElement createUserBtn;

	@FindBy(id = AndroidLocators.RegistrationPage.idVerifyEmailBtn)
	private WebElement verifyEmailBtn;

	@FindBy(id = AndroidLocators.RegistrationPage.idNextArrow)
	protected WebElement nextArrow;

	@FindBy(id = AndroidLocators.ContactListPage.idConfirmCancelButton)
	private WebElement laterBtn;

	@FindBy(id = AndroidLocators.PeoplePickerPage.idPickerSearch)
	private WebElement pickerSearch;

	public RegistrationPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void clickCameraButton() throws Exception {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(cameraButton));
		cameraButton.click();
	}

	public void selectPicture() {
		signUpGalleryIcon.click();
	}

	public boolean isPictureSelected() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AndroidLocators.DialogPage.xpathConfirmOKButton));
	}

	public void confirmPicture() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				confirmImageButton);
		confirmImageButton.click();
	}

	public void setName(String name) throws Exception {
		assert DriverUtils.isElementPresentAndDisplayed(nameField);
		nameField.sendKeys(name);
		this.getWait()
				.until(ExpectedConditions.elementToBeClickable(nextArrow));
		nextArrow.click();
	}
	
	public void createAccount() throws Exception {
		assert DriverUtils
				.waitUntilElementClickable(getDriver(), createUserBtn);
		createUserBtn.click();
	}

	public boolean isConfirmationVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.RegistrationPage.idVerifyEmailBtn));
	}

	public PeoplePickerPage continueRegistration() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(AndroidLocators.PeoplePickerPage.idPickerSearch));
		return new PeoplePickerPage(this.getLazyDriver());
	}

}
