package com.wearezeta.auto.osx.pages;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.osx.locators.OSXLocators;

public class UserProfilePage extends OSXPage {
	
	@FindBy(how = How.ID, using = OSXLocators.idMainWindow)
	private WebElement mainWindow;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureFromImageFile)
	private WebElement choosePictureFromImageFileButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureFromCamera)
	private WebElement choosePictureFromCameraButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathDoCameraShotButton)
	private WebElement doCameraShotButton;
	
	@FindBy(how = How.XPATH, using = OSXLocators.xpathPictureConfirmationButton)
	private WebElement confirmPictureChoiceButton;
	
	@FindBy(how = How.NAME, using = OSXLocators.nameQuitZClientMenuItem)
	private WebElement quitZClientMenuItem;
	
	public UserProfilePage(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}

	public void openPictureSettings() {
		mainWindow.click();
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
	}
	
	@Override
	public void Close() throws IOException {
		try {
			quitZClientMenuItem.click();
		} catch (Exception e) { }
		super.Close();
	}
}
