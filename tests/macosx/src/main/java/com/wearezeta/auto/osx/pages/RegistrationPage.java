package com.wearezeta.auto.osx.pages;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Future;

import javax.mail.Message;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.common.OSXConstants;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class RegistrationPage extends OSXPage {

	private static final Logger log = ZetaLogger.getLog(RegistrationPage.class
			.getSimpleName());

	@FindBy(how = How.CLASS_NAME, using = OSXLocators.classNameLoginField)
	private WebElement nameField;

	// @FindBy(how = How.ID, using = OSXLocators.idRegistrationEmailField)
	private WebElement emailField;

	@FindBy(how = How.ID, using = OSXLocators.idPasswordField)
	private WebElement passwordField;

	@FindBy(how = How.ID, using = OSXLocators.idSubmitRegistrationButton)
	private WebElement submitRegistrationButton;

	@FindBy(how = How.ID, using = OSXLocators.idConfirmationRequestedText)
	private WebElement confirmationRequestedField;

	@FindBy(how = How.ID, using = OSXLocators.idRegistrationTakePictureButton)
	private WebElement takePictureButton;

	@FindBy(how = How.ID, using = OSXLocators.idRegistrationPickImageButton)
	private WebElement pickImageButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathRegistrationPictureConfirmationButton)
	private WebElement confirmChosenPictureButton;

	private Future<Message> activationMessage;

	private String activationResponse = null;

	public RegistrationPage(ZetaOSXDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void enterName(String name) {
		nameField.sendKeys(name);
	}

	public WebElement findEmailField() {
		ArrayList<WebElement> candidates = new ArrayList<WebElement>(
				driver.findElements(By
						.className(OSXLocators.classNameLoginField)));
		return candidates.get(1);
	}

	public void enterEmail(String email) {
		emailField = findEmailField();
		emailField.sendKeys(email);
	}

	public String getEnteredEmail() {
		emailField = findEmailField();
		return emailField.getText();
	}

	public void enterPassword(String password) {
		passwordField.click();
		passwordField.sendKeys(password);
	}

	public void submitRegistration() {
		submitRegistrationButton.click();
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

	public boolean isConfirmationRequested() throws Exception {
		return DriverUtils.waitUntilElementAppears(driver,
				By.id(OSXLocators.idConfirmationRequestedText), 60);
	}

	public boolean isInvalidEmailMessageAppear() {
		try {
			return driver.findElement(By
					.xpath(OSXLocators.xpathPleaseProvideEmailAddress)) != null;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void activateUserFromBrowser() throws Exception {
		String activationLink = BackendAPIWrappers
				.getUserActivationLink(this.activationMessage);
		String script = String
				.format(OSXCommonUtils
						.readTextFileFromResources(OSXConstants.Scripts.ACTIVATE_USER_SCRIPT),
						activationLink);

		@SuppressWarnings("unchecked")
		Map<String, String> value = (Map<String, String>) driver
				.executeScript(script);
		activationResponse = value.get("result");
		log.debug(activationResponse);
	}

	public boolean isUserActivated() {
		return activationResponse
				.contains(OSXLocators.RegistrationPage.ACTIVATION_RESPONSE_VERIFIED);
	}

	public void setActivationMessage(Future<Message> activationMessage) {
		this.activationMessage = activationMessage;
	}

	public Future<Message> getActivationMessage() {
		return activationMessage;
	}
}
