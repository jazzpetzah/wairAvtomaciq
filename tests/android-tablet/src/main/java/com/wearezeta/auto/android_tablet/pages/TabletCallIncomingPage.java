package com.wearezeta.auto.android_tablet.pages;

import com.wearezeta.auto.android.pages.CallIncomingPage;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletCallIncomingPage extends AndroidTabletPage {

    public TabletCallIncomingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private CallIncomingPage getPage() throws Exception {
        return this.getAndroidPageInstance(CallIncomingPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return getPage().waitUntilVisible();
    }

    public boolean waitUntilNotVisible() throws Exception {
        return getPage().waitUntilNotVisible();
    }

    public boolean waitUntilNameAppearsOnCallingBarCaption(String name) throws Exception {
        return getPage().waitUntilNameAppearsOnCallingBarCaption(name);
    }

    public void ignoreCall() throws Exception {
        getPage().ignoreCall();
    }

    public void acceptCall() throws Exception {
        getPage().ignoreCall();
    }

}
