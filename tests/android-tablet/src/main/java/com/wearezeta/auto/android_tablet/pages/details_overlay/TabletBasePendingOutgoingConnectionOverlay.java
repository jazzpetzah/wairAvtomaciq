package com.wearezeta.auto.android_tablet.pages.details_overlay;


import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public abstract class TabletBasePendingOutgoingConnectionOverlay extends TabletBasePendingConnectionOverlay {
    public TabletBasePendingOutgoingConnectionOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
}
