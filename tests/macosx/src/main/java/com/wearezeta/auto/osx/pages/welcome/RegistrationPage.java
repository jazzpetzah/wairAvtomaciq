package com.wearezeta.auto.osx.pages.welcome;

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
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
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

	@FindBy(how = How.ID, using = OSXLocators.idRegistrationTakePictureButton)
	private WebElement takePictureButton;

	@FindBy(how = How.ID, using = OSXLocators.idRegistrationPickImageButton)
	private WebElement pickImageButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathRegistrationPictureConfirmationButton)
	private WebElement confirmChosenPictureButton;

	public RegistrationPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
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
				this.getDriver(), this.getWait(), email);

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(5,
				TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS);

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

	public void acceptTakenPicture() throws InterruptedException {
		Thread.sleep(1000);
		confirmChosenPictureButton.click();
	}

	public boolean isInvalidEmailMessageAppear() {
		passwordField.click();
		emailField.click();
		try {
			return driver.findElement(By
					.xpath(OSXLocators.xpathPleaseProvideEmailAddress)) != null;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

}
