package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;

public class TakePicturePage extends AndroidPage {
    private static final By xpathTakePhotoButton = By
            .xpath("//*[@id='gtv__camera_control__take_a_picture' and @shown='true']");

    private static final By idCloseTakePictureViewButton = By.id("gtv__camera_control__back_to_change_image");

    private static final By idChangePhotoBtn = By.id("gtv__camera_control__change_image_source");

    private static final By idGalleryBtn = By.id("gtv__camera_control__pick_from_gallery");

    private static final By idGalleryCameraBtn = By.id("gtv__camera_control__pick_from_gallery_in_camera");

    private static final By idSwitchCameraButton = By.id("gtv__camera__top_control__back_camera");

    private static final By xpathConfirmOKButton = By.xpath("//*[@id='ttv__confirmation__confirm' and @value='OK']");

    private static final By xpathCancelButton = By.xpath("//*[@id='ttv__confirmation__cancel' and @value='CANCEL']");

    private static final By idSketchImagePaintButton = By.id("gtv__sketch_image_paint_button");

    public TakePicturePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapCloseTakePictureViewButton() throws Exception {
        getElement(idCloseTakePictureViewButton).click();
    }

    public void tapChangePhotoButton() throws Exception {
        getElement(idChangePhotoBtn).click();
    }

    public boolean isTakePhotoButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathTakePhotoButton);
    }

    public boolean isTakePhotoButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathTakePhotoButton);
    }

    public boolean isChangePhotoButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idChangePhotoBtn);
    }

    public boolean isChangePhotoButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idChangePhotoBtn);
    }

    public boolean isGalleryButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idGalleryBtn);
    }

    public boolean isGalleryButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idGalleryBtn);
    }

    /**
     * @return false if Take Photo button is not visible after Switch Camera button is clicked
     * @throws Exception
     */
    public boolean tapSwitchCameraButton() throws Exception {
        getElement(idSwitchCameraButton).click();
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathTakePhotoButton);
    }

    public void openGallery() throws Exception {
        getElement(idGalleryBtn, "Gallery button is still not visible").click();
    }

    public void openGalleryFromCamera() throws Exception {
        getElement(idGalleryCameraBtn, "Gallery within camera is still not visible").click();
    }

    public void closeFullScreenImage() throws Exception {
        // Sometimes X button is opened automatically after some timeout
        final int MAX_TRIES = 4;
        int ntry = 1;
        while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCloseImageBtn, 4) && ntry <= MAX_TRIES) {
            this.tapOnCenterOfScreen();
            ntry++;
        }
        getElement(idCloseImageBtn).click();
    }

    public void takePhoto() throws Exception {
        final WebElement btn = getElement(xpathTakePhotoButton, "Take Photo button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), btn)) {
            throw new IllegalStateException("Take Photo button is not clickable");
        }
        btn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathTakePhotoButton)) {
            throw new IllegalStateException("Take Photo button is still visible after being clicked");
        }
    }

    public void confirm() throws Exception {
        final WebElement okBtn = getElement(xpathConfirmOKButton, "OK button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), okBtn)) {
            throw new IllegalStateException("OK button is not clickable");
        }
        okBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathConfirmOKButton)) {
            okBtn.click();
        }
    }

    public void cancel() throws Exception {
        final WebElement cancelBtn = getElement(xpathCancelButton, "Cancel button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), cancelBtn)) {
            throw new IllegalStateException("Cancel button is not clickable");
        }
        cancelBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathCancelButton)) {
            cancelBtn.click();
        }
    }

    public void tapSketchOnImageButton() throws Exception {
        getElement(idSketchImagePaintButton, "Draw sketch on image button is not visible").click();
    }
}
