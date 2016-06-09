package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.TakePicturePage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletTakePicturePage extends AndroidTabletPage {

    public TabletTakePicturePage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private TakePicturePage getAndroidTakePicturePage() throws Exception {
        return this.getAndroidPageInstance(TakePicturePage.class);
    }

    public void confirm() throws Exception {
        Thread.sleep(1500);
        this.getAndroidTakePicturePage().confirm();
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
    }

    public void openGalleryFromCamera() throws Exception {
        getAndroidTakePicturePage().openGalleryFromCamera();
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
