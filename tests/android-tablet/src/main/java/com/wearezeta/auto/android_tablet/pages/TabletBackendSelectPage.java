package com.wearezeta.auto.android_tablet.pages;


import com.wearezeta.auto.android.pages.registration.BackendSelectPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletBackendSelectPage extends AndroidTabletPage {

    public TabletBackendSelectPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private BackendSelectPage getPage() throws Exception {
        return this.getAndroidPageInstance(BackendSelectPage.class);
    }

    public void waitForInitialScreen() throws Exception {
        getPage().waitForInitialScreen();
    }
}