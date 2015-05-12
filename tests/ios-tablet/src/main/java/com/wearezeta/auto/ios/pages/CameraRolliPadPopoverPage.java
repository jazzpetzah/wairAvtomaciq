package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.tablet.locators.IOSTabletLocators;

public class CameraRolliPadPopoverPage {
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathCameraLibraryButton)
	private WebElement cameraLibraryButton;
	
	@FindBy(how = How.XPATH, using = IOSTabletLocators.xpathIPADCameraLibraryFirstFolder)
	private WebElement cameraLibraryFirstFolderiPadPopover;
	
	@FindBy(how = How.XPATH, using = IOSTabletLocators.xpathIPADCameraLibraryFirtImage)
	private WebElement ipadPopoverLibraryFirstPicture;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameConfirmPictureButton)
	private WebElement confirmPictureButton;
	
	public CameraRolliPadPopoverPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super();
	}
		

	public void pressSelectFromLibraryButtoniPad() throws Exception {
//		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
//				By.xpath(IOSLocators.xpathCameraLibraryButton));
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
//		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
//				By.xpath(IOSLocators.xpathCameraLibraryFirstFolder));
		cameraLibraryFirstFolderiPadPopover.click();
	}

	public void clickFirstImage() throws Exception {
//		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
//				By.xpath(IOSLocators.xpathLibraryFirstPicture));
		ipadPopoverLibraryFirstPicture.click();
	}

	public void pressConfirmButtoniPad() throws Exception {
//		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
//				By.name(IOSLocators.nameConfirmPictureButton));
		confirmPictureButton.click();
	}
}
