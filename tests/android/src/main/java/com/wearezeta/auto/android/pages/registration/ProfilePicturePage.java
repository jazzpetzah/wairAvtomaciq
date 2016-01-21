package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ProfilePicturePage extends AndroidPage {

    private static final By idDialogTakePhotoButton = By.id("gtv__camera_control__take_a_picture");

    public ProfilePicturePage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void clickCameraButton() throws Exception {
        getElement(idDialogTakePhotoButton, "Camera button is clickable").click();
    }

    public void confirmPicture() throws Exception {
        getElement(DialogPage.xpathConfirmOKButton, "Confirm button is not visible").click();
    }
}
