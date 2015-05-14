package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Future;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class RegistrationPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idDialogTakePhotoButton")
	private WebElement cameraButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.RegistrationPage.CLASS_NAME, locatorKey = "idRegistrationBack")
	private WebElement backButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.DialogPage.CLASS_NAME, locatorKey = "idConfirmButton")
	private WebElement confirmImageButton;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.RegistrationPage.CLASS_NAME, locatorKey = "idSignUpGalleryIcon")
	protected WebElement signUpGalleryIcon;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idNameEdit")
	protected WebElement nameField;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PersonalInfoPage.CLASS_NAME, locatorKey = "idEmailField")
	private WebElement emailField;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.RegistrationPage.CLASS_NAME, locatorKey = "idNewPasswordField")
	private WebElement passwordField;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.RegistrationPage.CLASS_NAME, locatorKey = "idCreateUserBtn")
	private WebElement createUserBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.RegistrationPage.CLASS_NAME, locatorKey = "idVerifyEmailBtn")
	private WebElement verifyEmailBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.RegistrationPage.CLASS_NAME, locatorKey = "idNextArrow")
	protected WebElement nextArrow;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idConfirmCancelButton")
	private WebElement laterBtn;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.PeoplePickerPage.CLASS_NAME, locatorKey = "idPickerSearch")
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

	public void takePhoto() throws Exception {
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
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				AndroidLocators.DialogPage.getByForDialogConfirmImageButtn());
		return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				AndroidLocators.DialogPage.getByForDialogConfirmImageButtn());
	}

	public void confirmPicture() throws Exception {
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(confirmImageButton));
		confirmImageButton.click();
	}

	public void setName(String name) throws Exception {
		this.getWait()
				.until(ExpectedConditions.elementToBeClickable(nameField));
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
		this.getWait().until(ExpectedConditions.visibilityOf(verifyEmailBtn));
		return DriverUtils.isElementPresentAndDisplayed(verifyEmailBtn);
	}

	public PeoplePickerPage continueRegistration() throws Exception {
		try {
			this.getWait().until(ExpectedConditions.visibilityOf(pickerSearch));
		} catch (NoSuchElementException e) {

		} catch (TimeoutException e) {

		}
		return new PeoplePickerPage(this.getLazyDriver());
	}

	public void pressBackButton() throws Exception {
		backButton.click();
	}

}
