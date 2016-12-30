package com.wearezeta.auto.android_tablet.pages.details_overlay;

import com.wearezeta.auto.android.pages.details_overlay.BasePendingConnectionOverlay;
import com.wearezeta.auto.android.pages.details_overlay.BaseUserDetailsOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public abstract class TabletBasePendingConnectionOverlay extends TabletBaseUserDetailsOverlay<BasePendingConnectionOverlay> {
    private static final By popoverRootLocator = By.id("fl__pending_connect_request");

    public TabletBasePendingConnectionOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected By getPopoverRootLocator() {
        return popoverRootLocator;
    }
}
