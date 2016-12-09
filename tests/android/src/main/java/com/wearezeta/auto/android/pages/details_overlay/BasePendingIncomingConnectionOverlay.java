package com.wearezeta.auto.android.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public abstract class BasePendingIncomingConnectionOverlay extends BasePendingConnectionOverlay {
    public BasePendingIncomingConnectionOverlay(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }
}
