package com.wearezeta.auto.osx.pages;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class RegistrationPage extends OSXPage {
	
	@FindBy(how = How.CLASS_NAME, using = OSXLocators.classNameLoginField)
	private WebElement nameField;

//	@FindBy(how = How.ID, using = OSXLocators.idRegistrationEmailField)
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
	
	public RegistrationPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}

	public void enterName(String name) {
		nameField.sendKeys(name);
	}
	
	public WebElement findEmailField() {
		ArrayList<WebElement> candidates = new ArrayList<WebElement>(driver.findElements(By.className(OSXLocators.classNameLoginField)));
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
	
	public boolean isConfirmationRequested() {
		return DriverUtils.waitUntilElementAppears(driver, By.id(OSXLocators.idConfirmationRequestedText), 60);
	}
	
	public boolean isInvalidEmailMessageAppear() {
		try {
			return driver.findElement(By.xpath(OSXLocators.xpathPleaseProvideEmailAddress)) != null;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
