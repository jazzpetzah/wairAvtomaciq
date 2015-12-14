package com.wearezeta.auto.android_tablet.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.CallingLockscreenPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class TabletFullScreenCallingOverlayPage extends AndroidTabletPage {

    public TabletFullScreenCallingOverlayPage(
            Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private CallingLockscreenPage getCallingLockscreenPage() throws Exception {
        return this.getAndroidPageInstance(CallingLockscreenPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        try {
            return getCallingLockscreenPage().isVisible();
        } finally {
            ScreenOrientationHelper.getInstance().fixOrientation(getDriver());
        }
    }

    private static final int ACCEPT_CALL_CONTROLS_Y = 95; // percent

    public void acceptCall() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(),
                getDriver().findElement(By.id(CallingLockscreenPage.idMainContent)),
                1500, 50, ACCEPT_CALL_CONTROLS_Y, 70, ACCEPT_CALL_CONTROLS_Y);
    }
}
