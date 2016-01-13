package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class CameraRollTabletPopoverPage extends CameraRollPage{

	public static final String nameCameraLibraryButton = "cameraLibraryButton";
	@FindBy(name = nameCameraLibraryButton)
	private WebElement cameraLibraryButton;

    public static final String xpathIPADCameraLibraryFirstFolder =
            "//UIAApplication/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIATableView[1]/UIATableCell[@name='Moments']";
    @FindBy(xpath = xpathIPADCameraLibraryFirstFolder)
	private WebElement cameraLibraryFirstFolderiPadPopover;

    public static final String xpathIPADCameraLibraryFirtImage =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIACollectionView[1]/UIACollectionCell[1]";
    @FindBy(xpath = xpathIPADCameraLibraryFirtImage)
	private WebElement ipadPopoverLibraryFirstPicture;

    public static final String xpathIPADCameraLibraryConfirmButton =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAButton[@name='OK']";
    @FindBy(xpath = xpathIPADCameraLibraryConfirmButton)
	private WebElement confirmPictureButton;
	
	public CameraRollTabletPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
		

	public void pressSelectFromLibraryButtoniPad() throws Exception {
		cameraLibraryButton.click();
	}

	public void selectImageFromLibraryiPad() throws Exception {
		try {
			clickFirstLibraryFolderiPad();
		} catch (NoSuchElementException ex) {

		}

		clickFirstImage();
	}

	public void clickFirstLibraryFolderiPad() throws Exception {
		cameraLibraryFirstFolderiPadPopover.click();
	}

	public void clickFirstImage() throws Exception {
		ipadPopoverLibraryFirstPicture.click();
	}

	public void pressConfirmButtoniPad() throws Exception {
		confirmPictureButton.click();
	}
}
