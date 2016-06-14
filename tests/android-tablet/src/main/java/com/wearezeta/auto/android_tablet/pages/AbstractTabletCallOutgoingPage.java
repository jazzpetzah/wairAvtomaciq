package com.wearezeta.auto.android_tablet.pages;


import com.wearezeta.auto.android.pages.AbstractCallOutgoingPage;
import com.wearezeta.auto.android.pages.CallOutgoingAudioPage;
import com.wearezeta.auto.android.pages.CallOutgoingVideoPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.util.concurrent.Future;

public abstract class AbstractTabletCallOutgoingPage extends AndroidTabletPage {

    public AbstractTabletCallOutgoingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private <T extends AbstractCallOutgoingPage> T getPage(Class<T> cls) throws Exception {
        return this.getAndroidPageInstance(cls);
    }

    public void hangup() throws Exception {
        getPage(getAndroidPageClass()).hangup();
    }

    public void toggleMute() throws Exception {
        getPage(getAndroidPageClass()).toggleMute();
    }

    public boolean waitUntilVisible() throws Exception {
        return getPage(getAndroidPageClass()).waitUntilVisible();
    }

    public boolean waitUntilInvisible() throws Exception {
        return getPage(getAndroidPageClass()).waitUntilInvisible();
    }

    protected abstract Class getAndroidPageClass();
}
