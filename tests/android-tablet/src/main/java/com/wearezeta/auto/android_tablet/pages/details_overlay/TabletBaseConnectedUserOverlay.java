package com.wearezeta.auto.android_tablet.pages.details_overlay;

import com.wearezeta.auto.android.pages.details_overlay.BaseConnectedUserOverlay;
import com.wearezeta.auto.android.pages.details_overlay.ISupportsTabSwitching;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public abstract class TabletBaseConnectedUserOverlay extends TabletBaseUserDetailsOverlay<BaseConnectedUserOverlay>
        implements ISupportsTabSwitching {
    public TabletBaseConnectedUserOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void switchToTab(String tabName) throws Exception {
        getDedicatePage().switchToTab(tabName);
    }
}
