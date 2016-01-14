package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.FirstTimeOverlay;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletFirstTimeOverlay extends AndroidTabletPage {

    public TabletFirstTimeOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private FirstTimeOverlay getFirstTimeOverlay() throws Exception {
        return this.getAndroidPageInstance(FirstTimeOverlay.class);
    }

    public boolean isVisible() throws Exception {
        return getFirstTimeOverlay().isVisible();
    }

    public void tapGotItButton() throws Exception {
        getFirstTimeOverlay().tapGotItButton();
    }
}
