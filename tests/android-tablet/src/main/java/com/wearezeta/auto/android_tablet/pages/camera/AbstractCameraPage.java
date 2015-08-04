package com.wearezeta.auto.android_tablet.pages.camera;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AbstractCameraPage extends AndroidTabletPage {
	public static final String xpathConfirmButton = "//*[@id='ttv__confirmation__confirm' and @value='OK']";
	@FindBy(xpath = xpathConfirmButton)
	protected WebElement okConfirmButton;

	public static final String idGalleryButton = "gtv__camera_control__pick_from_gallery";
	@FindBy(id = idGalleryButton)
	private WebElement galleryButton;

	public static final String idTakePhotoButton = "gtv__camera_control__take_a_picture";
	@FindBy(id = idTakePhotoButton)
	protected WebElement takePhotoButton;

	public AbstractCameraPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	protected abstract By getLensButtonLocator();

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				getLensButtonLocator(), 20);
	}

	public void tapLensButton() throws Exception {
		this.getDriver().findElement(getLensButtonLocator()).click();
	}

	public void tapTakePhotoButton() throws Exception {
		// FIXME: Add valid timer for camera view initilization
		Thread.sleep(5000);
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idTakePhotoButton));
		takePhotoButton.click();
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idTakePhotoButton))) {
			takePhotoButton.click();
			assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
					By.id(idTakePhotoButton)) : "The Take Photo button is still visible after the second click";
		}
	}

	public void confirmPictureSelection() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathConfirmButton));
		okConfirmButton.click();
		ScreenOrientationHelper.getInstance().fixOrientation(getDriver());
	}

	public void tapGalleryButton() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idGalleryButton));
		galleryButton.click();
	}

}
