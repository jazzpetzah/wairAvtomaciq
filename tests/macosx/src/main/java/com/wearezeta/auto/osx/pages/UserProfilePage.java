package com.wearezeta.auto.osx.pages;

import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.osx.locators.OSXLocators;

public class UserProfilePage extends OSXPage {
	
	@FindBy(how = How.ID, using = OSXLocators.idMainWindow)
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
	
	public UserProfilePage(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}

	public void openPictureSettings() {
		selfProfileViewSettingsButton.sendKeys("");
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

	public void confirmPictureChoice() {
		confirmPictureChoiceButton.click();
		pictureSettingsCloseButton.click();
		DriverUtils.waitUntilElementDissapear(driver, By.xpath(OSXLocators.xpathPictureFromImageFile));
	}
}
