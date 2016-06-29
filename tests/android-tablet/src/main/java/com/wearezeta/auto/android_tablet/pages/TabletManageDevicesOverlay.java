package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.ManageDevicesOverlay;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class TabletManageDevicesOverlay extends AndroidTabletPage {

    public TabletManageDevicesOverlay(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private ManageDevicesOverlay getPage() throws Exception {
        return this.getAndroidPageInstance(ManageDevicesOverlay.class);
    }

    public boolean isVisible() throws Exception {
        return getPage().isVisible();
    }

    public boolean isInvisible() throws Exception {
        return getPage().isInvisible();
    }

    public void tapManageDevicesButton() throws Exception {
        getPage().tapManageDevicesButton();
    }
}
