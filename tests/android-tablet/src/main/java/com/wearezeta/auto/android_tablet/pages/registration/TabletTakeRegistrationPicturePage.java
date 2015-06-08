package com.wearezeta.auto.android_tablet.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletTakeRegistrationPicturePage extends AndroidTabletPage {
	public static final String idCameraButton = "gtv__sign_up__lens_icon";
	@FindBy(id = idCameraButton)
	protected WebElement cameraButton;

	public static final String idTakePictureButton = "gtv__camera_control__take_a_picture";
	@FindBy(id = idTakePictureButton)
	protected WebElement takePictureButton;

	public static final String xpathConfirmButton = "//*[@id='ttv__confirmation__confirm' and @value='OK']";
	@FindBy(xpath = xpathConfirmButton)
	protected WebElement okConfirmButton;

	public TabletTakeRegistrationPicturePage(
			Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCameraButton));
	}

	public void tapCameraButton() {
		cameraButton.click();
	}

	public void tapTakePictureButton() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idTakePictureButton));
		takePictureButton.click();
	}

	public void tapOKButton() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathConfirmButton));
		okConfirmButton.click();
	}

}
