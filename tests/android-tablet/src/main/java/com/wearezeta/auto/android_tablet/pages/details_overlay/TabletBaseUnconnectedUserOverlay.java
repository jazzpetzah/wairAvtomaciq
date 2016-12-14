package com.wearezeta.auto.android_tablet.pages.details_overlay;

import com.wearezeta.auto.android.pages.details_overlay.BaseUnconnectedUserOverlay;
import com.wearezeta.auto.android.pages.details_overlay.BaseUserDetailsOverlay;
import com.wearezeta.auto.android.pages.details_overlay.ISupportsCommonConnections;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class TabletBaseUnconnectedUserOverlay extends TabletBaseUserDetailsOverlay<BaseUnconnectedUserOverlay> {

    public TabletBaseUnconnectedUserOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
}
