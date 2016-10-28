package com.wearezeta.auto.android_tablet.pages;


import com.wearezeta.auto.android.pages.cursor.CameraOverlayPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletExtendedCursorCameraOverlayPage extends AndroidTabletPage {
    public TabletExtendedCursorCameraOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private CameraOverlayPage getExtendedCursorCameraOverlayPage() throws Exception {
        return this.getAndroidPageInstance(CameraOverlayPage.class);
    }

    public void tapOnButton(String buttonName) throws Exception {
        getExtendedCursorCameraOverlayPage().tapOnButton(buttonName);
    }

}
