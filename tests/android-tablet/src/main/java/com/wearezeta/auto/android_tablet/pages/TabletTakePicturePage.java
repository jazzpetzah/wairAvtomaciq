package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.TakePicturePage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;
import java.util.concurrent.Future;

public class TabletTakePicturePage extends AndroidTabletPage {

    private boolean isGalleryModeActivated = false;

    public TabletTakePicturePage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private TakePicturePage getAndroidTakePicturePage() throws Exception {
        return this.getAndroidPageInstance(TakePicturePage.class);
    }

    public void confirm() throws Exception {
        Thread.sleep(1500);
        final Optional<WebElement> confirmButton = getElementIfDisplayed(TakePicturePage.xpathConfirmOKButton);
        if (confirmButton.isPresent()) {
            confirmButton.get().click();
        } else {
            // Workaround for unexpected orientation change issue
            final Optional<WebElement> takePhotoButton = getElementIfDisplayed(TakePicturePage.xpathTakePhotoButton);
            if (takePhotoButton.isPresent()) {
                if (isGalleryModeActivated) {
                    openGallery();
                    isGalleryModeActivated = false;
                } else {
                    takePhotoButton.get().click();
                }
                getElement(TakePicturePage.xpathConfirmOKButton,
                        "Picture selection confirmation has not been shown after the timeout", 5).click();
            } else {
                final Optional<WebElement> lensButton = getElementIfDisplayed(TakePicturePage.idChangePhotoBtn);
                if (lensButton.isPresent()) {
                    lensButton.get().click();
                    if (isGalleryModeActivated) {
                        openGallery();
                        isGalleryModeActivated = false;
                    } else {
                        takePhoto();
                    }
                    getElement(TakePicturePage.xpathConfirmOKButton,
                            "Picture selection confirmation has not been shown after the timeout", 5).click();
                }
            }
        }
        Thread.sleep(1500);
        ScreenOrientationHelper.getInstance().fixOrientation(getDriver());
    }

    public void takePhoto() throws Exception {
        getAndroidTakePicturePage().takePhoto();
    }

    public void tapChangePhotoButton() throws Exception {
        getAndroidTakePicturePage().tapChangePhotoButton();
    }

    public void cancel() throws Exception {
        getAndroidTakePicturePage().cancel();
    }

    public void openGallery() throws Exception {
        getAndroidTakePicturePage().openGallery();
        isGalleryModeActivated = true;
    }

    public void openGalleryFromCamera() throws Exception {
        getAndroidTakePicturePage().openGalleryFromCamera();
        isGalleryModeActivated = true;
    }

    public void closeFullScreenImage() throws Exception {
        getAndroidTakePicturePage().closeFullScreenImage();
    }

    public void tapCloseTakePictureViewButton() throws Exception {
        getAndroidTakePicturePage().tapCloseTakePictureViewButton();
    }

    public boolean tapSwitchCameraButton() throws Exception {
        return getAndroidTakePicturePage().tapSwitchCameraButton();
    }

    public void tapSketchOnImageButton() throws Exception {
        getAndroidTakePicturePage().tapSketchOnImageButton();
    }

    public boolean isTakePhotoButtonVisible() throws Exception {
        return getAndroidTakePicturePage().isTakePhotoButtonVisible();
    }

    public boolean isTakePhotoButtonInvisible() throws Exception {
        return getAndroidTakePicturePage().isTakePhotoButtonInvisible();
    }

    public boolean isChangePhotoButtonVisible() throws Exception {
        return getAndroidTakePicturePage().isChangePhotoButtonVisible();
    }

    public boolean isChangePhotoButtonInvisible() throws Exception {
        return getAndroidTakePicturePage().isChangePhotoButtonInvisible();
    }

    public boolean isGalleryButtonVisible() throws Exception {
        return getAndroidTakePicturePage().isGalleryButtonVisible();
    }

    public boolean isGalleryButtonInvisible() throws Exception {
        return getAndroidTakePicturePage().isGalleryButtonInvisible();
    }


}
