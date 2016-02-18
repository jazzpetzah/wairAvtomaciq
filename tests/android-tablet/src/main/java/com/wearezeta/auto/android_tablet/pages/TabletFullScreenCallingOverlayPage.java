package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.CallIncomingPage;
import java.util.concurrent.Future;

import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletFullScreenCallingOverlayPage extends AndroidTabletPage {

    public TabletFullScreenCallingOverlayPage(
            Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private CallIncomingPage getPage() throws Exception {
        return this.getAndroidPageInstance(CallIncomingPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        try {
            return getPage().waitUntilVisible();
        } finally {
            ScreenOrientationHelper.getInstance().fixOrientation(getDriver());
        }
    }

    private static final int ACCEPT_CALL_CONTROLS_Y = 95; // percent

    public void acceptCall() throws Exception {
        //TODO
//        DriverUtils.swipeElementPointToPoint(getDriver(),
//                getDriver().findElement(CallingLockscreenPage.idMainContent),
//                1500, 50, ACCEPT_CALL_CONTROLS_Y, 70, ACCEPT_CALL_CONTROLS_Y);
    }
}
