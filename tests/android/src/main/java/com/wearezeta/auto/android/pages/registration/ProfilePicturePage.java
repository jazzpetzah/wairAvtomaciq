package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ProfilePicturePage extends AndroidPage{

	public static final String idDialogTakePhotoButton = "gtv__camera_control__take_a_picture";
	@FindBy(id = idDialogTakePhotoButton)
	protected WebElement cameraButton;
	
	public static final String xPathConfirmButton = "//*[@id='ttv__confirmation__confirm' and @value='OK']";
	@FindBy(xpath = xPathConfirmButton)
	protected WebElement confirmButton;
	
	public ProfilePicturePage(Future<ZetaAndroidDriver> lazyDriver)
		throws Exception {
		super(lazyDriver);
	}
	
	public void clickCameraButton() throws Exception {
		Assert.assertTrue(DriverUtils.waitUntilElementClickable(this.getDriver(), cameraButton));
		cameraButton.click();
	}
	
	public ContactListPage confirmPicture() throws Exception {
		confirmButton.click();
		return new ContactListPage(this.getLazyDriver());
	}

}
