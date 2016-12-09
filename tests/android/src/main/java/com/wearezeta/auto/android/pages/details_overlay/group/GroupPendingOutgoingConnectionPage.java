package com.wearezeta.auto.android.pages.details_overlay.group;

import com.wearezeta.auto.android.pages.details_overlay.BasePendingOutgoingConnectionOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public abstract class GroupPendingOutgoingConnectionPage extends BasePendingOutgoingConnectionOverlay {
    public GroupPendingOutgoingConnectionPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }
}
