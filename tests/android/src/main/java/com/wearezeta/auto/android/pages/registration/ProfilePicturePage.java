package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ProfilePicturePage extends AndroidPage{

	public static final String idDialogTakePhotoButton = "gtv__camera_control__take_a_picture";
	
	public static final String idConfirmButton = "ttv__confirmation__confirm";
	
	public ProfilePicturePage(Future<ZetaAndroidDriver> lazyDriver)
		throws Exception {
		super(lazyDriver);
	}
	
	public void clickCameraButton() throws Exception {
		WebElement cameraButton = this.getDriver().findElementById(idDialogTakePhotoButton);
		this.getWait().until(
				ExpectedConditions.elementToBeClickable(cameraButton));
		cameraButton.click();
	}
	
	public ContactListPage confirmPicture() throws Exception {
		WebElement confirmImageButton = this.getDriver().findElementById(idConfirmButton);
		confirmImageButton.click();
		return new ContactListPage(this.getLazyDriver());
	}

}
