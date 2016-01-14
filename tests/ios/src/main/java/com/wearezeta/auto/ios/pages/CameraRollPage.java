package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class CameraRollPage extends IOSPage {

    private static final String nameCameraLibraryButton = "cameraLibraryButton";
	@FindBy(name = nameCameraLibraryButton)
	private WebElement cameraLibraryButton;

    private static final String nameCameraRollCancel = "Cancel";
    @FindBy(name = nameCameraRollCancel)
	private WebElement cameraRollCancel;

    private static final String xpathCameraLibraryFirstFolder = "//UIATableCell[@name='Moments']";
    @FindBy(xpath = xpathCameraLibraryFirstFolder)
	private WebElement cameraLibraryFirstFolder;

    private static final String xpathLibraryFirstPicture =
			xpathMainWindow + "/UIACollectionView/UIACollectionCell[1]";
    @FindBy(xpath = xpathLibraryFirstPicture)
	private WebElement libraryFirstPicture;

    private static final String xpathLibraryLastPicture = "//UIACollectionView/UIACollectionCell[last()]";
    @FindBy(xpath = xpathLibraryLastPicture)
	private WebElement libraryLastPicture;

    private static final String xpathConfirmPictureButton = "//UIAButton[@name='OK' and @visible='true']";
    @FindBy(xpath = xpathConfirmPictureButton)
	private WebElement confirmPictureButton;

    private static final String nameCameraRollSketchButton = "editNotConfirmedImageButton";
    @FindBy(name = nameCameraRollSketchButton)
	private WebElement cameraRollSketchButton;

	public CameraRollPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void pressSelectFromLibraryButton() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(nameCameraLibraryButton));
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
				By.xpath(xpathLibraryFirstPicture));
		libraryFirstPicture.click();
	}

	public void clickLastImage() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(xpathLibraryLastPicture));
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
