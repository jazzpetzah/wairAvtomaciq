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

    public void tapOnButton(String buttonName) throws Exception {
        getAndroidTakePicturePage().tapOnButton(buttonName);
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
