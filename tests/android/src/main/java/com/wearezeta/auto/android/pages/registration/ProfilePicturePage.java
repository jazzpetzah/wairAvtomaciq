package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ProfilePicturePage extends AndroidPage {

    public static final String idDialogTakePhotoButton = "gtv__camera_control__take_a_picture";
    @FindBy(id = idDialogTakePhotoButton)
    protected WebElement cameraButton;

    @FindBy(xpath = DialogPage.xpathConfirmOKButton)
    protected WebElement confirmButton;

    public ProfilePicturePage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void clickCameraButton() throws Exception {
        getElement(By.id(idDialogTakePhotoButton), "Camera button is clickable").click();
    }

    public void confirmPicture() throws Exception {
        getElement(By.xpath(DialogPage.xpathConfirmOKButton), "Confirm button is not visible").click();
    }
}
