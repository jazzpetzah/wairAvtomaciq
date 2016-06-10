package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.TakePicturePage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.ScreenOrientation;

import java.util.concurrent.Future;

public class TabletTakePicturePage extends AndroidTabletPage {

    private static final long ROTATION_DELAY_MILLISECONDS = 3000;

    public TabletTakePicturePage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private TakePicturePage getAndroidTakePicturePage() throws Exception {
        return this.getAndroidPageInstance(TakePicturePage.class);
    }

    public void confirm() throws Exception {
        getAndroidTakePicturePage().confirm();
        restoreTestOrientation();
    }

    public void takePhoto() throws Exception {
        getAndroidTakePicturePage().takePhoto();
    }

    public void tapChangePhotoButton() throws Exception {
        setOrientationForTakePicture();
        getAndroidTakePicturePage().tapChangePhotoButton();
    }

    public void cancel() throws Exception {
        getAndroidTakePicturePage().cancel();
        restoreTestOrientation();
    }

    public void openGallery() throws Exception {
        setOrientationForTakePicture();
        getAndroidTakePicturePage().openGallery();
    }

    public void openGalleryFromCameraView() throws Exception {
        setOrientationForTakePicture();
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

    public boolean isGalleryButtonVisible() throws Exception {
        return getAndroidTakePicturePage().isGalleryButtonVisible();
    }

    public boolean isGalleryButtonInvisible() throws Exception {
        return getAndroidTakePicturePage().isGalleryButtonInvisible();
    }

    /**
     * Workaround for rotation issue
     *
     * @throws Exception
     */
    private void setOrientationForTakePicture() throws Exception {
        this.getAndroidTakePicturePage().rotateLandscape();
        Thread.sleep(ROTATION_DELAY_MILLISECONDS);
    }

    /**
     * Workaround for rotation issue
     *
     * @throws Exception
     */
    private void restoreTestOrientation() throws Exception {
        if (ScreenOrientationHelper.getInstance().getOrientation().isPresent()) {
            ScreenOrientation orientation = ScreenOrientationHelper.getInstance().getOrientation().get();
            if (orientation == ScreenOrientation.PORTRAIT) {
                this.getAndroidTakePicturePage().rotatePortrait();
                Thread.sleep(ROTATION_DELAY_MILLISECONDS);
            }
        }
    }


}
