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

	@FindBy(how = How.XPATH, using = IOSLocators.xpathCameraLibraryButton)
	private WebElement cameraLibraryButton;

	@FindBy(how = How.NAME, using = IOSLocators.nameCameraRollCancel)
	private WebElement cameraRollCancel;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathCameraLibraryFirstFolder)
	private WebElement cameraLibraryFirstFolder;

	@FindBy(how = How.XPATH, using = IOSLocators.xpathLibraryFirstPicture)
	private WebElement libraryFirstPicture;

	@FindBy(how = How.NAME, using = IOSLocators.nameConfirmPictureButton)
	private WebElement confirmPictureButton;

	public CameraRollPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void pressSelectFromLibraryButton() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathCameraLibraryButton));
		cameraLibraryButton.click();
	}

	public void selectImageFromLibrary() throws Exception {
		try {
			clickFirstLibraryFolder();
		} catch (NoSuchElementException ex) {

		}

		clickFirstImage();
	}

	public void clickFirstLibraryFolder() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathCameraLibraryFirstFolder));
		cameraLibraryFirstFolder.click();
	}

	public void clickFirstImage() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.xpath(IOSLocators.xpathLibraryFirstPicture));
		libraryFirstPicture.click();
	}

	public void pressConfirmButton() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameConfirmPictureButton));
		confirmPictureButton.click();
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		IOSPage page = null;
		switch (direction) {
		case DOWN: {
			break;
		}
		case UP: {
			break;
		}
		case LEFT: {
			page = new OtherUserPersonalInfoPage(this.getLazyDriver());
			break;
		}
		case RIGHT: {
			page = new ContactListPage(this.getLazyDriver());
			break;
		}
		}
		return page;
	}

}
