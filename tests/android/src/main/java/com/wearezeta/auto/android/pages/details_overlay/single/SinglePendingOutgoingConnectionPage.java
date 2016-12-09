package com.wearezeta.auto.android.pages.details_overlay.single;

import com.wearezeta.auto.android.pages.details_overlay.BasePendingOutgoingConnectionOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class SinglePendingOutgoingConnectionPage extends BasePendingOutgoingConnectionOverlay {
    public SinglePendingOutgoingConnectionPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }
}
