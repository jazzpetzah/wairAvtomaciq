package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;
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

	@FindBy(id = AndroidLocators.RegistrationPage.idRegistrationBack)
	private WebElement backButton;

	@FindBy(id = AndroidLocators.DialogPage.idConfirmButton)
	private WebElement confirmImageButton;

	@FindBy(id = AndroidLocators.RegistrationPage.idSignUpGalleryIcon)
	protected WebElement signUpGalleryIcon;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathNameEdit)
	protected WebElement nameField;

	@FindBy(xpath = AndroidLocators.PersonalInfoPage.xpathEmailField)
	private WebElement emailField;

	@FindBy(id = AndroidLocators.RegistrationPage.idNewPasswordField)
	private WebElement passwordField;

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

	@FindBy(xpath = AndroidLocators.CommonLocators.xpathImagesFrameLayout)
	private List<WebElement> frameLayouts;

	@FindBy(xpath = AndroidLocators.CommonLocators.xpathImage)
	private List<WebElement> image;

	private static final String YOUR_NAME = "your full name";
	private static final String EMAIL = "email";

	public RegistrationPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void clickCameraButton() throws Exception {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(cameraButton));
		cameraButton.click();
	}

	public void selectPicture() {
		signUpGalleryIcon.click();
	}

	public void chooseFirstPhoto() throws Exception {
		try {
			frameLayouts.get(0).click();
			return;
		} catch (Exception ex) {
		}
		try {
			image.get(0).click();
			return;
		} catch (Exception ex) {
		}
	}

	public boolean isPictureSelected() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AndroidLocators.DialogPage.idConfirmButton));
	}

	public void confirmPicture() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				confirmImageButton);
		confirmImageButton.click();
	}

	public void setName(String name) throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				nameField);
		// TABLET fix
		if (nameField.getText().toLowerCase().contains(YOUR_NAME)) {
			nameField.sendKeys(name);
			this.getWait().until(
					ExpectedConditions.elementToBeClickable(nextArrow));
			nextArrow.click();
		}
	}

	public void setEmail(String email) throws Exception {
		// TABLET fix
		if (nameField.getText().toLowerCase().contains(EMAIL)) {
			nameField.sendKeys(email);
			this.getWait().until(
					ExpectedConditions.elementToBeClickable(nextArrow));
			nextArrow.click();
		}
	}

	public void setPassword(String password) throws Exception {
		passwordField.sendKeys(password);
	}

	public void createAccount() {
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

	public void pressBackButton() throws Exception {
		backButton.click();
	}

}
