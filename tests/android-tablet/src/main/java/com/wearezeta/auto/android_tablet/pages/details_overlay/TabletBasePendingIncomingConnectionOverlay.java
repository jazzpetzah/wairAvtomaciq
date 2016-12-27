package com.wearezeta.auto.android_tablet.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public abstract class TabletBasePendingIncomingConnectionOverlay extends TabletBasePendingConnectionOverlay {
    public TabletBasePendingIncomingConnectionOverlay(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }
}
