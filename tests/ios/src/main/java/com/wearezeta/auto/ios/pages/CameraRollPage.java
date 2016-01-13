package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class CameraRollPage extends IOSPage {

	@FindBy(how = How.NAME, using = IOSLocators.nameCameraLibraryButton)
	private WebElement cameraLibraryButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameCameraRollCancel)
	private WebElement cameraRollCancel;

	@FindBy(how = How.XPATH, using = IOSLocators.CameraRollPage.xpathCameraLibraryFirstFolder)
	private WebElement cameraLibraryFirstFolder;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathLibraryFirstPicture)
	private WebElement libraryFirstPicture;

	@FindBy(how = How.XPATH, using = IOSLocators.CameraRollPage.xpathLibraryLastPicture)
	private WebElement libraryLastPicture;

	@FindBy(how = How.XPATH, using = IOSLocators.CameraRollPage.xpathConfirmPictureButton)
	private WebElement confirmPictureButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameCameraRollSketchButton)
	private WebElement cameraRollSketchButton;

	public CameraRollPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void pressSelectFromLibraryButton() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameCameraLibraryButton));
		cameraLibraryButton.click();
	}

	public void selectImageFromLibrary() throws Exception {
		try {
			clickFirstLibraryFolder();
		} catch (NoSuchElementException ex) {
			// Ignore silently
		}

		clickFirstImage();
	}

	public void clickFirstLibraryFolder() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(),
				cameraLibraryFirstFolder);
		cameraLibraryFirstFolder.click();
	}

	public void clickFirstImage() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathLibraryFirstPicture));
		libraryFirstPicture.click();
	}

	public void clickLastImage() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.CameraRollPage.xpathLibraryLastPicture));
		libraryLastPicture.click();
	}

	public void pressConfirmButton() throws Exception {
		DriverUtils
				.waitUntilElementClickable(getDriver(), confirmPictureButton);
		confirmPictureButton.click();
	}

	public void clickCameraRollSketchButton() {
		cameraRollSketchButton.click();
	}

}
