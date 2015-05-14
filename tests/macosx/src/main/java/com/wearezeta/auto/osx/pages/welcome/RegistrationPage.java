package com.wearezeta.auto.osx.pages.welcome;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.pages.OSXPage;

public class RegistrationPage extends OSXPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(RegistrationPage.class
			.getSimpleName());

	@FindBy(how = How.CSS, using = OSXLocators.RegistrationPage.relativePathFullNameField)
	private WebElement fullNameField;

	@FindBy(how = How.CSS, using = OSXLocators.RegistrationPage.relativePathEmailField)
	private WebElement emailField;

	@FindBy(how = How.ID, using = OSXLocators.RegistrationPage.idPasswordField)
	private WebElement passwordField;

	@FindBy(how = How.ID, using = OSXLocators.RegistrationPage.idCreateAccountButton)
	private WebElement createAccountButton;

	@FindBy(how = How.ID, using = OSXLocators.RegistrationPage.idTakePictureButton)
	private WebElement takePictureButton;

	@FindBy(how = How.ID, using = OSXLocators.RegistrationPage.idPickImageButton)
	private WebElement pickImageButton;

	@FindBy(how = How.XPATH, using = OSXLocators.RegistrationPage.xpathAcceptTakenPictureButton)
	private WebElement acceptTakenPictureButton;

	@FindBy(how = How.XPATH, using = OSXLocators.RegistrationPage.xpathRejectTakenPictureButton)
	private WebElement rejectTakenPictureButton;

	@FindBy(how = How.ID, using = OSXLocators.RegistrationPage.idRegistrationBackButton)
	private WebElement backButton;

	public RegistrationPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void typeFullName(String name) {
		fullNameField.sendKeys(name);
	}

	public void typeEmail(String email) {
		emailField.sendKeys(email);
	}

	public String getEnteredEmail() {
		return emailField.getText();
	}

	public void typePassword(String password) {
		passwordField.click();
		passwordField.sendKeys(password);
	}

	public VerificationPage createAccount(String email) throws Exception {
		VerificationPage verificationPage = new VerificationPage(
				this.getLazyDriver(), email);

		Wait<WebDriver> wait = new FluentWait<WebDriver>(this.getDriver())
				.withTimeout(5, TimeUnit.SECONDS).pollingEvery(1,
						TimeUnit.SECONDS);

		try {
			wait.until(new Function<WebDriver, Boolean>() {
				public Boolean apply(WebDriver driver) {
					return createAccountButton.isEnabled();
				}
			});
		} catch (TimeoutException e) {
			throw new Exception("Create account button is disabled. "
					+ "Please check registration data.");
		}

		createAccountButton.click();
		return verificationPage;
	}

	public void chooseToTakePicture() {
		takePictureButton.click();
	}

	public void chooseToPickImage() {
		pickImageButton.click();
	}

	public void acceptTakenPicture() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		acceptTakenPictureButton.click();
	}

	public void rejectTakenPicture() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		rejectTakenPictureButton.click();
	}

	public boolean isInvalidEmailMessageAppear() throws Exception {
		passwordField.click();
		emailField.click();
		try {
			return this
					.getDriver()
					.findElement(
							By.xpath(OSXLocators.RegistrationPage.xpathPleaseProvideEmailAddressMessage)) != null;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void goBack() {
		backButton.click();
	}

	public boolean isChoosePictureMessageVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(OSXLocators.RegistrationPage.xpathChoosePictureAndSelectColourMessage));
	}

	public boolean isTakePictureConfirmationScreen() throws Exception {
		final int CONFIRMATION_SCREEN_BUTTONS_COUNT = 2;
		List<WebElement> buttons = getDriver()
				.findElements(
						By.xpath(OSXLocators.RegistrationPage.xpathConfirmPictureButton));
		return buttons.size() == CONFIRMATION_SCREEN_BUTTONS_COUNT;
	}
}
