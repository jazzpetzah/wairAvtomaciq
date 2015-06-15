package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class RegistrationPage extends AndroidPage {

	private static final String xpathParentSignUpContainer = "//*[@id='fl__sign_up__main_container']";

	private static final String idSignUpGalleryIcon = "gtv__sign_up__gallery_icon";
	@FindBy(id = idSignUpGalleryIcon)
	protected WebElement signUpGalleryIcon;

	private static final String xpathNameField = "("
			+ xpathParentSignUpContainer
			+ "//*[@id='tet__profile__guided'])[1]";
	@FindBy(xpath = xpathNameField)
	protected WebElement nameField;

	private static final String idCreateUserBtn = "zb__sign_up__create_account";
	@FindBy(id = idCreateUserBtn)
	private WebElement createUserBtn;

	private static final String idVerifyEmailBtn = "ttv__sign_up__resend";
	@FindBy(id = idVerifyEmailBtn)
	private WebElement verifyEmailBtn;

	private static final String idNextArrow = "gtv__sign_up__next";
	@FindBy(id = idNextArrow)
	protected WebElement nextArrow;

	@FindBy(id = ContactListPage.idConfirmCancelButton)
	private WebElement laterBtn;

	@FindBy(id = PeoplePickerPage.idPickerSearch)
	private WebElement pickerSearch;

	public RegistrationPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void selectPicture() {
		signUpGalleryIcon.click();
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
				By.id(idVerifyEmailBtn));
	}

	public PeoplePickerPage continueRegistration() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(PeoplePickerPage.idPickerSearch));
		return new PeoplePickerPage(this.getLazyDriver());
	}

}
