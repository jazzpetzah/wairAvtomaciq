package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class CameraRollTabletPopoverPage extends CameraRollPage{

	private static final By nameCameraLibraryButton = By.name("cameraLibraryButton");

    private static final By xpathIPADCameraLibraryFirstFolder = By.xpath(xpathStrMainWindow +
            "/UIAPopover[1]/UIATableView[1]/UIATableCell[@name='Moments']");

    public static final By xpathIPADCameraLibraryFirtImage = By.xpath(xpathStrMainWindow +
            "/UIAPopover[1]/UIACollectionView[1]/UIACollectionCell[1]");

    public static final By xpathIPADCameraLibraryConfirmButton = By.xpath(xpathStrMainWindow +
            "/UIAPopover[1]/UIAButton[@name='OK']");
	
	public CameraRollTabletPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
		

	public void pressSelectFromLibraryButtoniPad() throws Exception {
		getElement(nameCameraLibraryButton).click();
	}

	public void selectImageFromLibraryiPad() throws Exception {
		try {
			clickFirstLibraryFolderiPad();
		} catch (Exception ex) {

		}
		clickFirstImage();
	}

	public void clickFirstLibraryFolderiPad() throws Exception {
		getElement(xpathIPADCameraLibraryFirstFolder).click();
	}

	public void clickFirstImage() throws Exception {
		getElement(xpathIPADCameraLibraryFirtImage).click();
	}

	public void pressConfirmButtoniPad() throws Exception {
		getElement(xpathIPADCameraLibraryConfirmButton).click();
	}
}
