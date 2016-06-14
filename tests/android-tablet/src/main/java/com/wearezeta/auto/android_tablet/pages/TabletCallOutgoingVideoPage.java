package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.CallOutgoingVideoPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public class TabletCallOutgoingVideoPage extends AbstractTabletCallOutgoingPage {

    public TabletCallOutgoingVideoPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private CallOutgoingVideoPage getVideoAndroidPage() throws Exception {
        return this.getAndroidPageInstance(CallOutgoingVideoPage.class);
    }

    public void toggleVideo() throws Exception {
        getVideoAndroidPage().toggleVideo();
    }

    protected Class getAndroidPageClass() {
        return CallOutgoingVideoPage.class;
    }
}
