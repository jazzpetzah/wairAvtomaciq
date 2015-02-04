package com.wearezeta.auto.osx.pages;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class UserProfilePage extends OSXPage {
	private static final Logger log = ZetaLogger.getLog(UserProfilePage.class.getSimpleName());
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathMainWindow)
	private WebElement mainWindow;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathOpenUserPictureSettingsButton)
	private WebElement userPictureButton;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureFromImageFile)
	private WebElement choosePictureFromImageFileButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureFromCamera)
	private WebElement choosePictureFromCameraButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathDoCameraShotButton)
	private WebElement doCameraShotButton;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureConfirmationButton)
	private WebElement confirmPictureChoiceButton;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureSettingsCloseButton)
	private WebElement pictureSettingsCloseButton;

	@FindBy(how = How.ID, using = OSXLocators.idSelfProfileSettingsButton)
	private WebElement selfProfileViewSettingsButton;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathRemoveUserPictureCheckBox)
	private WebElement removeUserPictureCheckBox;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathRemoveUserPictureConfirmation)
	private WebElement removeUserPictureConfirmationButton;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathRemoveUserPictureCancel)
	private WebElement removeUserPictureCancelButton;
	
	@FindBy(how = How.ID, using = OSXLocators.idSelfProfileEmailTextField)
	private WebElement selfProfileEmailTextField;
	
	public UserProfilePage(String URL, String path) throws IOException {
		super(URL, path, false);
	}

	public void openPictureSettings() {
		Actions builder = new Actions(driver);
		builder.moveToElement(mainWindow).build().perform();
		userPictureButton.click();
		DriverUtils.waitUntilElementAppears(driver, By.xpath(OSXLocators.xpathPictureFromImageFile));
	}
	
	public void openChooseImageFileDialog() {
		choosePictureFromImageFileButton.click();
	}
	
	public void openCameraDialog() {
		choosePictureFromCameraButton.click();
	}
	
	public void doPhotoInCamera() {
		doCameraShotButton.click();
	}

	public void confirmPictureChoice() throws InterruptedException {
		Thread.sleep(3000);
		confirmPictureChoiceButton.click();
		try {
			pictureSettingsCloseButton.click();
		} catch (NoSuchElementException e) { }
		DriverUtils.waitUntilElementDissapear(driver, By.xpath(OSXLocators.xpathPictureFromImageFile));
	}

	public void chooseToRemovePhoto() {
		removeUserPictureCheckBox.click();
	}

	public void confirmPhotoRemoving() {
		removeUserPictureConfirmationButton.click();
		try {
			pictureSettingsCloseButton.click();
		} catch (NoSuchElementException e) { }
		DriverUtils.waitUntilElementDissapear(driver, By.xpath(OSXLocators.xpathPictureFromImageFile));
	}

	public void cancelPhotoRemoving() {
		removeUserPictureCancelButton.click();
	}
	
	public boolean selfProfileEmailEquals(String email) {
		return email.equals(selfProfileEmailTextField.getText());
	}
	
	public boolean selfProfileNameEquals(String name) {
		String xpath = String.format(OSXLocators.xpathFormatSelfProfileNameTextField, name);
		log.debug("Looking for name " + name + " by xpath '" + xpath + "' in user profile. Page source: " + driver.getPageSource());
		return DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath), 60);
	}
}
