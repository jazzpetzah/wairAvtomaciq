package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.TakePicturePage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.ScreenOrientation;

import java.util.concurrent.Future;

public class TabletTakePicturePage extends AndroidTabletPage {
    public TabletTakePicturePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private ScreenOrientationHelper helper = ScreenOrientationHelper.getInstance();

    private TakePicturePage getAndroidTakePicturePage() throws Exception {
        return this.getAndroidPageInstance(TakePicturePage.class);
    }

    public void confirm() throws Exception {
        getAndroidTakePicturePage().confirm();
        helper.fixOrientation(getDriver());
    }

    private void adjustDefaultOrientation() throws Exception {
        if (helper.getOriginalOrientation().isPresent() &&
                helper.getOriginalOrientation().get() == ScreenOrientation.PORTRAIT) {
            AndroidCommonUtils.rotateLandscape();
            Thread.sleep(ScreenOrientationHelper.ROTATION_DELAY_MS);
        }
    }

    public void takePhoto() throws Exception {
        adjustDefaultOrientation();
        getAndroidTakePicturePage().takePhoto();
    }

    public void tapChangePhotoButton() throws Exception {
        adjustDefaultOrientation();
        getAndroidTakePicturePage().tapChangePhotoButton();
    }

    public void cancel() throws Exception {
        getAndroidTakePicturePage().cancel();
        helper.fixOrientation(getDriver());
    }

    public void openGalleryFromCameraView() throws Exception {
        adjustDefaultOrientation();
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
}
