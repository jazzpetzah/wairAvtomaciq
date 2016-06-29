package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.ShareLocationPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletShareLocationPage extends AndroidTabletPage {
    public TabletShareLocationPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private ShareLocationPage getAndroidShareLocationPage() throws Exception {
        return this.getAndroidPageInstance(ShareLocationPage.class);
    }

    public void tapButton(String buttonName) throws Exception {
        getAndroidShareLocationPage().tapButton(buttonName);
    }
}
