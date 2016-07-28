package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.TakePicturePage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletTakePicturePage extends AndroidTabletPage {
    public TabletTakePicturePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private TakePicturePage getAndroidTakePicturePage() throws Exception {
        return this.getAndroidPageInstance(TakePicturePage.class);
    }

    public void confirm() throws Exception {
        getAndroidTakePicturePage().confirm();
    }

    public void cancel() throws Exception {
        getAndroidTakePicturePage().cancel();
    }

    public void takePhoto() throws Exception {
        getAndroidTakePicturePage().takePhoto();
    }

    public void tapChangePhotoButton() throws Exception {
        getAndroidTakePicturePage().tapChangePhotoButton();
    }

    public void openGalleryFromCameraView() throws Exception {
        getAndroidTakePicturePage().openGalleryFromCameraView();
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
        // FIXEME : once AN-4223 fixed or automation find a workaround for tablet rotation issue, should remove this
        //getAndroidTakePicturePage().tapSketchOnImageButton();
        throw new IllegalStateException("Based on PR https://github.com/wearezeta/zclient-android/pull/3380, " +
                "we will skip the image confirm process");
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
}
