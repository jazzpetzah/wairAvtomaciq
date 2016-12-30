package com.wearezeta.auto.android_tablet.pages.details_overlay;


import com.wearezeta.auto.android.pages.details_overlay.BaseBlockedUserOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public abstract class TabletBaseBlockedUserOverlay extends TabletBaseUserDetailsOverlay<BaseBlockedUserOverlay> {
    public TabletBaseBlockedUserOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
}
