package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class CameraRollPage extends IOSPage {

    private static final By nameCameraLibraryButton = By.name("cameraLibraryButton");

    private static final By xpathCameraLibraryFirstFolder = By.xpath("//UIATableCell[@name='Moments']");

    private static final By xpathLibraryFirstPicture = By.xpath(xpathStrMainWindow +
			"/UIACollectionView/UIACollectionCell[1]");

    private static final By xpathLibraryLastPicture = By.xpath("//UIACollectionView/UIACollectionCell[last()]");

    private static final By xpathConfirmPictureButton = By.xpath("//UIAButton[@name='OK' and @visible='true']");

    private static final By nameCameraRollSketchButton = By.name("editNotConfirmedImageButton");

	public CameraRollPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void pressSelectFromLibraryButton() throws Exception {
		getElement(nameCameraLibraryButton).click();
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
		getElement(xpathCameraLibraryFirstFolder).click();
	}

	public void clickFirstImage() throws Exception {
		getElement(xpathLibraryFirstPicture).click();
	}

	public void clickLastImage() throws Exception {
		getElement(xpathLibraryLastPicture).click();
	}

	public void pressConfirmButton() throws Exception {
		getElement(xpathConfirmPictureButton).click();
	}

	public void clickCameraRollSketchButton() throws Exception {
		getElement(nameCameraRollSketchButton).click();
	}

}
